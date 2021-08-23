package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.GpsFragment
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.NetworkFragment

class RecyclerPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GpsFragment.newInstance()
            1 -> NetworkFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid Fragment position for pager tab: '$position'")
        }
    }

    fun getTitleResIdForPosition(position: Int): Int {
        return when (position) {
            0 -> R.string.title_section1
            1 -> R.string.title_section2
            else -> throw IllegalArgumentException("Invalid Title position for pager tab: '$position'")
        }
    }

}