package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;
import java.util.Locale;


// @CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchView extends SearchLayout implements View.OnClickListener {

    public static final String TAG = SearchView.class.getName();
    private List<Boolean> mSearchFiltersStates;
    private List<SearchFilter> mSearchFilters;
    private View mMenuItemView; // todo
    // init + kotlin 1.2.1 + 4.4.1 + glide 4.4.0 mbuild tools
    private CharSequence mQuery = "";//todo
    private int mMenuItemCx = -1; //todo





    private boolean mShadow;
    private long mAnimationDuration;

    private int mTextStyle = Typeface.NORMAL;
    private Typeface mTextFont = Typeface.DEFAULT;

    @Search.Version
    private int mVersion;
    @Search.VersionMargins
    private int mVersionMargins;

    private MenuItem mMenuItem;
    private View mViewShadow;
    private View mViewDivider;
    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private FlexboxLayout mFlexboxLayout;
    private SearchEditText mSearchEditText;

    private Search.OnLogoClickListener mOnLogoClickListener;
    private Search.OnQueryTextListener mOnQueryTextListener;
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
    public void setHintColor(@ColorInt int color) {
        mSearchEditText.setHintTextColor(color);
    }

    @Override
    public void setTextColor(@ColorInt int color) {
        mSearchEditText.setTextColor(color);
    }

    @Override
    public void setText(@StringRes int text) {
        mSearchEditText.setText(text);
    }

    @Search.Layout
    @Override
    public int getLayout() {
        return Search.Layout.VIEW;
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

    // todo check google search margin
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
    public void setOnLogoClickListener(Search.OnLogoClickListener listener) {
        mOnLogoClickListener = listener;
    }

    public void setOnQueryTextListener(Search.OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    public void setOnOpenCloseListener(Search.OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
    }

    @Size
    public int getCustomHeight() {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        return params.height;
    }

    public void setCustomHeight(@Size int height) {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLinearLayout.setLayoutParams(params);
    }

    public Editable getText() {
        return mSearchEditText.getText();
    }

    @Override
    public void setText(CharSequence text) {
        mSearchEditText.setText(text);
    }

    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        mSearchEditText.setHint(hint);
    }

    public void setImeOptions(int imeOptions) {
        mSearchEditText.setImeOptions(imeOptions);
    }

    public void setInputType(int inputType) {
        mSearchEditText.setInputType(inputType);
    }

    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     */
    public void setTextStyle(int style) {
        mTextStyle = style;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    /**
     * Typeface.DEFAULT
     * Typeface.DEFAULT_BOLD
     * Typeface.MONOSPACE
     * Typeface.SANS_SERIF
     * Typeface.SERIF
     */
    public void setTextFont(Typeface font) {
        mTextFont = font;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    // Icons
    public void setLogoIcon(@DrawableRes int resource) {
        mImageViewLogo.setImageResource(resource);
    }

    public void setLogoIcon(@Nullable Drawable drawable) {
        if (drawable != null) {
            mImageViewLogo.setImageDrawable(drawable);
        } else {
            mImageViewLogo.setVisibility(GONE);
        }
    }

    public void setMicIcon(@DrawableRes int resource) {
        mImageViewMic.setImageResource(resource);
    }

    public void setMicIcon(@Nullable Drawable drawable) {
        mImageViewMic.setImageDrawable(drawable);
    }

    public void setMenuIcon(@DrawableRes int resource) {
        mImageViewMenu.setImageResource(resource);
    }

    public void setMenuIcon(@Nullable Drawable drawable) {
        mImageViewMenu.setImageDrawable(drawable);
    }

    public void setClearIcon(@DrawableRes int resource) {
        mImageViewClear.setImageResource(resource);
    }

    public void setClearIcon(@Nullable Drawable drawable) {
        mImageViewClear.setImageDrawable(drawable);
    }

    public void setClearIconColor(@ColorInt int color) {
        mImageViewClear.setColorFilter(color);
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

    public void setSearchItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerViewAdapter = adapter;
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void setShadow(boolean shadow) {
        mShadow = shadow;
    }

    public void setShadowColor(@ColorInt int color) {
        mViewShadow.setBackgroundColor(color);
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
    }

    public boolean isOpen() {
        return getVisibility() == View.VISIBLE;
    }

    public void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(mSearchEditText, 0); // todo this or edittext
                inputMethodManager.showSoftInput(this, 0);
            }
        }
    }

    public void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // TODO: more Attributes + callsuper + anotace vseho
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        final int layoutResId = a.getResourceId(R.styleable.SearchView_search_layout, R.layout.search_view);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(layoutResId, this, true);

        mCardView = findViewById(R.id.search_cardView);

        mImageViewLogo = findViewById(R.id.search_imageView_logo);
        mImageViewLogo.setOnClickListener(this);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setOnClickListener(this);
        mImageViewMic.setVisibility(View.GONE);

        mImageViewClear = findViewById(R.id.search_imageView_clear);
        mImageViewClear.setOnClickListener(this);
        mImageViewClear.setImageResource(R.drawable.ic_clear_black_24dp);
        mImageViewClear.setVisibility(View.GONE);

        mImageViewMenu = findViewById(R.id.search_imageView_menu);
        mImageViewMenu.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_menu_black_24dp));
        mImageViewMenu.setOnClickListener(this);
        mImageViewMenu.setVisibility(View.GONE);

        mAnimationDuration = mContext.getResources().getInteger(R.integer.search_animation_duration);

        mViewShadow = findViewById(R.id.search_view_shadow);
        mViewShadow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_shadow));
        mViewShadow.setOnClickListener(this);
        mViewShadow.setVisibility(View.GONE);

        mLinearLayout = findViewById(R.id.search_linearLayout);

        mSearchEditText = findViewById(R.id.search_searchEditText);
        mSearchEditText.setSearchView(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchView.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //onSubmitQuery();
                return true;
            }
        });
        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    addFocus();
                } else {
                    removeFocus();
                }
            }
        });

        mViewDivider = findViewById(R.id.search_view_divider);
        mViewDivider.setVisibility(View.GONE);

        mFlexboxLayout = findViewById(R.id.search_flexboxLayout);
        mFlexboxLayout.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.search_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setVisibility(View.GONE);
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
        // todo ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        // todo test PARAMETRY + README
        /*if (a.hasValue(R.styleable.SearchView_search_custom_height)) {
            setCustomHeight(a.getDimensionPixelSize(R.styleable.SearchView_search_custom_height, mContext.getResources().getDimensionPixelSize(R.dimen.search_height)));
        }
        if (a.hasValue(R.styleable.SearchView_search_logo_icon)) {
            setLogoIcon(a.getResourceId(R.styleable.SearchView_search_logo_icon, 0));
        }
        if (a.hasValue(R.styleable.SearchView_search_icon_color)) {
            setIconColor(a.getColor(R.styleable.SearchView_search_icon_color, 0));//Color.BLACK
        }
        if (a.hasValue(R.styleable.SearchView_search_background_color)) {
            setBackgroundColor(a.getColor(R.styleable.SearchView_search_background_color, 0));
        }
        if (a.hasValue(R.styleable.SearchView_search_text_color)) {
            setTextColor(a.getColor(R.styleable.SearchView_search_text_color, 0));
        }
        if (a.hasValue(R.styleable.SearchView_search_text_highlight_color)) {
            setTextHighlightColor(a.getColor(R.styleable.SearchView_search_text_highlight_color, 0));
        }
        if (a.hasValue(R.styleable.SearchView_search_text_size)) {
            setTextSize(a.getDimension(R.styleable.SearchView_search_text_size, mContext.getResources().getDimension(R.dimen.search_text_16)));
        }
        if (a.hasValue(R.styleable.SearchView_search_text_style)) {
            setTextStyle(agetInt(R.styleable.SearchView_search_text_style, Typeface.NORMAL));
        }
        if (a.hasValue(R.styleable.SearchView_search_hint)) {
            setHint(a.getString(R.styleable.SearchView_search_hint));
        }
        if (a.hasValue(R.styleable.SearchView_search_hint_color)) {
            setHintColor(a.getColor(R.styleable.SearchView_search_hint_color, 0));
        }
        if (a.hasValue(R.styleable.SearchView_search_animation_duration)) {
            setAnimationDuration(a.getInt(R.styleable.SearchView_search_animation_duration, 300));
        }
        if (a.hasValue(R.styleable.SearchView_search_shadow)) {
            setShadow(a.getBoolean(R.styleable.SearchView_search_shadow, true));
        }
        if (a.hasValue(R.styleable.SearchView_search_shadow_color)) {
            setShadowColor(a.getColor(R.styleable.SearchView_search_shadow_color, mContext.getResources().getColor(R.color.search_shadow)));
        }

        if (a.hasValue(R.styleable.SearchView_search_elevation)) {
            setElevation(a.getDimensionPixelSize(R.styleable.SearchView_search_elevation, mContext.getResources().getDimensionPixelSize(R.dimen.search_elevation)));
        }*/


        if (a.hasValue(R.styleable.SearchView_search_shadow)) {
            setShadow(a.getBoolean(R.styleable.SearchView_search_shadow, true));
        } else {
            setShadow(true);
        }

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

        if (a.hasValue(R.styleable.SearchView_search_version)) {
            setVersion(a.getInt(R.styleable.SearchView_search_version, Search.Version.TOOLBAR));
        } else {
            setVersion(Search.Version.TOOLBAR);
        }

        if (a.hasValue(R.styleable.SearchView_search_version_margins)) {
            setVersionMargins(a.getInt(R.styleable.SearchView_search_version_margins, Search.VersionMargins.TOOLBAR_SMALL));
        } else {
            setVersionMargins(Search.VersionMargins.TOOLBAR_SMALL);
        }

        a.recycle();
    }

    // ---------------------------------------------------------------------------------------------
    public void open() {
        open(null);
    }

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
                        getMenuItemPosition(menuItem.getItemId());
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

    public void addFocus() {
        if (mShadow) {
            SearchAnimator.fadeIn(mViewShadow, mAnimationDuration);
        }

        setMicOrClearIcon();
        showKeyboard();
        //showSuggestions();

        if (mVersion == Search.Version.TOOLBAR) {
            animateLogoHamburgerToLogoArrow(true);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onOpen();
                    }
                }
            }, mAnimationDuration);
        }
    }

    public void removeFocus() {
        if (mShadow) {
            SearchAnimator.fadeOut(mViewShadow, mAnimationDuration);
        }

        setMicOrClearIcon();
        hideKeyboard();
        //hideSuggestions();

        if (mVersion == Search.Version.TOOLBAR) {
            animateLogoHamburgerToLogoArrow(false);

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

    public void animateLogoHamburgerToLogoArrow(boolean animate) {
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

    public void setLogoHamburgerToLogoArrow(boolean set) {
        if (mSearchArrowDrawable != null) {
            if (set) {
                mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_ARROW);
            } else {
                mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_HAMBURGER);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    private void setMicOrClearIcon() {
        if (!TextUtils.isEmpty(mQuery)) {
            if (mOnMicClickListener != null) {
                mImageViewMic.setVisibility(View.GONE);
            }
            mImageViewClear.setVisibility(View.VISIBLE);
        } else {
            if (mOnMicClickListener != null) {
                mImageViewMic.setVisibility(View.VISIBLE);
            }
            mImageViewClear.setVisibility(View.GONE);
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

    // TODO plus marginy dle searchview
    private void onTextChanged(CharSequence s) {
        mQuery = s;

        setMicOrClearIcon();

        /*if (mRecyclerViewAdapter != null && mRecyclerViewAdapter instanceof Filterable) {
            final CharSequence mFilterKey = s.toString().toLowerCase(Locale.getDefault());
            ((SearchAdapter) mRecyclerViewAdapter).getFilter().filter(s, new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int i) {
                    if (mFilterKey.equals(((SearchAdapter) mRecyclerViewAdapter).getKey())) {
                        if (i > 0) {
                            if (mRecyclerView.getVisibility() == View.GONE) {
                                mViewDivider.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
                            }
                        } else {
                            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                                mViewDivider.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.GONE);
                                SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
                            }
                        }
                    }
                }
            });
        }*/

        if (mOnQueryTextListener != null) {
            // dispatchFilters();
            mOnQueryTextListener.onQueryTextChange(mQuery.toString());
        }
    }


    private void showSuggestions() {
        if (mFlexboxLayout.getChildCount() > 0 && mFlexboxLayout.getVisibility() == View.GONE) {
            mViewDivider.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.VISIBLE);
        }

        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter.getItemCount() > 0) {
            mViewDivider.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
        }
    }

    private void hideSuggestions() {
        if (mFlexboxLayout.getVisibility() == View.VISIBLE) {
            mViewDivider.setVisibility(View.GONE);
            mFlexboxLayout.setVisibility(View.GONE);
        }

        if (mRecyclerViewAdapter != null) {
            mViewDivider.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
        }
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
        }

        if (v == mImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
            }
        }

        if (v == mImageViewClear) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
        }

        if (v == mImageViewMenu) {
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        }

        if (v == mViewShadow) {
            close();
        }
    }

}

    /*


break >= continue return instanceof

        public void setQuery(CharSequence query, boolean submit) {
        mQuery = query;
        mSearchEditText.setText(query);
        mSearchEditText.setSelection(mSearchEditText.length());



        if (submit) {
            //onSubmitQuery();
        }
    }



    public void setFilters(@Nullable List<SearchFilter> filters) {
        mSearchFilters = filters;
        mFlexboxLayout.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            mFlexboxLayout.setVisibility(View.GONE);
        } else {
            mSearchFiltersStates = new ArrayList<>();
            for (SearchFilter filter : filters) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(mContext);
                checkBox.setText(filter.getTitle());
                checkBox.setTextSize(12);
                checkBox.setTextColor(mTextColor);
                checkBox.setChecked(filter.isChecked());

                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.search_filter_margin_start), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top));

                checkBox.setLayoutParams(lp);
                checkBox.setTag(filter.getTagId());
                mFlexboxLayout.addView(checkBox);
                mSearchFiltersStates.add(filter.isChecked());
            }
        }
    }

    public List<SearchFilter> getSearchFilters() {
        if (mSearchFilters == null) {
            return new ArrayList<>();
        }

        dispatchFilters();

        List<SearchFilter> searchFilters = new ArrayList<>();
        for (SearchFilter filter : mSearchFilters) {
            searchFilters.add(new SearchFilter(filter.getTitle(), filter.isChecked(), filter.getTagId()));
        }

        return searchFilters;
    }

    public List<Boolean> getFiltersStates() {
        return mSearchFiltersStates;
    }




        do {
          setShadow(false);
        } while (false);



    public void setTextSize(float size) {
        mSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }




        if (mRecyclerViewAdapter instanceof SearchAdapter) {
            ((SearchAdapter) mRecyclerViewAdapter).setSuggestionsList(suggestionsList);
        }


    private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View view = mFlexboxLayout.getChildAt(i);
            if (view instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }





    //krishkapil filter  listener

    private void isVoiceAvailable() {
        if (isInEditMode()) {
            return;//break continue + krisnha filter listener
        }
    }


    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            dispatchFilters();
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(query);
            }
        }
    }
@FloatRange(from = SearchArrowDrawable.STATE_HAMBURGER, to = SearchArrowDrawable.STATE_ARROW)

    private void dispatchFilters() {
        if (mSearchFiltersStates != null) {
            for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
                View view = mFlexboxLayout.getChildAt(i);
                if (view instanceof AppCompatCheckBox) {
                    boolean isChecked = ((AppCompatCheckBox) view).isChecked();
                    mSearchFiltersStates.set(j, isChecked);
                    mSearchFilters.get(j).setChecked(isChecked);
                    j++;
                }
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());

        bundle.putCharSequence("query", mQuery);
        bundle.putBoolean("isSearchOpen", getVisibility() == View.VISIBLE);

        dispatchFilters();
        ArrayList<Integer> searchFiltersStatesInt = null;
        if (mSearchFiltersStates != null) {
            searchFiltersStatesInt = new ArrayList<>();
            for (Boolean bool : mSearchFiltersStates) {
                searchFiltersStatesInt.add((bool) ? 1 : 0);
            }
        }
        bundle.putIntegerArrayList("searchFiltersStates", searchFiltersStatesInt);

        ArrayList<SearchFilter> searchFilters = null;
        if (mSearchFilters != null) {
            searchFilters = new ArrayList<>();
            searchFilters.addAll(mSearchFilters);
        }
        bundle.putParcelableArrayList("searchFilters", searchFilters);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            mQuery = bundle.getCharSequence("query");
            if (bundle.getBoolean("isSearchOpen")) {
                open(true);
                setQuery(mQuery, false);
                mSearchEditText.requestFocus();
            }

            ArrayList<Integer> searchFiltersStatesInt = bundle.getIntegerArrayList("searchFiltersStates");
            ArrayList<Boolean> searchFiltersStatesBool = null;
            if (searchFiltersStatesInt != null) {
                searchFiltersStatesBool = new ArrayList<>();
                for (Integer value : searchFiltersStatesInt) {
                    searchFiltersStatesBool.add(value == 1);
                }
            }
            restoreFiltersState(searchFiltersStatesBool);

            mSearchFilters = bundle.getParcelableArrayList("searchFilters");

            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
*/
// ---------------------------------------------------------------------------------------------

// @FloatRange(from = SearchArrowDrawable.STATE_HAMBURGER, to = SearchArrowDrawable.STATE_ARROW)


          /*  for (int i = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View child = mFlexboxLayout.getChildAt(i);
            if (child instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) child).setTextColor(mTextColor);
            }
        }*/


// Kotlinize + NULLABLE
/*
todo
or a onFilterClickListener method is fine
*/// int id = view.getId();
// this(context, null);


// aj

        /*if(mRecyclerViewAdapter instanceof SearchAdapter) {
                ((SearchAdapter) mRecyclerViewAdapter).setTextFont(font);
                }*/
/*<declare-styleable name="SearchView2">
<attr name="layout2" format="reference" />
<attr name="android:maxWidth" />
<attr name="queryHint" format="string" />
<attr name="defaultQueryHint" format="string" />
<attr name="android:imeOptions" />
<attr name="android:inputType" />
<attr name="closeIcon" format="reference" />
<attr name="goIcon" format="reference" />
<attr name="searchIcon" format="reference" />
<attr name="searchHintIcon" format="reference" />
<attr name="voiceIcon" format="reference" />
<attr name="commitIcon" format="reference" />
<attr name="suggestionRowLayout" format="reference" />
<attr name="queryBackground" format="reference" />
<attr name="submitBackground" format="reference" />
<attr name="android:focusable" />
</declare-styleable>*/
// ---------------------------------------------------------------------------------------------
    /*@ColorInt
    //@Contract(pure = true)
    public static int getIconColor() {
        return mIconColor;
    }*/

// ontrola anotaci
// https://stackoverflow.com/questions/35625247/android-is-it-ok-to-put-intdef-values-inside-interface
// https://developer.android.com/reference/android/support/annotation/FloatRange.html
// ViewCompat.setBackground
// mSearchButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
