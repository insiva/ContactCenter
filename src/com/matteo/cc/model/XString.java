package com.matteo.cc.model;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private Pinyin mPinyin;

	public XString(String value, String sortKey) {
		this.mValue = value;
		this.mPinyin = new Pinyin(sortKey);
	}
	
	public XString(String value){
		this(value, value);
	}

	@Override
	public String toString() {
		return this.mValue;
	}
	
	public String toPinyin(){
		return this.mPinyin.toString();
	}

	static class PinyinUnit {
		String mValue;
		String mPinyin;
		String mPinyinNumbers;
		boolean mIsPinyin;

		PinyinUnit(String value, String pinyin, boolean isPinyin) {
			this.mValue = value;
			this.mPinyin = pinyin;
			this.mIsPinyin = isPinyin;
			StringBuilder sb = new StringBuilder();
			char[] cs = this.mPinyin.toCharArray();
			for (char c : cs) {
				char lowerC=toLower(c);
				if (CharacterDic.containsKey(lowerC)) {
					sb.append(CharacterDic.get(lowerC));
				} else {
					sb.append(c);
				}
			}
			this.mPinyinNumbers = sb.toString();
		}

		@Override
		public String toString() {
			return this.mPinyin;
		}
	}

	static class Pinyin {
		List<PinyinUnit> mPinyinUnitList;
		List<String> mStringUnitList;
		private String mPinyinValue;

		Pinyin(String sortKey) {
			this.mPinyinUnitList = new ArrayList<PinyinUnit>();
			this.mStringUnitList = new ArrayList<String>();
			this.parseSortKey(sortKey);
		}

		private void parseSortKey(String sortKey) {
			char[] cs = sortKey.toCharArray();
			int l = cs.length;
			StringBuilder sbPinyinUnit = new StringBuilder();
			StringBuilder sbStringUnit = new StringBuilder();
			for (int i = 0; i < l; i++) {
				char c = cs[i];
				if (!isCharacter(c)) {
					for (int j = i - 1; j >= 0; j--) {
						sbStringUnit.deleteCharAt(sbStringUnit.length() - 1);
						char c1 = cs[j];
						if (c1 != ' ') {
							sbPinyinUnit.append(toLower(c1));
						}else{//c1==' '
						}
						if((c1==' '&&j!=(i-1))||j==0){
							String unit = sbStringUnit.toString();
							if(!TextUtils.isEmpty(unit)){
								this.mPinyinUnitList.add(new PinyinUnit(unit, unit,
										false));
								this.mStringUnitList.add(unit);
							}
							this.mPinyinUnitList.add(new PinyinUnit(String.valueOf(c), sbPinyinUnit.reverse().toString(), true));
							this.mStringUnitList.add(String.valueOf(c));
							sbPinyinUnit=new StringBuilder();
							sbStringUnit=new StringBuilder();
							break;
						}
					}
				}else{
					sbStringUnit.append(c);
				}
			}
			String unit=sbStringUnit.toString();
			if(!TextUtils.isEmpty(unit)){
				this.mPinyinUnitList.add(new PinyinUnit(unit, unit,
						false));
				this.mStringUnitList.add(unit);
			}
		}
		
		@Override
		public String toString() {
			if(TextUtils.isEmpty(this.mPinyinValue)){
				StringBuilder sb=new StringBuilder();
				for (PinyinUnit pinyinUnit : mPinyinUnitList) {
					sb.append(pinyinUnit.toString());
				}
				this.mPinyinValue=sb.toString();
			}
			return this.mPinyinValue;
		} 
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
}
