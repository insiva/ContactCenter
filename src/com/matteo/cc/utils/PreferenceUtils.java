package com.matteo.cc.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
	public static final String DEFAULT_SHARED_PREF_NAME="cc";
	
	private static Context mAppContext=null;
	private static SharedPreferences mSharedPreferences=null;
	
	private PreferenceUtils(){
	}
	
	public static void init(Context context){
		mAppContext=context;
		mSharedPreferences=mAppContext.getSharedPreferences(DEFAULT_SHARED_PREF_NAME, Context.MODE_PRIVATE);
	}
	
	public static String get(String key){
		return mSharedPreferences.getString(key, null);
	}
	
	public static void put(String key,String value){
		mSharedPreferences.edit().putString(key, value).commit();
	}
}
