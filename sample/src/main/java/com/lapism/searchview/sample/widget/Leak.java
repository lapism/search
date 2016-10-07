package com.lapism.searchview.sample.widget;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


public class Leak extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }

}
