package com.matteo.cc.sip.service;

import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;
import org.pjsip.pjsua2.pj_log_decoration;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import android.content.Intent;

import com.matteo.cc.sip.constant.Constant;
import com.matteo.cc.sip.model.XCallInfo;
import com.matteo.cc.sip.model.SipAccount;

public class PjService implements SipAccount.IObserver, XCallInfo.IObserver {

	public static final int SIP_PORT = 6000;
	public static final int LOG_LEVEL = 4;

	static {
		try {
			// System.loadLibrary("openh264");
			// System.loadLibrary("yuv");
			System.loadLibrary("pjsua2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SipService mSipServiceInstance;
	private SipAccount mSipAccount;
	private Endpoint mEndpoint;
	private TransportConfig mTransportConfig;
	private EpConfig mEpConfig;
	private XCallInfo mCurrentCall;
	private MyLogWriter mLogWriter;

	private PjService(SipService sipService) {
		this.mSipServiceInstance = sipService;
		this.init();
	}

	private void init() {
		this.mEndpoint = new Endpoint();
		this.mTransportConfig = new TransportConfig();
		this.mEpConfig = new EpConfig();
		try {
			this.mEndpoint.libCreate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mTransportConfig.setPort(SIP_PORT);
		this.mEpConfig.getLogConfig().setLevel(LOG_LEVEL);
		this.mEpConfig.getLogConfig().setConsoleLevel(LOG_LEVEL);

		/* Set log config. */
		LogConfig log_cfg = this.mEpConfig.getLogConfig();
		this.mLogWriter = new MyLogWriter();
		log_cfg.setWriter(this.mLogWriter);
		log_cfg.setDecor(log_cfg.getDecor()
				& ~(pj_log_decoration.PJ_LOG_HAS_CR.swigValue() | pj_log_decoration.PJ_LOG_HAS_NEWLINE
						.swigValue()));

		/* Set ua config. */
		UaConfig ua_cfg = this.mEpConfig.getUaConfig();
		ua_cfg.setUserAgent("Pjsua2 Android "
				+ this.mEndpoint.libVersion().getFull());
		StringVector stun_servers = new StringVector();
		stun_servers.add("stun.pjsip.org");
		ua_cfg.setStunServer(stun_servers);
		/*
		 * if (own_worker_thread) { ua_cfg.setThreadCnt(0);
		 * ua_cfg.setMainThreadOnly(true); }
		 */
		/* Init endpoint */
		try {
			this.mEndpoint.libInit(this.mEpConfig);
		} catch (Exception e) {
			return;
		}

		/* Create transports. */
		try {
			this.mEndpoint.transportCreate(
					pjsip_transport_type_e.PJSIP_TRANSPORT_UDP,
					this.mTransportConfig);
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			this.mEndpoint.transportCreate(
					pjsip_transport_type_e.PJSIP_TRANSPORT_TCP,
					this.mTransportConfig);
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			this.mEndpoint.libStart();
		} catch (Exception e) {
			return;
		}
	}

	public void deinit() {

		/*
		 * Try force GC to avoid late destroy of PJ objects as they should be
		 * deleted before lib is destroyed.
		 */
		Runtime.getRuntime().gc();

		/*
		 * Shutdown pjsua. Note that Endpoint destructor will also invoke
		 * libDestroy(), so this will be a test of double libDestroy().
		 */
		try {
			this.mEndpoint.libDestroy();
		} catch (Exception e) {
		}

		/*
		 * Force delete Endpoint here, to avoid deletion from a non- registered
		 * thread (by GC?).
		 */
		this.mEndpoint.delete();
		this.mEndpoint = null;
	}

	public void register(SipAccount sipAccount) {
		this.mSipAccount = sipAccount;
		if (this.mSipAccount == null) {
			return;
		}
		this.mSipAccount.setObserver(this);
		this.mSipAccount.refreshAccountConfig();
	}
	
	public void cancelRegister(){
		if(this.mSipAccount==null){
			return;
		}
		//this.mSipAccount.modify(null);
		int newState=Constant.SipAccountState.INACTIVATE;
		this.setSipAccountState(newState);
		this.mSipAccount.delete();
		this.mSipAccount=null;
	}

	public int makeCall(String callee) {
		if (this.mSipAccount == null
				|| this.mSipAccount.getState() != Constant.SipAccountState.ACTIVATE) {
			return Constant.Status.ERROR_INACTIVATE;
		}
		if (this.mCurrentCall != null) {
			return Constant.Status.ERROR_MULTI_CALLS;
		}
		this.mCurrentCall = new XCallInfo(callee, Constant.CallDirect.OUT,
				Constant.CallState.OUT_NOT_START);
		this.mCurrentCall.setObserver(this);
		int result=this.mCurrentCall.makeCall(this.mSipAccount);
		return result;
	}
	

	public int acceptCall() {
		if(this.mCurrentCall==null){
			return Constant.Status.OK;
		}
		return this.mCurrentCall.accept();
	}

	public int rejectCall() {
		if(this.mCurrentCall==null){
			return Constant.Status.OK;
		}
		return this.mCurrentCall.hangupOrReject();
	}

	public int hangupCall() {
		if(this.mCurrentCall==null){
			return Constant.Status.OK;
		}
		return this.mCurrentCall.hangupOrReject();
	}
	
	private void setSipAccountState(int newState){
		if(newState!=this.mSipAccount.getState()){
			this.mSipAccount.setState(newState);
			Intent broadcastIntent=new Intent(Constant.Action.SIP_STATE_CHANGED);
			broadcastIntent.putExtra(Constant.Key.SIP_STATE,newState);
			this.mSipServiceInstance.sendBroadcast(broadcastIntent);
		}
	}
	
	public XCallInfo getCurrentCall(){
		return this.mCurrentCall;
	}

	public int sendDtmf(char c) {
		return 0;
	}

	public static PjService createInstance(SipService sipService) {
		PjService pjService = new PjService(sipService);
		return pjService;
	}

	@Override
	public void notifyRegState(pjsip_status_code code, String reason,
			int expiration) {
		int newState=Constant.SipAccountState.UNKNOW;
		if(code.swigValue()/100 ==2){
			newState=Constant.SipAccountState.ACTIVATE;
		}
		this.setSipAccountState(newState);
	}

	class MyLogWriter extends LogWriter {
		@Override
		public void write(LogEntry entry) {
			System.out.println(entry.getMsg());
		}
	}

	@Override
	public void notifyIncomingCall(OnIncomingCallParam prm) {
		XCallInfo callInfo=new XCallInfo(this.mSipAccount, prm.getCallId());
		this.mCurrentCall=callInfo;
		//CallActivity.startActivity(this.mSipServiceInstance, callInfo);
		this.mCurrentCall.setObserver(this);
		this.mSipServiceInstance.onIncomingCall();
	}


	@Override
	public void notifyCallState(XCallInfo callInfo) {
		if(this.mCurrentCall==null){
			return;
		}
		this.mCurrentCall=callInfo;
		Intent broadcastIntent=new Intent(Constant.Action.CALL_STATE_CHANGED);
		broadcastIntent.putExtra(Constant.Key.CALL_INFO, callInfo);
		this.mSipServiceInstance.sendBroadcast(broadcastIntent);
		if(this.mCurrentCall.getState()==Constant.CallState.DISCONNECTED){
			this.mCurrentCall.writeToDb(mSipServiceInstance);
			this.mCurrentCall.delete();
			this.mCurrentCall=null;
			int newState=Constant.SipAccountState.ACTIVATE;
			this.setSipAccountState(newState);
		}else{
			int newState=Constant.SipAccountState.OCCUPIED;
			this.setSipAccountState(newState);
		}
	}
}
