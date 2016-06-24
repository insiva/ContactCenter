package com.matteo.cc.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.matteo.cc.Constant;
import com.matteo.cc.R;
import com.matteo.cc.ui.activity.dialog.WaitingDialog;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.utils.PreferenceUtils;
import com.matteo.cc.utils.ToastUtils;
import com.matteo.cc.utils.task.JsonTask;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class LoginActivity extends BaseActivity implements OnClickListener{
	public static final String URL_LOGIN = Constant.HOST
			+ "login.aspx?agentid=%s&password=%s";
	
	@ViewInject(R.id.btnLogin)
	private Button btnLogin;
	@ViewInject(R.id.etUserId)
	private EditText etUserId;
	@ViewInject(R.id.etPassword)
	private EditText etPassword;
	
	private String mUserId,mPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		this.btnLogin.setOnClickListener(this);
	}
	
	public static void startActivity(Context context){
		Intent intent=new Intent(context,LoginActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			this.login();
			break;

		default:
			break;
		}
	}
	
	private void login(){
		this.mUserId=this.etUserId.getText().toString();
		if(TextUtils.isEmpty(this.mUserId)){
			ToastUtils.show(R.string.please_input_user_id);
			return;
		}
		this.mPassword=this.etPassword.getText().toString();
		if(TextUtils.isEmpty(this.mPassword)){
			ToastUtils.show(R.string.please_input_password);
			return;
		}
		(new LoginTask()).execute();
	}
	
	class LoginTask extends JsonTask{
		
		public LoginTask(){
			this.mUrl=String.format(URL_LOGIN, mUserId,mPassword);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitingDialog.startActivity(LoginActivity.this);
		}
		
		@Override
		protected void doInBackground() {
			super.doInBackground();
		}
		
		@Override
		protected void onPostExecute() {
			super.onPostExecute();
			if(this.mStatus==Constant.STATUS_OK){
				LoginActivity.this.finish();
				MainActivity.startActivity(LoginActivity.this);
				PreferenceUtils.put(Constant.TOKEN, this.mData.optString(Constant.TOKEN));
				PreferenceUtils.put(com.matteo.cc.sip.constant.Constant.SIP_ACCOUNT_KEY, this.mData.optJSONObject("agent").toString());
			}else{
				ToastUtils.show(this.mInfo);
			}
			WaitingDialog.closeActivity(LoginActivity.this);
		}
	}
}
