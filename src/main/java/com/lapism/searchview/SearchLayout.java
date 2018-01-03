package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

@RestrictTo(LIBRARY_GROUP)
public abstract class SearchLayout extends FrameLayout {

    @Search.Logo
    protected int mLogo;
    @Search.Shape
    protected int mShape;
    @Search.Theme
    protected int mTheme;

    protected int mTextStyle = Typeface.NORMAL;
    protected Typeface mTextFont = Typeface.DEFAULT;

    protected Context mContext;
    protected CardView mCardView;
    protected ImageView mImageViewLogo;
    protected ImageView mImageViewMic;
    protected ImageView mImageViewClear;
    protected ImageView mImageViewMenu;
    protected SearchArrowDrawable mSearchArrowDrawable;

    protected Search.OnMicClickListener mOnMicClickListener;
    protected Search.OnMenuClickListener mOnMenuClickListener;

    // ---------------------------------------------------------------------------------------------
    public SearchLayout(@NonNull Context context) {
        super(context);
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    public abstract void setHintColor(@ColorInt int color);

    public abstract void setText(@StringRes int text);

    public abstract void setText(CharSequence text);

    public abstract void setTextColor(@ColorInt int color);

    public abstract void setTextStyle(int style);

    public abstract void setTextFont(Typeface font);

    @Search.Layout
    public abstract int getLayout();

    // ---------------------------------------------------------------------------------------------
    @Search.Logo
    public int getLogo() {
        return mLogo;
    }

    public void setLogo(@Search.Logo int logo) {
        mLogo = logo;

        switch (mLogo) {
            case Search.Logo.G:
                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_g_color_24dp));
                break;
            case Search.Logo.HAMBURGER:
                mSearchArrowDrawable = new SearchArrowDrawable(mContext);
                mImageViewLogo.setImageDrawable(mSearchArrowDrawable);
                break;
            case Search.Logo.ARROW:
                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_back_black_24dp));
                break;
        }
    }

    @Search.Shape
    public int getShape() {
        return mShape;
    }

    public void setShape(@Search.Shape int shape) {
        mShape = shape;

        switch (mShape) {
            case Search.Shape.CLASSIC:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_classic));
                break;
            case Search.Shape.ROUNDED:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_rounded));
                break;
            case Search.Shape.OVAL:
                if (getLayout() == Search.Layout.BAR) {
                    mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_oval));
                }
                break;
        }
    }

    @Search.Theme
    public int getTheme() {
        return mTheme;
    }

    public void setTheme(@Search.Theme int theme) {
        mTheme = theme;

        switch (mTheme) {
            case Search.Theme.COLOR:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_color_24dp));

                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_color_background));
                clearIconsColor();
                if (mImageViewClear != null) {
                    mImageViewClear.setColorFilter(ContextCompat.getColor(mContext, R.color.search_color_icon));
                }
                setMenuIconColor(ContextCompat.getColor(mContext, R.color.search_color_menu));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_color_hint));
                if (getLayout() == Search.Layout.BAR) {
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_color_menu));
                } else {
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_color_title));
                }
                break;
            case Search.Theme.LIGHT:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_black_24dp));

                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
                setLogoIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setMicIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                if (mImageViewClear != null) {
                    mImageViewClear.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
                }
                setMenuIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_light_title));
                break;
            case Search.Theme.DARK:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_black_24dp));

                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
                setLogoIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setMicIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                if (mImageViewClear != null) {
                    mImageViewClear.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                }
                setMenuIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_title));
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    public void setOnMicClickListener(Search.OnMicClickListener listener) {
        mOnMicClickListener = listener;
        mImageViewMic.setVisibility(View.VISIBLE);
    }

    public void setOnMenuClickListener(Search.OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
        mImageViewMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setLogoIconColor(@ColorInt int color) {
        mImageViewLogo.setColorFilter(color);
    }

    public void setMicIconColor(@ColorInt int color) {
        mImageViewMic.setColorFilter(color);
    }

    public void setMenuIconColor(@ColorInt int color) {
        mImageViewMenu.setColorFilter(color);
    }

    public void setShading(@FloatRange(from = 0.5, to = 1.0) float alpha) {
        setAlpha(alpha);
    }

    // ---------------------------------------------------------------------------------------------
    private void clearIconsColor() {
        mImageViewLogo.clearColorFilter();
        mImageViewMic.clearColorFilter();
        if (mImageViewClear != null) {
            mImageViewClear.clearColorFilter();
        }
    }

}
