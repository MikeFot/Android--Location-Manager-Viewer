package com.michaelfotiadis.locationmanagerviewer.ui.activity;

import com.michaelfotiadis.locationmanagerviewer.R;
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.GpsFragment;
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.NetworkFragment;
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.NmeaFragment;
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.PassiveFragment;
import com.michaelfotiadis.locationmanagerviewer.ui.fragment.SatelliteFragment;
import com.michaelfotiadis.locationmanagerviewer.ui.pager.SmartFragmentPagerPage;
import com.michaelfotiadis.locationmanagerviewer.ui.pager.SmartFragmentPagerPages;

/**
 *
 */
/*package*/ class HomeTabsFactory {

    private final BaseActivity mActivity;

    HomeTabsFactory(final BaseActivity activity) {
        this.mActivity = activity;
    }

    SmartFragmentPagerPages getPages() {
        final SmartFragmentPagerPages pages = new SmartFragmentPagerPages();

        pages.add(getGpsFragment(mActivity.getString(R.string.title_section1)));
        pages.add(getNetworkFragment(mActivity.getString(R.string.title_section2)));
        pages.add(getPassiveFragment(mActivity.getString(R.string.title_section3)));
        pages.add(getSatelliteFragment(mActivity.getString(R.string.title_section4)));
        pages.add(getNmeaFragment(mActivity.getString(R.string.title_section5)));

        return pages;
    }

    private static SmartFragmentPagerPage getGpsFragment(final String title) {

        return new SmartFragmentPagerPage.Builder()
                .withNavBarTitle(title)
                .withTabIcon(R.drawable.ic_gps_fixed_white_24dp)
                .withFragment(GpsFragment.newInstance()).build();
    }

    private static SmartFragmentPagerPage getNetworkFragment(final String title) {

        return new SmartFragmentPagerPage.Builder()
                .withNavBarTitle(title)
                .withTabIcon(R.drawable.ic_network_wifi_white_24dp)
                .withFragment(NetworkFragment.newInstance()).build();
    }

    private static SmartFragmentPagerPage getPassiveFragment(final String title) {

        return new SmartFragmentPagerPage.Builder()
                .withNavBarTitle(title)
                .withTabIcon(R.drawable.ic_location_on_white_24dp)
                .withFragment(PassiveFragment.newInstance()).build();
    }

    private static SmartFragmentPagerPage getSatelliteFragment(final String title) {

        return new SmartFragmentPagerPage.Builder()
                .withNavBarTitle(title)
                .withTabIcon(R.drawable.ic_satellite_white_24dp)
                .withFragment(SatelliteFragment.newInstance()).build();
    }

    private static SmartFragmentPagerPage getNmeaFragment(final String title) {

        return new SmartFragmentPagerPage.Builder()
                .withNavBarTitle(title)
                .withTabIcon(R.drawable.ic_assignment_white_24dp)
                .withFragment(NmeaFragment.newInstance()).build();
    }


}