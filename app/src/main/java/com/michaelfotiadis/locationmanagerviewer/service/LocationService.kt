package com.michaelfotiadis.locationmanagerviewer.service

import android.Manifest
import android.content.Intent
import android.os.*
import androidx.lifecycle.LifecycleService
import com.anthonycr.grant.PermissionsManager
import com.michaelfotiadis.locationmanagerviewer.location.GpsListenerWrapper
import com.michaelfotiadis.locationmanagerviewer.location.NetworkListenerWrapper
import com.michaelfotiadis.locationmanagerviewer.location.NmeaListenerWrapper
import com.michaelfotiadis.locationmanagerviewer.location.PassiveListenerWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.apache.commons.collections4.queue.CircularFifoQueue
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class LocationService : LifecycleService(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private val binder = LocationBinder()

    private val gpsListenerWrapper: GpsListenerWrapper by inject()
    private val networkListenerWrapper: NetworkListenerWrapper by inject()
    private val passiveListenerWrapper: PassiveListenerWrapper by inject()
    private val nmeaListenerWrapper: NmeaListenerWrapper by inject()

    private var scanningStatus = false
    val statusFlow = MutableStateFlow<LocationStatus>(LocationStatus.ScanningStopped)

    private var updatesJob: Job? = null

    inner class LocationBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        loadKoinModules(locationServiceModule)
        Timber.d("Service Created")
        scanningStatus = false

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Service Started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        unloadKoinModules(locationServiceModule)
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
        launch {
            if (isFineLocationPermissionGranted()) {
                scanningStatus = true
                statusFlow.emit(LocationStatus.ScanningStarted)
                requestLocationUpdates()
            } else {
                statusFlow.emit(LocationStatus.PermissionsNotGranted)
            }
        }
    }

    fun stopScanning() {
        launch {
            scanningStatus = false
            unregisterLocationListeners()
            statusFlow.emit(LocationStatus.ScanningStopped)
            Timber.d("Stopped scanning")
        }
    }

    private fun isFineLocationPermissionGranted(): Boolean {
        return PermissionsManager.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestLocationUpdates() {

        val nmeaBuffer: CircularFifoQueue<String> = CircularFifoQueue(NMEA_BUFFER_SIZE)

        updatesJob = launch {

            combine(
                gpsListenerWrapper.registerForGpsUpdates(
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES
                )
                    .catch {
                        statusFlow.emit(LocationStatus.GpsProviderUnavailable)
                    },
                networkListenerWrapper.registerForNetworkUpdates(
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES
                )
                    .catch {
                        statusFlow.emit(LocationStatus.NetworkProviderUnavailable)
                    },
                passiveListenerWrapper.registerForPassiveUpdates(
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES
                )
                    .catch {
                        statusFlow.emit(LocationStatus.PassiveProviderUnavailable)
                    },
                nmeaListenerWrapper.registerForNmeaUpdates()
                    .catch {
                        statusFlow.emit(LocationStatus.NmeaUpdatesUnavailable)
                    },
                transform = { gpsUpdate, networkUpdate, passiveUpdate, nmeaSentence ->

                    LocationStatus.CombinedLocationUpdate(
                        gpsLocation = gpsUpdate.location,
                        networkLocation = networkUpdate.location,
                        passiveLocation = passiveUpdate.location,
                        gnssStatus = null,
                        nmeaBuffer = nmeaBuffer.apply {
                            if (nmeaSentence.message.isNotBlank()) {
                                add(nmeaSentence.message)
                            }
                        }
                    )
                }

            )
                .collect(statusFlow::emit)
        }
    }

    private fun unregisterLocationListeners() {
        updatesJob?.cancel()
        updatesJob = null
    }

    private companion object {
        // The minimum distance to change Updates in meters
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 0f

        // The minimum time between updates in milliseconds
        const val MIN_TIME_BW_UPDATES: Long = 2000

        const val NMEA_BUFFER_SIZE = 50
    }

}