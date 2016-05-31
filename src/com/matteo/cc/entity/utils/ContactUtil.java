package com.matteo.cc.entity.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;

import com.matteo.cc.entity.ContactInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactUtil {
	private static final String[] PHONES_PROJECTION = new String[] {  
	       Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,
	       Phone.CONTACT_ID,Phone.SORT_KEY_PRIMARY ,Phone.LABEL,Phone.TYPE}; 
	
	private static String OrderCondition=String.format("%s asc", Phone.SORT_KEY_PRIMARY);
	public static List<ContactInfo> readAllContacts(Context context){
		List<ContactInfo> cs=new ArrayList<ContactInfo>();
		Cursor cursor=null;
		try{
			cursor = context.getContentResolver().query(Phone.CONTENT_URI, 
					PHONES_PROJECTION, null, null, OrderCondition);
			ContactInfo newContact=null,lastContact=null;
			while (cursor.moveToNext()) {
				newContact=getByCursor(cursor,lastContact);
				if(newContact!=null){
					if(lastContact==null||lastContact.mId!=newContact.mId){
						cs.add(newContact);
					}
				}
				lastContact=newContact;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		return cs;
	}
	
	static int NameIndex=-1;
	static int NumberIndex=-1;
	static int PhotoIdIndex=-1;
	static int ContactIdIndex=-1;
	static int SortKeyIndex=-1;
	static int TypeIndex=-1;
	
	public static ContactInfo getByCursor(Cursor cursor,ContactInfo lastContact){
		if(NameIndex<0){
			NameIndex=cursor.getColumnIndex(Phone.DISPLAY_NAME);
		}
		if(NumberIndex<0){
			NumberIndex=cursor.getColumnIndex(Phone.NUMBER);
		}
		if(PhotoIdIndex<0){
			PhotoIdIndex=cursor.getColumnIndex(Phone.PHOTO_ID);
		}
		if(ContactIdIndex<0){
			ContactIdIndex=cursor.getColumnIndex(Phone.CONTACT_ID);
		}
		if(SortKeyIndex<0){
			SortKeyIndex=cursor.getColumnIndex(Phone.SORT_KEY_PRIMARY);
		}
		if(TypeIndex<0){
			TypeIndex=cursor.getColumnIndex(Phone.TYPE);
		}
		ContactInfo c=null;
		int id=cursor.getInt(ContactIdIndex);
		if(lastContact==null||lastContact.mId!=id){
			c=new ContactInfo();
			//c.mName=cursor.getString(NameIndex);
			c.mPhotoId=cursor.getLong(PhotoIdIndex);
			c.mId=id;
			//c.setSortKey(cursor.getString(SortKeyIndex));
			c.setName(cursor.getString(NameIndex), cursor.getString(SortKeyIndex));
		}else{
			c=lastContact;
		}
		c.addNumber(cursor.getString(NumberIndex).replace(" ", ""), cursor.getInt(TypeIndex));
		return c;
	}
	static Pattern  PinyinPattern=Pattern.compile("\\d+.\\d+|\\w+");  

	public static String findPinyin(String src){
		Matcher matcher=PinyinPattern.matcher(src);
		StringBuilder sb=new StringBuilder();
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		String pinyin=sb.toString();
		return pinyin;
	}
}
