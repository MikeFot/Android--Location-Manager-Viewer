package com.michaelfotiadis.locationmanagerviewer.service

import com.michaelfotiadis.locationmanagerviewer.location.GpsListenerWrapper
import com.michaelfotiadis.locationmanagerviewer.location.NetworkListenerWrapper
import com.michaelfotiadis.locationmanagerviewer.location.NmeaListenerWrapper
import com.michaelfotiadis.locationmanagerviewer.location.PassiveListenerWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val locationServiceModule = module {
    factory {
        GpsListenerWrapper(get())
    }
    factory {
        NetworkListenerWrapper(get())
    }
    factory {
        PassiveListenerWrapper(get())
    }
    factory {
        NmeaListenerWrapper(get())
    }
}