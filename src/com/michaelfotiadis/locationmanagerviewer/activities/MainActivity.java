package com.michaelfotiadis.locationmanagerviewer.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.adapters.CustomFragmentAdapter;
import com.michaelfotiadis.locationmanagerviewer.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.fragments.FragmentMergeAdapter;
import com.michaelfotiadis.locationmanagerviewer.utils.Dialogs;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

public class MainActivity extends ActionBarActivity implements
OnCheckedChangeListener, TabListener, OnPageChangeListener {

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
	private ViewPager mViewPager;
	private Switch mSwitchButton;
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
		final CustomFragmentAdapter cAdapter = new CustomFragmentAdapter(this);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(cAdapter);
		mViewPager.setOnPageChangeListener(this);
		// Initialise the adapter
		Logger.d(TAG, "Setting Adapter");


		new FragmentMergeAdapter();
		//Fragment f = FragmentGPS.newInstance(0);

		Tab tab;
		Fragment fragment;
		String tabTitle = "";


		for (int i = 0; i < 4; i++) {
			tab = getSupportActionBar().newTab();
			switch (i) {
			case 0:
				tabTitle = getString(R.string.title_section1);
				fragment = FragmentMergeAdapter.newInstance(MyConstants.FragmentCode.FRAGMENT_CODE_1.getCode());
				break;
			case 1:
				tabTitle = getString(R.string.title_section2);

				fragment = FragmentMergeAdapter.newInstance(MyConstants.FragmentCode.FRAGMENT_CODE_2.getCode());
				break;
			case 2:
				tabTitle = getString(R.string.title_section3);
				fragment = FragmentMergeAdapter.newInstance(MyConstants.FragmentCode.FRAGMENT_CODE_3.getCode());
				break;
			case 3:
				tabTitle = getString(R.string.title_section4);
				fragment = FragmentMergeAdapter.newInstance(MyConstants.FragmentCode.FRAGMENT_CODE_4.getCode());
				break;
			default:
				throw new IllegalStateException("We should not be here!");
			}

			tab.setText(tabTitle);
			tab.setTabListener(this);

			// Add the tab to the Action Bar
			getSupportActionBar().addTab(tab);

			// Add the fragment to the Adapter
			cAdapter.add(fragment, tabTitle);
		}

		// Set the adapter to the ViewPager
		mViewPager.getAdapter().notifyDataSetChanged();

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
		case R.id.action_dialog :
			new Dialogs().makeDialog(MainActivity.this, 
					generateTextForPosition(MainActivity.this.getSupportActionBar().getSelectedNavigationIndex()));
			break;
		case R.id.action_show_map:
			showOnMap();
			break;
		default:
			Logger.e(TAG, "Nothing Selected. How did we get here?");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showOnMap() {
		String uri = "https://maps.google.com/maps?f=d";   
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(i);
	}
	
	@Override
	protected void onPause() {
		Logger.i(TAG, "onPause");

		unregisterResponseReceivers();
		Singleton.getInstance().stopCollectingLocationData();
		super.onPause();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Logger.i(TAG, "onPrepareOptionsMenu");

		// Set up the Switch Button
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
			// Store the state of the Action Bar Switch Button
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
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// Do nothing
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		// Set the current item to the position of the tab
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// Do nothing
	}

	/**
	 * Registers a receiver for changes to Airplane Mode
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
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				Singleton.getInstance().startCollectingLocationData();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Singleton.getInstance().startCollectingLocationData();
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
			Singleton.getInstance().startCollectingLocationData();
		} else {
			Logger.d(TAG, "Stopped Location Manager");
			Singleton.getInstance().stopCollectingLocationData();
		}
	}

	/**
	 * Unregisters the Airplane mode receiver
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

	@Override
	public void onPageScrollStateChanged(int state) {
		// Do nothing
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// Do nothing
	}

	@Override
	public void onPageSelected(int position) {
		// Change the selected page
		getSupportActionBar().setSelectedNavigationItem(position);
	}

	public String generateTextForPosition (int position) {
		
		String message = "";
		
		 switch (position) {
		 case 0:
			 message = getString(R.string.message_gps);
			 break;
		 case 1:
			 message = getString(R.string.message_nmea);
			 break;
		 case 2:
			 message = getString(R.string.message_network);
			 break;
		 case 3:
			 message = getString(R.string.message_passive);
			 break;
		default:
			break;
		 }
		
		return message;
	}
	

}
