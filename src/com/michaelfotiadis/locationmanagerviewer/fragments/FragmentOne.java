package com.michaelfotiadis.locationmanagerviewer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.utils.GPSTracker;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class FragmentOne extends ListFragment {

	private static final String ARG_POSITION = "position";

	public static FragmentOne newInstance(int position) {
		FragmentOne f = new FragmentOne();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	GPSTracker mGPSTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		GPSTracker mGPSTracker = new GPSTracker(getActivity());
		if (!mGPSTracker.isGPSEnabled()) {
			mGPSTracker.showSettingsAlert();
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {


		monitorUpdates();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		Logger.d(TAG, "onResume");

		super.onResume();
	}

	@Override
	public void onPause() {
		Logger.d(TAG, "onPause");
		mGPSTracker = null;
		super.onPause();
	}

	private void monitorUpdates() {

		new Thread() {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(1000);
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (getActivity().isFinishing()) {
										interrupt();
									}
									populateDetails();
								}
							});
						}
					}
				} catch (InterruptedException e) {
				}
			}
		}.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_one, container, false);
	}


	private void populateDetails() {

		
		if (mGPSTracker == null) {
			Logger.d(TAG, "Initialising the GPS Tracker");
			mGPSTracker =  new GPSTracker(getActivity());
		}

		Logger.d(TAG, "Populating the Merge Adapter");
		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, "Network Status");
		appendSimpleText(adapter, "Network Supported: " + mGPSTracker.isNetworkEnabled());
		appendSimpleText(adapter, "Location Supported: " + mGPSTracker.canGetLocation());
		appendSimpleText(adapter, "GPS Enabled: " + mGPSTracker.isGPSEnabled());

		appendHeader(adapter, "GPS Location");
		appendSimpleText(adapter, "Latitude: " + mGPSTracker.getLatitude() + " degrees");
		appendSimpleText(adapter, "Longitude: " + mGPSTracker.getLongitude()+ " degrees");
		appendSimpleText(adapter, "Altitude: " + mGPSTracker.getAltitude() + " metres");;

		appendHeader(adapter, "GPS Details");
		appendSimpleText(adapter, "Accuracy: " + mGPSTracker.getAccuracy() + " metres");
		appendSimpleText(adapter, "Bearing: " + mGPSTracker.getBearing() + " degrees");
		appendSimpleText(adapter, "Speed: " + mGPSTracker.getSpeed() + " metres/second");
		appendSimpleText(adapter, "Time: " + mGPSTracker.getTime() + " milliseconds");
		
		appendHeader(adapter, "NMEA");
		appendSimpleText(adapter, mGPSTracker.getNMEA());
		
		getListView().setAdapter(adapter);
	}

	private final String TAG = "Fragment ONE";

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
}
