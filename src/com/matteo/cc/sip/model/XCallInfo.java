package com.matteo.cc.sip.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

import com.matteo.cc.sip.constant.Constant;
import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CallLog;
import android.provider.CallLog.Calls;

public class XCallInfo implements Parcelable, PjCall.IObserver {

	public static int PJ_STATE_NULL=pjsip_inv_state.PJSIP_INV_STATE_NULL.swigValue(); //0
	public static int PJ_STATE_CALLING=pjsip_inv_state.PJSIP_INV_STATE_CALLING.swigValue(); //1
	public static int PJ_STATE_INCOMING=pjsip_inv_state.PJSIP_INV_STATE_INCOMING.swigValue(); //2
	public static int PJ_STATE_EARLY=pjsip_inv_state.PJSIP_INV_STATE_EARLY.swigValue(); //3
	public static int PJ_STATE_CONNECTING=pjsip_inv_state.PJSIP_INV_STATE_CONNECTING.swigValue(); //4
	public static int PJ_STATE_CONFIRMED=pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue(); //5
	public static int PJ_STATE_DISCONNECTED=pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED.swigValue(); //6
	
	private int mDirect;
	private int mState;
	private String mNumber;
	private String mUuid;
	private PjCall mPjCall;
	private long mDate;

	private IObserver mObserver;

	private XCallInfo() {
		this.mDate=System.currentTimeMillis();
	}

	public XCallInfo(String number, int direct, int state) {
		this.mNumber = number;
		this.mDirect = direct;
		this.mState = state;
		this.mDate=System.currentTimeMillis();
	}
	
	public XCallInfo(SipAccount acc,int pjCallId){
		this.mPjCall=new PjCall(acc, pjCallId,this);
		//this("",Constant.CallDirect.IN,Constant.CallState.RING);
		this.mDirect=Constant.CallDirect.IN;
		this.mState=Constant.CallState.RING;
		String number="";
		try {
			number=this.findNumberFromUri(this.mPjCall.getInfo().getRemoteUri());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mNumber=number;
		this.mDate=System.currentTimeMillis();
	}
	static Pattern UriPattern = Pattern.compile("sip:(.*)@");  
	
	private String findNumberFromUri(String uri){
		Matcher m = UriPattern.matcher(uri);  
		String number="";
		if(m.find()){
			number=m.group(1);
		}
		return number;
	}

	public void setObserver(IObserver observer) {
		this.mObserver = observer;
	}

	public int getDirect() {
		return this.mDirect;
	}

	public int getState() {
		return this.mState;
	}

	public String getNumber() {
		return this.mNumber;
	}

	public String getUuid() {
		return this.mUuid;
	}

	public int getCallState2() {
		return this.mDirect | this.mState;
	}

	public void setState(int state) {
		this.mState = state;
	}

	public void setUuid(String uuid) {
		this.mUuid = uuid;
	}

	public int makeCall(SipAccount sipAccount) {
		this.mPjCall = new PjCall(sipAccount, this);
		return this.mPjCall.startCall(this.mNumber);
	}
	
	public int hangupOrReject(){
		if(this.mPjCall==null){
			return Constant.Status.OK;
		}
		CallOpParam prm = new CallOpParam();
		prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
		int result=Constant.Status.ERROR_DEFAULT;
		try {
			this.mPjCall.hangup(prm);
			result=Constant.Status.OK;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int accept(){
		if(this.mPjCall==null){
			return Constant.Status.OK;
		}
		CallOpParam prm = new CallOpParam();
		prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
		int result=Constant.Status.ERROR_DEFAULT;
		try {
			this.mPjCall.answer(prm);
			result=Constant.Status.OK;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.mDirect);
		dest.writeInt(this.mState);
		dest.writeString(this.mNumber);
		dest.writeString(this.mUuid);
	}

	public static final Parcelable.Creator<XCallInfo> CREATOR = new Parcelable.Creator<XCallInfo>() {

		@Override
		public XCallInfo createFromParcel(Parcel source) {
			XCallInfo callState = new XCallInfo();
			callState.mDirect = source.readInt();
			callState.mState = source.readInt();
			callState.mNumber = source.readString();
			callState.mUuid = source.readString();
			return callState;
		}

		@Override
		public XCallInfo[] newArray(int size) {
			return null;
		}
	};

	@Override
	public void notifyCallState(PjCall call) {
		CallInfo ci = null;
		try {
			ci = call.getInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ci == null) {
			return;
		}
		int state = ci.getState().swigValue();
		int newState = 0;
		if (state == PJ_STATE_CALLING) { //1
			newState = Constant.CallState.ESTABLISHING;
		} else if (state == PJ_STATE_INCOMING) { //2
			newState = Constant.CallState.RING;
		} else if (state == PJ_STATE_EARLY) {//3
			newState = Constant.CallState.RING;
		} else if (state ==PJ_STATE_CONNECTING) {//4
			newState = Constant.CallState.RING;
		} else if (state == PJ_STATE_CONFIRMED) {//5
			newState = Constant.CallState.CONFIRMED;
		} else if (state == PJ_STATE_DISCONNECTED) {//6
			newState = Constant.CallState.DISCONNECTED;
		} else if (state == PJ_STATE_NULL) { //0
			//newState = Constant.CallState.DISCONNECTED;
		}
		if(newState>0){
			this.mState=newState;
			if (this.mObserver != null) {
				this.mObserver.notifyCallState(this);
			}
		}
	}

	public interface IObserver {
		abstract void notifyCallState(XCallInfo callInfo);
	}
	
	public void delete(){
		if(this.mPjCall!=null){
			//this.mPjCall.delete();
		}
	}
	
	public void writeToDb(Context context){
		CallInfo ci=null;
		try {
			ci=this.mPjCall.getInfo();
		} catch (Exception e) {
			ci=null;
			e.printStackTrace();
		}
		if(ci==null){
			return;
		}
		int cd=ci.getConnectDuration().getSec();
		ContentValues cv=new ContentValues();
		cv.put(Calls.NUMBER, this.mNumber);
		cv.put(Calls.DURATION, cd);
		cv.put(Calls.DATE, this.mDate);
		int type=Calls.OUTGOING_TYPE;
		if(this.mDirect==Constant.CallDirect.IN){
			if(cd<1){
				type=Calls.MISSED_TYPE;
			}else{
				type=Calls.INCOMING_TYPE;
			}
		}
		cv.put(Calls.TYPE, type);
		context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, cv);
	}
}
