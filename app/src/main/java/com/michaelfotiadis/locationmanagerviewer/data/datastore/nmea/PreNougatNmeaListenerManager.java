package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea;

import android.annotation.SuppressLint;
import android.location.GpsStatus;
import android.location.LocationManager;

import java.util.regex.Pattern;

public class PreNougatNmeaListenerManager implements NmeaListenerManager {

    private final LocationManager locationManager;
    private Listener listener;

    public PreNougatNmeaListenerManager(final LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void register(Callback callback) {
        listener = new Listener(callback);
        locationManager.addNmeaListener(listener);
    }

    @Override
    public void unregister() {
        if (listener != null) {
            locationManager.removeNmeaListener(listener);
        }
    }

    public static class Listener implements GpsStatus.NmeaListener {

        private final Pattern PATTERN = Pattern.compile("$");

        private final NmeaListenerManager.Callback callback;

        public Listener(final NmeaListenerManager.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onNmeaReceived(
                final long timestamp,
                final String nmea
        ) {

            for (final String data : PATTERN.split(nmea)) {
                if (data.length() > 2) {
                    callback.onNmeaMessageReceived(data);
                }
            }

        }
    }

}
