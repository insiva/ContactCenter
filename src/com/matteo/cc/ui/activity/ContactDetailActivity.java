package com.matteo.cc.ui.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.ContactInfo;
import com.matteo.cc.entity.utils.ContactUtil;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.ui.view.TitleView;
import com.matteo.cc.ui.view.xlistview.XListView;
import com.matteo.cc.utils.SipUtils;
import com.matteo.cc.utils.ToastUtils;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class ContactDetailActivity extends BaseActivity{
	static final String ID_KEY="id";
	
	@ViewInject(R.id.tvName)
	TextView tvName;
	@ViewInject(R.id.lvNumbers)
	XListView lvNumbers;
	@ViewInject(R.id.headerTitle)
	TitleView headerTitle;
	
	private ContactInfo mContact;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_contact_detail);
		ViewUtils.inject(this);
		int id=this.getIntent().getIntExtra(ID_KEY, 0);
		this.mContact=ContactUtil.getContactById(id, ContentManager.get().getContacts());
		this.lvNumbers.setAdapter(new PhoneNumberAdapter());
		this.tvName.setText(this.mContact.mName.toString());
		this.headerTitle.setTitle(this.mContact.mName.toString());
	}


	private void dial(String number) {
		if (TextUtils.isEmpty(number)) {
			ToastUtils.show(R.string.warning_number_cannot_be_empty);
			return;
		}
		SipUtils.makeCall(this, number);
	}
	
	class PhoneNumberItem extends LinearLayout implements OnClickListener{
		
		@ViewInject(R.id.tvType)
		TextView tvType;
		@ViewInject(R.id.vLine)
		View vLine;
		@ViewInject(R.id.tvNumber)
		TextView tvNumber;
		@ViewInject(R.id.ivDial)
		ImageView ivDial;
		
		private Context mContext;
		private ContactInfo.PhoneNumber mPhoneNumber;

		public PhoneNumberItem(Context context) {
			super(context);
			this.mContext=context;
			View.inflate(this.mContext, R.layout.layout_contact_number_item, this);
			ViewUtils.inject(this);
			this.ivDial.setOnClickListener(this);
		}
		
		public void setPhoneNumber(ContactInfo.PhoneNumber phoneNumber){
			this.mPhoneNumber=phoneNumber;
			this.tvType.setText(ContactUtil.getTypeInfo(this.mPhoneNumber.getType()));
			this.tvNumber.setText(this.mPhoneNumber.mNumber);
		}
		
		public void setPhoneNumber(List<ContactInfo.PhoneNumber> phoneNumbers,int position){
			ContactInfo.PhoneNumber phoneNumber=phoneNumbers.get(position);
			this.setPhoneNumber(phoneNumber);
			boolean showType=true;
			if(position>0){
				ContactInfo.PhoneNumber lastPhoneNumber=phoneNumbers.get(position-1);
				if(lastPhoneNumber.getType()==phoneNumber.getType()){
					showType=false;
				}
			}
			this.tvType.setVisibility(showType?View.VISIBLE:View.GONE);
			this.vLine.setVisibility(showType?View.VISIBLE:View.GONE);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ivDial:
				ContactDetailActivity.this.dial(this.mPhoneNumber.mNumber);
				break;

			default:
				break;
			}
		}
	}
	
	class PhoneNumberAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mContact.mPhoneNumbers.size();
		}

		@Override
		public Object getItem(int position) {
return mContact.mPhoneNumbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PhoneNumberItem item=null;
			if(convertView==null){
				item=new PhoneNumberItem(ContactDetailActivity.this);
				convertView=item;
			}else{
				item=(PhoneNumberItem)convertView;
			}
			item.setPhoneNumber(mContact.mPhoneNumbers, position);
			return convertView;
		}
		
	}
	
	public static void startActivity(Context context,int id){
		Intent intent=new Intent(context,ContactDetailActivity.class);
		intent.putExtra(ID_KEY, id);
		context.startActivity(intent);
	}
}
