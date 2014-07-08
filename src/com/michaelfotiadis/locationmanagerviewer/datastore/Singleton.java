package com.michaelfotiadis.locationmanagerviewer.datastore;

import static android.provider.Settings.System.AIRPLANE_MODE_ON;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.michaelfotiadis.locationmanagerviewer.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.containers.MyGPSData;
import com.michaelfotiadis.locationmanagerviewer.containers.MyNetworkStatus;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

/**
 * Singleton Class
 * 
 * @since 07/07/2014
 * @author Michael Fotiadis
 * 
 */
public class Singleton implements LocationListener, NmeaListener, GpsStatus.Listener {

	private static volatile Singleton _instance = null;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 500;

	/**
	 * 
	 * @return Instance of the Singleton
	 */
	public static Singleton getInstance() {
		if (_instance == null) {
			synchronized (Singleton.class) {
				if (_instance == null) {
					_instance = new Singleton();
				}
			}
		}
		return _instance;
	}

	/**
	 * 
	 * @param instance
	 *            Sets the Instance of the Singleton
	 */
	public static void setInstance(Singleton instance) {
		Singleton._instance = instance;
	}

	private final String TAG = "Singleton";

	// **** Application Fields
	private Context mContext;

	// **** Location Manager Fields
	private LocationManager mLocationManager;

	// **** GPS Fields
	private MyGPSData mGPSData;

	// **** Network Fields
	private MyNetworkStatus mNetworkStatus;

	/**
	 * Singleton Constructor
	 */
	public Singleton() {
		setGPSData(new MyGPSData());
		setNetworkStatus(new MyNetworkStatus());
	}

	/**
	 * 
	 * @return Stored context
	 */
	public Context getContext() {
		return mContext;
	}

	public MyGPSData getGPSData() {
		return mGPSData;
	}

	public MyNetworkStatus getNetworkStatus() {
		return mNetworkStatus;
	}

	/**
	 * Modifies GPSData object and broadcasts the change
	 */
	public void notifyGPSDataChanged() {
		if (mLocationManager != null) {
			// Set the GPS Location according to the GPS Provider
			mGPSData.setLocation(mLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			// Broadcast that data has changed
			Intent broadcastIntent = new Intent(MyConstants.Broadcasts.BROADCAST_2.getString());
			Singleton.getInstance().getContext().sendBroadcast(broadcastIntent);
		}
	}

	/**
	 * Modifies Network Status object and broadcasts the change
	 */
	public void notifyNetworkStateChanged() {
		if (mLocationManager != null) {
			mNetworkStatus.setGPSEnabled(mLocationManager.
					isProviderEnabled(LocationManager.GPS_PROVIDER));

			if (isAirplaneModeOn(mContext)) {
				mNetworkStatus.setCellNetworkEnabled(false);
			} else {
				mNetworkStatus.setCellNetworkEnabled(mLocationManager.
						isProviderEnabled(LocationManager.NETWORK_PROVIDER));
			}
			// Broadcast that data has changed
			Intent broadcastIntent = new Intent(MyConstants.Broadcasts.BROADCAST_1.getString());
			Logger.i(TAG, "Broadcasting Network State Changed");
			mContext.sendBroadcast(broadcastIntent);
		}
	}

	/**
	 * Broadcasts that the NMEA buffer has changed
	 */
	public void notifyNMEAChanged() {
		// Broadcast that data has changed
		Intent broadcastIntent = new Intent(MyConstants.Broadcasts.BROADCAST_3.getString());
		Singleton.getInstance().getContext().sendBroadcast(broadcastIntent);
	}

	@Override
	public void onGpsStatusChanged(int event) {
		GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			mGPSData.setGPSEvent("GPS Started");
			break;

		case GpsStatus.GPS_EVENT_STOPPED:
			mGPSData.setGPSEvent("GPS Stopped");
			break;

		case GpsStatus.GPS_EVENT_FIRST_FIX:
			mGPSData.setGPSEvent("GPS First Fix");
			break;

		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			mGPSData.setGPSEvent("Satellite Detected");
			break;
		}
		mGPSData.setSatellites(gpsStatus.getSatellites());
		mGPSData.setMaxSatellites(gpsStatus.getMaxSatellites());
		notifyGPSDataChanged();
	}

	@Override
	public void onLocationChanged(Location location) {
		notifyGPSDataChanged();
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		mGPSData.appendToNmea(nmea);
		notifyNMEAChanged();
	}

	@Override
	public void onProviderDisabled(String provider) {
		notifyNetworkStateChanged();
	}

	@Override
	public void onProviderEnabled(String provider) {
		notifyNetworkStateChanged();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		notifyNetworkStateChanged();
	}

	/**
	 * Notifies the location manager to request updates using predefined parameters
	 */
	public void requestGPSLocationUpdates() {
		Logger.d(TAG, "Requesting GPS Updates with time between updates " + MIN_TIME_BW_UPDATES 
				+ " and min distance change for updates " + MIN_DISTANCE_CHANGE_FOR_UPDATES);
		mLocationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MIN_TIME_BW_UPDATES,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		Logger.d(TAG, "GPS Enabled");
	}

	public void requestNetworkUpdate() {
		Logger.d(TAG, "Requesting Network Status Update");
		if (mLocationManager != null) {
			notifyNetworkStateChanged();
		} else {
			mLocationManager = (LocationManager) mContext	.getSystemService(Context.LOCATION_SERVICE);
			notifyNetworkStateChanged();
		}
	}

	public void setContext(Context mContext) {
		Logger.d(TAG, "Setting Singleton Context " + mContext.getApplicationContext().getPackageName());
		this.mContext = mContext.getApplicationContext();

	} 

	public void setGPSData(MyGPSData gpsData) {
		this.mGPSData = gpsData;
	}

	public void setNetworkStatus(MyNetworkStatus networkStatus) {
		this.mNetworkStatus = networkStatus;
	}

	public void startCollectingGPSData() {
		Logger.d(TAG, "Attempting to Start GPS");
		// Initialise the location manager for GPS collection
		mLocationManager = (LocationManager) mContext	.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.addGpsStatusListener(this);
		mLocationManager.addNmeaListener(this);

		requestGPSLocationUpdates();
		requestNetworkUpdate();
	}

	public void stopCollectingGPSData() {
		Logger.d(TAG, "Attempting to Stop GPS");
		if(mLocationManager != null) {
			mLocationManager.removeUpdates(this);
			mLocationManager.removeGpsStatusListener(this);
			mLocationManager.removeNmeaListener(this);
		}
	}

	/**
	 * Gets the state of Airplane Mode.
	 * 
	 * @param context
	 * @return true if enabled.
	 */
	@SuppressWarnings("deprecation")
	public static boolean isAirplaneModeOn(Context context) {
		 ContentResolver contentResolver = context.getContentResolver();
		  return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
	}



}
