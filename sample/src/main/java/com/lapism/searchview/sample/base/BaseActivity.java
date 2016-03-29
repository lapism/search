package com.lapism.searchview.sample.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.sample.R;
import com.lapism.searchview.sample.activity.AboutActivity;
import com.lapism.searchview.sample.activity.MenuItemActivity;
import com.lapism.searchview.sample.activity.ResultActivity;
import com.lapism.searchview.sample.activity.ToolbarActivity;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String[] sCheeseStrings = {
            "Search 1", "Search 2"
    };
    protected static final int NAV_ITEM_INVALID = -1;
    protected static final int NAV_ITEM_TOOLBAR = 0;
    protected static final int NAV_ITEM_MENU_ITEM = 2;

    protected SearchView mSearchView = null;
    protected DrawerLayout mDrawerLayout = null;
    protected FloatingActionButton mFab = null;
    protected Toolbar mToolbar = null;

    private SearchHistoryTable mHistoryDatabase;

    // ---------------------------------------------------------------------------------------------
    protected int getNavItem() {
        return NAV_ITEM_INVALID;
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                // mToolbar.setNavigationContentDescription(getResources().getString(R.string.nav_drawer_description);
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() { // new DrawerLayout.DrawerListener();
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                if (mSearchView != null && mSearchView.isSearchOpen()) {
                    mSearchView.hide(true);
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

    private void setNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            if (getNavItem() > -1) {
                navigationView.getMenu().getItem(getNavItem()).setChecked(true);
            }
        }
    }

    private void setFab() {
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
        /*if (mSearchView != null) {
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_light_toolbar) {
            Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra("version", SearchView.VERSION_TOOLBAR);
            intent.putExtra("version_margins", SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra("theme", SearchView.THEME_LIGHT);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_dark_toolbar) {
            Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra("version", SearchView.VERSION_TOOLBAR);
            intent.putExtra("version_margins", SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra("theme", SearchView.THEME_DARK);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_light_menu_item) {
            Intent intent = new Intent(this, MenuItemActivity.class);
            intent.putExtra("version", SearchView.VERSION_MENU_ITEM);
            intent.putExtra("version_margins", SearchView.VERSION_MARGINS_MENU_ITEM);
            intent.putExtra("theme", SearchView.THEME_LIGHT);
            startActivity(intent);
        }

        if (id == R.id.nav_dark_menu_item) {
            Intent intent = new Intent(this, MenuItemActivity.class);
            intent.putExtra("version", SearchView.VERSION_MENU_ITEM);
            intent.putExtra("version_margins", SearchView.VERSION_MARGINS_MENU_ITEM);
            intent.putExtra("theme", SearchView.THEME_DARK);
            startActivity(intent);
        }

        if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START); // mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mSearchView != null && mSearchView.isSearchOpen()) {
            mSearchView.hide(true);
        } else {
            super.onBackPressed();
        }
    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);
        List<SearchItem> mSuggestionsList = new ArrayList<>();

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setHint(R.string.search);
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText("Voice");
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mSearchView.hide(false);
                    sexy(query, 0);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            List<SearchItem> mResultsList = new ArrayList<>();
            mResultsList.addAll(mHistoryDatabase.getAllItems());

            SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList);
            mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mSearchView.hide(false);
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    sexy(query, position);
                }
            });
            mSearchView.setAdapter(mSearchAdapter);
        }
    }

    protected void customSearchView() {
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSearchView.setVersion(extras.getInt("version"));
            mSearchView.setVersionMargins(extras.getInt("version_margins")); // TODO SEARCH INTENT
            mSearchView.setTheme(extras.getInt("theme"));
        }
    }

    protected void setViewPager() {
        final Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment(), getString(R.string.search_1));
        adapter.addFragment(new SearchFragment(), getString(R.string.search_2));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }


  /*  public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_delete:


                break;
            case R.id.fab_git_hub_source:
                String url = "https://github.com/lapism/SearchView";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            default:
                break;
        }
    }*/
    /*
    *                      mCheck.setChecked(false);
                Snackbar.make(view, "Search history deleted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mHistoryDatabase.clearDatabase();
       *
       *      mCh
    *        eck = (CheckableView) findViewById(R.id.checkableImageView);
        mCheck.setOnClickListener(this);
        mCheck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCheck.setChecked(true);
                return true; // false
            }
        });
    * */

    //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    private void sexy(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));

        Intent intent = new Intent(getApplicationContext(), ResultActivity.class); // LightToolbarActivity.this
        intent.putExtra("search", text);
        intent.putExtra("version", SearchView.VERSION_TOOLBAR);
        // intent.putExtra("theme", mTheme);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
    }
    // private static final Random RANDOM = new Random();
//    <include layout="@layout/include_list_viewpager" />
       /* switch (RANDOM.nextInt(5)) {
            case 0:
                return R.drawable.cheese_2;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_2;
            case 3:
                return R.drawable.cheese_2;
            case 4:
                return R.drawable.cheese_2;
            default:
        }*/

           /* final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // assert toolbar != null;
        // toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu));

        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }*/


// public static final String EXTRA_NAME = "cheese_name";
//final Intent intent = getIntent();
// final String cheeseName = intent.getStringExtra(EXTRA_NAME);

       /* final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }*/
}


// NavUtils.navigateUpFromSameTask(this);
// NavUtils.navigateUpTo();
// DatabaseUtils.
// finish();

               /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
                    mSearch = extras.getString("search");
            mVersion = extras.getInt("version");
            mTheme = extras.getInt("theme");

            if (mTheme == SearchCodes.THEME_LIGHT) {
                setTheme(R.style.AppThemeLight);
                checkedMenuItem = 0;
            }

            if (mTheme == SearchCodes.THEME_DARK) {
                setTheme(R.style.AppThemeDark);
                checkedMenuItem = 1;
            }
        }*/

   /* void showSearchView() { // todo fix
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
        mSuggestionsList.add(new SearchItem("Google"));
        mSuggestionsList.add(new SearchItem("Android"));
        mSearchAdapter.notifyDataSetChanged();
    }*/

      /*  Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTheme = extras.getInt("theme");

            if (mTheme == SearchCodes.THEME_LIGHT) {
                setTheme(R.style.AppThemeLight);
                checkedMenuItem = -1;
            }

            if (mTheme == SearchCodes.THEME_DARK) {
                setTheme(R.style.AppThemeDark);
                checkedMenuItem = -1;
            }
        }



        mToolbar = getToolbar();
        setTitle(mTheme == SearchCodes.THEME_LIGHT ? "Light" : "Dark");
        mToolbar.setTitle(mTheme == SearchCodes.THEME_LIGHT ? "Light" : "Dark");
        mToolbar.setSubtitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });/**/