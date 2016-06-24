package com.matteo.cc;

public class Constant {
	public static final String HOST = "http://106.39.38.21:8081/cc/";
	
	public static final int STATUS_OK=0;
	public static final int STATUS_ERROR_DEFAULT=-1;
	
	public static final String TOKEN="token";
	public static final int MODULE_COUNT=4;
	public static final String PACKAGE_NAME="com.matteo.cc";
	
	public interface Action{
		public static final String SIP_SERVICE_CONNECTED="com.matteo.cc.SIP_SERVICE_CONNECTED";
	}
}
