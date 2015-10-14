package com.lapism.searchview;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class SearchView extends FrameLayout implements Filter.FilterListener {

    public static final int REQUEST_VOICE = 9999;

    public static final int STYLE_CLASSIC = 0;
    public static final int STYLE_COLOR = 1;
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    private boolean mIsSearchOpen = false;
    private boolean mClearingFocus;
    private View mTintView;
    private View mSeparatorView;
    private RecyclerView mSuggestionsRecyclerView;
    private EditText mSearchEditText;
    private ImageView mBackImageView;
    private ImageView mVoiceImageView;
    private ImageView mEmptyImageView;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private OnQueryTextListener mOnQueryChangeListener;
    private SearchViewListener mSearchViewListener;
    private SearchViewAdapter mAdapter;
    private SavedState mSavedState;
    private final Context mContext;
    private CardView mCardView;
    private int mStyle = 0;


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

    private void initView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);
        setVisibility(GONE);

        mCardView = (CardView) findViewById(R.id.cardView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mSuggestionsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSuggestionsRecyclerView.setLayoutManager(layoutManager);
        mSuggestionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSuggestionsRecyclerView.setVisibility(GONE);

        mSearchEditText = (EditText) findViewById(R.id.editText_input);
        mBackImageView = (ImageView) findViewById(R.id.imageView_arrow_back);
        mVoiceImageView = (ImageView) findViewById(R.id.imageView_mic);
        mEmptyImageView = (ImageView) findViewById(R.id.imageView_clear);
        mTintView = findViewById(R.id.view_transparent);
        mSeparatorView = findViewById(R.id.view_separator);

        mSearchEditText.setOnClickListener(mOnClickListener);
        mBackImageView.setOnClickListener(mOnClickListener);
        mVoiceImageView.setOnClickListener(mOnClickListener);
        mEmptyImageView.setOnClickListener(mOnClickListener);
        mTintView.setOnClickListener(mOnClickListener);
        mSeparatorView.setVisibility(GONE);

        showVoice(true);
        initSearchView();
    }

    private void initStyle(AttributeSet attributeSet, int defStyleAttr) {
        TypedArray attr = mContext.obtainStyledAttributes(attributeSet, R.styleable.SearchView, defStyleAttr, 0);
        if (attr != null) {
            try {
                if (attr.hasValue(R.styleable.SearchView_search_style)) {
                    setStyle(attr.getInt(R.styleable.SearchView_search_style, 0));
                }
                if (attr.hasValue(R.styleable.SearchView_search_theme)) {
                    setTheme(attr.getInt(R.styleable.SearchView_search_theme, 0));
                }
                if (attr.hasValue(R.styleable.SearchView_search_divider)) {
                    setDivider(attr.getBoolean(R.styleable.SearchView_search_divider, false));
                }
            } finally {
                attr.recycle();
            }
        }
    }

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
            }
        });

        mCardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                showSearch();
            }
        });
    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null) {
            (mAdapter).getFilter().filter(s, SearchView.this);
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mBackImageView) {
                closeSearch();
            } else if (v == mVoiceImageView) {
                onVoiceClicked();
            } else if (v == mEmptyImageView) {
                mSearchEditText.setText(null);
            } else if (v == mSearchEditText) {
                showSuggestions();
            } else if (v == mTintView) {
                closeSearch();
            }
        }
    };

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, REQUEST_VOICE);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchEditText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyImageView.setVisibility(VISIBLE);
            showVoice(false);
        } else {
            mEmptyImageView.setVisibility(GONE);
            showVoice(true);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                closeSearch();
            }
        }
    }

    private boolean isVoiceAvailable() {
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return (activities.size() != 0);
    }

    private void showSuggestions() {
        if (mAdapter != null && mAdapter.getItemCount() > 0 && mSuggestionsRecyclerView.getVisibility() == GONE) {
            mSuggestionsRecyclerView.setVisibility(VISIBLE);
            mSeparatorView.setVisibility(VISIBLE);
        }
    }

    private void hideSuggestions() {
        if (mSuggestionsRecyclerView.getVisibility() == VISIBLE) {
            mSuggestionsRecyclerView.setVisibility(GONE);
            mSeparatorView.setVisibility(GONE);
        }
    }

    public void setAdapter(SearchViewAdapter adapter) {
        mAdapter = adapter;
        mSuggestionsRecyclerView.setAdapter(adapter);
        startFilter(mSearchEditText.getText());
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

    public void setMenuItem(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                return true;
            }
        });
    }

    //@Deprecated
    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    public void showSearch() {
        if(isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        mSearchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mSearchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mSearchEditText, 0);

        showSuggestions();
        mIsSearchOpen = true;

        setVisibility(View.VISIBLE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewShown();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SearchViewAnimation.revealInAnimation(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        } else {
            SearchViewAnimation.fadeInAnimation(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        }
    }

    public void closeSearch() {
        if(!isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        mSearchEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//////////////////////
        imm.hideSoftInputFromWindow( mSearchEditText.getWindowToken(), 0);

        hideSuggestions();
        mIsSearchOpen = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SearchViewAnimation.revealOutAnimation(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        } else {
            SearchViewAnimation.fadeOutAnimation(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        }
        postDelayed(new Runnable() {

            @Override
            public void run() {
                setVisibility(View.GONE);
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewClosed();
                }
            }
        }, SearchViewAnimation.ANIMATION_DURATION);
    }

    public void showVoice(boolean show) {
        if (show && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(VISIBLE);
        } else {
            mVoiceImageView.setVisibility(GONE);
        }
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
            mSuggestionsRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, null));
        }
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }

    public interface OnQueryTextListener {

        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
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
    public void clearFocus() {
        mClearingFocus = true;
        super.clearFocus();
        mSearchEditText.clearFocus();
        mClearingFocus = false;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return !mClearingFocus && isFocusable() && mSearchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        mSavedState = (SavedState) state;
        if (mSavedState.isSearchOpen) {
            showSearch();
            setQuery(mSavedState.query, false);
        }
        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = mIsSearchOpen;
        return mSavedState;
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