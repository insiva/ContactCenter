package com.matteo.cc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.matteo.cc.Constant;
import com.matteo.cc.R;
import com.matteo.cc.ui.activity.LoginActivity;
import com.matteo.cc.ui.activity.dialog.ConfirmDialog;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.utils.PreferenceUtils;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class SettingFragment extends BaseFragment implements OnClickListener,SeekBar.OnSeekBarChangeListener {
	private  String SPEAKER_LEVEL_TEXT;
	private  String MIC_LEVEL_TEXT;
	private float mSpeakerLevel, mMicLevel;
	private static final int MAX_LEVEL=15;
	private  static final int MAX_PROGRESS=150;
	private static final int SUBDIVISION=MAX_PROGRESS/(MAX_LEVEL*2);
	
	static final int LOGOUT_REQUEST_CODE=0x032;

	@ViewInject(R.id.tvLogout)
	private TextView tvLogout;
	@ViewInject(R.id.tvSpeakerLevel)
	private TextView tvSpeakerLevel;
	@ViewInject(R.id.tvMicLevel)
	private TextView tvMicLevel;
	@ViewInject(R.id.sbSpeakerLevel)
	private SeekBar sbSpeakerLevel;
	@ViewInject(R.id.sbMicLevel)
	private SeekBar sbMicLevel;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = View.inflate(this.getActivity(),
				R.layout.fragment_setting, null);
		ViewUtils.inject(this, fragment);
		this.init();
		return fragment;
	}
	
	private void init(){
		this.SPEAKER_LEVEL_TEXT = getActivity().getResources()
				.getString(R.string.speaker_level_setting) + "(%1$,.1fdB)";
		this.MIC_LEVEL_TEXT= getActivity().getResources()
				.getString(R.string.mic_level_setting) + "(%1$,.1fdB)";
		this.mSpeakerLevel = 1.0f;
		this.mMicLevel = 1.0f;
		this.sbMicLevel.setMax(MAX_PROGRESS );
		this.sbSpeakerLevel.setMax( MAX_PROGRESS );
		this.sbMicLevel.setOnSeekBarChangeListener(this);
		this.sbSpeakerLevel.setOnSeekBarChangeListener(this);
		this.show();
		this.tvLogout.setOnClickListener(this);
	}
	
	private void startLogout(){
		ConfirmDialog.startActivity(this, LOGOUT_REQUEST_CODE, R.string.confirm_logout);
	}
	
	private void logout(){
		PreferenceUtils.put(Constant.TOKEN, null);
		LoginActivity.startActivity(this.getActivity());
		this.getActivity().finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==LOGOUT_REQUEST_CODE){
			if(resultCode==Constant.STATUS_OK){
				this.logout();
			}
		}
	}
	
	private void show() {
		this.sbMicLevel.setProgress(this.valueToProgressUnit(this.mMicLevel));
		this.sbSpeakerLevel.setProgress(this.valueToProgressUnit(this.mSpeakerLevel));
	}
	
	private int valueToProgressUnit(float val) {
		double dB = (10.0f * Math.log10(val));
		return (int) ((dB + MAX_LEVEL) * SUBDIVISION);
	}
/*
	private float progressUnitToValue(int pVal) {
		double dB = pVal / SUBDIVISION -MAX_LEVEL;
		return (float) Math.pow(10, dB / 10.0f);
	}
	*/
	private float progressUnitToDisplay(int pVal){
		return pVal/SUBDIVISION-MAX_LEVEL;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		float db=this.progressUnitToDisplay(progress);
		//float level=this.progressUnitToValue(progress);
		switch (seekBar.getId()) {
		case R.id.sbMicLevel:
			this.tvMicLevel.setText(String.format(MIC_LEVEL_TEXT, db));
			break;
		case R.id.sbSpeakerLevel:
			this.tvSpeakerLevel.setText(String.format(SPEAKER_LEVEL_TEXT, db));
			break;

		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLogout:
			this.startLogout();
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return this.getResources().getString(R.string.setting);
	}
}
