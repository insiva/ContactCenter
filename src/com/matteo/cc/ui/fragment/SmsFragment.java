package com.matteo.cc.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matteo.cc.R;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.utils.view.ViewUtils;

public class SmsFragment extends BaseFragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment=View.inflate(this.getActivity(), R.layout.fragment_sms, null);
		ViewUtils.inject(this, fragment);
		return fragment;
	}
}
