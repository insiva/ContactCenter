package com.matteo.cc.ui.activity.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.matteo.cc.R;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class WaitingDialog extends BaseActivity{
	
	public static final String ACTION="com.matteo.cc.action.waiting_dialog";
	public static final String MSG_KEY="what";
	public static final int MSG_CLOSE=0x001;
	
	@ViewInject(R.id.pbWaiting)
	private ProgressBar pbWaiting;
	
	private DialogReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.dialog_waiting);
		ViewUtils.inject(this);
		this.mReceiver=new DialogReceiver();
		IntentFilter intentFilter=new IntentFilter(ACTION);
		this.registerReceiver(this.mReceiver, intentFilter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(this.mReceiver);
	}
	
	@Override
	public void onBackPressed() {
	}
	
	
	public static void startActivity(Context context){
		Intent intent=new Intent(context, WaitingDialog.class);
		context.startActivity(intent);
	}
	
	public static void closeActivity(Context context){
		Intent intent=new Intent(ACTION);
		intent.putExtra(MSG_KEY, MSG_CLOSE);
		context.sendBroadcast(intent);
	}
	
	class DialogReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ACTION)){
				if(intent.getIntExtra(MSG_KEY, 0)==MSG_CLOSE){
					WaitingDialog.this.finish();
				}
			}
		}
		
	}
}
