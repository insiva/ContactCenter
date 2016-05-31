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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteo.cc.Config;
import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.CallLogInfo;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.ui.view.DialPad;
import com.matteo.cc.ui.view.XListView;
import com.matteo.cc.ui.view.DialPad.OnClickDialPadListener;
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
	
	private OnClickDialPadListener mOnClickDialPadListener;

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

	}

	@Override
	public void onNumberChanged(String oldNumber, String newNumber) {

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
				//this.tvDate.setTextColor(COLOR_MISSED_CALL);
			}else{
				this.tvPerson.setTextColor(COLOR_PERSON);
				//this.tvDate.setTextColor(COLOR_DATE);
			}
			this.ivDirect.setVisibility(this.mCallLog.mType==CallLogInfo.OUTGOING_TYPE?View.VISIBLE:View.INVISIBLE);
			this.tvDate.setText(this.mCallLog.getDisplayDate());
		}

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.ivDetail){
				
			}else{
				
			}
		}
	}
}
