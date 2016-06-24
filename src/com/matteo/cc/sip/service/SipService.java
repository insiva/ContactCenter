package com.matteo.cc.sip.service;

import com.matteo.cc.sip.constant.Constant;
import com.matteo.cc.sip.model.SipAccount;
import com.matteo.cc.sip.service.ISip.Stub;
import com.matteo.cc.sip.ui.activity.CallActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class SipService extends Service {
	private static final int WHAT_INCOMING_CALL=0x001;
	private PjService mPjServiceInstance;
	private SipAccount mSipAccount;
	private SipBinder mSipBinder;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what==WHAT_INCOMING_CALL){
				if(mPjServiceInstance.getCurrentCall()!=null){
					CallActivity.startActivity(SipService.this, mPjServiceInstance.getCurrentCall());
				}
			}
		}
	};

	public class SipBinder extends Stub{

		@Override
		public int makeCall(String callee) throws RemoteException {
			return mPjServiceInstance.makeCall(callee);
		}

		@Override
		public int sendDtmf(char c) throws RemoteException {
			return mPjServiceInstance.sendDtmf(c);
		}

		@Override
		public int getSipState() throws RemoteException {
			return mSipAccount==null?Constant.SipAccountState.UNKNOW:mSipAccount.getState();
		}

		@Override
		public int acceptCall() throws RemoteException {
			return mPjServiceInstance.acceptCall();
		}

		@Override
		public int rejectCall() throws RemoteException {
			return mPjServiceInstance.rejectCall();
		}

		@Override
		public int hangupCall() throws RemoteException {
			return mPjServiceInstance.hangupCall();
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return this.mSipBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.mSipBinder=new SipBinder();
		this.mPjServiceInstance=PjService.createInstance(this);
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
		String sipAccountJson=preferences.getString(Constant.SIP_ACCOUNT_KEY, null);
		this.mSipAccount=SipAccount.readJson(sipAccountJson);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.mPjServiceInstance.register(this.mSipAccount);
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.mPjServiceInstance.deinit();
	}
	
	public static void startService(Context context){
		Intent intent=new Intent();
		intent.setAction(Constant.Action.SIP_SERVICE);
		intent.setPackage("com.matteo.cc");
		context.startService(intent);
	}
	
	public static void stopService(Context context){
		Intent intent=new Intent();
		intent.setAction(Constant.Action.SIP_SERVICE);
		intent.setPackage("com.matteo.cc");
		context.stopService(intent);
	}
	
	public void onIncomingCall(){
		this.mHandler.sendEmptyMessage(WHAT_INCOMING_CALL);
	}
}
