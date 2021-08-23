package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea

interface NmeaListenerManager {

    fun register(callback: Callback)

    fun unregister()

    interface Callback {
        fun onNmeaMessageReceived(message: String?)
    }
}