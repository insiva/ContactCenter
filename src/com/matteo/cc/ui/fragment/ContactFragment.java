package com.matteo.cc.ui.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.ContactInfo;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.ui.view.XListView;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class ContactFragment extends BaseFragment {

	@ViewInject(R.id.etKeyword)
	private EditText etKeyword;
	@ViewInject(R.id.lvContacts)
	private XListView lvContacts;
	@ViewInject(R.id.cvCatalog)
	private CatalogView cvCatalog;
	
	private ContactAdapter mContactAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment=View.inflate(this.getActivity(), R.layout.fragment_contact, null);
		ViewUtils.inject(this, fragment);
		this.init();
		return fragment;
	}
	
	private void init(){
		this.mContactAdapter=new ContactAdapter();
		this.lvContacts.setAdapter(this.mContactAdapter);
		this.cvCatalog.setOnCatalogSelectedListener(this.mContactAdapter);
	}
	
	class ContactAdapter extends BaseAdapter implements OnCatalogSelectedListener{
		
		private List<ContactInfo> mContacts;
		
		public ContactAdapter(){
			this.mContacts=ContentManager.get().getContacts();
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
				ci=new ContactItem(getActivity());
				convertView=ci;
			}else{
				ci=(ContactItem)convertView;
			}
			ci.setContact(this.mContacts, position);
			return convertView;
		}

		@Override
		public void onCatalogSelected(char catalog) {
			
		}
		
	}
	
	class ContactItem extends LinearLayout implements OnClickListener{
		
		@ViewInject(R.id.tvCatalog)
		private TextView tvCatalog;
		@ViewInject(R.id.tvName)
		private TextView tvName;
		@ViewInject(R.id.llContact)
		private LinearLayout llContact;
		@ViewInject(R.id.tvNumbers)
		private TextView tvNumbers;
		
		private ContactInfo mContact;
		
		public ContactItem(Context context) {
			super(context);
			View.inflate(getActivity(), R.layout.layout_contact_item, this);
			ViewUtils.inject(this);
			this.setOrientation(LinearLayout.VERTICAL);
			this.llContact.setOnClickListener(this);
		}
		
		public void setContact(List<ContactInfo> contacts,int position){
			this.mContact=contacts.get(position);
			if(position==0){
				this.tvCatalog.setVisibility(View.VISIBLE);
			}else{
				ContactInfo lastContact=contacts.get(position-1);
				if(lastContact.mFirstChar!=this.mContact.mFirstChar){
					this.tvCatalog.setVisibility(View.VISIBLE);
				}else{
					this.tvCatalog.setVisibility(View.GONE);
				}
			}
			this.tvCatalog.setText(String.valueOf(this.mContact.mFirstChar));
			this.tvName.setText(this.mContact.mName);
		}

		@Override
		public void onClick(View v) {
			
		}
	}
	
	public static class CatalogView extends View{
		
		private OnCatalogSelectedListener mOnCatalogSelectedListener;

		public CatalogView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		
		public void setOnCatalogSelectedListener(OnCatalogSelectedListener listener){
			this.mOnCatalogSelectedListener=listener;
		}
		
	}
	
	public static  interface OnCatalogSelectedListener{
		public void onCatalogSelected(char catalog);
	}
}
