package com.matteo.cc;

import com.matteo.cc.sip.service.ISip;

import android.content.Context;

public class Config {
	private static Context mAppContext;
	private static ISip mSipService;
	
	public static void init(Context context){
		Config.mAppContext=context;
	}
	
	public static Context getAppContext(){
		return Config.mAppContext;
	}
	
	public static void setSipService(ISip sipService){
		Config.mSipService=sipService;
	}
	
	public static ISip getSipService(){
		return Config.mSipService;
	}
}
