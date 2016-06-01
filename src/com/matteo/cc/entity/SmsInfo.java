package com.matteo.cc.entity;

import java.util.ArrayList;
import java.util.Date;

import com.matteo.cc.utils.XTimeUtils;

import android.text.TextUtils;

public class SmsInfo {
	public int mId;
	public int mThreadId;
	public String mNumber;
	public String mPersonName;
	public Date mDate;
	public boolean mRead;
	public int mStatus;
	public int mType;
	public String mBody;
	private String mDisplayDate;
	
	public String getDisplayDate(){
		if(TextUtils.isEmpty(this.mDisplayDate)){
			this.mDisplayDate=XTimeUtils.formatDate(this.mDate);
		}
		return this.mDisplayDate;
	}
	
	public static class SmsThreadInfo extends ArrayList<SmsInfo> implements Comparable<SmsThreadInfo>{

		private static final long serialVersionUID = 1L;
		public SmsInfo mNewestSms=null;
		
		@Override
		public boolean add(SmsInfo sms) {
			if(this.mNewestSms==null){
				this.mNewestSms=sms;
			}else{
				if(sms.mDate.after(this.mNewestSms.mDate)){
					this.mNewestSms=sms;
				}
			}
			return super.add(sms);
		}

		@Override
		public int compareTo(SmsThreadInfo another) {
			return another.mNewestSms.mDate.compareTo(this.mNewestSms.mDate);
		}
	}
}
