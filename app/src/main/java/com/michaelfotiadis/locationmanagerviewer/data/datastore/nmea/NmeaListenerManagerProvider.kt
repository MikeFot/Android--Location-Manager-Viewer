package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea

import android.location.LocationManager
import android.os.Build
import timber.log.Timber

class NmeaListenerManagerProvider(private val locationManager: LocationManager) {

    fun getNmeaListenerManager(): NmeaListenerManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Timber.d("Using Nougat NMEA Listener")
            NougatNmeaListenerManager(locationManager)
        } else {
            Timber.d("Using Pre-Nougat NMEA Listener")
            PreNougatNmeaListenerManager(locationManager)
        }
    }

}