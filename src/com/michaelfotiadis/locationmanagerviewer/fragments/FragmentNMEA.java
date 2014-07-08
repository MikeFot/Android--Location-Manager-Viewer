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

public class FragmentNMEA extends ListFragment {
	
	/**
	 * Custom receiver for NMEA changes
	 * @author Michael Fotiadis
	 *
	 */
	private class ResponseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					MyConstants.Broadcasts.BROADCAST_3.getString())) {
				populateMergeAdapter();
			} 
		}
	}

	private static final String ARG_POSITION = "position";
	public static FragmentNMEA newInstance(int position) {
		FragmentNMEA f = new FragmentNMEA();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	private final String TAG = "Fragment TWO";

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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_nmea, container, false);
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
		populateMergeAdapter();
		super.onResume();
	}
	
	/**
	 * Adds data to the Merge Adapter
	 */
	private void populateMergeAdapter() {

		final MergeAdapter adapter = new MergeAdapter();

		appendHeader(adapter, "NMEA Sentences");
		appendSimpleText(adapter, Singleton.getInstance().getGPSData().getNmea());
		
		getListView().setAdapter(adapter);
	}
	
	/**
	 * Registers response receiver looking for NMEA change broadcasts
	 */
	private void registerResponseReceiver() {
		Logger.d(TAG, "Registering Response Receiver");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_3.getString());

		mResponseReceiver = new ResponseReceiver();
		getActivity().registerReceiver(mResponseReceiver, intentFilter);
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


