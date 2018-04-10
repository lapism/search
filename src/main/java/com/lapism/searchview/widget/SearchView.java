package com.lapism.searchview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.graphics.SearchAnimator;
import com.lapism.searchview.graphics.SearchArrowDrawable;

import java.util.Objects;


public class SearchView extends SearchLayout implements Filter.FilterListener, CoordinatorLayout.AttachedBehavior {

    @Search.Version
    private int mVersion;

    private int mMenuItemCx = -1;
    private boolean mShadow;
    private long mAnimationDuration;

    private ImageView mImageViewImage;
    private MenuItem mMenuItem;
    private View mMenuItemView;
    private View mViewShadow;
    private View mViewDivider;
    private RecyclerView mRecyclerView;

    private Search.OnLogoClickListener mOnLogoClickListener;
    private Search.OnOpenCloseListener mOnOpenCloseListener;

    // ---------------------------------------------------------------------------------------------
    public SearchView(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    private static int getCenterX(@NonNull View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onTextChanged(CharSequence s) {
        mQueryText = s;

        setMicOrClearIcon(true);
        filter(s);

        if (mOnQueryTextListener != null) {
            mOnQueryTextListener.onQueryTextChange(mQueryText);
        }
    }

    @Override
    protected void addFocus() {
        filter(mQueryText);

        if (mShadow) {
            SearchAnimator.fadeOpen(mViewShadow, mAnimationDuration);
        }

        setMicOrClearIcon(true);

        if (mVersion == Search.Version.TOOLBAR) {
            setLogoHamburgerToLogoArrowWithAnimation(true);
            // todo SavedState, marginy kulate a barva divideru
            if (mOnOpenCloseListener != null) {
                mOnOpenCloseListener.onOpen();
            }
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, mAnimationDuration);
    }

    @Override
    protected void removeFocus() {
        if (mShadow) {
            SearchAnimator.fadeClose(mViewShadow, mAnimationDuration);
        }

        //setTextImageVisibility(false); todo error + shadow error pri otoceni, pak mizi animace
        hideSuggestions();
        hideKeyboard();
        setMicOrClearIcon(false);

        if (mVersion == Search.Version.TOOLBAR) {
            setLogoHamburgerToLogoArrowWithAnimation(false);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onClose();
                    }
                }
            }, mAnimationDuration);
        }
    }

    @Override
    protected boolean isView() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.search_view;
    }

    @Override
    protected void open() {
        open(null);
    }

    @Override
    public void close() {
        switch (mVersion) {
            case Search.Version.TOOLBAR:
                mSearchEditText.clearFocus();
                break;
            case Search.Version.MENU_ITEM:
                if (mMenuItem != null) {
                    getMenuItemPosition(mMenuItem.getItemId());
                }
                SearchAnimator.revealClose(
                        mContext,
                        mCardView,
                        mMenuItemCx,
                        mAnimationDuration,
                        mSearchEditText,
                        this,
                        mOnOpenCloseListener);
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        int layoutResId = getLayout();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutResId, this, true);

        super.init(context, attrs, defStyleAttr, defStyleRes);

        mImageViewImage = findViewById(R.id.search_imageView_image);
        mImageViewImage.setOnClickListener(this);

        mImageViewClear = findViewById(R.id.search_imageView_clear);
        mImageViewClear.setOnClickListener(this);
        mImageViewClear.setVisibility(View.GONE);

        mViewShadow = findViewById(R.id.search_view_shadow);
        mViewShadow.setVisibility(View.GONE);
        mViewShadow.setOnClickListener(this);

        mViewDivider = findViewById(R.id.search_view_divider);
        mViewDivider.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.search_recyclerView);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard();
                }
            }

        });

        setLogo(a.getInteger(R.styleable.SearchView_search_logo, Search.Logo.HAMBURGER_ARROW));
        setShape(a.getInteger(R.styleable.SearchView_search_shape, Search.Shape.CLASSIC));
        setTheme(a.getInteger(R.styleable.SearchView_search_theme, Search.Theme.PLAY));
        setVersionMargins(a.getInteger(R.styleable.SearchView_search_version_margins, Search.VersionMargins.TOOLBAR));
        setVersion(a.getInteger(R.styleable.SearchView_search_version, Search.Version.TOOLBAR));

        if (a.hasValue(R.styleable.SearchView_search_logo_icon)) {
            setLogoIcon(a.getInteger(R.styleable.SearchView_search_logo_icon, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_logo_color)) {
            setLogoColor(ContextCompat.getColor(mContext, a.getResourceId(R.styleable.SearchView_search_logo_color, 0)));
        }

        if (a.hasValue(R.styleable.SearchView_search_mic_icon)) {
            setMicIcon(a.getResourceId(R.styleable.SearchView_search_mic_icon, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_mic_color)) {
            setMicColor(a.getColor(R.styleable.SearchView_search_mic_color, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_clear_icon)) {
            setClearIcon(a.getDrawable(R.styleable.SearchView_search_clear_icon));
        } else {
            setClearIcon(ContextCompat.getDrawable(mContext, R.drawable.search_ic_clear_black_24dp));
        }

        if (a.hasValue(R.styleable.SearchView_search_clear_color)) {
            setClearColor(a.getColor(R.styleable.SearchView_search_clear_color, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_menu_icon)) {
            setMenuIcon(a.getResourceId(R.styleable.SearchView_search_menu_icon, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_menu_color)) {
            setMenuColor(a.getColor(R.styleable.SearchView_search_menu_color, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_background_color)) {
            setBackgroundColor(a.getColor(R.styleable.SearchView_search_background_color, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_text_image)) {
            setTextImage(a.getResourceId(R.styleable.SearchView_search_text_image, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_text_color)) {
            setTextColor(a.getColor(R.styleable.SearchView_search_text_color, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_text_size)) {
            setTextSize(a.getDimension(R.styleable.SearchView_search_text_size, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_text_style)) {
            setTextStyle(a.getInt(R.styleable.SearchView_search_text_style, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_hint)) {
            setHint(a.getString(R.styleable.SearchView_search_hint));
        }

        if (a.hasValue(R.styleable.SearchView_search_hint_color)) {
            setHintColor(a.getColor(R.styleable.SearchView_search_hint_color, 0));
        }

        setAnimationDuration(a.getInt(R.styleable.SearchView_search_animation_duration, mContext.getResources().getInteger(R.integer.search_animation_duration)));
        setShadow(a.getBoolean(R.styleable.SearchView_search_shadow, true));
        setShadowColor(a.getColor(R.styleable.SearchView_search_shadow_color, ContextCompat.getColor(mContext, R.color.search_shadow)));

        if (a.hasValue(R.styleable.SearchView_search_elevation)) {
            setElevation(a.getDimensionPixelSize(R.styleable.SearchView_search_elevation, 0));
        }

        a.recycle();

        setSaveEnabled(true);

        mSearchEditText.setVisibility(View.VISIBLE); // todo
    }

    // ---------------------------------------------------------------------------------------------
    @Search.Version
    public int getVersion() {
        return mVersion;
    }

    public void setVersion(@Search.Version int version) {
        mVersion = version;

        if (mVersion == Search.Version.MENU_ITEM) {
            setVisibility(View.GONE);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Divider
    @Override
    public void setDividerColor(@ColorInt int color) {
        mViewDivider.setBackgroundColor(color);
    }

    // Clear
    public void setClearIcon(@DrawableRes int resource) {
        mImageViewClear.setImageResource(resource);
    }

    public void setClearIcon(@Nullable Drawable drawable) {
        mImageViewClear.setImageDrawable(drawable);
    }

    @Override
    public void setClearColor(@ColorInt int color) {
        mImageViewClear.setColorFilter(color);
    }

    // Image
    public void setTextImage(@DrawableRes int resource) {
        mImageViewImage.setImageResource(resource);
        setTextImageVisibility(false);
    }

    public void setTextImage(@Nullable Drawable drawable) {
        mImageViewImage.setImageDrawable(drawable);
        setTextImageVisibility(false);
    }

    // Animation duration
    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    // Shadow
    public void setShadow(boolean shadow) {
        mShadow = shadow;
    }

    public void setShadowColor(@ColorInt int color) {
        mViewShadow.setBackgroundColor(color);
    }

    // Adapter
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void addDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void removeDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
    }

    // Others
    public void open(MenuItem menuItem) {
        mMenuItem = menuItem;

        switch (mVersion) {
            case Search.Version.TOOLBAR:
                mSearchEditText.requestFocus();
                break;
            case Search.Version.MENU_ITEM:
                setVisibility(View.VISIBLE);
                if (mMenuItem != null) {
                    getMenuItemPosition(mMenuItem.getItemId());
                }
                mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        SearchAnimator.revealOpen(
                                mContext,
                                mCardView,
                                mMenuItemCx,
                                mAnimationDuration,
                                mSearchEditText,
                                mOnOpenCloseListener);
                    }
                });
                break;
        }
    }

    public void setLogoHamburgerToLogoArrowWithAnimation(boolean animate) {
        if (mSearchArrowDrawable != null) {
            if (animate) {
                mSearchArrowDrawable.setVerticalMirror(false);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
            } else {
                mSearchArrowDrawable.setVerticalMirror(true);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_HAMBURGER, mAnimationDuration);
            }
        }
    }

    public void setLogoHamburgerToLogoArrowWithoutAnimation(boolean animation) {
        if (mSearchArrowDrawable != null) {
            if (animation) {
                mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_ARROW);
            } else {
                mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_HAMBURGER);
            }
        }
    }

    // Listeners
    public void setOnLogoClickListener(Search.OnLogoClickListener listener) {
        mOnLogoClickListener = listener;
    }

    public void setOnOpenCloseListener(Search.OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    private void setMicOrClearIcon(boolean hasFocus) {
        if (hasFocus && !TextUtils.isEmpty(mQueryText)) {
            if (mOnMicClickListener != null) {
                mImageViewMic.setVisibility(View.GONE);
            }
            mImageViewClear.setVisibility(View.VISIBLE);
        } else {
            mImageViewClear.setVisibility(View.GONE);
            if (mOnMicClickListener != null) {
                mImageViewMic.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setTextImageVisibility(boolean hasFocus) {
        if (hasFocus) {
            mImageViewImage.setVisibility(View.GONE);
            mSearchEditText.setVisibility(View.VISIBLE);
            mSearchEditText.requestFocus();
        } else {
            mSearchEditText.setVisibility(View.GONE);
            mImageViewImage.setVisibility(View.VISIBLE);
        }
    }

    private void filter(CharSequence s) {
        if (getAdapter() != null && getAdapter() instanceof Filterable) {
            ((Filterable) getAdapter()).getFilter().filter(s, this);
        }
    }

    private void showSuggestions() {
        if (getAdapter() != null && getAdapter().getItemCount() > 0) {
            mViewDivider.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSuggestions() {
        if (getAdapter() != null && getAdapter().getItemCount() > 0) {
            mViewDivider.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private void getMenuItemPosition(int menuItemId) {
        if (mMenuItemView != null) {
            mMenuItemCx = getCenterX(mMenuItemView);
        }
        ViewParent viewParent = getParent();
        while (viewParent != null && viewParent instanceof View) {
            View parent = (View) viewParent;
            View view = parent.findViewById(menuItemId);
            if (view != null) {
                mMenuItemView = view;
                mMenuItemCx = getCenterX(mMenuItemView);
                break;
            }
            viewParent = viewParent.getParent();
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SearchViewSavedState ss = new SearchViewSavedState(superState);
        ss.hasFocus = mSearchEditText.hasFocus();
        ss.shadow = mShadow;
        ss.query = mQueryText != null ? mQueryText.toString() : null;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SearchViewSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SearchViewSavedState ss = (SearchViewSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mShadow = ss.shadow;
        if (mShadow) {
            mViewShadow.setVisibility(View.VISIBLE);
        }
        if (ss.hasFocus) {
            open();
        }
        if (ss.query != null) {
            setText(ss.query);
        }
        requestLayout();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (Objects.equals(v, mImageViewLogo)) {
            if (mSearchEditText.hasFocus()) {
                close();
            } else {
                if (mOnLogoClickListener != null) {
                    mOnLogoClickListener.onLogoClick();
                }
            }
        } else if (Objects.equals(v, mImageViewImage)) {
            setTextImageVisibility(true);
        } else if (Objects.equals(v, mImageViewMic)) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
            }
        } else if (Objects.equals(v, mImageViewClear)) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
        } else if (Objects.equals(v, mImageViewMenu)) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        } else if (Objects.equals(v, mViewShadow)) {
            close();
        }
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            hideSuggestions();
        }
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return new SearchBehavior();
    }

}
