package com.lapism.searchview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Search {

    @IntDef({Logo.GOOGLE, Logo.G, Logo.HAMBURGER, Logo.ARROW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Logo {
        int GOOGLE = 1000;
        int G = 1001;
        int HAMBURGER = 1002;
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

    @IntDef({VersionMargins.TOOLBAR_SMALL, VersionMargins.TOOLBAR_BIG, VersionMargins.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VersionMargins {
        int TOOLBAR_SMALL = 5000;
        int TOOLBAR_BIG = 5001;
        int MENU_ITEM = 5002;
    }

    public interface OnBarClickListener {
        void onBarClick();
    }

    public interface OnLogoClickListener {
        void onLogoClick();
    }

    public interface OnMicClickListener {
        void onMicClick();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

    public interface OnQueryTextListener {
        boolean onQueryTextChange(String newText);

        boolean onQueryTextSubmit(String query);
    }

    public interface OnOpenCloseListener {
        boolean onClose();

        boolean onOpen();
    }

}
