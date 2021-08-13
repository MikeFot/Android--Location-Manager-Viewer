package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.regex.Pattern;

public class NougatNmeaListenerManager implements NmeaListenerManager {

    private final LocationManager locationManager;
    private Listener listener;

    public NougatNmeaListenerManager(final LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    @Override
    public void register(final Callback callback) {
        listener = new Listener(callback);
        locationManager.addNmeaListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void unregister() {
        if (listener != null) {
            locationManager.removeNmeaListener(listener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static class Listener implements OnNmeaMessageListener {

        private final Pattern PATTERN = Pattern.compile("$");

        private final Callback callback;

        public Listener(final Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onNmeaMessage(final String message, final long timestamp) {
            for (final String data : PATTERN.split(message)) {
                if (data.length() > 2) {
                    callback.onNmeaMessageReceived(data);
                }
            }
        }
    }

}
