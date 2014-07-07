package com.michaelfotiadis.locationmanagerviewer.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.containers.CustomConstants;
import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class FragmentOne extends ListFragment {

	public class ResponseReceiver extends BroadcastReceiver {
		private String TAG = "Response Receiver";
		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.d(TAG, "On Receiver Result");
			if (intent.getAction().equalsIgnoreCase(
					CustomConstants.Broadcasts.BROADCAST_1.getString())) {
				Logger.d(TAG, "Network Status Changed");
				populateDetails();
			} else if  (intent.getAction().equalsIgnoreCase(
					CustomConstants.Broadcasts.BROADCAST_2.getString())) {
				Logger.d(TAG, "GPS Location Changed");
				populateDetails();
			}
		}
	}

	private static final String ARG_POSITION = "position";


	public static FragmentOne newInstance(int position) {
		FragmentOne f = new FragmentOne();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	private final String TAG = "Fragment ONE";
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {



		
		super.onActivityCreated(savedInstanceState);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_one, container, false);
	}

	@Override
	public void onPause() {
		Logger.d(TAG, "onPause");
		Singleton.getInstance().stopCollectingGPSData();
		unregisterResponseReceivers();
		super.onPause();
	}

	@Override
	public void onResume() {
		Logger.d(TAG, "onResume");
		registerResponseReceiver();
		// Call the Singleton to start the GPS Tracker
		Singleton.getInstance().startCollectingGPSData();
		populateDetails();
		super.onResume();
	}


	private void populateDetails() {
		
		Logger.d(TAG, "Populating the Merge Adapter");
		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, "Network Status");
		appendSimpleText(adapter, "Cell Network Status: " + Singleton.getInstance().isCellNetworkEnabled());
		appendSimpleText(adapter, "GPS Enabled: " + Singleton.getInstance().isGPSEnabled());
		appendSimpleText(adapter, "Scanning for GPS: " + Singleton.getInstance().isGPSLocationSupported());

		appendHeader(adapter, "GPS Location");
		appendSimpleText(adapter, "Latitude: " + Singleton.getInstance().getGPSData().getLatitudeAsString());
		appendSimpleText(adapter, "Longitude: " + Singleton.getInstance().getGPSData().getLongitudeAsString());
		appendSimpleText(adapter, "Altitude: " + Singleton.getInstance().getGPSData().getAltitudeAsString());
		
		appendHeader(adapter, "GPS Details");
		appendSimpleText(adapter, "Accuracy: " + Singleton.getInstance().getGPSData().getAccuracyAsString());
		appendSimpleText(adapter, "Bearing: " + Singleton.getInstance().getGPSData().getBearingAsString());
		appendSimpleText(adapter, "Speed: " + Singleton.getInstance().getGPSData().getSpeedAsString());
		appendSimpleText(adapter, "UTC Time of Fix: " + Singleton.getInstance().getGPSData().getUtcFixTimeAsString());
		
		appendHeader(adapter, "GPS Satellites");
		appendSimpleText(adapter, "Max Satellites: " + Singleton.getInstance().getGPSData().getMaxSatellites());
		appendSimpleText(adapter, "Event: " + Singleton.getInstance().getGPSData().getGPSEvent());
		
		
		getListView().setAdapter(adapter);
	}


	private void registerResponseReceiver() {
		Logger.d(TAG, "Registering Response Receiver");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CustomConstants.Broadcasts.BROADCAST_1.getString());
		intentFilter.addAction(CustomConstants.Broadcasts.BROADCAST_2.getString());

		mResponseReceiver = new ResponseReceiver();
		getActivity().registerReceiver(mResponseReceiver, intentFilter);
	}

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
	
	/**
	 * Function to show settings alert dialog
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				getActivity().startActivity(intent);
				Singleton.getInstance().startCollectingGPSData();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Singleton.getInstance().startCollectingGPSData();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
