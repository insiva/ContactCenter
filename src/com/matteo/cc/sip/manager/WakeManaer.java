package com.matteo.cc.sip.manager;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class WakeManaer {
	private static KeyguardManager mKeyguardManager;
	private static PowerManager mPowerManager;
	private static PowerManager.WakeLock mWakeLock;

	public static void wakeUp(Context context) {
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock kl = mKeyguardManager.newKeyguardLock("unLock");
		// 解锁
		kl.disableKeyguard();
		// 获取电源管理器对象
		mPowerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		// 点亮屏幕
		mWakeLock.acquire();
		// 释放
		//mWakeLock.release();
	}
	
	public static void lock(Context context){
		if(mWakeLock!=null){
			//mWakeLock.release();
		}
	}
}
