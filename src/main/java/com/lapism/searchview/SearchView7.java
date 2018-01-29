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



    /*void onTextChanged2(CharSequence newText) {
        CharSequence text = mSearchEditText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }*/

// todo test VSEHO  + README + Color.BLACK
// TODO + callsuper + anotace vseho
// todo room + check google search margin
// TODO PROMYSLET
// slideDown TODO
// TODO plus marginy dle searchview + check
// todo

    /*dispatchFilters();
    ArrayList<Integer> searchFiltersStatesInt = null;
        if (mSearchFiltersStates != null) {
        searchFiltersStatesInt = new ArrayList<>();
        for (Boolean bool : mSearchFiltersStates) {
            searchFiltersStatesInt.add((bool) ? 1 : 0);
        }
    }
        bundle.putIntegerArrayList("searchFiltersStates", searchFiltersStatesInt);

    ArrayList<SearchFilter> searchFilters = null;
        if (mSearchFilters != null) {
        searchFilters = new ArrayList<>();
        searchFilters.addAll(mSearchFilters);
    }
        bundle.putParcelableArrayList("searchFilters", searchFilters);*/




        /*if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            mQuery = bundle.getCharSequence("query");
            if (bundle.getBoolean("isSearchOpen")) {
                open(true);
                setQuery(mQuery, false);
                mSearchEditText.requestFocus();
            }

            ArrayList<Integer> searchFiltersStatesInt = bundle.getIntegerArrayList("searchFiltersStates");
            ArrayList<Boolean> searchFiltersStatesBool = null;
            if (searchFiltersStatesInt != null) {
                searchFiltersStatesBool = new ArrayList<>();
                for (Integer value : searchFiltersStatesInt) {
                    searchFiltersStatesBool.add(value == 1);
                }
            }
            restoreFiltersState(searchFiltersStatesBool);

            mSearchFilters = bundle.getParcelableArrayList("searchFilters");

            state = bundle.getParcelable("superState");
        }*/

    /*
        public void setQuery(CharSequence query, boolean submit) {
        mQuery = query;
        mSearchEditText.setText(query);
        mSearchEditText.setSelection(mSearchEditText.length());



        if (submit) {
            //onSubmitQuery();
        }
    }


//callsuper
    public void setFilters(@Nullable List<SearchFilter> filters) {
        mSearchFilters = filters;
        mFlexboxLayout.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            mFlexboxLayout.setVisibility(View.GONE);
        } else {
            mSearchFiltersStates = new ArrayList<>();
            for (SearchFilter filter : filters) {
                AppCompatCheckBox checkBox = new AppCompatCheckBox(mContext);
                checkBox.setText(filter.getTitle());
                checkBox.setTextSize(12);
                checkBox.setTextColor(mTextColor);
                checkBox.setChecked(filter.isChecked());

                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.search_filter_margin_start), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top));

                checkBox.setLayoutParams(lp);
                checkBox.setTag(filter.getTagId());
                mFlexboxLayout.addView(checkBox);
                mSearchFiltersStates.add(filter.isChecked());
            }
        }
    }

    public List<SearchFilter> getSearchFilters() {
        if (mSearchFilters == null) {
            return new ArrayList<>();
        }

        dispatchFilters();

        List<SearchFilter> searchFilters = new ArrayList<>();
        for (SearchFilter filter : mSearchFilters) {
            searchFilters.add(new SearchFilter(filter.getTitle(), filter.isChecked(), filter.getTagId()));
        }

        return searchFilters;
    }

    public List<Boolean> getFiltersStates() {
        return mSearchFiltersStates;
    }




        do {
          setShadow(false);
        } while (false);





        //break >= continue return instanceof + << atd
        // ViewCompat.setBackground(mSearchPlate,
        // AppCompatResources.getDrawable()
        // a.getType(R.styleable.SearchView_search_menu_color);
        // ColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);




    private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View view = mFlexboxLayout.getChildAt(i);
            if (view instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }





    //krishkapil filter  listener

    private void isVoiceAvailable() {
        if (isInEditMode()) {
            return;//break continue + krisnha filter listener
        }
    }



@FloatRange(from = SearchArrowDrawable.STATE_HAMBURGER, to = SearchArrowDrawable.STATE_ARROW)

    private void dispatchFilters() {
        if (mSearchFiltersStates != null) {
            for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
                View view = mFlexboxLayout.getChildAt(i);
                if (view instanceof AppCompatCheckBox) {
                    boolean isChecked = ((AppCompatCheckBox) view).isChecked();
                    mSearchFiltersStates.set(j, isChecked);
                    mSearchFilters.get(j).setChecked(isChecked);
                    j++;
                }
            }
        }
    }


*/
// ---------------------------------------------------------------------------------------------

// @FloatRange(from = SearchArrowDrawable.STATE_HAMBURGER, to = SearchArrowDrawable.STATE_ARROW)


          /*  for (int i = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View child = mFlexboxLayout.getChildAt(i);
            if (child instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) child).setTextColor(mTextColor);
            }
        }*/


// Kotlinize + NULLABLE
/*
todo
or a onFilterClickListener method is fine
*/// int id = view.getId();
// this(context, null);


// aj
    /* Future release
    // In this library >> R.drawable.search_round_background_top
    @Override
    public void setBackgroundResource(int resid) {
        mCardView.setBackgroundResource(resid);
    }

    // In this library >> R.drawable.search_round_background_top
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setBackground(Drawable background) {
        mCardView.setBackground(background);
    }

    @Override
    public void setBackgroundColor(int color) {
        mCardView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }*/
        /*if(mRecyclerViewAdapter instanceof SearchAdapter) {
                ((SearchAdapter) mRecyclerViewAdapter).setTextFont(font);
                }*/
/*<declare-styleable name="SearchView2">
<attr name="layout2" format="reference" />
<attr name="android:maxWidth" />
<attr name="queryHint" format="string" />
<attr name="defaultQueryHint" format="string" />
<attr name="android:imeOptions" />
<attr name="android:inputType" />
<attr name="closeIcon" format="reference" />
<attr name="goIcon" format="reference" />
<attr name="searchIcon" format="reference" />
<attr name="searchHintIcon" format="reference" />
<attr name="voiceIcon" format="reference" />
<attr name="commitIcon" format="reference" />
<attr name="suggestionRowLayout" format="reference" />
<attr name="queryBackground" format="reference" />
<attr name="submitBackground" format="reference" />
<attr name="android:focusable" />
</declare-styleable>*/
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


// todo https://lab.getbase.com/nested-scrolling-with-coordinatorlayout-on-android/
// ColorUtils.setAlphaComponent(SearchView.getIconColor(), 0x33)android:alpha="0.4",

/*
                /*if (mSearchEditText.length() > 0) {
            mSearchEditText.getText().clear();
        }*/
        /*if(!TextUtils.isEmpty(mQuery)) {
            mSearchEditText.setText(mQuery);
        }*/
// SearchEditText.setVisibility(View.VISIBLE); todo bug a mizi text, hidesuggestion


//private List<Boolean> mSearchFiltersStates;
//private List<SearchFilter> mSearchFilters;
// init + kotlin 1.2.21 + 4.4.1 + glide 4.5.0 mbuild tools BETA4 427.0.3 / 02



    /*
    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept focus if in the middle of clearing focus
        if (mClearingFocus) return false;
        // Check if SearchView is focusable.
        if (!isFocusable()) return false;
        return mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
    }


    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mClearingFocus = false;
    }*/

/*


    <!--     android:foreground="?attr/selectableItemBackground"
        android:drawSelectorOnTop="true" -->
                <!--
                android:background="@android:color/transparent"
                zkontorlovat vsechny attrutbt todo foreground view 23-->
*/