package com.michaelfotiadis.locationmanagerviewer.utils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;
/**
 * 
 * @author Michael Fotiadis
 *
 */
public class GPSTracker extends Service implements LocationListener, NmeaListener, Listener  {

	private final String TAG = "GPSTracker";

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 500;

	// Declaring a Location Manager
	protected LocationManager locationManager;

	private String mNMEA = "";

	public GPSTracker(Context context) {
		this.mContext = context;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}


	public Location startUsingGPS() {
		Logger.d(TAG, "Starting GPS");
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Logger.d(TAG, "No Network Provider is enabled");
			} else {

				// First get location from Network Provider
				//                if (isNetworkEnabled) {
				//                    locationManager.requestLocationUpdates(
				//                            LocationManager.NETWORK_PROVIDER,
				//                            MIN_TIME_BW_UPDATES,
				//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				//                    Log.d("Network", "Network");
				//                    if (locationManager != null) {
				//                        location = locationManager
				//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				//                    }
				//                }
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					Logger.d(TAG, "GPS is enabled");
					this.canGetLocation = true;
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Logger.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							locationManager.addNmeaListener(this);
							// Set the location 
							Singleton.getInstance().getGPSData().setLocation(location);
						}
					}
				} else {
					this.canGetLocation = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}


	public boolean isGPSEnabled() {
		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);
		return locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean isCellNetworkEnabled() {
		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		Logger.d(TAG, "onLocationChanged");
		Singleton.getInstance().getGPSData().setLocation(location);
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		mNMEA += System.getProperty("line.separator");
		mNMEA += nmea;
	}

	@Override
	public void onProviderDisabled(String provider) {
		Logger.d(TAG, "onProviderDisabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Logger.d(TAG, "onProviderEnabled");
		Logger.d(TAG, "GPS Provider Enabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Logger.d(TAG, "onSatusChanged");
	}


	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		Logger.d(TAG, "Attempting to Stop GPS");
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}       
	}

	@Override
	public void onGpsStatusChanged(int event) {
		Logger.d(TAG, "onGPSStatusChanged");
		 GpsStatus gpsStatus = locationManager.getGpsStatus(null);
	        if(gpsStatus != null) {
	            Singleton.getInstance().getGPSData().setSatellites(gpsStatus.getSatellites());
	            Singleton.getInstance().getGPSData().setMaxSatellites(gpsStatus.getMaxSatellites());
	        }
	}

}