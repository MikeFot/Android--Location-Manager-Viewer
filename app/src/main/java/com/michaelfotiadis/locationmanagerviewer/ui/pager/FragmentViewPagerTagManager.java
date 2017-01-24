package com.michaelfotiadis.locationmanagerviewer.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;

/*package*/ class FragmentViewPagerTagManager {
    private final FragmentManager mFragmentManager;
    private final SparseArray<String> mFragmentTags = new SparseArray<>();

    FragmentViewPagerTagManager(final FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    Fragment get(final int position) {
        return this.mFragmentManager.findFragmentByTag(this.mFragmentTags.get(position));
    }

    boolean has(final int position) {
        return position < this.mFragmentTags.size()
                && this.mFragmentManager.findFragmentByTag(this.mFragmentTags.get(position)) != null;
    }

    Fragment itemInstantiated(final int position, final Fragment fragment) {
        this.mFragmentTags.put(position, fragment.getTag());
        return fragment;
    }
}