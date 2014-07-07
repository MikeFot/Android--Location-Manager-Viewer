package com.michaelfotiadis.locationmanagerviewer.datastore;

import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.michaelfotiadis.locationmanagerviewer.containers.CustomConstants;
import com.michaelfotiadis.locationmanagerviewer.containers.MyGPSData;
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

	/**
	 * Singleton Constructor
	 */
	public Singleton() {

		mGPSData = new MyGPSData();
	}

	public boolean isGPSLocationSupported() {
		if (isCellNetworkEnabled() && isGPSEnabled()) {
			return true;
		}  else {
			return false;
		}
	}

	public Context getContext() {
		return mContext;
	}

	public MyGPSData getGPSData() {
		return mGPSData;
	}

	public boolean isCellNetworkEnabled() {
		return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	public boolean isGPSEnabled() {
		return  mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public void setContext(Context mContext) {
		Logger.d(TAG, "Setting Singleton Context " + mContext.getApplicationContext().getPackageName());
		this.mContext = mContext.getApplicationContext();

	}

	public void setGPSData(MyGPSData gpsData) {
		this.mGPSData = gpsData;
	}

	public void startCollectingGPSData() {
		mLocationManager = (LocationManager) mContext	.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.addGpsStatusListener(this);

		requestGPSLocationUpdates();
	}

	public void stopCollectingGPSData() {
		Logger.d(TAG, "Attempting to Stop GPS");
		if(mLocationManager != null){
			mLocationManager.removeUpdates(this);
		}
	}

	public void notifyNetworkDataChanged() {
		// Broadcast that data has changed
		Intent broadcastIntent = new Intent(CustomConstants.Broadcasts.BROADCAST_1.getString());
		Logger.i(TAG, "Broadcasting Scanning Status Started");
		mContext.sendBroadcast(broadcastIntent);
	}

	@Override
	public void onGpsStatusChanged(int event) {
		Logger.d(TAG, "onGPSStatusChanged");
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


		if(gpsStatus != null) {
			mGPSData.setSatellites(gpsStatus.getSatellites());
			mGPSData.setMaxSatellites(gpsStatus.getMaxSatellites());
		}
		notifyGPSDataChanged();
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {

		// Set the GPS Location according to the GPS Provider
		mGPSData.setLocation( mLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		notifyGPSDataChanged();
	}

	public void notifyGPSDataChanged() {
		// Broadcast that data has changed
		Intent broadcastIntent = new Intent(CustomConstants.Broadcasts.BROADCAST_2.getString());
		Logger.i(TAG, "Broadcasting Scanning Status Started");
		Singleton.getInstance().getContext().sendBroadcast(broadcastIntent);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		notifyNetworkDataChanged();
	}

	@Override
	public void onProviderEnabled(String provider) {
		notifyNetworkDataChanged();
	}

	@Override
	public void onProviderDisabled(String provider) {
		notifyNetworkDataChanged();
	}

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; 

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 500;

	public void requestGPSLocationUpdates() {
		mLocationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MIN_TIME_BW_UPDATES,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		Logger.d(TAG, "GPS Enabled");
	}
}
