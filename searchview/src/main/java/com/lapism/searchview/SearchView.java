package com.lapism.searchview;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// import android.support.v7.widget.DefaultItemAnimator;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchView extends FrameLayout implements View.OnClickListener {

    public static final int VERSION_TOOLBAR = 1000;
    public static final int VERSION_MENU_ITEM = 1001;

    public static final int VERSION_MARGINS_TOOLBAR_SMALL = 2000;
    public static final int VERSION_MARGINS_TOOLBAR_BIG = 2001;
    public static final int VERSION_MARGINS_MENU_ITEM = 2002;

    public static final int THEME_LIGHT = 3000;
    public static final int THEME_DARK = 3001;

    public static final int SPEECH_REQUEST_CODE = 4000;

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
    protected OnQueryTextListener mOnQueryChangeListener = null;
    protected OnOpenCloseListener mOnOpenCloseListener = null;
    protected OnMenuClickListener mOnMenuClickListener = null;
    protected OnVoiceClickListener mOnVoiceClickListener = null;
    protected Activity mActivity = null;
    protected Fragment mFragment = null;
    protected android.support.v4.app.Fragment mSupportFragment = null;
    protected SearchArrowDrawable mSearchArrow = null;
    protected RecyclerView.Adapter mAdapter = null;
    protected RecyclerView mRecyclerView;
    protected View mShadowView;
    protected View mDividerView;
    protected CardView mCardView;
    protected SearchEditText mEditText;
    protected ProgressBar mProgressBar;
    protected ImageView mBackImageView;
    protected ImageView mVoiceImageView;
    protected ImageView mEmptyImageView;
    protected LinearLayout mFiltersContainer;
    protected LinearLayout mLinearLayout;
    protected String mVoiceText = "Speak now";
    protected int mVersion = VERSION_TOOLBAR;
    protected int mAnimationDuration = ANIMATION_DURATION;
    protected float mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
    protected boolean mShadow = true;
    protected boolean mVoice = true;
    protected boolean mIsSearchOpen = false;
    protected List<Boolean> mSearchFiltersStates = null;
    protected SavedState mSavedState;
    protected CharSequence mOldQueryText;
    private CharSequence mUserQuery = "";
    private boolean mShouldClearOnClose = true;
    private boolean mShouldClearOnOpen = true;
    private boolean mShouldHideOnKeyboardClose = true;
    private View mMenuItemView = null;
    private int mMenuItemCx = -1;

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

    public void setIconColor(@ColorInt int color) {
        mIconColor = color;

        ColorFilter colorFilter = new PorterDuffColorFilter(mIconColor, PorterDuff.Mode.SRC_IN);

        mBackImageView.setColorFilter(colorFilter);
        mVoiceImageView.setColorFilter(colorFilter);
        mEmptyImageView.setColorFilter(colorFilter);

        if (mSearchArrow != null) {
            mSearchArrow.setColorFilter(colorFilter);
        }
    }

    public static int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        mEditText.setTextColor(mTextColor);
        for (int i = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
            View child = mFiltersContainer.getChildAt(i);
            if (child instanceof AppCompatCheckBox)
                ((AppCompatCheckBox) child).setTextColor(mTextColor);
        }
    }

    public static int getTextHighlightColor() {
        return mTextHighlightColor;
    }

    public void setTextHighlightColor(@ColorInt int color) {
        mTextHighlightColor = color;
    }

    public static Typeface getTextFont() {
        return mTextFont;
    }

    public void setTextFont(Typeface font) {
        mTextFont = font;
        mEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    public static int getTextStyle() {
        return mTextStyle;
    }

    public void setTextStyle(int style) {
        mTextStyle = style;
        mEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    // ---------------------------------------------------------------------------------------------
    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mCardView = (CardView) findViewById(R.id.cardView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_result);
        // mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutTransition(getRecyclerViewLayoutTransition());
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mRecyclerView.setLayoutTransition(null);
                    hideKeyboard();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mRecyclerView.setLayoutTransition(getRecyclerViewLayoutTransition());
                    }
                }
            }
        });

        mDividerView = findViewById(R.id.view_divider);
        mDividerView.setVisibility(View.GONE);

        mShadowView = findViewById(R.id.view_shadow);
        mShadowView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_shadow_layout));
        mShadowView.setOnClickListener(this);
        mShadowView.setVisibility(View.GONE);

        mBackImageView = (ImageView) findViewById(R.id.imageView_arrow_back);
        mBackImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        mBackImageView.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mVoiceImageView = (ImageView) findViewById(R.id.imageView_mic);
        mVoiceImageView.setImageResource(R.drawable.ic_mic_black_24dp);
        mVoiceImageView.setOnClickListener(this);

        mEmptyImageView = (ImageView) findViewById(R.id.imageView_clear);
        mEmptyImageView.setImageResource(R.drawable.ic_clear_black_24dp);
        mEmptyImageView.setOnClickListener(this);
        mEmptyImageView.setVisibility(View.GONE);

        mEditText = (SearchEditText) findViewById(R.id.searchEditText_input);
        mEditText.setSearchView(this);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
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
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    addFocus();
                } else {
                    removeFocus();
                }
            }
        });

        mFiltersContainer = (LinearLayout) findViewById(R.id.filters_container);

        setVersion(VERSION_TOOLBAR);
        setVersionMargins(VERSION_MARGINS_TOOLBAR_SMALL);
        setTheme(THEME_LIGHT);
        setVoice(true);
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        final TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, 0);
        if (attr != null) {
            if (attr.hasValue(R.styleable.SearchView_search_height)) {
                setHeight(attr.getDimension(R.styleable.SearchView_search_height, 0));
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
            if (attr.hasValue(R.styleable.SearchView_search_text_input)) {
                setTextInput(attr.getString(R.styleable.SearchView_search_text_input));
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
            if (attr.hasValue(R.styleable.SearchView_search_divider)) {
                setDivider(attr.getBoolean(R.styleable.SearchView_search_divider, false));
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
                setShadowColor(attr.getColor(R.styleable.SearchView_search_shadow_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_elevation)) {
                setElevation(attr.getDimensionPixelSize(R.styleable.SearchView_search_elevation, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_clear_on_close)) {
                setShouldClearOnClose(attr.getBoolean(R.styleable.SearchView_search_clear_on_close, true));
            }
            if (attr.hasValue(R.styleable.SearchView_search_clear_on_open)) {
                setShouldClearOnOpen(attr.getBoolean(R.styleable.SearchView_search_clear_on_open, true));
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

    public int getVersion() {
        return mVersion;
    }

    public void setVersion(int version) {
        mVersion = version;
        mFiltersContainer.setVisibility(View.GONE);

        if (mVersion == VERSION_TOOLBAR) {
            setVisibility(View.VISIBLE);
            mEditText.clearFocus();
        }

        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.GONE);
        }
    }

    // ---------------------------------------------------------------------------------------------
    public void setHeight(float dp) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLinearLayout.setLayoutParams(params);
    }

    public void setVersionMargins(int version) {
        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
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
    }

    public void setTheme(int theme) {
        setTheme(theme, true);
    }

    public void setTheme(int theme, boolean tint) {
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
    }

    public void setNavigationIcon(@DrawableRes int resource) {
        mBackImageView.setImageResource(resource);
    }

    public void setNavigationIcon(Drawable drawable) {
        if (drawable == null) {
            mBackImageView.setVisibility(View.GONE);
        } else {
            mBackImageView.setImageDrawable(drawable);
        }
    }

    public void setNavigationIconArrowHamburger() {
        mSearchArrow = new SearchArrowDrawable(mContext);
        mBackImageView.setImageDrawable(mSearchArrow);
    }

    //@IntR
    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setTextInput(CharSequence text) {
        mEditText.setText(text);
    }

    public void setTextInput(@StringRes int text) {
        mEditText.setText(text);
    }

    public void setTextSize(float size) {
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setHint(CharSequence hint) {
        mEditText.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        mEditText.setHint(hint);
    }

    public void setHintColor(@ColorInt int color) {
        mEditText.setHintTextColor(color);
    }

    public void setDivider(boolean divider) {
        if (divider) {
            mRecyclerView.addItemDecoration(new SearchDivider(mContext));
        } else {
            mRecyclerView.removeItemDecoration(new SearchDivider(mContext));
        }
    }

    public void setVoice(boolean voice) {
        mVoice = voice;
        if (voice && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(View.VISIBLE);
        } else {
            mVoiceImageView.setVisibility(View.GONE);
        }
    }

    public void setVoice(boolean voice, Activity context) {
        mActivity = context;
        setVoice(voice);
    }

    public void setVoice(boolean voice, Fragment context) {
        mFragment = context;
        setVoice(voice);
    }

    public void setVoice(boolean voice, android.support.v4.app.Fragment context) {
        mSupportFragment = context;
        setVoice(voice);
    }

    public void setVoiceText(String text) {
        mVoiceText = text;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void setShadow(boolean shadow) {
        if (shadow) {
            mShadowView.setVisibility(View.VISIBLE);
        } else {
            mShadowView.setVisibility(View.GONE);
        }
        mShadow = shadow;
    }

    public void setShadowColor(@ColorInt int color) {
        mShadowView.setBackgroundColor(color);
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
        invalidate();
    }

    public boolean getShouldClearOnClose() {
        return mShouldClearOnClose;
    }

    public void setShouldClearOnClose(boolean shouldClearOnClose) {
        mShouldClearOnClose = shouldClearOnClose;
    }

    public boolean getShouldClearOnOpen() {
        return mShouldClearOnOpen;
    }

    public void setShouldClearOnOpen(boolean shouldClearOnOpen) {
        mShouldClearOnOpen = shouldClearOnOpen;
    }

    public boolean getShouldHideOnKeyboardClose() {
        return mShouldHideOnKeyboardClose;
    }

    public void setShouldHideOnKeyboardClose(boolean shouldHideOnKeyboardClose) {
        mShouldHideOnKeyboardClose = shouldHideOnKeyboardClose;
    }

    // more here: http://stackoverflow.com/questions/11554078/set-textcursordrawable-programatically
    public void setCursorDrawable(@DrawableRes int drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            try {
                f.set(mEditText, drawable);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------------------------
        /*
    private void setQuery2(CharSequence query) {
        mEditText.setText(query);
        mEditText.setSelection(TextUtils.isEmpty(query) ? 0 : query.length());
    }
    */

    public CharSequence getQuery() {
        return mUserQuery;
    }

    public void setQuery(CharSequence query) {
        setQueryWithoutSubmitting(query);

        if (!TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    private void setQueryWithoutSubmitting(CharSequence query) {
        mEditText.setText(query);
        if (query != null) {
            mEditText.setSelection(mEditText.length());
            mUserQuery = query;
        } else {
            mEditText.getText().clear();
        }
    }

    private LayoutTransition getRecyclerViewLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(LAYOUT_TRANSITION_DURATION);
        return layoutTransition;
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter instanceof SearchAdapter)
            ((SearchAdapter) mAdapter).addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    dispatchFilters();
                }
            }, 0);
    }

    public void open(boolean animate) {
        open(animate, null);
    }

    public void open(boolean animate, MenuItem menuItem) {
        mFiltersContainer.setVisibility(View.VISIBLE);
        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.VISIBLE);

            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (menuItem != null)
                        getMenuItemPosition(menuItem.getItemId());
                    reveal();
                } else {
                    SearchAnimator.fadeOpen(mCardView, mAnimationDuration, mEditText, mShouldClearOnOpen, mOnOpenCloseListener);
                }
            } else {
                mCardView.setVisibility(View.VISIBLE);
                if (mShouldClearOnOpen && mEditText.length() > 0) {
                    mEditText.getText().clear();
                }
                mEditText.requestFocus();
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
            }
        }
        if (mVersion == VERSION_TOOLBAR) {
            if (mShouldClearOnOpen && mEditText.length() > 0) {
                mEditText.getText().clear();
            }
            mEditText.requestFocus();
        }
    }

    public void close(boolean animate) {
        mFiltersContainer.setVisibility(View.GONE);
        if (mVersion == VERSION_MENU_ITEM) {
            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SearchAnimator.revealClose(mCardView, mMenuItemCx, mAnimationDuration, mContext, mEditText, mShouldClearOnClose, this, mOnOpenCloseListener);
                } else {
                    SearchAnimator.fadeClose(mCardView, mAnimationDuration, mEditText, mShouldClearOnClose, this, mOnOpenCloseListener);
                }
            } else {
                if (mShouldClearOnClose && mEditText.length() > 0) {
                    mEditText.getText().clear();
                }
                mEditText.clearFocus();
                mCardView.setVisibility(View.GONE);
                setVisibility(View.GONE);
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onClose();
                }
            }
        }
        if (mVersion == VERSION_TOOLBAR) {
            if (mShouldClearOnClose && mEditText.length() > 0) {
                mEditText.getText().clear();
            }
            mEditText.clearFocus();
        }
    }

    public void addFocus() {
        mIsSearchOpen = true;
        setArrow();
        showSuggestions();
        if (mShadow) {
            SearchAnimator.fadeIn(mShadowView, mAnimationDuration);
        }
        showKeyboard();
        showClearTextIcon();
        if (mVersion != VERSION_MENU_ITEM) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onOpen();
                    }
                }
            }, mAnimationDuration);
        }
        mFiltersContainer.setVisibility(View.VISIBLE);
    }

    public void removeFocus() {
        mIsSearchOpen = false;
        setHamburger();
        if (mShadow) {
            SearchAnimator.fadeOut(mShadowView, mAnimationDuration);
        }
        hideSuggestions();
        hideKeyboard();
        hideClearTextIcon();
        if (mVersion != VERSION_MENU_ITEM) {
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

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    // ---------------------------------------------------------------------------------------------
    private void onSubmitQuery() {
        CharSequence query = mEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            dispatchFilters();
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                mEditText.setText(query);
            }
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mEditText.getText();
        mUserQuery = text;
        if (mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable) mAdapter).getFilter().filter(text);
        }
        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            dispatchFilters();
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();

        if (!TextUtils.isEmpty(newText)) {
            showClearTextIcon();
        } else {
            hideClearTextIcon();
        }
    }

    private void checkVoiceStatus(boolean status) {
        if (mVoice && status && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(View.VISIBLE);
        } else {
            mVoiceImageView.setVisibility(View.GONE);
        }
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public boolean isShowingProgress() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    public void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditText, 0);
            imm.showSoftInput(this, 0);
        }
    }

    public void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    public void showSuggestions() {
        if (mRecyclerView.getVisibility() == View.GONE) {
            if (mAdapter != null) {
                if (mAdapter.getItemCount() > 0) {
                    mDividerView.setVisibility(View.VISIBLE);
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
            }
        }
    }

    public void hideSuggestions() {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            mDividerView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
        }
    }

    protected void setArrow() {
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

    private int getCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
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

    private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
            View view = mFiltersContainer.getChildAt(i);
            if (view instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }

    private void dispatchFilters() {
        if (mSearchFiltersStates != null) {
            for (int i = 0, j = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
                View view = mFiltersContainer.getChildAt(i);
                if (view instanceof AppCompatCheckBox)
                    mSearchFiltersStates.set(j++, ((AppCompatCheckBox) view).isChecked());
            }
        }
    }

    public void setFilters(@Nullable List<SearchFilter> filters) {
        mFiltersContainer.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = 0;
            params.bottomMargin = 0;
            mFiltersContainer.setLayoutParams(params);
        } else {
            mSearchFiltersStates = new ArrayList<>();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.filter_margin_top);
            params.bottomMargin = params.topMargin / 2;
            mFiltersContainer.setLayoutParams(params);
            for (SearchFilter filter : filters) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(mContext);
                checkBox.setText(filter.getTitle());
                checkBox.setTextSize(11);
                checkBox.setTextColor(mTextColor);
                checkBox.setChecked(filter.isChecked());
                mFiltersContainer.addView(checkBox);

                boolean isChecked = filter.isChecked();
                mSearchFiltersStates.add(isChecked);
            }
        }
    }

    public List<Boolean> getFiltersStates() {
        return mSearchFiltersStates;
    }

    private void hideClearTextIcon() {
        if (mUserQuery.length() == 0) {
            mEmptyImageView.setVisibility(View.GONE);
            checkVoiceStatus(true);
        }
    }

    private void showClearTextIcon() {
        if (mUserQuery.length() > 0) {
            mEmptyImageView.setVisibility(View.VISIBLE);
            checkVoiceStatus(false);
        }
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

    private boolean isVoiceAvailable() {
        if (isInEditMode()) {
            return true;
        }
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void reveal() {
        mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SearchAnimator.revealOpen(mCardView, mMenuItemCx, mAnimationDuration, mContext, mEditText, mShouldClearOnOpen, mOnOpenCloseListener);
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
                } else {
                    close(true);
                }
            }
        } else if (v == mVoiceImageView) {
            onVoiceClicked();
        } else if (v == mEmptyImageView) {
            if (mEditText.length() > 0) {
                mEditText.getText().clear();
            }
        } else if (v == mShadowView) {
            close(true);
        }
    }

    // ---------------------------------------------------------------------------------------------
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnOpenCloseListener(OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
    }

    public void setOnVoiceClickListener(OnVoiceClickListener listener) {
        mOnVoiceClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = mIsSearchOpen;
        dispatchFilters();
        mSavedState.searchFiltersStates = mSearchFiltersStates;
        return mSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        mSavedState = (SavedState) state;
        if (mSavedState.isSearchOpen) {
            open(true);
            setQueryWithoutSubmitting(mSavedState.query);
        }
        restoreFiltersState(mSavedState.searchFiltersStates);
        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public interface OnQueryTextListener {
        boolean onQueryTextChange(String newText);

        boolean onQueryTextSubmit(String query);
    }

    public interface OnOpenCloseListener {
        void onClose();

        void onOpen();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    @SuppressWarnings("EmptyMethod")
    public interface OnVoiceClickListener {
        void onVoiceClick();
    }

    private static class SavedState extends View.BaseSavedState {

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
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

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 0; // FIXME
            in.readList(searchFiltersStates, List.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
            out.writeList(searchFiltersStates);
        }

    }

}