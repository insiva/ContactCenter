package com.matteo.cc.ui.activity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.SmsInfo;
import com.matteo.cc.entity.SmsInfo.SmsThreadInfo;
import com.matteo.cc.entity.utils.SmsUtil;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.ui.view.TitleView;
import com.matteo.cc.ui.view.xlistview.XListView;
import com.matteo.cc.ui.view.TitleView.OnTitleClickedListener;
import com.matteo.cc.utils.XTimeUtils;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class SmsThreadDetailActivity extends BaseActivity implements OnTitleClickedListener {
	static final String ID_KEY="id";
	
	@ViewInject(R.id.headerTitle)
	TitleView headerTitle;
	@ViewInject(R.id.etBody)
	EditText etBody;
	@ViewInject(R.id.btnSend)
	Button btnSend;
	@ViewInject(R.id.lvMsgs)
	XListView lvMsgs;
	
	private SmsThreadInfo mSmsThread;
	private MsgsAdapter mMsgsAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_sms_thread_detail);
		ViewUtils.inject(this);
		int id=this.getIntent().getIntExtra(ID_KEY, 0);
		this.mSmsThread=SmsUtil.getSmsThreadById(id, ContentManager.get().getSmsThreads());
		this.headerTitle.setTitle(this.mSmsThread.getDisplayName());
		if(this.mSmsThread.getContact()!=null){
			this.headerTitle.setActionIcon(R.drawable.contact_default_avatar);
			this.headerTitle.setOnTitleClickedListener(this);
		}
		this.mMsgsAdapter=new MsgsAdapter();
		this.lvMsgs.setAdapter(this.mMsgsAdapter);
		this.lvMsgs.setSelection(this.mMsgsAdapter.getCount()-1);
	}
	
	public static void startActivity(Context context,int id){
		Intent intent=new Intent(context,SmsThreadDetailActivity.class);
		intent.putExtra(ID_KEY, id);
		context.startActivity(intent);
	}

	@Override
	public void onReturnClicked() {
	}

	@Override
	public void onActionClicked() {
		ContactDetailActivity.startActivity(this, this.mSmsThread.getContact().mId);
	}
	
	class MsgItem extends LinearLayout{
		
		@ViewInject(R.id.tvDate)
		TextView tvDate;
		@ViewInject(R.id.tvSentBody)
		TextView tvSentBody;
		@ViewInject(R.id.tvRecvBody)
		TextView tvRecvBody;

		private SmsInfo mMsg;
		
		public MsgItem(Context context) {
			super(context);
			View.inflate(context, R.layout.layout_msg_item, this);
			ViewUtils.inject(this);
			this.setOrientation(LinearLayout.VERTICAL);
			this.setPadding(20, 0, 20, 0);
		}
		
		public void setMsg(SmsInfo msg){
			this.mMsg=msg;
			this.tvDate.setText(XTimeUtils.formatDateTime(this.mMsg.mDate));
			if(this.mMsg.isRecv()){
				this.tvRecvBody.setVisibility(View.VISIBLE);
				this.tvSentBody.setVisibility(View.GONE);
				this.tvRecvBody.setText(this.mMsg.mBody);
			}else{
				this.tvRecvBody.setVisibility(View.GONE);
				this.tvSentBody.setVisibility(View.VISIBLE);
				this.tvSentBody.setText(this.mMsg.mBody);
			}
		}
		
		public void setMsg(List<SmsInfo> msgs,int position){
			SmsInfo msg=msgs.get(position);
			this.setMsg(msg);
			boolean showDate=true;
			if(position>0){
				SmsInfo lastMsg=msgs.get(position-1);
				if(XTimeUtils.isSameMinute(lastMsg.mDate, msg.mDate)){
					showDate=false;
				}
			}
			this.tvDate.setVisibility(showDate?View.VISIBLE:View.GONE);
		}
		
	}
	
	class MsgsAdapter extends BaseAdapter{

		private List<SmsInfo> mMsgs;
		
		public MsgsAdapter(){
			this.mMsgs=new ArrayList<SmsInfo>(mSmsThread);
			Collections.reverse(this.mMsgs);
		}
		
		@Override
		public int getCount() {
			return this.mMsgs.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mMsgs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MsgItem item=null;
			if(convertView==null){
				item=new MsgItem(SmsThreadDetailActivity.this);
				convertView=item;
			}else{
				item=(MsgItem)convertView;
			}
			item.setMsg(this.mMsgs, position);
			return convertView;
		}
		
	}
}
