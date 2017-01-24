package com.michaelfotiadis.locationmanagerviewer.ui.fragment;

import android.content.IntentFilter;
import android.widget.ListAdapter;

import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;

/**
 *
 */

public class SatelliteFragment extends BaseListFragment {


    @Override
    protected ListAdapter getListAdapter() {
        return getMergeAdapterBuilder().generateSatelliteAdapter();
    }

    @Override
    protected void registerResponseReceiver() {

        final IntentFilter intentFilter = new IntentFilter();

        intentFilter
                .addAction(MyConstants.Broadcasts.BROADCAST_GPS_STATE_CHANGED
                        .getString());

        mResponseReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mResponseReceiver, intentFilter);
    }

    public static BaseListFragment newInstance() {
        return new SatelliteFragment();
    }
}
