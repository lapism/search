package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.lapism.searchview.R;
import com.lapism.searchview.Search;
import com.lapism.searchview.graphics.SearchAnimator;
import com.lapism.searchview.graphics.SearchArrowDrawable;

import java.util.List;


// @CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchView extends SearchLayout implements View.OnClickListener, Filter.FilterListener {


    public static final String TAG = SearchView.class.getName();
    private List<Boolean> mSearchFiltersStates;
    private List<SearchFilter> mSearchFilters;


    // init + kotlin 1.2.1 + 4.4.1 + glide 4.5.0 mbuild tools BETA4 427.0.3 / 02

    private View mMenuItemView; // todo
    private int mMenuItemCx = -1; // todo


    private boolean mShadow;
    private long mAnimationDuration;
    private CharSequence mQueryText = "";

    @Search.Version
    private int mVersion;
    @Search.VersionMargins
    private int mVersionMargins;

    private ImageView mImageViewImage;
    private MenuItem mMenuItem;
    private View mViewShadow;
    private View mViewDivider;
    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private FlexboxLayout mFlexboxLayout;

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

    public static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    @Override
    public void setHint(@StringRes int hint) {
        mSearchEditText.setHint(hint);
    }

    @Override
    public void setHintColor(@ColorInt int color) {
        mSearchEditText.setHintTextColor(color);
    }

    @Override
    public void setTextStyle(int style) {
        mTextStyle = style;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    @Override
    public void setTextFont(Typeface font) {
        mTextFont = font;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    @Override
    protected boolean isView() {
        return true;
    }

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

    public void setMenuIcon(@DrawableRes int resource) {
        mImageViewMenu.setImageResource(resource);
    }

    public void setMenuIcon(@Nullable Drawable drawable) {
        mImageViewMenu.setImageDrawable(drawable);
    }

    public void setTextImage(@DrawableRes int resource) {
        mImageViewImage.setImageResource(resource);
        setTextImageVisibility(false);
    }

    public void setTextImage(@Nullable Drawable drawable) {
        mImageViewImage.setImageDrawable(drawable);
        setTextImageVisibility(false);
    }

    public Editable getText() {
        return mSearchEditText.getText();
    }

    public void setText(CharSequence text) {
        mSearchEditText.setText(text);
    }

    public void setText(@StringRes int text) {
        mSearchEditText.setText(text);
    }

    @Override
    public void setTextColor(@ColorInt int color) {
        mSearchEditText.setTextColor(color);
    }

    public void setTextSize(float size) {
        mSearchEditText.setTextSize(size);
    }

    public void setImeOptions(int imeOptions) {
        mSearchEditText.setImeOptions(imeOptions);
    }

    public void setInputType(int inputType) {
        mSearchEditText.setInputType(inputType);
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

    /**
     * In this library >>
     * R.drawable.round_background_top
     */
    @Override
    public void setBackgroundResource(int resid) {
        mCardView.setBackgroundResource(resid);
    }

    /**
     * In this library >>
     * R.drawable.round_background_top
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setBackground(Drawable background) {
        mCardView.setBackground(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        mCardView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
    }

    public int getCustomHeight() {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        return params.height;
    }

    public void setCustomHeight(int height) {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLinearLayout.setLayoutParams(params);
    }

    // INT VERSION
    public Editable getQuery() {
        return mSearchEditText.getText();
    }

    private void setQuery(CharSequence query) {
        mSearchEditText.setText(query);
        mSearchEditText.setSelection(TextUtils.isEmpty(query) ? 0 : query.length());
    }

    public void setQuery(CharSequence query, boolean submit) {
        mSearchEditText.setText(query);
        if (query != null) {
            mSearchEditText.setSelection(mSearchEditText.length());
            mQueryText = query;
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setQueryHint(@Nullable CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    @Nullable
    public CharSequence getQueryHint() {
        return mSearchEditText.getHint();
    }

    public void setQueryHint(@StringRes int hint) {
        mSearchEditText.setHint(hint);
    }

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

    public void setOnLogoClickListener(Search.OnLogoClickListener listener) {
        mOnLogoClickListener = listener;
    }

    public void setOnQueryTextListener(Search.OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    public void setOnOpenCloseListener(Search.OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
    }

    public boolean isOpen() {
        return getVisibility() == View.VISIBLE;
    }

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
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        final int layoutResId = R.layout.search_view;

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(layoutResId, this, true);

        mCardView = findViewById(R.id.search_cardView);

        mImageViewLogo = findViewById(R.id.search_imageView_logo);
        mImageViewLogo.setOnClickListener(this);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setOnClickListener(this);

        mImageViewClear = findViewById(R.id.search_imageView_clear);
        mImageViewClear.setOnClickListener(this);

        mImageViewMenu = findViewById(R.id.search_imageView_menu);
        mImageViewMenu.setOnClickListener(this);

        mImageViewImage = findViewById(R.id.search_imageView_image);
        mImageViewImage.setOnClickListener(this);

        mViewShadow = findViewById(R.id.search_view_shadow);
        mViewShadow.setOnClickListener(this);

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
                onSubmitQuery();
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
        mFlexboxLayout = findViewById(R.id.search_flexboxLayout);

        mRecyclerView = findViewById(R.id.search_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new SearchItemAnimator());
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

        mImageViewMic.setVisibility(View.GONE);
        mImageViewClear.setVisibility(View.GONE);
        mImageViewMenu.setVisibility(View.GONE);
        mImageViewImage.setVisibility(View.GONE);
        mViewShadow.setVisibility(View.GONE);
        mViewDivider.setVisibility(View.GONE);
        mFlexboxLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        setLogo(a.getInteger(R.styleable.SearchView_search_logo, Search.Logo.G));
        setShape(a.getInteger(R.styleable.SearchView_search_shape, Search.Shape.CLASSIC));
        setTheme(a.getInteger(R.styleable.SearchView_search_theme, Search.Theme.COLOR));
        setVersion(a.getInteger(R.styleable.SearchView_search_version, Search.Version.TOOLBAR));
        setVersionMargins(a.getInteger(R.styleable.SearchView_search_version_margins, Search.VersionMargins.TOOLBAR_SMALL));

        if (a.hasValue(R.styleable.SearchView_search_logo_icon)) {
            setLogoIcon(a.getInteger(R.styleable.SearchView_search_logo_icon, 0)); // todo maybe bug + test + check every attribute
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
            setClearIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_clear_black_24dp));
        }

        if (a.hasValue(R.styleable.SearchView_search_clear_color)) {
            setClearColor(a.getColor(R.styleable.SearchView_search_clear_color, 0));
        }

        if (a.hasValue(R.styleable.SearchView_search_menu_icon)) {
            setMenuIcon(a.getResourceId(R.styleable.SearchView_search_menu_icon, 0));
        } else {
            setMenuIcon(R.drawable.ic_menu_black_24dp);
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

        setSaveEnabled(true);// TODO
    }

    // todo test VSEHO  + README + Color.BLACK
    // TODO + callsuper + anotace vseho
    // todo room + check google search margin
    // TODO PROMYSLET
    private void addFocus() {
        filter(mQueryText);

        if (mShadow) {
            SearchAnimator.fadeOpen(mViewShadow, mAnimationDuration);
        }

        showSuggestions();
        setMicOrClearIcon(true);

        if (mVersion == Search.Version.TOOLBAR) {
            animateLogoHamburgerToLogoArrow(true);

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

    private void removeFocus() {
        if (mShadow) {
            SearchAnimator.fadeClose(mViewShadow, mAnimationDuration);
        }

        setTextImageVisibility(false);
        hideSuggestions();
        hideKeyboard();
        setMicOrClearIcon(false);

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
        if (mImageViewImage.getDrawable() != null) {
            if (hasFocus) {
                mImageViewImage.setVisibility(View.GONE);
                mSearchEditText.setVisibility(View.VISIBLE);
                mSearchEditText.requestFocus();
            } else {
                mSearchEditText.setVisibility(View.GONE);
                mImageViewImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onTextChanged(CharSequence s) {
        mQueryText = s;

        setMicOrClearIcon(true);
        filter(s);

        if (mOnQueryTextListener != null) {
            // dispatchFilters(); todo
            mOnQueryTextListener.onQueryTextChange(mQueryText);
        }
    }

    private void filter(CharSequence s) {
        if (getAdapter() != null && getAdapter() instanceof Filterable) {
            ((Filterable) getAdapter()).getFilter().filter(s, this);
        }
    }

    private void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(mSearchEditText, 0);
            }
        }
    }

    private void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
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

    /*void onTextChanged2(CharSequence newText) {
        CharSequence text = mSearchEditText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }*/

    // slideDown TODO
    // TODO plus marginy dle searchview + check
    // todo
    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            // dispatchFilters(); todo
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(query);
                // dismissSuggestions();
            }
        }
    }

    // todo
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

    // todo
    private int getCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
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

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SearchViewSavedState ss = new SearchViewSavedState(superState);
        ss.hasFocus = mSearchEditText.hasFocus();
        ss.shadow = mShadow;
        ss.query = mQueryText != null ? mQueryText.toString() : null;// todo ""
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

}

    /*dispatchFilters();
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
        bundle.putParcelableArrayList("searchFilters", searchFilters);*/




        /*if (state instanceof Bundle) {
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
        }*/

    /*
        public void setQuery(CharSequence query, boolean submit) {
        mQuery = query;
        mSearchEditText.setText(query);
        mSearchEditText.setSelection(mSearchEditText.length());



        if (submit) {
            //onSubmitQuery();
        }
    }


//callsuper
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





        //break >= continue return instanceof + << atd
        // ViewCompat.setBackground(mSearchPlate,
        // AppCompatResources.getDrawable()
        // a.getType(R.styleable.SearchView_search_menu_color);
        // ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);




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


// todo https://lab.getbase.com/nested-scrolling-with-coordinatorlayout-on-android/
// ColorUtils.setAlphaComponent(SearchView.getIconColor(), 0x33)android:alpha="0.4",

/*
                /*if (mSearchEditText.length() > 0) {
            mSearchEditText.getText().clear();
        }*/
        /*if(!TextUtils.isEmpty(mQuery)) {
            mSearchEditText.setText(mQuery);
        }*/
// SearchEditText.setVisibility(View.VISIBLE); todo bug a mizi text, hidesuggestion
