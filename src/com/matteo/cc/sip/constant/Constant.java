package com.matteo.cc.sip.constant;

public interface Constant {

	
	public interface Status{
		public static final int OK=0;
		public static final int ERROR_DEFAULT=-1;
		public static final int ERROR_INACTIVATE=1;
		public static final int ERROR_MULTI_CALLS=2;
	}
	
	//public static final int CALL_DIRECT_MASK=1>>1;
	//public static final int CALL_STATE_MASK=0x00;
	
	public static final String SIP_ACCOUNT_KEY="sip_account";
	
	public interface CallDirect {
		public static final int OUT = 1;
		public static final int IN = 0 ;
	}

	public interface CallState {
		public static final int OUT_NOT_START = 0x01; // 0001
		public static final int ESTABLISHING = 0x02; // 0010
		public static final int RING = 0x03;// 0011;
		public static final int CONFIRMED = 0x04;// 0100;
		public static final int DISCONNECTED = 0x05;// 0101;
	}
	
	public interface Action{
		public static final String SIP_SERVICE="com.matteo.cc.sip.service.SIP_SERVICE";
		public static final String SIP_STATE_CHANGED="com.matteo.cc.SIP_STATE_CHANGED";
		public static final String CALL_STATE_CHANGED="com.matteo.cc.CALL_STATE_CHANGED";
	}
	
	public interface Key{
		public static final String SIP_STATE="sip_state";
		public static final String CALL_INFO="call_info";
	}
	
	public interface Permission{
		public static final String SIP_PERMISSION="com.matteo.cc.PERMISSION.SIP";
	}
	
	 public interface SipAccountState{
		 public static final int UNKNOW=0;
		 public static final int ACTIVATE=1;
		 public static final int INACTIVATE=2;
		 public static final int OCCUPIED=3;
	 }
}
