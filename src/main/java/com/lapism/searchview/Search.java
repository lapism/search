package com.lapism.searchview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


public class Search {

    public static final int SPEECH_REQUEST_CODE = 100;

    public static void setVoiceSearch(@NonNull Activity activity, String text) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        activity.startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    public static boolean isVoiceSearchAvailable(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    @IntDef({Logo.GOOGLE, Logo.HAMBURGER_ARROW, Logo.ARROW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Logo {
        int GOOGLE = 1000;
        int HAMBURGER_ARROW = 1001;
        int ARROW = 1002;
    }

    @IntDef({Shape.CLASSIC, Shape.ROUNDED, Shape.OVAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
        int CLASSIC = 2000;
        int ROUNDED = 2001;
        int OVAL = 2002;
    }

    @IntDef({Theme.PLAY, Theme.GOOGLE, Theme.LIGHT, Theme.DARK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {
        int PLAY = 3000;
        int GOOGLE = 3001;
        int LIGHT = 3002;
        int DARK = 3003;
    }

    @IntDef({VersionMargins.BAR, VersionMargins.TOOLBAR, VersionMargins.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VersionMargins {
        int BAR = 5000;
        int TOOLBAR = 5001;
        int MENU_ITEM = 5002;
    }

    @IntDef({Version.TOOLBAR, Version.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Version {
        int TOOLBAR = 4000;
        int MENU_ITEM = 4001;
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

        void onQueryTextChange(CharSequence newText);
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
