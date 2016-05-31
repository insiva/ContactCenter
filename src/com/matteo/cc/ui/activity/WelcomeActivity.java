package com.matteo.cc.ui.activity;

import com.matteo.cc.Constant;
import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.utils.PreferenceUtils;
import com.matteo.cc.utils.task.AbsAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

public class WelcomeActivity extends Activity {
	
	static final long WAITING_DURATION=1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		(new ReadAllContentsTask()).execute();
	}
	
	private void onReadAllContentsCompleted(){
		String token=PreferenceUtils.get(Constant.TOKEN);
		if(TextUtils.isEmpty(token)){
			LoginActivity.startActivity(this);
		}else{
			MainActivity.startActivity(this);
		}
		this.finish();
	}
	
	class ReadAllContentsTask extends AbsAsyncTask{
		
		@Override
		protected void doInBackground() {
			long t1=System.currentTimeMillis();
			ContentManager.get().readAllContents(WelcomeActivity.this);
			long duration=System.currentTimeMillis()-t1;
			if(duration<WAITING_DURATION){
				try {
					Thread.sleep(WAITING_DURATION-duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		protected void onPostExecute() {
			WelcomeActivity.this.onReadAllContentsCompleted();
		}
	}
}
