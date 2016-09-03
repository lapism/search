/*
When you press the search icon, the material searchview animation start from a different ActionBar icon,
it should start from the clicked icon, but it's starting from the 3dots icon (menu), same for closing animation .

    if (mVersion == VERSION_MENU_ITEM) {
        setVisibility(View.VISIBLE);
        if (animate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                reveal();
            } else {
                SearchAnimator.fadeOpen(mCardView, mAnimationDuration, mEditText, mShouldClearOnOpen, mOnOpenCloseListener);
            }
        } else {
            mCardView.setVisibility(View.VISIBLE);

            if (mShouldClearOnOpen && mEditText.length() > 0) {
                mEditText.getText().clear();
            }
            mEditText.requestFocus();
            if (mOnOpenCloseListener != null) {
                mOnOpenCloseListener.onOpen();
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getViewLayout().setVisibility(View.VISIBLE);
                getViewLayout().setBackgroundColor(getPrimaryColor());
            }
        },1000);

    }
/*
I'm using SearchView inside my Fragment.

When I open(true) my SearchView, the keyboard is showing.
When I rotate my phone, the SearchView is still shown but the keyboard has gone.
searchView.isSearchOpen() is false

VISIBLE DIVIDER BUG
onPostCreate,
fix example
TOOLBAR ICON SET DEPRECATED
*/


// --------------------------------------------------------------------------------------------------
// ANALYZE, TODO, FIXME
// file:///E:/Android/SearchView/sample/build/outputs/lint-results-debug.html
// file:///E:/Android/SearchView/searchview/build/outputs/lint-results-debug.html

/*
SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length
RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead
beginBatchEdit on inactive InputConnection
getSelectedText on inactive InputConnection
endBatchEdit on inactive InputConnection
getTextBeforeCursor on inactive InputConnection
getTextAfterCursor on inactive InputConnection
No adapter attached; skipping layout
*/

// SingleTask
// s.removeSpan(new ForegroundColorSpan(SearchView.getTextColor()));
// viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);getContext();
// @ColorRes, Filter.FilterListener
// mRecyclerView.setAlpha(0.0f);
// mRecyclerView.animate().alpha(1.0f);