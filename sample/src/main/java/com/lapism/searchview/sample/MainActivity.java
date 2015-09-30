package com.lapism.searchview.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Button mLightClassic = (Button) findViewById(R.id.button_light_classic);
        Button mLightColor = (Button) findViewById(R.id.button_light_color);
        Button mDarkClassic = (Button) findViewById(R.id.button_dark_classic);
        Button mDarkColor = (Button) findViewById(R.id.button_dark_color);
        Button mGitSource = (Button) findViewById(R.id.button_git_source);

        mLightClassic.setOnClickListener(this);
        mLightColor.setOnClickListener(this);
        mDarkClassic.setOnClickListener(this);
        mDarkColor.setOnClickListener(this);
        mGitSource.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        switch (view.getId()) {
            case R.id.button_light_classic:
                intent.putExtra("style", 0);
                intent.putExtra("theme", 0);
                startActivity(intent);
                break;
            case R.id.button_light_color:
                intent.putExtra("style", 1);
                intent.putExtra("theme", 0);
                startActivity(intent);
                break;
            case R.id.button_dark_classic:
                intent.putExtra("style", 0);
                intent.putExtra("theme", 1);
                startActivity(intent);
                break;
            case R.id.button_dark_color:
                intent.putExtra("style", 1);
                intent.putExtra("theme", 1);
                startActivity(intent);
                break;
            case R.id.button_git_source:
                final String appPackageName = getPackageName();
                String url = "";
                try {
                    this.getPackageManager().getPackageInfo("com.android.vending", 0);
                    url = "market://details?id=" + appPackageName;
                } catch (final Exception e) {
                    url = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                final Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            default:
                break;
        }
    }

}

