package com.michaelfotiadis.locationmanagerviewer.ui.fragment;

import android.content.IntentFilter;
import android.widget.ListAdapter;

import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;

/**
 *
 */

public class PassiveFragment extends BaseListFragment {


    @Override
    protected ListAdapter getListAdapter() {
        return getMergeAdapterBuilder().generatePassiveAdapter();
    }

    @Override
    protected void registerResponseReceiver() {

        final IntentFilter intentFilter = new IntentFilter();

        // Passive broadcast
        intentFilter
                .addAction(MyConstants.Broadcasts.BROADCAST_PASSIVE_CHANGED
                        .getString());

        mResponseReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mResponseReceiver, intentFilter);
    }

    public static BaseListFragment newInstance() {
        return new PassiveFragment();
    }
}
