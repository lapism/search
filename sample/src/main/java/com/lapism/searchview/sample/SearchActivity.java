package com.lapism.searchview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchViewAdapter;
import com.lapism.searchview.SearchViewItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private int theme = 0;
    private int style = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            style = extras.getInt("style");
            theme = extras.getInt("theme");
            if (theme == 0)
                setTheme(R.style.AppThemeLight);
            if (theme == 1)
                setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(theme == 0 ? "Light" : "Dark");
        mToolbar.setSubtitle(style == 0 ? "Classic" : "Color");
        setSupportActionBar(mToolbar);
        // mToolbar.inflateMenu(R.menu.menu_settings);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setStyle(style);
        mSearchView.setTheme(theme);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {}

            @Override
            public void onSearchViewClosed() {}
        });

        List<SearchViewItem> mSuggestionsList = new ArrayList<>();
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Wi-Fi"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Bluetooth"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "GPS"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Ad-Hoc"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Google"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Android"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Piconet"));
        mSuggestionsList.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "Scatternet"));

        List<SearchViewItem> mResultsList = new ArrayList<>();
        SearchViewAdapter mSearchViewAdapter = new SearchViewAdapter(this, mResultsList, mSuggestionsList, theme);
        mSearchViewAdapter.setOnItemClickListener(new SearchViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView mText = (TextView) view.findViewById(R.id.textView_result);
                CharSequence text = "Item!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });
        mSearchView.setAdapter(mSearchViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

}