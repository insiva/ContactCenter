package com.matteo.cc.sip.model;

import com.matteo.cc.sip.constant.Constant;

import android.os.Parcel;
import android.os.Parcelable;

public class CallInfo implements Parcelable {

	private int mDirect;
	private int mState;
	private String mNumber;
	private String mUuid;
	
	private CallInfo(){
		
	}
	
	public CallInfo(String number,int direct,int state){
		this.mNumber=number;
		this.mDirect=direct;
		this.mState=state;
	}
	
	public CallInfo(String number,int callState){
		this(number,callState&Constant.DIRECT_MASK, callState&Constant.STATE_MASK);
	}
	
	public int getDirect(){
		return this.mDirect;
	}
	
	public int getState(){
		return this.mState;
	}
	
	public String getNumber(){
		return this.mNumber;
	}
	
	public String getUuid(){
		return this.mUuid;
	}
	
	public int getCallState(){
		return this.mDirect|this.mState;
	}
	
	public void setState(int state){
		this.mState=state;
	}
	
	public void setUuid(String uuid){
		this.mUuid=uuid;
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
	
	public static final Parcelable.Creator<CallInfo> CREATOR=new Parcelable.Creator<CallInfo>() {

		@Override
		public CallInfo createFromParcel(Parcel source) {
			CallInfo callState=new CallInfo();
			callState.mDirect=source.readInt();
			callState.mState=source.readInt();
			callState.mNumber=source.readString();
			callState.mUuid=source.readString();
			return callState;
		}

		@Override
		public CallInfo[] newArray(int size) {
			return null;
		}
	};
}
