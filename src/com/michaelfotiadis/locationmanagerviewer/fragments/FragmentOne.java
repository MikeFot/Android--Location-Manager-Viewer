package com.michaelfotiadis.locationmanagerviewer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelfotiadis.locationmanagerviewer.R;

public class FragmentOne extends Fragment {

	private static final String ARG_POSITION = "position";
	
	public static FragmentOne newInstance(int position) {
		FragmentOne f = new FragmentOne();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_one, container, false);
	}
	

}
