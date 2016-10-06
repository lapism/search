package com.lapism.searchview.sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lapism.searchview.sample.R;


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
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mActionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_delete_white_24dp);
        /*mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mDrawerLayout.openDrawer(GravityCompat.START); finish
            }
        });*/

        setSearchView();
        customSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mSearchView.open(true, item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}