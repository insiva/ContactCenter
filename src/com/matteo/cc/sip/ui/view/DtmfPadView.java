package com.matteo.cc.sip.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.matteo.cc.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class DtmfPadView extends LinearLayout implements OnClickListener {
	static final int LINES_COUNT = 4;
	static final int[] DTMF_NUMBER_VIEW_RES_IDS = {R.id.vNumber1,R.id.vNumber2,R.id.vNumber3,
		R.id.vNumber4,R.id.vNumber5,R.id.vNumber6,
		R.id.vNumber7,R.id.vNumber8,R.id.vNumber9,
		R.id.vNumberStar,R.id.vNumber0,R.id.vNumberSharp};
	
	private List<DtmfNumberView> mDtmfNumbers;

	private OnDtmfNumberClickListener mOnDtmfNumberClickListener;
	//private int mMostHeight = 0, mDtmfNumberMostHeight = 0;

	public DtmfPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.layout_dtmf_pad, this);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setPadding(90, 0, 90, 0);
		this.findViews();
	}

	private void findViews() {
		this.mDtmfNumbers=new ArrayList<DtmfNumberView>();
		for(int i=0;i<DTMF_NUMBER_VIEW_RES_IDS.length;i++){
			DtmfNumberView v=(DtmfNumberView)this.findViewById(DTMF_NUMBER_VIEW_RES_IDS[i]);
			this.mDtmfNumbers.add(v);
			v.setOnClickListener(this);
		}
	}

	public void setOnDtmfNumberClickListener(OnDtmfNumberClickListener listener) {
		this.mOnDtmfNumberClickListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v instanceof DtmfNumberView) {
			if (this.mOnDtmfNumberClickListener != null) {
				DtmfNumberView dnv=(DtmfNumberView)v;
				this.mOnDtmfNumberClickListener.onDtmfNumberClick(dnv.getDtmf());
			}
		}
	}
/*
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.mMostHeight = MeasureSpec.getSize(heightMeasureSpec);
		this.mDtmfNumberMostHeight=this.mMostHeight/LINES_COUNT;
		for (DtmfNumberView v : this.mDtmfNumbers) {
			v.setMostHeight(this.mDtmfNumberMostHeight);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
*/
	public static interface OnDtmfNumberClickListener {
		public void onDtmfNumberClick(char dtmf);
	}

}
