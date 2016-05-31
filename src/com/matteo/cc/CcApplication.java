package com.matteo.cc;

import com.matteo.cc.utils.PreferenceUtils;
import com.matteo.cc.utils.ToastUtils;

import android.app.Application;

public class CcApplication extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate();
		Config.init(this);
		PreferenceUtils.init(this);
		ToastUtils.init(this);
	}
}
