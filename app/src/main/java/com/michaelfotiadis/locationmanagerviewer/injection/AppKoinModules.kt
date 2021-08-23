package com.michaelfotiadis.locationmanagerviewer.injection

import android.location.LocationManager
import androidx.lifecycle.LifecycleService
import com.michaelfotiadis.locationmanagerviewer.data.ApplicationInitializer
import com.michaelfotiadis.locationmanagerviewer.data.ConfigurationRepository
import com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea.NmeaListenerManagerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        ConfigurationRepository()
    }
    single {
        ApplicationInitializer(
            context = androidContext(),
            configurationRepository = get()
        )
    }

}

val locationModule = module {
    single {
        (androidContext().getSystemService(LifecycleService.LOCATION_SERVICE) as LocationManager)
    }

    single {
        NmeaListenerManagerProvider(get()).getNmeaListenerManager()
    }
}