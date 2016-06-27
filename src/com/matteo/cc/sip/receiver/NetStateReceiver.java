package com.matteo.cc.sip.receiver;

import com.matteo.cc.sip.service.SipService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStateReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        //NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
        //NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
        /*
        Toast.makeText(context, "mobile:"+mobileInfo.isConnected()+"\n"+"wifi:"+wifiInfo.isConnected()  
                        +"\n"+"active:"+activeInfo.getTypeName(), 1).show();  
                        */
        NetworkInfo activeInfo = manager.getActiveNetworkInfo(); 
        if(activeInfo!=null&&activeInfo.isConnected()){
        	SipService.startService(context);
        }else {
			SipService.cancelRegister(context);
		}
	}

}
