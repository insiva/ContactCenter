package com.matteo.cc.ui.fragment;

import java.util.List;

import android.view.View.OnClickListener;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteo.cc.Config;
import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.CallLogInfo;
import com.matteo.cc.ui.activity.CallLogDetailActivity;
import com.matteo.cc.ui.adapter.ContactFragmentAdapter;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.ui.view.DialPad;
import com.matteo.cc.ui.view.XListView;
import com.matteo.cc.ui.view.DialPad.OnClickDialPadListener;
import com.matteo.cc.utils.SipUtils;
import com.matteo.cc.utils.ToastUtils;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class DialFragment extends BaseFragment implements
		OnClickDialPadListener {

	@ViewInject(R.id.llContent)
	MyLinearLayout llContent;
	@ViewInject(R.id.dpPad)
	DialPad dpPad;
	@ViewInject(R.id.lvCallLogs)
	XListView lvCallLogs;
	@ViewInject(R.id.flContact)
	FrameLayout flContact;
	
	private OnClickDialPadListener mOnClickDialPadListener;
	private ContactFragment fragContact;
	private ContactAdapter mContactAdapter;
	private String mNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = View.inflate(this.getActivity(),
				R.layout.fragment_dial, null);
		ViewUtils.inject(this, fragment);
		this.init();
		return fragment;
	}

	private void init() {
		this.dpPad.setOnClickDialPadListener(this);
		this.llContent.setDialFragment(this);
		this.lvCallLogs.setAdapter(new CallLogsAdapter());
		//this.lvCallLogs.setVisibility(View.GONE);
		this.fragContact=new ContactFragment();
		this.mContactAdapter=new ContactAdapter(this.getActivity());
		this.fragContact.setAdapter(this.mContactAdapter);
		this.getFragmentManager().beginTransaction().add(R.id.flContact, this.fragContact).commit();
		this.flContact.setVisibility(View.GONE);
	}

	public void setOnClickDialPadListener(OnClickDialPadListener listener) {
		this.mOnClickDialPadListener = listener;
	}

	public void showDialPad() {
		this.dpPad.setVisibility(View.VISIBLE);
	}

	public void hideDialPad() {
		this.dpPad.setVisibility(View.GONE);
	}

	@Override
	public void onTitleDialDownClick() {
		if (this.mOnClickDialPadListener != null) {
			this.hideDialPad();
			this.mOnClickDialPadListener.onTitleDialDownClick();
		}
	}

	@Override
	public void onTitleContactClick() {
		if (this.mOnClickDialPadListener != null) {
			this.mOnClickDialPadListener.onTitleContactClick();
		}
	}

	@Override
	public void onDial() {
		this.dial(this.mNumber);
	}
	
	private void dial(String number){
		if(TextUtils.isEmpty(number)){
			ToastUtils.show(R.string.warning_number_cannot_be_empty);
			return;
		}
		SipUtils.makeCall(this.getActivity(), number);
	}

	@Override
	public void onNumberChanged(String oldNumber, String newNumber) {
		if(TextUtils.isEmpty(newNumber)){
			this.lvCallLogs.setVisibility(View.VISIBLE);
			this.flContact.setVisibility(View.GONE);
		}else{
			this.lvCallLogs.setVisibility(View.GONE);
			this.flContact.setVisibility(View.VISIBLE);
		}
		this.mContactAdapter.searchByInteger(newNumber);
		this.mNumber=newNumber;
	}

	public static class MyLinearLayout extends LinearLayout {

		private DialFragment fragDial;

		public MyLinearLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public void setDialFragment(DialFragment frag) {
			this.fragDial = frag;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			if (this.fragDial.dpPad.getVisibility() == View.VISIBLE) {
				this.fragDial.onTitleDialDownClick();
				return true;
			}
			return super.onInterceptTouchEvent(ev);
		}
	}
	
	class CallLogsAdapter extends BaseAdapter{

		private List<CallLogInfo> mCallLogs;
		
		public CallLogsAdapter(){
			this.mCallLogs=ContentManager.get().getCallLogs();
		}
		
		@Override
		public int getCount() {
			return this.mCallLogs.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mCallLogs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CallLogItem callLogItem=null;
			if(convertView==null){
				callLogItem=new CallLogItem(getActivity());
				convertView=callLogItem;
			}else{
				callLogItem=(CallLogItem)convertView;
			}
			callLogItem.setCallLog(this.mCallLogs.get(position));
			return convertView;
		}
	}
	
	static int COLOR_MISSED_CALL=Config.getAppContext().getResources().getColor(R.color.red);
	static int COLOR_PERSON=Config.getAppContext().getResources().getColor(R.color.font_dark_gray);
	static int COLOR_DATE=Config.getAppContext().getResources().getColor(R.color.font_gray);
	
	class CallLogItem extends RelativeLayout implements OnClickListener{
		private CallLogInfo mCallLog;
		
		@ViewInject(R.id.ivDirect)
		private ImageView ivDirect;
		@ViewInject(R.id.tvPerson)
		private TextView tvPerson;
		@ViewInject(R.id.tvDate)
		private TextView tvDate;
		@ViewInject(R.id.ivDetail)
		private ImageView ivDetail;

		public CallLogItem(Context context) {
			super(context);
			View.inflate(context, R.layout.layout_calllog_item, this);
			ViewUtils.inject(this);
			this.setOnClickListener(this);
			this.ivDetail.setOnClickListener(this);
			this.setBackgroundResource(R.drawable.default_selector);
			this.setClickable(true);
		}
		
		public void setCallLog(CallLogInfo callLog){
			this.mCallLog=callLog;
			if(TextUtils.isEmpty(this.mCallLog.mName)){
				this.tvPerson.setText(this.mCallLog.mNumber);
			}else{
				this.tvPerson.setText(this.mCallLog.mName);
			}
			if(this.mCallLog.mType==CallLogInfo.MISSED_TYPE){
				this.tvPerson.setTextColor(COLOR_MISSED_CALL);
			}else{
				this.tvPerson.setTextColor(COLOR_PERSON);
			}
			this.ivDirect.setVisibility(this.mCallLog.mType==CallLogInfo.OUTGOING_TYPE?View.VISIBLE:View.INVISIBLE);
			this.tvDate.setText(this.mCallLog.getDisplayDate());
		}

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.ivDetail){
				CallLogDetailActivity.startActivity(getActivity(),this.mCallLog.mId);
			}else{
				DialFragment.this.dial(this.mCallLog.mNumber);
			}
		}
	}
	
	class ContactAdapter extends ContactFragmentAdapter{
		
		public ContactAdapter(Context context) {
			super(context);
		}

		@Override
		public boolean showCatalog() {
			return false;
		}
		
		@Override
		public boolean showSearchEt() {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.getResources().getString(R.string.dial);
	}
}
