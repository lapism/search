package com.lapism.searchview.sample.future;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.sample.R;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {

    private MenuItem searchItem;
    private SearchRecentSuggestions suggestions;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.search_view);

        suggestions = new SearchRecentSuggestions(this,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(1000);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Collapse the search menu when the user hits the back key
        searchAutoComplete.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    showSearch(false);
            }
        });

        try {
            // This sets the cursor
            // resource ID to 0 or @null
            // which will make it visible
            // on white background
            Field mCursorDrawableRes = TextView.class
                    .getDeclaredField("mCursorDrawableRes");

            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchAutoComplete, 0);

        } catch (Exception e) {
        }

        findViewById(R.id.view_shadow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = SuggestionProvider.generateRandomSuggestion();

                suggestions.saveRecentQuery(query, "is a nice cheese");
            }
        });

        findViewById(R.id.view_shadow).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        suggestions.clearHistory();
                    }
                });

        CheckBox submitEnabled = (CheckBox) findViewById(R.id.button_light_classic);
        submitEnabled.setChecked(searchView.isSubmitButtonEnabled());
        submitEnabled
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        searchView.setSubmitButtonEnabled(isChecked);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        searchItem = menu.add(android.R.string.search_go);

        menu.add("One");
        menu.add("Two");
        menu.add("Three");

        searchItem.setIcon(R.drawable.search_ic_arrow_back_black_24dp);

        MenuItemCompat.setActionView(searchItem, searchView);

        MenuItemCompat.setShowAsAction(searchItem,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS
                        | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSearch(false);
        Bundle extras = intent.getExtras();
        String userQuery = String.valueOf(extras.get(SearchManager.USER_QUERY));
        String query = String.valueOf(extras.get(SearchManager.QUERY));

        Toast.makeText(this, "query: " + query + " user_query: " + userQuery,
                Toast.LENGTH_SHORT).show();
    }

    protected void showSearch(boolean visible) {
        if (visible)
            MenuItemCompat.expandActionView(searchItem);
        else
            MenuItemCompat.collapseActionView(searchItem);
    }

    @Override
    public boolean onSearchRequested() {
        showSearch(true);
        return false;
    }
}
