package com.lapism.searchview;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;


public class SearchEditText extends AppCompatEditText {

    private SearchView mSearchView;

    public SearchEditText(Context context) {
        super(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void setSearchView(SearchView searchView) {
        mSearchView = searchView;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (mSearchView.getShouldHideOnKeyboardClose()) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (mSearchView != null && mSearchView.isSearchOpen()) {
                    mSearchView.close();
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
        // return super.dispatchKeyEvent(event);
    }

}
        /*
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.SearchView, defStyleAttr, 0);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final int layoutResId = a.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view);
        inflater.inflate(layoutResId, this, true);
        */
//  ComponentName searchActivity = searchable.getSearchActivity();
// PendingIntent pending = PendingIntent.getActivity(getContext(), 0, queryIntent, PendingIntent.FLAG_ONE_SHOT);
       /* Bundle queryExtras = new Bundle();
        if (mAppSearchData != null) {
            queryExtras.putParcelable(SearchManager.APP_DATA, mAppSearchData);
        }*/
// Resources resources = getResources();