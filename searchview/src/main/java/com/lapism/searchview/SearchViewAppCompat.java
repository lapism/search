package com.lapism.searchview;


/*
public class SearchViewAppCompat extends LinearLayoutCompat implements CollapsibleActionView {

    //FrameLayoutCompat gfa;
    static final boolean DBG = false;
    static final String LOG_TAG = "SearchView";

    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    final SearchAutoComplete mSearchSrcTextView;
    private final View mSearchEditFrame;
    private final View mSearchPlate;
    private final View mSubmitArea;
    final ImageView mSearchButton;
    final ImageView mGoButton;
    final ImageView mCloseButton;
    final ImageView mVoiceButton;
    private final View mDropDownAnchor;
    private UpdatableTouchDelegate mTouchDelegate;
    private final Rect mSearchSrcTextViewBounds = new Rect();
    private final Rect mSearchSrtTextViewBoundsExpanded = new Rect();
    private final int[] mTemp = new int[2];
    private final int[] mTemp2 = new int[2];
    private final ImageView mCollapsedIcon;
    private final Drawable mSearchHintIcon;
    private final int mSuggestionRowLayout;
    private final int mSuggestionCommitIconResId;
    private final Intent mVoiceWebSearchIntent;
    private final Intent mVoiceAppSearchIntent;
    private final CharSequence mDefaultQueryHint;
    private OnQueryTextListener mOnQueryChangeListener;
    private OnCloseListener mOnCloseListener;
    OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private OnSuggestionListener mOnSuggestionListener;
    private OnClickListener mOnSearchClickListener;
    private boolean mIconifiedByDefault;

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
    SearchableInfo mSearchable;
    private Bundle mAppSearchData;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache = new WeakHashMap<>();
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();

    private final Runnable mShowImeRunnable = () -> {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(imm, SearchViewAppCompat.this, 0);
        }
    };

    private final Runnable mUpdateDrawableStateRunnable = this::updateFocusedState;

    private final Runnable mReleaseCursorRunnable = new Runnable() {
        @Override
        public void run() {
            if (mSuggestionsAdapter != null && mSuggestionsAdapter instanceof SuggestionsAdapter) {
                mSuggestionsAdapter.changeCursor(null);
            }
        }
    };


    public SearchViewAppCompat(Context context) {
        this(context, null);
    }

    public SearchViewAppCompat(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }

    public SearchViewAppCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SearchView, defStyleAttr, 0);

        final LayoutInflater inflater = LayoutInflater.from(context);
        final int layoutResId = a.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view);
        inflater.inflate(layoutResId, this, true);

        mSearchSrcTextView = (SearchAutoComplete) findViewById(R.id.search_src_text);
        mSearchSrcTextView.setSearchView(this);

        mSearchEditFrame = findViewById(R.id.search_edit_frame);
        mSearchPlate = findViewById(R.id.search_plate);
        mSubmitArea = findViewById(R.id.submit_area);
        mSearchButton = (ImageView) findViewById(R.id.search_button);
        mGoButton = (ImageView) findViewById(R.id.search_go_btn);
        mCloseButton = (ImageView) findViewById(R.id.search_close_btn);
        mVoiceButton = (ImageView) findViewById(R.id.search_voice_btn);
        mCollapsedIcon = (ImageView) findViewById(R.id.search_mag_icon);

        ViewCompat.setBackground(mSearchPlate, a.getDrawable(R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(mSubmitArea, a.getDrawable(R.styleable.SearchView_submitBackground));
        mSearchButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
        mGoButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_goIcon));
        mCloseButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_closeIcon));
        mVoiceButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_voiceIcon));
        mCollapsedIcon.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));

        mSearchHintIcon = a.getDrawable(R.styleable.SearchView_searchHintIcon);
        mSuggestionRowLayout = a.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
        mSuggestionCommitIconResId = a.getResourceId(R.styleable.SearchView_commitIcon, 0);

        //trida , impelemtns a nebo tohle
        OnClickListener mOnClickListener = v -> {
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
        };
        mSearchButton.setOnClickListener(mOnClickListener);
        mCloseButton.setOnClickListener(mOnClickListener);
        mGoButton.setOnClickListener(mOnClickListener);
        mVoiceButton.setOnClickListener(mOnClickListener);
        mSearchSrcTextView.setOnClickListener(mOnClickListener);

        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                SearchViewAppCompat.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mSearchSrcTextView.addTextChangedListener(mTextWatcher);
        OnEditorActionListener mOnEditorActionListener = (v, actionId, event) -> {
            onSubmitQuery();
            return true;
        };
        mSearchSrcTextView.setOnEditorActionListener(mOnEditorActionListener);
        OnItemClickListener mOnItemClickListener = (parent, view, position, id) -> {
            if (DBG) Log.d(LOG_TAG, "onItemClick() position " + position);
            onItemClicked(position, KeyEvent.KEYCODE_UNKNOWN, null);
        };
        mSearchSrcTextView.setOnItemClickListener(mOnItemClickListener);
        OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (DBG) Log.d(LOG_TAG, "onItemSelected() position " + position);
                SearchViewAppCompat.this.onItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (DBG)
                    Log.d(LOG_TAG, "onNothingSelected()");
            }
        };
        mSearchSrcTextView.setOnItemSelectedListener(mOnItemSelectedListener);
        mSearchSrcTextView.setOnKeyListener(mTextKeyListener);
        mSearchSrcTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (mOnQueryTextFocusChangeListener != null) {
                mOnQueryTextFocusChangeListener.onFocusChange(SearchViewAppCompat.this, hasFocus);
            }
        });
        setIconifiedByDefault(a.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));

        final int maxWidth = a.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
        if (maxWidth != -1) {
            setMaxWidth(maxWidth);
        }

        mDefaultQueryHint = a.getText(R.styleable.SearchView_defaultQueryHint);
        mQueryHint = a.getText(R.styleable.SearchView_queryHint);

        final int imeOptions = a.getInt(R.styleable.SearchView_android_imeOptions, -1);
        if (imeOptions != -1) {
            setImeOptions(imeOptions);
        }

        final int inputType = a.getInt(R.styleable.SearchView_android_inputType, -1);
        if (inputType != -1) {
            setInputType(inputType);
        }

        boolean focusable = a.getBoolean(R.styleable.SearchView_android_focusable, true);
        setFocusable(focusable);

        a.recycle();

        mVoiceWebSearchIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        mVoiceWebSearchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mVoiceWebSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mVoiceAppSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mVoiceAppSearchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mDropDownAnchor = findViewById(mSearchSrcTextView.getDropDownAnchor());
        if (mDropDownAnchor != null) {
            addOnLayoutChangeListenerToDropDownAnchorSDK11();
        }

        updateViewsVisibility(mIconifiedByDefault);
        updateQueryHint();
    }

    private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
        mDropDownAnchor.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> adjustDropDownSizeAndPosition());
    }

    private void addOnLayoutChangeListenerToDropDownAnchorBase() {
        mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener(this::adjustDropDownSizeAndPosition);
    }

    int getSuggestionRowLayout() {
        return mSuggestionRowLayout;
    }

    int getSuggestionCommitIconResId() {
        return mSuggestionCommitIconResId;
    }

    public void setSearchableInfo(SearchableInfo searchable) {
        mSearchable = searchable;
        if (mSearchable != null) {
            updateSearchAutoComplete();
            updateQueryHint();
        }
        mVoiceButtonEnabled = hasVoiceSearch();

        if (mVoiceButtonEnabled) {
            // TODO: use imeOptions to disable voice input when the new API will be available
            mSearchSrcTextView.setPrivateImeOptions(IME_OPTION_NO_MICROPHONE);
        }
        updateViewsVisibility(isIconified());
    }

    public void setAppSearchData(Bundle appSearchData) {
        mAppSearchData = appSearchData;
    }

    public void setImeOptions(int imeOptions) {
        mSearchSrcTextView.setImeOptions(imeOptions);
    }

    public int getImeOptions() {
        return mSearchSrcTextView.getImeOptions();
    }

    public void setInputType(int inputType) {
        mSearchSrcTextView.setInputType(inputType);
    }

    public int getInputType() {
        return mSearchSrcTextView.getInputType();
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnCloseListener(OnCloseListener listener) {
        mOnCloseListener = listener;
    }

    public void setOnQueryTextFocusChangeListener(OnFocusChangeListener listener) {
        mOnQueryTextFocusChangeListener = listener;
    }

    public void setOnSuggestionListener(OnSuggestionListener listener) {
        mOnSuggestionListener = listener;
    }

    public void setOnSearchClickListener(OnClickListener listener) {
        mOnSearchClickListener = listener;
    }

    public CharSequence getQuery() {
        return mSearchSrcTextView.getText();
    }


    public void setQuery(CharSequence query, boolean submit) {
        mSearchSrcTextView.setText(query);
        if (query != null) {
            mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
            mUserQuery = query;
        }

        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setQueryHint(@Nullable CharSequence hint) {
        mQueryHint = hint;
        updateQueryHint();
    }

    @Nullable
    public CharSequence getQueryHint() {
        final CharSequence hint;
        if (mQueryHint != null) {
            hint = mQueryHint;
        } else if (mSearchable != null && mSearchable.getHintId() != 0) {
            hint = getContext().getText(mSearchable.getHintId());
        } else {
            hint = mDefaultQueryHint;
        }
        return hint;
    }

    public void setIconifiedByDefault(boolean iconified) {
        if (mIconifiedByDefault == iconified) return;
        mIconifiedByDefault = iconified;

        updateQueryHint();
    }

    public boolean isIconfiedByDefault() {
        return mIconifiedByDefault;
    }

    public void setIconified(boolean iconify) {
        if (iconify) {
            onCloseClicked();
        } else {
            onSearchClicked();
        }
    }


    public void setSubmitButtonEnabled(boolean enabled) {
        mSubmitButtonEnabled = enabled;
    }

    public boolean isSubmitButtonEnabled() {
        return mSubmitButtonEnabled;
    }

    public boolean isQueryRefinementEnabled() {
        return mQueryRefinement;
    }

    public void setSuggestionsAdapter(CursorAdapter adapter) {
        mSuggestionsAdapter = adapter;

        mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
    }

    public CursorAdapter getSuggestionsAdapter() {
        return mSuggestionsAdapter;
    }

    public void setMaxWidth(int maxpixels) {
        mMaxWidth = maxpixels;
        requestLayout();
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }


    protected void oAnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //MeasureSpec.AT_MOST:
    }

    private void getChildBoundsWithinSearchView(View view, Rect rect) {
        view.getLocationInWindow(mTemp);
        getLocationInWindow(mTemp2);
        final int top = mTemp[1] - mTemp2[1];
        final int left = mTemp[0] - mTemp2[0];
        rect.set(left, top, left + view.getWidth(), top + view.getHeight());
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
    }

    private void updateViewsVisibility(final boolean collapsed) {
        final int visCollapsed = collapsed ? VISIBLE : GONE;
        final boolean hasText = !TextUtils.isEmpty(mSearchSrcTextView.getText());

        mSearchButton.setVisibility(visCollapsed);
        updateSubmitButton(hasText);
        mSearchEditFrame.setVisibility(collapsed ? GONE : VISIBLE);

        final int iconVisibility;
        if (mCollapsedIcon.getDrawable() == null || mIconifiedByDefault) {
            iconVisibility = GONE;
        } else {
            iconVisibility = VISIBLE;
        }
        mCollapsedIcon.setVisibility(iconVisibility);

        updateCloseButton();
        updateVoiceButton(!hasText);
        updateSubmitArea();
    }

    private boolean hasVoiceSearch() {
        if (mSearchable != null && mSearchable.getVoiceSearchEnabled()) {
            Intent testIntent = null;
            if (mSearchable.getVoiceSearchLaunchWebSearch()) {
                testIntent = mVoiceWebSearchIntent;
            } else if (mSearchable.getVoiceSearchLaunchRecognizer()) {
                testIntent = mVoiceAppSearchIntent;
            }
            if (testIntent != null) {
                ResolveInfo ri = getContext().getPackageManager().resolveActivity(testIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                return ri != null;
            }
        }
        return false;
    }







    private void updateCloseButton() {
        final boolean hasText = !TextUtils.isEmpty(mSearchSrcTextView.getText());
        final boolean showClose = hasText || (mIconifiedByDefault && !mExpandedInActionView);
        mCloseButton.setVisibility(showClose ? VISIBLE : GONE);
        final Drawable closeButtonImg = mCloseButton.getDrawable();
        if (closeButtonImg != null) {
            closeButtonImg.setState(hasText ? ENABLED_STATE_SET : EMPTY_STATE_SET);
        }
    }

    private void postUpdateFocusedState() {
        post(mUpdateDrawableStateRunnable);
    }

    void updateFocusedState() {
        final boolean focused = mSearchSrcTextView.hasFocus();
        final int[] stateSet = focused ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        final Drawable searchPlateBg = mSearchPlate.getBackground();
        if (searchPlateBg != null) {
            searchPlateBg.setState(stateSet);
        }
        final Drawable submitAreaBg = mSubmitArea.getBackground();
        if (submitAreaBg != null) {
            submitAreaBg.setState(stateSet);
        }
        invalidate();
    }



    void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    void onQueryRefine(CharSequence queryText) {
        setQuery(queryText);
    }

    final OnKeyListener mTextKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (mSearchable == null) {
                return false;
            }

            if (DBG) {
                Log.d(LOG_TAG, "mTextListener.onKey(" + keyCode + "," + event + "), selection: "
                        + mSearchSrcTextView.getListSelection());
            }

            if (mSearchSrcTextView.isPopupShowing()
                    && mSearchSrcTextView.getListSelection() != ListView.INVALID_POSITION) {
                return onSuggestionsKey(v, keyCode, event);
            }

            if (mSearchSrcTextView.isEmpty() && KeyEventCompat.hasNoModifiers(event)) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();
                        launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null, mSearchSrcTextView.getText().toString());
                        return true;
                    }
                }
            }
            return false;
        }
    };

    boolean onSuggestionsKey(View v, int keyCode, KeyEvent event) {
        if (mSearchable == null) {
            return false;
        }
        if (mSuggestionsAdapter == null) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEventCompat.hasNoModifiers(event)) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH
                    || keyCode == KeyEvent.KEYCODE_TAB) {
                int position = mSearchSrcTextView.getListSelection();
                return onItemClicked(position, KeyEvent.KEYCODE_UNKNOWN, null);
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                // TODO: Reverse left/right for right-to-left languages, e.g.
                int selPoint = (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) ? 0 : mSearchSrcTextView.length();
                mSearchSrcTextView.setSelection(selPoint);
                mSearchSrcTextView.setListSelection(0);
                mSearchSrcTextView.clearListSelection();
                HIDDEN_METHOD_INVOKER.ensureImeVisible(mSearchSrcTextView, true);

                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_UP && 0 == mSearchSrcTextView.getListSelection()) {
                // TODO: restoreUserQuery();
                return false;
            }
        }
        return false;
    }

    private CharSequence getDecoratedHint(CharSequence hintText) {
        if (!mIconifiedByDefault || mSearchHintIcon == null) {
            return hintText;
        }

        final int textSize = (int) (mSearchSrcTextView.getTextSize() * 1.25);
        mSearchHintIcon.setBounds(0, 0, textSize, textSize);

        final SpannableStringBuilder ssb = new SpannableStringBuilder("   ");
        ssb.setSpan(new ImageSpan(mSearchHintIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(hintText);
        return ssb;
    }

    private void updateQueryHint() {
        final CharSequence hint = getQueryHint();
        mSearchSrcTextView.setHint(getDecoratedHint(hint == null ? "" : hint));
    }

    private void updateSearchAutoComplete() {
        mSearchSrcTextView.setThreshold(mSearchable.getSuggestThreshold());
        mSearchSrcTextView.setImeOptions(mSearchable.getImeOptions());
        int inputType = mSearchable.getInputType();

        if ((inputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT) {
            inputType &= ~InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
            if (mSearchable.getSuggestAuthority() != null) {
                inputType |= InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
                inputType |= InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
            }
        }
        mSearchSrcTextView.setInputType(inputType);
        if (mSuggestionsAdapter != null) {
            mSuggestionsAdapter.changeCursor(null);
        }

        if (mSearchable.getSuggestAuthority() != null) {
            mSuggestionsAdapter = new SuggestionsAdapter(getContext(), this, mSearchable, mOutsideDrawablesCache);
            mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
        }
    }

    private void updateVoiceButton(boolean empty) {
        int visibility = GONE;
        if (mVoiceButtonEnabled && empty) {
            visibility = VISIBLE;
            mGoButton.setVisibility(GONE);
        }
        mVoiceButton.setVisibility(visibility);
    }

    void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        updateSubmitButton(hasText);
        updateVoiceButton(!hasText);
        updateCloseButton();
        updateSubmitArea();
        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    void onSubmitQuery() {
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

    private void dismissSuggestions() {
        mSearchSrcTextView.dismissDropDown();
    }

    void onCloseClicked() {
        CharSequence text = mSearchSrcTextView.getText();
        if (TextUtils.isEmpty(text)) {
            if (mIconifiedByDefault) {
                if (mOnCloseListener == null || !mOnCloseListener.onClose()) {
                    clearFocus();
                    updateViewsVisibility(true);
                }
            }
        } else {
            mSearchSrcTextView.setText("");
            mSearchSrcTextView.requestFocus();
            setImeVisibility(true);
        }

    }

    void onSearchClicked() {
        updateViewsVisibility(false);
        mSearchSrcTextView.requestFocus();
        setImeVisibility(true);
        if (mOnSearchClickListener != null) {
            mOnSearchClickListener.onClick(this);
        }
    }

    void onVoiceClicked() {
        if (mSearchable == null) {
            return;
        }
        SearchableInfo searchable = mSearchable;
        try {
            if (searchable.getVoiceSearchLaunchWebSearch()) {
                Intent webSearchIntent = createVoiceWebSearchIntent(mVoiceWebSearchIntent,
                        searchable);
                getContext().startActivity(webSearchIntent);
            } else if (searchable.getVoiceSearchLaunchRecognizer()) {
                Intent appSearchIntent = createVoiceAppSearchIntent(mVoiceAppSearchIntent,
                        searchable);
                getContext().startActivity(appSearchIntent);
            }
        } catch (ActivityNotFoundException e) {
            Log.w(LOG_TAG, "Could not find voice search activity");
        }
    }

    void onTextFocusChanged() {
        postUpdateFocusedState();
        if (mSearchSrcTextView.hasFocus()) {
            forceSuggestionQuery();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        postUpdateFocusedState();
    }

    @Override
    public void onActionViewCollapsed() {
        setQuery("", false);
        clearFocus();
        updateViewsVisibility(true);
        mSearchSrcTextView.setImeOptions(mCollapsedImeOptions);
        mExpandedInActionView = false;
    }

    @Override
    public void onActionViewExpanded() {
        if (mExpandedInActionView) return;

        mExpandedInActionView = true;
        mCollapsedImeOptions = mSearchSrcTextView.getImeOptions();
        mSearchSrcTextView.setImeOptions(mCollapsedImeOptions | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        mSearchSrcTextView.setText("");
        setIconified(false);
    }

    private static class SavedState extends AbsSavedState {
        boolean isIconified;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            isIconified = (Boolean) source.readValue(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeValue(isIconified);
        }

        @Override
        public String toString() {
            return "SearchView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " isIconified=" + isIconified + "}";
        }

        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(
                new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                        return new SavedState(in, loader);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isIconified = isIconified();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        updateViewsVisibility(ss.isIconified);
        requestLayout();
    }

    void adjustDropDownSizeAndPosition() {
        if (mDropDownAnchor.getWidth() > 1) {
            Resources res = getContext().getResources();
            int anchorPadding = mSearchPlate.getPaddingLeft();
            Rect dropDownPadding = new Rect();
            final boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
            int iconOffset = mIconifiedByDefault
                    ? res.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width)
                    + res.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left)
                    : 0;
            mSearchSrcTextView.getDropDownBackground().getPadding(dropDownPadding);
            int offset;
            if (isLayoutRtl) {
                offset = -dropDownPadding.left;
            } else {
                offset = anchorPadding - (dropDownPadding.left + iconOffset);
            }
            mSearchSrcTextView.setDropDownHorizontalOffset(offset);
            final int width = mDropDownAnchor.getWidth() + dropDownPadding.left
                    + dropDownPadding.right + iconOffset - anchorPadding;
            mSearchSrcTextView.setDropDownWidth(width);
        }
    }

    boolean onItemClicked(int position, int actionKey, String actionMsg) {
        if (mOnSuggestionListener == null
                || !mOnSuggestionListener.onSuggestionClick(position)) {
            launchSuggestion(position, KeyEvent.KEYCODE_UNKNOWN, null);
            setImeVisibility(false);
            dismissSuggestions();
            return true;
        }
        return false;
    }

    boolean onItemSelected(int position) {
        if (mOnSuggestionListener == null
                || !mOnSuggestionListener.onSuggestionSelect(position)) {
            rewriteQueryFromSuggestion(position);
            return true;
        }
        return false;
    }

    private void rewriteQueryFromSuggestion(int position) {
        CharSequence oldQuery = mSearchSrcTextView.getText();
        Cursor c = mSuggestionsAdapter.getCursor();
        if (c == null) {
            return;
        }
        if (c.moveToPosition(position)) {
            CharSequence newQuery = mSuggestionsAdapter.convertToString(c);
            if (newQuery != null) {
                setQuery(newQuery);
            } else {
                setQuery(oldQuery);
            }
        } else {
            setQuery(oldQuery);
        }
    }

    private boolean launchSuggestion(int position, int actionKey, String actionMsg) {
        Cursor c = mSuggestionsAdapter.getCursor();
        if ((c != null) && c.moveToPosition(position)) {

            Intent intent = createIntentFromSuggestion(c, actionKey, actionMsg);
            launchIntent(intent);

            return true;
        }
        return false;
    }

    private void launchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            getContext().startActivity(intent);
        } catch (RuntimeException ex) {
            Log.e(LOG_TAG, "Failed launch activity: " + intent, ex);
        }
    }

    private void setQuery(CharSequence query) {
        mSearchSrcTextView.setText(query);
        mSearchSrcTextView.setSelection(TextUtils.isEmpty(query) ? 0 : query.length());
    }

    void launchQuerySearch(int actionKey, String actionMsg, String query) {
        String action = Intent.ACTION_SEARCH;
        Intent intent = createIntent(action, null, null, query, actionKey, actionMsg);
        getContext().startActivity(intent);
    }

    private Intent createIntent(String action, Uri data, String extraData, String query, int actionKey, String actionMsg) {
        Intent intent = new Intent(action);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra(SearchManager.USER_QUERY, mUserQuery);
        if (query != null) {
            intent.putExtra(SearchManager.QUERY, query);
        }
        if (extraData != null) {
            intent.putExtra(SearchManager.EXTRA_DATA_KEY, extraData);
        }
        if (mAppSearchData != null) {
            intent.putExtra(SearchManager.APP_DATA, mAppSearchData);
        }
        if (actionKey != KeyEvent.KEYCODE_UNKNOWN) {
            intent.putExtra(SearchManager.ACTION_KEY, actionKey);
            intent.putExtra(SearchManager.ACTION_MSG, actionMsg);
        }
        intent.setComponent(mSearchable.getSearchActivity());
        return intent;
    }

    private Intent createVoiceWebSearchIntent(Intent baseIntent, SearchableInfo searchable) {
        Intent voiceIntent = new Intent(baseIntent);
        ComponentName searchActivity = searchable.getSearchActivity();
        voiceIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, searchActivity == null ? null : searchActivity.flattenToShortString());
        return voiceIntent;
    }

    private Intent createVoiceAppSearchIntent(Intent baseIntent, SearchableInfo searchable) {
        ComponentName searchActivity = searchable.getSearchActivity();

        Intent queryIntent = new Intent(Intent.ACTION_SEARCH);
        queryIntent.setComponent(searchActivity);
        PendingIntent pending = PendingIntent.getActivity(getContext(), 0, queryIntent, PendingIntent.FLAG_ONE_SHOT);

        Bundle queryExtras = new Bundle();
        if (mAppSearchData != null) {
            queryExtras.putParcelable(SearchManager.APP_DATA, mAppSearchData);
        }

        Intent voiceIntent = new Intent(baseIntent);
        String languageModel = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
        String prompt = null;
        String language = null;
        int maxResults = 1;

        Resources resources = getResources();
        if (searchable.getVoiceLanguageModeId() != 0) {
            languageModel = resources.getString(searchable.getVoiceLanguageModeId());
        }
        if (searchable.getVoicePromptTextId() != 0) {
            prompt = resources.getString(searchable.getVoicePromptTextId());
        }
        if (searchable.getVoiceLanguageId() != 0) {
            language = resources.getString(searchable.getVoiceLanguageId());
        }
        if (searchable.getVoiceMaxResults() != 0) {
            maxResults = searchable.getVoiceMaxResults();
        }

        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, searchActivity == null ? null : searchActivity.flattenToShortString());
        voiceIntent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT, pending);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_RESULTS_PENDINGINTENT_BUNDLE, queryExtras);

        return voiceIntent;
    }

    private Intent createIntentFromSuggestion(Cursor c, int actionKey, String actionMsg) {
        try {
            String action = getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_ACTION);

            if (action == null) {
                action = mSearchable.getSuggestIntentAction();
            }
            if (action == null) {
                action = Intent.ACTION_SEARCH;
            }

            String data = getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_DATA);
            if (data == null) {
                data = mSearchable.getSuggestIntentData();
            }
            if (data != null) {
                String id = getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
                if (id != null) {
                    data = data + "/" + Uri.encode(id);
                }
            }
            Uri dataUri = (data == null) ? null : Uri.parse(data);

            String query = getColumnString(c, SearchManager.SUGGEST_COLUMN_QUERY);
            String extraData = getColumnString(c, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);

            return createIntent(action, dataUri, extraData, query, actionKey, actionMsg);
        } catch (RuntimeException e) {
            int rowNum;
            try {
                rowNum = c.getPosition();
            } catch (RuntimeException e2) {
                rowNum = -1;
            }
            Log.w(LOG_TAG, "Search suggestions cursor at row " + rowNum +
                    " returned exception.", e);
            return null;
        }
    }

    void forceSuggestionQuery() {
        HIDDEN_METHOD_INVOKER.doBeforeTextChanged(mSearchSrcTextView);
        HIDDEN_METHOD_INVOKER.doAfterTextChanged(mSearchSrcTextView);
    }

    static boolean isLandscapeMode(Context context) {
        // TouchDelegate
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }





    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {

        private int mThreshold;
        private SearchViewAppCompat mSearchView;

        public SearchAutoComplete(Context context) {
            this(context, null);
        }

        public SearchAutoComplete(Context context, AttributeSet attrs) {
            this(context, attrs, R.attr.autoCompleteTextViewStyle);
        }

        public SearchAutoComplete(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            mThreshold = getThreshold();
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    getSearchViewTextMinWidthDp(), metrics));
        }

        void setSearchView(SearchViewAppCompat searchView) {
            mSearchView = searchView;
        }

        @Override
        public void setThreshold(int threshold) {
            super.setThreshold(threshold);
            mThreshold = threshold;
        }

        private boolean isEmpty() {
            return TextUtils.getTrimmedLength(getText()) != 0;
        }

        @Override
        protected void replaceText(CharSequence text) {
        }

        @Override
        public void performCompletion() {
        }

        @Override
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);

            if (hasWindowFocus && mSearchView.hasFocus() && getVisibility() == VISIBLE) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(this, 0);
                if (isLandscapeMode(getContext())) {
                    HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }

        @Override
        protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            mSearchView.onTextFocusChanged();
        }

        @Override
        public boolean enoughToFilter() {
            return mThreshold <= 0 || super.enoughToFilter();
        }

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.handleUpEvent(event);
                    }
                    if (event.isTracking() && !event.isCanceled()) {
                        mSearchView.clearFocus();
                        mSearchView.setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(keyCode, event);
        }

        private int getSearchViewTextMinWidthDp() {
            final Configuration config = getResources().getConfiguration();
            final int widthDp = ConfigurationHelper.getScreenWidthDp(getResources());
            final int heightDp = ConfigurationHelper.getScreenHeightDp(getResources());

            if (widthDp >= 960 && heightDp >= 720
                    && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return 256;
            } else if (widthDp >= 600 || (widthDp >= 640 && heightDp >= 480)) {
                return 192;
            }
            return 160;
        }
    }

    private static class AutoCompleteTextViewReflector {

        private Method doBeforeTextChanged, doAfterTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                ensureImeVisible = AppCompatAutoCompleteTextView.class.getMethod("ensureImeVisible", boolean.class);
                ensureImeVisible.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                showSoftInputUnchecked = InputMethodManager.class.getMethod(
                        "showSoftInputUnchecked", int.class, ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView view) {
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        void doAfterTextChanged(AutoCompleteTextView view) {
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView view, boolean visible) {
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(view, visible);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        void showSoftInputUnchecked(InputMethodManager imm, View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            imm.showSoftInput(view, flags);
        }
    }




    public interface OnQueryTextListener {

        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface OnCloseListener {

        boolean onClose();
    }

    public interface OnSuggestionListener {

        boolean onSuggestionSelect(int position);

        boolean onSuggestionClick(int position);
    }
}*/