package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.injection

import androidx.fragment.app.FragmentActivity
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.dispatcher.IntentDispatcher
import org.koin.dsl.module


val homeModule = module {

    factory { (activity: FragmentActivity) ->
        IntentDispatcher(activity)
    }
}