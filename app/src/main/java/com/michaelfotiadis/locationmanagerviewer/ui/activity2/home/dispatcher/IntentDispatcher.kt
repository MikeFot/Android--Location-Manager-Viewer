package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.dispatcher

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

internal class IntentDispatcher(private val activity: FragmentActivity) {

    fun navigateToLicenses() {
        activity.startActivity(
            Intent(
                activity,
                OssLicensesMenuActivity::class.java
            )
        )
    }

    fun navigateToDeviceLocationSettings() {
        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    fun navigateToAppDetails() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:${activity.packageName}")
            activity.startActivity(intent)
        }
    }

}