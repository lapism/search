package com.lapism.searchview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;


public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    int checkedMenuItem = 0;
    DrawerLayout mDrawer = null;
    SearchView mSearchView = null;

    Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setTitle(null);
                setSupportActionBar(mToolbar);
                mToolbar.setTitle(null);
                mToolbar.setSubtitle(null);
            }
        }
        return mToolbar;
    }

    private void setup() {
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_git_hub_source);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });*/

        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() { // mDrawer.setDrawerListener(new DrawerLayout.DrawerListener()
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                if (mSearchView != null && mSearchView.isSearchOpen()) {
                    mSearchView.hide(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (checkedMenuItem > -1) {
            navigationView.getMenu().getItem(checkedMenuItem).setChecked(true);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setup();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_light) {
            Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra("version", SearchCodes.VERSION_TOOLBAR);
            intent.putExtra("style", SearchCodes.STYLE_TOOLBAR_CLASSIC);
            intent.putExtra("theme", SearchCodes.THEME_LIGHT);
            startActivity(intent);
        }

        if (id == R.id.nav_dark) {
            Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra("version", SearchCodes.VERSION_TOOLBAR);
            intent.putExtra("style", SearchCodes.STYLE_TOOLBAR_CLASSIC);
            intent.putExtra("theme", SearchCodes.THEME_DARK);
            startActivity(intent);
        }

        mDrawer.closeDrawer(GravityCompat.START); // mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView != null && mSearchView.isSearchOpen()) {
            mSearchView.hide(true);
        } else if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}