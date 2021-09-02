package com.michaelfotiadis.locationmanagerviewer.service

import android.location.GnssStatus
import android.location.Location
import org.apache.commons.collections4.queue.CircularFifoQueue

sealed class LocationStatus {

    object ScanningStarted : LocationStatus()
    object ScanningStopped : LocationStatus()

    object PermissionsNotGranted : LocationStatus()
    object GpsProviderUnavailable : LocationStatus()
    object NetworkProviderUnavailable : LocationStatus()
    object PassiveProviderUnavailable : LocationStatus()
    object NmeaUpdatesUnavailable : LocationStatus()

    data class CombinedLocationUpdate(
        val gpsLocation: Location? = null,
        val passiveLocation: Location? = null,
        val networkLocation: Location? = null,
        val nmeaBuffer: CircularFifoQueue<String> = CircularFifoQueue(),
        val gnssStatus: GnssStatus? = null
    ) : LocationStatus()
}