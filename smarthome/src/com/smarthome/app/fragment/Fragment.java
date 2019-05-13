package com.smarthome.app.fragment;

import com.smarthome.app.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment extends BaseFragment
{

	@Override
	protected View bindLayout(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_allequipment, null, false);
		return view;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View rootView = bindLayout(inflater);
    	 return rootView;
    }
	
}
