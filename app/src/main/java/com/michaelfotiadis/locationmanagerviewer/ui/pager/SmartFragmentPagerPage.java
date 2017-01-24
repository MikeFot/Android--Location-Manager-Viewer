package com.michaelfotiadis.locationmanagerviewer.ui.pager;

import android.support.v4.app.Fragment;

public final class SmartFragmentPagerPage {
    private final Fragment mFragment;
    private final CharSequence mNavBarTitle;
    private final int mTabIcon;
    private final CharSequence mTabTitle;

    private SmartFragmentPagerPage(final Builder builder) {
        this.mFragment = builder.mFragment;
        this.mTabIcon = builder.mIconResId;
        this.mTabTitle = builder.mTabTitle;
        this.mNavBarTitle = builder.mNavBarTitle;

        if (mFragment == null) {
            throw new IllegalStateException("Fragment cannot be null!");
        }
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public CharSequence getNavBarTitle() {
        return mNavBarTitle;
    }

    public int getTabIcon() {
        return mTabIcon;
    }

    public CharSequence getTabTitle() {
        return mTabTitle;
    }

    public static final class Builder {
        private Fragment mFragment;
        private int mIconResId;
        private CharSequence mNavBarTitle;
        private CharSequence mTabTitle;

        public SmartFragmentPagerPage build() {
            return new SmartFragmentPagerPage(this);
        }

        public Builder withFragment(final Fragment mFragment) {
            this.mFragment = mFragment;
            return this;
        }

        public Builder withNavBarTitle(final CharSequence mNavBarTitle) {
            this.mNavBarTitle = mNavBarTitle;
            return this;
        }

        public Builder withTabIcon(final int iconResId) {
            this.mIconResId = iconResId;
            return this;
        }

        public Builder withTabTitle(final CharSequence mTabTitle) {
            this.mTabTitle = mTabTitle;
            return this;
        }
    }
}