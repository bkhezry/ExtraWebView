package com.github.bkhezry.extrawebview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.github.bkhezry.extrawebview.AppUtils;
import com.github.bkhezry.extrawebview.R;


public class IconButton extends ImageButton {
    private static final int[][] STATES = new int[][]{
            new int[]{android.R.attr.state_enabled},
            new int[]{-android.R.attr.state_enabled}
    };
    private ColorStateList mColorStateList;
    private final boolean mTinted;

    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(AppUtils.getThemedResId(context, R.attr.selectableItemBackgroundBorderless));
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconButton, 0, 0);
        int colorDisabled = ContextCompat.getColor(context,
                AppUtils.getThemedResId(context, android.R.attr.textColorSecondary));
        int colorDefault = ContextCompat.getColor(context,
                AppUtils.getThemedResId(context, android.R.attr.textColorPrimary));
        int colorEnabled = ta.getColor(R.styleable.IconButton_tintChanged, colorDefault);
        mColorStateList = new ColorStateList(STATES, new int[]{colorEnabled, colorDisabled});
        mTinted = ta.hasValue(R.styleable.IconButton_tintChanged);
        if (getSuggestedMinimumWidth() == 0) {
            setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.icon_button_width));
        }
        setScaleType(ScaleType.CENTER);
        setImageDrawable(getDrawable());
        ta.recycle();
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(tint(drawable));
    }

    private Drawable tint(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable tintDrawable = DrawableCompat.wrap(mTinted ? drawable.mutate() : drawable);
        DrawableCompat.setTintList(tintDrawable, mColorStateList);
        return tintDrawable;
    }
}
