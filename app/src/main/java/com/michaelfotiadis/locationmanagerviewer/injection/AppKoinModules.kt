package com.michaelfotiadis.locationmanagerviewer.injection

import android.location.LocationManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleService
import com.michaelfotiadis.locationmanagerviewer.data.ApplicationInitializer
import com.michaelfotiadis.locationmanagerviewer.data.ConfigurationRepository
import com.michaelfotiadis.locationmanagerviewer.data.datastore.nmea.NmeaListenerManagerProvider
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.HomeActivity
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.HomeViewModel
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.dispatcher.IntentDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        ConfigurationRepository()
    }
    single {
        ApplicationInitializer(
            context = androidContext(),
            configurationRepository = get()
        )
    }

}

val locationModule = module {
    single {
        (androidContext().getSystemService(LifecycleService.LOCATION_SERVICE) as LocationManager)
    }

    single {
        NmeaListenerManagerProvider(get()).getNmeaListenerManager()
    }
}

@ExperimentalCoroutinesApi
val homeActivityModule = module {

    scope<HomeActivity> {
        scoped { (activity: FragmentActivity) ->
            IntentDispatcher(activity)
        }
        viewModel<HomeViewModel>()
    }

}
