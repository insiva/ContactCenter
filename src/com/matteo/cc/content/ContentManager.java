package com.matteo.cc.content;

import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.matteo.cc.entity.CallLogInfo;
import com.matteo.cc.entity.ContactInfo;
import com.matteo.cc.entity.SmsInfo.SmsThreadInfo;
import com.matteo.cc.entity.utils.CallLogUtil;
import com.matteo.cc.entity.utils.ContactUtil;
import com.matteo.cc.entity.utils.SmsUtil;

public class ContentManager {
	private static ContentManager mInstance=null;
	
	private List<CallLogInfo> mCallLogs=null;
	private List<ContactInfo> mContacts=null;
	private List<SmsThreadInfo> mSmsThreds=null;
	
	private ContentManager(){
		
	}
	
	public List<ContactInfo> getContacts(){
		return this.mContacts;
	}
	
	public List<CallLogInfo> getCallLogs(){
		return this.mCallLogs;
	}
	
	public List<SmsThreadInfo> getSmsThreads(){
		return this.mSmsThreds;
	}
	
	public void readAllContents(Context context){
		this.mCallLogs=CallLogUtil.readAllCallLogs(context);
		this.mContacts=ContactUtil.readAllContacts(context);
		this.mSmsThreds=SmsUtil.readAllSms(context);
	}
	
	public void readNewestCallLogs(Context context){
		List<CallLogInfo> callLogs=CallLogUtil.readAllCallLogs(context);
		Collections.reverse(callLogs);
		for (CallLogInfo callLog : callLogs) {
			this.mCallLogs.add(0, callLog);
		}
	}
	
	public static ContentManager get(){
		if(mInstance==null){
			init();
		}
		return mInstance;
	}
	
	private static synchronized void init(){
		if(mInstance==null){
			mInstance=new ContentManager();
		}
	}
}
