package com.michaelfotiadis.locationmanagerviewer;

import android.app.Application;

import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class MyApp extends Application {
	public String TAG = "MYAPP_CONTEXT";
	@Override
	public void onCreate() {
		Logger.i(TAG, "MyApp Context Created");
		super.onCreate();
	}
}
