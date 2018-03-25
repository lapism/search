package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.lapism.searchview.R.layout;
import com.lapism.searchview.R.styleable;
import com.lapism.searchview.Search;
import com.lapism.searchview.Search.Logo;
import com.lapism.searchview.Search.Shape;
import com.lapism.searchview.Search.Theme;
import com.lapism.searchview.Search.VersionMargins;

import java.util.Objects;


public class SearchBar extends SearchLayout {

    public static final String TAG = SearchBar.class.getName();

    private Search.OnBarClickListener mOnBarClickListener;

    // ---------------------------------------------------------------------------------------------
    public SearchBar(@NonNull Context context) {
        this(context, null);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    private SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onTextChanged(CharSequence s) {
        if (this.mOnQueryTextListener != null) {
            this.mOnQueryTextListener.onQueryTextChange(s);
        }
    }

    @Override
    protected void addFocus() {
        if (this.mOnMicClickListener == null) {
            this.mImageViewMic.setVisibility(View.VISIBLE);
        }
        this.showKeyboard();
    }

    @Override
    protected void removeFocus() {
        if (this.mOnMicClickListener == null) {
            this.mImageViewMic.setVisibility(View.GONE);
        }
        this.hideKeyboard();
    }

    @Override
    protected boolean isView() {
        return false;
    }

    @Override
    protected int getLayout() {
        return layout.search_bar;
    }

    @Override
    protected void open() {
        this.mSearchEditText.setVisibility(View.VISIBLE);
        this.mSearchEditText.requestFocus();
    }

    @Override
    public void close() {
        this.mSearchEditText.clearFocus();
        this.mSearchEditText.setVisibility(View.GONE);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.SearchBar, defStyleAttr, defStyleRes);
        int layoutResId = this.getLayout();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutResId, this, true);

        super.init(context, attrs, defStyleAttr, defStyleRes);

        this.setLogo(a.getInt(styleable.SearchBar_search_logo, Logo.GOOGLE));
        this.setShape(a.getInt(styleable.SearchBar_search_shape, Shape.OVAL));
        this.setTheme(a.getInt(styleable.SearchBar_search_theme, Theme.GOOGLE));
        this.setVersionMargins(a.getInt(styleable.SearchBar_search_version_margins, VersionMargins.BAR));

        if (a.hasValue(styleable.SearchBar_search_elevation)) {
            this.setElevation(a.getDimensionPixelSize(styleable.SearchBar_search_elevation, 0));
        }

        a.recycle();

        this.setOnClickListener(this);
    }

    // ---------------------------------------------------------------------------------------------
    // Listeners
    public void setOnBarClickListener(Search.OnBarClickListener listener) {
        mOnBarClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (Objects.equals(v, mImageViewLogo)) {
            close();
        } else if (Objects.equals(v, mImageViewMic)) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
            }
        } else if (Objects.equals(v, mImageViewMenu)) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        } else if (Objects.equals(v, this)) {
            if (mOnBarClickListener != null) {
                mOnBarClickListener.onBarClick();
            } else {
                open();
            }
        }
    }

}
