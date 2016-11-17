package com.github.bkhezry.extrawebview.widget;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Build;
import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;


public class CacheableWebView extends WebView {
    private ArchiveClient mArchiveClient = new ArchiveClient();

    public CacheableWebView(Context context) {
        this(context, null);
    }

    public CacheableWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CacheableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mArchiveClient.lastProgress = 0;
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mArchiveClient.lastProgress = 0;
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        if (!(client instanceof ArchiveClient)) {
            throw new IllegalArgumentException("client should be an instance of " +
                    ArchiveClient.class.getName());
        }
        mArchiveClient = (ArchiveClient) client;
        super.setWebChromeClient(mArchiveClient);
    }

    private void init() {
        enableCache();
        setLoadSettings();
        setWebViewClient(new WebViewClient());
        setWebChromeClient(mArchiveClient);
    }

    private void enableCache() {
        WebSettings webSettings = getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCachePath(getContext().getApplicationContext()
                .getCacheDir().getAbsolutePath());
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void setLoadSettings() {
        WebSettings webSettings = getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
    }

    public static class ArchiveClient extends WebChromeClient {
        int lastProgress = 0;
        String cacheFileName = null;

        @CallSuper
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (view.getSettings().getCacheMode() == WebSettings.LOAD_CACHE_ONLY) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                    cacheFileName != null && lastProgress != 100 && newProgress == 100) {
                lastProgress = newProgress;
            }
        }

    }
}
