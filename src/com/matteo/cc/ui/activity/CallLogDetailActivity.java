package com.matteo.cc.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.CallLogInfo;
import com.matteo.cc.entity.utils.CallLogUtil;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.ui.view.TitleView;
import com.matteo.cc.ui.view.xlistview.XListView;
import com.matteo.cc.utils.SipUtils;
import com.matteo.cc.utils.ToastUtils;
import com.matteo.cc.utils.XTimeUtils;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class CallLogDetailActivity extends BaseActivity implements
		OnClickListener {
	static final String ID_KEY = "id";
	static final int CALLLOG_MAX_COUNT=10;
	@ViewInject(R.id.headerTitle)
	TitleView headerTitle;
	@ViewInject(R.id.tvName)
	TextView tvName;
	@ViewInject(R.id.tvNumber)
	TextView tvNumber;
	@ViewInject(R.id.ivDial)
	ImageView ivDial;
	@ViewInject(R.id.itemThisCallLog)
	CallLogDetailItem itemThisCallLog;
	@ViewInject(R.id.lvRecentCallLogs)
	XListView lvRecentCallLogs;
	
	private CallLogInfo mCallLog;
	private List<CallLogInfo> mRelativeCallLogs;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_calllog_detail);
		ViewUtils.inject(this);
		this.init();
	}
	
	private void init(){
		this.mRelativeCallLogs=new ArrayList<CallLogInfo>();
		List<CallLogInfo> calllogs=ContentManager.get().getCallLogs();
		int id=this.getIntent().getIntExtra(ID_KEY, 0);
		for (CallLogInfo cl : calllogs) {
			if(cl.mId==id){
				this.mCallLog=cl;
				break;
			}
		}
		for (CallLogInfo cl : calllogs) {
			if(cl.mNumber.equals(this.mCallLog.mNumber)){
				this.mRelativeCallLogs.add(cl);
				if(this.mRelativeCallLogs.size()>=CALLLOG_MAX_COUNT){
					break;
				}
			}
		}
		
		if(TextUtils.isEmpty(this.mCallLog.mName)){
			this.tvName.setText(R.string.anonymous);
		}else{
			this.tvName.setText(this.mCallLog.mName);
		}
		this.tvNumber.setText(this.mCallLog.mNumber);
		this.ivDial.setOnClickListener(this);
		
		this.itemThisCallLog.setCallLog(this.mCallLog);
		this.lvRecentCallLogs.setAdapter(new CallLogsAdapter());
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivDial:
			this.dial(this.mCallLog.mNumber);
			break;

		default:
			break;
		}
	}

	private void dial(String number) {
		if (TextUtils.isEmpty(number)) {
			ToastUtils.show(R.string.warning_number_cannot_be_empty);
			return;
		}
		SipUtils.makeCall(this, number);
	}


	public static void startActivity(Context context, int callLogId) {
		Intent intent = new Intent(context, CallLogDetailActivity.class);
		intent.putExtra(ID_KEY, callLogId);
		context.startActivity(intent);
	}
	
	public static class CallLogDetailItem extends LinearLayout{
		private Context mContext;
		
		@ViewInject(R.id.tvDay)
		private TextView tvDay;
		@ViewInject(R.id.tvTime)
		private TextView tvTime;
		@ViewInject(R.id.tvType)
		private TextView tvType;
		@ViewInject(R.id.tvDuration)
		private TextView tvDuration;
		
		private CallLogInfo mCallLog;

		public CallLogDetailItem(Context context) {
			super(context);
			this.init(context);
		}
		
		public CallLogDetailItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.init(context);
		}
		
		private void init(Context context){
			this.mContext=context;
			View.inflate(this.mContext, R.layout.layout_calllog_detail_item, this);
			ViewUtils.inject(this);
			this.setOrientation(LinearLayout.VERTICAL);
		}
		
		public void setCallLog(CallLogInfo callLog){
			this.mCallLog=callLog;
			this.tvDay.setText(XTimeUtils.formatDay(this.mCallLog.mCallDate));
			this.tvTime.setText(XTimeUtils.formatTime(this.mCallLog.mCallDate));
			this.tvDuration.setText(XTimeUtils.formatDuration(this.mCallLog.mDuration));
			this.tvType.setText(CallLogUtil.getTypeInfo(this.mCallLog.mType));
			int colorRed=this.getResources().getColor(R.color.red);
			int colorFontGray=this.getResources().getColor(R.color.font_gray);
			if(this.mCallLog.mType==CallLogInfo.MISSED_TYPE){
				this.tvTime.setTextColor(colorRed);
				this.tvType.setTextColor(colorRed);
				this.tvDuration.setTextColor(colorRed);
			}else{
				this.tvTime.setTextColor(colorFontGray);
				this.tvType.setTextColor(colorFontGray);
				this.tvDuration.setTextColor(colorFontGray);
			}
		}
		
		public void setCallLog(List<CallLogInfo> callLogs,int position){
			CallLogInfo callLog=callLogs.get(position);
			this.setCallLog(callLog);
			boolean showTvDay=true;
			if(position>0){
				CallLogInfo lastCallLog=callLogs.get(position-1);
				if(XTimeUtils.isSameDay(lastCallLog.mCallDate, callLog.mCallDate)){
					showTvDay=false;
				}
			}
			this.tvDay.setVisibility(showTvDay?View.VISIBLE:View.GONE);
		}
		
	}
	
	class CallLogsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mRelativeCallLogs.size();
		}

		@Override
		public Object getItem(int position) {
			return mRelativeCallLogs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CallLogDetailItem item=null;
			if(convertView==null){
				item=new CallLogDetailItem(CallLogDetailActivity.this);
				convertView=item;
			}else{
				item=(CallLogDetailItem)convertView;
			}
			item.setCallLog(mRelativeCallLogs,position);
			return convertView;
		}
		
	}
}
