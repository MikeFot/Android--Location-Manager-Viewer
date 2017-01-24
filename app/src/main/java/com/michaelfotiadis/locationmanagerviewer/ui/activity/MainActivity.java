package com.michaelfotiadis.locationmanagerviewer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.data.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.ui.pager.SmartFragmentPagerAdapter;
import com.michaelfotiadis.locationmanagerviewer.ui.pager.SmartFragmentPagerBinder;
import com.michaelfotiadis.locationmanagerviewer.ui.pager.SmartFragmentPagerPages;
import com.michaelfotiadis.locationmanagerviewer.utils.AppLog;
import com.michaelfotiadis.locationmanagerviewer.utils.DialogUtils.AboutDialog;
import com.michaelfotiadis.locationmanagerviewer.utils.DialogUtils.ProviderInformationDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener {


    private static final int OFF_PAGE_LIMIT = 1;

    @BindView(R.id.view_pager)
    protected ViewPager mPager;
    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;
    ResponseReceiver mResponseReceiver;
    private SmartFragmentPagerAdapter mPagerAdapter;
    private Switch mSwitchButton;
    private boolean mIsScanning = false;

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        mIsScanning = isChecked;
        toggleScanning();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        AppLog.i("onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        AppLog.i("onPrepareOptionsMenu");

        // Set up the Switch Button
        final MenuItem switchMenuItem = menu.getItem(0);
        mSwitchButton = (Switch) switchMenuItem.getActionView().findViewById(
                R.id.switchForActionBar);
        mSwitchButton.setChecked(mIsScanning);
        mSwitchButton.setOnCheckedChangeListener(this);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        AppLog.d("Action on " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_show_map:
                showOnMap();
                break;
            case R.id.action_dialog:
                // Start a dialog showing location provider information
                new ProviderInformationDialog().show(getSupportFragmentManager(), "Dialog");
                break;
            case R.id.action_settings:
                this.startActivity(new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            case R.id.action_about:
                // Start a dialog showing the about dialog
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                new AboutDialog().show(transaction, AboutDialog.class.getSimpleName());
                transaction.commit();
                break;
            default:
                AppLog.e("Nothing Selected. How did we get here?");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setDisplayHomeAsUpEnabled(false);

        mIsScanning = savedInstanceState != null && savedInstanceState.getBoolean(MyConstants.Payloads.PAYLOAD_1.toString(), false);

        setUpViewPager();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        AppLog.i("onStart");
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        if (mSwitchButton != null) {
            // Store the state of the Action Bar Switch Button
            outState.putBoolean(MyConstants.Payloads.PAYLOAD_1.toString(),
                    mSwitchButton.isChecked());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        AppLog.i("onPause");

        unregisterResponseReceivers();
        Singleton.getInstance().stopCollectingLocationData();
        super.onPause();
    }

    @Override
    protected void onResume() {
        AppLog.i("onResume");
        registerResponseReceiver();
        toggleScanning();
        super.onResume();
    }

    private void setUpViewPager() {
        final HomeTabsFactory tabsFactory = new HomeTabsFactory(this);
        final SmartFragmentPagerPages pages = tabsFactory.getPages();
        mPagerAdapter = new SmartFragmentPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setFragments(pages);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(OFF_PAGE_LIMIT);

        final SmartFragmentPagerBinder binder = new SmartFragmentPagerBinder(mPager, pages, mTabLayout,
                new SmartFragmentPagerBinder.NavBarTitleNeedsChangingListener() {
                    @Override
                    public void onNavBarTitleNeedsChanging(final CharSequence newTitle) {
                        setTitle(newTitle);
                    }
                });

        binder.bind();
        mPager.setCurrentItem(0);
        // The onPageSelectedEvent of OnPageChangeListener is not called for the first page
        binder.onPageSelected(0);

    }

    /**
     * Registers a receiver for changes to Airplane Mode
     */
    private void registerResponseReceiver() {
        AppLog.d("Registering Response Receiver");
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstants.ACTION_AIRPLANE_MODE);

        mResponseReceiver = new ResponseReceiver();
        this.registerReceiver(mResponseReceiver, intentFilter);
    }

    private void showOnMap() {
        final double latitude;
        final double longitude;
        if (Singleton.getInstance().getGPSData().getLocation() != null) {
            AppLog.d("Using GPS Location");
            latitude = Singleton.getInstance().getGPSData().getLatitude();
            longitude = Singleton.getInstance().getGPSData().getLongitude();
        } else {
            AppLog.d("Using Passive Location");
            latitude = Singleton.getInstance().getPassiveData().getLatitude();
            longitude = Singleton.getInstance().getPassiveData().getLongitude();
        }

        if (latitude != 0 && longitude != 0) {

            final String label = "My Location";
            final StringBuilder uri = new StringBuilder();
            uri.append("geo:<");
            uri.append(latitude);
            uri.append(">,<");
            uri.append(longitude);
            uri.append(">?q=<");
            uri.append(latitude);
            uri.append(">,<");
            uri.append(longitude);
            uri.append(">(");
            uri.append(label);
            uri.append(")");

            final Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
            startActivity(i);
        } else {
            Toast.makeText(this, "No Location",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts/Stops scanning, depending on a stored boolean
     */
    private void toggleScanning() {
        if (mIsScanning) {
            AppLog.d("Started Location Manager");
            Singleton.getInstance().startCollectingLocationData();
        } else {
            AppLog.d("Stopped Location Manager");
            Singleton.getInstance().stopCollectingLocationData();
        }
    }

    /**
     * Unregisters the Airplane mode receiver
     */
    private void unregisterResponseReceivers() {
        try {
            this.unregisterReceiver(mResponseReceiver);
            AppLog.d("Response Receiver Unregistered Successfully");
        } catch (final Exception e) {
            AppLog.d(
                    "Response Receiver Already Unregistered. Exception : "
                            + e.getLocalizedMessage());
        }
    }

    /**
     * Custom receiver for airplane mode changes
     *
     * @author Michael Fotiadis
     */
    class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equalsIgnoreCase(
                    MyConstants.ACTION_AIRPLANE_MODE)) {
                AppLog.i("Airplane Mode Toggled");
                Singleton.getInstance().requestNetworkUpdate();
            }
        }
    }


}
