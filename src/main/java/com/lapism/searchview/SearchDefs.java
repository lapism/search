package com.lapism.searchview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class SearchDefs {

    @IntDef({Version.TOOLBAR, Version.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Version {
        int TOOLBAR = 1000;
        int MENU_ITEM = 1001;
    }

    @IntDef({VersionMargins.TOOLBAR_SMALL, VersionMargins.TOOLBAR_BIG, VersionMargins.MENU_ITEM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VersionMargins {
        int TOOLBAR_SMALL = 2000;
        int TOOLBAR_BIG = 2001;
        int MENU_ITEM = 2002;
    }

    @IntDef({Shape.CLASSIC, Shape.ROUNDED_TOP, Shape.ROUNDED, Shape.OVAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
        int CLASSIC = 3000;
        int ROUNDED_TOP = 3001;
        int ROUNDED = 3002;
        int OVAL = 3003;
    }

    @IntDef({Logo.GOOGLE, Logo.G})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Logo {
        int GOOGLE = 4000;
        int G = 4001;
    }

    @IntDef({Theme.COLOR, Theme.LIGHT, Theme.DARK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {
        int COLOR = 5000;
        int LIGHT = 5001; // SYSTEM TODo CHECK BAREV
        int DARK = 5002;
    }

}
