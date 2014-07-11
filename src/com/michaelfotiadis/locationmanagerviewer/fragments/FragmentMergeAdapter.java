package com.michaelfotiadis.locationmanagerviewer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;
import com.michaelfotiadis.locationmanagerviewer.utils.MergeAdapterBuilder;

public class FragmentMergeAdapter extends ListFragment {

	/**
	 * Custom receiver for Network and GPS changes
	 * 
	 * @author Michael Fotiadis
	 * 
	 */
	private class ResponseReceiver extends BroadcastReceiver {
		@Override
		/*All of the cases are going to the same method but it is at least expandable 
		 in case I want to add more functionality*/
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_NETWORK_STATE_CHANGED
					.getString())) {
				populateMergeAdapterByConstructorArguments();
			} else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_GPS_CHANGED.getString())) {
				populateMergeAdapterByConstructorArguments();
			} else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_GPS_STATE_CHANGED
					.getString())) {
				populateMergeAdapterByConstructorArguments();
			} else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_NMEA_CHANGED.getString())) {
				populateMergeAdapterByConstructorArguments();
			} else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_NETWORK_CHANGED
					.getString())) {
				populateMergeAdapterByConstructorArguments();
			} else if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_PASSIVE_CHANGED
					.getString())) {
				populateMergeAdapterByConstructorArguments();
			} else {
				// Do nothing
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

		return inflater.inflate(R.layout.fragment_merge_adapter, container,
				false);
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

		populateMergeAdapterByConstructorArguments();

		Singleton.getInstance().requestNetworkUpdate();
		super.onResume();
	}

	/**
	 * Registers a response receiver waiting for network change broadcasts or
	 * GPS broadcasts
	 */
	private void registerResponseReceiver() {
		Logger.d(TAG, "Registering Response Receiver");
		IntentFilter intentFilter = new IntentFilter();
		if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_GPS
				.getCode()) {
			// GPS broadcast
			intentFilter
			.addAction(MyConstants.Broadcasts.BROADCAST_NETWORK_STATE_CHANGED
					.getString());
			intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_GPS_CHANGED
					.getString());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_SATELLITES
				.getCode()) {
			// NMEA broadcast
			intentFilter
			.addAction(MyConstants.Broadcasts.BROADCAST_GPS_STATE_CHANGED
					.getString());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_NETWORK
				.getCode()) {
			// Network broadcast
			intentFilter
			.addAction(MyConstants.Broadcasts.BROADCAST_NETWORK_CHANGED
					.getString());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_PASSIVE
				.getCode()) {
			// Passive broadcast
			intentFilter
			.addAction(MyConstants.Broadcasts.BROADCAST_PASSIVE_CHANGED
					.getString());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_NMEA
				.getCode()) {
			// NMEA broadcast
			intentFilter
			.addAction(MyConstants.Broadcasts.BROADCAST_NMEA_CHANGED
					.getString());
		}

		mResponseReceiver = new ResponseReceiver();
		getActivity().registerReceiver(mResponseReceiver, intentFilter);
	}

	public void populateMergeAdapterByConstructorArguments() {
		// Create a new builder using the layout inflater

		MergeAdapterBuilder builder = new MergeAdapterBuilder(getActivity());

		// Switch for the constructor argument
		if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_GPS
				.getCode()) {
			getListView().setAdapter(
					builder.generateGPSAdapter());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_NETWORK
				.getCode()) {
			getListView().setAdapter(
					builder.generateNetworkAdapter());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_PASSIVE
				.getCode()) {
			getListView().setAdapter(
					builder.generatePassiveAdapter());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_SATELLITES
				.getCode()) {
			getListView().setAdapter(
					builder.generateSatelliteAdapter());
		} else if (getConstructorArguments() == MyConstants.FragmentCode.FRAGMENT_CODE_NMEA
				.getCode()) {
			getListView().setAdapter(
					builder.generateNMEAAdapter());
		} else {
			// Do Nothing
		}
		builder = null;
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
