package com.lapism.searchview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.List;


public class SearchView5 extends FrameLayout implements View.OnClickListener {

    public static final String TAG = "SearchView";

    public static final int SPEECH_REQUEST_CODE = 100;
    public static final int ANIMATION_DURATION = 300;

    private static int mIconColor = Color.BLACK;
    private static int mTextColor = Color.BLACK;
    private static int mTextHighlightColor = Color.BLACK;
    private static int mTextStyle = Typeface.NORMAL;
    private static Typeface mTextFont = Typeface.DEFAULT;

    private final Context mContext;

    private SearchArrowDrawable mSearchArrowDrawable;
    private View mViewShadow;
    private View mViewDivider;
    private View mMenuItemView;
    private CardView mCardView;
    private LinearLayout mLinearLayout;
    private ImageView mImageViewArrow;
    private ImageView mImageViewMic;
    private ImageView mImageViewClear;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private FlexboxLayout mFlexboxLayout;
    private SearchEditText mSearchEditText;
    private OnQueryTextListener mOnQueryTextListener;
    private OnOpenCloseListener mOnOpenCloseListener;
    private OnNavigationIconClickListener mOnNavigationIconClickListener;
    private OnVoiceIconClickListener mOnVoiceIconClickListener;

    private int mVersion;
    private int mVersionMargins;
    private int mTheme;

    private int mMenuItemCx = -1;
    private int mAnimationDuration = ANIMATION_DURATION;
    private float mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
    private boolean mArrow = false;
    private boolean mShadow = true;
    private boolean mVoice = true;
    private boolean mShouldClearOnOpen = false;
    private boolean mShouldClearOnClose = false;
    private boolean mShouldHideOnKeyboardClose = true;
    private String mVoiceText = "";

    // ---------------------------------------------------------------------------------------------
    public SearchView5(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
        initStyle(null, 0);
    }

    public SearchView5(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initStyle(attrs, 0);
    }

    public SearchView5(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchView5(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    // ---------------------------------------------------------------------------------------------
    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);

        mViewShadow = findViewById(R.id.view_shadow);
        mViewShadow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_shadow_layout));
        mViewShadow.setOnClickListener(this);
        mViewShadow.setVisibility(View.GONE);

        mViewDivider = findViewById(R.id.view_divider);
        mViewDivider.setVisibility(View.GONE);

        mCardView = findViewById(R.id.cardView);
        mLinearLayout = findViewById(R.id.linearLayout);

        mSearchArrowDrawable = new SearchArrowDrawable(mContext);

        mImageViewArrow = findViewById(R.id.imageView_arrow);
        mImageViewArrow.setImageDrawable(mSearchArrowDrawable);
        mImageViewArrow.setOnClickListener(this);

        mImageViewMic = findViewById(R.id.imageView_mic);
        mImageViewMic.setImageResource(R.drawable.ic_mic_black_24dp);
        mImageViewMic.setOnClickListener(this);
        mImageViewMic.setVisibility(View.GONE);

        mImageViewClear = findViewById(R.id.imageView_clear);
        mImageViewClear.setImageResource(R.drawable.ic_clear_black_24dp);
        mImageViewClear.setOnClickListener(this);
        mImageViewClear.setVisibility(View.GONE);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.recyclerView);
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

        mFlexboxLayout = findViewById(R.id.flexboxLayout);
        mFlexboxLayout.setVisibility(View.GONE);

        mSearchEditText = findViewById(R.id.searchEditText);
        mSearchEditText.setSearchView(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchView5.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        setVersion(Version.TOOLBAR);
        setVersionMargins(VersionMargins.TOOLBAR_SMALL);
        setTheme(THEME_LIGHT);
        setVoice(true);
    }

    // ---------------------------------------------------------------------------------------------
    public void setVersion(@Version int version) {
        mVersion = version;

        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.GONE);
        }
    }

    @Version
    public int getVersion() {
        return mVersion;
    }

    public void setTheme(@Theme int theme) {
        setTheme(theme, true);
    }

    public void setTheme(@Theme int theme, boolean tint) {
        mTheme = theme;

        if (theme == Theme.LIGHT) {
            setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            if (tint) {
                setIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
                setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_light_text_highlight));
            }
        }

        if (theme == Theme.DARK) {
            setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            if (tint) {
                setIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
                setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight));
            }
        }

        if (theme == Theme.PLAY_STORE) {
            setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_play_store_background));
            if (tint) {
                setIconColor(ContextCompat.getColor(mContext, R.color.search_play_store_icon));
                setHintColor(ContextCompat.getColor(mContext, R.color.search_play_store_hint));
                setTextColor(ContextCompat.getColor(mContext, R.color.search_play_store_text));
                setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_play_store_text_highlight));
            }
        }
    }

    @Theme
    public int getTheme() {
        return mTheme;
    }

    public void setVersionMargins(@VersionMargins int versionMargins) {
        mVersionMargins = versionMargins;

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        if (versionMargins == VersionMargins.TOOLBAR_SMALL) {
            int top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
            int leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_small_left_right);
            int bottom = 0;

            params.setMargins(leftRight, top, leftRight, bottom);

        } else if (versionMargins == VersionMargins.TOOLBAR_BIG) {
            int top = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
            int leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_big_left_right);
            int bottom = 0;

            params.setMargins(leftRight, top, leftRight, bottom);

        } else if (versionMargins == VersionMargins.MENU_ITEM) {
            int top = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);
            int leftRight = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin_left_right);
            int bottom = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);

            params.setMargins(leftRight, top, leftRight, bottom);

        } else {
            params.setMargins(0, 0, 0, 0);
        }

        mCardView.setLayoutParams(params);
    }

    @VersionMargins
    public int getVersionMargins() {
        return mVersionMargins;
    }

    @ColorInt
    public static int getTextHighlightColor() {
        return mTextHighlightColor;
    }

    public void setTextHighlightColor(@ColorInt int color) {
        mTextHighlightColor = color;
    }

    // todo tadz da;lsi

    public void setShouldClearOnClose(boolean shouldClearOnClose) {
        mShouldClearOnClose = shouldClearOnClose;
    }

    public boolean getShouldClearOnClose() {
        return mShouldClearOnClose;
    }

    public void setShouldClearOnOpen(boolean shouldClearOnOpen) {
        mShouldClearOnOpen = shouldClearOnOpen;
    }

    public boolean getShouldClearOnOpen() {
        return mShouldClearOnOpen;
    }

    public void setShouldHideOnKeyboardClose(boolean shouldHideOnKeyboardClose) {
        mShouldHideOnKeyboardClose = shouldHideOnKeyboardClose;
    }

    public boolean getShouldHideOnKeyboardClose() {
        return mShouldHideOnKeyboardClose;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerViewAdapter = adapter;
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
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

    public void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(mSearchEditText, 0);
            inputManager.showSoftInput(this, 0);
        }
    }

    public void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public void open(boolean animate) {
        open(animate, null);
    }

    public void open(boolean animate, MenuItem menuItem) {
        if (mVersion == VERSION_MENU_ITEM) {
            setVisibility(View.VISIBLE);

            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (menuItem != null) {
                        getMenuItemPosition(menuItem.getItemId());
                    }
                    mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            SearchAnimator.revealOpen(
                                    mCardView,
                                    mMenuItemCx,
                                    mAnimationDuration,
                                    mContext,
                                    mSearchEditText,
                                    mShouldClearOnOpen,
                                    mOnOpenCloseListener);
                        }
                    });
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
            if (mShouldClearOnOpen && mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
            mSearchEditText.requestFocus();
        }
    }

    // TODO close with menu item
    public void close(boolean animate) {
        if (mVersion == VERSION_MENU_ITEM) {

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
            if (mShouldClearOnClose && mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
            mSearchEditText.clearFocus();
        }
    }

    public void addFocus() {
        if (mArrow) {
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_ARROW;
        } else {
            if (mSearchArrowDrawable != null) {
                mSearchArrowDrawable.setVerticalMirror(false);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
                mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_ARROW;
            }
        }

        if (mShadow) {
            SearchAnimator.fadeIn(mViewShadow, mAnimationDuration);
        }

        if (!TextUtils.isEmpty(mQuery)) {
            mImageViewClear.setVisibility(View.VISIBLE);
            if (mVoice) {
                mImageViewMic.setVisibility(View.GONE);
            }
        }

        showKeyboard();
        showSuggestions();

        if (mVersion == VERSION_TOOLBAR) {
            postDelayed(() -> {
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
            }, mAnimationDuration);
        }
    }

    public void removeFocus() {
        if (mArrow) {
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
        } else {
            if (mSearchArrowDrawable != null) {
                mSearchArrowDrawable.setVerticalMirror(true);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_HAMBURGER, mAnimationDuration);
                mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
            }
        }

        if (mShadow) {
            SearchAnimator.fadeOut(mViewShadow, mAnimationDuration);
        }

        if (!TextUtils.isEmpty(mQuery)) {
            mImageViewClear.setVisibility(View.GONE);
            if (mVoice) {
                mImageViewMic.setVisibility(View.VISIBLE);
            }
        }

        hideKeyboard();
        hideSuggestions();

        if (mVersion == VERSION_TOOLBAR) {
            postDelayed(() -> {
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onClose();
                }
            }, mAnimationDuration);
        }
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void showSuggestions() {
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

    public void hideSuggestions() {
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

    public void setShadowColor(@ColorInt int color) {
        mViewShadow.setBackgroundColor(color);
    }

    public void setGoogleIcons() {
        mImageViewArrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color_24dp));
        mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_color_24dp));
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    public void setOnOpenCloseListener(OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
    }

    public void setNavigationIcon(@DrawableRes int resource) {
        mImageViewArrow.setImageResource(resource);
    }

    public void setNavigationIcon(@Nullable Drawable drawable) {
        if (drawable == null) {
            mImageViewArrow.setVisibility(View.GONE);
        } else {
            mImageViewArrow.setImageDrawable(drawable);
        }
    }

    public void setNavigationIconClickListener(View.OnClickListener listener) {
        mImageViewArrow.setOnClickListener(listener);
    }

    public void setOnNavigationIconClickListener(OnNavigationIconClickListener listener) {
        mOnNavigationIconClickListener = listener;
    }

    public void setNavigationIconAnimation(boolean animate) {
        if (animate) {
            if (mSearchArrowDrawable != null) {
                mSearchArrowDrawable.setVerticalMirror(false);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
            }
        } else {
            mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_ARROW);
        }

        mArrow = true;
    }

    public void setVoiceIcon(@DrawableRes int resource) {
        mImageViewMic.setImageResource(resource);
    }

    public void setVoiceIcon(@Nullable Drawable drawable) {
        if (drawable == null) {
            mImageViewMic.setVisibility(View.GONE);
        } else {
            mImageViewMic.setImageDrawable(drawable);
        }
    }

    public void setVoiceIconClickListener(View.OnClickListener listener) {
        mImageViewMic.setOnClickListener(listener);
    }

    public void setOnVoiceIconClickListener(OnVoiceIconClickListener listener) {
        mOnVoiceIconClickListener = listener;
    }

    public void setVoice(boolean voice) {
        if (voice && isVoiceAvailable()) {
            mImageViewMic.setVisibility(View.VISIBLE);
        } else {
            mImageViewMic.setVisibility(View.GONE);
        }
        mVoice = voice;
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
    }

    /**
     * This method might not work.
     * http://stackoverflow.com/questions/11554078/set-textcursordrawable-programatically
     */
    public void setCursorDrawable(@DrawableRes int drawable) {
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
    }

    public boolean isOpen() {
        return getVisibility() == View.VISIBLE;
    }

    public boolean isShowingProgress() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    // ---------------------------------------------------------------------------------------------
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

    private void onVoiceClicked() {
        if (mOnVoiceIconClickListener != null) {
            mOnVoiceIconClickListener.onVoiceIconClick();
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mVoiceText);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        if (mContext instanceof FragmentActivity) {
            ((FragmentActivity) mContext).startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, SPEECH_REQUEST_CODE);
        }
    }

    private boolean isVoiceAvailable() {
        if (isInEditMode()) {
            return true;
        }
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view == mImageViewArrow) {
            if (mSearchArrowDrawable != null && mIsSearchArrowHamburgerState == SearchArrowDrawable.STATE_ARROW) {
                close(true);
            } else {
                if (mOnNavigationIconClickListener != null) {
                    mOnNavigationIconClickListener.onNavigationIconClick(mIsSearchArrowHamburgerState);
                }
            }
        } else if (view == mImageViewMic) {
            onVoiceClicked();
        } else if (view == mImageViewClear) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
        } else if (view == mViewShadow) {
            close(true);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    // ---------------------------------------------------------------------------------------------
    @IntDef({Version.TOOLBAR, Version.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Version {
        int TOOLBAR = 1000;
        int MENU_ITEM = 1001;
    }

    @IntDef({VersionMargins.TOOLBAR_SMALL, VersionMargins.TOOLBAR_BIG, VersionMargins.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VersionMargins {
        int TOOLBAR_SMALL = 2000;
        int TOOLBAR_BIG = 2001;
        int MENU_ITEM = 2002;
    }

    @IntDef({Theme.LIGHT, Theme.DARK, Theme.PLAY_STORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {
        int LIGHT = 3000;
        int DARK = 3001;
        int PLAY_STORE = 3002;
    }

    @IntDef({TextStyle.NORMAL, TextStyle.BOLD, TextStyle.ITALIC, TextStyle.BOLD_ITALIC})
    //@Retention(RetentionPolicy.SOURCE)TODO supress a drarwwable a vzcisstt komenty
    public @interface TextStyle {
        int NORMAL = 0;
        int BOLD = 1;
        int ITALIC = 2;
        int BOLD_ITALIC = 3;
    }

    // ---------------------------------------------------------------------------------------------
    public interface OnQueryTextListener {
        boolean onQueryTextChange(String newText);

        boolean onQueryTextSubmit(String query);
    }

    public interface OnOpenCloseListener {
        boolean onClose();

        boolean onOpen();
    }

    public interface OnNavigationIconClickListener {
        void onNavigationIconClick(float state);
    }

    public interface OnVoiceIconClickListener {
        void onVoiceIconClick();
    }

    // ---------------------------------------------------------------------------------------------
    private static class SavedState extends BaseSavedState {

        public SavedState(Parcel source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @RequiresApi(api = Build.VERSION_CODES.N)
        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

    }

}
// @RestrictTo(LIBRARY_GROUP)
// @CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
/*    //@ColorRes


                <!--      <requestFocus />
                 android:background="?android:attr/selectableItemBackgroundBorderless"
                 android:background="?android:attr/listDivider"
                    @android:color/transparent
                 android:hint="Search"
                 android:imeOptions="actionSearch|flagNoFullscreen"
                 -->
    <!-- background
    ?android:attr/selectableItemBackgroundBorderless
    ?android:attr/selectableItemBackground
    ?android:attr/selectableItemBackgroundBorderless
      ?android:attr/selectableItemBackgroundBorderless
    android:background="?attr/selectableItemBackground"-->
*/
// getgcontex
//weakreference        // this(context, null);
// this(context, attrs, 0);
// this(context, attrs, R.attr.searchViewStyle);