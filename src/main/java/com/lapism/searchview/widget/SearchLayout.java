package com.lapism.searchview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lapism.searchview.R.color;
import com.lapism.searchview.R.dimen;
import com.lapism.searchview.R.drawable;
import com.lapism.searchview.R.id;
import com.lapism.searchview.Search;
import com.lapism.searchview.Search.Logo;
import com.lapism.searchview.Search.Shape;
import com.lapism.searchview.Search.Theme;
import com.lapism.searchview.Search.VersionMargins;
import com.lapism.searchview.graphics.SearchArrowDrawable;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

@RestrictTo(LIBRARY_GROUP)
public abstract class SearchLayout extends FrameLayout implements OnClickListener {

    CharSequence mQueryText = "";
    boolean mIconAnimation = true;
    Context mContext;
    CardView mCardView;
    ImageView mImageViewLogo;
    ImageView mImageViewMic;
    ImageView mImageViewClear;
    ImageView mImageViewMenu;
    SearchEditText mSearchEditText;
    SearchArrowDrawable mSearchArrowDrawable;
    Search.OnMicClickListener mOnMicClickListener;
    Search.OnMenuClickListener mOnMenuClickListener;
    Search.OnQueryTextListener mOnQueryTextListener;
    @Logo
    private int mLogo;
    @Shape
    private int mShape;
    @Theme
    private int mTheme;
    private int mTextStyle = Typeface.NORMAL;
    private Typeface mTextFont = Typeface.DEFAULT;
    private LinearLayout mLinearLayout;
    @VersionMargins
    private int mVersionMargins;

    // ---------------------------------------------------------------------------------------------
    public SearchLayout(@NonNull Context context) {
        super(context);
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    @TargetApi(VERSION_CODES.LOLLIPOP)
    SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // ---------------------------------------------------------------------------------------------
    protected abstract void onTextChanged(CharSequence s);

    protected abstract void addFocus();

    protected abstract void removeFocus();

    protected abstract boolean isView();

    protected abstract int getLayout();

    protected abstract void open();

    public abstract void close();

    // ---------------------------------------------------------------------------------------------
    @CallSuper
    void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mContext = context;

        this.mCardView = this.findViewById(id.search_cardView);

        this.mLinearLayout = this.findViewById(id.search_linearLayout);

        this.mImageViewLogo = this.findViewById(id.search_imageView_logo);
        this.mImageViewLogo.setOnClickListener(this);

        this.mImageViewMic = this.findViewById(id.search_imageView_mic);
        this.mImageViewMic.setVisibility(View.GONE);
        this.mImageViewMic.setOnClickListener(this);

        this.mImageViewMenu = this.findViewById(id.search_imageView_menu);
        this.mImageViewMenu.setVisibility(View.GONE);
        this.mImageViewMenu.setOnClickListener(this);

        this.mSearchEditText = this.findViewById(id.search_searchEditText);
        this.mSearchEditText.setVisibility(View.GONE);
        this.mSearchEditText.setLayout(this);
        this.mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchLayout.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.mSearchEditText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SearchLayout.this.onSubmitQuery();
                return true;
            }
        });
        this.mSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SearchLayout.this.addFocus();
                } else {
                    SearchLayout.this.removeFocus();
                }
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    @Logo
    public int getLogo() {
        return this.mLogo;
    }

    public void setLogo(@Logo int logo) {
        this.mLogo = logo;

        switch (this.mLogo) {
            case Logo.GOOGLE:
                this.mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(this.mContext, drawable.search_ic_g_color_24dp));
                break;
            case Logo.HAMBURGER_ARROW:
                this.mSearchArrowDrawable = new SearchArrowDrawable(this.mContext);
                this.mImageViewLogo.setImageDrawable(this.mSearchArrowDrawable);
                break;
            case Logo.ARROW:
                this.mImageViewLogo.setImageDrawable(ContextCompat.getDrawable(this.mContext, drawable.search_ic_arrow_back_black_24dp));
                break;
        }
    }

    @Shape
    public int getShape() {
        return this.mShape;
    }

    void setShape(@Shape int shape) {
        this.mShape = shape;

        switch (this.mShape) {
            case Shape.CLASSIC:
                this.mCardView.setRadius(this.getResources().getDimensionPixelSize(dimen.search_shape_classic));
                break;
            case Shape.ROUNDED:
                this.mCardView.setRadius(this.getResources().getDimensionPixelSize(dimen.search_shape_rounded));
                break;
            case Shape.OVAL:
                if (!this.isView()) {
                    this.mCardView.setRadius(this.getResources().getDimensionPixelSize(dimen.search_shape_oval));
                }
                break;
        }
    }

    @Theme
    public int getTheme() {
        return this.mTheme;
    }

    void setTheme(@Theme int theme) {
        this.mTheme = theme;

        switch (this.mTheme) {
            case Theme.PLAY:
                this.setBackgroundColor(ContextCompat.getColor(this.mContext, color.search_play_background));
                this.setDividerColor(ContextCompat.getColor(this.mContext, color.search_play_divider));
                this.setLogoColor(ContextCompat.getColor(this.mContext, color.search_play_icon));
                this.setMicColor(ContextCompat.getColor(this.mContext, color.search_play_icon));
                this.setClearColor(ContextCompat.getColor(this.mContext, color.search_play_icon));
                this.setMenuColor(ContextCompat.getColor(this.mContext, color.search_play_icon));
                this.setHintColor(ContextCompat.getColor(this.mContext, color.search_play_hint));
                this.setTextColor(ContextCompat.getColor(this.mContext, color.search_play_title));
                break;
            case Theme.GOOGLE:
                this.setBackgroundColor(ContextCompat.getColor(this.mContext, color.search_google_background));
                this.setDividerColor(ContextCompat.getColor(this.mContext, color.search_google_divider));
                this.clearIconsColor();
                this.setClearColor(ContextCompat.getColor(this.mContext, color.search_google_icon));
                this.setMenuColor(ContextCompat.getColor(this.mContext, color.search_google_menu));
                this.setHintColor(ContextCompat.getColor(this.mContext, color.search_google_hint));
                this.setTextColor(ContextCompat.getColor(this.mContext, color.search_google_title));
                break;
            case Theme.LIGHT:
                this.setBackgroundColor(ContextCompat.getColor(this.mContext, color.search_light_background));
                this.setDividerColor(ContextCompat.getColor(this.mContext, color.search_light_divider));
                this.setLogoColor(ContextCompat.getColor(this.mContext, color.search_light_icon));
                this.setMicColor(ContextCompat.getColor(this.mContext, color.search_light_icon));
                this.setClearColor(ContextCompat.getColor(this.mContext, color.search_light_icon));
                this.setMenuColor(ContextCompat.getColor(this.mContext, color.search_light_icon));
                this.setHintColor(ContextCompat.getColor(this.mContext, color.search_light_hint));
                this.setTextColor(ContextCompat.getColor(this.mContext, color.search_light_title));
                break;
            case Theme.DARK:
                this.setBackgroundColor(ContextCompat.getColor(this.mContext, color.search_dark_background));
                this.setDividerColor(ContextCompat.getColor(this.mContext, color.search_dark_divider));
                this.setLogoColor(ContextCompat.getColor(this.mContext, color.search_dark_icon));
                this.setMicColor(ContextCompat.getColor(this.mContext, color.search_dark_icon));
                this.setClearColor(ContextCompat.getColor(this.mContext, color.search_dark_icon));
                this.setMenuColor(ContextCompat.getColor(this.mContext, color.search_dark_icon));
                this.setHintColor(ContextCompat.getColor(this.mContext, color.search_dark_hint));
                this.setTextColor(ContextCompat.getColor(this.mContext, color.search_dark_title));
                break;
        }
    }

    @VersionMargins
    public int getVersionMargins() {
        return this.mVersionMargins;
    }

    void setVersionMargins(@VersionMargins int versionMargins) {
        this.mVersionMargins = versionMargins;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int left, top, right, bottom;

        switch (this.mVersionMargins) {
            case VersionMargins.BAR:
                left = this.mContext.getResources().getDimensionPixelSize(dimen.search_bar_margin_left_right);
                top = this.mContext.getResources().getDimensionPixelSize(dimen.search_bar_margin_top);
                right = this.mContext.getResources().getDimensionPixelSize(dimen.search_bar_margin_left_right);
                bottom = this.mContext.getResources().getDimensionPixelSize(dimen.search_bar_margin_bottom);

                params.setMargins(left, top, right, bottom);

                this.mCardView.setLayoutParams(params);
                break;
            case VersionMargins.TOOLBAR:
                left = this.mContext.getResources().getDimensionPixelSize(dimen.search_toolbar_margin_left_right);
                top = this.mContext.getResources().getDimensionPixelSize(dimen.search_toolbar_margin_top_bottom);
                right = this.mContext.getResources().getDimensionPixelSize(dimen.search_toolbar_margin_left_right);
                bottom = this.mContext.getResources().getDimensionPixelSize(dimen.search_toolbar_margin_top_bottom);

                params.setMargins(left, top, right, bottom);

                this.mCardView.setLayoutParams(params);
                break;
            case VersionMargins.MENU_ITEM:
                left = this.mContext.getResources().getDimensionPixelSize(dimen.search_menu_item_margin);
                top = this.mContext.getResources().getDimensionPixelSize(dimen.search_menu_item_margin);
                right = this.mContext.getResources().getDimensionPixelSize(dimen.search_menu_item_margin);
                bottom = this.mContext.getResources().getDimensionPixelSize(dimen.search_menu_item_margin);

                params.setMargins(left, top, right, bottom);

                this.mCardView.setLayoutParams(params);
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Logo
    void setLogoIcon(@DrawableRes int resource) {
        this.mImageViewLogo.setImageResource(resource);
    }

    public void setLogoIcon(@Nullable Drawable drawable) {
        if (drawable != null) {
            this.mImageViewLogo.setImageDrawable(drawable);
        } else {
            this.mImageViewLogo.setVisibility(View.GONE);
        }
    }

    void setLogoColor(@ColorInt int color) {
        this.mImageViewLogo.setColorFilter(color);
    }

    // Mic
    void setMicIcon(@DrawableRes int resource) {
        this.mImageViewMic.setImageResource(resource);
    }

    public void setMicIcon(@Nullable Drawable drawable) {
        this.mImageViewMic.setImageDrawable(drawable);
    }

    void setMicColor(@ColorInt int color) {
        this.mImageViewMic.setColorFilter(color);
    }

    // Menu
    void setMenuIcon(@DrawableRes int resource) {
        this.mImageViewMenu.setImageResource(resource);
    }

    public void setMenuIcon(@Nullable Drawable drawable) {
        this.mImageViewMenu.setImageDrawable(drawable);
    }

    void setMenuColor(@ColorInt int color) {
        this.mImageViewMenu.setColorFilter(color);
    }

    // Text
    public void setTextImeOptions(int imeOptions) {
        this.mSearchEditText.setImeOptions(imeOptions);
    }

    public void setTextInputType(int inputType) {
        this.mSearchEditText.setInputType(inputType);
    }

    public Editable getText() {
        return this.mSearchEditText.getText();
    }

    void setText(CharSequence text) {
        this.mSearchEditText.setText(text);
    }

    public void setText(@StringRes int text) {
        this.mSearchEditText.setText(text);
    }

    void setTextColor(@ColorInt int color) {
        this.mSearchEditText.setTextColor(color);
    }

    void setTextSize(float size) {
        this.mSearchEditText.setTextSize(size);
    }

    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     */
    void setTextStyle(int style) {
        this.mTextStyle = style;
        this.mSearchEditText.setTypeface((Typeface.create(this.mTextFont, this.mTextStyle)));
    }

    /**
     * Typeface.DEFAULT
     * Typeface.DEFAULT_BOLD
     * Typeface.SANS_SERIF
     * Typeface.SERIF
     * Typeface.MONOSPACE
     */
    public void setTextFont(Typeface font) {
        this.mTextFont = font;
        this.mSearchEditText.setTypeface((Typeface.create(this.mTextFont, this.mTextStyle)));
    }

    /*
    * Use Gravity or GravityCompat
    */
    public void setTextGravity(int gravity) {
        this.mSearchEditText.setGravity(gravity);
    }

    // Hint
    public void setHint(CharSequence hint) {
        this.mSearchEditText.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        this.mSearchEditText.setHint(hint);
    }

    void setHintColor(@ColorInt int color) {
        this.mSearchEditText.setHintTextColor(color);
    }

    // Query
    public Editable getQuery() {
        return this.mSearchEditText.getText();
    }

    public void setQuery(CharSequence query, boolean submit) {
        this.mSearchEditText.setText(query);
        if (query != null) {
            this.mSearchEditText.setSelection(this.mSearchEditText.length());
            this.mQueryText = query;
        }

        if (submit && !TextUtils.isEmpty(query)) {
            this.onSubmitQuery();
        }
    }

    public void setQuery(@StringRes int query, boolean submit) {
        this.mSearchEditText.setText(query);
        if (query != 0) {
            this.mSearchEditText.setSelection(this.mSearchEditText.length());
            this.mQueryText = String.valueOf(query);
        }

        if (submit && !(String.valueOf(query).isEmpty())) {
            this.onSubmitQuery();
        }
    }

    // Height
    public int getCustomHeight() {
        ViewGroup.LayoutParams params = this.mLinearLayout.getLayoutParams();
        return params.height;
    }

    public void setCustomHeight(int height) {
        ViewGroup.LayoutParams params = this.mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        this.mLinearLayout.setLayoutParams(params);
    }

    // Overrides
    @Override
    public void setElevation(float elevation) {
        this.mCardView.setMaxCardElevation(elevation);
        this.mCardView.setCardElevation(elevation);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        this.mCardView.setCardBackgroundColor(color);
    }

    //@FloatRange(from = 0.5, to = 1.0)
    // Others
    public boolean isOpen() {
        return this.getVisibility() == View.VISIBLE;
    }

    public void open(boolean iconAnimation) {
        this.mIconAnimation = iconAnimation;
        this.open();
    }

    public void close(boolean iconAnimation) {
        this.mIconAnimation = iconAnimation;
        this.close();
    }

    // Listeners
    public void setOnMicClickListener(Search.OnMicClickListener listener) {
        this.mOnMicClickListener = listener;
        if (this.mOnMicClickListener != null) {
            this.mImageViewMic.setVisibility(View.VISIBLE);
            if (this.mTheme == Theme.GOOGLE) {
                this.mImageViewMic.setImageDrawable(ContextCompat.getDrawable(this.mContext, drawable.search_ic_mic_color_24dp));
            } else {
                this.mImageViewMic.setImageDrawable(ContextCompat.getDrawable(this.mContext, drawable.search_ic_mic_black_24dp));
            }
        } else {
            this.mImageViewMic.setVisibility(View.GONE);
        }
    }

    public void setOnMenuClickListener(Search.OnMenuClickListener listener) {
        this.mOnMenuClickListener = listener;
        if (this.mOnMenuClickListener != null) {
            this.mImageViewMenu.setVisibility(View.VISIBLE);
            this.mImageViewMenu.setImageDrawable(ContextCompat.getDrawable(this.mContext, drawable.search_ic_menu_black_24dp));
        } else {
            this.mImageViewMenu.setVisibility(View.GONE);
        }
    }

    public void setOnQueryTextListener(Search.OnQueryTextListener listener) {
        mOnQueryTextListener = listener;
    }

    // ---------------------------------------------------------------------------------------------
    void setDividerColor(@ColorInt int color) {

    }

    void setClearColor(@ColorInt int color) {

    }

    void showKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(mSearchEditText, 0);
            }
        }
    }

    void hideKeyboard() {
        if (!isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    private void clearIconsColor() {
        mImageViewLogo.clearColorFilter();
        mImageViewMic.clearColorFilter();
        if (mImageViewClear != null) {
            mImageViewClear.clearColorFilter();
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(query);
            }
        }
    }

}
