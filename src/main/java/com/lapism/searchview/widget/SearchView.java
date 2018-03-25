package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.lapism.searchview.R.color;
import com.lapism.searchview.R.drawable;
import com.lapism.searchview.R.id;
import com.lapism.searchview.R.integer;
import com.lapism.searchview.R.layout;
import com.lapism.searchview.R.styleable;
import com.lapism.searchview.Search;
import com.lapism.searchview.Search.Logo;
import com.lapism.searchview.Search.Shape;
import com.lapism.searchview.Search.Theme;
import com.lapism.searchview.Search.VersionMargins;
import com.lapism.searchview.graphics.SearchAnimator;
import com.lapism.searchview.graphics.SearchArrowDrawable;

import java.util.Objects;


public class SearchView extends SearchLayout implements Filter.FilterListener, CoordinatorLayout.AttachedBehavior {

    public static final String TAG = SearchView.class.getName();

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
        this(context, null);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    private SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs, defStyleAttr, defStyleRes);
    }

    private static int getCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onTextChanged(CharSequence s) {
        this.mQueryText = s;

        this.setMicOrClearIcon(true);
        this.filter(s);

        if (this.mOnQueryTextListener != null) {
            this.mOnQueryTextListener.onQueryTextChange(this.mQueryText);
        }
    }

    @Override
    protected void addFocus() {
        this.filter(this.mQueryText);

        if (this.mShadow) {
            SearchAnimator.fadeOpen(this.mViewShadow, this.mAnimationDuration);
        }

        this.showSuggestions();
        this.setMicOrClearIcon(true);

        if (this.mVersion == Search.Version.TOOLBAR) {
            if (this.mIconAnimation) {
                this.setLogoHamburgerToLogoArrowWithAnimation(true);
            } else {
                this.setLogoHamburgerToLogoArrowWithoutAnimation(true);
            }

            if (this.mOnOpenCloseListener != null) {
                this.mOnOpenCloseListener.onOpen();
            }
        }

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                SearchView.this.showKeyboard();
            }
        }, this.mAnimationDuration);
    }

    @Override
    protected void removeFocus() {
        if (this.mShadow) {
            SearchAnimator.fadeClose(this.mViewShadow, this.mAnimationDuration);
        }

        this.setTextImageVisibility(false);
        this.hideSuggestions();
        this.hideKeyboard();
        this.setMicOrClearIcon(false);

        if (this.mVersion == Search.Version.TOOLBAR) {
            if (this.mIconAnimation) {
                this.setLogoHamburgerToLogoArrowWithAnimation(false);
            } else {
                this.setLogoHamburgerToLogoArrowWithoutAnimation(false);
            }

            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SearchView.this.mOnOpenCloseListener != null) {
                        SearchView.this.mOnOpenCloseListener.onClose();
                    }
                }
            }, this.mAnimationDuration);
        }
    }

    @Override
    protected boolean isView() {
        return true;
    }

    @Override
    protected int getLayout() {
        return layout.search_view;
    }

    @Override
    protected void open() {
        this.open(null);
    }

    @Override
    public void close() {
        switch (this.mVersion) {
            case Search.Version.TOOLBAR:
                this.mSearchEditText.clearFocus();
                break;
            case Search.Version.MENU_ITEM:
                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    if (this.mMenuItem != null) {
                        this.getMenuItemPosition(this.mMenuItem.getItemId());
                    }
                    SearchAnimator.revealClose(
                            this.mContext,
                            this.mCardView,
                            this.mMenuItemCx,
                            this.mAnimationDuration,
                            this.mSearchEditText,
                            this,
                            this.mOnOpenCloseListener);
                } else {
                    SearchAnimator.fadeClose(
                            this.mCardView,
                            this.mAnimationDuration,
                            this.mSearchEditText,
                            this,
                            this.mOnOpenCloseListener);
                }
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.SearchView, defStyleAttr, defStyleRes);
        int layoutResId = this.getLayout();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutResId, this, true);

        super.init(context, attrs, defStyleAttr, defStyleRes);

        this.mImageViewImage = this.findViewById(id.search_imageView_image);
        this.mImageViewImage.setOnClickListener(this);

        this.mImageViewClear = this.findViewById(id.search_imageView_clear);
        this.mImageViewClear.setOnClickListener(this);
        this.mImageViewClear.setVisibility(View.GONE);

        this.mViewShadow = this.findViewById(id.search_view_shadow);
        this.mViewShadow.setVisibility(View.GONE);
        this.mViewShadow.setOnClickListener(this);

        this.mViewDivider = this.findViewById(id.search_view_divider);
        this.mViewDivider.setVisibility(View.GONE);

        this.mRecyclerView = this.findViewById(id.search_recyclerView);
        this.mRecyclerView.setVisibility(View.GONE);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.mRecyclerView.setNestedScrollingEnabled(false);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    SearchView.this.hideKeyboard();
                }
            }

        });

        this.setLogo(a.getInteger(styleable.SearchView_search_logo, Logo.HAMBURGER_ARROW));
        this.setShape(a.getInteger(styleable.SearchView_search_shape, Shape.CLASSIC));
        this.setTheme(a.getInteger(styleable.SearchView_search_theme, Theme.PLAY));
        this.setVersionMargins(a.getInteger(styleable.SearchView_search_version_margins, VersionMargins.TOOLBAR));
        this.setVersion(a.getInteger(styleable.SearchView_search_version, Search.Version.TOOLBAR));

        if (a.hasValue(styleable.SearchView_search_logo_icon)) {
            this.setLogoIcon(a.getInteger(styleable.SearchView_search_logo_icon, 0));
        }

        if (a.hasValue(styleable.SearchView_search_logo_color)) {
            this.setLogoColor(ContextCompat.getColor(this.mContext, a.getResourceId(styleable.SearchView_search_logo_color, 0)));
        }

        if (a.hasValue(styleable.SearchView_search_mic_icon)) {
            this.setMicIcon(a.getResourceId(styleable.SearchView_search_mic_icon, 0));
        }

        if (a.hasValue(styleable.SearchView_search_mic_color)) {
            this.setMicColor(a.getColor(styleable.SearchView_search_mic_color, 0));
        }

        if (a.hasValue(styleable.SearchView_search_clear_icon)) {
            this.setClearIcon(a.getDrawable(styleable.SearchView_search_clear_icon));
        } else {
            this.setClearIcon(ContextCompat.getDrawable(this.mContext, drawable.search_ic_clear_black_24dp));
        }

        if (a.hasValue(styleable.SearchView_search_clear_color)) {
            this.setClearColor(a.getColor(styleable.SearchView_search_clear_color, 0));
        }

        if (a.hasValue(styleable.SearchView_search_menu_icon)) {
            this.setMenuIcon(a.getResourceId(styleable.SearchView_search_menu_icon, 0));
        }

        if (a.hasValue(styleable.SearchView_search_menu_color)) {
            this.setMenuColor(a.getColor(styleable.SearchView_search_menu_color, 0));
        }

        if (a.hasValue(styleable.SearchView_search_background_color)) {
            this.setBackgroundColor(a.getColor(styleable.SearchView_search_background_color, 0));
        }

        if (a.hasValue(styleable.SearchView_search_text_image)) {
            this.setTextImage(a.getResourceId(styleable.SearchView_search_text_image, 0));
        }

        if (a.hasValue(styleable.SearchView_search_text_color)) {
            this.setTextColor(a.getColor(styleable.SearchView_search_text_color, 0));
        }

        if (a.hasValue(styleable.SearchView_search_text_size)) {
            this.setTextSize(a.getDimension(styleable.SearchView_search_text_size, 0));
        }

        if (a.hasValue(styleable.SearchView_search_text_style)) {
            this.setTextStyle(a.getInt(styleable.SearchView_search_text_style, 0));
        }

        if (a.hasValue(styleable.SearchView_search_hint)) {
            this.setHint(a.getString(styleable.SearchView_search_hint));
        }

        if (a.hasValue(styleable.SearchView_search_hint_color)) {
            this.setHintColor(a.getColor(styleable.SearchView_search_hint_color, 0));
        }

        this.setAnimationDuration(a.getInt(styleable.SearchView_search_animation_duration, this.mContext.getResources().getInteger(integer.search_animation_duration)));
        this.setShadow(a.getBoolean(styleable.SearchView_search_shadow, true));
        this.setShadowColor(a.getColor(styleable.SearchView_search_shadow_color, ContextCompat.getColor(this.mContext, color.search_shadow)));

        if (a.hasValue(styleable.SearchView_search_elevation)) {
            this.setElevation(a.getDimensionPixelSize(styleable.SearchView_search_elevation, 0));
        }

        a.recycle();

        this.setSaveEnabled(true);

        this.mSearchEditText.setVisibility(View.VISIBLE);
    }

    // ---------------------------------------------------------------------------------------------
    @Search.Version
    public int getVersion() {
        return this.mVersion;
    }

    public void setVersion(@Search.Version int version) {
        this.mVersion = version;

        if (this.mVersion == Search.Version.MENU_ITEM) {
            this.setVisibility(View.GONE);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Divider
    @Override
    public void setDividerColor(@ColorInt int color) {
        this.mViewDivider.setBackgroundColor(color);
    }

    // Clear
    public void setClearIcon(@DrawableRes int resource) {
        this.mImageViewClear.setImageResource(resource);
    }

    private void setClearIcon(@Nullable Drawable drawable) {
        this.mImageViewClear.setImageDrawable(drawable);
    }

    @Override
    void setClearColor(@ColorInt int color) {
        this.mImageViewClear.setColorFilter(color);
    }

    // Image
    private void setTextImage(@DrawableRes int resource) {
        this.mImageViewImage.setImageResource(resource);
        this.setTextImageVisibility(false);
    }

    public void setTextImage(@Nullable Drawable drawable) {
        this.mImageViewImage.setImageDrawable(drawable);
        this.setTextImageVisibility(false);
    }

    // Animation duration
    private void setAnimationDuration(long animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    // Shadow
    private void setShadow(boolean shadow) {
        this.mShadow = shadow;
    }

    private void setShadowColor(@ColorInt int color) {
        this.mViewShadow.setBackgroundColor(color);
    }

    // Adapter
    private Adapter getAdapter() {
        return this.mRecyclerView.getAdapter();
    }

    public void setAdapter(Adapter adapter) {
        this.mRecyclerView.setAdapter(adapter);
    }

    /**
     * new SearchDivider(Context)
     * new DividerItemDecoration(Context, DividerItemDecoration.VERTICAL)
     */
    public void addDivider(ItemDecoration itemDecoration) {
        this.mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * new SearchDivider(Context)
     * new DividerItemDecoration(Context, DividerItemDecoration.VERTICAL)
     */
    public void removeDivider(ItemDecoration itemDecoration) {
        this.mRecyclerView.removeItemDecoration(itemDecoration);
    }

    // Others
    public void open(MenuItem menuItem) {
        this.mMenuItem = menuItem;

        switch (this.mVersion) {
            case Search.Version.TOOLBAR:
                this.mSearchEditText.requestFocus();
                break;
            case Search.Version.MENU_ITEM:
                this.setVisibility(View.VISIBLE);

                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    if (this.mMenuItem != null) {
                        this.getMenuItemPosition(this.mMenuItem.getItemId());
                    }
                    this.mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                                SearchView.this.mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                SearchAnimator.revealOpen(
                                        SearchView.this.mContext,
                                        SearchView.this.mCardView,
                                        SearchView.this.mMenuItemCx,
                                        SearchView.this.mAnimationDuration,
                                        SearchView.this.mSearchEditText,
                                        SearchView.this.mOnOpenCloseListener);
                            }
                        }
                    });
                } else {
                    SearchAnimator.fadeOpen(
                            this.mCardView,
                            this.mAnimationDuration,
                            this.mSearchEditText,
                            this.mOnOpenCloseListener);
                }
                break;
        }
    }

    private void setLogoHamburgerToLogoArrowWithAnimation(boolean animate) {
        if (this.mSearchArrowDrawable != null) {
            if (animate) {
                this.mSearchArrowDrawable.setVerticalMirror(false);
                this.mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_ARROW, this.mAnimationDuration);
            } else {
                this.mSearchArrowDrawable.setVerticalMirror(true);
                this.mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_HAMBURGER, this.mAnimationDuration);
            }
        }
    }

    public void setLogoHamburgerToLogoArrowWithoutAnimation(boolean animation) {
        if (this.mSearchArrowDrawable != null) {
            if (animation) {
                this.mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_ARROW);
            } else {
                this.mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_HAMBURGER);
            }
        }
    }

    // Listeners
    public void setOnLogoClickListener(Search.OnLogoClickListener listener) {
        this.mOnLogoClickListener = listener;
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
        return null; // TODO: 25.3.18
    }

}