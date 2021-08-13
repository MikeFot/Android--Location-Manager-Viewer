package com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea;

public interface NmeaListenerManager {

    void register(Callback callback);

    void unregister();

    interface Callback {
        void onNmeaMessageReceived(String message);
    }


}
