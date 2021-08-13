package com.michaelfotiadis.locationmanagerviewer.ui.fragment;

import android.content.IntentFilter;
import android.widget.ListAdapter;

import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;

/**
 *
 */

public class NmeaFragment extends BaseListFragment {


    @Override
    protected ListAdapter getListAdapter() {
        return getMergeAdapterBuilder().generateNmeaAdapter();
    }

    @Override
    protected void registerResponseReceiver() {

        final IntentFilter intentFilter = new IntentFilter();

        // NMEA broadcast
        intentFilter
                .addAction(MyConstants.Broadcasts.BROADCAST_NMEA_CHANGED
                        .getString());

        mResponseReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mResponseReceiver, intentFilter);
    }

    public static BaseListFragment newInstance() {
        return new NmeaFragment();
    }
}
