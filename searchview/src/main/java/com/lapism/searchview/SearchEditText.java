package com.lapism.searchview;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

// @RestrictTo(LIBRARY_GROUP)
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

    public void setSearchView(SearchView searchView) {
        mSearchView = searchView;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (mSearchView.getShouldHideOnKeyboardClose()) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (mSearchView != null && mSearchView.isSearchOpen()) {
                    mSearchView.close(true);
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

}