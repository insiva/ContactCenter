package com.matteo.cc.sip.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactUtils {

	public static String getNameByNumber(String number, Context context) {
		String name = null;
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(Phone.CONTENT_URI,
					new String[] { Phone.DISPLAY_NAME }, null, null,
					String.format("%s='%s'", Phone.NUMBER, number));
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
