package com.lapism.searchview.sample.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;

import java.util.List;


public class MenuItemActivity extends BaseActivity {

    @Override
    protected int getNavItem() {
        return NAV_ITEM_MENU_ITEM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        setViewPager();

        // -----------------------------------------------------------------------------------------
        setSearchView();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mSearchView.open(true);
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); // finish()
                return true;
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                return true;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return true;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }

    /*
       @Override
115     public boolean onOptionsItemSelected(MenuItem item) {
116         switch (item.getItemId()) {
117             case android.R.id.home:
118                 mDrawerLayout.openDrawer(GravityCompat.START);
119                 return true;
120             case R.id.menu_night_mode_system:
121                 setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
122                 break;
123             case R.id.menu_night_mode_day:
124                 setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
125                 break;
126             case R.id.menu_night_mode_night:
127                 setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
128                 break;
129             case R.id.menu_night_mode_auto:
130                 setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
131                 break;
132         }
133         return super.onOptionsItemSelected(item);
134     }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

}