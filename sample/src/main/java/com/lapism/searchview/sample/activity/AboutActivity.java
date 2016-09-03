package com.lapism.searchview.sample.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;


public class AboutActivity extends BaseActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_INVALID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            //mToolbar.setNavigationOnClickListener();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}