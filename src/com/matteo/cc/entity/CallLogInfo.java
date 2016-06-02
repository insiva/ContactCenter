package com.matteo.cc.entity;

import java.sql.Date;

import com.matteo.cc.utils.XTimeUtils;

import android.provider.CallLog.Calls;
import android.text.TextUtils;

public class CallLogInfo {
    /** Call log type for incoming calls. */
    public static final int INCOMING_TYPE = Calls.INCOMING_TYPE;
    /** Call log type for outgoing calls. */
    public static final int OUTGOING_TYPE = Calls.OUTGOING_TYPE;
    /** Call log type for missed calls. */
    public static final int MISSED_TYPE = Calls.MISSED_TYPE;
	
   
    
	static int ID = 0;
	public int mId;
	public Date mCallDate;
	public String mNumber;
	public int mType;
	public String mName;
	public long mDuration;
	private String mDisplayDate;

	public CallLogInfo() {
		this.mId = ++ID;
	}
	
	
	public String getDisplayDate(){
		if(TextUtils.isEmpty(this.mDisplayDate)){
			this.mDisplayDate=XTimeUtils.formatDateWithWeekDay(this.mCallDate);
		}
		return this.mDisplayDate;
	}
}
