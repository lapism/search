package com.lapism.searchview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class SearchView extends FrameLayout implements Filter.FilterListener {

    public static final int STYLE_CLASSIC = 0;
    public static final int STYLE_COLOR = 1;
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    public static final int SPEECH_REQUEST_CODE = 0;

    private int mStyle = 0;
    private boolean mIsSearchOpen = false;
    private boolean mClearingFocus;
    private boolean allowVoiceSearch;
    private final Context mContext;
    private RecyclerView mSuggestionsRecyclerView;
    private CardView mCardView;
    private EditText mSearchEditText;
    private ImageView mBackImageView;
    private ImageView mVoiceImageView;
    private ImageView mEmptyImageView;
    private View mTintView;
    private View mSeparatorView;
    private SearchAdapter mSearchAdapter;
    private OnQueryTextListener mOnQueryChangeListener;
    private SearchViewListener mSearchViewListener;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private SavedState mSavedState;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);
        setVisibility(View.INVISIBLE);

        SearchLinearLayoutManager layoutManager = new SearchLinearLayoutManager(mContext, SearchLinearLayoutManager.VERTICAL, false);
        layoutManager.clearChildSize();
        layoutManager.setChildSize(getResources().getDimensionPixelSize(R.dimen.search_item_height));

        mSuggestionsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSuggestionsRecyclerView.setLayoutManager(layoutManager);
        //mSuggestionsRecyclerView.setHasFixedSize(true);

        mSuggestionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSuggestionsRecyclerView.setVisibility(View.GONE);

        mCardView = (CardView) findViewById(R.id.cardView);
        mCardView.setVisibility(View.INVISIBLE);
        mSearchEditText = (EditText) findViewById(R.id.editText_input);
        mSearchEditText.setOnClickListener(mOnClickListener);
        mBackImageView = (ImageView) findViewById(R.id.imageView_arrow_back);
        mBackImageView.setOnClickListener(mOnClickListener);
        mVoiceImageView = (ImageView) findViewById(R.id.imageView_mic);
        mVoiceImageView.setOnClickListener(mOnClickListener);
        mEmptyImageView = (ImageView) findViewById(R.id.imageView_clear);
        mEmptyImageView.setOnClickListener(mOnClickListener);
        mTintView = findViewById(R.id.view_transparent);
        mTintView.setOnClickListener(mOnClickListener);
        mSeparatorView = findViewById(R.id.view_separator);
        mSeparatorView.setVisibility(View.GONE);

        allowVoiceSearch = false;
        showVoice(true);
        initSearchView();
    }

    private boolean isVoiceAvailable() {
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return (activities.size() != 0);
    }

    private void initStyle(AttributeSet attributeSet, int defStyleAttr) {
        TypedArray attr = mContext.obtainStyledAttributes(attributeSet, R.styleable.SearchView, defStyleAttr, 0);
        if (attr != null) {
            if (attr.hasValue(R.styleable.SearchView_search_style)) {
                setStyle(attr.getInt(R.styleable.SearchView_search_style, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_theme)) {
                setTheme(attr.getInt(R.styleable.SearchView_search_theme, 0));
            }
            if (attr.hasValue(R.styleable.SearchView_search_divider)) {
                setDivider(attr.getBoolean(R.styleable.SearchView_search_divider, false));
            }
            attr.recycle();
        }
    }

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, SPEECH_REQUEST_CODE);
        }
    }

    private void showSuggestions() {
        if (mSearchAdapter != null && mSearchAdapter.getItemCount() > 0 && mSuggestionsRecyclerView.getVisibility() == View.GONE) {
            mSuggestionsRecyclerView.setVisibility(View.VISIBLE);
            mSeparatorView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSuggestions() {
        if (mSuggestionsRecyclerView.getVisibility() == View.VISIBLE) {
            mSuggestionsRecyclerView.setVisibility(View.GONE);
            mSeparatorView.setVisibility(View.GONE);
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(null);
            }
        }
    }

    private void startFilter(CharSequence s) {
        if (mSearchAdapter != null) {
            (mSearchAdapter).getFilter().filter(s, SearchView.this);
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mBackImageView) {
                closeSearch(true);
            } else if (v == mVoiceImageView) {
                onVoiceClicked();
            } else if (v == mEmptyImageView) {
                mSearchEditText.setText(null);
            } else if (v == mSearchEditText) {
                showSuggestions();
            } else if (v == mTintView) {
                closeSearch(true);
            }
        }
    };

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) mSearchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mSearchEditText, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mSearchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchEditText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyImageView.setVisibility(View.VISIBLE);
            showVoice(false);
        } else {
            mEmptyImageView.setVisibility(View.GONE);
            showVoice(true);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void showVoice(boolean show) {
        if (show && isVoiceAvailable() && allowVoiceSearch) {
            mVoiceImageView.setVisibility(View.VISIBLE);
        } else {
            mVoiceImageView.setVisibility(View.GONE);
        }
    }

    /* Need update ********************************************************************************/
    private void initSearchView() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
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

        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard();
                    showSuggestions();
                } else {
                    hideKeyboard();
                    hideSuggestions();
                }
            }
        });
    }

    /* Need update ********************************************************************************/

    public void setVoiceSearch(boolean voiceSearch) {
        allowVoiceSearch = voiceSearch;
    }

    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    public void setHint(int hint) {
        mSearchEditText.setHint(hint);
    }

    public void setStyle(int style) {
        if (style == STYLE_CLASSIC) {
            mBackImageView.setImageResource(R.drawable.search_ic_arrow_back_black_24dp);
            mVoiceImageView.setImageResource(R.drawable.search_ic_mic_black_24dp);
            mEmptyImageView.setImageResource(R.drawable.search_ic_clear_black_24dp);
        }
        if (style == STYLE_COLOR) {
            mBackImageView.setImageResource(R.drawable.search_ic_arrow_back_color_24dp);
            mVoiceImageView.setImageResource(R.drawable.search_ic_mic_color_24dp);
            mEmptyImageView.setImageResource(R.drawable.search_ic_clear_color_24dp);
        }
        mStyle = style;
    }

    public void setTheme(int theme) {
        if (theme == THEME_LIGHT) {
            if (mStyle == STYLE_CLASSIC) {
                mBackImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
                mVoiceImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
                mEmptyImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            }
            mSeparatorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_separator));
            mSuggestionsRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mSearchEditText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mSearchEditText.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
            mSearchEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.search_light_text_hint));
        }
        if (theme == THEME_DARK) {
            if (mStyle == STYLE_CLASSIC) {
                mBackImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                mVoiceImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                mEmptyImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            }
            mSeparatorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_separator));
            mSuggestionsRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mSearchEditText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mSearchEditText.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
            mSearchEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text_hint));
        }
    }

    public void setDivider(boolean divider) {
        if (divider) {
            mSuggestionsRecyclerView.addItemDecoration(new SearchViewDivider(mContext, null));
        }
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    public void setAdapter(SearchAdapter adapter) {
        mSearchAdapter = adapter;
        mSuggestionsRecyclerView.setAdapter(adapter);
        startFilter(mSearchEditText.getText());
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }

    public void showSearch(boolean animate) {
        if (isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        mSearchEditText.requestFocus();

        setVisibility(View.VISIBLE);
        if (animate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                revealInAnimation();
            } else {
                SearchAnimator.fadeInAnimation(mCardView, SearchAnimator.ANIMATION_DURATION);
            }
        }
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewShown();
        }
        mIsSearchOpen = true;
    }

    public void closeSearch(boolean animate) {
        if (!isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        mSearchEditText.clearFocus();
        clearFocus();

        if (animate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SearchAnimator.revealOutAnimation(mContext, mCardView, SearchAnimator.ANIMATION_DURATION);
            } else {
                SearchAnimator.fadeOutAnimation(mCardView, SearchAnimator.ANIMATION_DURATION);
            }
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(View.GONE);
                    if (mSearchViewListener != null) {
                        mSearchViewListener.onSearchViewClosed();
                    }
                }
            }, SearchAnimator.ANIMATION_DURATION);
        } else {
            setVisibility(View.GONE);
            if (mSearchViewListener != null) {
                mSearchViewListener.onSearchViewClosed();
            }
        }
        mIsSearchOpen = false;
    }

    public void setQuery(CharSequence query, boolean submit) {
        mSearchEditText.setText(query);
        if (query != null) {
            mSearchEditText.setSelection(mSearchEditText.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return !mClearingFocus && isFocusable() && mSearchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard();
        super.clearFocus();
        mSearchEditText.clearFocus();
        mClearingFocus = false;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = this.mIsSearchOpen;
        return mSavedState;
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
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        mSavedState = (SavedState) state;
        if (mSavedState.isSearchOpen) {
            showSearch(true);
            setQuery(mSavedState.query, false);
        }
        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {

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
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealInAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    SearchAnimator.revealInAnimation(mContext, mCardView, SearchAnimator.ANIMATION_DURATION);
                }
            });
        }
    }

    @Deprecated
    public void setMenuItem(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch(true);
                return true;
            }
        });
    }

        /*private void setUpLayoutTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            LinearLayout searchRoot = (LinearLayout) findViewById(R.id.search_layout_item);
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setDuration(SearchAnimator.ANIMATION_DURATION);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                // layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
                layoutTransition.setStartDelay(LayoutTransition.CHANGING, 0);
            }
            layoutTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
            mCardView.setLayoutTransition(layoutTransition);
        }
    }*/

}