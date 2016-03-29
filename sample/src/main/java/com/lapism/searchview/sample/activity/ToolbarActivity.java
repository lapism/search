package com.lapism.searchview.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;

import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;
import com.lapism.searchview.view.SearchView;

import java.util.List;


public class ToolbarActivity extends BaseActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_TOOLBAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        setTitle(null);

        setViewPager();

        // -----------------------------------------------------------------------------------------
        setSearchView();
        mSearchView.setOnSearchMenuListener(new SearchView.SearchMenuListener() {
            @Override
            public void onMenuClick() {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        // -----------------------------------------------------------------------------------------

        customSearchView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}