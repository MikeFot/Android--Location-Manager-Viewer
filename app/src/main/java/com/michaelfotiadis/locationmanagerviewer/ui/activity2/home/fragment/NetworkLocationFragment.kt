package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.databinding.FragmentNetworkLocationBinding

class NetworkLocationFragment : Fragment() {

    private lateinit var binding: FragmentNetworkLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_network_location, container, false)
    }

    companion object {
        fun newInstance() = NetworkLocationFragment()
    }
}