package com.matteo.cc.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private static Context mAppContext;
	
	public static void init(Context context){
		mAppContext=context;
	}
	
	public static void show(int resId){
		ToastUtils.show(mAppContext.getResources().getString(resId));
	}
	
	public static void show(String text){
		Toast.makeText(mAppContext, text, Toast.LENGTH_SHORT).show();;
	}
}
