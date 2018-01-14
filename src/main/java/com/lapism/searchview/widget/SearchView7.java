

    /*private static final String IME_OPTION_NO_MICROPHONE = "nm";

    private final View mSearchEditFrame;
    private final View mSearchPlate;
    private final View mSubmitArea;
    final ImageView mSearchButton;
    final ImageView mGoButton;
    final ImageView mCloseButton;
    final ImageView mVoiceButton;
    private final View mDropDownAnchor;

    private final ImageView mCollapsedIcon;


    private final Intent mVoiceWebSearchIntent;
    private final Intent mVoiceAppSearchIntent;

    OnFocusChangeListener mOnQueryTextFocusChangeListener;*/



   /* private final Runnable mUpdateDrawableStateRunnable = new Runnable() {
        @Override
        public void run() {
            updateFocusedState();
        }
    };

    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache =
            new WeakHashMap<String, Drawable.ConstantState>();*/





/*
    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.searchViewStyle);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context,
                attrs, R.styleable.SearchView, defStyleAttr, 0);

        final LayoutInflater inflater = LayoutInflater.from(context);
        final int layoutResId = a.getResourceId(
                R.styleable.SearchView_layout, R.layout.abc_search_view);
        inflater.inflate(layoutResId, this, true);

        mSearchSrcTextView = findViewById(R.id.search_src_text);
        mSearchSrcTextView.setSearchView(this);

        mSearchEditFrame = findViewById(R.id.search_edit_frame);
        mSearchPlate = findViewById(R.id.search_plate);
        mSubmitArea = findViewById(R.id.submit_area);
        mSearchButton = findViewById(R.id.search_button);
        mGoButton = findViewById(R.id.search_go_btn);
        mCloseButton = findViewById(R.id.search_close_btn);
        mVoiceButton = findViewById(R.id.search_voice_btn);
        mCollapsedIcon = findViewById(R.id.search_mag_icon);

        // Set up icons and backgrounds.
        ViewCompat.setBackground(mSearchPlate,
                a.getDrawable(R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(mSubmitArea,
                a.getDrawable(R.styleable.SearchView_submitBackground));
        mSearchButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));
        mGoButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_goIcon));
        mCloseButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_closeIcon));
        mVoiceButton.setImageDrawable(a.getDrawable(R.styleable.SearchView_voiceIcon));
        mCollapsedIcon.setImageDrawable(a.getDrawable(R.styleable.SearchView_searchIcon));

        mSearchHintIcon = a.getDrawable(R.styleable.SearchView_searchHintIcon);

        TooltipCompat.setTooltipText(mSearchButton,
                getResources().getString(R.string.abc_searchview_description_search));

        // Extract dropdown layout resource IDs for later use.
        mSuggestionRowLayout = a.getResourceId(R.styleable.SearchView_suggestionRowLayout,
                R.layout.abc_search_dropdown_item_icons_2line);
        mSuggestionCommitIconResId = a.getResourceId(R.styleable.SearchView_commitIcon, 0);

        mSearchButton.setOnClickListener(mOnClickListener);
        mCloseButton.setOnClickListener(mOnClickListener);
        mGoButton.setOnClickListener(mOnClickListener);
        mVoiceButton.setOnClickListener(mOnClickListener);
        mSearchSrcTextView.setOnClickListener(mOnClickListener);

        mSearchSrcTextView.addTextChangedListener(mTextWatcher);
        mSearchSrcTextView.setOnEditorActionListener(mOnEditorActionListener);
        mSearchSrcTextView.setOnKeyListener(mTextKeyListener);

        // Inform any listener of focus changes
        mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnQueryTextFocusChangeListener != null) {
                    mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, hasFocus);
                }
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

        boolean focusable = true;
        focusable = a.getBoolean(R.styleable.SearchView_android_focusable, focusable);
        setFocusable(focusable);

        a.recycle();

        // Save voice intent for later queries/launching
        mVoiceWebSearchIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        mVoiceWebSearchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mVoiceWebSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        mVoiceAppSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mVoiceAppSearchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mDropDownAnchor = findViewById(mSearchSrcTextView.getDropDownAnchor());
        if (mDropDownAnchor != null) {
            mDropDownAnchor.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    adjustDropDownSizeAndPosition();
                }
            });
        }

        updateViewsVisibility(mIconifiedByDefault);
        updateQueryHint();
    }
*/

    /*@Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mSearchSrcTextView.setImeVisibility(false);
        mClearingFocus = false;
    }*/

    /*
        static class SavedState extends AbsSavedState {
        boolean isIconified;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            isIconified = (Boolean) source.readValue(null);
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

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    */


    /*void onCloseClicked() {
        CharSequence text = mSearchSrcTextView.getText();
        if (TextUtils.isEmpty(text)) {
        } else {
            mSearchSrcTextView.setText("");
            mSearchSrcTextView.requestFocus();
            mSearchSrcTextView.setImeVisibility(true);
        }
    }*/

