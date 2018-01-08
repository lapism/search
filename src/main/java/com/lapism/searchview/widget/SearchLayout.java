package com.lapism.searchview.widget;

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

import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.graphics.SearchArrowDrawable;

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
    protected SearchEditText mSearchEditText;
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
    public abstract void setHint(CharSequence hint);

    public abstract void setHint(@StringRes int hint);

    public abstract void setHintColor(@ColorInt int color);

    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     */
    public abstract void setTextStyle(int style);

    /**
     * Typeface.DEFAULT
     * Typeface.DEFAULT_BOLD
     * Typeface.SANS_SERIF
     * Typeface.SERIF
     * Typeface.MONOSPACE
     */
    public abstract void setTextFont(Typeface font);

    protected abstract boolean isView();

    // ---------------------------------------------------------------------------------------------
    @Search.Logo
    public int getLogo() {
        return mLogo;
    }

    public void setLogo(@Search.Logo int logo) {
        mLogo = logo;

        switch (mLogo) {
            case Search.Logo.GOOGLE:
                int left = getResources().getDimensionPixelSize(R.dimen.search_logo_padding_left);
                int top = getContext().getResources().getDimensionPixelSize(R.dimen.search_logo_padding_top);
                int right = mContext.getResources().getDimensionPixelSize(R.dimen.search_logo_padding_right);
                int bottom = 0;

                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color));
                mImageViewLogo.setPadding(left, top, right, bottom);
                break;
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
                setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_classic));
                break;
            case Search.Shape.ROUNDED:
                setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_rounded));
                break;
            case Search.Shape.OVAL:
                if (!isView()) {
                    setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_oval));
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

                setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_color_background));
                clearIconsColor();
                setClearColor(ContextCompat.getColor(mContext, R.color.search_color_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_color_menu));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_color_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_color_title));
                break;
            case Search.Theme.LIGHT:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_black_24dp));

                setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
                setLogoColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setMicColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setClearColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_light_title));
                break;
            case Search.Theme.DARK:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_black_24dp));

                setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
                setLogoColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setMicColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setClearColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_title));
                break;
        }
    }

    public void setLogoColor(@ColorInt int color) {
        mImageViewLogo.setColorFilter(color);
    }

    public void setMicColor(@ColorInt int color) {
        mImageViewMic.setColorFilter(color);
    }

    public void setMenuColor(@ColorInt int color) {
        mImageViewMenu.setColorFilter(color);
    }

    public void setCardBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setShading(@FloatRange(from = 0.5, to = 1.0) float alpha) {
        setAlpha(alpha);
    }

    public void setOnMicClickListener(Search.OnMicClickListener listener) {
        mOnMicClickListener = listener;
        mImageViewMic.setVisibility(View.VISIBLE);
    }

    public void setOnMenuClickListener(Search.OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
        mImageViewMenu.setVisibility(View.VISIBLE);
    }

    // ---------------------------------------------------------------------------------------------
    protected void setClearColor(@ColorInt int color) {
        if (mImageViewClear != null) {
            mImageViewClear.setColorFilter(color);
        }
    }

    protected void setTextColor(@ColorInt int color) {
        if (mSearchEditText != null) {
            mSearchEditText.setTextColor(color);
        }
    }

    private void setRadius(int radius) {
        mCardView.setRadius(radius);
    }

    private void clearIconsColor() {
        mImageViewLogo.clearColorFilter();
        mImageViewMic.clearColorFilter();
        if (mImageViewClear != null) {
            mImageViewClear.clearColorFilter();
        }
    }

}
