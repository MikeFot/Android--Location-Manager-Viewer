package com.michaelfotiadis.locationmanagerviewer.service

import android.location.Location

sealed class LocationUpdate {

    data class GpsLocationUpdate(val location: Location? = null) : LocationUpdate()

    data class PassiveLocationUpdate(val location: Location? = null) : LocationUpdate()

    data class NetworkLocationUpdate(val location: Location? = null) : LocationUpdate()

    data class NmeaUpdate(val message: String) : LocationUpdate()

}
