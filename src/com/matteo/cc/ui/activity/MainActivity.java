package com.matteo.cc.ui.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.matteo.cc.Config;
import com.matteo.cc.Constant;
import com.matteo.cc.R;
import com.matteo.cc.sip.service.ISip;
import com.matteo.cc.sip.service.SipService;
import com.matteo.cc.ui.base.BaseActivity;
import com.matteo.cc.ui.fragment.ContactFragment;
import com.matteo.cc.ui.fragment.DialFragment;
import com.matteo.cc.ui.fragment.SettingFragment;
import com.matteo.cc.ui.fragment.SmsFragment;
import com.matteo.cc.ui.view.DialPad.OnClickDialPadListener;
import com.matteo.cc.ui.view.MainBottomBar;
import com.matteo.cc.ui.view.TitleView;
import com.matteo.cc.ui.view.MainBottomBar.OnTitleIndexChangedListener;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class MainActivity extends BaseActivity implements
		OnTitleIndexChangedListener,OnPageChangeListener,OnClickDialPadListener {
	static final int FRAGMENT_COUNT = Constant.MODULE_COUNT;
	static final int[] TITLE_RES_IDS={R.string.dial,R.string.contact,R.string.sms,R.string.setting};

	@ViewInject(R.id.mbbFooter)
	private MainBottomBar mbbFooter;
	@ViewInject(R.id.headerTitle)
	private TitleView headerTitle;
	@ViewInject(R.id.vpFragments)
	private ViewPager vpFragments;

	private int mCurrFragmentIndex = 0;
	private boolean mIsDialPadShow = false;

	private DialFragment fragDial;
	private ContactFragment fragContact;
	private SmsFragment fragSms;
	private SettingFragment fragSetting;

	private Fragment[] mFragments;
	
	private ServiceConnection mSipServiceConn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			//mSipService=null;
			Config.setSipService(null);
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ISip sipService=ISip.Stub.asInterface(service);
			//mHandler.sendEmptyMessage(WHAT_SERVICE_CONNECTED);
			Config.setSipService(sipService);
			Intent broadcastIntent=new Intent(Constant.Action.SIP_SERVICE_CONNECTED);
			MainActivity.this.sendBroadcast(broadcastIntent);
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_main);
		SipService.startService(this);
		this.bindSipService();
		ViewUtils.inject(this);
		this.init();
	}
	
	private void bindSipService(){
		Intent intent=new Intent();
		intent.setAction(com.matteo.cc.sip.constant.Constant.Action.SIP_SERVICE);
		intent.setPackage(Constant.PACKAGE_NAME);
		this.bindService(intent, this.mSipServiceConn, Service.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unbindService(this.mSipServiceConn);
		SipService.stopService(this);
	}

	private void init() {
		this.mbbFooter.setOnTitleIndexChangedListener(this);
		int index = this.mbbFooter.getIndex();
		this.initFragments();
		this.setFragmentIndex(index);
	}

	private void initFragments() {
		this.mFragments = new Fragment[FRAGMENT_COUNT];

		this.fragDial = new DialFragment();
		this.mFragments[0] = this.fragDial;
		this.fragDial.setOnClickDialPadListener(this);

		this.fragContact = new ContactFragment();
		this.mFragments[1] = this.fragContact;

		this.fragSms = new SmsFragment();
		this.mFragments[2] = this.fragSms;

		this.fragSetting = new SettingFragment();
		this.mFragments[3] = this.fragSetting;
		
		this.vpFragments.setAdapter(new MyFragmentAdapter(this.getSupportFragmentManager()));
		this.vpFragments.setOnPageChangeListener(this);
		this.vpFragments.setOffscreenPageLimit(FRAGMENT_COUNT);
		this.headerTitle.setTitle(TITLE_RES_IDS[this.mCurrFragmentIndex]);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public static void startActivity(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	private void setFragmentIndex(int index) {
		if (index != this.mCurrFragmentIndex) {
			this.mCurrFragmentIndex = index;
			if (this.mCurrFragmentIndex == 0 && this.mIsDialPadShow) {
				this.mbbFooter.setVisibility(View.GONE);
			} else {
				this.mbbFooter.setVisibility(View.VISIBLE);
			}
			this.vpFragments.setCurrentItem(this.mCurrFragmentIndex);
		}
	}

	@Override
	public void onTitleIndexChanged(int oldIndex, int newIndex) {
		this.setFragmentIndex(newIndex);
	}

	@Override
	public void onTitleDialUpClick() {
		this.mIsDialPadShow = true;
		this.mbbFooter.setVisibility(View.GONE);
		this.fragDial.showDialPad();
	}
	
	class MyFragmentAdapter extends FragmentPagerAdapter{

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			return MainActivity.this.mFragments[i];
		}

		@Override
		public int getCount() {
			return FRAGMENT_COUNT;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int i) {
		this.mCurrFragmentIndex=i;
		if (this.mCurrFragmentIndex == 0 && this.mIsDialPadShow) {
			this.mbbFooter.setVisibility(View.GONE);
		} else {
			this.mbbFooter.setVisibility(View.VISIBLE);
		}
		this.mbbFooter.setTitleIndex(this.mCurrFragmentIndex);
		this.headerTitle.setTitle(TITLE_RES_IDS[this.mCurrFragmentIndex]);
	}

	@Override
	public void onTitleDialDownClick() {
		this.mIsDialPadShow=false;
		this.mbbFooter.setVisibility(View.VISIBLE);
	}

	@Override
	public void onTitleContactClick() {
		this.setFragmentIndex(1);
	}

	@Override
	public void onDial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNumberChanged(String oldNumber, String newNumber) {
		
	}
}
