package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class SearchBar extends FrameLayout implements View.OnClickListener {

    public static final String TAG = SearchBar.class.getName();

    private Context mContext;
    private CardView mCardView;
    private ImageView mImageViewLogo;
    private ImageView mImageViewMic;
    private ImageView mImageViewMenu;
    private TextView mTextView;

    private OnBarClickListener mOnBarClickListener;
    private OnMicClickListener mOnMicClickListener;
    private OnMenuClickListener mOnMenuClickListener;

    // ---------------------------------------------------------------------------------------------
    public SearchBar(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchBar, defStyleAttr, defStyleRes);
        final int layoutResId = a.getResourceId(R.styleable.SearchBar_layout_bar, R.layout.search_bar);
        a.recycle();
        // TODO: Attributes + text size

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(layoutResId, this, true);

        mCardView = findViewById(R.id.search_cardView);

        mImageViewLogo = findViewById(R.id.search_imageView_logo);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setOnClickListener(this);

        mImageViewMenu = findViewById(R.id.search_imageView_menu);
        mImageViewMenu.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_menu_black_24dp));
        mImageViewMenu.setOnClickListener(this);

        mTextView = findViewById(R.id.search_textView);

        setLogo(Search.Logo.GOOGLE);
        setShape(Search.Shape.DEFAULT);
        setTheme(Search.Theme.COLOR);
    }

    private void clearIconColor() {
        mImageViewLogo.clearColorFilter();
        mImageViewMic.clearColorFilter();
        mImageViewMenu.clearColorFilter();
    }

    private void setIconColor(@ColorInt int color) {
        // ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        mImageViewLogo.setColorFilter(color);
        mImageViewMic.setColorFilter(color);
    }

    private void setMenuColor(@ColorInt int color) {
        mImageViewMenu.setColorFilter(color);
    }

    // ---------------------------------------------------------------------------------------------
    public void setLogo(@Search.Logo int logo) {
        int left, top, right, bottom;

        switch (logo) {
            case Search.Logo.GOOGLE:
                left = getResources().getDimensionPixelSize(R.dimen.search_logo_padding_left);
                top = getContext().getResources().getDimensionPixelSize(R.dimen.search_logo_padding_top);
                right = mContext.getResources().getDimensionPixelSize(R.dimen.search_logo_padding_right);
                bottom = 0;

                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color));
                mImageViewLogo.setPadding(left, top, right, bottom);
                break;
            case Search.Logo.G:
                top = 0;
                leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_logo_padding_left_right_bar);
                bottom = 0;// TODO DODELAT A PRIDAT MENU IKONKU I DO TOOLBARU

                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_g_color_24dp));
                mImageViewLogo.setPadding(leftRight, 0, leftRight, 0);
                break;
            case Search.Logo.HAMBURGER:
                break;
        }
    }

    public void setShape(@Search.Shape int shape) {
        switch (shape) {
            case Search.Shape.DEFAULT:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_default));
                break;
            case Search.Shape.ROUNDED_TOP:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_rounded));
                break;
            case Search.Shape.ROUNDED:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_rounded));
                break;
            case Search.Shape.OVAL:
                mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_shape_oval));
                break;
        }
    }

    public void setTheme(@Search.Theme int theme) {
        switch (theme) {
            case Search.Theme.COLOR:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_color_24dp));

                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_bar_color_background));
                clearIconColor();
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_bar_color_menu));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_bar_color_menu));
                break;
            case Search.Theme.LIGHT:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_black_24dp));

                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_bar_light_background));
                setIconColor(ContextCompat.getColor(mContext, R.color.search_bar_light_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_bar_light_menu));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_bar_light_menu));
                break;
            case Search.Theme.DARK:
                mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_black_24dp));

                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_bar_dark_background));
                setIconColor(ContextCompat.getColor(mContext, R.color.search_bar_dark_icon));
                setMenuColor(ContextCompat.getColor(mContext, R.color.search_bar_dark_menu));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_bar_dark_menu));
                break;
        }
    }

    public void setShading(@FloatRange(from = 0.5, to = 1.0) float alpha) {
        setAlpha(alpha);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setText(@StringRes int text) {
        mTextView.setText(text);
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        mTextView.setTextColor(color);
    }

    public void setOnBarClickListener(OnBarClickListener listener) {
        mOnBarClickListener = listener;
    }

    public void setOnMicClickListener(OnMicClickListener listener) {
        mOnMicClickListener = listener;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view == mImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
            }
        } else if (view == mImageViewMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        } else {
            if (mOnBarClickListener != null) {
                mOnBarClickListener.onBarClick();
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    public interface OnBarClickListener {
        void onBarClick();
    }

    public interface OnMicClickListener {
        void onMicClick();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

}
