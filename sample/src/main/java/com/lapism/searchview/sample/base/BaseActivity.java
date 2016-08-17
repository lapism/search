package com.lapism.searchview.sample.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.activity.AboutActivity;
import com.lapism.searchview.sample.activity.FiltersActivity;
import com.lapism.searchview.sample.activity.HistoryToggleActivity;
import com.lapism.searchview.sample.activity.MenuItemActivity;
import com.lapism.searchview.sample.activity.ResultActivity;
import com.lapism.searchview.sample.activity.ToggleActivity;
import com.lapism.searchview.sample.activity.ToolbarActivity;
import com.lapism.searchview.sample.view.FragmentAdapter;
import com.lapism.searchview.sample.view.SearchFragment;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String EXTRA_KEY_VERSION = "version";
    protected static final String EXTRA_KEY_THEME = "theme";
    protected static final int NAV_ITEM_INVALID = -1;
    protected static final int NAV_ITEM_TOOLBAR = 0;
    protected static final int NAV_ITEM_TOOLBAR_DARK = 1;
    protected static final int NAV_ITEM_MENU_ITEM = 2;
    protected static final int NAV_ITEM_MENU_ITEM_DARK = 3;
    protected static final int NAV_ITEM_TOGGLE = 4;
    protected static final int NAV_ITEM_HISTORY_TOGGLE = 5;
    protected static final int NAV_ITEM_FILTERS = 6;
    private static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
    private static final String EXTRA_KEY_TEXT = "text";
    protected SearchView mSearchView = null;
    protected DrawerLayout mDrawerLayout = null;
    protected Toolbar mToolbar = null;

    protected FloatingActionButton mFab = null;
    private SearchHistoryTable mHistoryDatabase;

    // ---------------------------------------------------------------------------------------------
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setFab();
        setDrawer();
        setNavigationView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_light_toolbar || id == R.id.nav_filters_version) {
            Intent intent = new Intent(this, id == R.id.nav_light_toolbar ? ToolbarActivity.class : FiltersActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_dark_toolbar) {
            Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_DARK);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_light_menu_item) {
            Intent intent = new Intent(this, MenuItemActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_MENU_ITEM);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_MENU_ITEM);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_dark_menu_item) {
            Intent intent = new Intent(this, MenuItemActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_MENU_ITEM);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_MENU_ITEM);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_DARK);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_toggle_versions || id == R.id.nav_history_toggle_versions) {
            Intent intent = new Intent(this, id == R.id.nav_toggle_versions ? ToggleActivity.class : HistoryToggleActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            finish();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START); // mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else //noinspection StatementWithEmptyBody
            if (mSearchView != null && mSearchView.isSearchOpen()) { // TODO
                // mSearchView.close(true);
            } else {
                super.onBackPressed();
            }
    }

    // ---------------------------------------------------------------------------------------------
    protected int getNavItem() {
        return NAV_ITEM_INVALID;
    }

    private void getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                mToolbar.setNavigationContentDescription(getResources().getString(R.string.app_name));
                setSupportActionBar(mToolbar);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    protected void setFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_delete);
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHistoryDatabase.clearDatabase();
                    Snackbar.make(v, "Search history deleted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }

    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (mDrawerLayout != null) {
            mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() { // new DrawerLayout.DrawerListener();
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
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
                    invalidateOptionsMenu();
                    if (mFab != null) {
                        mFab.show();
                    }
                }
            });
        }
    }

    private void setNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            if (getNavItem() > -1) {
                navigationView.getMenu().getItem(getNavItem()).setChecked(true);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    protected void setViewPager() {
        final FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment(), getString(R.string.installed));
        adapter.addFragment(new SearchFragment(), getString(R.string.all));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setHint(R.string.search);
            mSearchView.setTextSize(16);
            mSearchView.setHint("Search");
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText("Set permission on Android 6+ !");
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
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

            if (mSearchView.getAdapter() == null)
            {
                List<SearchItem> suggestionsList = new ArrayList<>();
                suggestionsList.add(new SearchItem("search1"));
                suggestionsList.add(new SearchItem("search2"));
                suggestionsList.add(new SearchItem("search3"));

                SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
                searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
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
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSearchView.setVersion(extras.getInt(EXTRA_KEY_VERSION));
            mSearchView.setVersionMargins(extras.getInt(EXTRA_KEY_VERSION_MARGINS));
            mSearchView.setTheme(extras.getInt(EXTRA_KEY_THEME), true);
            mSearchView.setText(extras.getString(EXTRA_KEY_TEXT));
        }
    }

    @CallSuper
    protected void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));

        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR_ICON);
        intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
        intent.putExtra(EXTRA_KEY_TEXT, text);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
    }

    protected void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
        recreate();
    }

}