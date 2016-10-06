package com.lapism.searchview.sample.base;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;

import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;


public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        setSearchView();
        mSearchView.setTextInput(R.string.search);
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                finish();
            }
        });
        customSearchView();
        mSearchView.open(false);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();// NAV UTILS
        }
    }

}