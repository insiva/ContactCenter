package com.matteo.cc.model;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.matteo.cc.utils.PinYinUtils;

@SuppressLint("UseSparseArrays")
public class XString {
	private static HashMap<Integer, Character[]> IntegerDic;
	private static HashMap<Character, Integer> CharacterDic;

	static {
		IntegerDic = new HashMap<Integer, Character[]>();
		IntegerDic.put(2, new Character[] { 'a', 'b', 'c' });
		IntegerDic.put(3, new Character[] { 'd', 'e', 'f' });
		IntegerDic.put(4, new Character[] { 'g', 'h', 'i' });
		IntegerDic.put(5, new Character[] { 'j', 'k', 'l' });
		IntegerDic.put(6, new Character[] { 'm', 'n', 'o' });
		IntegerDic.put(7, new Character[] { 'p', 'q', 'r', 's' });
		IntegerDic.put(8, new Character[] { 't', 'u', 'v' });
		IntegerDic.put(9, new Character[] { 'w', 'x', 'y', 'z' });
		CharacterDic = new HashMap<Character, Integer>();
		CharacterDic.put('a', 2);
		CharacterDic.put('b', 2);
		CharacterDic.put('c', 2);
		CharacterDic.put('d', 3);
		CharacterDic.put('e', 3);
		CharacterDic.put('f', 3);
		CharacterDic.put('g', 4);
		CharacterDic.put('h', 4);
		CharacterDic.put('i', 4);
		CharacterDic.put('j', 5);
		CharacterDic.put('k', 5);
		CharacterDic.put('l', 5);
		CharacterDic.put('m', 6);
		CharacterDic.put('n', 6);
		CharacterDic.put('o', 6);
		CharacterDic.put('p', 7);
		CharacterDic.put('q', 7);
		CharacterDic.put('r', 7);
		CharacterDic.put('s', 7);
		CharacterDic.put('t', 8);
		CharacterDic.put('u', 8);
		CharacterDic.put('v', 8);
		CharacterDic.put('w', 9);
		CharacterDic.put('x', 9);
		CharacterDic.put('y', 9);
		CharacterDic.put('z', 9);
	}

	private String mValue;
	//private Pinyin mPinyin;
	private String mPinyinValue;
	private String mIntegerValue;
	private SearchResult mSearchResult;
	private List<String> mCharList;
	private List<String> mPinyinList;
	private List<String> mIntegerList;

	public XString(String value) {
		this.mValue = value;
		char[] cs=this.mValue.toCharArray();
		this.mCharList=new ArrayList<String>();
		this.mPinyinList=new ArrayList<String>();
		this.mIntegerList=new ArrayList<String>();
		for (char c : cs) {
			String s=String.valueOf(c);
			this.mCharList.add(s);
			if(isCharacter(c)){
				this.mPinyinList.add(s);
			}else{
				this.mPinyinList.add(PinYinUtils.getPingYin(s));
			}
		}
		for (String pinyin : this.mPinyinList) {
			cs=pinyin.toCharArray();
			StringBuilder sb=new StringBuilder();
			for (char c : cs) {
				char lowerC=toLower(c);
				if(CharacterDic.containsKey(lowerC)){
					sb.append(CharacterDic.get(lowerC));
				}else{
					sb.append(c);
				}
			}
			this.mIntegerList.add(sb.toString());
		}
	}
	
	public SearchResult getSearchResult(){
		return this.mSearchResult;
	}
	
	public void clearSearchResult(){
		this.mSearchResult=null;
	}

	@Override
	public String toString() {
		return this.mValue;
	}
	
	public String toPinyin(){
		if(TextUtils.isEmpty(this.mPinyinValue)){
			StringBuilder sb=new StringBuilder();
			for (String string : mPinyinList) {
				sb.append(string);
			}
			this.mPinyinValue=sb.toString();
		}
		return this.mPinyinValue;
	}
	
	public String toInteger(){
		if(TextUtils.isEmpty(this.mIntegerValue)){
			StringBuilder sb=new StringBuilder();
			for (String string : mIntegerList) {
				sb.append(string);
			}
			this.mIntegerValue=sb.toString();
		}
		return this.mIntegerValue;
	}
	
	public boolean search(String key,boolean isAllCharacter){
		this.mSearchResult=null;
		int len=key.length();
		int start=-1;
		if((start=this.mValue.indexOf(key))>=0){
			this.mSearchResult=new SearchResult(SearchResultField.VALUE,start,start+len);
			return true;
		}
		if(isAllCharacter){
			return this.searchByPinyin(key);
		}
		return false;
	}
	
	public boolean searchByPinyin(String key){
		int indexStart=this.toPinyin().indexOf(key);
		if(indexStart<0){
			return false;
		}
		int indexEnd=indexStart+key.length()-1;
		int s=this.mCharList.size();
		int pinyinStartI=0,pinyinEndI=0,start=-1,end=-1;
		for(int i=0;i<s;i++){
			String pinyin=this.mPinyinList.get(i);
			pinyinEndI=pinyinStartI+pinyin.length()-1;
			if(start<0&&indexStart>=pinyinStartI&&indexStart<=pinyinEndI){
				start=i;
			}
			if(end<0&&indexEnd>=pinyinStartI&&indexEnd<=pinyinEndI){
				end=i;
				break;
			}
			pinyinStartI=pinyinEndI+1;
		}
		this.mSearchResult=new SearchResult(SearchResultField.PINYIN,start,end+1);
		return true;
	}

	public boolean searchByInteger(String key){
		int indexStart=this.toInteger().indexOf(key);
		if(indexStart<0){
			return false;
		}
		int indexEnd=indexStart+key.length()-1;
		int s=this.mCharList.size();
		int pinyinStartI=0,pinyinEndI=0,start=-1,end=-1;
		for(int i=0;i<s;i++){
			String pinyin=this.mIntegerList.get(i);
			pinyinEndI=pinyinStartI+pinyin.length()-1;
			if(start<0&&indexStart>=pinyinStartI&&indexStart<=pinyinEndI){
				start=i;
			}
			if(end<0&&indexEnd>=pinyinStartI&&indexEnd<=pinyinEndI){
				end=i;
				break;
			}
			pinyinStartI=pinyinEndI+1;
		}
		this.mSearchResult=new SearchResult(SearchResultField.PINYIN,start,end+1);
		return true;
	}
	public static boolean isCharacter(Character c) {
		if (c == null) {
			return false;
		}
		return c >= 0 && c <= 255;
	}
	
	public static char toLower(char c){
		if(c>='A'&&c<='Z'){
			return (char)(c+('a'-'A'));
		}
		return c;
	}
	
	public static boolean isEmpty(XString text){
		return text==null?true:"".equals(text.mValue);
	}
	
	public static boolean isAllCharacter(String text){
		if(TextUtils.isEmpty(text)){
			return false;
		}
		char[] cs=text.toCharArray();
		for (char c : cs) {
			if(!isCharacter(c)){
				return false;
			}
		}
		return true;
	}
	
	public enum SearchResultField{
		NONE,VALUE,PINYIN
	}
	
	public static class SearchResult{
		private SearchResultField mResultField;
		private int mStart,mEnd;
		
		private SearchResult(){
			this.mResultField=SearchResultField.NONE;
			this.mStart=-1;
			this.mEnd=-1;
		}
		private SearchResult(SearchResultField field,int start,int end){
			this.mResultField=field;
			this.mStart=start;
			this.mEnd=end;
		}
		
		public int getStart(){
			return this.mStart;
		}
		
		public int getEnd(){
			return this.mEnd;
		}
		
		public SearchResultField getResultField(){
			return this.mResultField;
		}
	}
}
