package com.michaelfotiadis.locationmanagerviewer.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.anthonycr.grant.PermissionsManager
import com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea.NmeaListenerManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class LocationService : LifecycleService() {

    private val binder = LocationBinder()
    private val locationManager: LocationManager by inject()
    private val nmeaListenerManager: NmeaListenerManager by inject()

    private val statusChannel = Channel<LocationStatus>()
    private var scanningStatus = false
    val statusFlow = statusChannel.receiveAsFlow()

    sealed class LocationStatus {

        object ScanningStarted : LocationStatus()
        object ScanningStopped : LocationStatus()
        object PermissionsNotGranted : LocationStatus()

    }

    inner class LocationBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("Service Created")
        scanningStatus = false
        statusChannel.offer(LocationStatus.ScanningStopped)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Service Started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        statusChannel.close()
        super.onDestroy()
        Timber.d("Service Destroyed")
    }

    fun toggleScanning() {
        if (scanningStatus) {
            stopScanning()
        } else {
            startScanning()
        }
    }

    fun startScanning() {
        if (isFineLocationPermissionGranted()) {
            scanningStatus = true
            statusChannel.offer(LocationStatus.ScanningStarted)
            requestLocationUpdates()
        } else {
            statusChannel.offer(LocationStatus.PermissionsNotGranted)
        }
    }

    fun stopScanning() {
        scanningStatus = false
        unregisterLocationListeners()
        statusChannel.offer(LocationStatus.ScanningStopped)
    }

    private fun isFineLocationPermissionGranted(): Boolean {
        return PermissionsManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestLocationUpdates() {
        requestSourceLocationUpdates(LocationManager.GPS_PROVIDER, gpsLocationListener)
        requestSourceLocationUpdates(LocationManager.NETWORK_PROVIDER, networkLocationListener)
        requestSourceLocationUpdates(LocationManager.PASSIVE_PROVIDER, passiveLocationListener)
    }

    @SuppressLint("MissingPermission")
    private fun requestSourceLocationUpdates(source: String, locationListener: LocationListener) {
        Timber.d(
            "Requesting $source Updates with time between updates $MIN_TIME_BW_UPDATES " +
                    "and min distance change for updates $MIN_DISTANCE_CHANGE_FOR_UPDATES"
        )
        if (locationManager.allProviders.contains(source)) {
            locationManager.requestLocationUpdates(
                source,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener
            )
        } else {
            Timber.e("$source Provider unavailable")
        }
    }

    private fun unregisterLocationListeners() {
        locationManager.removeUpdates(gpsLocationListener)
        locationManager.removeUpdates(networkLocationListener)
        locationManager.removeUpdates(passiveLocationListener)
    }

    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Timber.d("On GPS Location Changed $location")
        }
    }

    private val networkLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Timber.d("On Network Location Changed $location")
        }
    }

    private val passiveLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Timber.d("On Passive Location Changed $location")
        }
    }

    private companion object {
        // The minimum distance to change Updates in meters
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 0f

        // The minimum time between updates in milliseconds
        const val MIN_TIME_BW_UPDATES: Long = 2000
    }

}