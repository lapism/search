package com.lapism.searchview.sample.activity.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;


public class MenuItemActivity extends BaseActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_MENU_ITEM;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        setToolbar();
        setViewPager();
        // invalidateOptionsMenu();
        // int mCurrentVersion = getIntent().getIntExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mActionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START); // WITHOUT finish(); + finish();
            }
        });

        setSearchView();
        mSearchView.setArrowOnly(false);
        customSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true; // false
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                mSearchView.open(item);
                return true;
            /* case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); finish();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}