// appcompat searchgvei a snioppet

    /*
    public void setProgress(@FloatRange(from = 0.0, to = 1.0) f
    loat progress) {
    @FloatRange(from = 0.0, to = 1.0)
    public float getProgress() {
        return mProgress;
    }
    */

    /*private void setImeVisibility(boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }*/

            /*        if (mQuery.equals(newText)) {
            return;
        }
                if (mQuery == newText)) {
            return;
        }//TextUtil.equals
        isEmptz
                 if (!mQuery.equals(newText)) {
            return;
        }
        */

    /*private LayoutTransition getRecyclerViewLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(200);
        return layoutTransition;
    }

    public List<Boolean> getFiltersStates() {
        return mSearchFiltersStates;
    }

    private void restoreFiltersState(List<Boolean> states) {
        mSearchFiltersStates = states;
        for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
            View view = mFlexboxLayout.getChildAt(i);
            if (view instanceof CheckBox) {
                ((CheckBox) view).setChecked(mSearchFiltersStates.get(j++));
            }
        }
    }*/

    /*public void setFilters(@Nullable List<SearchFilter> filters) {
        mSearchFilters = filters;
        mFlexboxLayout.removeAllViews();
        if (filters == null) {
            mSearchFiltersStates = null;
            mFlexboxLayout.setVisibility(View.GONE);
        } else {
            mSearchFiltersStates = new ArrayList<>();
            for (SearchFilter filter : filters) {
                CheckBox checkBox = new CheckBox(mContext);
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
    }*/
                /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFiltersContainer.getLayoutParams();
            params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.filter_margin_top);
            params.bottomMargin = params.topMargin / 2;
            mFlexboxLayout.setLayoutParams(params);*/
                /*FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) mFlexboxLayout.getLayoutParams();
            params.topMargin = 0;
            params.bottomMargin = 0;
            mFlexboxLayout.setLayoutParams(params);*/


   /* public List<SearchFilter> getSearchFilters() {
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

    private void dispatchFilters() {
        if (mSearchFiltersStates != null) {
            for (int i = 0, j = 0, n = mFlexboxLayout.getChildCount(); i < n; i++) {
                View view = mFlexboxLayout.getChildAt(i);
                if (view instanceof CheckBox) {
                    boolean isChecked = ((CheckBox) view).isChecked();
                    mSearchFiltersStates.set(j, isChecked);
                    mSearchFilters.get(j).setChecked(isChecked);
                    j++;
                }
            }
        }
    }

    private void setInfo() {
        mVoice = isVoiceAvailable();
        if (mVoice) {
            mSearchEditText.setPrivateImeOptions("nm");
        }
    }

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        if (mAdapter instanceof SearchAdapter) {
            ((SearchAdapter) mAdapter).setSuggestionsList(suggestionsList);
        }
    }*/
