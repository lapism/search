package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class SearchWidget extends FrameLayout {

    public static final String TAG = SearchWidget.class.getName();

    private Context mContext;
    private CardView mCardView;
    private ImageView mImageViewLogo;
    private ImageView mImageViewMic;
    private ImageView mImageViewMenu;
    private TextView mTextView;

    private OnMicClickListener mOnMicClickListener;
    private OnMenuClickListener mOnMenuClickListener;

    private int mTheme = SearchDefs.Theme.COLOR;

    public SearchWidget(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SearchWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SearchWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchWidget, defStyleAttr, defStyleRes);
        final int layoutResId = a.getResourceId(R.styleable.SearchWidget_layout1, R.layout.search_widget);

        a.recycle();
        /*if(a != null) {
            if (a.length() > 0) {
                if (a.hasValue(R.styleable.SearchWidget_layout)) {
                    final int layoutResId = a.getResourceId(R.styleable.SearchWidget_layout, R.layout.search_widget);
                }
            }
        }*/

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(layoutResId, this, true);

        mCardView = findViewById(R.id.search_cardView);

        mImageViewLogo = findViewById(R.id.search_imageView_logo);

        int left = getResources().getDimensionPixelSize(R.dimen.search_logo_padding_left);
        int right  = mContext.getResources().getDimensionPixelSize(R.dimen.search_logo_padding_right);

        mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color));
        mImageViewLogo.setPadding(left,0,right,0);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_color_24dp));
        mImageViewMic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnMicClickListener != null) {
                    mOnMicClickListener.onMicClick();
                }
            }
        });

        mImageViewMenu = findViewById(R.id.search_imageView_menu);
        mImageViewMenu.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_menu_black_24dp));
        mImageViewMenu.setColorFilter(R.color.search_play_store_text_highlight, PorterDuff.Mode.SRC_IN); // todo
        mImageViewMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onMenuClick();
                }
            }
        });

        mTextView = findViewById(R.id.search_textView);
        mTextView.setText(mContext.getString(R.string.search_widget));//getRes

        //setClickable(true);
    }

    public interface OnMicClickListener {
        void onMicClick();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    public void setOnMicClickListener(OnMicClickListener listener) {
        mOnMicClickListener = listener;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
    }

    // getContext, mContext.Getrouserces, getResoursces
    public void setLogo(@SearchDefs.Logo int logo) {
        switch (logo) {
            case SearchDefs.Logo.GOOGLE:
                int left = getResources().getDimensionPixelSize(R.dimen.search_logo_padding_left);
                int right  = mContext.getResources().getDimensionPixelSize(R.dimen.search_logo_padding_right);

                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color));
                mImageViewLogo.setPadding(left,0,right,0);
                break;
            case SearchDefs.Logo.G:
                mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_g_color_24dp));
                break;
        }
    }

    public void setShape(@SearchDefs.Shape int shape) {
        switch (shape) {
            case SearchDefs.Shape.CLASSIC:
                mCardView.setBackgroundResource(0);
                break;
            case SearchDefs.Shape.ROUNDED:

                break;
            case SearchDefs.Shape.OVAL:

                break;
            case SearchDefs.Shape.ROUNDED_TOP:

                break;
        }
    }

    public void setTheme(@SearchDefs.Theme int theme) {
        mTheme = theme;

        switch (mTheme) {
            case SearchDefs.Theme.COLOR:
                setBackgroundColor(0);
                clearIconColor();
                break;
            case SearchDefs.Theme.LIGHT:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
                setIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                break;
            case SearchDefs.Theme.DARK:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
                setIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                break;
        }
    }

    public void setShading(float alpha) {
        setAlpha(alpha);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setTextColor(@ColorInt int color) {
        mTextView.setTextColor(color);
    }

    private void clearIconColor(){
        mImageViewLogo.clearColorFilter();
        mImageViewMic.clearColorFilter();
        mImageViewMenu.clearColorFilter();
    }

    private void setIconColor(@ColorInt int color) {
        ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);//todo

        mImageViewLogo.setColorFilter(color);
        mImageViewMic.setColorFilter(color);
        mImageViewMenu.setColorFilter(color);
    }

}
