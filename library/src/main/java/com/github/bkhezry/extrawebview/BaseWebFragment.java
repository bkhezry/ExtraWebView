package com.github.bkhezry.extrawebview;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.github.bkhezry.extrawebview.widget.CacheableWebView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

abstract class BaseWebFragment extends LazyLoadFragment
        implements Scrollable, KeyDelegate.BackInterceptor {

    static final String ACTION_FULLSCREEN = BaseWebFragment.class.getName() + ".ACTION_FULLSCREEN";
    static final String EXTRA_FULLSCREEN = BaseWebFragment.class.getName() + ".EXTRA_FULLSCREEN";
    private static final int DEFAULT_PROGRESS = 15;
    private WebView mWebView;
    private NestedScrollView mScrollView;
    private boolean mExternalRequired = false;
    private KeyDelegate.NestedScrollViewHelper mScrollableHelper;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setFullscreen(intent.getBooleanExtra(BaseWebFragment.EXTRA_FULLSCREEN, false));
        }
    };
    private ViewGroup mFullscreenView;
    private ViewGroup mScrollViewContent;
    private ImageButton mButtonRefresh;
    private ViewSwitcher mControls;
    private EditText mEditText;
    private View mButtonNext;
    protected ProgressBar mProgressBar;
    private boolean mFullscreen;
    private String mUrl;
    private AppUtils.SystemUiHelper mSystemUiHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver,
                new IntentFilter(ACTION_FULLSCREEN));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = getLayoutInflater(savedInstanceState)
                .inflate(R.layout.fragment_web, container, false);
        mFullscreenView = (ViewGroup) view.findViewById(R.id.fullscreen);
        mScrollViewContent = (ViewGroup) view.findViewById(R.id.scroll_view_content);
        mScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        mControls = (ViewSwitcher) view.findViewById(R.id.control_switcher);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mButtonRefresh = (ImageButton) view.findViewById(R.id.button_refresh);
        mButtonNext = view.findViewById(R.id.button_next);
        mButtonNext.setEnabled(false);
        mEditText = (EditText) view.findViewById(R.id.edittext);
        setUpWebControls(view);
        setUpWebView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mScrollableHelper = new KeyDelegate.NestedScrollViewHelper(mScrollView);
        mSystemUiHelper = new AppUtils.SystemUiHelper(getActivity().getWindow());
        mSystemUiHelper.setEnabled(!getResources().getBoolean(R.bool.multi_pane));
        if (mFullscreen) {
            setFullscreen(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onResume();
        }
        mWebView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause();
        }
        mWebView.pauseTimers();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebView.destroy();
    }

    @Override
    public void scrollToTop() {
        if (mFullscreen) {
            mWebView.pageUp(true);
        } else {
            mScrollableHelper.scrollToTop();
        }
    }

    @Override
    public boolean scrollToNext() {
        if (mFullscreen) {
            mWebView.pageDown(false);
            return true;
        } else {
            return mScrollableHelper.scrollToNext();
        }
    }

    @Override
    public boolean scrollToPrevious() {
        if (mFullscreen) {
            mWebView.pageUp(false);
            return true;
        } else {
            return mScrollableHelper.scrollToPrevious();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }


    final void loadUrl(String url) {
        mUrl = url;
        setWebSettings(true);
        mWebView.loadUrl(url);
    }


    private void setUpWebControls(View view) {
        view.findViewById(R.id.toolbar_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToTop();
            }
        });
        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.goBack();
            }
        });
        view.findViewById(R.id.button_forward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.goForward();
            }
        });
        view.findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSystemUiHelper.setFullscreen(true);
                reset();
                mControls.showNext();
            }
        });
        view.findViewById(R.id.button_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.requestFocus();
                toggleSoftKeyboard(true);
                mControls.showNext();
            }
        });
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.getProgress() == 100) {
                    mWebView.loadUrl("about:blank");
                    load();
                } else {
                    mWebView.stopLoading();
                }
            }
        });
        view.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                        new Intent(BaseWebFragment.ACTION_FULLSCREEN)
                                .putExtra(EXTRA_FULLSCREEN, false));
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.findNext(true);
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                findInPage();
                return true;
            }
        });
    }

    private void setUpWebView(final View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mProgressBar.setProgress(DEFAULT_PROGRESS);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new CacheableWebView.ArchiveClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setVisibility(VISIBLE);
                if (newProgress > 20)
                    mProgressBar.setProgress(newProgress);
                if (!TextUtils.isEmpty(mUrl)) {
                    mWebView.setBackgroundColor(Color.WHITE);
                }
                if (newProgress == 100) {
                    mProgressBar.setVisibility(GONE);
                    mWebView.setVisibility(mExternalRequired ? GONE : VISIBLE);
                }
                mButtonRefresh.setImageResource(newProgress == 100 ?
                        R.drawable.ic_refresh_white_24dp : R.drawable.ic_clear_white_24dp);
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (getActivity() == null) {
                    return;
                }
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
                    return;
                }
                mExternalRequired = true;
                mWebView.setVisibility(GONE);
                view.findViewById(R.id.empty).setVisibility(VISIBLE);
            }
        });
        AppUtils.toggleWebViewZoom(mWebView.getSettings(), false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebSettings(boolean isRemote) {
        mWebView.getSettings().setLoadWithOverviewMode(isRemote);
        mWebView.getSettings().setUseWideViewPort(isRemote);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    private void setFullscreen(boolean isFullscreen) {
        if (!getUserVisibleHint()) {
            return;
        }
        mFullscreen = isFullscreen;
        mControls.setVisibility(isFullscreen ? VISIBLE : View.GONE);
        AppUtils.toggleWebViewZoom(mWebView.getSettings(), isFullscreen);
        if (isFullscreen) {
            mScrollView.removeView(mScrollViewContent);
            mFullscreenView.addView(mScrollViewContent);
        } else {
            reset();
            mWebView.pageUp(true);
            mFullscreenView.removeView(mScrollViewContent);
            mScrollView.addView(mScrollViewContent);
        }
    }


    private void reset() {
        mEditText.setText(null);
        mButtonNext.setEnabled(false);
        toggleSoftKeyboard(false);
        mWebView.clearMatches();
    }

    private void findInPage() {
        String query = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.setFindListener(new WebView.FindListener() {
                @Override
                public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
                    if (isDoneCounting) {
                        handleFindResults(numberOfMatches);
                    }
                }
            });
            mWebView.findAllAsync(query);
        } else {
            //noinspection deprecation
            handleFindResults(mWebView.findAll(query));
        }
    }

    private void handleFindResults(int numberOfMatches) {
        mButtonNext.setEnabled(numberOfMatches > 0);
        if (numberOfMatches == 0) {
            Toast.makeText(getContext(), R.string.no_matches, Toast.LENGTH_SHORT).show();
        } else {
            toggleSoftKeyboard(false);
        }
    }

    private void toggleSoftKeyboard(boolean visible) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (visible) {
            imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }
}
