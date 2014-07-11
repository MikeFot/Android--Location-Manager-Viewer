package com.michaelfotiadis.locationmanagerviewer.containers;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import android.location.GpsSatellite;
import android.location.Location;

import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class MyLocationData {

	private Location location;
	private CircularFifoQueue<String> nmeaBuffer;
	private  List<GpsSatellite>satellites;
	private int satellitesInFix = 0;

	private final String TAG = "GPS Data Object";

	

	private int nmeaBufferSize = 50;
	private String mGPSEvent = "";

	// Unit Suffixes
	private final String _not_available = "N/A";
	private final String _meters_seconds = " m/sec";
	private final String _milliseconds = " ms";
	private final String _degrees = " deg";
	private final String _metres = " m";
	private final String _confidence = " (68% confidence)";

	public MyLocationData() {
		nmeaBuffer = new CircularFifoQueue<>(nmeaBufferSize);
	}

	public MyLocationData(Location location) {
		setLocation(location);
	}

	public void appendToNmea(String nmea) {
		if (!nmea.equals("")) {
			nmeaBuffer.add(nmea);
		}
	}

	public float getAccuracy() {
		if (this.location != null) {
			return location.getAccuracy();
		} else {
			return 0;
		}
	}

	public String getAccuracyAsString() {
		if (this.location != null) {
			return String.valueOf(location.getAccuracy()) + _metres + _confidence;
		} else {
			return _not_available;
		}
	}

	public double getAltitude() {
		if (this.location != null) {
			return location.getAltitude();
		} else {
			return 0;
		}
	}

	public String getAltitudeAsString() {
		if (this.location != null) {
			return String.valueOf(location.getAltitude()) +_metres;
		} else {
			return _not_available;
		}
	}

	public float getBearing() {
		if (this.location != null) {
			return location.getBearing();
		} else {
			return 0;
		}
	}

	public String getBearingAsString() {
		if (this.location != null) {
			return String.valueOf(location.getBearing()) + _degrees;
		} else {
			return _not_available;
		}
	}

	public long getCurrentTime() {
		return Calendar.getInstance().getTimeInMillis();
	}


	public String getCurrentTimeAsString() {
		if (getUtcFixTime() > 0) {
			return String.valueOf(getCurrentTime()) + _milliseconds;
		} else {
			return _not_available;
		}
	}

	public String getGPSEvent() {
		return mGPSEvent;
	}


	public double getLatitude() {
		if (this.location != null) {
			return location.getLatitude();
		} else {
			return 0;
		}
	}



	public String getLatitudeAsString() {
		if (this.location != null) {
			return String.valueOf(location.getLatitude()) + _degrees;
		} else {
			return _not_available;
		}
	}

	public Location getLocation() {
		return location;
	}

	public double getLongitude() {
		if (this.location != null) {
			return location.getLongitude();
		} else {
			return 0;
		}
	}
	
	public String getLongitudeAsString() {
		if (this.location != null) {
			return String.valueOf(location.getLongitude() + _degrees);
		} else {
			return _not_available;
		}
	}


	public int getMaxSatellites() {
		return getSatellitesInFix();
	}

	public String getNmea() {
		return nmeaBuffer.toString();
	}

	public String getProvider() {
		if (this.location != null) {
			return location.getProvider();
		} else {
			return _not_available;
		}
	}

	public List<GpsSatellite> getSatellites() {
		return satellites;
	}

	public int getSatellitesSize() {
		if (satellites != null ) {
			return satellites.size();
		} else {
			return  0;
		}
	}
	
	public float getSpeed() {
		if (this.location != null) {
			return location.getSpeed();
		} else {
			return 0;
		}
	}

	public String getSpeedAsString() {
		if (this.location != null) {
			return String.valueOf(location.getSpeed() + _meters_seconds);
		} else {
			return _not_available;
		}
	}


	public String getTimeSinceLastFixAsString() {
		if (this.location != null && getUtcFixTime() > 0) {
			return String.valueOf(getCurrentTime() - getUtcFixTime()) + _milliseconds;
		} else {
			return _not_available;
		}
	}

	public long getUtcFixTime() {
		if (this.location != null) {
			return location.getTime();
		} else {
			return 0;
		}
	}
	
	public String getUtcFixTimeAsString() {
		if (this.location != null) {
			return String.valueOf(location.getTime() + _milliseconds);
		} else {
			return _not_available;
		}
	}


	public void setGPSEvent(String mGPSEvent) {
		this.mGPSEvent = mGPSEvent;
	}


	public void setLocation(Location location) {
		if (location != null) {
			this.location = location;
		} else {
			Logger.e(TAG, "Null Location");
		}
	}

	public void setSatellites(List<GpsSatellite> satellites) {
		this.satellites = satellites;
	}

	public int getSatellitesInFix() {
		return satellitesInFix;
	}

	public void setSatellitesInFix(int satellitesInFix) {
		this.satellitesInFix = satellitesInFix;
	}
}
