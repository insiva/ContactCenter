package com.matteo.cc.ui.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.SmsInfo.SmsThreadInfo;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.ui.view.XListView;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class SmsFragment extends BaseFragment{

	@ViewInject(R.id.lvSms)
	XListView lvSms;
	
	private SmsThreadAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment=View.inflate(this.getActivity(), R.layout.fragment_sms, null);
		ViewUtils.inject(this, fragment);
		this.init();
		return fragment;
	}
	
	private void init(){
		this.mAdapter=new SmsThreadAdapter();
		this.lvSms.setAdapter(this.mAdapter);
	}
	
	class SmsThreadAdapter extends BaseAdapter{
		
		private List<SmsThreadInfo> mSmsThreads;

		public SmsThreadAdapter(){
			this.mSmsThreads=ContentManager.get().getSmsThreads();
		}
		
		@Override
		public int getCount() {
			return this.mSmsThreads.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mSmsThreads.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SmsThreadItem sti=null;
			if(convertView==null){
				sti=new SmsThreadItem(getActivity());
				convertView=sti;
			}else{
				sti=(SmsThreadItem)convertView;
			}
			sti.setSmsThread(this.mSmsThreads.get(position));
			return convertView;
		}
		
	}
	
	class SmsThreadItem extends RelativeLayout implements OnClickListener{

		@ViewInject(R.id.tvName)
		private TextView tvName;
		@ViewInject(R.id.tvDate)
		private TextView tvDate;
		@ViewInject(R.id.tvBody)
		private TextView tvBody;
		
		private SmsThreadInfo mSmsThread;
		
		public SmsThreadItem(Context context) {
			super(context);
			View.inflate(getActivity(), R.layout.layout_sms_thread_item, this);
			ViewUtils.inject(this);
			this.setOnClickListener(this);
			this.setBackgroundResource(R.drawable.default_selector);
			this.setClickable(true);
			this.setPadding(10, 16, 10, 16);
		}
		
		public void setSmsThread(SmsThreadInfo smsThread) {
			this.mSmsThread = smsThread;
			if(TextUtils.isEmpty(this.mSmsThread.mNewestSms.mPersonName)){
				this.tvName.setText(this.mSmsThread.mNewestSms.mNumber);
			}else{
				this.tvName.setText(this.mSmsThread.mNewestSms.mPersonName);
			}
			this.tvDate.setText(this.mSmsThread.mNewestSms.getDisplayDate());
			this.tvBody.setText(this.mSmsThread.mNewestSms.mBody);
		}

		@Override
		public void onClick(View v) {
			
		}
	}
}
