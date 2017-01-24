package com.michaelfotiadis.locationmanagerviewer.ui.pager;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class SmartFragmentPagerPages {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<Integer> mIcons = new ArrayList<>();
    private final List<CharSequence> mNavBarTitles = new ArrayList<>();
    private final List<CharSequence> mTitles = new ArrayList<>();

    public void add(final SmartFragmentPagerPage page) {
        mFragments.add(page.getFragment());
        mIcons.add(page.getTabIcon());
        mTitles.add(page.getTabTitle());
        mNavBarTitles.add(page.getNavBarTitle());
    }

    public void add(final String title, final Fragment fragment) {
        mTitles.add(title);
        mFragments.add(fragment);
        mIcons.add(0);
        mNavBarTitles.add(null);
    }

    public void add(final String tabTitle, final int iconResId, final Fragment fragment) {
        mTitles.add(tabTitle);
        mFragments.add(fragment);
        mIcons.add(iconResId);
        mNavBarTitles.add(null);
    }

    public void add(final int iconResId, final Fragment fragment) {
        mTitles.add(null);
        mFragments.add(fragment);
        mIcons.add(iconResId);
        mNavBarTitles.add(null);
    }

    public Fragment[] getFragments() {
        return mFragments.toArray(new Fragment[mFragments.size()]);
    }

    public int[] getIcons() {
        final int[] icons = new int[mIcons.size()];

        for (int i = 0; i < mIcons.size(); i++) {
            icons[i] = mIcons.get(i);
        }

        return icons;
    }

    public CharSequence[] getNavBarTitles() {
        return mNavBarTitles.toArray(new CharSequence[mNavBarTitles.size()]);
    }

    public int getSize() {
        return mTitles.size();
    }

    public CharSequence[] getTabTitles() {
        return mTitles.toArray(new CharSequence[mTitles.size()]);
    }
}