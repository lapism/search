package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class SearchBar extends SearchLayout implements View.OnClickListener {

    public static final String TAG = SearchBar.class.getName();

    private TextView mTextView;

    private Search.OnBarClickListener mOnBarClickListener;

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
    @Override
    public void setHintColor(@ColorInt int color) {
        mTextView.setHintTextColor(color);
    }

    @Override
    public void setText(@StringRes int text) {
        mTextView.setText(text);
    }

    @Override
    public void setText(CharSequence text) {
        mTextView.setText(text);
    }

    @Override
    public void setTextColor(@ColorInt int color) {
        mTextView.setTextColor(color);
    }

    @Override
    public void setTextStyle(int style) {
        mTextStyle = style;
        mTextView.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    @Override
    public void setTextFont(Typeface font) {
        mTextFont = font;
        mTextView.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    @Search.Layout
    @Override
    public int getLayout() {
        return Search.Layout.BAR;
    }

    // ---------------------------------------------------------------------------------------------
    public void setOnBarClickListener(Search.OnBarClickListener listener) {
        mOnBarClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchBar, defStyleAttr, defStyleRes);
        final int layoutResId = a.getResourceId(R.styleable.SearchBar_search_layout, R.layout.search_bar);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(layoutResId, this, true);

        mCardView = findViewById(R.id.search_cardView);

        mImageViewLogo = findViewById(R.id.search_imageView_logo);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setOnClickListener(this);
        mImageViewMic.setVisibility(View.GONE);

        mImageViewMenu = findViewById(R.id.search_imageView_menu);
        mImageViewMenu.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_menu_black_24dp));
        mImageViewMenu.setOnClickListener(this);
        mImageViewMenu.setVisibility(View.GONE);

        mTextView = findViewById(R.id.search_textView);

        setOnClickListener(this);

        if (a.hasValue(R.styleable.SearchView_search_logo)) {
            setLogo(a.getInt(R.styleable.SearchView_search_logo, Search.Logo.G));
        } else {
            setLogo(Search.Logo.G);
        }

        if (a.hasValue(R.styleable.SearchView_search_shape)) {
            setShape(a.getInt(R.styleable.SearchView_search_shape, Search.Shape.CLASSIC));
        } else {
            setShape(Search.Shape.CLASSIC);
        }

        if (a.hasValue(R.styleable.SearchView_search_theme)) {
            setTheme(a.getInt(R.styleable.SearchView_search_theme, Search.Theme.COLOR));
        } else {
            setTheme(Search.Theme.COLOR);
        }

        a.recycle();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (v == mImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
            }
        }

        if (v == mImageViewMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        }

        if (v == this) {
            if (mOnBarClickListener != null) {
                mOnBarClickListener.onBarClick();
            }
        }
    }

}
