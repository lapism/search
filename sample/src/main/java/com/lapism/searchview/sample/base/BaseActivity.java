package com.lapism.searchview.sample.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.activity.FiltersActivity;
import com.lapism.searchview.sample.activity.HistoryToggleActivity;
import com.lapism.searchview.sample.activity.ToggleActivity;
import com.lapism.searchview.sample.view.FragmentAdapter;
import com.lapism.searchview.sample.view.SearchFragment;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {

    protected static final int NAV_ITEM_INVALID = -1;
    protected static final int NAV_ITEM_TOOLBAR = 0;
    protected static final int NAV_ITEM_TOGGLE = 2;
    protected static final int NAV_ITEM_HISTORY_TOGGLE = 3;
    protected static final int NAV_ITEM_FILTERS = 4;
    protected static final String EXTRA_KEY_VERSION = "version";
    static final int NAV_ITEM_MENU_ITEM = 1;
    private static final String EXTRA_KEY_THEME = "theme";
    private static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
    private static final String EXTRA_KEY_TEXT = "text";

    protected SearchView mSearchView = null;
    protected DrawerLayout mDrawerLayout = null;
    protected FloatingActionButton mFab = null;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar = null;
    private SearchHistoryTable mHistoryDatabase;

    // ---------------------------------------------------------------------------------------------
    protected int getNavItem() {
        return NAV_ITEM_INVALID;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setFab();
        setDrawer();
        setNavigationView();
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // menu.setVisible(!mDrawerLayout.isDrawerOpen(GravityCompat.START));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    if (mSearchView != null) {
                        mSearchView.setQuery(searchWrd);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ---------------------------------------------------------------------------------------------
    void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setNavigationContentDescription(getResources().getString(R.string.app_name));
            setSupportActionBar(mToolbar);
        }
    }

    protected void setViewPager() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment(), getString(R.string.installed));
        adapter.addFragment(new SearchFragment(), getString(R.string.all));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // ---------------------------------------------------------------------------------------------
    protected void setFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_delete);
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHistoryDatabase.clearDatabase();
                    Snackbar.make(v, "Search history deleted.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }

    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (mDrawerLayout != null && mToolbar != null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    if (mSearchView != null && mSearchView.isSearchOpen()) {
                        mSearchView.close(true);
                    }
                    if (mFab != null) {
                        mFab.hide();
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    if (mFab != null) {
                        mFab.show();
                    }
                }
            };
            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            mActionBarDrawerToggle.syncState();
        }
    }

    private void setNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_light_toolbar || id == R.id.nav_filters_version) {
                    Intent intent = new Intent(BaseActivity.this, id == R.id.nav_light_toolbar ? ToolbarActivity.class : FiltersActivity.class);
                    intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
                    intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
                    intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
                    startActivity(intent);
                    finish();
                }

                if (id == R.id.nav_light_menu_item) {
                    Intent intent = new Intent(BaseActivity.this, MenuItemActivity.class);
                    intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_MENU_ITEM);
                    intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_MENU_ITEM);
                    intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
                    startActivity(intent);
                    finish();
                }

                if (id == R.id.nav_toggle_versions || id == R.id.nav_history_toggle_versions) {
                    Intent intent = new Intent(BaseActivity.this, id == R.id.nav_toggle_versions ? ToggleActivity.class : HistoryToggleActivity.class);
                    intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
                    intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
                    intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
                    startActivity(intent);
                    finish();
                }

                // item.setChecked(true);
                // mDrawerLayout.closeDrawers();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        if (getNavItem() > NAV_ITEM_INVALID) {
            navigationView.getMenu().getItem(getNavItem()).setChecked(true);
        }
    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setHint(R.string.search);
            mSearchView.setVoiceText("Set permission on Android 6+ !");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getData(query, 0);
                    // mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {
                    if (mFab != null) {
                        mFab.hide();
                    }
                }

                @Override
                public void onClose() {
                    if (mFab != null) {
                        mFab.show();
                    }
                }
            });

            if (mSearchView.getAdapter() == null) {
                List<SearchItem> suggestionsList = new ArrayList<>();
                suggestionsList.add(new SearchItem("search1"));
                suggestionsList.add(new SearchItem("search2"));
                suggestionsList.add(new SearchItem("search3"));

                SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
                searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                        String query = textView.getText().toString();
                        getData(query, position);
                        // mSearchView.close(false);
                    }
                });
                mSearchView.setAdapter(searchAdapter);
            }

            /*
            List<SearchFilter> filter = new ArrayList<>();
            filter.add(new SearchFilter("Filter1", true));
            filter.add(new SearchFilter("Filter2", true));
            mSearchView.setFilters(filter);
            //use mSearchView.getFiltersStates() to consider filter when performing search
            */
        }
    }

    protected void customSearchView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && mSearchView != null) {
            mSearchView.setVersion(extras.getInt(EXTRA_KEY_VERSION));
            mSearchView.setVersionMargins(extras.getInt(EXTRA_KEY_VERSION_MARGINS));
            mSearchView.setTheme(extras.getInt(EXTRA_KEY_THEME), true);
            mSearchView.setTextInput(extras.getString(EXTRA_KEY_TEXT));
        }
    }

    // ---------------------------------------------------------------------------------------------
    @CallSuper
    protected void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));

        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
        intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
        intent.putExtra(EXTRA_KEY_TEXT, text);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
    }

}