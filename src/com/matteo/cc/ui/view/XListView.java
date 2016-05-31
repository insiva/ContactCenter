package com.matteo.cc.ui.view;

import com.matteo.cc.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class XListView extends ListView {

	static int COLOR_GRAY=-1;
	
	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(COLOR_GRAY<0){
			COLOR_GRAY=context.getResources().getColor(R.color.gray);
		}
		this.setDivider(new ColorDrawable(COLOR_GRAY));
		this.setDividerHeight(1);
	}

}
