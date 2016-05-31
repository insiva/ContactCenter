package com.matteo.cc.entity;

import java.util.ArrayList;
import java.util.List;

import com.matteo.cc.model.XString;

public class ContactInfo {
	public static char OTHER_FIRST_CHAR='#';
	
	public int mId; // id
	//public String mName;
	public Long mPhotoId; // 图片id
	//private String mSortKey;
	public XString mName;
	public char mFirstChar;
	public List<PhoneNumber> mPhoneNumbers;
	//public String mPinyinName;
	private XString mDisplayNumbers;
	
	public ContactInfo(){
		this.mPhoneNumbers=new ArrayList<PhoneNumber>();
	}
	
	public void addNumber(String number,int type){
		this.mPhoneNumbers.add(new PhoneNumber(number, type));
	}
	
	public void setName(String name){
		//this.mSortKey=sortKey;
		this.mName=new XString(name);
		char firstChar=this.mName.toPinyin().charAt(0);
		if(firstChar>='A'&&firstChar<='Z'){
			this.mFirstChar=firstChar;
		}else if(firstChar>='a'&&firstChar<='z'){
			this.mFirstChar=(char)(firstChar-('a'-'A'));
		}else{
			this.mFirstChar=OTHER_FIRST_CHAR;
		}
		//this.mPinyinName=ContactUtil.findPinyin(this.mSortKey).toLowerCase(Locale.ENGLISH);
	}
	
	public XString getDisplayNumbers(){
		if(XString.isEmpty(this.mDisplayNumbers)){
			StringBuilder sb=new StringBuilder();
			int s=this.mPhoneNumbers.size();
			for(int i=0;i<s;i++){
				sb.append(this.mPhoneNumbers.get(i).mNumber);
				if(i<(s-1)){
					sb.append(" | ");
				}
			}
			this.mDisplayNumbers=new XString(sb.toString());//sb.toString();
		}
		return this.mDisplayNumbers;
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
