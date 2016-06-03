package com.matteo.cc.utils;

import com.matteo.cc.sip.ui.activity.CallActivity;

import android.content.Context;

public class SipUtils {
	
	public static void makeCall(Context context,String number){
		CallActivity.startActivity(context, number);
	}
}
