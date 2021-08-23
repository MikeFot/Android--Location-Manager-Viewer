package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea

import android.annotation.SuppressLint
import android.location.GpsStatus.NmeaListener
import android.location.LocationManager
import java.util.regex.Pattern

class PreNougatNmeaListenerManager(
    private val locationManager: LocationManager
) : NmeaListenerManager {

    private var listener: Listener? = null

    @SuppressLint("MissingPermission")
    override fun register(callback: NmeaListenerManager.Callback) {
        listener = Listener(callback).apply {
            locationManager.addNmeaListener(this)
        }

    }

    override fun unregister() {
        listener?.apply {
            locationManager.removeNmeaListener(this)
        }
    }

    class Listener(private val callback: NmeaListenerManager.Callback) : NmeaListener {

        override fun onNmeaReceived(
            timestamp: Long,
            nmea: String
        ) {
            for (data in PATTERN.split(nmea)) {
                if (data.length > 2) {
                    callback.onNmeaMessageReceived(data)
                }
            }
        }
    }

    private companion object {
        val PATTERN: Pattern = Pattern.compile("$")
    }
}