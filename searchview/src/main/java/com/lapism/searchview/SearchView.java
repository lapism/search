package com.lapism.searchview;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SearchView extends FrameLayout implements View.OnClickListener {

    public static final int VERSION_TOOLBAR = 1000;
    public static final int VERSION_MENU_ITEM = 1001;

    public static final int VERSION_MARGINS_TOOLBAR_SMALL = 2000;
    public static final int VERSION_MARGINS_TOOLBAR_BIG = 2001;
    public static final int VERSION_MARGINS_MENU_ITEM = 2002;

    public static final int THEME_PLAY_STORE = 3002;// WHITE
    public static final int THEME_LIGHT = 3000;
    public static final int THEME_DARK = 3001;

    public static final int SPEECH_REQUEST_CODE = 100;
    public static final int LAYOUT_TRANSITION_DURATION = 200;
    public static final int ANIMATION_DURATION = 300;

    public static final int TEXT_STYLE_NORMAL = 0;
    public static final int TEXT_STYLE_BOLD = 1;
    public static final int TEXT_STYLE_ITALIC = 2;
    public static final int TEXT_STYLE_BOLD_ITALIC = 3;

    private static int mIconColor = Color.BLACK;
    private static int mTextColor = Color.BLACK;
    private static int mTextHighlightColor = Color.BLACK;
    private static int mTextStyle = Typeface.NORMAL;
    private static Typeface mTextFont = Typeface.DEFAULT;

    private final Context mContext;
    private final Runnable mShowImeRunnable = () -> {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    };
    private View mMenuItemView = null;
    private Activity mActivity = null;
    private Fragment mFragment = null;
    private android.support.v4.app.Fragment mSupportFragment = null;
    private SearchArrowDrawable mSearchArrow = null;
    private RecyclerView.Adapter mAdapter = null;
    private List<Boolean> mSearchFiltersStates = null;
    private List<SearchFilter> mSearchFilters = null;
    private OnQueryTextListener mOnQueryChangeListener = null;
    private OnOpenCloseListener mOnOpenCloseListener = null;
    private OnMenuClickListener mOnMenuClickListener = null;
    private OnVoiceClickListener mOnVoiceClickListener = null;
    private RecyclerView mRecyclerView;
    private View mShadowView;
    private View mDividerView;
    private CardView mCardView;
    private SearchEditText mSearchEditText;
    private ProgressBar mProgressBar;
    private ImageView mBackImageView;
    private ImageView mVoiceImageView;
    private ImageView mEmptyImageView;
    private FlexboxLayout mFiltersContainer;
    private LinearLayout mLinearLayout;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery = "";
    private String mVoiceText = "Speak now";
    private int mVersion = VERSION_TOOLBAR;
    private int mAnimationDuration = ANIMATION_DURATION;
    private int mMenuItemCx = -1;
    private float mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
    private boolean mShadow = true;
    private boolean mArrow = false;
    private boolean mVoice = false;
    private boolean mIsSearchOpen = false;
    private boolean mShouldClearOnOpen = false;
    private boolean mShouldClearOnClose = false;
    private boolean mShouldHideOnKeyboardClose = true;

    // ---------------------------------------------------------------------------------------------
    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    // ---------------------------------------------------------------------------------------------
    public static int getIconColor() {
        return mIconColor;
    }

    public SearchView setIconColor(@ColorInt int color) {
        mIconColor = color;
        ColorFilter colorFilter = new PorterDuffColorFilter(mIconColor, PorterDuff.Mode.SRC_IN);

        mBackImageView.setColorFilter(colorFilter);
        mVoiceImageView.setColorFilter(colorFilter);
        mEmptyImageView.setColorFilter(colorFilter);
        return this;
    }

    public static int getTextColor() {
        return mTextColor;
    }

    public SearchView setTextColor(@ColorInt int color) {
        mTextColor = color;
        mSearchEditText.setTextColor(mTextColor);
        for (int i = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
            View child = mFiltersContainer.getChildAt(i);
            if (child instanceof CheckBox)
                ((CheckBox) child).setTextColor(mTextColor);
        }
        return this;
    }

    public static int getTextHighlightColor() {
        return mTextHighlightColor;
    }

    public SearchView setTextHighlightColor(@ColorInt int color) {
        mTextHighlightColor = color;
        return this;
    }

    public static Typeface getTextFont() {
        return mTextFont;
    }

    public void setTextFont(Typeface font) {
        mTextFont = font;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    public static int getTextStyle() {
        return mTextStyle;
    }

    public SearchView setTextStyle(int style) {
        mTextStyle = style;
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
        return this;
    }

    private static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    // ---------------------------------------------------------------------------------------------
    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mCardView = (CardView) findViewById(R.id.cardView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard();
                }
            }
        });

        mShadowView = findViewById(R.id.view_shadow);
        mShadowView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_shadow_layout));
        mShadowView.setOnClickListener(this);
        mShadowView.setVisibility(View.GONE);

        mDividerView = findViewById(R.id.view_divider);
        mDividerView.setVisibility(View.GONE);

        mFiltersContainer = (FlexboxLayout) findViewById(R.id.flexboxLayout);
        mFiltersContainer.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mVoiceImageView = (ImageView) findViewById(R.id.imageView_mic);
        mVoiceImageView.setImageResource(R.drawable.ic_mic_black_24dp);
        mVoiceImageView.setOnClickListener(this);
        mVoiceImageView.setVisibility(View.GONE);

        mEmptyImageView = (ImageView) findViewById(R.id.imageView_clear);
        mEmptyImageView.setImageResource(R.drawable.ic_clear_black_24dp);
        mEmptyImageView.setOnClickListener(this);
        mEmptyImageView.setVisibility(View.GONE);

        mSearchEditText = (SearchEditText) findViewById(R.id.searchEditText);
        mSearchEditText.setSearchView(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchView.this.onTextChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSearchEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            onSubmitQuery();
            return true;
        });
        mSearchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                addFocus();
            } else {
                removeFocus();
            }
        });

        setVersion(VERSION_TOOLBAR);

        mSearchArrow = new SearchArrowDrawable(mContext);
        mBackImageView = (ImageView) findViewById(R.id.imageView_arrow);
        mBackImageView.setImageDrawable(mSearchArrow);
        mBackImageView.setOnClickListener(this);

        setVoice(true);
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        final TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, 0);
        if (attr != null) {
            if (attr.hasValue(R.styleable.SearchView_search_height)) {
                setHeightInDP(attr.getDimension(R.styleable.SearchView_search_height, 0));
                // mContext.getResources().getDimension(R.dimen.search_height))
            }
            if (attr.hasValue(R.styleable.SearchView_search_version)) {
                setVersion(attr.getInt(R.styleable.SearchView_search_version, VERSION_TOOLBAR));
            }
            if (attr.hasValue(R.styleable.SearchView_search_version_margins)) {
                setVersionMargins(attr.getInt(R.styleable.SearchView_search_version_margins, VERSION_MARGINS_TOOLBAR_SMALL));
            }
            if (attr.hasValue(R.styleable.SearchView_search_theme)) {
                setTheme(attr.getInt(R.styleable.SearchView_search_theme, THEME_LIGHT));
            }
            if (attr.hasValue(R.styleable.SearchView_search_navigation_icon)) {
                setNavigationIcon(attr.getResourceId(R.styleable.SearchView_search_navigation_icon, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_icon_color)) {
                setIconColor(attr.getColor(R.styleable.SearchView_search_icon_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_background_color)) {
                setBackgroundColor(attr.getColor(R.styleable.SearchView_search_background_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_color)) {
                setTextColor(attr.getColor(R.styleable.SearchView_search_text_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_highlight_color)) {
                setTextHighlightColor(attr.getColor(R.styleable.SearchView_search_text_highlight_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_size)) {
                setTextSize(attr.getDimension(R.styleable.SearchView_search_text_size, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_style)) {
                setTextStyle(attr.getInt(R.styleable.SearchView_search_text_style, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_hint)) {
                setHint(attr.getString(R.styleable.SearchView_search_hint));
            }
            if (attr.hasValue(R.styleable.SearchView_search_hint_color)) {
                setHintColor(attr.getColor(R.styleable.SearchView_search_hint_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_voice)) {
                setVoice(attr.getBoolean(R.styleable.SearchView_search_voice, true));
            }
            if (attr.hasValue(R.styleable.SearchView_search_voice_text)) {
                setVoiceText(attr.getString(R.styleable.SearchView_search_voice_text));
            }
            if (attr.hasValue(R.styleable.SearchView_search_animation_duration)) {
                setAnimationDuration(attr.getInteger(R.styleable.SearchView_search_animation_duration, mAnimationDuration));
            }
            if (attr.hasValue(R.styleable.SearchView_search_shadow)) {
                setShadow(attr.getBoolean(R.styleable.SearchView_search_shadow, true));
            }
            if (attr.hasValue(R.styleable.SearchView_search_shadow_color)) {
                setShadowColor(attr.getColor(R.styleable.SearchView_search_shadow_color, 0)); // TODO
            }
            if (attr.hasValue(R.styleable.SearchView_search_elevation)) {
                setElevation(attr.getDimensionPixelSize(R.styleable.SearchView_search_elevation, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_clear_on_open)) {
                setShouldClearOnOpen(attr.getBoolean(R.styleable.SearchView_search_clear_on_open, false));
            }
            if (attr.hasValue(R.styleable.SearchView_search_clear_on_close)) {
                setShouldClearOnClose(attr.getBoolean(R.styleable.SearchView_search_clear_on_close, true));
            }
            if (attr.hasValue(R.styleable.SearchView_search_hide_on_keyboard_close)) {
                setShouldHideOnKeyboardClose(attr.getBoolean(R.styleable.SearchView_search_hide_on_keyboard_close, true));
            }
            if (attr.hasValue(R.styleable.SearchView_search_cursor_drawable)) {
                setCursorDrawable(attr.getResourceId(R.styleable.SearchView_search_cursor_drawable, 0));
            }
            attr.recycle();
        }
    }

    // ---------------------------------------------------------------------------------------------
    public SearchView setTextOnly(CharSequence text) {
        mSearchEditText.setText(text);
        return this;
    }

    public CharSequence getTextOnly() {
        return mSearchEditText.getText();
    }

    public SearchView setTextOnly(@StringRes int text) {
        mSearchEditText.setText(text);
        return this;
    }

    public SearchView setQuery(CharSequence query, boolean submit) {
        setQueryWithoutSubmitting(query);

        if (!TextUtils.isEmpty(mUserQuery)) {
            mEmptyImageView.setVisibility(View.GONE);
            if (mVoice) {
                mVoiceImageView.setVisibility(View.VISIBLE);
            }
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
        return this;
    }

    public SearchView setQuery(@StringRes int query, boolean submit) {
        return setQuery(String.valueOf(query), submit);
    }

    public CharSequence getQuery() {
        return mSearchEditText.getText();
    }

    public SearchView setHint(@Nullable CharSequence hint) {
        mSearchEditText.setHint(hint);
        return this;
    }

    @Nullable
    public CharSequence getHint() {
        return mSearchEditText.getHint();
    }

    public SearchView setHint(@StringRes int hint) {
        mSearchEditText.setHint(hint);
        return this;
    }

    public int getImeOptions() {
        return mSearchEditText.getImeOptions();
    }

    public SearchView setImeOptions(int imeOptions) {
        mSearchEditText.setImeOptions(imeOptions);
        return this;
    }

    public int getInputType() {
        return mSearchEditText.getInputType();
    }

    public SearchView setInputType(int inputType) {
        mSearchEditText.setInputType(inputType);
        return this;
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public SearchView setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        return this;
    }

    public boolean getShouldClearOnClose() {
        return mShouldClearOnClose;
    }

    public SearchView setShouldClearOnClose(boolean shouldClearOnClose) {
        mShouldClearOnClose = shouldClearOnClose;
        return this;
    }

    public boolean getShouldClearOnOpen() {
        return mShouldClearOnOpen;
    }

    public SearchView setShouldClearOnOpen(boolean shouldClearOnOpen) {
        mShouldClearOnOpen = shouldClearOnOpen;
        return this;
    }

    public boolean getShouldHideOnKeyboardClose() {
        return mShouldHideOnKeyboardClose;
    }

    public SearchView setShouldHideOnKeyboardClose(boolean shouldHideOnKeyboardClose) {
        mShouldHideOnKeyboardClose = shouldHideOnKeyboardClose;
        return this;
    }

    public int getVersion() {
        return mVersion;
    }

    public SearchView setVersion(int version) {
        mVersion = version;

        if (mVersion == VERSION_TOOLBAR) {
            setVisibility(View.VISIBLE);
            mSearchEditText.clearFocus();
        }

        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.GONE);
        }
        return this;
    }

    public SearchView setFilters(@Nullable List<SearchFilter> filters) {
        mSearchFilters = filters;
        mFiltersContainer.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = 0;
            params.bottomMargin = 0;
            mFiltersContainer.setLayoutParams(params);
        } else {
            mSearchFiltersStates = new ArrayList<>();//todo
            /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.filter_margin_top);
            params.bottomMargin = params.topMargin / 2;
            mFiltersContainer.setLayoutParams(params);*/

            for (SearchFilter filter : filters) {
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setText(filter.getTitle());
                checkBox.setTextSize(12);
                checkBox.setTextColor(mTextColor);
                checkBox.setChecked(filter.isChecked());

                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(36,12,24,12);// todo

                checkBox.setLayoutParams(lp);
                checkBox.setTag(filter.getTagId());
                mFiltersContainer.addView(checkBox);
                mSearchFiltersStates.add(filter.isChecked());
            }
        }
        mDividerView.setVisibility(View.VISIBLE);
        mFiltersContainer.setVisibility(View.VISIBLE);
        return this;
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

    public int getHeightInDP() {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        return pxToDp(params.height);
    }

    // TODO GET
    // ---------------------------------------------------------------------------------------------
    public SearchView setHeightInDP(float dp) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLinearLayout.setLayoutParams(params);
        return this;
    }

    public SearchView setVersionMargins(int version) {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        if (version == VERSION_MARGINS_TOOLBAR_SMALL) {
            int top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
            int leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_small_left_right);
            int bottom = 0;

            params.setMargins(leftRight, top, leftRight, bottom);

        } else if (version == VERSION_MARGINS_TOOLBAR_BIG) {
            int top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
            int leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_big_left_right);
            int bottom = 0;

            params.setMargins(leftRight, top, leftRight, bottom);

        } else if (version == VERSION_MARGINS_MENU_ITEM) {
            int top = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);
            int leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin_left_right);
            int bottom = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);

            params.setMargins(leftRight, top, leftRight, bottom);

        } else {
            params.setMargins(0, 0, 0, 0);
        }

        mCardView.setLayoutParams(params);
        return this;
    }

    public SearchView setTheme(int theme) {
        return setTheme(theme, true);
    }

    public SearchView setTheme(int theme, boolean tint) {
        if (theme == THEME_LIGHT) {
            setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            if (tint) {
                setIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
                setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_light_text_highlight));
            }
        }

        if (theme == THEME_DARK) {
            setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            if (tint) {
                setIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
                setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight));
            }
        }

        if (theme == THEME_PLAY_STORE) {
            setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_play_store_background));
            if (tint) {
                setIconColor(ContextCompat.getColor(mContext, R.color.search_play_store_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_play_store_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_play_store_text));
                setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_play_store_text_highlight));
            }
        }

        return this;
    }

    public SearchView setNavigationIcon(@DrawableRes int resource) {
        mBackImageView.setImageResource(resource);
        return this;
    }

    public SearchView setNavigationIcon(Drawable drawable) {
        if (drawable == null) {
            mBackImageView.setVisibility(View.GONE);
        } else {
            mBackImageView.setImageDrawable(drawable);
        }
        return this;
    }

    public SearchView setTextSize(float size) {
        mSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public SearchView setHintColor(@ColorInt int color) {
        mSearchEditText.setHintTextColor(color);
        return this;
    }

    // new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
    // new SearchDivider(mContext)
    public SearchView addDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
        return this;
    }

    // new SearchDivider(mContext)
    public SearchView removeDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
        return this;
    }

    public SearchView setVoice(boolean voice) {
        if (voice && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(View.VISIBLE);
            mVoice = voice;
        } else {
            mVoiceImageView.setVisibility(View.GONE);
            mVoice = voice;
        }
        return this;
    }

    public SearchView setVoice(boolean voice, Activity context) {
        mActivity = context;
        return setVoice(voice);
    }

    public SearchView setVoice(boolean voice, Fragment context) {
        mFragment = context;
        return setVoice(voice);
    }

    public SearchView setVoice(boolean voice, android.support.v4.app.Fragment context) {
        mSupportFragment = context;
        return setVoice(voice);
    }

    public SearchView setVoiceText(String text) {
        mVoiceText = text;
        return this;
    }

    public SearchView setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
        return this;
    }

    public SearchView setShadow(boolean shadow) {
        if (shadow) {
            mShadowView.setVisibility(View.VISIBLE);
        } else {
            mShadowView.setVisibility(View.GONE);
        }
        mShadow = shadow;
        return this;
    }

    public SearchView setShadowColor(@ColorInt int color) {
        mShadowView.setBackgroundColor(color);
        return this;
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
        invalidate();
    }

    // http://stackoverflow.com/questions/11554078/set-textcursordrawable-programatically
    public SearchView setCursorDrawable(@DrawableRes int drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            try {
                f.set(mSearchEditText, drawable);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    // ---------------------------------------------------------------------------------------------
    public SearchView open(boolean animate) {
        open(animate, null);
        return this;
    }

    public SearchView open(boolean animate, MenuItem menuItem) {
        if (mVersion == VERSION_MENU_ITEM) {
            mIsSearchOpen = true;

            setVisibility(View.VISIBLE);

            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (menuItem != null) {
                        getMenuItemPosition(menuItem.getItemId());
                    }
                    reveal();
                } else {
                    SearchAnimator.fadeOpen(
                            mCardView,
                            mAnimationDuration,
                            mSearchEditText,
                            mShouldClearOnOpen,
                            mOnOpenCloseListener);
                }
            } else {
                mCardView.setVisibility(View.VISIBLE);
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
                if (mShouldClearOnOpen && mSearchEditText.length() > 0) {
                    mSearchEditText.getText().clear();
                }
                mSearchEditText.requestFocus();
            }
        }
        if (mVersion == VERSION_TOOLBAR) {
            mIsSearchOpen = true;
            if (mShouldClearOnOpen && mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
            mSearchEditText.requestFocus();
        }
        return this;
    }

    public SearchView close(boolean animate) {
        if (mVersion == VERSION_MENU_ITEM) {
            mIsSearchOpen = false;
            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SearchAnimator.revealClose(
                            mCardView,
                            mMenuItemCx,
                            mAnimationDuration,
                            mContext,
                            mSearchEditText,
                            mShouldClearOnClose,
                            this,
                            mOnOpenCloseListener);
                } else {
                    SearchAnimator.fadeClose(
                            mCardView,
                            mAnimationDuration,
                            mSearchEditText,
                            mShouldClearOnClose,
                            this,
                            mOnOpenCloseListener);
                }
            } else {
                if (mShouldClearOnClose && mSearchEditText.length() > 0) {
                    mSearchEditText.getText().clear();
                }
                mSearchEditText.clearFocus();
                mCardView.setVisibility(View.GONE);
                setVisibility(View.GONE);
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onClose();
                }
            }
        }

        if (mVersion == VERSION_TOOLBAR) {
            mIsSearchOpen = false;
            if (mShouldClearOnClose && mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
            mSearchEditText.clearFocus();
        }
        return this;
    }

    public SearchView addFocus() {

        if (mArrow) {
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_ARROW;
        } else {
            setArrow();
        }
        if (mShadow) {
            SearchAnimator.fadeIn(mShadowView, mAnimationDuration);
        }
        if (mVersion == VERSION_TOOLBAR) {
            postDelayed(() -> {
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
            }, mAnimationDuration);
        }
        showKeyboard();

        // TODO
        if (!TextUtils.isEmpty(mUserQuery)) {
            mEmptyImageView.setVisibility(View.VISIBLE);
            if (mVoice) {
                mVoiceImageView.setVisibility(View.GONE);
            }
        }
        showSuggestions();
        return this;
    }

    public SearchView removeFocus() {

        if (mArrow) {
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
        } else {
            setHamburger();
        }
        if (mShadow) {
            SearchAnimator.fadeOut(mShadowView, mAnimationDuration);
        }
        if (mVersion == VERSION_TOOLBAR) {
            postDelayed(() -> {
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onClose();
                }
            }, mAnimationDuration);
        }
        hideKeyboard();

        // TODO
        if (!TextUtils.isEmpty(mUserQuery)) {
            mEmptyImageView.setVisibility(View.GONE);
            if (mVoice) {
                mVoiceImageView.setVisibility(View.VISIBLE);
            }
        }
        hideSuggestions();
        return this;
    }

    public SearchView showSuggestions() {
        if (mAdapter != null && mAdapter.getItemCount() > 0) { // ||
            mDividerView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
        }

        if (mFiltersContainer.getChildCount() > 0 && mFiltersContainer.getVisibility() == View.GONE) {
            mDividerView.setVisibility(View.VISIBLE);
            mFiltersContainer.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public SearchView hideSuggestions() {
        if (mAdapter != null) {
            mDividerView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
        }

        if (mFiltersContainer.getVisibility() == View.VISIBLE) {
            mDividerView.setVisibility(View.GONE);
            mFiltersContainer.setVisibility(View.GONE);
        }
        return this;
    }

    public SearchView setArrowOnly(boolean animate) {
        if (animate) {
            if (mSearchArrow != null) {
                mSearchArrow.setVerticalMirror(false);
                mSearchArrow.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
            }
        } else {
            mBackImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        }
        mArrow = true;
        return this;
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen; // getVisibility();
    }

    public SearchView showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(mSearchEditText, 0);
            inputManager.showSoftInput(this, 0);
        }
        return this;
    }

    public SearchView hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return this;
    }

    public SearchView showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        return this;
    }

    public SearchView hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        return this;
    }

    public boolean isShowingProgress() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    public SearchView setGoogleIcons() {
        // mBackImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_logo));
        // mVoiceImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic));
        return this;
    }

    // ---------------------------------------------------------------------------------------------
    private void onTextChanged(CharSequence newText) {
        if (newText.equals(mOldQueryText)) {
            return;
        }
        CharSequence text = mSearchEditText.getText();
        mUserQuery = text;

        if (mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable) mAdapter).getFilter().filter(text);
        }

        if (!TextUtils.isEmpty(mUserQuery)) {
            showSuggestions();
            mEmptyImageView.setVisibility(View.VISIBLE);
            if (mVoice) {
                mVoiceImageView.setVisibility(View.GONE);
            }
        } else {
            hideSuggestions();
            mEmptyImageView.setVisibility(View.GONE);
            if (mVoice) {
                mVoiceImageView.setVisibility(View.VISIBLE);
            }
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            dispatchFilters();
            mOnQueryChangeListener.onQueryTextChange(text.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void setQueryWithoutSubmitting(CharSequence query) {
        mSearchEditText.setText(query);
        if (query != null) {
            mSearchEditText.setSelection(mSearchEditText.length());
            mUserQuery = query;
        } else {
            mSearchEditText.getText().clear(); // mSearchEditText.setText("");
        }
    }

    private void setArrow() {
        if (mSearchArrow != null) {
            mSearchArrow.setVerticalMirror(false);
            mSearchArrow.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_ARROW;
        }
    }

    private void setHamburger() {
        if (mSearchArrow != null) {
            mSearchArrow.setVerticalMirror(true);
            mSearchArrow.animate(SearchArrowDrawable.STATE_HAMBURGER, mAnimationDuration);
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
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

    private LayoutTransition getRecyclerViewLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(LAYOUT_TRANSITION_DURATION);
        return layoutTransition;
    }

    private void onVoiceClicked() {
        if (mOnVoiceClickListener != null) {
            mOnVoiceClickListener.onVoiceClick();
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mVoiceText);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        if (mActivity != null) {
            mActivity.startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else if (mSupportFragment != null) {
            mSupportFragment.startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else {
            if (mContext instanceof Activity) {
                ((Activity) mContext).startActivityForResult(intent, SPEECH_REQUEST_CODE);
            }
        }
    }

    private void setInfo() {
        mVoice = isVoiceAvailable();
        if (mVoice) {
            mSearchEditText.setPrivateImeOptions("nm");
        }
    }

    private boolean isVoiceAvailable() {
        if (isInEditMode()) {
            return true;
        }
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    private void setImeVisibility(boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            dispatchFilters();
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(query);
            }
        }
    }

    private int getCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
    }

    private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
            View view = mFiltersContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }

    private void dispatchFilters() {
        if (mSearchFiltersStates != null) {
            for (int i = 0, j = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
                View view = mFiltersContainer.getChildAt(i);
                if (view instanceof CheckBox) {
                    boolean isChecked = ((CheckBox) view).isChecked();
                    mSearchFiltersStates.set(j, isChecked);
                    mSearchFilters.get(j).setChecked(isChecked);
                    j++;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void reveal() {
        mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SearchAnimator.revealOpen(mCardView, mMenuItemCx, mAnimationDuration, mContext, mSearchEditText, mShouldClearOnOpen, mOnOpenCloseListener);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (v == mBackImageView) {
            if (mSearchArrow != null && mIsSearchArrowHamburgerState == SearchArrowDrawable.STATE_ARROW) {
                close(true);
            } else {
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onMenuClick();
                }
            }
        } else if (v == mVoiceImageView) {
            onVoiceClicked();
        } else if (v == mEmptyImageView) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
        } else if (v == mShadowView) {
            close(true);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.query = mUserQuery != null ? mUserQuery.toString() : null;
        ss.isSearchOpen = mIsSearchOpen;
        dispatchFilters();
        ss.searchFiltersStates = mSearchFiltersStates;
        ss.searchFilters = mSearchFilters;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        if (ss.isSearchOpen) {
            open(true);
            setQueryWithoutSubmitting(ss.query); // TODO
            mSearchEditText.requestFocus();
        }

        restoreFiltersState(ss.searchFiltersStates);
        mSearchFilters = ss.searchFilters;
        super.onRestoreInstanceState(ss.getSuperState());
        requestLayout();
    }

    // ---------------------------------------------------------------------------------------------
    public SearchView setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
        return this;
    }

    public SearchView setOnOpenCloseListener(OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
        return this;
    }

    public SearchView setOnMenuClickListener(OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
        return this;
    }

    public SearchView setOnVoiceClickListener(OnVoiceClickListener listener) {
        mOnVoiceClickListener = listener;
        return this;
    }

    // ---------------------------------------------------------------------------------------------
    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface OnOpenCloseListener {
        boolean onClose();

        boolean onOpen();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    public interface OnVoiceClickListener {
        void onVoiceClick();
    }

    // ---------------------------------------------------------------------------------------------
    private static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        String query;
        boolean isSearchOpen;
        List<Boolean> searchFiltersStates;
        List<SearchFilter> searchFilters;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel source) {
            super(source);
            this.query = source.readString();
            this.isSearchOpen = source.readInt() == 1;
            source.readList(searchFiltersStates, List.class.getClassLoader());
            searchFilters = new ArrayList<>();
            source.readTypedList(searchFilters, SearchFilter.CREATOR);
        }

        @TargetApi(Build.VERSION_CODES.N)
        SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.query = source.readString();
            this.isSearchOpen = source.readInt() == 1;
            source.readList(searchFiltersStates, List.class.getClassLoader());
            searchFilters = new ArrayList<>();
            source.readTypedList(searchFilters, SearchFilter.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
            out.writeList(searchFiltersStates);
            out.writeTypedList(searchFilters);
        }

    }

}
