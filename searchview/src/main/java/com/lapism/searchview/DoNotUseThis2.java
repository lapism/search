package com.lapism.searchview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.R;
import android.support.v7.widget.*;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import java.util.WeakHashMap;


public class DoNotUseThis2 extends LinearLayoutCompat {

    static final boolean DBG = false;
    static final String LOG_TAG = "SearchView";


    final SearchEditText mSearchSrcTextView;

    private Rect mSearchSrcTextViewBounds = new Rect();
    private Rect mSearchSrtTextViewBoundsExpanded = new Rect();
    private int[] mTemp = new int[2];
    private int[] mTemp2 = new int[2];

    OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private OnClickListener mOnSearchClickListener;
    private boolean mIconifiedByDefault;
    private boolean mIconified;
    CursorAdapter mSuggestionsAdapter;
    private boolean mSubmitButtonEnabled;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private boolean mClearingFocus;
    private int mMaxWidth;
    private boolean mVoiceButtonEnabled;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private boolean mExpandedInActionView;
    private int mCollapsedImeOptions;
    private Bundle mAppSearchData;

    private final Runnable mUpdateDrawableStateRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache =
            new WeakHashMap<String, Drawable.ConstantState>();

    public DoNotUseThis2(Context context) {
        this(context, null);
    }

    public DoNotUseThis2(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }

    public DoNotUseThis2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SearchView, defStyleAttr, 0);

        final int layoutResId = a.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view);

        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layoutResId, this, true);

        //ViewCompat.setBackground(mSearchPlate, a.getDrawable(R.styleable.SearchView_queryBackground));

        mSearchSrcTextView = (SearchEditText) findViewById(R.id.search_src_text);
        mSearchSrcTextView.setOnKeyListener(mTextKeyListener);
    }


    View.OnKeyListener mTextKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (!TextUtils.isEmpty(mSearchSrcTextView.getText()) && KeyEventCompat.hasNoModifiers(event)) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();
                        return true;
                    }
                }
            }
            return false;
        }
    };
       /* mDropDownAnchor = findViewById(mSearchSrcTextView.getDropDownAnchor());
        mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adjustDropDownSizeAndPosition();
            }
        });
        mDropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                adjustDropDownSizeAndPosition();
            }
        });*/

        // mSearchSrcTextView.setHint(getDecoratedHint(hint == null ? "" : hint));
    }

    /*private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mSearchButton) {
                onSearchClicked();
            } else if (v == mCloseButton) {
                onCloseClicked();
            } else if (v == mGoButton) {
                onSubmitQuery();
            } else if (v == mVoiceButton) {
                onVoiceClicked();
            } else if (v == mSearchSrcTextView) {
                forceSuggestionQuery();
            }
        }
    };*/




/*
    private void updateVoiceButton(boolean empty) {
        int visibility = GONE;
        if (mVoiceButtonEnabled && !isIconified() && empty) {
            visibility = VISIBLE;
            mGoButton.setVisibility(GONE);
        }
        mVoiceButton.setVisibility(visibility);
    }

    private void updateCloseButton() {
        final boolean hasText = !TextUtils.isEmpty(mSearchSrcTextView.getText());
        mCloseButton.setVisibility(showClose ? VISIBLE : GONE);
    }


    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        updateVoiceButton(!hasText);
        updateCloseButton();
        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchSrcTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null
                    || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                if (mSearchable != null) {
                    launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null, query.toString());
                }
                setImeVisibility(false);
                dismissSuggestions();
            }
        }
    }

    private void onCloseClicked() {
        CharSequence text = mSearchSrcTextView.getText();
        if (!TextUtils.isEmpty(text)) {
            mSearchSrcTextView.setText("");
            mSearchSrcTextView.requestFocus();
            setImeVisibility(true);
        }
    }

    private void onSearchClicked() {
        mSearchSrcTextView.requestFocus();
        setImeVisibility(true);
        if (mOnSearchClickListener != null) {
            mOnSearchClickListener.onClick(this);
        }
    }

*/

    /**
     * @hide
     */
    //@RestrictTo(GROUP_ID)/
    //AppCompatAutoCompleteTextView {

    /**
     * Sets the APP_DATA for legacy SearchDialog use.
     *
     * @param appSearchData bundle provided by the app when launching the search dialog
     * @hide
     */
    //@RestrictTo(GROUP_ID)


