package com.lapism.searchview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;

import java.util.Objects;


public class SearchBar extends SearchLayout {

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

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onTextChanged(CharSequence s) {
        if (mOnQueryTextListener != null) {
            mOnQueryTextListener.onQueryTextChange(s);
        }
    }

    @Override
    protected void addFocus() {
        if (mOnMicClickListener == null) {
            mImageViewMic.setVisibility(View.VISIBLE);
        }
        showKeyboard();
    }

    @Override
    protected void removeFocus() {
        if (mOnMicClickListener == null) {
            mImageViewMic.setVisibility(View.GONE);
        }
        hideKeyboard();
    }

    @Override
    protected boolean isView() {
        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.search_bar;
    }

    @Override
    protected void open() {
        mSearchEditText.setVisibility(View.VISIBLE);
        mSearchEditText.requestFocus();
    }

    @Override
    public void close() {
        mSearchEditText.clearFocus();
        mSearchEditText.setVisibility(View.GONE);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchBar, defStyleAttr, defStyleRes);
        int layoutResId = getLayout();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutResId, this, true);

        super.init(context, attrs, defStyleAttr, defStyleRes);

        setLogo(a.getInt(R.styleable.SearchBar_search_logo, Search.Logo.GOOGLE));
        setShape(a.getInt(R.styleable.SearchBar_search_shape, Search.Shape.OVAL));
        setTheme(a.getInt(R.styleable.SearchBar_search_theme, Search.Theme.GOOGLE));
        setVersionMargins(a.getInt(R.styleable.SearchBar_search_version_margins, Search.VersionMargins.BAR));

        if (a.hasValue(R.styleable.SearchBar_search_elevation)) {
            setElevation(a.getDimensionPixelSize(R.styleable.SearchBar_search_elevation, 0));
        }

        a.recycle();

        setOnClickListener(this);
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
