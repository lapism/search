package com.lapism.searchview.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


public class Leak extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}
