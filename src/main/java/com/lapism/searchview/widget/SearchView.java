package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.google.android.flexbox.FlexboxLayout;
import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.graphics.SearchAnimator;
import com.lapism.searchview.graphics.SearchArrowDrawable;

// @CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchView extends SearchLayout implements Filter.FilterListener {

    public static final String TAG = SearchView.class.getName();

    @Search.Version
    private int mVersion;
    @Search.VersionMargins
    private int mVersionMargins;

    private int mMenuItemCx = -1;
    private boolean mShadow;
    private long mAnimationDuration;

    private ImageView mImageViewImage;
    private MenuItem mMenuItem;
    private View mMenuItemView; // todo
    private View mViewShadow;
    private View mViewDivider;

    private RecyclerView mRecyclerView;
    private FlexboxLayout mFlexboxLayout;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onTextChanged(CharSequence s) {
        mQueryText = s;

        setMicOrClearIcon(true);
        filter(s);

        if (mOnQueryTextListener != null) {
            // dispatchFilters();
            mOnQueryTextListener.onQueryTextChange(mQueryText);
        }
    }

    @Override
    protected void addFocus() {
        filter(mQueryText);

        if (mShadow) {
            SearchAnimator.fadeOpen(mViewShadow, mAnimationDuration);
        }

        showSuggestions();
        setMicOrClearIcon(true);

        if (mVersion == Search.Version.TOOLBAR) {
            setLogoHamburgerToLogoArrowWithAnimation(true);

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

        setTextImageVisibility(false);
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
    public void open() {
        open(null);
    }

    @Override
    public void close() {
        switch (mVersion) {
            case Search.Version.TOOLBAR:
                mSearchEditText.clearFocus();
                break;
            case Search.Version.MENU_ITEM:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                } else {
                    SearchAnimator.fadeClose(
                            mCardView,
                            mAnimationDuration,
                            mSearchEditText,
                            this,
                            mOnOpenCloseListener);
                }
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        final int layoutResId = getLayout();

        final LayoutInflater inflater = LayoutInflater.from(context);
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

        mFlexboxLayout = findViewById(R.id.search_flexboxLayout);
        mFlexboxLayout.setVisibility(View.GONE);

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

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        setLogo(a.getInteger(R.styleable.SearchView_search_logo, Search.Logo.G));
        setShape(a.getInteger(R.styleable.SearchView_search_shape, Search.Shape.CLASSIC));
        setTheme(a.getInteger(R.styleable.SearchView_search_theme, Search.Theme.COLOR));
        setVersion(a.getInteger(R.styleable.SearchView_search_version, Search.Version.TOOLBAR));
        setVersionMargins(a.getInteger(R.styleable.SearchView_search_version_margins, Search.VersionMargins.TOOLBAR_SMALL));

        if (a.hasValue(R.styleable.SearchView_search_logo_icon)) {
            setLogoIcon(a.getInteger(R.styleable.SearchView_search_logo_icon, 0)); // todo bug + test + check every attribute
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

    @Search.VersionMargins
    public int getVersionMargins() {
        return mVersionMargins;
    }

    public void setVersionMargins(@Search.VersionMargins int versionMargins) {
        mVersionMargins = versionMargins;

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int left, top, right, bottom;

        switch (mVersionMargins) {
            case Search.VersionMargins.TOOLBAR_SMALL:
                left = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_small_left_right);
                top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
                right = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_small_left_right);
                bottom = 0;

                params.setMargins(left, top, right, bottom);

                mCardView.setLayoutParams(params);
                break;
            case Search.VersionMargins.TOOLBAR_MEDIUM:
                left = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_medium_left_right);
                top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
                right = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_medium_left_right);
                bottom = 0;

                params.setMargins(left, top, right, bottom);

                mCardView.setLayoutParams(params);
                break;
            case Search.VersionMargins.TOOLBAR_BIG:
                left = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_big_left_right);
                top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
                right = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_big_left_right);
                bottom = 0;

                params.setMargins(left, top, right, bottom);

                mCardView.setLayoutParams(params);
                break;
            case Search.VersionMargins.MENU_ITEM:
                left = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin_left_right);
                top = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);
                right = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin_left_right);
                bottom = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);

                params.setMargins(left, top, right, bottom);

                mCardView.setLayoutParams(params);
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
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

    /**
     * new SearchDivider(Context)
     * new DividerItemDecoration(Context, DividerItemDecoration.VERTICAL)
     */
    public void addDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * new SearchDivider(Context)
     * new DividerItemDecoration(Context, DividerItemDecoration.VERTICAL)
     */
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mMenuItem != null) {
                        getMenuItemPosition(mMenuItem.getItemId());
                    }
                    mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                SearchAnimator.revealOpen(
                                        mContext,
                                        mCardView,
                                        mMenuItemCx,
                                        mAnimationDuration,
                                        mSearchEditText,
                                        mOnOpenCloseListener);
                            }
                        }
                    });
                } else {
                    SearchAnimator.fadeOpen(
                            mCardView,
                            mAnimationDuration,
                            mSearchEditText,
                            mOnOpenCloseListener);
                }
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
        if (mFlexboxLayout.getChildCount() > 0 && mFlexboxLayout.getVisibility() == View.GONE) {
            mViewDivider.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.VISIBLE);
        }

        if (getAdapter() != null && getAdapter().getItemCount() > 0) {
            mViewDivider.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSuggestions() {
        if (mFlexboxLayout.getChildCount() > 0 && mFlexboxLayout.getVisibility() == View.VISIBLE) {
            mViewDivider.setVisibility(View.GONE);
            mFlexboxLayout.setVisibility(View.GONE);
        }

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

    private int getCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SearchViewSavedState ss = new SearchViewSavedState(superState);
        ss.hasFocus = mSearchEditText.hasFocus();
        ss.shadow = mShadow;
        ss.query = mQueryText != null ? mQueryText.toString() : null; // todo never null ""
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
        if (v == mImageViewLogo) {
            if (mSearchEditText.hasFocus()) {
                close();
            } else {
                if (mOnLogoClickListener != null) {
                    mOnLogoClickListener.onLogoClick();
                }
            }
        } else if (v == mImageViewImage) {
            setTextImageVisibility(true);
        } else if (v == mImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
            }
        } else if (v == mImageViewClear) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
        } else if (v == mImageViewMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        } else if (v == mViewShadow) {
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

}
