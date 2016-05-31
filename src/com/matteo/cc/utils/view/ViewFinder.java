package com.matteo.cc.utils.view;

import android.app.Activity;
import android.view.View;

public class ViewFinder {
	private enum ViewType{
		ACTIVITY,VIEW
	}
	
	private ViewType mViewType;
	private Activity mActivity;
	private View mView;

	public ViewFinder(Activity activity){
		this.mViewType=ViewType.ACTIVITY;
		this.mActivity=activity;
	}
	
	public ViewFinder(View view){
		this.mViewType=ViewType.VIEW;
		this.mView=view;
	}
	
	public View findViewById(int id){
		View view=null;
		switch (this.mViewType) {
		case ACTIVITY:
			view=this.mActivity.findViewById(id);
			break;
		case VIEW:
			view=this.mView.findViewById(id);
			break;

		default:
			break;
		}
		return view;
	}
}
