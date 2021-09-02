package com.michaelfotiadis.locationmanagerviewer

import android.app.Application
import com.michaelfotiadis.locationmanagerviewer.data.ApplicationInitializer
import com.michaelfotiadis.locationmanagerviewer.injection.appModule
import com.michaelfotiadis.locationmanagerviewer.injection.homeActivityModule
import com.michaelfotiadis.locationmanagerviewer.injection.locationModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class LocationApplication : Application() {

    private val applicationInitializer: ApplicationInitializer by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@LocationApplication)
            modules(
                appModule,
                locationModule,
                homeActivityModule
            )
        }

        applicationInitializer.run {
            initialiseLogging()
            initialiseFirebase()
        }

    }
}