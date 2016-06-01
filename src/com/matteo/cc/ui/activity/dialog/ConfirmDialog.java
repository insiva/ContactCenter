package com.matteo.cc.ui.activity.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteo.cc.Constant;
import com.matteo.cc.R;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class ConfirmDialog extends BaseActivity implements OnClickListener{
	
	@ViewInject(R.id.tvTitle)
	TextView tvTitle;
	@ViewInject(R.id.tvConfirm)
	TextView tvConfirm;
	@ViewInject(R.id.tvCancel)
	TextView tvCancel;
	@ViewInject(R.id.rlDialog)
	RelativeLayout rlDialog;
	
	static final String TITLE_KEY="title";
	private String mTitle;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.dialog_confirm);
		ViewUtils.inject(this);
		this.mTitle=this.getIntent().getStringExtra(TITLE_KEY);
		this.tvTitle.setText(this.mTitle);
		this.tvConfirm.setOnClickListener(this);
		this.tvCancel.setOnClickListener(this);
		this.rlDialog.setVisibility(View.VISIBLE);
		this.rlDialog.startAnimation(AnimationUtils.loadAnimation(this, R.anim.footer_appear));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvConfirm:
			this.clickConfirm();
			break;
		case R.id.tvCancel:
			this.clickCancel();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		this.clickCancel();
	}
	
	private void clickConfirm(){
		this.setResult(Constant.STATUS_OK);
		this.finish();
	}
	
	private void clickCancel(){
		this.setResult(Constant.STATUS_ERROR_DEFAULT);
		this.finish();
	}

	public static void startActivity(Activity activity,int requestCode,String title){
		Intent intent=new Intent(activity,ConfirmDialog.class);
		intent.putExtra(TITLE_KEY, title);
		activity.startActivityForResult(intent, requestCode);
	}

	public static void startActivity(Activity activity,int requestCode,int titleResId){
		ConfirmDialog.startActivity(activity, requestCode, activity.getResources().getString(titleResId));
	}
	
	public static void startActivity(Fragment fragment,int requestCode,String title){
		Intent intent=new Intent(fragment.getActivity(),ConfirmDialog.class);
		intent.putExtra(TITLE_KEY, title);
		fragment.startActivityForResult(intent, requestCode);
	}

	public static void startActivity(Fragment fragment,int requestCode,int titleResId){
		ConfirmDialog.startActivity(fragment, requestCode, fragment.getResources().getString(titleResId));
	}
}
