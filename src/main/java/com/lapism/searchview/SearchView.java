package com.lapism.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Kotlinize + NULLABLE
/*
todo
or a onFilterClickListener method is fine
*/// int id = view.getId();
// this(context, null);
public class SearchView extends FrameLayout {

    private View mMenuItemView;
    private int mMenuItemCx = -1;
    private float mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
    private CharSequence mQuery = "";

    private Context mContext;

    // todo doplnit hodnoty
    private void initStyle(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes);
        if (a != null) {
            if (a.hasValue(R.styleable.SearchView_search_custom_height)) {
                setCustomHeight(a.getDimensionPixelSize(R.styleable.SearchView_search_custom_height, mContext.getResources().getDimensionPixelSize(R.dimen.search_height)));
            }
            if (a.hasValue(R.styleable.SearchView_search_version)) {
                setVersion(a.getInt(R.styleable.SearchView_search_version, Version.TOOLBAR));
            }
            if (a.hasValue(R.styleable.SearchView_search_version_margins)) {
                setVersionMargins(a.getInt(R.styleable.SearchView_search_version_margins, VersionMargins.TOOLBAR_SMALL));
            }
            if (a.hasValue(R.styleable.SearchView_search_theme)) {
                setTheme(a.getInt(R.styleable.SearchView_search_theme, Theme.LIGHT));
            }
            if (a.hasValue(R.styleable.SearchView_search_navigation_icon)) {
                setNavigationIcon(a.getResourceId(R.styleable.SearchView_search_navigation_icon, 0));
            }
            if (a.hasValue(R.styleable.SearchView_search_icon_color)) {
                setIconColor(a.getColor(R.styleable.SearchView_search_icon_color, Color.BLACK));
            }
            if (a.hasValue(R.styleable.SearchView_search_background_color)) {
                setBackgroundColor(a.getColor(R.styleable.SearchView_search_background_color, Color.WHITE));
            }
            if (a.hasValue(R.styleable.SearchView_search_text_color)) {
                setTextColor(a.getColor(R.styleable.SearchView_search_text_color, Color.BLACK));
            }
            if (a.hasValue(R.styleable.SearchView_search_text_highlight_color)) {
                setTextHighlightColor(a.getColor(R.styleable.SearchView_search_text_highlight_color, Color.GRAY));
            }
            if (a.hasValue(R.styleable.SearchView_search_text_size)) {
                setTextSize(a.getDimension(R.styleable.SearchView_search_text_size, mContext.getResources().getDimension(R.dimen.search_text_16)));
            }
            if (a.hasValue(R.styleable.SearchView_search_text_style)) {
                setTextStyle(agetInt(R.styleable.SearchView_search_text_style, Typeface.NORMAL));
            }
            if (a.hasValue(R.styleable.SearchView_search_hint)) {
                setHint(a.getString(R.styleable.SearchView_search_hint));
            }
            if (a.hasValue(R.styleable.SearchView_search_hint_color)) {
                setHintColor(a.getColor(R.styleable.SearchView_search_hint_color, Color.BLACK));
            }
            if (a.hasValue(R.styleable.SearchView_search_animation_duration)) {
                setAnimationDuration(a.getInteger(R.styleable.SearchView_search_animation_duration, mAnimationDuration));
            }
            if (a.hasValue(R.styleable.SearchView_search_shadow)) {
                setShadow(a.getBoolean(R.styleable.SearchView_search_shadow, true));
            }
            if (a.hasValue(R.styleable.SearchView_search_shadow_color)) {
                setShadowColor(a.getColor(R.styleable.SearchView_search_shadow_color, mContext.getResources().getColor(R.color.search_shadow)));
            }
            if (a.hasValue(R.styleable.SearchView_search_elevation)) {
                setElevation(a.getDimensionPixelSize(R.styleable.SearchView_search_elevation, mContext.getResources().getDimensionPixelSize(R.dimen.search_elevation));
            }
            a.recycle();
        }
    }


    // ---------------------------------------------------------------------------------------------


    public void setIconColor(@ColorInt int color) {
        mIconColor = color;
        ColorFilter colorFilter = new PorterDuffColorFilter(mIconColor, PorterDuff.Mode.SRC_IN);

        mImageViewNavigation.setColorFilter(colorFilter);
        mImageViewMic.setColorFilter(colorFilter);
        mImageViewMenu.setColorFilter(colorFilter);
    }

    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        mSearchEditText.setTextColor(mTextColor);
    }

            /*for (int i = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View child = mFlexboxLayout.getChildAt(i);
            if (child instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) child).setTextColor(mTextColor);
            }
        }*/

    public void setTextHighlightColor(@ColorInt int color) {
        mTextHighlightColor = color;
    }

    public void setVersion(@Version int version) {
        mVersion = version;

        if (mVersion == Version.MENU_ITEM) {
            setVisibility(View.GONE);
        }
    }

    public void setTheme(@Theme int theme, boolean tint) {
        mTheme = theme;

        switch (mTheme) {
            case Theme.LIGHT:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
                if (tint) {
                    setIconColor(ContextCompat.getColor(mContext, R.color.search_light_icon));
                    setHintColor(ContextCompat.getColor(mContext, R.color.search_light_hint));
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
                    setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_light_text_highlight));
                }
                break;
            case Theme.DARK:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
                if (tint) {
                    setIconColor(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                    setHintColor(ContextCompat.getColor(mContext, R.color.search_dark_hint));
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
                    setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_dark_text_highlight));
                }
                break;
            case Theme.PLAY_STORE:
                setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_play_store_background));
                if (tint) {
                    setIconColor(ContextCompat.getColor(mContext, R.color.search_play_store_icon));
                    setHintColor(ContextCompat.getColor(mContext, R.color.search_play_store_hint));
                    setTextColor(ContextCompat.getColor(mContext, R.color.search_play_store_text));
                    setTextHighlightColor(ContextCompat.getColor(mContext, R.color.search_play_store_text_highlight));
                }
                break;
        }
    }

    public void setTheme(@Theme int theme) {
        setTheme(theme, true);
    }


    public void setQuery(CharSequence query, boolean submit) {
        mQuery = query;
        mSearchEditText.setText(query);
        mSearchEditText.setSelection(mSearchEditText.length());

        if (!TextUtils.isEmpty(mQuery)) {
            mImageViewMic.setVisibility(View.VISIBLE);
        } else {
            mImageViewMic.setVisibility(View.GONE);
        }

        if (submit) {
            onSubmitQuery();
        }
    }


    public void open(boolean animate) {
        open(animate, null);
    }

    public void open(boolean animate, MenuItem menuItem) {
        if (mVersion == Version.MENU_ITEM) {
            setVisibility(View.VISIBLE);

            if (animate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (menuItem != null) {
                        getMenuItemPosition(menuItem.getItemId());
                    }
                    mCardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mCardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                SearchAnimator.revealOpen(
                                        mCardView,
                                        mMenuItemCx,
                                        mAnimationDuration,
                                        mContext,
                                        mSearchEditText,
                                        mOnOpenCloseListener);
                            }
                        }
                    });
                } else {
                    SearchAnimator.fadeOpen(
                            mCardView,
                            mAnimationDuration,
                            mSearchEditText,
                            mOnOpenCloseListener);
                }
            } else {
                mCardView.setVisibility(View.VISIBLE);
                if (mOnOpenCloseListener != null) {
                    mOnOpenCloseListener.onOpen();
                }
                if (mSearchEditText.length() > 0) {
                    mSearchEditText.getText().clear();
                }
                mSearchEditText.requestFocus();
            }
        }

        if (mVersion == Version.TOOLBAR) {
            if (mSearchEditText.length() > 0) {
                mSearchEditText.getText().clear();
            }
            mSearchEditText.requestFocus();
        }
    }

    public void close(boolean animate) {
        close(animate, null);
    }

    public void close(boolean animate, MenuItem menuItem) {
        switch (mVersion) {
            case Version.MENU_ITEM:
                if (animate) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (menuItem != null) {
                            getMenuItemPosition(menuItem.getItemId());
                        }
                        SearchAnimator.revealClose(
                                mCardView,
                                mMenuItemCx,
                                mAnimationDuration,
                                mContext,
                                mSearchEditText,
                                this,
                                mOnOpenCloseListener);
                    } else {
                        SearchAnimator.fadeClose(
                                mCardView,
                                mAnimationDuration,
                                mSearchEditText,
                                this,
                                mOnOpenCloseListener);
                    }
                } else {
                    if (mSearchEditText.length() > 0) {
                        mSearchEditText.getText().clear();
                    }
                    mSearchEditText.clearFocus();
                    mCardView.setVisibility(View.GONE);
                    setVisibility(View.GONE);
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onClose();
                    }
                }
                break;
            case Version.TOOLBAR:
                if (mSearchEditText.length() > 0) {
                    mSearchEditText.getText().clear();
                }
                mSearchEditText.clearFocus();
                break;
        }
    }

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

    public void addFocus() {
        if (mArrow) {
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_ARROW;
        } else {
            if (mSearchArrowDrawable != null) {
                mSearchArrowDrawable.setVerticalMirror(false);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_ARROW, mAnimationDuration);
                mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_ARROW;
            }
        }

        if (mShadow) {
            SearchAnimator.fadeIn(mViewShadow, mAnimationDuration);
        }

        if (!TextUtils.isEmpty(mQuery)) {
            mImageViewMic.setVisibility(View.VISIBLE);
        }

        showKeyboard();
        showSuggestions();

        if (mVersion == Version.TOOLBAR) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onOpen();
                    }
                }
            }, mAnimationDuration);
        }
    }

    public void removeFocus() {
        if (mArrow) {
            mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
        } else {
            if (mSearchArrowDrawable != null) {
                mSearchArrowDrawable.setVerticalMirror(true);
                mSearchArrowDrawable.animate(SearchArrowDrawable.STATE_HAMBURGER, mAnimationDuration);
                mIsSearchArrowHamburgerState = SearchArrowDrawable.STATE_HAMBURGER;
            }
        }

        if (mShadow) {
            SearchAnimator.fadeOut(mViewShadow, mAnimationDuration);
        }

        if (TextUtils.isEmpty(mQuery)) {
            mImageViewMic.setVisibility(View.GONE);
            mImageViewMic.setVisibility(View.VISIBLE);
        }

        hideKeyboard();
        hideSuggestions();

        if (mVersion == Version.TOOLBAR) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnOpenCloseListener != null) {
                        mOnOpenCloseListener.onClose();
                    }
                }
            }, mAnimationDuration);
        }
    }

    public void showSuggestions() {
        if (mFlexboxLayout.getChildCount() > 0 && mFlexboxLayout.getVisibility() == View.GONE) {
            mViewDivider.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.VISIBLE);
        }

        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter.getItemCount() > 0) {
            mViewDivider.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
        }
    }

    public void hideSuggestions() {
        if (mFlexboxLayout.getVisibility() == View.VISIBLE) {
            mViewDivider.setVisibility(View.GONE);
            mFlexboxLayout.setVisibility(View.GONE);
        }

        if (mRecyclerViewAdapter != null) {
            mViewDivider.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
        }
    }


    public void setGoogleIcons(boolean google) {
        mGoogle = google;

        if (mGoogle) {
            mImageViewNavigation.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_google_color_24dp));
            mImageViewMic.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_mic_color_24dp));
            // mImageViewMenu.setColorFilter(null);
        } else {
            mImageViewNavigation.setImageDrawable(mSearchArrowDrawable);
            mImageViewMic.setImageResource(R.drawable.ic_mic_black_24dp);
        }
    }

    //todo
    public void setRoundCorners(boolean roundCorners) {
        if (roundCorners) {
            //  mCardView.setRadius(getResources().getDimensionPixelSize(R.dimen.search_height));
            mCardView.setPreventCornerOverlap(false);
            mCardView.setBackgroundResource(R.drawable.round_background);
        } else {

        }
    }


    public void setTextSize(float size) {
        mSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }


    public void setNavigationIconAnimation(boolean animate) {
        if (!animate) {
            mSearchArrowDrawable.setProgress(SearchArrowDrawable.STATE_ARROW);
        }
        mArrow = !animate;
    }


    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        if (mRecyclerViewAdapter instanceof SearchAdapter) {
            ((SearchAdapter) mRecyclerViewAdapter).setSuggestionsList(suggestionsList);
        }
    }


    // ---------------------------------------------------------------------------------------------
    private void initView() {
        //krishkapil filter  listener

    }

    private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View view = mFlexboxLayout.getChildAt(i);
            if (view instanceof AppCompatCheckBox) {
                ((AppCompatCheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }

    private void getMenuItemPosition(int menuItemId) {
        if (mMenuItemView != null) {
            mMenuItemCx = getCenterX(mMenuItemView);
        }
        ViewParent viewParent = getParent();
        while (viewParent != null && viewParent instanceof View) {
            View parent = (View) viewParent;
            View view = parent.findViewById(menuItemId);
            if (view != null) {
                mMenuItemView = view;
                mMenuItemCx = getCenterX(mMenuItemView);
                break;
            }
            viewParent = viewParent.getParent();
        }
    }

    private int getCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0] + view.getWidth() / 2;
    }

    private void isVoiceAvailable() {
        if (isInEditMode()) {
            return;//break continue
        }
    }

    private void onTextChanged(CharSequence newText) {
        mQuery = newText;

        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter instanceof Filterable) {
            final CharSequence mFilterKey = newText.toString().toLowerCase(Locale.getDefault());
            ((SearchAdapter) mRecyclerViewAdapter).getFilter().filter(newText, new Filter.FilterListener() {
                @Override
                public void onFilterComplete(int i) {
                    if (mFilterKey.equals(((SearchAdapter) mRecyclerViewAdapter).getKey())) {
                        if (i > 0) {
                            if (mRecyclerView.getVisibility() == View.GONE) {
                                mViewDivider.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                SearchAnimator.fadeIn(mRecyclerView, mAnimationDuration);
                            }
                        } else {
                            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                                mViewDivider.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.GONE);
                                SearchAnimator.fadeOut(mRecyclerView, mAnimationDuration);
                            }
                        }
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(newText)) {
            mImageViewMic.setImageResource(R.drawable.ic_clear_black_24dp);
        } else {
            if (mGoogle) {
                mImageViewMic.setImageResource(R.drawable.ic_mic_color_24dp);
            } else {
                mImageViewMic.setImageResource(R.drawable.ic_mic_black_24dp);
                // TODO BARVY
            }
        }

        if (mOnQueryTextListener != null) {
            dispatchFilters();
            mOnQueryTextListener.onQueryTextChange(newText.toString());
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            dispatchFilters();
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                mSearchEditText.setText(query);
            }
        }
    }

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

    // aj

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());

        bundle.putCharSequence("query", mQuery);
        bundle.putBoolean("isSearchOpen", getVisibility() == View.VISIBLE);

        dispatchFilters();
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
        bundle.putParcelableArrayList("searchFilters", searchFilters);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
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
        }
        super.onRestoreInstanceState(state);
    }

}
