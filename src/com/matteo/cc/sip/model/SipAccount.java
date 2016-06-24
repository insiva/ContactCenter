package com.matteo.cc.sip.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsip_status_code;

import com.matteo.cc.sip.constant.Constant;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class SipAccount extends Account implements Parcelable {
	public static final String AGENT_ID_TAG="id";
	public static final String NAME_TAG="name";
	public static final String PASSWORD_TAG="password";
	public static final String SIP_SERVER_TAG="sip_server";
	public static final String PROXY_TAG="proxy";
	public static final String STATE_TAG="state";
	
	private String mAgentId;
	private String mName;
	private String mPassword;
	private String mSipServer;
	private String mProxy;
	private int mState;
	//private AccountConfig mAccountConfig;
	private IObserver mObserver;
	
	private SipAccount(){
		super();
		this.mState=Constant.SipAccountState.UNKNOW;
		AccountConfig accCfg = new AccountConfig();
		accCfg.setIdUri("sip:localhost");
		accCfg.getNatConfig().setIceEnabled(true);
		accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
		accCfg.getVideoConfig().setAutoShowIncoming(true);
		try {
			this.create(accCfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setObserver(IObserver observer){
		this.mObserver=observer;
	}
	
	public String getAgentId(){
		return this.mAgentId;
	}
	
	public String getName(){
		return this.mName;
	}
	
	public String getSipServer(){
		return this.mSipServer;
	}
	
	public String getProxy(){
		return this.mProxy;
	}
	
	public String getPassword(){
		return this.mPassword;
	}
	
	public int getState(){
		return this.mState;
	}
	
	public void setState(int state){
		this.mState=state;
	}
	
	public void refreshAccountConfig(){
		AccountConfig accountConfig=new AccountConfig();
		accountConfig.setIdUri(String.format("sip:%s@%s", this.mAgentId,this.mSipServer));
		accountConfig.getRegConfig().setRegistrarUri(String.format("sip:%s", this.mSipServer));
		AuthCredInfoVector creds = accountConfig.getSipConfig().getAuthCreds();
		creds.clear();
		if (!TextUtils.isEmpty(this.mName)) {
			creds.add(new AuthCredInfo("Digest", "*", this.mName, 0,
					this.mPassword));
		}
		StringVector proxies = accountConfig.getSipConfig().getProxies();
		proxies.clear();
		if (!TextUtils.isEmpty(this.mProxy)) {
			proxies.add(this.mProxy);
		}

		/* Enable ICE */
		accountConfig.getNatConfig().setIceEnabled(true);
		accountConfig.getVideoConfig().setAutoTransmitOutgoing(true);
		accountConfig.getVideoConfig().setAutoShowIncoming(true);
		try {
			this.modify(accountConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onIncomingCall(OnIncomingCallParam prm) {
		if(this.mObserver!=null){
			this.mObserver.notifyIncomingCall(prm);
		}
	}
	
	@Override
	public void onRegState(org.pjsip.pjsua2.OnRegStateParam prm) {
		if(this.mObserver!=null){
			this.mObserver.notifyRegState(prm.getCode(), prm.getReason(), prm.getExpiration());
		}
	}
	
	public String toJson(){
		JSONObject jsonSipAccount=new JSONObject();
		try {
			jsonSipAccount.put(AGENT_ID_TAG, this.mAgentId);
			jsonSipAccount.put(NAME_TAG, this.mName);
			jsonSipAccount.put(PASSWORD_TAG, this.mPassword);
			jsonSipAccount.put(PROXY_TAG, this.mProxy);
			jsonSipAccount.put(SIP_SERVER_TAG, this.mSipServer);
			jsonSipAccount.put(STATE_TAG, this.mState);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonSipAccount.toString();
	}
	
	public static SipAccount readJson(String json){
		JSONObject jsonSipAccount=null;
		SipAccount sipAccount=null;
		try {
			jsonSipAccount=new JSONObject(json);
			sipAccount=new SipAccount();
			sipAccount.mAgentId=jsonSipAccount.optString(AGENT_ID_TAG, "");
			sipAccount.mSipServer=jsonSipAccount.optString(SIP_SERVER_TAG, "");
			sipAccount.mName=jsonSipAccount.optString(NAME_TAG, "");
			sipAccount.mProxy=jsonSipAccount.optString(PROXY_TAG, "");
			sipAccount.mPassword=jsonSipAccount.optString(PASSWORD_TAG, "");
			sipAccount.mState=jsonSipAccount.optInt(STATE_TAG, Constant.SipAccountState.UNKNOW);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sipAccount;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.toJson());
	}
	
	public static final Parcelable.Creator<SipAccount> CREATOR=new Parcelable.Creator<SipAccount>() {

		@Override
		public SipAccount createFromParcel(Parcel source) {
			return SipAccount.readJson(source.readString());
		}

		@Override
		public SipAccount[] newArray(int size) {
			return null;
		}
	};
	
	public interface IObserver
	{
	    abstract void notifyRegState(pjsip_status_code code, String reason,
					 int expiration);
	    abstract void notifyIncomingCall(OnIncomingCallParam prm);
	}
}
