package com.matteo.cc.ui.view;

import com.matteo.cc.R;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleView extends RelativeLayout implements OnClickListener {

	@ViewInject(R.id.tvTitle)
	TextView tvTitle;
	@ViewInject(R.id.ivReturn)
	ImageView ivReturn;
	@ViewInject(R.id.ivState)
	ImageView ivState;
	@ViewInject(R.id.ivAction)
	ImageView ivAction;

	private boolean mShowReturnIcon;
	private boolean mShowStateIcon;
	private OnTitleClickedListener mOnTitleClickedListener;
	private int mTitleResId = 0;
	private int mIvActionResId=0;
	private String mTitle = null;

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.matteo); 
		this.mShowReturnIcon=a.getBoolean(R.styleable.matteo_showReturn, false);
		this.mShowStateIcon=a.getBoolean(R.styleable.matteo_showState, false);
		this.mTitleResId=a.getResourceId(R.styleable.matteo_text, 0);
		a.recycle();
		View.inflate(context, R.layout.layout_title, this);
		ViewUtils.inject(this);
		this.setBackgroundColor(this.getResources().getColor(R.color.green));
		this.ivReturn.setOnClickListener(this);
		this.ivAction.setOnClickListener(this);
		this.onSettingChanged();
	}
	
	private void onSettingChanged(){
		this.ivReturn.setVisibility(this.mShowReturnIcon ? View.VISIBLE
				: View.GONE);
		this.ivState.setVisibility(this.mShowStateIcon ? View.VISIBLE
				: View.GONE);
		if (TextUtils.isEmpty(this.mTitle)) {
			if (this.mTitleResId > 0) {
				this.tvTitle.setText(this.mTitleResId);
			}
		} else {
			this.tvTitle.setText(this.mTitle);
		}
		if(this.mIvActionResId>0){
			this.ivAction.setVisibility(View.VISIBLE);
			this.ivAction.setImageResource(this.mIvActionResId);
			this.ivState.setVisibility(View.GONE);
		}else{
			this.ivAction.setVisibility(View.GONE);
			this.ivState.setVisibility(View.VISIBLE);
		}
	}

	public void setReturnIconVisible(boolean visible) {
		this.mShowReturnIcon = visible;
		this.onSettingChanged();
	}

	public void setStateIconVisible(boolean visible) {
		this.mShowStateIcon = visible;
		this.onSettingChanged();
	}
	
	public void setActionIcon(int resId){
		this.mIvActionResId=resId;
		this.onSettingChanged();
	}

	public void setOnTitleClickedListener(OnTitleClickedListener listener) {
		this.mOnTitleClickedListener = listener;
	}

	public void setTitle(int resId) {
		if (this.tvTitle != null){
			this.tvTitle.setText(resId);
		}else{
			this.mTitleResId=resId;
		}
	}

	public void setTitle(String text) {
		if (this.tvTitle != null){
			this.tvTitle.setText(text);
		}else{
			this.mTitle=text;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivReturn:
			if (this.mOnTitleClickedListener != null) {
				this.mOnTitleClickedListener.onReturnClicked();
			}
			if(this.getContext() instanceof Activity){
				((Activity)(this.getContext())).finish();
			}
			break;
		case R.id.ivAction:
			if(this.mOnTitleClickedListener!=null){
				this.mOnTitleClickedListener.onActionClicked();
			}
			break;
		default:
			break;
		}
	}

	public static interface OnTitleClickedListener {
		public void onReturnClicked();
		public void onActionClicked();
	}
}
