package com.matteo.cc.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.ContactInfo;
import com.matteo.cc.entity.utils.ContactUtil;
import com.matteo.cc.ui.fragment.ContactFragment.CatalogAdapter;
import com.matteo.cc.ui.fragment.ContactFragment.CatalogView;
import com.matteo.cc.ui.fragment.ContactFragment.ContactItem;
import com.matteo.cc.ui.view.XListView;

public class ContactFragmentAdapter  extends BaseAdapter implements CatalogAdapter {


	private List<ContactInfo> mContacts;
	private char[] mCatalogs;
	private boolean mIsSearchResult;
	private CatalogView mCatalogView;
	private Context mContext;
	private XListView mListView;
	
	public ContactFragmentAdapter(Context context){
		this.mContext=context;
		this.mIsSearchResult=false;
		this.init(ContentManager.get().getContacts());
	}
	
	private void init(List<ContactInfo> contacts){
		this.mContacts=contacts;
		List<Character> catalogList=new ArrayList<Character>();
		Character lastCatalog=null;
		for (ContactInfo contact : this.mContacts) {
			if(lastCatalog==null||lastCatalog!=contact.mFirstChar){
				lastCatalog=contact.mFirstChar;
				catalogList.add(lastCatalog);
			}
		}
		int i=0;
		this.mCatalogs=new char[catalogList.size()];
		for (Character catalog : catalogList) {
			this.mCatalogs[i++]=catalog;
		}
		if(this.mCatalogView!= null&&this.showCatalog()){
			this.mCatalogView.initCatalogs();
		}
	}
	
	public void setCatalogView(CatalogView view) {
		this.mCatalogView = view;
	}

	public void setListView(XListView listView) {
		this.mListView = listView;
	}
	@Override
	public int getCount() {
		return this.mContacts.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactItem ci=null;
		if(convertView==null){
			ci=new ContactItem(this.mContext,this);
			convertView=ci;
		}else{
			ci=(ContactItem)convertView;
		}
		ci.setContact(this.mContacts, position,this.mIsSearchResult);
		return convertView;
	}

	@Override
	public void onCatalogSelected(char catalog) {
		int i=0;
		boolean equaled=false;
		for (ContactInfo contact : this.mContacts) {
			if(contact.mFirstChar==catalog){
				equaled=true;
				break;
			}
			i++;
		}
		if(equaled){
			//ContactFragment.this.lvContacts.setSelection(i);
			this.mListView.setSelection(i);
		}
	}

	@Override
	public char[] getCatalogs() {
		return this.mCatalogs;
	}
	
	public void search(String key){
		if(TextUtils.isEmpty(key)){
			this.mIsSearchResult=false;
			this.init(ContentManager.get().getContacts());
		}else{
			this.mIsSearchResult=true;
			List<ContactInfo> contacts=ContactUtil.search(key, ContentManager.get().getContacts());
			this.init(contacts);
		}
		this.notifyDataSetChanged();
	}
	
	public void searchByInteger(String key){
		if(TextUtils.isEmpty(key)){
			this.mIsSearchResult=false;
			this.init(ContentManager.get().getContacts());
		}else{
			this.mIsSearchResult=true;
			List<ContactInfo> contacts=ContactUtil.searchByInteger(key, ContentManager.get().getContacts());
			this.init(contacts);
		}
		this.notifyDataSetChanged();
	}
	
	public boolean showCatalog(){
		return true;
	}
	
	public boolean showSearchEt(){
		return true;
	}
	


}
