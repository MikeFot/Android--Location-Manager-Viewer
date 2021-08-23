package com.michaelfotiadis.locationmanagerviewer.data

import com.michaelfotiadis.locationmanagerviewer.BuildConfig

class ConfigurationRepository {

    val isDebugEnabled: Boolean
        get() = BuildConfig.DEBUG
    val isFirebaseEnabled: Boolean
        get() = BuildConfig.FIREBASE_ENABLED

}