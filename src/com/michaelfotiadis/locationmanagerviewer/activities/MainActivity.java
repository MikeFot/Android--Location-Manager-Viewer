package com.michaelfotiadis.locationmanagerviewer.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.adapters.TabsAdapter;
import com.michaelfotiadis.locationmanagerviewer.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.fragments.FragmentGPS;
import com.michaelfotiadis.locationmanagerviewer.fragments.FragmentNMEA;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class MainActivity extends ActionBarActivity implements
		OnCheckedChangeListener {

	/**
	 * Custom receiver for airplane mode changes
	 * @author Michael Fotiadis
	 *
	 */
	private class ResponseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					MyConstants.ACTION_AIRPLANE_MODE)) {
				Logger.i(TAG, "Airplane Mode Toggled");
				Singleton.getInstance().requestNetworkUpdate();
			}
		}
	}

	private final String TAG = "Main Activity";
	private TabsAdapter mAdapter;

	private ViewPager mViewPager;
	// private PagerSlidingTabStrip mTabStrip;

	Switch mSwitchButton;

	private boolean isScanning = false;

	ResponseReceiver mResponseReceiver;

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isScanning = isChecked;
		toggleScanning();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar
		Logger.d(TAG, "Creating Action Bar");
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		if (savedInstanceState != null) {
			isScanning = savedInstanceState.getBoolean(
					MyConstants.Payloads.PAYLOAD_1.toString(), false);
		} else {
			isScanning = false;
		}

		// Set ViewPager
		Logger.d(TAG, "Setting ViewPager");
		mViewPager = (ViewPager) findViewById(R.id.pager);

		// Initialise the adapter
		Logger.d(TAG, "Setting Adapter");
		Bundle arguments = new Bundle(); // no arguments to pass yet
		mAdapter = new TabsAdapter(this, mViewPager, arguments, 3);

		// Add 2 tabs
		Logger.d(TAG, "Setting First Tab");
		Tab t = getSupportActionBar().newTab()
				.setText(getString(R.string.title_section1))
				.setTabListener(mAdapter);
		mAdapter.addTab(t, FragmentGPS.class);

		Logger.d(TAG, "Setting Second Tab");
		t = getSupportActionBar().newTab()
				.setText(getString(R.string.title_section2))
				.setTabListener(mAdapter);
		mAdapter.addTab(t, FragmentNMEA.class);
		
		// Set the adapter to the ViewPager
		Logger.d(TAG, "Setting ViewPager Adapter");
		mViewPager.setAdapter(mAdapter);

		Logger.d(TAG, "onCreate finished");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Logger.i(TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		Logger.i(TAG, "onPause");

		unregisterResponseReceivers();
		Singleton.getInstance().stopCollectingGPSData();
		super.onPause();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Logger.i(TAG, "onPrepareOptionsMenu");
		MenuItem switchMenuItem = menu.getItem(0);
		mSwitchButton = (Switch) switchMenuItem.getActionView().findViewById(
				R.id.switchForActionBar);
		mSwitchButton.setChecked(isScanning);
		mSwitchButton.setOnCheckedChangeListener(this);
		mSwitchButton.setChecked(isScanning);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		Logger.i(TAG, "onResume");
		registerResponseReceiver();
		toggleScanning();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mSwitchButton != null) {
			outState.putBoolean(MyConstants.Payloads.PAYLOAD_1.toString(),
					mSwitchButton.isChecked());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		Logger.i(TAG, "onStart");
		super.onStart();
	}

	/**
	 * Registers a receiver for changes to Airplance Mode
	 */
	private void registerResponseReceiver() {
		Logger.d(TAG, "Registering Response Receiver");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MyConstants.ACTION_AIRPLANE_MODE);

		mResponseReceiver = new ResponseReceiver();
		this.registerReceiver(mResponseReceiver, intentFilter);
	}

	/**
	 * Function to show settings alert dialog
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
						Singleton.getInstance().startCollectingGPSData();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						Singleton.getInstance().startCollectingGPSData();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Starts/Stops scanning, depending on a stored boolean
	 */
	private void toggleScanning() {
		if (isScanning) {
			Logger.d(TAG, "Started Location Manager");
			Singleton.getInstance().startCollectingGPSData();
		} else {
			Logger.d(TAG, "Stopped Location Manager");
			Singleton.getInstance().stopCollectingGPSData();
		}
	}

	/**
	 * Unregisters the airplane mode receiver
	 */
	private void unregisterResponseReceivers() {
		try {
			this.unregisterReceiver(mResponseReceiver);
			Logger.d(TAG, "Response Receiver Unregistered Successfully");
		} catch (Exception e) {
			Logger.d(
					TAG,
					"Response Receiver Already Unregistered. Exception : "
							+ e.getLocalizedMessage());
		}
	}

}
