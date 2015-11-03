package com.lapism.searchview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
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
        // setContentView(R.layout.activity_search_classic);
        setContentView(R.layout.activity_search_design);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(theme == 0 ? "Light" : "Dark");
        mToolbar.setSubtitle(style == 0 ? "Classic" : "Color");
        setSupportActionBar(mToolbar);
        // mToolbar.setElevation(4);
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
        mSearchView.setVoiceSearch(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Snackbar.make(getApplicationContext(), "Query: " + query, Snackbar.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new SearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        List<SearchItem> mSuggestionsList = new ArrayList<>();
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Wi-Fi"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Bluetooth"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "GPS"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Ad-Hoc"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Google"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Android"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Piconet"));
        mSuggestionsList.add(new SearchItem(R.drawable.search_ic_search_black_24dp, "Scatternet"));

        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, theme);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView mText = (TextView) view.findViewById(R.id.textView_result);
                Toast.makeText(getApplicationContext(), mText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        mSearchView.setAdapter(mSearchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        // DEPRECATED, use onOptionsItemSelected(MenuItem item)
        // MenuItem item = menu.findItem(R.id.action_search);
        // mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                mSearchView.showSearch();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
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

}