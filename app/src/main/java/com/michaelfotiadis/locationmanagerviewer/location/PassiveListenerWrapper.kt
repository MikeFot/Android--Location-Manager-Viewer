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

class PassiveListenerWrapper(
    private val locationManager: LocationManager
) {

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    fun registerForPassiveUpdates(
        minDistanceChangeForUpdates: Float,
        minTimeInMillisBetweenUpdates: Long
    ): Flow<LocationUpdate.PassiveLocationUpdate> {

        return callbackFlow {
            offer(LocationUpdate.PassiveLocationUpdate())
            val listener = LocationListener { location ->
                offer(
                    LocationUpdate.PassiveLocationUpdate(location)
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
                error("Passive Provider unavailable")
            }
            awaitClose {
                locationManager.removeUpdates(listener)
                cancel()
            }
        }
    }

    private companion object {
        const val LOCATION_SOURCE = LocationManager.PASSIVE_PROVIDER
    }

}