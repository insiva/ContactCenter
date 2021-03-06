package com.matteo.cc.sip.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactUtils {

	public static String getNameByNumber(String number, Context context) {
		String name = null;
		Cursor cursor = null;
		try {
			//String selectrion=String.format("%s='%s'", Phone.NUMBER, number);
			cursor = context.getContentResolver().query(Phone.CONTENT_URI,
					new String[] { Phone.DISPLAY_NAME }, Phone.NUMBER+"=?", 
					new String[]{number},
					null);
			if (cursor.moveToFirst()) {
				name = cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return name;
	}
}
