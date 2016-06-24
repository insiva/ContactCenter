package com.matteo.cc.entity.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.CallLog.Calls;

import com.matteo.cc.entity.CallLogInfo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CallLogUtil {
	
	private static long mReadedTime=0;

	public static List<CallLogInfo> readAllCallLogs(Context context) {
		List<CallLogInfo> callLogs = new ArrayList<CallLogInfo>();
		Cursor cursor = null;
		ContentResolver contentResolver = context.getContentResolver();
		try {
			cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null,
					Calls.DATE+">?", new String[]{Long.toString(mReadedTime)}, CallLog.Calls.DATE + " desc");
			mReadedTime=System.currentTimeMillis();
			while (cursor.moveToNext()) {
				CallLogInfo cl=getByCursor(cursor);
				if(cl!=null){
					callLogs.add(cl);
				}
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return callLogs;
	}
	static int NumberIndex=-1;
	static int TypeIndex=-1;
	static int DateIndex=-1;
	static int NameIndex=-1;
	static int DurationIndex=-1;
	private static CallLogInfo getByCursor(Cursor cursor){
		CallLogInfo cl=new CallLogInfo();
		if(NumberIndex<0){
			NumberIndex=cursor.getColumnIndex(Calls.NUMBER);
		}
		cl.mNumber = cursor.getString(NumberIndex);
		if(TypeIndex<0){
			TypeIndex=cursor.getColumnIndex(Calls.TYPE);
		}
		cl.mType=cursor.getInt(TypeIndex);
		if(DateIndex<0){
			DateIndex=cursor.getColumnIndex(Calls.DATE);
		}
		cl.mCallDate=new Date(cursor.getLong(DateIndex));
		if(NameIndex<0){
			NameIndex=cursor.getColumnIndex(Calls.CACHED_NAME);
		}
		cl.mName=cursor.getString(NameIndex);
		if(DurationIndex<0){
			DurationIndex=cursor.getColumnIndex(Calls.DURATION);
		}
		cl.mDuration=cursor.getLong(DurationIndex);
		return cl;
	}
	
	public static String getTypeInfo(int type){
		String info=null;
		switch (type) {
		case CallLogInfo.INCOMING_TYPE:
			info="呼入";
			break;
		case CallLogInfo.OUTGOING_TYPE:
			info="呼出";
			break;
		case CallLogInfo.MISSED_TYPE:
			info="未接";
			break;

		default:
			break;
		}
		return info;
	}
}
