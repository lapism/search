package com.lapism.searchview.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapism.searchview.R;
import com.lapism.searchview.adapter.SearchAdapter;

import java.util.List;


public class SearchView extends FrameLayout implements Filter.FilterListener, View.OnClickListener {

    public static final int VERSION_TOOLBAR = 1000;
    public static final int VERSION_MENU_ITEM = 1001;
    public static final int VERSION_MARGINS_TOOLBAR_SMALL = 2000;
    public static final int VERSION_MARGINS_TOOLBAR_BIG = 2001;
    public static final int VERSION_MARGINS_MENU_ITEM = 2002;
    public static final int THEME_LIGHT = 3000;
    public static final int THEME_DARK = 3001;

    public static final int ANIMATION_DURATION = 300;

    public static final int SPEECH_REQUEST_CODE = 1234;
    private static int mTheme = THEME_LIGHT;
    private final Context mContext;
    private int mVersion = VERSION_TOOLBAR;
    private int mAnimationDuration = ANIMATION_DURATION;
    private boolean mShadow = true;
    private boolean mVoice = true;
    private boolean mIsSearchOpen = false;
    private float mIsSearchArrowHamburgerState = ArrowDrawable.STATE_HAMBURGER;
    private String VOICE_SEARCH_TEXT = "Speak now";
    private boolean mArrow = false;
    private boolean mHamburger = false;
    private View mDivider;
    private View mShadowView;
    private Activity mActivity = null;
    private Fragment mFragment = null;
    private android.support.v4.app.Fragment mSupportFragment = null;
    private SearchAdapter mSearchAdapter;
    private CharSequence mOldQueryText;
    private ArrowDrawable mSearchArrow;
    private RecyclerView mRecyclerView;
    private CardView mCardView;
    private EditText mEditText;
    private ImageView mBackImageView;
    private ImageView mVoiceImageView;
    private ImageView mEmptyImageView;
    private OnQueryTextListener mOnQueryChangeListener;
    private SearchViewListener mSearchViewListener;
    private SearchMenuListener mSearchMenuListener = null;
    private CharSequence mUserQuery;
///    private SavedState mSavedState;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context; // getContext();
        initView();
        initStyle(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr, defStyleRes);
    }

    public static int getTheme() {
        return mTheme;
    }

    // init ----------------------------------------------------------------------------------------
    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVisibility(View.GONE);

        mBackImageView = (ImageView) findViewById(R.id.imageView_arrow_back);
        mBackImageView.setOnClickListener(this);

        mVoiceImageView = (ImageView) findViewById(R.id.imageView_mic);
        mVoiceImageView.setOnClickListener(this);

        mEmptyImageView = (ImageView) findViewById(R.id.imageView_clear);
        mEmptyImageView.setOnClickListener(this);
        mEmptyImageView.setVisibility(View.GONE);

        mShadowView = findViewById(R.id.view_shadow);
        mShadowView.setOnClickListener(this);
        mShadowView.setVisibility(View.GONE);

        mDivider = findViewById(R.id.view_divider);
        mDivider.setVisibility(View.GONE);

        mCardView = (CardView) findViewById(R.id.cardView);

        mEditText = (EditText) findViewById(R.id.editText_input);
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
                mUserQuery = s;
                startFilter(s);
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
                    in();
                } else {
                    out();
                }
            }
        });

        setVersion(mVersion);
        setTheme(THEME_LIGHT);
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        if (attr != null) {
            if (attr.hasValue(R.styleable.SearchView_search_version)) {
                setVersion(attr.getInt(R.styleable.SearchView_search_version, VERSION_TOOLBAR));
            }
            if (attr.hasValue(R.styleable.SearchView_search_version_margins)) {
                setVersionMargins(attr.getInt(R.styleable.SearchView_search_version_margins, VERSION_MARGINS_TOOLBAR_SMALL));
            }
            if (attr.hasValue(R.styleable.SearchView_search_theme)) {
                setTheme(attr.getInt(R.styleable.SearchView_search_theme, THEME_LIGHT));
            }
            if (attr.hasValue(R.styleable.SearchView_search_theme_background_color)) {
                setThemeBackgroundColor(attr.getColor(R.styleable.SearchView_search_theme_background_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text)) {
                setText(attr.getString(R.styleable.SearchView_search_text));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_color)) {
                setTextColor(attr.getColor(R.styleable.SearchView_search_text_color, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_text_size)) {
                setTextSize(attr.getDimension(R.styleable.SearchView_search_text_size, 0));
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

    // attributes ----------------------------------------------------------------------------------
    public void setVersion(int version) {
        mVersion = version;

        if (mVersion == VERSION_TOOLBAR) {
            mIsSearchOpen = true;
            mEditText.clearFocus();

            mSearchArrow = new ArrowDrawable(mContext);
            mBackImageView.setImageDrawable(mSearchArrow);
            mVoiceImageView.setImageResource(R.drawable.search_ic_mic_black_24dp);
            mEmptyImageView.setImageResource(R.drawable.search_ic_clear_black_24dp);
        }

        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.GONE);

            mBackImageView.setImageResource(R.drawable.search_ic_arrow_back_black_24dp);
            mVoiceImageView.setImageResource(R.drawable.search_ic_mic_black_24dp);
            mEmptyImageView.setImageResource(R.drawable.search_ic_clear_black_24dp);
        }
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

    public void setTheme(int theme) {
        mTheme = theme;

        if (theme == THEME_LIGHT) {
            if (mVersion == VERSION_TOOLBAR) {
                mSearchArrow.setColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
            }
            if (mVersion == VERSION_MENU_ITEM) {
                mBackImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            }
            mVoiceImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            mEmptyImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));

            // mRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mEditText.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
            mEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.search_light_text_hint));
        }

        if (theme == THEME_DARK) {
            if (mVersion == VERSION_TOOLBAR) {
                mSearchArrow.setColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            }
            if (mVersion == VERSION_MENU_ITEM) {
                mBackImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            }
            mVoiceImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            mEmptyImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));

            // mRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mEditText.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
            mEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text_hint));
        }
    }
    // TODO
    public void setThemeTint(@ColorInt int color) {
        if (mVersion == VERSION_TOOLBAR) {
            mSearchArrow.setColor(color);
        }
        if (mVersion == VERSION_MENU_ITEM) {
            mBackImageView.setColorFilter(color);
        }
        mVoiceImageView.setColorFilter(color);
        mEmptyImageView.setColorFilter(color);
    }

    public void setThemeBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    public void setText(@StringRes int text) {
        mEditText.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        mEditText.setTextColor(color);
    }

    public void setTextSize(float size) {
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);//, SP?
    }

    public void setHint(CharSequence hint) {
        mEditText.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        mEditText.setHint(hint);
    }

    public void setHintColor(@ColorInt int color) { // @ColorRes
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

    public void setVoiceText(String text) {
        VOICE_SEARCH_TEXT = text;
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
        mCardView.setCardElevation(elevation);
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

    /* future release ------------------------------------------------------------------------------
    // @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setHeight(int height) {
        // float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        CardView.LayoutParams params = (CardView.LayoutParams) mCardView.getLayoutParams();
        params.height = height;
        params.width = CardView.LayoutParams.WRAP_CONTENT;
        mCardView.setLayoutParams(params);
    }*/

    // public --------------------------------------------------------------------------------------
    public void show(boolean animate) {
        setVisibility(View.VISIBLE);

        mEditText.requestFocus();
        mEditText.setText(null);

        if (mVersion == VERSION_MENU_ITEM) {
            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    revealInAnimation();
                } else {
                    SearchAnimator.fadeInAnimation(mCardView, mAnimationDuration);
                }
            } else {
                mCardView.setVisibility(View.VISIBLE);
            }
            if (mSearchViewListener != null) {
                mSearchViewListener.onSearchViewShown();
            }
        }
    }

    public void hide(boolean animate) {
        mEditText.setText(null);
        mEditText.clearFocus();

        if (mVersion == VERSION_MENU_ITEM) {
            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SearchAnimator.revealOutAnimation(mContext, mCardView, mAnimationDuration);
                } else {
                    SearchAnimator.fadeOutAnimation(mCardView, mAnimationDuration);
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(View.GONE);
                        if (mSearchViewListener != null) {
                            mSearchViewListener.onSearchViewClosed();
                        }
                    }
                }, mAnimationDuration);
            } else {
                setVisibility(View.GONE);
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewClosed();
                }
            }
        }
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    public void setQuery(CharSequence query) {
        mEditText.setText(query);
        if (query != null) {
            mEditText.setSelection(mEditText.length());
            mUserQuery = query;
        }
        if (!TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setAdapter(SearchAdapter adapter) {
        mSearchAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
        startFilter(mEditText.getText());
    }

    private void in() {
        mIsSearchOpen = true;
        showKeyboard();
        showSuggestions();
        if (mShadow) {
            SearchAnimator.fadeInAnimation(mShadowView, mAnimationDuration);
        }
        setArrow(true);
    }

    public void out() {
        mIsSearchOpen = false;
        hideKeyboard();
        hideSuggestions();
        if (mShadow) {
            SearchAnimator.fadeOutAnimation(mShadowView, mAnimationDuration);
        }
        setHamburger(true);
    }

    // todo FIX
    public void setArrow() {
        mArrow = true;
        setArrow(false);
    }

    // todo FIX
    public void setHamburger() {
        mHamburger = true;
        setHamburger(false);
    }

    private void setArrow(boolean animate) {
        if (mSearchArrow != null && mVersion == VERSION_TOOLBAR) {
            if (animate) {
                mSearchArrow.setVerticalMirror(false);
                mSearchArrow.animate(ArrowDrawable.STATE_ARROW);
            } else {
                mSearchArrow.setProgress(ArrowDrawable.STATE_ARROW);
            }
            mIsSearchArrowHamburgerState = ArrowDrawable.STATE_ARROW;
        }
    }

    private void setHamburger(boolean animate) {
        if (mSearchArrow != null && mVersion == VERSION_TOOLBAR) {
            if (animate) {
                mSearchArrow.setVerticalMirror(true);
                mSearchArrow.animate(ArrowDrawable.STATE_HAMBURGER);
            } else {
                mSearchArrow.setProgress(ArrowDrawable.STATE_HAMBURGER);
            }
            mIsSearchArrowHamburgerState = ArrowDrawable.STATE_HAMBURGER;
        }
    }

    private void showSuggestions() {
        if (mSearchAdapter != null && mSearchAdapter.getItemCount() > 0 && mRecyclerView.getVisibility() == View.GONE) {
            mDivider.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAlpha(0.0f);
            mRecyclerView.animate().alpha(1.0f);
        }
    }

    private void hideSuggestions() {
        if (mRecyclerView.getVisibility() == View.VISIBLE) {
            mRecyclerView.setVisibility(View.GONE);
            mDivider.setVisibility(View.GONE);
        }
    }

    // private -------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealInAnimation() {
        mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SearchAnimator.revealInAnimation(mContext, mCardView, mAnimationDuration);
            }
        });
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    private boolean isVoiceAvailable() {
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, VOICE_SEARCH_TEXT);
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

    private void onSubmitQuery() {
        CharSequence query = mEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                mEditText.setText(null);
            }
        }
    }

    private void startFilter(CharSequence s) {
        if (mSearchAdapter != null) {
            (mSearchAdapter).getFilter().filter(s, this);
        }
    }

    private void checkVoiceStatus(boolean status) {
        if (mVoice && status && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(View.VISIBLE);
        } else {
            mVoiceImageView.setVisibility(View.GONE);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mEditText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);

        if (hasText) {
            mEmptyImageView.setVisibility(View.VISIBLE);
            checkVoiceStatus(false);
        } else {
            mEmptyImageView.setVisibility(View.GONE);
            checkVoiceStatus(true);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }

        mOldQueryText = newText.toString();
    }

    // interfaces ----------------------------------------------------------------------------------
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }

    public void setOnSearchMenuListener(SearchMenuListener listener) {
        mSearchMenuListener = listener;
    }

    // implements ----------------------------------------------------------------------------------
    @Override
    public void onFilterComplete(int text) {
        if (text > 0) {
            showSuggestions();
        } else {
            hideSuggestions();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBackImageView || v == mShadowView) {
            if (mVersion == VERSION_TOOLBAR) {
                if (mIsSearchArrowHamburgerState == ArrowDrawable.STATE_HAMBURGER) {
                    if (mSearchMenuListener != null) {
                        mSearchMenuListener.onMenuClick();
                    }
                } else {
                    hide(false);
                }
            }
            if (mVersion == VERSION_MENU_ITEM) {
                hide(true);
            }
        }
        if (v == mVoiceImageView) {
            onVoiceClicked();
        }
        if (v == mEmptyImageView) {
            mEditText.setText(null);
        }
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
    }

    public interface SearchMenuListener {
        void onMenuClick();
    }

    // ---------------------------------------------------------------------------------------------

  /*   @Override
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
            show(true);
            setQuery(mSavedState.query);
        }
        super.onRestoreInstanceState(mSavedState.getSuperState());

        //editTextValue = mEditText.getText().toString();
        //mTextView.setText(editTextValue);

    }



   /* private static class SavedState extends View.BaseSavedState {

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

        public SavedState(Parcelable superState) {
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

    }*/

}