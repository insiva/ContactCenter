package com.matteo.cc.sip.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.sip.constant.Constant;
import com.matteo.cc.sip.model.CallInfo;
import com.matteo.cc.sip.ui.view.CallControlView;
import com.matteo.cc.sip.ui.view.CallControlView.CallControlListener;
import com.matteo.cc.sip.ui.view.FunctionsView;
import com.matteo.cc.sip.ui.view.FunctionsView.FunctionsListener;
import com.matteo.cc.sip.utils.ContactUtils;

public class CallActivity extends Activity implements FunctionsListener,
		CallControlListener {

	//static final String NUMBER_KEY = "number";
	static final String CALL_INFO_KEY = "call_info";

	private TextView tvNameAndDtmf, tvStateAndDuration;
	private FunctionsView vFunctions;
	private CallControlView vCallControl;

	private String mDisplayName;
	private CallInfo mCallInfo;
	private String mDtmf;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_call);
		this.tvNameAndDtmf = (TextView) this.findViewById(R.id.tvNameAndDtmf);
		this.tvStateAndDuration = (TextView) this.findViewById(R.id.tvStateAndDuration);
		this.vFunctions = (FunctionsView) this.findViewById(R.id.vFunctions);
		this.vFunctions.setDtmfPadListener(this);
		this.vCallControl = (CallControlView) this
				.findViewById(R.id.vCallControl);
		this.vCallControl.setCallControlListener(this);
		this.init(this.getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.init(intent);
	}

	private void init(Intent intent) {
		this.mCallInfo=intent.getParcelableExtra(CALL_INFO_KEY);
		String name = ContactUtils.getNameByNumber(this.mCallInfo.getNumber(), this);
		if (TextUtils.isEmpty(name)) {
			this.mDisplayName = this.mCallInfo.getNumber();
		} else {
			this.mDisplayName = name;
		}
		this.vCallControl.setCallState(this.mCallInfo.getCallState());
		this.mDtmf="";
		this.displayViews();
	}

	private void displayViews() {
		//this.tvName.setText(this.mDisplayName);
		this.tvStateAndDuration.setText(R.string.dialling);
		if(TextUtils.isEmpty(this.mDtmf)){
			this.tvNameAndDtmf.setText(this.mDisplayName);
		}else{
			this.tvNameAndDtmf.setText(this.mDtmf);
		}
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

	public static void startActivity(Context context, String number,CallInfo callInfo) {
		Intent intent = new Intent(context, CallActivity.class);
		intent.putExtra(CALL_INFO_KEY, callInfo);
		context.startActivity(intent);
	}

	public static void startActivity(Context context, String number) {
		CallActivity.startActivity(context, number, new CallInfo(number, Constant.Direct.OUT, Constant.State.OUT_NOT_START));
	}

	@Override
	public void onAnswer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHangup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideTvShowDtmfPad() {
		this.vFunctions.hideDtmfPad();
	}

	@Override
	public void onDtmfNumberClick(char dtmf) {
		this.mDtmf=this.mDtmf+dtmf;
		this.displayViews();
	}
}
