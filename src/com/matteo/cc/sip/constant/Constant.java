package com.matteo.cc.sip.constant;

public interface Constant {
	public static int DIRECT_MASK=1>>1;
	public static int STATE_MASK=0x00;
	
	public interface Direct {
		public static final char OUT = 1 >> 1;
		public static final int IN = 0 >> 1;
	}

	public interface State {
		public static final char OUT_NOT_START = 0x01; // 0001
		public static final char ESTABLISHING = 0x02; // 0010
		public static final char RING = 0x03;// 0011;
		public static final char CONNECTED = 0x04;// 0100;
		public static final char DISCONNECTED = 0x05;// 0101;
	}
}
