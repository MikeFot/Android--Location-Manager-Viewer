package com.michaelfotiadis.locationmanagerviewer.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import timber.log.Timber

class ApplicationInitializer(
    private val context: Context,
    private val configurationRepository: ConfigurationRepository
) {

    @SuppressLint("LogNotTimber")
    fun initialiseLogging() {
        if (configurationRepository.isDebugEnabled) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber initialised.")
        } else {
            Log.d(this::class.simpleName, "Skipped Timber initialisation")
        }
    }

    fun initialiseFirebase() {
        if (configurationRepository.isFirebaseEnabled) {
            FirebaseApp.initializeApp(context)
            Timber.d("Firebase Initialised.")
        } else {
            Timber.d("Skipped Firebase initialisation...")
        }
    }

}