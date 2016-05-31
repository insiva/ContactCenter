package com.matteo.cc.ui.view;

import com.matteo.cc.R;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DialPad extends LinearLayout implements OnClickListener,OnLongClickListener{

	@ViewInject(R.id.llDial)
	LinearLayout llDial;
	@ViewInject(R.id.llContact)
	LinearLayout llContact;
	@ViewInject(R.id.btnDial)
	Button btnDial;
	@ViewInject(R.id.rlNumber0)
	RelativeLayout rlNumber0;
	@ViewInject(R.id.rlNumber1)
	RelativeLayout rlNumber1;
	@ViewInject(R.id.rlNumber2)
	RelativeLayout rlNumber2;
	@ViewInject(R.id.rlNumber3)
	RelativeLayout rlNumber3;
	@ViewInject(R.id.rlNumber4)
	RelativeLayout rlNumber4;
	@ViewInject(R.id.rlNumber5)
	RelativeLayout rlNumber5;
	@ViewInject(R.id.rlNumber6)
	RelativeLayout rlNumber6;
	@ViewInject(R.id.rlNumber7)
	RelativeLayout rlNumber7;
	@ViewInject(R.id.rlNumber8)
	RelativeLayout rlNumber8;
	@ViewInject(R.id.rlNumber9)
	RelativeLayout rlNumber9;
	@ViewInject(R.id.rlNumberStar)
	RelativeLayout rlNumberStar;
	@ViewInject(R.id.rlNumberSharp)
	RelativeLayout rlNumberSharp;
	@ViewInject(R.id.rlDisplayNumber)
	RelativeLayout rlDisplayNumber;
	@ViewInject(R.id.llDisplayNumber)
	LinearLayout llDisplayNumber;
	@ViewInject(R.id.ivDeleteNumber)
	ImageView ivDeleteNumber;
	@ViewInject(R.id.tvNumber)
	TextView tvNumber;
	
	private Context mContext;
	private OnClickDialPadListener mOnClickDialPadListener;
	private String mNumber="";
	
	public DialPad(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	private void init(Context context) {
		this.mContext=context;
		//this.setSoundEffectsEnabled(true);
		View.inflate(this.mContext, R.layout.layout_dialpad, this);
		ViewUtils.inject(this);
		this.setOrientation(LinearLayout.VERTICAL);
		this.llContact.setOnClickListener(this);
		this.llDial.setOnClickListener(this);
		this.btnDial.setOnClickListener(this);
		
		this.rlNumber0.setOnClickListener(this);
		this.rlNumber1.setOnClickListener(this);
		this.rlNumber2.setOnClickListener(this);
		this.rlNumber3.setOnClickListener(this);
		this.rlNumber4.setOnClickListener(this);
		this.rlNumber5.setOnClickListener(this);
		this.rlNumber6.setOnClickListener(this);
		this.rlNumber7.setOnClickListener(this);
		this.rlNumber8.setOnClickListener(this);
		this.rlNumber9.setOnClickListener(this);
		this.rlNumberStar.setOnClickListener(this);
		this.rlNumberSharp.setOnClickListener(this);
		this.ivDeleteNumber.setOnClickListener(this);
		this.ivDeleteNumber.setOnLongClickListener(this);
	}
	
	public void setOnClickDialPadListener(OnClickDialPadListener listener){
		this.mOnClickDialPadListener=listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llDial:
			if (this.mOnClickDialPadListener!=null) {
				this.mOnClickDialPadListener.onTitleDialDownClick();
			}
			break;
		case R.id.llContact:
			if(this.mOnClickDialPadListener!=null){
				this.mOnClickDialPadListener.onTitleContactClick();
			}
			break;
		case R.id.btnDial:
			if(this.mOnClickDialPadListener!=null){
				this.mOnClickDialPadListener.onDial();
			}
			break;
		case R.id.rlNumber0:
			this.addNumber('0');
			break;
		case R.id.rlNumber1:
			this.addNumber('1');
			break;
		case R.id.rlNumber2:
			this.addNumber('2');
			break;
		case R.id.rlNumber3:
			this.addNumber('3');
			break;
		case R.id.rlNumber4:
			this.addNumber('4');
			break;
		case R.id.rlNumber5:
			this.addNumber('5');
			break;
		case R.id.rlNumber6:
			this.addNumber('6');
			break;
		case R.id.rlNumber7:
			this.addNumber('7');
			break;
		case R.id.rlNumber8:
			this.addNumber('8');
			break;
		case R.id.rlNumber9:
			this.addNumber('9');
			break;
		case R.id.rlNumberStar:
			this.addNumber('*');
			break;
		case R.id.rlNumberSharp:
			this.addNumber('#');
			break;
		case R.id.ivDeleteNumber:
			this.deleteLastNumber();
			break;
		default:
			break;
		}
	}
	
	private void displayNumber(){
		this.tvNumber.setText(this.mNumber);
		if(TextUtils.isEmpty(this.mNumber)){
			this.rlDisplayNumber.setVisibility(View.VISIBLE);
			this.llDisplayNumber.setVisibility(View.GONE);
		}else{
			this.rlDisplayNumber.setVisibility(View.GONE);
			this.llDisplayNumber.setVisibility(View.VISIBLE);
		}
	}
	
	private void addNumber(char n){
		String number=this.mNumber+n;
		if(!number.equals(this.mNumber)){
			if(this.mOnClickDialPadListener!=null){
				this.mOnClickDialPadListener.onNumberChanged(this.mNumber,number);
			}
			this.mNumber=number;
			this.displayNumber();
		}
	}
	
	private void deleteLastNumber(){
		if(!TextUtils.isEmpty(this.mNumber)){
			String number=this.mNumber.substring(0,this.mNumber.length()-1);
			if(this.mOnClickDialPadListener!=null){
				this.mOnClickDialPadListener.onNumberChanged(this.mNumber,number);
			}
			this.mNumber=number;
			this.displayNumber();
		}
	}
	
	private void deleteAllNumbers(){
		String number="";
		if(!TextUtils.isEmpty(this.mNumber)){
			if(this.mOnClickDialPadListener!=null){
				this.mOnClickDialPadListener.onNumberChanged(this.mNumber,number);
			}
			this.mNumber=number;
			this.displayNumber();
		}
	}
	
	public static interface OnClickDialPadListener{
		public void onTitleDialDownClick();
		public void onTitleContactClick();
		public void onDial();
		public void onNumberChanged(String oldNumber,String newNumber);
	}

	@Override
	public boolean onLongClick(View v) {
		this.deleteAllNumbers();
		return false;
	}
}
