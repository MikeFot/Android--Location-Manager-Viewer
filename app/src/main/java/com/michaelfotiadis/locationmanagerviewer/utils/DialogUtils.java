package com.michaelfotiadis.locationmanagerviewer.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.michaelfotiadis.locationmanagerviewer.R;

public class DialogUtils {

	@SuppressLint("InflateParams")
	public static class AboutDialog extends DialogFragment {

		public AboutDialog() {
			
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View content = inflater.inflate(R.layout.dialog_about, null, false);
			final TextView version =(TextView) content.findViewById(R.id.version);

			version.setText( "Version: " + getString(R.string.version));

			return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.app_name)
			.setView(content)
			.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}
					)
					.create();
		}
	}

	public static class ProviderInformationDialog extends DialogFragment {
		WebView mWebView;
		public ProviderInformationDialog() {

		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			mWebView = new WebView(getActivity());
			mWebView.loadUrl("file:///android_asset/about.html");
			return new AlertDialog.Builder(getActivity())
			.setTitle(getString(R.string.provider_information))
			.setView(mWebView)
			.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}
					)
					.create();
		}
	}

	
}
