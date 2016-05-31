package com.matteo.cc.ui.view;

import com.matteo.cc.Config;
import com.matteo.cc.Constant;
import com.matteo.cc.R;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainBottomBar extends LinearLayout implements OnClickListener {
	private Context mContext;
	static final int TITLE_COUNT = Constant.MODULE_COUNT;
	static final int[][] TITLE_RES_IDS = new int[][] {
			{ R.drawable.bottom_bar_icon_dial_up_selected,
					R.drawable.bottom_bar_icon_dial_unselected, R.string.dial },
			{ R.drawable.bottom_bar_icon_contact_selected,
					R.drawable.bottom_bar_icon_contact_unselected,
					R.string.contact },
			{ R.drawable.bottom_bar_icon_sms_selected,
					R.drawable.bottom_bar_icon_sms_unselected, R.string.sms },
			{ R.drawable.bottom_bar_icon_setting_selected,
					R.drawable.bottom_bar_icon_setting_unselected,
					R.string.setting } };
	static int COlOR_GRAY = Config.getAppContext().getResources()
			.getColor(R.color.font_gray);
	static int COLOR_GREEN = Config.getAppContext().getResources()
			.getColor(R.color.green);

	@ViewInject(R.id.llDial)
	LinearLayout llDial;
	@ViewInject(R.id.llContact)
	LinearLayout llContact;
	@ViewInject(R.id.llSms)
	LinearLayout llSms;
	@ViewInject(R.id.llSetting)
	LinearLayout llSetting;

	private LinearLayout[] mLlTitles;
	private ImageView[] mIvTitles;
	private TextView[] mTvTitles;
	private OnTitleIndexChangedListener mOnTitleIndexChangedListener;
	private int mCurrentTitleIndex = 0;

	public MainBottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		View.inflate(this.mContext, R.layout.layout_main_bottom_bar, this);
		ViewUtils.inject(this);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.mLlTitles = new LinearLayout[TITLE_COUNT];
		this.mLlTitles[0] = llDial;
		this.mLlTitles[1] = llContact;
		this.mLlTitles[2] = llSms;
		this.mLlTitles[3] = llSetting;
		this.mIvTitles = new ImageView[TITLE_COUNT];
		this.mTvTitles = new TextView[TITLE_COUNT];
		for (int i = 0; i < TITLE_COUNT; i++) {
			this.mLlTitles[i].setOnClickListener(this);
			this.mIvTitles[i] = (ImageView) this.mLlTitles[i].getChildAt(0);
			this.mTvTitles[i] = (TextView) this.mLlTitles[i].getChildAt(1);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		default:
			int i = 0;
			for (; i < TITLE_COUNT; i++) {
				if (id == this.mLlTitles[i].getId()) {
					break;
				}
			}
			if(this.mCurrentTitleIndex==0&&i==0){
				if (this.mOnTitleIndexChangedListener != null) {
					this.mOnTitleIndexChangedListener.onTitleDialUpClick();
				}
			}
			if (i != this.mCurrentTitleIndex) {
				// this.onTitleIndexChanged(this.mCurrentTitleIndex, i);
				this.setTitleIndex(i);
				if (this.mOnTitleIndexChangedListener != null) {
					this.mOnTitleIndexChangedListener.onTitleIndexChanged(
							this.mCurrentTitleIndex, i);
				}
			}
			break;
		}
	}
	
	public int getIndex(){
		return this.mCurrentTitleIndex;
	}

	public void setTitleIndex(int index) {
		if (index != this.mCurrentTitleIndex) {
			// this.onTitleIndexChanged(this.mCurrentTitleIndex, index);
			this.mIvTitles[this.mCurrentTitleIndex]
					.setImageResource(TITLE_RES_IDS[this.mCurrentTitleIndex][1]);
			this.mTvTitles[this.mCurrentTitleIndex].setTextColor(COlOR_GRAY);
			this.mIvTitles[index].setImageResource(TITLE_RES_IDS[index][0]);
			this.mTvTitles[index].setTextColor(COLOR_GREEN);
			this.mCurrentTitleIndex = index;
		}
	}

	public void setOnTitleIndexChangedListener(
			OnTitleIndexChangedListener listener) {
		this.mOnTitleIndexChangedListener = listener;
	}

	public static interface OnTitleIndexChangedListener {
		public void onTitleIndexChanged(int oldIndex, int newIndex);

		public void onTitleDialUpClick();
	}

}
