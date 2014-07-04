package com.michaelfotiadis.locationmanagerviewer.adapters;

import java.util.ArrayList;

import com.michaelfotiadis.locationmanagerviewer.utils.Logger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

/**
 * This is a helper class that implements the management of tabs and all
 * details of connecting a ViewPager with associated TabHost.  It relies on a
 * trick.  Normally a tab host has a simple API for supplying a View or
 * Intent that each tab will show.  This is not sufficient for switching
 * between pages.  So instead we make the content part of the tab host
 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
 * view to show as the tab content.  It listens to changes in tabs, and takes
 * care of switch to the correct paged in the ViewPager whenever the selected
 * tab changes.
 * @author Radu Savutiu
 */
public class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final static String TAG = "TabsAdapter";
    private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final int count;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private Bundle arguments;

    static final class TabInfo {
        private final Class<?> clss;

        TabInfo(Class<?> _class) {
            clss = _class;
        }
    }

    public TabsAdapter(FragmentActivity activity, ViewPager pager, Bundle arguments, int count) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mActionBar = ((ActionBarActivity)activity).getSupportActionBar();
        
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
        
        this.count = count;
        this.arguments = arguments;
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss) {
        Logger.i(TAG, "addTab");
        TabInfo info = new TabInfo(clss);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.add(info);
        mActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
//        Logger.i(TAG, "getCount: " + Integer.toString(count));
        return count;
    }

    @Override
    public Fragment getItem(int position) {
                
//        ItemDetailImageFragment fragment1 = new ItemDetailImageFragment();
//        fragment1.setArguments(arguments);
//    
//        ItemDetailInfoFragment fragment2 = new ItemDetailInfoFragment();
//        fragment2.setArguments(arguments);

        TabInfo info = mTabs.get(position);
        Logger.i(TAG, "Get Item: " + Integer.toString(position) + " " + info.clss.getName());
        return Fragment.instantiate(mContext, info.clss.getName(), arguments);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Logger.i(TAG, "onPageScrolled: " + Integer.toString(position));
    }

    @Override
    public void onPageSelected(int position) {
        Logger.i(TAG, "onPageSelected: " + Integer.toString(position));
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        Logger.i(TAG, "onTabSelected");
        Object tag = tab.getTag();
        for (int i=0; i<mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i);
                Logger.i(TAG, "onTabSelected " + Integer.toString(i));
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        Logger.i(TAG, "onTabUnselected");
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        Logger.i(TAG, "onTabReselected");
    }
}