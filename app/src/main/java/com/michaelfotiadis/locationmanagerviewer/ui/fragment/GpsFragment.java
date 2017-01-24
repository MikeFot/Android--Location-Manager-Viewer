package com.michaelfotiadis.locationmanagerviewer.ui.fragment;

import android.content.IntentFilter;
import android.widget.ListAdapter;

import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;

/**
 *
 */

public class GpsFragment extends BaseListFragment {


    @Override
    protected ListAdapter getListAdapter() {
        return getMergeAdapterBuilder().generateGPSAdapter();
    }

    @Override
    protected void registerResponseReceiver() {

        final IntentFilter intentFilter = new IntentFilter();
        // GPS broadcast
        intentFilter
                .addAction(MyConstants.Broadcasts.BROADCAST_NETWORK_STATE_CHANGED
                        .getString());
        intentFilter.addAction(MyConstants.Broadcasts.BROADCAST_GPS_CHANGED
                .getString());
        mResponseReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mResponseReceiver, intentFilter);
    }

    public static BaseListFragment newInstance() {
        return new GpsFragment();
    }
}
