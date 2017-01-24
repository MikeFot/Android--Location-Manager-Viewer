package com.michaelfotiadis.locationmanagerviewer.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class SmartFragmentPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    private final FragmentViewPagerTagManager mFragmentViewPagerTagManager;
    private FragmentAttachedListener mFragmentAttachedListener;
    private Fragment[] mFragments = {};
    private CharSequence[] mTitles = {};

    public SmartFragmentPagerAdapter(final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mFragmentViewPagerTagManager = new FragmentViewPagerTagManager(fragmentManager);
        this.mFragmentManager = fragmentManager;
    }

    public void clear() {
        for (final Fragment fragment : mFragments) {
            mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        if (mFragmentViewPagerTagManager.has(position)) {
            result = mFragmentViewPagerTagManager.get(position);
        } else {
            result = mFragments[position];
        }

        if (mFragmentAttachedListener != null) {
            mFragmentAttachedListener.onFragmentAttached(position, result);
        }

        return result;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        return mFragmentViewPagerTagManager.itemInstantiated(position,
                (Fragment) super.instantiateItem(container, position));
    }

    public void setFragmentAttachedListener(final FragmentAttachedListener fragmentAttachedListener) {
        this.mFragmentAttachedListener = fragmentAttachedListener;
    }

    public void setFragments(final Fragment[] fragments, final CharSequence[] titles) {
        if (fragments.length != titles.length) {
            throw new IllegalArgumentException("The fragments and titles arrays must have the same length");
        }

        this.mFragments = fragments;
        this.mTitles = titles;
    }

    public void setFragments(final SmartFragmentPagerPages pages) {
        setFragments(pages.getFragments(), pages.getTabTitles());
    }

    public static void onEnterPage(final Fragment fragment) {
        if (fragment instanceof SmartFragmentPagerAdapter.PagingFragment) {
            ((PagingFragment) fragment).onEnterPage();
        }
    }

    public static void onLeavePage(final Fragment fragment) {
        if (fragment instanceof PagingFragment) {
            ((PagingFragment) fragment).onLeavePage();
        }
    }

    public static void onScrollToTop(final Fragment fragment) {
        if (fragment instanceof PagingFragment) {
            ((PagingFragment) fragment).scrollToTop();
        }
    }

    public interface FragmentAttachedListener {
        void onFragmentAttached(int position, Fragment fragment);
    }

    public interface PagingFragment {

        void onEnterPage();

        void onLeavePage();

        void scrollToTop();
    }
}