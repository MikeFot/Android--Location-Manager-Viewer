package com.michaelfotiadis.locationmanagerviewer.containers;

import android.location.GpsSatellite;
import android.location.Location;

import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class MyGPSData {

	private Location location;
	private  Iterable<GpsSatellite>satellites;
	private int maxSatellites = 0;

	private final String TAG = "GPS Data Object";


	private final String NOT_AVAILABLE = "N/A";


	public MyGPSData() {

	}


	public MyGPSData(Location location) {
		setLocation(location);
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
			return String.valueOf(location.getAccuracy() + " metres (68% confidence)");
		} else {
			return NOT_AVAILABLE;
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
			return String.valueOf(location.getAltitude() + " metres");
		} else {
			return NOT_AVAILABLE;
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
			return String.valueOf(location.getBearing() + " degrees");
		} else {
			return NOT_AVAILABLE;
		}
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
			return String.valueOf(location.getLatitude() + " degrees");
		} else {
			return NOT_AVAILABLE;
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
			return String.valueOf(location.getLongitude() + " degrees");
		} else {
			return NOT_AVAILABLE;
		}
	}


	public int getMaxSatellites() {
		return maxSatellites;
	}

	public String getProvider() {
		if (this.location != null) {
			return location.getProvider();
		} else {
			return "N/A";
		}
	}


	public Iterable<GpsSatellite> getSatellites() {
		return satellites;
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
			return String.valueOf(location.getSpeed() + " metres/seconds");
		} else {
			return NOT_AVAILABLE;
		}
	}

	private String mGPSEvent = "";
	
	
	public long getUtcFixTime() {
		if (this.location != null) {
			return location.getTime();
		} else {
			return 0;
		}
	}

	public String getUtcFixTimeAsString() {
		if (this.location != null) {
			return String.valueOf(location.getTime() + " milliseconds");
		} else {
			return NOT_AVAILABLE;
		}
	}

	public void setLocation(Location location) {
		if (location != null) {
			Logger.d(TAG, "Setting Location");
			this.location = location;
		} else {
			Logger.d(TAG, "Null Location");
		}
	}


	public void setMaxSatellites(int maxSatellites) {
		this.maxSatellites = maxSatellites;
	}

	public void setSatellites(Iterable<GpsSatellite> satellites) {
		this.satellites = satellites;
	}


	public String getGPSEvent() {
		return mGPSEvent;
	}


	public void setGPSEvent(String mGPSEvent) {
		this.mGPSEvent = mGPSEvent;
	}
}
