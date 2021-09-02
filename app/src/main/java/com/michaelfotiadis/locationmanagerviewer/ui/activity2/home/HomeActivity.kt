package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.anthonycr.grant.PermissionsManager
import com.anthonycr.grant.PermissionsResultAction
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.data.datastore.Singleton
import com.michaelfotiadis.locationmanagerviewer.databinding.ActivityHomeBinding
import com.michaelfotiadis.locationmanagerviewer.service.LocationService
import com.michaelfotiadis.locationmanagerviewer.service.LocationStatus
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.dispatcher.IntentDispatcher
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.ui.DepthPageTransformer
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.ui.RecyclerPagerAdapter
import com.michaelfotiadis.locationmanagerviewer.utils.AppLog
import com.michaelfotiadis.locationmanagerviewer.utils.DialogUtils.AboutDialog
import com.michaelfotiadis.locationmanagerviewer.utils.DialogUtils.ProviderInformationDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import timber.log.Timber
import kotlin.coroutines.CoroutineContext


@ExperimentalCoroutinesApi
class HomeActivity : AppCompatActivity(), CoroutineScope, AndroidScopeComponent {

    override val scope: Scope by activityRetainedScope()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private val intentDispatcher: IntentDispatcher by inject { parametersOf(this) }
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var binding: ActivityHomeBinding
    private var locationService: LocationService? = null
    private var snackBar: Snackbar? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            locationService = (service as? LocationService.LocationBinder)?.getService()
            binding.fabProgressCircle.visibility = View.VISIBLE
            launch {
                collectServiceStatus()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            locationService = null
            binding.fabProgressCircle.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.homeToolbar)

        val pagerAdapter = RecyclerPagerAdapter(this)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(DepthPageTransformer())
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            pagerAdapter.getTitleResIdForPosition(position).let { tabTitle ->
                tab.setText(tabTitle)
                tab.setContentDescription(tabTitle)
            }
            tab.setIcon(pagerAdapter.getIconResIdForPosition(position))
        }.attach()

        binding.fabProgressCircle.visibility = View.GONE
        binding.fab.setOnClickListener {
            Timber.d("Click on FAB")
            locationService?.toggleScanning()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("Action on " + item.itemId)
        when (item.itemId) {
            R.id.action_show_map -> showOnMap()
            R.id.action_dialog ->
                ProviderInformationDialog().show(supportFragmentManager, "Dialog")
            R.id.action_app_settings -> {
                intentDispatcher.navigateToAppDetails()
            }
            R.id.action_device_settings -> {
                intentDispatcher.navigateToDeviceLocationSettings()
            }
            R.id.action_show_licenses -> {
                intentDispatcher.navigateToLicenses()
            }
            R.id.action_about -> {
                // Start a dialog showing the about dialog
                val transaction = supportFragmentManager.beginTransaction()
                AboutDialog().show(transaction, AboutDialog::class.java.simpleName)
            }
            else -> AppLog.e("Nothing Selected. How did we get here?")
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        Intent(this, LocationService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onPause() {
        super.onPause()
        locationService?.stopScanning()
        snackBar?.dismiss()
        unbindService(connection)
    }

    private suspend fun collectServiceStatus() {
        locationService?.statusFlow?.collect { status ->
            Timber.d("Received Status $status")
            when (status) {
                LocationStatus.PermissionsNotGranted -> {
                    checkPermissions()
                }
                LocationStatus.ScanningStarted -> {
                    binding.fabProgressCircle.show()
                    binding.fab.setImageResource(R.drawable.ic_baseline_stop_24)
                }
                LocationStatus.ScanningStopped -> {
                    binding.fabProgressCircle.hide()
                    binding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                is LocationStatus.CombinedLocationUpdate -> {
                    Timber.d("Received Combined Location")
                }
                LocationStatus.GpsProviderUnavailable -> {

                    Timber.w("TODO: Gps Provider Unavailable")
                }
                LocationStatus.NetworkProviderUnavailable -> {
                    Timber.w("TODO: Network Provider Unavailable")
                }

                LocationStatus.NmeaUpdatesUnavailable -> {
                    Timber.w("TODO: NMEA Provider Unavailable")
                }
                LocationStatus.PassiveProviderUnavailable -> {
                    Timber.w("TODO: Passive Provider Unavailable")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }

    private fun checkPermissions() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            object : PermissionsResultAction() {
                override fun onGranted() {
                    Timber.d("Permissions granted")
                }

                override fun onDenied(permission: String) {
                    snackBar = Snackbar.make(
                        binding.homeCoordinatorLayout,
                        R.string.toast_warning_permission_not_granted,
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        setAction(R.string.snackbar_action_settings) {
                            intentDispatcher.navigateToAppDetails()
                        }
                        show()
                    }
                }
            })
    }

    private fun showOnMap() {
        val latitude: Double
        val longitude: Double
        if (Singleton.getInstance().gpsData.location != null) {
            AppLog.d("Using GPS Location")
            latitude = Singleton.getInstance().gpsData.latitude
            longitude = Singleton.getInstance().gpsData.longitude
        } else {
            AppLog.d("Using Passive Location")
            latitude = Singleton.getInstance().passiveData.latitude
            longitude = Singleton.getInstance().passiveData.longitude
        }
        if (latitude != 0.0 && longitude != 0.0) {
            val label = "My Location"
            val uri = String.format(
                "geo:<%s>,<%s>?q=<%s>,<%s>(%s)",
                latitude,
                longitude,
                latitude,
                longitude,
                label
            )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        } else {
            Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show()
        }
    }


}