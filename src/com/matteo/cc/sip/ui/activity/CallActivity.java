package com.matteo.cc.sip.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.matteo.cc.R;
import com.matteo.cc.sip.constant.Constant;
import com.matteo.cc.sip.manager.WakeManaer;
import com.matteo.cc.sip.model.XCallInfo;
import com.matteo.cc.sip.service.ISip;
import com.matteo.cc.sip.ui.view.CallControlView;
import com.matteo.cc.sip.ui.view.CallControlView.CallControlListener;
import com.matteo.cc.sip.ui.view.FunctionsView;
import com.matteo.cc.sip.ui.view.FunctionsView.FunctionsListener;
import com.matteo.cc.sip.utils.ContactUtils;

public class CallActivity extends Activity implements FunctionsListener,
		CallControlListener, SensorEventListener {

	// static final String NUMBER_KEY = "number";
	static final String CALL_INFO_KEY = "call_info";
	static final long CLOSE_INTERVAL = 3000;
	static final int WHAT_CLOSE = 0x001;
	static final int WHAT_SERVICE_CONNECTED = 0x002;

	private ISip mSipService;
	private TextView tvNameAndDtmf, tvState, tvDuration;
	private FunctionsView vFunctions;
	private CallControlView vCallControl;

	private String mDisplayName;
	private XCallInfo mCallInfo;
	private String mDtmf;
	private CallStateReceiver mCallStateReceiver;
	private int mDurationSeconds;
	private Timer mDurationTimer;
	private DurationTask mDurationTask;
	private SensorManager mSensorManager;// 传感器管理对象

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHAT_CLOSE) {
				CallActivity.this.finish();
			} else if (msg.what == WHAT_SERVICE_CONNECTED) {
				if (mCallInfo.getState() == Constant.CallState.OUT_NOT_START) {
					makeCall();
				}
			}
		}
	};

	private ServiceConnection mSipServiceConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mSipService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSipService = ISip.Stub.asInterface(service);
			mHandler.sendEmptyMessage(WHAT_SERVICE_CONNECTED);
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_call);
		this.bindSipService();
		this.tvNameAndDtmf = (TextView) this.findViewById(R.id.tvNameAndDtmf);
		this.tvState = (TextView) this.findViewById(R.id.tvState);
		this.tvDuration = (TextView) this.findViewById(R.id.tvDuration);
		this.vFunctions = (FunctionsView) this.findViewById(R.id.vFunctions);
		this.vFunctions.setDtmfPadListener(this);
		this.vCallControl = (CallControlView) this
				.findViewById(R.id.vCallControl);
		this.vCallControl.setCallControlListener(this);
		this.mCallStateReceiver = new CallStateReceiver();
		this.registerReceiver(this.mCallStateReceiver,
				this.mCallStateReceiver.mFilter);
		WakeManaer.wakeUp(this);
		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		this.init(this.getIntent());
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),// 距离感应器
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unbindService(this.mSipServiceConn);
		this.unregisterReceiver(this.mCallStateReceiver);
		if (mSensorManager != null) {
			// localWakeLock.release();//释放电源锁，如果不释放finish这个acitivity后仍然会有自动锁屏的效果，不信可以试一试
			mSensorManager.unregisterListener(this);// 注销传感器监听
		}
	}

	private void bindSipService() {
		Intent intent = new Intent();
		intent.setAction(Constant.Action.SIP_SERVICE);
		intent.setPackage("com.matteo.cc");
		this.bindService(intent, this.mSipServiceConn, Service.BIND_AUTO_CREATE);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.init(intent);
	}

	private void init(Intent intent) {
		this.mCallInfo = intent.getParcelableExtra(CALL_INFO_KEY);
		String name = ContactUtils.getNameByNumber(this.mCallInfo.getNumber(),
				this);
		if (TextUtils.isEmpty(name)) {
			this.mDisplayName = this.mCallInfo.getNumber();
		} else {
			this.mDisplayName = name;
		}
		this.vCallControl.updateCallState(this.mCallInfo);
		this.mDtmf = "";
		this.displayViews();
	}

	private void makeCall() {
		int result = Constant.Status.OK;
		// this.tvNameAndDtmf.
		try {
			result = this.mSipService.makeCall(this.mCallInfo.getNumber());
		} catch (RemoteException e) {
			e.printStackTrace();
			result = Constant.Status.ERROR_DEFAULT;
		}
		if (result == Constant.Status.OK) {

		} else {
			Toast.makeText(this,
					this.getResources().getString(R.string.call_out_failed),
					Toast.LENGTH_SHORT).show();
			this.closeActivity();
		}
	}

	private void displayViews() {
		this.tvState.setText(R.string.dialling);
		if (TextUtils.isEmpty(this.mDtmf)) {
			this.tvNameAndDtmf.setText(this.mDisplayName);
		} else {
			this.tvNameAndDtmf.setText(this.mDtmf);
		}
		this.updateCallInfo(this.mCallInfo);
	}

	private void updateCallInfo(XCallInfo newCallInfo) {
		// this.mCallInfo=newCallInfo;
		if (newCallInfo.getState() == this.mCallInfo.getState()
				&& newCallInfo.getDirect() == this.mCallInfo.getDirect()) {
			return;
		}
		this.mCallInfo.setState(newCallInfo.getState());
		switch (this.mCallInfo.getState()) {
		case Constant.CallState.OUT_NOT_START:
			this.tvState.setText(R.string.call_state_out_not_start);
			this.tvDuration.setVisibility(View.INVISIBLE);
			break;
		case Constant.CallState.ESTABLISHING:
			this.tvState.setText(R.string.call_state_establishing);
			this.tvDuration.setVisibility(View.INVISIBLE);
			break;
		case Constant.CallState.RING:
			this.tvState.setText(R.string.call_state_ring);
			this.tvDuration.setVisibility(View.VISIBLE);
			this.refreshDuration();
			break;
		case Constant.CallState.CONFIRMED:
			this.tvState.setText(R.string.call_state_confirmed);
			this.tvDuration.setVisibility(View.VISIBLE);
			this.refreshDuration();
			break;
		case Constant.CallState.DISCONNECTED:
			this.tvState.setText(R.string.call_state_disconnected);
			this.tvDuration.setVisibility(View.VISIBLE);
			this.refreshDuration();
			this.closeActivity();
			break;

		default:
			break;
		}
		this.vCallControl.updateCallState(this.mCallInfo);
	}

	private void refreshDuration() {
		this.mDurationSeconds = 0;
		if (this.mDurationTimer != null) {
			this.mDurationTimer.cancel();
			this.mDurationTask.cancel();
		}
		this.mDurationTimer = new Timer();
		this.mDurationTask = new DurationTask();
		this.mDurationTimer.schedule(this.mDurationTask, 0, 1000);
	}

	@Override
	public void onMuteEnabled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMuteDisabled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHandsfreeEnabled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHandsfreeDisabled() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShowDtmfPad() {
		this.vCallControl.showTvHideDtmfPad();
	}

	public static void startActivity(Context context, XCallInfo callInfo) {
		Intent intent = new Intent(context, CallActivity.class);
		intent.putExtra(CALL_INFO_KEY, callInfo);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void startActivity(Context context, String number) {
		CallActivity.startActivity(context, new XCallInfo(number,
				Constant.CallDirect.OUT, Constant.CallState.OUT_NOT_START));
	}

	@Override
	public void onAnswer() {
		try {
			this.mSipService.acceptCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReject() {
		try {
			this.mSipService.rejectCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onHangup() {
		try {
			this.mSipService.hangupCall();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onHideTvShowDtmfPad() {
		this.vFunctions.hideDtmfPad();
	}

	@Override
	public void onDtmfNumberClick(char dtmf) {
		this.mDtmf = this.mDtmf + dtmf;
		this.displayViews();
	}

	private void closeActivity() {
		this.mHandler.sendEmptyMessageDelayed(WHAT_CLOSE, CLOSE_INTERVAL);
	}

	class CallStateReceiver extends BroadcastReceiver {

		IntentFilter mFilter;

		CallStateReceiver() {
			this.mFilter = new IntentFilter(Constant.Action.CALL_STATE_CHANGED);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			XCallInfo newCallInfo = intent
					.getParcelableExtra(Constant.Key.CALL_INFO);
			if (newCallInfo == null) {
				return;
			}
			CallActivity.this.updateCallInfo(newCallInfo);
		}

	}

	class DurationTask extends TimerTask {

		@Override
		public void run() {
			final int duration = mDurationSeconds++;
			CallActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					int minutes = duration / 60;
					int seconds = duration % 60;
					tvDuration.setText(String.format("%02d:%02d", minutes,
							seconds));
				}
			});
		}

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] its = event.values;
		// Log.d(TAG,"its array:"+its+"sensor type :"+event.sensor.getType()+" proximity type:"+Sensor.TYPE_PROXIMITY);
		if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			// 经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
			if (its[0] == 0.0) {// 贴近手机
				WakeManaer.lock(this);
			} else {// 远离手机
				WakeManaer.wakeUp(this);
			}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
