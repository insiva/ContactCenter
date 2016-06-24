package com.matteo.cc.sip.model;

import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnCallStateParam;

import com.matteo.cc.sip.constant.Constant;

public class PjCall extends Call {

	private SipAccount mAccount;
	private IObserver mObserver;
	
	public PjCall(SipAccount acc,IObserver observer){
		super(acc,-1);
		this.mAccount=acc;
		this.mObserver=observer;
	}
	public PjCall(SipAccount acc, int call_id,IObserver observer) {
		super(acc, call_id);
		this.mAccount=acc;
		this.mObserver=observer;
	}
	
	public int startCall(String number){
		CallOpParam prm = new CallOpParam(true);
		String calleeUri=String.format("sip:%s@%s", number,this.mAccount.getSipServer());
		try {
			super.makeCall(calleeUri, prm);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.Status.ERROR_DEFAULT;
		}
		return Constant.Status.OK;
	}
	
	@Override
	public void onCallState(OnCallStateParam prm) {
		if(this.mObserver!=null){
			this.mObserver.notifyCallState(this);
		}
	}
	
	interface IObserver{
		abstract void notifyCallState(PjCall call);
	}

}
