package com.michaelfotiadis.locationmanagerviewer.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.GpsSatellite;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;

/**
 * Class for adding predefined views to a Merge Adapter
 * @author Michael Fotiadis
 *
 */
public class MergeAdapterBuilder {

	private final String TAG = "Merge Adapter Builder";
	private final Activity mActivity;
	private final MergeAdapter mAdapter;
	
	public MergeAdapterBuilder (Activity activity) {
		mActivity = activity;
		mAdapter = new MergeAdapter();
	}

	/**
	 * Append a header to the MergeAdapter
	 * @param layoutInflater Layout Inflater
	 * @param adapter Merge Adapter
	 * @param data String data
	 */
	@SuppressLint("InflateParams")
	public void appendHeader(final String title){
		
		final LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_view_header, null);
		if (layout == null) {
			Logger.e(TAG, "NULL LAYOUT");
			return;
		} 
		final TextView tvTitle = (TextView) layout.findViewById(R.id.title);
		tvTitle.setText(title);
		mAdapter.addView(layout);
	}

	/**
	 * Append Body Text to the Merge Adapter
	 * @param layoutInflater Layout Inflater
	 * @param adapter Merge Adapter
	 * @param data String data
	 */
	@SuppressLint("InflateParams")
	public void appendSimpleText(final String data){
		final LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_view_textview, null);
		if (layout == null) {
			Logger.e(TAG, "NULL LAYOUT");
			return;
		} 

		final TextView tvData = (TextView) layout.findViewById(R.id.data);
		tvData.setText(data);
		mAdapter.addView(layout);
	}

	public ListAdapter generateSatelliteAdapter() {
		
		appendHeader(mActivity.getString(R.string.header_satellite_list));
		
		List<GpsSatellite> satellites = Singleton.getInstance().getGPSData().getSatellites();
		
		if (satellites == null || satellites.size() == 0) {
			appendSimpleText(mActivity.getString(R.string.info_satellite_used));
			return mAdapter;
		}
		
		int i = 0;
		for (GpsSatellite satellite : satellites) {
			i++;
			appendHeader(mActivity.getString(R.string.header_satellite) + i);
			appendSimpleText(mActivity.getString(R.string.info_satellite_prn)  
					+ satellite.getPrn());
			appendSimpleText(mActivity.getString(R.string.info_satellite_used)  
					+ satellite.usedInFix());
			appendSimpleText(mActivity.getString(R.string.info_satellite_azimuth)  
					+ satellite.getAzimuth() + mActivity.getString(R.string.info_degrees));
			appendSimpleText(mActivity.getString(R.string.info_satellite_elevation)  
					+ satellite.getElevation() + mActivity.getString(R.string.info_degrees));
			appendSimpleText(mActivity.getString(R.string.info_satellite_snr)  
					+ satellite.getSnr());
			appendSimpleText(mActivity.getString(R.string.info_satellite_almanac)  
					+ satellite.hasAlmanac());
			appendSimpleText(mActivity.getString(R.string.info_satellite_ephemeris)  
					+ satellite.hasEphemeris());
		}
		
		return mAdapter;
	}

	public ListAdapter generateNMEAAdapter() {
		appendHeader(mActivity.getString(R.string.header_nmea));
		appendSimpleText(Singleton.getInstance().getGPSData().getNmea());
		
		return mAdapter;
	}
	
	public ListAdapter generateGPSAdapter() {

		appendHeader(mActivity.getString(R.string.header_network_status));
		appendSimpleText(mActivity.getString(R.string.info_gps_adapter)  
				+ Singleton.getInstance().getNetworkStatus().isGPSEnabledAsString());
		appendSimpleText(mActivity.getString(R.string.info_provider)  
				+ Singleton.getInstance().getGPSData().getProvider());

		appendHeader(mActivity.getString(R.string.header_location));
		appendSimpleText(mActivity.getString(R.string.info_latitude)  
				+ Singleton.getInstance().getGPSData().getLatitudeAsString());
		appendSimpleText(mActivity.getString(R.string.info_longitude)   
				+ Singleton.getInstance().getGPSData().getLongitudeAsString());
		appendSimpleText(mActivity.getString(R.string.info_altitude)   
				+ Singleton.getInstance().getGPSData().getAltitudeAsString());

		appendHeader(mActivity.getString(R.string.header_details));
		appendSimpleText(mActivity.getString(R.string.info_accuracy)  
				+ Singleton.getInstance().getGPSData().getAccuracyAsString());
		appendSimpleText(mActivity.getString(R.string.info_bearing)  
				+ Singleton.getInstance().getGPSData().getBearingAsString());
		appendSimpleText(mActivity.getString(R.string.info_speed)   
				+ Singleton.getInstance().getGPSData().getSpeedAsString());


		appendHeader(mActivity.getString(R.string.header_other));
		appendSimpleText(mActivity.getString(R.string.info_utc_time)
				+ Singleton.getInstance().getGPSData().getUtcFixTimeAsString());
		appendSimpleText(mActivity.getString(R.string.info_event)  
				+ Singleton.getInstance().getGPSData().getGPSEvent());
		appendSimpleText(mActivity.getString(R.string.info_satellites_total)  
				+ Singleton.getInstance().getGPSData().getSatellitesSize());
		appendSimpleText(mActivity.getString(R.string.info_satellites_in_fix)  
				+ Singleton.getInstance().getGPSData().getSatellitesInFix());
		
		return mAdapter;
	}

	public ListAdapter generateNetworkAdapter() {

		appendHeader(mActivity.getString(R.string.header_network_status));
		appendSimpleText(mActivity.getString(R.string.info_network_adapter)   
				+ Singleton.getInstance().getNetworkStatus().isCellNetworkEnabledAsString());
		appendSimpleText(mActivity.getString(R.string.info_provider)  
				+ Singleton.getInstance().getNetworkData().getProvider());

		appendHeader(mActivity.getString(R.string.header_location));
		appendSimpleText(mActivity.getString(R.string.info_latitude)  
				+ Singleton.getInstance().getNetworkData().getLatitudeAsString());
		appendSimpleText(mActivity.getString(R.string.info_longitude)  
				+ Singleton.getInstance().getNetworkData().getLongitudeAsString());
		appendSimpleText(mActivity.getString(R.string.info_altitude)  
				+ Singleton.getInstance().getNetworkData().getAltitudeAsString());

		appendHeader(mActivity.getString(R.string.header_details));
		appendSimpleText(mActivity.getString(R.string.info_accuracy)  
				+ Singleton.getInstance().getNetworkData().getAccuracyAsString());
		appendSimpleText(mActivity.getString(R.string.info_bearing)  
				+ Singleton.getInstance().getNetworkData().getBearingAsString());
		appendSimpleText(mActivity.getString(R.string.info_speed)  
				+ Singleton.getInstance().getNetworkData().getSpeedAsString());

		appendHeader(mActivity.getString(R.string.header_other));
		appendSimpleText(mActivity.getString(R.string.info_utc_time)  
				+ Singleton.getInstance().getNetworkData().getUtcFixTimeAsString());

		return mAdapter;
	}

	public ListAdapter generatePassiveAdapter() {

		appendHeader(mActivity.getString(R.string.header_network_status));
		appendSimpleText(mActivity.getString(R.string.info_network_adapter)  
				+ Singleton.getInstance().getNetworkStatus().isCellNetworkEnabledAsString());
		appendSimpleText(mActivity.getString(R.string.info_gps_adapter)  
				+ Singleton.getInstance().getNetworkStatus().isGPSEnabledAsString());
		appendSimpleText(mActivity.getString(R.string.info_provider)  
				+ Singleton.getInstance().getPassiveData().getProvider());

		appendHeader(mActivity.getString(R.string.header_location));
		appendSimpleText(mActivity.getString(R.string.info_latitude)  
				+ Singleton.getInstance().getPassiveData().getLatitudeAsString());
		appendSimpleText(mActivity.getString(R.string.info_longitude)  
				+ Singleton.getInstance().getPassiveData().getLongitudeAsString());
		appendSimpleText(mActivity.getString(R.string.info_altitude)  
				+ Singleton.getInstance().getPassiveData().getAltitudeAsString());

		appendHeader(mActivity.getString(R.string.header_details));
		appendSimpleText(mActivity.getString(R.string.info_accuracy)   
				+ Singleton.getInstance().getPassiveData().getAccuracyAsString());
		appendSimpleText(mActivity.getString(R.string.info_bearing)  
				+ Singleton.getInstance().getPassiveData().getBearingAsString());
		appendSimpleText(mActivity.getString(R.string.info_speed)  
				+ Singleton.getInstance().getPassiveData().getSpeedAsString());

		appendHeader(mActivity.getString(R.string.header_other));
		appendSimpleText(mActivity.getString(R.string.info_utc_time)  
				+ Singleton.getInstance().getPassiveData().getUtcFixTimeAsString());

		return mAdapter;
	}
	
}
