package com.lapism.searchview.sample.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.base.BaseActivity;

import java.util.List;


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
        setContentView(R.layout.activity_toggle);
        setTitle(null);

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
        mSearchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
            @Override
            public void onVoiceClick() {
                perm(Manifest.permission.RECORD_AUDIO, 0);
            }
        });
        // -----------------------------------------------------------------------------------------

        customSearchView();
    }

    @SuppressWarnings("SameParameterValue")
    private void perm(String permission, int permission_request) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, permission_request);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setFab();
    }

    @Override
    protected void setFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_toggle);
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

    // implements ActivityCompat.OnRequestPermissionsResultCallback
    // http://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                //noinspection StatementWithEmptyBody
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
            default:
                break;
        }
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

}