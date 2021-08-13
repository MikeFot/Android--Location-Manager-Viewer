package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea;

import android.location.LocationManager;
import android.os.Build;

import com.michaelfotiadis.locationmanagerviewer.utils.AppLog;

public class NmeaListenerManagerProvider {

    public NmeaListenerManager getNmeaListenerManager(final LocationManager locationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AppLog.d("Using Nougat NMEA Listener");
            return new NougatNmeaListenerManager(locationManager);
        } else {
            AppLog.d("Using Pre-Nougat NMEA Listener");
            return new PreNougatNmeaListenerManager(locationManager);
        }
    }

}
