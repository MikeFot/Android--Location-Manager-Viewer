@file:Suppress("DEPRECATION")

package com.michaelfotiadis.locationmanagerviewer.location

import android.annotation.SuppressLint
import android.location.GpsStatus
import android.location.LocationManager
import android.location.OnNmeaMessageListener
import android.os.Build
import com.michaelfotiadis.locationmanagerviewer.service.LocationUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.regex.Pattern


@ExperimentalCoroutinesApi
class NmeaListenerWrapper(
    private val locationManager: LocationManager
) {

    @SuppressLint("MissingPermission")
    fun registerForNmeaUpdates(): Flow<LocationUpdate.NmeaUpdate> {
        Timber.d("Asking for NMEA updates")
        return callbackFlow {
            offer(LocationUpdate.NmeaUpdate(""))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                val listener = OnNmeaMessageListener { message, _ ->
                    for (data in PATTERN.split(message ?: "")) {
                        if (data.length > 2) {
                            offer(LocationUpdate.NmeaUpdate(data))
                        }
                    }
                }
                val isAdded = locationManager.addNmeaListener(listener, null)
                Timber.d("NMEA Added $isAdded")
                if (!isAdded) {
                    error(LocationUpdate.NmeaUpdate("Failed to instantiate NMEA Listener."))
                }
                awaitClose {
                    Timber.d("NMEA updates cancelled")
                    if (isAdded) {
                        locationManager.removeNmeaListener(listener)
                    }
                    cancel()
                }
            } else {

                val listener = GpsStatus.NmeaListener { _, nmea ->
                    for (data in PATTERN.split(nmea ?: "")) {
                        if (data.length > 2) {
                            offer(LocationUpdate.NmeaUpdate(data))
                        }
                    }
                }
                val isAdded = locationManager.addNmeaListener(listener)
                if (!isAdded) {
                    error(LocationUpdate.NmeaUpdate("Failed to instantiate NMEA Listener."))
                }
                awaitClose {
                    Timber.d("NMEA updates cancelled")
                    if (isAdded) {
                        locationManager.removeNmeaListener(listener)
                    }
                    cancel()
                }
            }
        }
    }

    private companion object {
        val PATTERN: Pattern = Pattern.compile("$")
    }
}