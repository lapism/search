package com.lapism.searchview.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.adapter.SearchAdapter;
import com.lapism.searchview.sample.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();
        String string = "Search";

        if (extras != null && mSearchView != null) {
            string = extras.getString(EXTRA_KEY_TEXT);
        }

        List<String> list = new ArrayList<>(30);
        while (list.size() < 30) {
            list.add(string);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchAdapter(list));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setSearchView();
        mSearchView.setNavigationIconAnimation(true);
        // mSearchView.setTextOnly(R.string.search);
        mSearchView.setOnNavigationIconClickListener(state -> finish());
        customSearchView();
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        // mSearchView.setShouldClearOnOpen(true);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

}