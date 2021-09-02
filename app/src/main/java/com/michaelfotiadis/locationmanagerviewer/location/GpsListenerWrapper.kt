package com.michaelfotiadis.locationmanagerviewer.location

import android.annotation.SuppressLint
import android.location.LocationListener
import android.location.LocationManager
import com.michaelfotiadis.locationmanagerviewer.service.LocationUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GpsListenerWrapper(
    private val locationManager: LocationManager
) {

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    fun registerForGpsUpdates(
        minDistanceChangeForUpdates: Float,
        minTimeInMillisBetweenUpdates: Long
    ): Flow<LocationUpdate.GpsLocationUpdate> {

        return callbackFlow {
            offer(LocationUpdate.GpsLocationUpdate())
            val listener = LocationListener { location ->
                offer(
                    LocationUpdate.GpsLocationUpdate(location)
                )
            }
            if (locationManager.allProviders.contains(LOCATION_SOURCE)) {
                locationManager.requestLocationUpdates(
                    LOCATION_SOURCE,
                    minTimeInMillisBetweenUpdates,
                    minDistanceChangeForUpdates,
                    listener
                )
            } else {
                error("GPS Provider unavailable")
            }
            awaitClose {
                locationManager.removeUpdates(listener)
                cancel()
            }
        }
    }

    private companion object {
        const val LOCATION_SOURCE = LocationManager.GPS_PROVIDER
    }

}