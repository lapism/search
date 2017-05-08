package com.lapism.searchview;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// getContext
public class SearchViewCompat extends FrameLayout implements View.OnClickListener {


    private final Runnable mShowImeRunnable = () -> {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    };

    private List<Boolean> mSearchFiltersStates = null;
    private List<SearchFilter> mSearchFilters = null;

    public SearchViewCompat(@NonNull Context context) {
        super(context);
    }

    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    private static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    // ---------------------------------------------------------------------------------------------


    // mContext.getResources().getDimension(R.dimen.search_height))
    // @Nullable
    // ---------------------------------------------------------------------------------------------





    /*public SearchView setFilters(@Nullable List<SearchFilter> filters) {
        mSearchFilters = filters;
        ///mFiltersContainer.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = 0;
            params.bottomMargin = 0;
            //mFiltersContainer.setLayoutParams(params);
        } else {
            mSearchFiltersStates = new ArrayList<>();//todo
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.filter_margin_top);
            params.bottomMargin = params.topMargin / 2;
            mFiltersContainer.setLayoutParams(params);

            for (SearchFilter filter : filters) {
                CheckBox checkBox = new CheckBox(mContext);
                checkBox.setText(filter.getTitle());
                checkBox.setTextSize(mContext.getResources().getDimension(R.dimen.search_text_medium));
                checkBox.setTextColor(mTextColor);
                checkBox.setChecked(filter.isChecked());

                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.search_filter_margin_start), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top), getResources().getDimensionPixelSize(R.dimen.search_filter_margin_top));

                checkBox.setLayoutParams(lp);
                checkBox.setTag(filter.getTagId());
                mFiltersContainer.addView(checkBox);
                mSearchFiltersStates.add(filter.isChecked());
            }
        }
        return this;
    }*/

    public List<SearchFilter> getSearchFilters() {
        if (mSearchFilters == null) {
            return new ArrayList<>();
        }

        //dispatchFilters();

        List<SearchFilter> searchFilters = new ArrayList<>();
        for (SearchFilter filter : mSearchFilters) {
            searchFilters.add(new SearchFilter(filter.getTitle(), filter.isChecked(), filter.getTagId()));
        }
        return searchFilters;
    }

    public List<Boolean> getFiltersStates() {
        return mSearchFiltersStates;
    }

    /*public int getHeightInDP() {
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        return pxToDp(params.height);
    }*/

    // ---------------------------------------------------------------------------------------------
   /* public SearchView setHeightInDP(float dp) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
        params.height = height;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mLinearLayout.setLayoutParams(params);
        return this;
    }*/









   /* private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
            View view = mFiltersContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }

    private void dispatchFilters() {
        if (mSearchFiltersStates != null) {
            for (int i = 0, j = 0, n = mFiltersContainer.getChildCount(); i < n; i++) {
                View view = mFiltersContainer.getChildAt(i);
                if (view instanceof CheckBox) {
                    boolean isChecked = ((CheckBox) view).isChecked();
                    mSearchFiltersStates.set(j, isChecked);
                    mSearchFilters.get(j).setChecked(isChecked);
                    j++;
                }
            }
        }
    }*/







    @Override
    public void onClick(View v) {

    }




}
