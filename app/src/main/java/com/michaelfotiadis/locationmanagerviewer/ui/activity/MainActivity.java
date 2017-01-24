package com.michaelfotiadis.locationmanagerviewer.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
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

public class MainActivity extends BaseActivity {

    private static final int OFF_PAGE_LIMIT = 1;

    @BindView(R.id.view_pager)
    protected ViewPager mPager;
    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;
    ResponseReceiver mResponseReceiver;
    private boolean mIsScanning = false;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        if (!mIsScanning) {
            menu.findItem(R.id.button_stop).setVisible(false);
            menu.findItem(R.id.button_scan).setVisible(true);
        } else {
            menu.findItem(R.id.button_stop).setVisible(true);
            menu.findItem(R.id.button_scan).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        AppLog.d("Action on " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.button_scan:
                checkPermissions();

                break;
            case R.id.button_stop:
                toggleScanning(false);
                break;
            case R.id.action_show_map:
                showOnMap();
                break;
            case R.id.action_dialog:
                // Start a dialog showing location provider information
                new ProviderInformationDialog().show(getSupportFragmentManager(), "Dialog");
                break;
            case R.id.action_settings:
                this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            case R.id.action_about:
                // Start a dialog showing the about dialog
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                new AboutDialog().show(transaction, AboutDialog.class.getSimpleName());
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
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(MyConstants.Payloads.PAYLOAD_1.toString(), mIsScanning);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {

        super.onPause();
        unregisterResponseReceivers();
        Singleton.getInstance().stopCollectingLocationData();

    }

    @Override
    protected void onResume() {

        super.onResume();
        registerResponseReceiver();
        toggleScanning(mIsScanning);

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void checkPermissions() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        toggleScanning(true);
                    }

                    @Override
                    public void onDenied(final String permission) {

                        Toast.makeText(MainActivity.this, getString(R.string.toast_warning_permission_not_granted), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void setUpViewPager() {
        final HomeTabsFactory tabsFactory = new HomeTabsFactory(this);
        final SmartFragmentPagerPages pages = tabsFactory.getPages();

        final SmartFragmentPagerAdapter pagerAdapter = new SmartFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setFragments(pages);
        mPager.setAdapter(pagerAdapter);
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
            final String uri = String.format("geo:<%s>,<%s>?q=<%s>,<%s>(%s)", latitude, longitude, latitude, longitude, label);

            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts/Stops scanning, depending on a stored boolean
     */
    private void toggleScanning(final boolean isStart) {

        if (mIsScanning == isStart) {
            invalidateOptionsMenu();
            return;
        }

        mIsScanning = isStart;
        if (isStart) {
            AppLog.d("Started Location Manager");
            Singleton.getInstance().startCollectingLocationData();
            Toast.makeText(this, R.string.message_starting, Toast.LENGTH_SHORT).show();
        } else {
            AppLog.d("Stopped Location Manager");
            Singleton.getInstance().stopCollectingLocationData();
            Toast.makeText(this, R.string.message_stopping, Toast.LENGTH_SHORT).show();
        }
        invalidateOptionsMenu();
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
    private static class ResponseReceiver extends BroadcastReceiver {
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
