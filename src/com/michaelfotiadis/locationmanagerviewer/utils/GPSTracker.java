package com.michaelfotiadis.locationmanagerviewer.utils;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
/**
 * 
 * @author Ravi Tamada, modified by Michael Fotiadis
 *
 */
public class GPSTracker extends Service implements LocationListener, NmeaListener {

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
	private static final long MIN_TIME_BW_UPDATES = 500; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	private String mNMEA = "";

	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}


	public double getAltitude() {
		if(location != null){
			return location.getAltitude();
		} else {
			return 0;
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			return location.getLatitude();
		} else {
			return 0;
		}
	}

	public Location getLocation() {
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
				// no network provider is enabled
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
					this.canGetLocation = true;
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							locationManager.addNmeaListener(this);
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

	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			return location.getLongitude();
		} else {
			return 0;
		}
	}

	/**
	 * Function to get Speed
	 * */
	public double getSpeed(){
		if(location != null){
			return location.getSpeed();
		} else {
			return 0;
		}
	}

	/**
	 * Function to get Accuracy
	 * */
	public double getAccuracy(){
		if(location != null){
			return location.getAccuracy();
		} else {
			return 0;
		}
	}

	/**
	 * Function to get Time
	 * */
	public double getTime(){
		if(location != null){
			return location.getTime();
		} else {
			return 0;
		}
	}

	/**
	 * Function to get Bearing
	 * */
	public double getBearing(){
		if(location != null){
			return location.getBearing();
		} else {
			return 0;
		}
	}

	public String getNMEA() {
		String sendNMEA = mNMEA;
		mNMEA = "";
		return sendNMEA;
	}

	public boolean isGPSEnabled() {
		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);
		return locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean isNetworkEnabled() {
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
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		mNMEA += System.getProperty("line.separator");
		mNMEA += nmea;
	}

	@Override
	public void onProviderDisabled(String provider) {
		showSettingsAlert();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Logger.d(TAG, "GPS Provider Enabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will launch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}       
	}

}