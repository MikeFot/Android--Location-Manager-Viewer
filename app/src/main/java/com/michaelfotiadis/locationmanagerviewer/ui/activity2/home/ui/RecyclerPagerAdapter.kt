package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.fragment.NetworkLocationFragment
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.GpsFragment

class RecyclerPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GpsFragment.newInstance()
            1 -> NetworkLocationFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid Fragment position for pager tab: '$position'")
        }
    }

    @StringRes
    fun getTitleResIdForPosition(position: Int): Int {
        return when (position) {
            0 -> R.string.title_section1
            1 -> R.string.title_section2
            else -> throw IllegalArgumentException("Invalid Title position for pager tab: '$position'")
        }
    }

    @DrawableRes
    fun getIconResIdForPosition(position: Int): Int {
        return when (position) {
            0 -> R.drawable.ic_gps_fixed_white_24dp
            1 -> R.drawable.ic_network_wifi_white_24dp
            else -> throw IllegalArgumentException("Invalid Icon position for pager tab: '$position'")
        }
    }

}