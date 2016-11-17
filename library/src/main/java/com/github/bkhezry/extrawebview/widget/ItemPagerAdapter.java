package com.github.bkhezry.extrawebview.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.github.bkhezry.extrawebview.AppUtils;
import com.github.bkhezry.extrawebview.LazyLoadFragment;
import com.github.bkhezry.extrawebview.R;
import com.github.bkhezry.extrawebview.Scrollable;
import com.github.bkhezry.extrawebview.WebFragment;
import com.github.bkhezry.extrawebview.data.DataModel;



public class ItemPagerAdapter extends FragmentStatePagerAdapter {
    private final Fragment[] mFragments = new Fragment[1];
    private final Context mContext;
    private final DataModel mItem;
    private final int mDefaultItem;
    private CharSequence pagerTitle;
    private String fontPath;

    public ItemPagerAdapter(Context context, FragmentManager fm, @NonNull Builder builder) {
        super(fm);
        mContext = context;
        mItem = builder.item;
        mDefaultItem = 0;
        pagerTitle = builder.item.getPageTitle();
        fontPath = builder.fontPath;


    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments[position] != null) {
            return mFragments[position];
        }
        String fragmentName;
        Bundle args = new Bundle();
        args.putBoolean(LazyLoadFragment.EXTRA_EAGER_LOAD, mDefaultItem == position);
        args.putSerializable(WebFragment.EXTRA_ITEM, mItem);
        fragmentName = WebFragment.class.getName();
        return Fragment.instantiate(mContext, fragmentName, args);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mFragments[position] = (Fragment) super.instantiateItem(container, position);
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitle;
    }

    public void bind(final ViewPager viewPager, PagerSlidingTabStrip tabLayout, final FloatingActionButton genericFab) {
        viewPager.setPageMargin(viewPager.getResources().getDimensionPixelOffset(R.dimen.divider));
        viewPager.setPageMarginDrawable(R.color.blackT12);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(this);
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                Fragment fragment = getItem(viewPager.getCurrentItem());
                if (fragment != null) {
                    ((Scrollable) fragment).scrollToTop();
                }
            }
        });
        viewPager.setCurrentItem(mDefaultItem);
        toggleFabs(false, genericFab);

    }

    private void toggleFabs(boolean isComments,
                            FloatingActionButton genericFab) {
        AppUtils.toggleFab(genericFab, true);
        AppUtils.toggleFabAction(genericFab, isComments);
    }

    public static class Builder {
        DataModel item;
        String fontPath;

        public Builder setFontPath(@NonNull String fontPath) {
            this.fontPath = fontPath;
            return this;
        }

        public Builder setItem(@NonNull DataModel item) {
            this.item = item;
            return this;
        }
    }
}
