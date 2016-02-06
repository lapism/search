package com.lapism.searchview.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lapism.searchview.history.SearchHistoryDeleteAll;
import com.lapism.searchview.view.SearchView;

// import android.support.v7.app.ActionBarDrawerToggle;
// import android.support.v7.widget.SearchView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Button mDelete = (Button) findViewById(R.id.button_delete);
        Button mLightClassic = (Button) findViewById(R.id.button_light_classic);
        Button mLightColor = (Button) findViewById(R.id.button_light_color);
        Button mDarkClassic = (Button) findViewById(R.id.button_dark_classic);
        Button mDarkColor = (Button) findViewById(R.id.button_dark_color);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_git_hub_source);

        mDelete.setOnClickListener(this);
        mLightClassic.setOnClickListener(this);
        mLightColor.setOnClickListener(this);
        mDarkClassic.setOnClickListener(this);
        mDarkColor.setOnClickListener(this);
        mFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        switch (view.getId()) {
            case R.id.button_delete:
                Toast.makeText(this, "Search history deleted", Toast.LENGTH_SHORT).show();
                new SearchHistoryDeleteAll(this).execute();
                break;
            case R.id.button_light_classic:
                intent.putExtra("style", SearchView.STYLE_CLASSIC);
                intent.putExtra("theme", SearchView.THEME_LIGHT);
                startActivity(intent);
                break;
            case R.id.button_light_color:
                intent.putExtra("style", SearchView.STYLE_COLOR);
                intent.putExtra("theme", SearchView.THEME_LIGHT);
                startActivity(intent);
                break;
            case R.id.button_dark_classic:
                intent.putExtra("style", SearchView.STYLE_CLASSIC);
                intent.putExtra("theme", SearchView.THEME_DARK);
                startActivity(intent);
                break;
            case R.id.button_dark_color:
                intent.putExtra("style", SearchView.STYLE_COLOR);
                intent.putExtra("theme", SearchView.THEME_DARK);
                startActivity(intent);
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
    }

}