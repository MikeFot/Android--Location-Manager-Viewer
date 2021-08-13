package com.michaelfotiadis.locationmanagerviewer.data.containers;

import android.location.GpsSatellite;
import android.location.Location;

import com.michaelfotiadis.locationmanagerviewer.utils.AppLog;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Calendar;
import java.util.List;

public class LocationData {

    // Unit Suffixes
    private static final String NOT_AVAILABLE = "N/A";
    private static final String METERS_SECONDS = " m/sec";
    private static final String MILLISECONDS = " ms";
    private static final String DEGREES = " deg";
    private static final String METRES = " m";
    private static final String CONFIDENCE = " (68% confidence)";
    private final int mNmeaBufferSize = 50;
    private Location mLocation;
    private CircularFifoQueue<String> nmeaBuffer;
    private List<GpsSatellite> mSatellites;
    private int mSatellitesInFix = 0;
    private String mGPSEvent = "";

    public LocationData() {
        nmeaBuffer = new CircularFifoQueue<>(mNmeaBufferSize);
    }

    public LocationData(Location location) {
        setLocation(location);
    }

    public void appendToNmea(String nmea) {
        if (!nmea.equals("")) {
            nmeaBuffer.add(nmea);
        }
    }

    public float getAccuracy() {
        if (this.mLocation != null) {
            return mLocation.getAccuracy();
        } else {
            return 0;
        }
    }

    public String getAccuracyAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getAccuracy()) + METRES + CONFIDENCE;
        } else {
            return NOT_AVAILABLE;
        }
    }

    public double getAltitude() {
        if (this.mLocation != null) {
            return mLocation.getAltitude();
        } else {
            return 0;
        }
    }

    public String getAltitudeAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getAltitude()) + METRES;
        } else {
            return NOT_AVAILABLE;
        }
    }

    public float getBearing() {
        if (this.mLocation != null) {
            return mLocation.getBearing();
        } else {
            return 0;
        }
    }

    public String getBearingAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getBearing()) + DEGREES;
        } else {
            return NOT_AVAILABLE;
        }
    }

    public long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }


    public String getCurrentTimeAsString() {
        if (getUtcFixTime() > 0) {
            return String.valueOf(getCurrentTime()) + MILLISECONDS;
        } else {
            return NOT_AVAILABLE;
        }
    }

    public String getGPSEvent() {
        return mGPSEvent;
    }

    public void setGPSEvent(String mGPSEvent) {
        this.mGPSEvent = mGPSEvent;
    }

    public double getLatitude() {
        if (this.mLocation != null) {
            return mLocation.getLatitude();
        } else {
            return 0;
        }
    }

    public String getLatitudeAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getLatitude()) + DEGREES;
        } else {
            return NOT_AVAILABLE;
        }
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        if (location != null) {
            this.mLocation = location;
        } else {
            AppLog.e("Null Location");
        }
    }

    public double getLongitude() {
        if (this.mLocation != null) {
            return mLocation.getLongitude();
        } else {
            return 0;
        }
    }

    public String getLongitudeAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getLongitude() + DEGREES);
        } else {
            return NOT_AVAILABLE;
        }
    }

    public int getMaxSatellites() {
        return getSatellitesInFix();
    }

    public String getNmea() {
        return nmeaBuffer.toString();
    }

    public String getProvider() {
        if (this.mLocation != null) {
            return mLocation.getProvider();
        } else {
            return NOT_AVAILABLE;
        }
    }

    public List<GpsSatellite> getSatellites() {
        return mSatellites;
    }

    public void setSatellites(List<GpsSatellite> satellites) {
        this.mSatellites = satellites;
    }

    public int getSatellitesSize() {
        if (mSatellites != null) {
            return mSatellites.size();
        } else {
            return 0;
        }
    }

    public float getSpeed() {
        if (this.mLocation != null) {
            return mLocation.getSpeed();
        } else {
            return 0;
        }
    }

    public String getSpeedAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getSpeed() + METERS_SECONDS);
        } else {
            return NOT_AVAILABLE;
        }
    }

    public String getTimeSinceLastFixAsString() {
        if (this.mLocation != null && getUtcFixTime() > 0) {
            return String.valueOf(getCurrentTime() - getUtcFixTime()) + MILLISECONDS;
        } else {
            return NOT_AVAILABLE;
        }
    }

    public long getUtcFixTime() {
        if (this.mLocation != null) {
            return mLocation.getTime();
        } else {
            return 0;
        }
    }

    public String getUtcFixTimeAsString() {
        if (this.mLocation != null) {
            return String.valueOf(mLocation.getTime() + MILLISECONDS);
        } else {
            return NOT_AVAILABLE;
        }
    }

    public int getSatellitesInFix() {
        return mSatellitesInFix;
    }

    public void setSatellitesInFix(int satellitesInFix) {
        this.mSatellitesInFix = satellitesInFix;
    }
}
