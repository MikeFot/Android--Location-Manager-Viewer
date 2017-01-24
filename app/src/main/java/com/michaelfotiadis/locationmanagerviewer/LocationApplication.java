package com.michaelfotiadis.locationmanagerviewer;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.michaelfotiadis.locationmanagerviewer.data.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.utils.AppLog;

import io.fabric.sdk.android.Fabric;

public class LocationApplication extends Application {
    @Override
    public void onCreate() {
        AppLog.i("Singleton Initialised with App Context");
        Singleton.getInstance().setContext(this.getApplicationContext());
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}
