/*
 * Copyright (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.bkhezry.extrawebview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.github.bkhezry.extrawebview.data.DataModel;
import com.github.bkhezry.extrawebview.data.IntentServiceResult;
import com.github.bkhezry.extrawebview.data.ViewOption;
import com.github.bkhezry.extrawebview.widget.ItemPagerAdapter;
import com.github.bkhezry.extrawebview.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ItemActivity extends InjectableActivity {

    public static final String EXTRA_ITEM = ItemActivity.class.getName() + ".EXTRA_ITEM";
    public static final String EXTRA_VIEW_OPTION = ItemActivity.class.getName() + ":EXTRA_VIEW_OPTION";
    public static final String TYPE_ACTION_BOOKMARK = "BOOKMARK";
    public static final String TYPE_ACTION_VOTE = "VOTE";
    private ImageView mBookmark;
    @Inject
    CustomTabsDelegate mCustomTabsDelegate;
    @Inject
    KeyDelegate mKeyDelegate;
    private PagerSlidingTabStrip mTabLayout;
    private AppBarLayout mAppBar;
    private ImageButton mVoteButton;
    private FloatingActionButton mReplyButton;
    private ItemPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private boolean mFullscreen;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mFullscreen = intent.getBooleanExtra(BaseWebFragment.EXTRA_FULLSCREEN, false);
            setFullscreen();
        }
    };
    private final Preferences.Observable mPreferenceObservable = new Preferences.Observable();
    private AppUtils.SystemUiHelper mSystemUiHelper;
    private Toolbar toolbar;
    private TextView titleTextView;
    private TextView sourceTextView;
    private TextView byTextView;
    private TextView timeTextView;
    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        dataModel = (DataModel) intent.getSerializableExtra(EXTRA_ITEM);
        ViewOption viewOption = (ViewOption) intent.getSerializableExtra(EXTRA_VIEW_OPTION);
        Preferences.Theme.apply(this, viewOption.getThemeName());
        setContentView(R.layout.activity_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_HOME_AS_UP);
        mSystemUiHelper = new AppUtils.SystemUiHelper(getWindow());
        mReplyButton = (FloatingActionButton) findViewById(R.id.reply_button);
        mVoteButton = (ImageButton) findViewById(R.id.vote_button);
        mBookmark = (ImageView) findViewById(R.id.bookmarked);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (PagerSlidingTabStrip) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        AppUtils.toggleFab(mReplyButton, false);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(BaseWebFragment.ACTION_FULLSCREEN));
        if (dataModel != null && viewOption != null) {
            bindData(dataModel, viewOption);
        }
        if (!AppUtils.hasConnection(this)) {
            Toast.makeText(ItemActivity.this, "Offline", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabsDelegate.bindCustomTabsService(this);
        mKeyDelegate.attach(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_share).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_external) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataModel.getUrl())));
            return true;
        }
        if (item.getItemId() == R.id.menu_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, dataModel.getUrl());
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabsDelegate.unbindCustomTabsService(this);
        mKeyDelegate.detach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        mPreferenceObservable.unsubscribe(this);
    }

    @Override
    public void onBackPressed() {
        if (!mFullscreen) {
            super.onBackPressed();
        } else {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(
                    WebFragment.ACTION_FULLSCREEN).putExtra(WebFragment.EXTRA_FULLSCREEN, false));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mKeyDelegate.setScrollable(getCurrent(Scrollable.class), mAppBar);
        mKeyDelegate.setBackInterceptor(getCurrent(KeyDelegate.BackInterceptor.class));
        return mKeyDelegate.onKeyDown(keyCode, event) ||
                super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mKeyDelegate.onKeyUp(keyCode, event) ||
                super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mKeyDelegate.onKeyLongPress(keyCode, event) ||
                super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mSystemUiHelper.setFullscreen(hasFocus && mFullscreen);
    }


    private void setFullscreen() {
        mSystemUiHelper.setFullscreen(mFullscreen);
        mAppBar.setExpanded(!mFullscreen, true);
        mKeyDelegate.setAppBarEnabled(!mFullscreen);
        mViewPager.setSwipeEnabled(!mFullscreen);
        AppUtils.toggleFab(mReplyButton, !mFullscreen);
    }

    @SuppressWarnings("ConstantConditions")
    private void bindData(@Nullable final DataModel dataModel, ViewOption viewOption) {
        Typeface typeface = null;
        if (viewOption.getDefaultFontPath() != null) {
            typeface = Typeface.createFromAsset(getAssets(), viewOption.getDefaultFontPath());
        }
        mCustomTabsDelegate.mayLaunchUrl(Uri.parse(dataModel.getUrl()), null, null);
        if (viewOption.isBookMarkIcon()) {
            decorateFavorite(dataModel.isFavorite());
            mBookmark.setVisibility(View.VISIBLE);
            mBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataModel.setFavorite(!dataModel.isFavorite());
                    decorateFavorite(dataModel.isFavorite());
                    IntentServiceResult intentServiceResult = new IntentServiceResult();
                    intentServiceResult.setId(dataModel.getId());
                    intentServiceResult.setTypeEvent(TYPE_ACTION_BOOKMARK);
                    intentServiceResult.setChecked(dataModel.isFavorite());
                    EventBus.getDefault().post(intentServiceResult);

                }
            });
        } else {
            mBookmark.setVisibility(View.INVISIBLE);
        }
        if (viewOption.isVoteIcon()) {
            mVoteButton.setVisibility(View.VISIBLE);
            mVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataModel.setVoted(!dataModel.isVoted());
                    IntentServiceResult intentServiceResult = new IntentServiceResult();
                    intentServiceResult.setId(dataModel.getId());
                    intentServiceResult.setTypeEvent(TYPE_ACTION_VOTE);
                    intentServiceResult.setChecked(dataModel.isVoted());
                    EventBus.getDefault().post(intentServiceResult);
                }
            });
        } else {
            mVoteButton.setVisibility(View.INVISIBLE);
        }
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(dataModel.getDisplayedTitle());
        sourceTextView = (TextView) findViewById(R.id.source);
        if (!TextUtils.isEmpty(dataModel.getSource())) {
            sourceTextView.setText(dataModel.getSource());
            sourceTextView.setVisibility(View.VISIBLE);
        }
        byTextView = (TextView) findViewById(R.id.by);
        byTextView.setVisibility(View.VISIBLE);
        Long tsLong = System.currentTimeMillis() / 1000;
        byTextView.setText(AppUtils.compareTwoTimeStamps(tsLong, dataModel.getTime()) + " - " + dataModel.getBy());
        byTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mAdapter = new ItemPagerAdapter(this, getSupportFragmentManager(),
                new ItemPagerAdapter.Builder()
                        .setItem(dataModel)
                        .setFontPath(viewOption.getDefaultFontPath()));
        if (typeface != null) {
            mTabLayout.setTypeface(typeface, R.style.AppTheme);
        }
        mTabLayout.setAllCaps(false);
        mAdapter.bind(mViewPager, mTabLayout, mReplyButton);
        if (mFullscreen) {
            setFullscreen();
        }
        FrameLayout headerCardView = (FrameLayout) findViewById(R.id.header_card_view);
        headerCardView.setClickable(false);
    }

    private void decorateFavorite(boolean isFavorite) {
        mBookmark.setImageResource(isFavorite ?
                R.drawable.ic_bookmark_white_24dp : R.drawable.ic_bookmark_border_white_24dp);
    }

    private <T> T getCurrent(Class<T> clazz) {
        if (mAdapter == null) {
            return null;
        }
        Fragment currentItem = mAdapter.getItem(mViewPager.getCurrentItem());
        if (clazz.isInstance(currentItem)) {
            //noinspection unchecked
            return (T) currentItem;
        } else {
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
