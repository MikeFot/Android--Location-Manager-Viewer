package com.michaelfotiadis.locationmanagerviewer.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class FragmentMergeAdapter extends ListFragment {

	/**
	 * Custom receiver for Network and GPS changes
	 * @author Michael Fotiadis
	 *
	 */
	private class ResponseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_1.getString())) {
				populateGPSMergeAdapter();
			} else if  (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_2.getString())) {
				populateGPSMergeAdapter();
			} else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_3.getString())) {
				populateNMEAMergeAdapter();
			}  else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_4.getString())) {
				populateNetworkMergeAdapter();
			}    else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_5.getString())) {
				populatePassiveMergeAdapter();
			}
		}
	}

	private static final String ARG_POSITION = "position";

	public static FragmentMergeAdapter newInstance(int position) {
		FragmentMergeAdapter f = new FragmentMergeAdapter();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);

		return f;
	}

	private final String TAG = "Fragment Merge Adapter";
	private ResponseReceiver mResponseReceiver;

	/**
	 * Append a header to the MergeAdapter
	 * @param inflater 
	 * @param adapter
	 * @param title
	 */
	@SuppressLint("InflateParams")
	private void appendHeader(MergeAdapter adapter, String title){
		final LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.list_item_view_header, null);
		if (layout == null) {
			Logger.e(TAG, "NULL LAYOUT");
			return;
		} 
		final TextView tvTitle = (TextView) layout.findViewById(R.id.title);
		tvTitle.setText(title);
		adapter.addView(layout);
	}

	/**
	 * Append body text to the MergeAdapter
	 * @param adapter
	 * @param data
	 */
	@SuppressLint("InflateParams")
	private void appendSimpleText(MergeAdapter adapter, String data){
		final LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.list_item_view_textview, null);
		if (layout == null) {
			Logger.e(TAG, "NULL LAYOUT");
			return;
		} 

		final TextView tvData = (TextView) layout.findViewById(R.id.data);
		tvData.setText(data);
		adapter.addView(layout);
	}

	public int getConstructorArguments() {
		return getArguments().getInt(ARG_POSITION);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, "CREATED FRAGMENT WITH " + getConstructorArguments());

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_merge_adapter, container, false);
	}

	@Override
	public void onPause() {
		Logger.d(TAG, "onPause");

		unregisterResponseReceivers();
		super.onPause();
	}

	@Override
	public void onResume() {
		Logger.d(TAG, "onResume");
		registerResponseReceiver();

		switchConstructorArguments();

		Singleton.getInstance().requestNetworkUpdate();
		super.onResume();
	}

	/**
	 * Adds data to the Merge Adapter
	 */
	private void populateGPSMergeAdapter() {

		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, getString(R.string.header_network_status));
		appendSimpleText(adapter, getString(R.string.info_gps_adapter)  
				+ Singleton.getInstance().getNetworkStatus().isGPSEnabledAsString());
		appendSimpleText(adapter, getString(R.string.info_provider)  
				+ Singleton.getInstance().getGPSData().getProvider());

		appendHeader(adapter,getString(R.string.header_location));
		appendSimpleText(adapter, getString(R.string.info_latitude)  
				+ Singleton.getInstance().getGPSData().getLatitudeAsString());
		appendSimpleText(adapter, getString(R.string.info_longitude)   
				+ Singleton.getInstance().getGPSData().getLongitudeAsString());
		appendSimpleText(adapter, getString(R.string.info_altitude)   
				+ Singleton.getInstance().getGPSData().getAltitudeAsString());

		appendHeader(adapter,getString(R.string.header_details));
		appendSimpleText(adapter, getString(R.string.info_accuracy)  
				+ Singleton.getInstance().getGPSData().getAccuracyAsString());
		appendSimpleText(adapter, getString(R.string.info_bearing)  
				+ Singleton.getInstance().getGPSData().getBearingAsString());
		appendSimpleText(adapter, getString(R.string.info_speed)   
				+ Singleton.getInstance().getGPSData().getSpeedAsString());


		appendHeader(adapter, getString(R.string.header_other));
		appendSimpleText(adapter,getString(R.string.info_event)  
				+ Singleton.getInstance().getGPSData().getGPSEvent());
		appendSimpleText(adapter, getString(R.string.info_utc_time)
				+ Singleton.getInstance().getGPSData().getUtcFixTimeAsString());

		getListView().setAdapter(adapter);
	}

	/**
	 * Adds data to the Merge Adapter
	 */
	private void populateNetworkMergeAdapter() {

		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, getString(R.string.header_network_status));
		appendSimpleText(adapter, getString(R.string.info_network_adapter)   
				+ Singleton.getInstance().getNetworkStatus().isCellNetworkEnabledAsString());
		appendSimpleText(adapter, getString(R.string.info_provider)  
				+ Singleton.getInstance().getNetworkData().getProvider());

		appendHeader(adapter, getString(R.string.header_location));
		appendSimpleText(adapter, getString(R.string.info_latitude)  
				+ Singleton.getInstance().getNetworkData().getLatitudeAsString());
		appendSimpleText(adapter, getString(R.string.info_longitude)  
				+ Singleton.getInstance().getNetworkData().getLongitudeAsString());
		appendSimpleText(adapter, getString(R.string.info_altitude)  
				+ Singleton.getInstance().getNetworkData().getAltitudeAsString());

		appendHeader(adapter, getString(R.string.header_details));
		appendSimpleText(adapter, getString(R.string.info_accuracy)  
				+ Singleton.getInstance().getNetworkData().getAccuracyAsString());
		appendSimpleText(adapter, getString(R.string.info_bearing)  
				+ Singleton.getInstance().getNetworkData().getBearingAsString());
		appendSimpleText(adapter, getString(R.string.info_speed)  
				+ Singleton.getInstance().getNetworkData().getSpeedAsString());

		appendHeader(adapter, getString(R.string.header_other));
		appendSimpleText(adapter, getString(R.string.info_utc_time)  
				+ Singleton.getInstance().getNetworkData().getUtcFixTimeAsString());

		getListView().setAdapter(adapter);
	}

	/**
	 * Adds data to the Merge Adapter
	 */
	private void populateNMEAMergeAdapter() {

		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, getString(R.string.header_nmea));
		appendSimpleText(adapter, Singleton.getInstance().getGPSData().getNmea());

		getListView().setAdapter(adapter);
	}

	/**
	 * Adds data to the Merge Adapter
	 */
	private void populatePassiveMergeAdapter() {

		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, getString(R.string.header_network_status));
		appendSimpleText(adapter, getString(R.string.info_network_adapter)  
				+ Singleton.getInstance().getNetworkStatus().isCellNetworkEnabledAsString());
		appendSimpleText(adapter, getString(R.string.info_gps_adapter)  
				+ Singleton.getInstance().getNetworkStatus().isGPSEnabledAsString());
		appendSimpleText(adapter, getString(R.string.info_provider)  
				+ Singleton.getInstance().getPassiveData().getProvider());

		appendHeader(adapter, getString(R.string.header_location));
		appendSimpleText(adapter, getString(R.string.info_latitude)  
				+ Singleton.getInstance().getPassiveData().getLatitudeAsString());
		appendSimpleText(adapter, getString(R.string.info_longitude)  
				+ Singleton.getInstance().getPassiveData().getLongitudeAsString());
		appendSimpleText(adapter, getString(R.string.info_altitude)  
				+ Singleton.getInstance().getPassiveData().getAltitudeAsString());

		appendHeader(adapter, getString(R.string.header_details));
		appendSimpleText(adapter, getString(R.string.info_accuracy)   
				+ Singleton.getInstance().getPassiveData().getAccuracyAsString());
		appendSimpleText(adapter, getString(R.string.info_bearing)  
				+ Singleton.getInstance().getPassiveData().getBearingAsString());
		appendSimpleText(adapter, getString(R.string.info_speed)  
				+ Singleton.getInstance().getPassiveData().getSpeedAsString());

		appendHeader(adapter, getString(R.string.header_other));
		appendSimpleText(adapter, getString(R.string.info_utc_time)  
				+ Singleton.getInstance().getPassiveData().getUtcFixTimeAsString());

		getListView().setAdapter(adapter);
	}

	/**
	 * Registers a response receiver waiting for network change broadcasts or GPS broadcasts
	 */
	private void registerResponseReceiver() {
		Logger.d(TAG, "Registering Response Receiver");
		IntentFilter intentFilter = new IntentFilter();
		if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_1.getCode()) {
			// GPS broadcast
			intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_1.getString());
			intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_2.getString());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_2.getCode()) {
			// NMEA broadcast
			intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_3.getString());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_3.getCode()) {
			// Network broadcast
			intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_4.getString());
		}else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_4.getCode()) {
			// Passive broadcast
			intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_5.getString());
		}

		mResponseReceiver = new ResponseReceiver();
		getActivity().registerReceiver(mResponseReceiver, intentFilter);
	}

	public void switchConstructorArguments() {
		if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_1.getCode()) {
			populateGPSMergeAdapter();
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_2.getCode()) {
			populateNMEAMergeAdapter();
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_3.getCode()) {
			populateNetworkMergeAdapter();
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_4.getCode()) {
			populatePassiveMergeAdapter();
		} else {
			// Do Nothing
		}
	}

	/**
	 * Unregisters the response receiver
	 */
	private void unregisterResponseReceivers() {
		try {
			getActivity().unregisterReceiver(mResponseReceiver);
			Logger.d(TAG, "Response Receiver Unregistered Successfully");
		} catch (Exception e) {
			Logger.d(
					TAG,
					"Response Receiver Already Unregistered. Exception : "
							+ e.getLocalizedMessage());
		}
	}
}
