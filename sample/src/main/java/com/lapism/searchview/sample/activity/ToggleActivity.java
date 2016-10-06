package com.lapism.searchview.sample.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;


public class ToggleActivity extends BaseActivity {

    private int mCurrentVersion;

    @Override
    protected int getNavItem() {
        return NAV_ITEM_TOGGLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);


        setViewPager();

        mCurrentVersion = getIntent().getIntExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);

        // -----------------------------------------------------------------------------------------
        setSearchView();
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                mDrawerLayout.openDrawer(GravityCompat.START); // finish();
            }
        });
        // -----------------------------------------------------------------------------------------

        customSearchView();
    }

    @Override
    protected void setFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_delete);
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mCurrentVersion == SearchView.VERSION_TOOLBAR) {
                        mCurrentVersion = SearchView.VERSION_MENU_ITEM;
                        mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
                    } else {
                        mCurrentVersion = SearchView.VERSION_TOOLBAR;
                        mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
                    }
                    invalidateOptionsMenu();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCurrentVersion != SearchView.VERSION_MENU_ITEM) {
            return false;
        }

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mSearchView.open(true);
                return true;
            case android.R.id.home:
                //mDrawerLayout.openDrawer(GravityCompat.START);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}