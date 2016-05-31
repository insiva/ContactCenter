package com.matteo.cc.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.matteo.cc.entity.utils.ContactUtil;

public class ContactInfo {
	public static char OTHER_FIRST_CHAR='#';
	
	public int mId; // id
	public String mName;
	public Long mPhotoId; // 图片id
	private String mSortKey;
	public char mFirstChar;
	public List<PhoneNumber> mPhoneNumbers;
	public String mPinyinName;
	
	public ContactInfo(){
		this.mPhoneNumbers=new ArrayList<PhoneNumber>();
	}
	
	public void addNumber(String number,int type){
		this.mPhoneNumbers.add(new PhoneNumber(number, type));
	}
	
	public void setSortKey(String sortKey){
		this.mSortKey=sortKey;
		char firstChar=sortKey.charAt(0);
		if(firstChar>='A'&&firstChar<='Z'){
			this.mFirstChar=firstChar;
		}else if(firstChar>='a'&&firstChar<='z'){
			this.mFirstChar=(char)(firstChar-32);
		}else{
			this.mFirstChar=OTHER_FIRST_CHAR;
		}
		this.mPinyinName=ContactUtil.findPinyin(this.mSortKey).toLowerCase(Locale.ENGLISH);
	}
	
	public String getSortKey(){
		return this.mSortKey;
	}
	
	public class PhoneNumber{
		
		public PhoneNumber(String number,int type){
			this.mNumber=number;
			this.mType=type;
		}
		
		public int mType;
		public String mNumber;
	}
}
