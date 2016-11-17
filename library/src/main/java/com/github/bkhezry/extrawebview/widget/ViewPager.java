package com.github.bkhezry.extrawebview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPager extends android.support.v4.view.ViewPager {
    private boolean mSwipeEnabled = true;

    public ViewPager(Context context) {
        this(context, null);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mSwipeEnabled && super.onInterceptTouchEvent(ev);
    }

    public void setSwipeEnabled(boolean enabled) {
        mSwipeEnabled = enabled;
    }
}
