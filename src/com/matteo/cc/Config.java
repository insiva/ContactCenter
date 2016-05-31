package com.matteo.cc;

import android.content.Context;

public class Config {
	private static Context mAppContext;
	
	public static void init(Context context){
		Config.mAppContext=context;
	}
	
	public static Context getAppContext(){
		return Config.mAppContext;
	}
}
