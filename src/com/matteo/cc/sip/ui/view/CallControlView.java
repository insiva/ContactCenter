package com.matteo.cc.sip.ui.view;

import com.matteo.cc.R;
import com.matteo.cc.sip.constant.Constant;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class CallControlView extends RelativeLayout implements OnClickListener {

	private LinearLayout llRecvCall;
	private ImageView ivAnswer, ivHangup, ivReject;
	private TextView tvHideDtmfPad;

	private CallControlListener mCallControlListener;
	private int mCallState = 0;
	private boolean mShowTvHideDtmfPad = false;

	public CallControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.layout_call_control, this);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.findViews();
		this.displayViews();
	}

	private void findViews() {
		this.llRecvCall = (LinearLayout) this.findViewById(R.id.llRecvCall);
		this.ivAnswer = (ImageView) this.findViewById(R.id.ivAnswer);
		this.ivHangup = (ImageView) this.findViewById(R.id.ivHangup);
		this.ivReject = (ImageView) this.findViewById(R.id.ivReject);
		this.tvHideDtmfPad = (TextView) this.findViewById(R.id.tvHideDtmfPad);
		this.ivAnswer.setOnClickListener(this);
		this.ivHangup.setOnClickListener(this);
		this.ivReject.setOnClickListener(this);
		this.tvHideDtmfPad.setOnClickListener(this);
	}

	private void displayViews() {
		this.tvHideDtmfPad.setVisibility(this.mShowTvHideDtmfPad ? View.VISIBLE
				: View.GONE);
		if (this.mCallState != 0) {
			int direct = this.mCallState & Constant.DIRECT_MASK;
			int state = this.mCallState & Constant.STATE_MASK;
			if (direct == Constant.Direct.OUT) {
				this.llRecvCall.setVisibility(View.GONE);
				this.ivHangup.setVisibility(View.VISIBLE);
			} else { // IN
				switch (state) {
				case Constant.State.RING:
				case Constant.State.ESTABLISHING:
					this.llRecvCall.setVisibility(View.VISIBLE);
					this.ivHangup.setVisibility(View.GONE);
					break;

				default:
					this.llRecvCall.setVisibility(View.GONE);
					this.ivHangup.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	}

	public void setCallControlListener(CallControlListener listener) {
		this.mCallControlListener = listener;
	}

	public void setCallState(int callState) {
		this.mCallState = callState;
		this.displayViews();
	}

	public void hideTvHideDtmfPad() {
		this.mShowTvHideDtmfPad = false;
		this.displayViews();
	}

	public void showTvHideDtmfPad() {
		this.mShowTvHideDtmfPad = true;
		this.displayViews();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivAnswer:
			if (this.mCallControlListener != null) {
				this.mCallControlListener.onAnswer();
			}
			break;
		case R.id.ivReject:
			if (this.mCallControlListener != null) {
				this.mCallControlListener.onReject();
			}
			break;
		case R.id.ivHangup:
			if (this.mCallControlListener != null) {
				this.mCallControlListener.onHangup();
			}
			break;
		case R.id.tvHideDtmfPad:
			this.hideTvHideDtmfPad();
			if (this.mCallControlListener != null) {
				this.mCallControlListener.onHideTvShowDtmfPad();
			}
			break;

		default:
			break;
		}
	}

	public static interface CallControlListener {
		public void onAnswer();

		public void onReject();

		public void onHangup();

		public void onHideTvShowDtmfPad();
	}

}
