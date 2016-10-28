package com.lapism.searchview.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;

import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;


public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setSearchView();
        mSearchView.setNavigationIconArrowHamburger();
        mSearchView.setTextInput(R.string.search);
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                finish();
            }
        });
        customSearchView();
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