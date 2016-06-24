package com.matteo.cc.sip.ui.view;

import com.matteo.cc.R;
import com.matteo.cc.sip.ui.view.DtmfPadView.OnDtmfNumberClickListener;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FunctionsView extends RelativeLayout implements OnClickListener {

	private LinearLayout llFunctions;
	private ImageView ivHandsfree, ivMute, ivDtmf;
	private DtmfPadView vDtmfPad;
	//private Animation mAnimDtmfPad;

	private boolean mHandsfree = false, mMute = false, mShowDtmfPad = false,
			mCanShowDtmfPad = true;
	private FunctionsListener mFunctionsListener;

	public FunctionsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.layout_call_functions, this);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.findViews();
		this.displayViews();
	}

	private void findViews() {
		this.llFunctions = (LinearLayout) this.findViewById(R.id.llFunctions);
		this.ivHandsfree = (ImageView) this.findViewById(R.id.ivHandsfree);
		this.ivDtmf = (ImageView) this.findViewById(R.id.ivDtmf);
		this.ivMute = (ImageView) this.findViewById(R.id.ivMute);
		this.vDtmfPad=(DtmfPadView)this.findViewById(R.id.vDtmfPad);
		this.ivDtmf.setOnClickListener(this);
		this.ivHandsfree.setOnClickListener(this);
		this.ivMute.setOnClickListener(this);
		//this.mAnimDtmfPad=AnimationUtils.loadAnimation(this.getContext(), R.anim.dtmf_pad_appear);
	}

	private void displayViews() {
		this.ivHandsfree
				.setImageResource(this.mHandsfree ? R.drawable.handsfree_enabled_selector
						: R.drawable.handsfree_disabled_selector);
		this.ivMute
				.setImageResource(this.mMute ? R.drawable.mute_enabled_selector
						: R.drawable.mute_disable_selector);
		this.llFunctions.setVisibility(this.mShowDtmfPad ? View.GONE
				: View.VISIBLE);
		if(this.mShowDtmfPad){
			if(this.vDtmfPad.getVisibility()!=View.VISIBLE){
				this.vDtmfPad.setVisibility(View.VISIBLE);
				this.vDtmfPad.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dtmf_pad_appear));
			}
		}else{
			if(this.vDtmfPad.getVisibility()==VISIBLE){
				this.vDtmfPad.setVisibility(View.GONE);
				//this.vDtmfPad.startAnimation(this.mAnimDtmfPad);
			}
		}
	}

	public void setCanShowDtmfPad(boolean canShowDtmfPad) {
		this.mCanShowDtmfPad = canShowDtmfPad;
	}

	public void setDtmfPadListener(FunctionsListener listener) {
		this.mFunctionsListener = listener;
		this.vDtmfPad.setOnDtmfNumberClickListener(this.mFunctionsListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivHandsfree:
			this.clickHandsfree();
			break;
		case R.id.ivMute:
			this.clickMute();
			break;
		case R.id.ivDtmf:
			this.showDtmfPad();
			if(this.mFunctionsListener!=null){
				this.mFunctionsListener.onShowDtmfPad();
			}
			break;

		default:
			break;
		}
	}

	private void clickHandsfree() {
		this.mHandsfree = !this.mHandsfree;
		this.displayViews();
		if (this.mFunctionsListener != null) {
			if (this.mHandsfree) {
				this.mFunctionsListener.onHandsfreeEnabled();
			} else {
				this.mFunctionsListener.onHandsfreeDisabled();
			}
		}
	}

	private void clickMute() {
		this.mMute = !this.mMute;
		this.displayViews();
		if (this.mFunctionsListener != null) {
			if (this.mMute) {
				this.mFunctionsListener.onMuteEnabled();
			} else {
				this.mFunctionsListener.onMuteDisabled();
			}
		}
	}

	public void showDtmfPad() {
		if (this.mCanShowDtmfPad) {
			this.mShowDtmfPad = true;
			this.displayViews();
		}
	}
	
	public void hideDtmfPad(){
		this.mShowDtmfPad=false;
		this.displayViews();
	}

	public static interface FunctionsListener extends OnDtmfNumberClickListener{
		public void onMuteEnabled();

		public void onMuteDisabled();

		public void onHandsfreeEnabled();

		public void onHandsfreeDisabled();

		public void onShowDtmfPad();
	}

}
