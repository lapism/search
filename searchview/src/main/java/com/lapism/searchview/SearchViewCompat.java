package com.lapism.searchview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

// @RestrictTo(LIBRARY_GROUP)
// @CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchView extends FrameLayout implements View.OnClickListener {

    @IntDef({VERSION_TOOLBAR, VERSION_MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Version {}

    @IntDef({VERSION_MARGINS_TOOLBAR_SMALL, VERSION_MARGINS_TOOLBAR_BIG, VERSION_MARGINS_MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VersionMargins {}

    @IntDef({THEME_LIGHT, THEME_DARK, THEME_PLAY_STORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {}

    @IntDef({TEXT_STYLE_NORMAL, TEXT_STYLE_BOLD, TEXT_STYLE_ITALIC, TEXT_STYLE_BOLD_ITALIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle {}

    public static final String TAG = "SearchView";

    public static final int TEXT_STYLE_NORMAL = 0;
    public static final int TEXT_STYLE_BOLD = 1;
    public static final int TEXT_STYLE_ITALIC = 2;
    public static final int TEXT_STYLE_BOLD_ITALIC = 3;

    public static final int VERSION_TOOLBAR = 1000;
    public static final int VERSION_MENU_ITEM = 1001;

    public static final int VERSION_MARGINS_TOOLBAR_SMALL = 2000;
    public static final int VERSION_MARGINS_TOOLBAR_BIG = 2001;
    public static final int VERSION_MARGINS_MENU_ITEM = 2002;

    public static final int THEME_LIGHT = 3000;
    public static final int THEME_DARK = 3001;
    public static final int THEME_PLAY_STORE = 3002;

    public static final int SPEECH_REQUEST_CODE = 100;
    public static final int ANIMATION_DURATION = 300;

    private static int mIconColor = Color.BLACK;
    private static int mTextColor = Color.BLACK;
    private static int mTextHighlightColor = Color.BLACK;
    private static int mTextStyle = Typeface.NORMAL;
    private static Typeface mTextFont = Typeface.DEFAULT;

    private final Context mContext;
    private SearchArrowDrawable mSearchArrow;

    private View mMenuItemView = null;
    private Activity mActivity = null;
    private Fragment mFragment = null;
    private android.support.v4.app.Fragment mSupportFragment = null;
    private RecyclerView.Adapter mAdapter = null;
    private OnQueryTextListener mOnQueryChangeListener = null;
    private OnOpenCloseListener mOnOpenCloseListener = null;
    private OnNavigationIconClickListener mOnNavigationIconClickListener = null;
    private OnVoiceIconClickListener mOnVoiceIconClickListener = null;
    private List<Boolean> mSearchFiltersStates = null;
    private List<SearchFilter> mSearchFilters = null;

    private View mViewShadow;
    private View mViewDivider;
    private CardView mCardView;
    private LinearLayout mLinearLayout;
    private ImageView mImageViewArrow;
    private ImageView mImageViewMic;
    private ImageView mImageViewClear;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private FlexboxLayout mFlexboxLayout;
    private SearchEditText mSearchEditText;

    private int mMenuItemCx = -1;
    private int mVersion = VERSION_TOOLBAR;
    private int mAnimationDuration = ANIMATION_DURATION;
    private float mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
    private String mVoiceText = "";
    private CharSequence mQuery = "";
    private boolean mArrow = false;
    private boolean mShadow = true;
    private boolean mVoice = true;
    private boolean mShouldClearOnOpen = false;
    private boolean mShouldClearOnClose = false;
    private boolean mShouldHideOnKeyboardClose = true;

    // ---------------------------------------------------------------------------------------------
    public SearchView(@NonNull Context context) {
        // super(context);
        this(context, null);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        // super(context, attrs);
        this(context, attrs, 0);
        // this(context, attrs, R.attr.searchViewStyle);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        mCardView = (CardView) findViewById(R.id.cardView);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        mSearchArrow = new SearchArrowDrawable(mContext);

        mImageViewArrow = (ImageView) findViewById(R.id.imageView_arrow);
        mImageViewArrow.setImageDrawable(mSearchArrow);
        mImageViewArrow.setOnClickListener(this);

        mImageViewMic = (ImageView) findViewById(R.id.imageView_mic);
        mImageViewMic.setImageResource(R.drawable.ic_mic_black_24dp);
        mImageViewMic.setOnClickListener(this);
        mImageViewMic.setVisibility(View.GONE);

        mImageViewClear = (ImageView) findViewById(R.id.imageView_clear);
        mImageViewClear.setImageResource(R.drawable.ic_clear_black_24dp);
        mImageViewClear.setOnClickListener(this);
        mImageViewClear.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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

        mFlexboxLayout = (FlexboxLayout) findViewById(R.id.flexboxLayout);
        mFlexboxLayout.setVisibility(View.GONE);

        mSearchEditText = (SearchEditText) findViewById(R.id.searchEditText);
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

        setVoice(true);
    }

    // TODO ANOTACE A GETY, 1.SET, PAK GET
    // ---------------------------------------------------------------------------------------------
    public void setTheme(@Theme int theme) {
        setTheme(theme, true);
    }

    public void setTheme(@Theme int theme, boolean tint) {
        mTheme = theme;

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
    }

    @Theme
    public int getTheme() {
        return mTheme;
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

    // ---------------------------------------------------------------------------------------------
    public void setNavigationIcon(@DrawableRes int resource) {
        mImageViewArrow.setImageResource(resource);
    }

    public void setNavigationIcon(Drawable drawable) {
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
            if (mSearchArrow != null) {
                mSearchArrow.setVerticalMirror(false);
                mSearchArrow.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
            }
        } else {
            mSearchArrow.setProgress(SearchArrowDrawable.STATE_ARROW);
        }

        mArrow = true;
    }

    // ---------------------------------------------------------------------------------------------
    public void setVoiceIcon(@DrawableRes int resource) {
        mImageViewMic.setImageResource(resource);
    }

    public void setVoiceIcon(Drawable drawable) {
        if (drawable == null) {
            mImageViewMic.setVisibility(View.GONE);
        } else {
            mImageViewMic.setImageDrawable(drawable);
        }
    }

    public void setVoiceIconClickListener(View.OnClickListener listener) {
        mImageViewMic.setOnClickListener(listener);
    }


    public void setOnVoiceClickListener(OnVoiceIconClickListener listener) {
        mOnVoiceIconClickListener = listener;
    }

    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
    }

    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    /* new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
    new SearchDivider(mContext) */
    public void addDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /* new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
       new SearchDivider(mContext) */
    public void removeDivider(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
    }

    // ---------------------------------------------------------------------------------------------
    public void setGoogleIcons() {
        mImageViewArrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color_24dp));
        mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_color_24dp));
        mImageViewClear.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color_24dp)); //TODO
    }

    // ---------------------------------------------------------------------------------------------
    private boolean isVoiceAvailable() {
        if (isInEditMode()) {
            return true;
        }
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (v == mImageViewArrow) {
            if (mSearchArrow != null && mIsSearchArrowHamburgerState == SearchArrowDrawable.STATE_ARROW) {
                close(true);
            } else {
                if (mOnNavigationIconClickListener != null) {
                    mOnNavigationIconClickListener.onNavigationIconClick(mIsSearchArrowHamburgerState);
                }
            }
        } else if (v == mImageViewMic) {
            onVoiceClicked();
        } else if (v == mImageViewClear) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
        } else if (v == mViewShadow) {
            close(true);
        }
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

    public interface OnNavigationIconClickListener {
        void onNavigationIconClick(float state);
    }

    public interface OnVoiceIconClickListener {
        void onVoiceIconClick();
    }

    // ---------------------------------------------------------------------------------------------
    public void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(mSearchEditText, 0);
            inputManager.showSoftInput(this, 0);
        }
    }

    public void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    private static class SavedState extends BaseSavedState{

        public SavedState(Parcel source) {
            super(source);
        }

        //@RequiresApi(api = Build.VERSION_CODES.N)
        @TargetApi(Build.VERSION_CODES.N)
        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public void sett()
    {
     //if()
        Log.d(SearchView.TAG, "");
        Log.e(SearchView.TAG, "");
        Log.i(SearchView.TAG, "");
        Log.w(SearchView.TAG, "");
        Log.v(SearchView.TAG, "");
    }

}
