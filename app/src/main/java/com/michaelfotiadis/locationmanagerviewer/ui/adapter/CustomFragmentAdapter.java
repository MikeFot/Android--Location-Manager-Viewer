package com.michaelfotiadis.locationmanagerviewer.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.michaelfotiadis.locationmanagerviewer.data.containers.MyFragmentInfo;

import java.util.ArrayList;
import java.util.List;
/**
 * Fragment Adapter for handling Instantiated Fragments instead of Classes
 * @author Michael Fotiadis
 *
 */
public class CustomFragmentAdapter extends FragmentStatePagerAdapter {

	private final List<String> mTitleList; // array of titles
	private final List<MyFragmentInfo> mTabs; // array of custom FragmentInfo objects

	private final Context mContext;

	public CustomFragmentAdapter(FragmentActivity context) {
		super(context.getSupportFragmentManager());
		mTabs = new ArrayList<MyFragmentInfo>();
		mTitleList = new ArrayList<String>();

		mContext = context;
	}

	public void add(Fragment fragment, String title) {
		if (fragment == null) {
			return;
		}

		mTabs.add(new MyFragmentInfo(fragment));
		mTitleList.add(title);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		final MyFragmentInfo info = mTabs.get(position);

		if (info.getFrag() == null) {
			return Fragment.instantiate(mContext, info.getClss().getName(),
					info.getArgs());
		} else {
			return info.getFrag();
		}
	}
}