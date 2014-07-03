package com.michaelfotiadis.locationmanagerviewer.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.adapters.TabsAdapter;
import com.michaelfotiadis.locationmanagerviewer.fragments.FragmentOne;
import com.michaelfotiadis.locationmanagerviewer.fragments.FragmentTwo;
import com.michaelfotiadis.locationmanagerviewer.utils.Logger;


public class MainActivity extends ActionBarActivity {

	private final String TAG = "Main Activity";

	private TabsAdapter mAdapter;
	private ViewPager mViewPager;
	//	private PagerSlidingTabStrip mTabStrip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar
		Logger.d(TAG, "Creating Action Bar");
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set ViewPager
		Logger.d(TAG, "Setting ViewPager");
		mViewPager = (ViewPager) findViewById(R.id.pager);

		// Initialise the adapter
		Logger.d(TAG, "Setting Adapter");
		Bundle arguments = new Bundle(); // no arguments to pass yet
		mAdapter  = new TabsAdapter(this, mViewPager, arguments, 2);

		// Add 2 tabs
		Logger.d(TAG, "Setting First Tab");
		Tab t = getSupportActionBar().newTab()
				.setText(getString(R.string.title_section1))
				.setTabListener(mAdapter);
		mAdapter.addTab(t, FragmentOne.class);

		Logger.d(TAG, "Setting Second Tab");
		t = getSupportActionBar().newTab()
				.setText(getString(R.string.title_section2))
				.setTabListener(mAdapter);
		mAdapter.addTab(t, FragmentTwo.class);

		// Set the adapter to the ViewPager
		Logger.d(TAG, "Setting ViewPager Adapter");
		mViewPager.setAdapter(mAdapter);

		Logger.d(TAG, "onCreate finished");
	}

	@Override
	protected void onStart() {
		Logger.i(TAG, "onStart");
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Logger.i(TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPause() {
		Logger.i(TAG, "onPause");
		super.onPause();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Logger.i(TAG, "onPrepareOptionsMenu");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		Logger.i(TAG, "onResume");
		super.onResume();
	}

}
