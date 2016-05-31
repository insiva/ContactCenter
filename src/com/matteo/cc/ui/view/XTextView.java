package com.matteo.cc.ui.view;

import com.matteo.cc.model.XString;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class XTextView extends TextView {

	public XTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setText(XString text){
		super.setText(text.toString());
	}

}
