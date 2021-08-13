package com.michaelfotiadis.locationmanagerviewer.data.containers;


/**
 * Object for storing parameters pertaining to Network Status
 * @author Michael Fotiadis
 *
 */
public class MyNetworkStatus {

	private final String text_on = "ON";
	private final String text_off = "OFF";
	private boolean isGPSEnabled;
	private boolean isCellNetworkEnabled;
	public MyNetworkStatus() {

	}

	public String isGPSEnabledAsString() {
		if (isGPSEnabled()) {
			return text_on;
		} else {
			return text_off;
		}
	}

	public String isCellNetworkEnabledAsString() {
		if (isCellNetworkEnabled) {
			return text_on;
		} else {
			return text_off;
		}
	}

	public boolean isGPSEnabled() {
		return isGPSEnabled;
	}

	public void setGPSEnabled(boolean isGPSEnabled) {
		this.isGPSEnabled = isGPSEnabled;
	}

	public boolean isCellNetworkEnabled() {
		return isCellNetworkEnabled;
	}

	public void setCellNetworkEnabled(boolean isCellNetworkEnabled) {
		this.isCellNetworkEnabled = isCellNetworkEnabled;
	}
}
