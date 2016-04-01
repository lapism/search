package com.lapism.searchview.sample.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
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

        final Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        // or set Protected toolbar, jelikoz se to vola u setContentView, tak  s tim neco udelat

        // TODO DONATE
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