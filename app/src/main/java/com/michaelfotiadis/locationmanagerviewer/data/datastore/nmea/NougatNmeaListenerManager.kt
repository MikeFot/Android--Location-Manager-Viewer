package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea

import android.annotation.SuppressLint
import android.location.LocationManager
import android.location.OnNmeaMessageListener
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import java.util.regex.Pattern

class NougatNmeaListenerManager(
    private val locationManager: LocationManager
) : NmeaListenerManager {

    private var listener: Listener? = null

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    override fun register(callback: NmeaListenerManager.Callback) {
        listener = Listener(callback).apply {
            locationManager.addNmeaListener(this, Handler(Looper.getMainLooper()))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun unregister() {
        if (listener != null) {
            locationManager.removeNmeaListener(listener!!)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    class Listener(private val callback: NmeaListenerManager.Callback) : OnNmeaMessageListener {
        override fun onNmeaMessage(message: String, timestamp: Long) {
            for (data in PATTERN.split(message)) {
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