package com.lapism.searchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.jetbrains.annotations.Contract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


// @RestrictTo(LIBRARY_GROUP)
// @CoordinatorLayout.DefaultBehavior(SearchBehavior.class)
public class SearchView5beta1 extends FrameLayout implements View.OnClickListener {

    public static final String TAG = SearchView5beta1.class.getName();

    private int mTextStyle = Typeface.NORMAL;
    private Typeface mTextFont = Typeface.DEFAULT;

    @SearchDefs.Version
    private int mVersion = SearchDefs.Version.TOOLBAR;

    @SearchDefs.VersionMargins
    private int mVersionMargins = SearchDefs.VersionMargins.TOOLBAR_SMALL;

    @SearchDefs.Theme
    private int mTheme = SearchDefs.Theme.LIGHT;

    @FloatRange(from = SearchArrowDrawable.STATE_HAMBURGER, to = SearchArrowDrawable.STATE_ARROW)
    private float mIsSearchArrowState = SearchArrowDrawable.STATE_HAMBURGER;

    private boolean mGoogle = false;

    private long mAnimationDuration;

    private Context mContext;

    private OnQueryTextListener mOnQueryTextListener;
    private OnOpenCloseListener mOnOpenCloseListener;
    private OnNavigationClickListener mOnNavigationClickListener;
    private OnMicClickListener mOnMicClickListener;

    private ImageView mImageViewNavigation;
    private ImageView mImageViewMic;
    private SearchArrowDrawable mSearchArrowDrawable;
    private View mViewShadow;
    private View mViewDivider;
    private CardView mCardView;
    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private FlexboxLayout mFlexboxLayout;
    private SearchEditText mSearchEditText;
    private List<Boolean> mSearchFiltersStates;
    private List<SearchFilter> mSearchFilters;

    // ---------------------------------------------------------------------------------------------
    public SearchView5beta1(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SearchView5beta1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SearchView5beta1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchView5beta1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    // init + kotlin 1.2.1 + 4.4 + glide
    // ---------------------------------------------------------------------------------------------
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        mAnimationDuration = mContext.getResources().getInteger(R.integer.search_animation_duration);

        mSearchArrowDrawable = new SearchArrowDrawable(mContext);

        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        final int layoutResId = a.getResourceId(R.styleable.SearchView_layout, R.layout.search_view);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(layoutResId, this, true);

        mCardView = findViewById(R.id.search_cardView);

        mViewShadow = findViewById(R.id.search_view_shadow);
        mViewShadow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_shadow));
        mViewShadow.setOnClickListener(this);

        mLinearLayout = findViewById(R.id.search_linearLayout);

        mImageViewNavigation = findViewById(R.id.search_imageView_navigation);
        mImageViewNavigation.setOnClickListener(this);

        mImageViewMic = findViewById(R.id.search_imageView_mic);
        mImageViewMic.setOnClickListener(this);

        mSearchEditText = findViewById(R.id.search_searchEditText);
        //mSearchEditText.setSearchView(this);

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchView5beta1.this.onTextChanged(s);
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
        // null check+ init
        a.recycle();
    }

    // ---------------------------------------------------------------------------------------------
    public void onTextChanged(CharSequence s) {

    }

    public void onSubmitQuery() {
        CharSequence query = "";
        if (!TextUtils.isEmpty(query)) {
            //onSubmitQuery();
        }
    }

    public void addFocus() {

    }

    public void removeFocus() {

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
    @SearchDefs.Version
    public int getVersion() {
        return mVersion;
    }

    @SearchDefs.VersionMargins
    public int getVersionMargins() {
        return mVersionMargins;
    }

    public void setVersionMargins(@SearchDefs.VersionMargins int versionMargins) {
        mVersionMargins = versionMargins;

        switch (mVersionMargins) {
            case SearchDefs.VersionMargins.TOOLBAR_SMALL:
                int top_small = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
                int leftRight_small = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_small_left_right);
                int bottom_small = 0;

                LayoutParams params_small = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params_small.setMargins(leftRight_small, top_small, leftRight_small, bottom_small);

                mCardView.setLayoutParams(params_small);
                break;
            case SearchDefs.VersionMargins.TOOLBAR_BIG:
                int top_big = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_top);
                int leftRight_big = mContext.getResources().getDimensionPixelSize(R.dimen.search_toolbar_margin_big_left_right);
                int bottom_big = 0;

                LayoutParams params_big = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params_big.setMargins(leftRight_big, top_big, leftRight_big, bottom_big);

                mCardView.setLayoutParams(params_big);
                break;
            case SearchDefs.VersionMargins.MENU_ITEM:
                int top_menu = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);
                int leftRight_menu = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin_left_right);
                int bottom_menu = mContext.getResources().getDimensionPixelSize(R.dimen.search_menu_item_margin);

                LayoutParams params_menu = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params_menu.setMargins(leftRight_menu, top_menu, leftRight_menu, bottom_menu);

                mCardView.setLayoutParams(params_menu);
                break;
        }
    }

    @SearchDefs.Theme
    public int getTheme() {
        return mTheme;
    }

    public void setTheme(@SearchDefs.Theme int theme) {
        setTheme(theme, true);
    }

    public void setTheme(@SearchDefs.Theme int theme, boolean tint) {
        mTheme = theme;

        switch (mTheme) {
            case SearchDefs.Theme.COLOR:
                break;
            case SearchDefs.Theme.LIGHT:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
                if (tint) {
                    setIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                    setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
                    setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_light_text_highlight));
                }
                break;
            case SearchDefs.Theme.DARK:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
                if (tint) {
                    setIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                    setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
                    setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight));
                }
                break;
        }
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

    public void setIconColor(@ColorInt int color) {
        ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);

        mImageViewNavigation.setColorFilter(colorFilter);
        mImageViewMic.setColorFilter(colorFilter);
    }

    public void setHintColor(@ColorInt int color) {
        mSearchEditText.setHintTextColor(color);
    }

    public Editable getText() {
        return mSearchEditText.getText();
    }

    public void setText(@StringRes int text) {
        mSearchEditText.setText(text);
    }

    public void setText(CharSequence text) {
        mSearchEditText.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        // adatepre
        mSearchEditText.setTextColor(color);
    }

    public void setTextHighlightColor(@ColorInt int color) {
        // adapter
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

    public void setNavigationIcon(@DrawableRes int resource) {
        mImageViewNavigation.setImageResource(resource);
    }

    public void setNavigationIcon(@Nullable Drawable drawable) {
        mImageViewNavigation.setImageDrawable(drawable);
    }

    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     */
    public void setTextStyle(int textStyle) {
        // ada[ter
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
        // adapter
        mSearchEditText.setTypeface((Typeface.create(mTextFont, mTextStyle)));
    }

    public void setMicIcon(@DrawableRes int resource) {
        mImageViewMic.setImageResource(resource);
    }

    public void setMicIcon(@Nullable Drawable drawable) {
        mImageViewMic.setImageDrawable(drawable);
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

    public void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void setShadow(boolean shadow) {
        if (shadow) {
            mViewShadow.setVisibility(View.VISIBLE);
        } else {
            mViewShadow.setVisibility(View.GONE);
        }
    }

    public void setShadowColor(@ColorInt int color) {
        mViewShadow.setBackgroundColor(color);
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    public void setOnOpenCloseListener(OnOpenCloseListener listener) {
        mOnOpenCloseListener = listener;
    }

    public void setOnNavigationClickListener(OnNavigationClickListener listener) {
        mOnNavigationClickListener = listener;
    }

    public void setOnMicClickListener(OnMicClickListener listener) {
        mOnMicClickListener = listener;
    }

    @Override
    public void setElevation(float elevation) {
        mCardView.setMaxCardElevation(elevation);
        mCardView.setCardElevation(elevation);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public void setRadius(float radius) {
        mCardView.setRadius(radius);
    }

    public void setShape(@SearchDefs.Shape int shape) {
        switch (shape) {
            case SearchDefs.Shape.CLASSIC:
                // mCardView.setPreventCornerOverlap(false); todo + anotace
                mCardView.setBackgroundResource(R.drawable.round_background_top);
                break;
            case SearchDefs.Shape.OVAL:
                mCardView.setBackgroundResource(R.drawable.round_background_top_bottom);
                break;
            case SearchDefs.Shape.ROUNDED_TOP:
                break;
            case SearchDefs.Shape.ROUNDED:
                break;
        }
    }

    public boolean isOpen() {
        return getVisibility() == View.VISIBLE;
    }

    /*
    card_view:cardUseCompatPadding="true"
    card_view:cardPreventCornerOverlap="false"
    */

    @Override
    public void onClick(View v) {
        if (v == mImageViewNavigation) {
            if (mSearchArrowDrawable != null && mIsSearchArrowState == SearchArrowDrawable.STATE_ARROW) {
                // close(true);
            } else {
                if (mOnNavigationClickListener != null) {
                    mOnNavigationClickListener.onNavigationClick(mIsSearchArrowState);
                }
            }
        }

        if (v == mImageViewMic) {
            if (mOnMicClickListener != null) {
                mOnMicClickListener.onMicClick();
                if (mIsSearchArrowState == SearchArrowDrawable.STATE_ARROW) {

                } else {
                    if (mSearchEditText.length() > 0) {
                        mSearchEditText.getText().clear();
                    }
                }
            }
        }

        if (v == mViewShadow) {
            //close true
        }
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

    public interface OnNavigationClickListener {
        void onNavigationClick(@FloatRange(from = SearchArrowDrawable.STATE_HAMBURGER, to = SearchArrowDrawable.STATE_ARROW) float state);
    }

    public interface OnMicClickListener {
        void onMicClick();
    }

}

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
