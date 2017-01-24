package com.michaelfotiadis.locationmanagerviewer.ui.fragment;

import android.content.IntentFilter;
import android.widget.ListAdapter;

import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;

/**
 *
 */

public class NetworkFragment extends BaseListFragment {


    @Override
    protected ListAdapter getListAdapter() {
        return getMergeAdapterBuilder().generateNetworkAdapter();
    }

    @Override
    protected void registerResponseReceiver() {

        final IntentFilter intentFilter = new IntentFilter();

        // Network broadcast
        intentFilter
                .addAction(MyConstants.Broadcasts.BROADCAST_NETWORK_CHANGED
                        .getString());

        mResponseReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mResponseReceiver, intentFilter);
    }

    public static BaseListFragment newInstance() {
        return new NetworkFragment();
    }
}
