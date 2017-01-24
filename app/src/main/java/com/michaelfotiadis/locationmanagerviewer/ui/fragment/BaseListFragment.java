package com.michaelfotiadis.locationmanagerviewer.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.data.containers.MyConstants;
import com.michaelfotiadis.locationmanagerviewer.data.datastore.Singleton;
import com.michaelfotiadis.locationmanagerviewer.utils.AppLog;
import com.michaelfotiadis.locationmanagerviewer.utils.MergeAdapterBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public abstract class BaseListFragment extends Fragment {

    protected ResponseReceiver mResponseReceiver;

    @BindView(R.id.list_view)
    ListView mListView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_merge_adapter, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setNestedScrollingEnabled(mListView, true);

        populateMergeAdapter();
    }

    @Override
    public void onResume() {
        registerResponseReceiver();

        populateMergeAdapter();

        Singleton.getInstance().requestNetworkUpdate();
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterResponseReceivers();
        super.onPause();
    }

    public ListView getListView() {
        return mListView;
    }

    protected MergeAdapterBuilder getMergeAdapterBuilder() {
        return new MergeAdapterBuilder(getActivity());
    }

    protected abstract ListAdapter getListAdapter();

    protected abstract void registerResponseReceiver();

    protected void populateMergeAdapter() {
        if (mListView != null && getActivity() != null && !getActivity().isFinishing()) {
            mListView.setAdapter(getListAdapter());
        }
    }

    /**
     * Unregisters the response receiver
     */
    private void unregisterResponseReceivers() {
        try {
            getActivity().unregisterReceiver(mResponseReceiver);
        } catch (final Exception e) {
            AppLog.e("Response Receiver Already Unregistered. Exception : "
                    + e.getLocalizedMessage());
        }
    }

    /**
     * Custom receiver for Network and GPS changes
     *
     * @author Michael Fotiadis
     */
    protected class ResponseReceiver extends BroadcastReceiver {
        @Override
        /*All of the cases are going to the same method but it is at least expandable
         in case I want to add more functionality*/
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equalsIgnoreCase(
                    MyConstants.Broadcasts.BROADCAST_NETWORK_STATE_CHANGED
                            .getString())) {
                populateMergeAdapter();
            } else if (intent.getAction().equalsIgnoreCase(
                    MyConstants.Broadcasts.BROADCAST_GPS_CHANGED.getString())) {
                populateMergeAdapter();
            } else if (intent.getAction().equalsIgnoreCase(
                    MyConstants.Broadcasts.BROADCAST_GPS_STATE_CHANGED
                            .getString())) {
                populateMergeAdapter();
            } else if (intent.getAction().equalsIgnoreCase(
                    MyConstants.Broadcasts.BROADCAST_NMEA_CHANGED.getString())) {
                populateMergeAdapter();
            } else if (intent.getAction().equalsIgnoreCase(
                    MyConstants.Broadcasts.BROADCAST_NETWORK_CHANGED
                            .getString())) {
                populateMergeAdapter();
            } else if (intent.getAction().equalsIgnoreCase(
                    MyConstants.Broadcasts.BROADCAST_PASSIVE_CHANGED
                            .getString())) {
                populateMergeAdapter();
            }
        }
    }


}
