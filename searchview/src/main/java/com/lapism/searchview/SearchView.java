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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class SearchView extends RelativeLayout implements Filter.FilterListener {

    public static final int REQUEST_VOICE = 9999;

    public static final int STYLE_CLASSIC = 0;
    public static final int STYLE_COLOR = 1;
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    private boolean mIsSearchOpen = false;
    private boolean mClearingFocus;
    private View mSearchLayout;
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
    private Context mContext;
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
        initiateView();
        initStyle(attrs, defStyleAttr);
    }

    private void initiateView() {
        LayoutInflater.from(mContext).inflate((R.layout.search_view), this, true);

        mSearchLayout = findViewById(R.id.search_layout);
        mCardView = (CardView) mSearchLayout.findViewById(R.id.cardView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mSuggestionsRecyclerView = (RecyclerView) mSearchLayout.findViewById(R.id.recyclerView);
        mSuggestionsRecyclerView.setLayoutManager(layoutManager);
        mSuggestionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSuggestionsRecyclerView.setVisibility(GONE);

        mSearchEditText = (EditText) mSearchLayout.findViewById(R.id.editText_input);
        mBackImageView = (ImageView) mSearchLayout.findViewById(R.id.imageView_arrow_back);
        mVoiceImageView = (ImageView) mSearchLayout.findViewById(R.id.imageView_mic);
        mEmptyImageView = (ImageView) mSearchLayout.findViewById(R.id.imageView_clear);// CLEAR
        mTintView = mSearchLayout.findViewById(R.id.view_transparent);
        mSeparatorView = mSearchLayout.findViewById(R.id.view_separator);

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

    public void setStyle(int style) {
        if (style == STYLE_CLASSIC) {
            mBackImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            mVoiceImageView.setImageResource(R.drawable.ic_mic_black_24dp);
            mEmptyImageView.setImageResource(R.drawable.ic_clear_black_24dp);
        }
        if (style == STYLE_COLOR) {
            mBackImageView.setImageResource(R.drawable.ic_arrow_back_color_24dp);
            mVoiceImageView.setImageResource(R.drawable.ic_mic_color_24dp);
            mEmptyImageView.setImageResource(R.drawable.ic_clear_color_24dp);
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
                    showKeyboard(mSearchEditText);
                    showSuggestions();
                }
            }
        });
    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null) {
            (mAdapter).getFilter().filter(s, SearchView.this);
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

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

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public void showSuggestions() {
        if (mAdapter != null && mAdapter.getItemCount() > 0 && mSuggestionsRecyclerView.getVisibility() == GONE) {
            mSuggestionsRecyclerView.setVisibility(VISIBLE);
            mSeparatorView.setVisibility(VISIBLE);
        }
    }

    public void showVoice(boolean show) {
        if (show && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(VISIBLE);
        } else {
            mVoiceImageView.setVisibility(GONE);
        }
    }

    public void dismissSuggestions() {
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
                showSearch();// first show bug
                return true;
            }
        });
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    public void showSearch() {
        if (isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        mSearchEditText.requestFocus();
        mIsSearchOpen = true;
        mSearchLayout.setVisibility(View.VISIBLE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewShown();
        }
        setVisibleWithAnimationIn();
    }

    public void closeSearch() {
        if (!isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        dismissSuggestions();
        clearFocus();
        mIsSearchOpen = false;
        setVisibleWithAnimationOut();
        mSearchLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchLayout.setVisibility(View.GONE);
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewClosed();
                }
            }
        }, SearchViewAnimation.ANIMATION_DURATION);
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchEditText.clearFocus();
        mClearingFocus = false;
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
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (mClearingFocus) return false;
        if (!isFocusable()) return false;
        return mSearchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = this.mIsSearchOpen;
        return mSavedState;
    }

    private static class SavedState extends BaseSavedState {
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

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
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

    private void setVisibleWithAnimationIn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SearchViewAnimation.revealIn(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        } else {
            SearchViewAnimation.fadeIn(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        }
    }

    private void setVisibleWithAnimationOut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SearchViewAnimation.revealOut(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        } else {
            SearchViewAnimation.fadeOut(mCardView, SearchViewAnimation.ANIMATION_DURATION);
        }
    }

}