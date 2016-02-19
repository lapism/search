package com.lapism.searchview.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.check.CheckableImageView;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.List;


public class ToolbarActivity extends BaseActivity implements View.OnClickListener {

    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private int mVersion = SearchCodes.VERSION_TOOLBAR;
    private int mStyle = SearchCodes.STYLE_TOOLBAR_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;
    private CheckableImageView mCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mVersion = extras.getInt("version");
            mStyle = extras.getInt("style");
            mTheme = extras.getInt("theme");

            if (mTheme == SearchCodes.THEME_LIGHT) {
                setTheme(R.style.AppThemeLight);
                checkedMenuItem = 0;
            }

            if (mTheme == SearchCodes.THEME_DARK) {
                setTheme(R.style.AppThemeDark);
                checkedMenuItem = 1;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Button mDelete = (Button) findViewById(R.id.button_delete);
        Button mClassic = (Button) findViewById(R.id.button_classic);
        Button mColor = (Button) findViewById(R.id.button_color);
        final FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_git_hub_source);
        mCheck = (CheckableImageView) findViewById(R.id.checkableImageView);
        mCheck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCheck.setChecked(true);
                return true; // false
            }
        });

        // TODO ADD MENU ITEM CLICK LISTENER

        mDelete.setOnClickListener(this);
        mClassic.setOnClickListener(this);
        mColor.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mCheck.setOnClickListener(this);

        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        mSearchView = (SearchView) findViewById(R.id.searchView);
        // important -------------------------------------------------------------------------------
        mSearchView.setVersion(mVersion);
        mSearchView.setStyle(mStyle);
        mSearchView.setTheme(mTheme);
        // -----------------------------------------------------------------------------------------
        mSearchView.setDivider(true);
        mSearchView.setHint("Search");
        mSearchView.setHint(R.string.search);
        mSearchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        mSearchView.setVoice(true);
        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(360);
        mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistoryDatabase.addItem(new SearchItem(query));
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, mTheme);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        mSearchView.setAdapter(mSearchAdapter);

        showSearchView(); // TODO FIX
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

    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
        mSuggestionsList.add(new SearchItem("Google"));
        mSuggestionsList.add(new SearchItem("Android"));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_delete:
                Snackbar.make(view, "Search history deleted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mHistoryDatabase.clearDatabase();
                break;
            case R.id.button_classic:
                intent = new Intent(this, MenuItemActivity.class);
                intent.putExtra("version", SearchCodes.VERSION_MENU_ITEM);
                intent.putExtra("style", SearchCodes.STYLE_MENU_ITEM_CLASSIC);
                if (mTheme == SearchCodes.THEME_LIGHT) {
                    intent.putExtra("theme", SearchCodes.THEME_LIGHT);
                }
                if (mTheme == SearchCodes.THEME_DARK) {
                    intent.putExtra("theme", SearchCodes.THEME_DARK);
                }
                startActivity(intent);
                break;
            case R.id.button_color:
                intent = new Intent(this, MenuItemActivity.class);
                intent.putExtra("version", SearchCodes.VERSION_MENU_ITEM);
                intent.putExtra("style", SearchCodes.STYLE_MENU_ITEM_COLOR);
                if (mTheme == SearchCodes.THEME_LIGHT) {
                    intent.putExtra("theme", SearchCodes.THEME_LIGHT);
                }
                if (mTheme == SearchCodes.THEME_DARK) {
                    intent.putExtra("theme", SearchCodes.THEME_DARK);
                }
                startActivity(intent);
                break;
            case R.id.fab_git_hub_source:
                String url = "https://github.com/lapism/SearchView";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.checkableImageView:
                mCheck.setChecked(false);
                Snackbar.make(view, "Search history deleted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mHistoryDatabase.clearDatabase();
                break;
            default:
                break;
        }
    }

}