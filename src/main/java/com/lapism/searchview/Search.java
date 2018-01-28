package com.lapism.searchview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.speech.RecognizerIntent;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


public class Search {

    private static final int SPEECH_REQUEST_CODE = 100;

    public static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void search(Activity activity, String text) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        activity.startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    public static boolean isAvailable(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    @IntDef({Logo.GOOGLE, Logo.G, Logo.HAMBURGER_ARROW, Logo.ARROW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Logo {
        int GOOGLE = 1000;
        int G = 1001;
        int HAMBURGER_ARROW = 1002;
        int ARROW = 1003;
    }

    @IntDef({Shape.CLASSIC, Shape.ROUNDED, Shape.OVAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
        int CLASSIC = 2000;
        int ROUNDED = 2001;
        int OVAL = 2002;
    }

    @IntDef({Theme.COLOR, Theme.LIGHT, Theme.DARK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {
        int COLOR = 3000;
        int LIGHT = 3001;
        int DARK = 3002;
    }

    @IntDef({Version.TOOLBAR, Version.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Version {
        int TOOLBAR = 4000;
        int MENU_ITEM = 4001;
    }

    @IntDef({VersionMargins.TOOLBAR_SMALL, VersionMargins.TOOLBAR_MEDIUM, VersionMargins.TOOLBAR_BIG, VersionMargins.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VersionMargins {
        int TOOLBAR_SMALL = 5000;
        int TOOLBAR_MEDIUM = 5001;
        int TOOLBAR_BIG = 5002;
        int MENU_ITEM = 5003;
    }

    // SearchLayout
    public interface OnMicClickListener {
        void onMicClick();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(CharSequence query);

        boolean onQueryTextChange(CharSequence newText);
    }

    // SearchBar
    public interface OnBarClickListener {
        void onBarClick();
    }

    // SearchView
    public interface OnLogoClickListener {
        void onLogoClick();
    }

    public interface OnOpenCloseListener {
        void onOpen();

        void onClose();
    }

}
