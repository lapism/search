package com.lapism.searchview;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class SearchView extends FrameLayout implements View.OnClickListener {

    public static final int ANIMATION_DURATION = 300;
    public static final int VERSION_TOOLBAR = 1000;
    public static final int VERSION_TOOLBAR_ICON = 1001;
    public static final int VERSION_MENU_ITEM = 1002;
    public static final int VERSION_MARGINS_TOOLBAR_SMALL = 2000;
    public static final int VERSION_MARGINS_TOOLBAR_BIG = 2001;
    public static final int VERSION_MARGINS_MENU_ITEM = 2002;
    public static final int THEME_LIGHT = 3000;
    public static final int THEME_DARK = 3001;
    public static final int SPEECH_REQUEST_CODE = 4000;

    private static int mIconColor = Color.BLACK;
    private static int mTextColor = Color.BLACK;
    private static int mTextHighlightColor = Color.BLACK;

    private static Typeface mTextFont = Typeface.DEFAULT;
    private static int mTextStyle = Typeface.NORMAL;

    private static CharSequence mUserQuery = " ";

    private final Context mContext;

    private OnQueryTextListener mOnQueryChangeListener = null;
    private OnOpenCloseListener mOnOpenCloseListener = null;
    private OnMenuClickListener mOnMenuClickListener = null;
    private Activity mActivity = null;
    private Fragment mFragment = null;
    private android.support.v4.app.Fragment mSupportFragment = null;
    private SearchArrowDrawable mSearchArrow = null;
    private SearchAdapter mSearchAdapter = null;
    private RecyclerView.Adapter mAdapter = null;

    private RecyclerView mRecyclerView;
    private View mShadowView;
    private View mDividerView;
    private CardView mCardView;
    private SearchEditText mEditText;
    private ImageView mBackImageView;
    private ImageView mVoiceImageView;
    private ImageView mEmptyImageView;

    private String mVoiceSearchText = "Speak now";
    private int mVersion = VERSION_TOOLBAR;
    private int mAnimationDuration = ANIMATION_DURATION;
    private float mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
    private boolean mShadow = true;
    private boolean mVoice = true;
    private boolean mIsSearchOpen = false;

    private SavedState mSavedState;
    private CharSequence mOldQueryText;

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
    @SuppressWarnings("WeakerAccess")
    public static int getIconColor() {
        return mIconColor;
    }

    @SuppressWarnings("WeakerAccess")
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

    @SuppressWarnings("WeakerAccess")
    public static int getTextColor() {
        return mTextColor;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        mEditText.setTextColor(mTextColor);
    }

    @SuppressWarnings("WeakerAccess")
    public static int getTextHighlightColor() {
        return mTextHighlightColor;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTextHighlightColor(@ColorInt int color) {
        mTextHighlightColor = color;
    }

    @SuppressWarnings("WeakerAccess")
    public static Typeface getTextFont() {
        return mTextFont;
    }

    @SuppressWarnings("unused")
    public void setTextFont(Typeface font) {
        mTextFont = font;
        mEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    @SuppressWarnings("WeakerAccess")
    public static int getTextStyle() {
        return mTextStyle;
    }

    @SuppressWarnings("WeakerAccess")
    public void setTextStyle(int style) {
        mTextStyle = style;
        mEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    // ---------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    public static CharSequence getQuery() {
        return mUserQuery;
    }

    public void setQuery(CharSequence query) {
        mEditText.setText(query);
        if (query != null) {
            mEditText.setSelection(mEditText.length());
            mUserQuery = query;
        } else {
            mEditText.getText().clear();
        }
        if (!TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    @SuppressWarnings("unused")
    private void setQuery2(CharSequence query) {
        mEditText.setText(query);
        mEditText.setSelection(TextUtils.isEmpty(query) ? 0 : query.length());
    }

    // ---------------------------------------------------------------------------------------------
    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);

        mCardView = (CardView) findViewById(R.id.cardView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVisibility(View.GONE);

        mDividerView = findViewById(R.id.view_divider);
        mDividerView.setVisibility(View.GONE);

        mShadowView = findViewById(R.id.view_shadow);
        mShadowView.setOnClickListener(this);
        mShadowView.setVisibility(View.GONE);

        mBackImageView = (ImageView) findViewById(R.id.imageView_arrow_back);
        mBackImageView.setOnClickListener(this);

        mVoiceImageView = (ImageView) findViewById(R.id.imageView_mic);
        mVoiceImageView.setOnClickListener(this);

        mEmptyImageView = (ImageView) findViewById(R.id.imageView_clear);
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

        setVersion(VERSION_TOOLBAR);
        setVersionMargins(VERSION_MARGINS_TOOLBAR_SMALL);
        setTheme(THEME_LIGHT, true);
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        final TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, 0);
        if (attr != null) {
            if (attr.hasValue(R.styleable.SearchView_search_version)) {
                setVersion(attr.getInt(R.styleable.SearchView_search_version, VERSION_TOOLBAR));
            }
            if (attr.hasValue(R.styleable.SearchView_search_version_margins)) {
                setVersionMargins(attr.getInt(R.styleable.SearchView_search_version_margins, VERSION_MARGINS_TOOLBAR_SMALL));
            }
            if (attr.hasValue(R.styleable.SearchView_search_theme)) {
                setTheme(attr.getInt(R.styleable.SearchView_search_theme, THEME_LIGHT), false);
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
            if (attr.hasValue(R.styleable.SearchView_search_text)) {
                setText(attr.getString(R.styleable.SearchView_search_text));
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
                setVoice(attr.getBoolean(R.styleable.SearchView_search_voice, false));
            }
            if (attr.hasValue(R.styleable.SearchView_search_voice_text)) {
                setVoiceText(attr.getString(R.styleable.SearchView_search_voice_text));
            }
            if (attr.hasValue(R.styleable.SearchView_search_animation_duration)) {
                setAnimationDuration(attr.getInt(R.styleable.SearchView_search_animation_duration, mAnimationDuration));
            }
            if (attr.hasValue(R.styleable.SearchView_search_shadow)) {
                setShadow(attr.getBoolean(R.styleable.SearchView_search_shadow, false));
            }
            if (attr.hasValue(R.styleable.SearchView_search_shadow_color)) {
                setShadowColor(attr.getColor(R.styleable.SearchView_search_shadow_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_elevation)) {
                setElevation(attr.getDimensionPixelSize(R.styleable.SearchView_search_elevation, 0));
            }

            attr.recycle();
        }
    }

    // ---------------------------------------------------------------------------------------------
    public void setVersion(int version) {
        mVersion = version;

        if (mVersion == VERSION_TOOLBAR) {
            mEditText.clearFocus();
            mSearchArrow = new SearchArrowDrawable(mContext);

            mBackImageView.setImageDrawable(mSearchArrow);
        }

        if (mVersion == VERSION_TOOLBAR_ICON) {
            mEditText.clearFocus();
            mBackImageView.setImageResource(R.drawable.search_ic_arrow_back_black_24dp);
        }

        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.GONE);
            mBackImageView.setImageResource(R.drawable.search_ic_arrow_back_black_24dp);
        }

        mVoiceImageView.setImageResource(R.drawable.search_ic_mic_black_24dp);
        mEmptyImageView.setImageResource(R.drawable.search_ic_clear_black_24dp);
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
            int margin = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);

            params.setMargins(margin, margin, margin, margin);
        } else {
            params.setMargins(0, 0, 0, 0);
        }

        mCardView.setLayoutParams(params);
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

    @SuppressWarnings("WeakerAccess")
    public void setNavigationIcon(int resource) {
        if (mVersion != VERSION_TOOLBAR) {
            mBackImageView.setImageResource(resource);
        }
    }

    @SuppressWarnings("unused")
    public void setNavigationIcon(Drawable drawable) {
        if (mVersion != VERSION_TOOLBAR) {
            mBackImageView.setImageDrawable(drawable);
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    @SuppressWarnings("SameParameterValue")
    public void setText(@StringRes int text) {
        mEditText.setText(text);
    }

    public void setTextSize(float size) {
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setHint(CharSequence hint) {
        mEditText.setHint(hint);
    }

    @SuppressWarnings("SameParameterValue")
    public void setHint(@StringRes int hint) {
        mEditText.setHint(hint);
    }

    @SuppressWarnings("WeakerAccess")
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

    @SuppressWarnings("unused")
    public void setVoice(boolean voice, Activity context) {
        mActivity = context;
        setVoice(voice);
    }

    @SuppressWarnings("unused")
    public void setVoice(boolean voice, Fragment context) {
        mFragment = context;
        setVoice(voice);
    }

    @SuppressWarnings("unused")
    public void setVoice(boolean voice, android.support.v4.app.Fragment context) {
        mSupportFragment = context;
        setVoice(voice);
    }

    public void setVoiceText(String text) {
        mVoiceSearchText = text;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    @SuppressWarnings("WeakerAccess")
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

    // ---------------------------------------------------------------------------------------------
    public void setAdapter(SearchAdapter adapter) {
        mSearchAdapter = adapter;
        mRecyclerView.setAdapter(mSearchAdapter);
    }

    @SuppressWarnings("unused")
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressWarnings("SameParameterValue")
    public void open(boolean animate) {
        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.VISIBLE);

            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    reveal();
                } else {
                    SearchAnimator.fadeOpen(mCardView, mAnimationDuration, mEditText, mOnOpenCloseListener);
                }
            } else {
                mCardView.setVisibility(View.VISIBLE);
                if (mEditText.length() > 0) {
                    mEditText.getText().clear();
                }
                mEditText.requestFocus();
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
            }
        }
        if (mVersion == VERSION_TOOLBAR) {
            if (mEditText.length() > 0) {
                mEditText.getText().clear();
            }
            mEditText.requestFocus();
        }
        if (mVersion == VERSION_TOOLBAR_ICON) {
            mEditText.requestFocus();
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void close(boolean animate) {
        if (mVersion == VERSION_MENU_ITEM) {
            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SearchAnimator.revealClose(mCardView, mAnimationDuration, mContext, mEditText, this, mOnOpenCloseListener);
                } else {
                    SearchAnimator.fadeClose(mCardView, mAnimationDuration, mEditText, this, mOnOpenCloseListener);
                }
            } else {
                if (mEditText.length() > 0) {
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
            if (mEditText.length() > 0) {
                mEditText.getText().clear();
            }
            mEditText.clearFocus();
        }
        if (mVersion == VERSION_TOOLBAR_ICON) {
            mEditText.clearFocus();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void addFocus() {
        mIsSearchOpen = true;
        setArrow();
        showSuggestions();
        if (mShadow) {
            SearchAnimator.fadeIn(mShadowView, mAnimationDuration);
        }
        showKeyboard();
        if (mVersion == VERSION_TOOLBAR) {
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

    @SuppressWarnings("WeakerAccess")
    public void removeFocus() {
        mIsSearchOpen = false;
        setHamburger();
        if (mShadow) {
            SearchAnimator.fadeOut(mShadowView, mAnimationDuration);
        }
        hideSuggestions();
        hideKeyboard();
        if (mVersion == VERSION_TOOLBAR) {
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
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                mEditText.setText(query);
            }
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mEditText.getText();
        mUserQuery = text;
        if (mSearchAdapter != null) {
            (mSearchAdapter).getFilter().filter(text);
        }
        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();


        if (!TextUtils.isEmpty(newText)) {  // TODO
            if (mVersion != VERSION_TOOLBAR_ICON) {
                mEmptyImageView.setVisibility(View.VISIBLE);
                checkVoiceStatus(false);
            }
        } else {
            mEmptyImageView.setVisibility(View.GONE);
            checkVoiceStatus(true);
        }
    }

    private void checkVoiceStatus(boolean status) {
        if (mVoice && status && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(View.VISIBLE);
        } else {
            mVoiceImageView.setVisibility(View.GONE);
        }
    }

    private void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEditText, 0);
            imm.showSoftInput(this, 0);
        }
    }

    private void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    private void showSuggestions() {
        if (mRecyclerView.getVisibility() == View.GONE) {
            if (mSearchAdapter != null || mAdapter != null) {
                mDividerView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
            }
        }
    }

    private void hideSuggestions() {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            mDividerView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
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

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mVoiceSearchText);
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
                SearchAnimator.revealOpen(mCardView, mAnimationDuration, mContext, mEditText, mOnOpenCloseListener);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (v == mBackImageView) {
            if (mVersion == VERSION_TOOLBAR) {
                if (mIsSearchArrowHamburgerState == SearchArrowDrawable.STATE_HAMBURGER) {
                    if (mOnMenuClickListener != null) {
                        mOnMenuClickListener.onMenuClick();
                    }
                }
                if (mIsSearchArrowHamburgerState == SearchArrowDrawable.STATE_ARROW) {
                    close(true);
                }
            }
            if (mVersion == VERSION_TOOLBAR_ICON) {
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onMenuClick();
                }
            }
            if (mVersion == VERSION_MENU_ITEM) {
                close(true);
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

    // ---------------------------------------------------------------------------------------------
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = mIsSearchOpen;
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
            setQuery(mSavedState.query);
        }
        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    public interface OnQueryTextListener {
        @SuppressWarnings({"UnusedParameters", "UnusedReturnValue", "SameReturnValue"})
        boolean onQueryTextChange(String newText);

        @SuppressWarnings("SameReturnValue")
        boolean onQueryTextSubmit(String query);
    }

    public interface OnOpenCloseListener {
        void onClose();

        void onOpen();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
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

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
        }

    }

}