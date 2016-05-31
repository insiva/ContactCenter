package com.matteo.cc.entity.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Sms;

import com.matteo.cc.entity.SmsInfo;
import com.matteo.cc.entity.SmsInfo.SmsThreadInfo;

@SuppressLint("InlinedApi")
public class SmsUtil {

	private static String OrderCondition = String.format("%s asc,%s desc",
			Sms.THREAD_ID, Sms.DATE);
	private static final String SMS_URI_ALL = "content://sms/";

	public static List<SmsThreadInfo> readAllSms(Context context) {
		List<SmsThreadInfo> sts = new ArrayList<SmsThreadInfo>();
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(Uri.parse(SMS_URI_ALL),
					null, null, null, OrderCondition);
			// SmsThreadInfo theSmsThread=null,lastSmsThread=null;
			SmsInfo sms = null;
			SmsThreadInfo st = null;
			while (cursor.moveToNext()) {
				sms=getByCursor(cursor);
				if(sms!=null){
					if(st==null||st.mNewestSms.mThreadId!=sms.mThreadId){
						st=new SmsThreadInfo();
						sts.add(st);
					}
					st.add(sms);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Collections.sort(sts);
		return sts;
	}

	static int IdIndex = -1;
	static int ThreadIdIndex = -1;
	static int NumberIndex = -1;
	static int PersonIndex = -1;
	static int DateIndex = -1;
	static int ReadIndex = -1;
	static int StatusIndex = -1;
	static int TypeIndex = -1;
	static int BodyIndex = -1;

	public static SmsInfo getByCursor(Cursor cursor) {
		if (IdIndex < 0) {
			IdIndex = cursor.getColumnIndex(Sms._ID);
		}
		if (ThreadIdIndex < 0) {
			ThreadIdIndex = cursor.getColumnIndex(Sms.THREAD_ID);
		}
		if (NumberIndex < 0) {
			NumberIndex = cursor.getColumnIndex(Sms.ADDRESS);
		}
		if (PersonIndex < 0) {
			PersonIndex = cursor.getColumnIndex(Sms.PERSON);
		}
		if (DateIndex < 0) {
			DateIndex = cursor.getColumnIndex(Sms.DATE);
		}
		if (ReadIndex < 0) {
			ReadIndex = cursor.getColumnIndex(Sms.READ);
		}
		if (StatusIndex < 0) {
			StatusIndex = cursor.getColumnIndex(Sms.STATUS);
		}
		if (TypeIndex < 0) {
			TypeIndex = cursor.getColumnIndex(Sms.TYPE);
		}
		if (BodyIndex < 0) {
			BodyIndex = cursor.getColumnIndex(Sms.BODY);
		}
		SmsInfo sms=new SmsInfo();
		sms.mId=cursor.getInt(IdIndex);
		sms.mThreadId=cursor.getInt(ThreadIdIndex);
		sms.mNumber=cursor.getString(NumberIndex);
		sms.mPersonName=cursor.getString(PersonIndex);
		sms.mRead=cursor.getInt(ReadIndex)==1;
		sms.mStatus=cursor.getInt(StatusIndex);
		sms.mType=cursor.getInt(TypeIndex);
		sms.mBody=cursor.getString(BodyIndex);
		sms.mDate=new Date(cursor.getLong(DateIndex));
		return sms;
	}
}
