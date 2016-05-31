package com.matteo.cc.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.ui.adapter.ContactFragmentAdapter;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.ui.view.XListView;
import com.matteo.cc.utils.view.ViewInject;
import com.matteo.cc.utils.view.ViewUtils;

public class ContactFragment extends BaseFragment {

	@ViewInject(R.id.etKeyword)
	private EditText etKeyword;
	@ViewInject(R.id.lvContacts)
	private XListView lvContacts;
	@ViewInject(R.id.cvCatalog)
	private CatalogView cvCatalog;
	@ViewInject(R.id.tvCatalog)
	private TextView tvCatalog;
	
	//private ContactAdapter mContactAdapter;
	private ContactFragmentAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment=View.inflate(this.getActivity(), R.layout.fragment_contact, null);
		ViewUtils.inject(this, fragment);
		this.init();
		return fragment;
	}
	
	private void init(){
		if(this.mAdapter==null){
			this.mAdapter=new ContactFragmentAdapter(this.getActivity());
		}
		this.lvContacts.setAdapter(this.mAdapter);
		this.cvCatalog.setAdapter(this.mAdapter);
		this.cvCatalog.setTvCatalog(this.tvCatalog);
		this.mAdapter.setCatalogView(this.cvCatalog);
		this.mAdapter.setListView(this.lvContacts);
		this.etKeyword.addTextChangedListener(new SearchTextWatcher());
		this.cvCatalog.setVisibility(this.mAdapter.showCatalog()?View.VISIBLE:View.GONE);
		this.etKeyword.setVisibility(this.mAdapter.showSearchEt()?View.VISIBLE:View.GONE);
	}
	
	public void setAdapter(ContactFragmentAdapter adapter){
		if(this.mAdapter==null){
			this.mAdapter=adapter;
		}else{
			this.mAdapter=adapter;
			this.init();
		}
		//this.initByAdapter();
	}

	
	@SuppressLint("ClickableViewAccessibility")
	public static class CatalogView extends View{
		static final float TEXT_SIZE=26f;
		static final char DEFAULT_SHARP='#';
		
		private char[] mCatalogs;
		private CatalogAdapter mCatalogAdapter;
		private Paint mPaint;
		private int mWidth,mHeight;
		private Rect mCharBounds;
		private float mPerHeight;
		private TextView tvCatalog;
		private Character mSelectedChar;

		public CatalogView(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.mPaint=new Paint();
			this.mPaint.setColor(context.getResources().getColor(R.color.green));
			this.mPaint.setAntiAlias(true);  
			this.mPaint.setTextSize(TEXT_SIZE);
			this.setBackgroundColor(context.getResources().getColor(R.color.transparent));
			this.mCharBounds=new Rect();
			this.mPaint.getTextBounds(String.valueOf(DEFAULT_SHARP), 0, 1, this.mCharBounds);
			this.mSelectedChar=null;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			if(this.mCatalogAdapter!=null){
				int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
		        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
		        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
		        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
		        if (widthMode == MeasureSpec.EXACTLY) {  
		            this.mWidth = widthSize;  
		        } else {  
		        	this.mWidth=(this.mCharBounds.right-this.mCharBounds.left)*3;
		        }
		        if(heightMode==MeasureSpec.EXACTLY){
		        	this.mHeight=heightSize;
		        }else{
		        	this.mHeight=(this.mCharBounds.bottom-this.mCharBounds.top)*this.mCatalogs.length;
		        }
		        this.setMeasuredDimension(this.mWidth, this.mHeight);
		        this.mPerHeight=(float)this.mHeight/(float)this.mCatalogs.length;
			}else{
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//canvas.drawText(String.valueOf('#'), 10, 10, this.mPaint);
			float x=0,y=0,w=(float)this.mWidth;
			int l=this.mCatalogs.length;
			for(int i=0;i<l;i++){
				this.mPaint.getTextBounds(this.mCatalogs, i, 1, this.mCharBounds);
				float centerTextX=(float)(this.mCharBounds.right+this.mCharBounds.left)/2;
				float centerTextY=(float)(this.mCharBounds.bottom+this.mCharBounds.top)/2;
				float centerSquareX=w/2;
				float centerSquareY=this.mPerHeight/2;
				x=centerSquareX-centerTextX;
				y=centerSquareY-centerTextY;
				canvas.drawText(String.valueOf(this.mCatalogs[i]), x, y+i*this.mPerHeight, this.mPaint);
			}
		}
		
		public void initCatalogs(){
			if(this.mCatalogAdapter!=null){
				this.mCatalogs=this.mCatalogAdapter.getCatalogs();
				this.invalidate();
			}
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(this.mCatalogs.length==0){
				return false;
			}
			float y=event.getY();
			float r=y/this.mPerHeight;
			int i=(int) Math.floor(r);
			if(i>=this.mCatalogs.length){
				i=this.mCatalogs.length-1;
			}else if(i<0){
				i=0;
			}
			char c=this.mCatalogs[i];
			if(this.mSelectedChar==null||!this.mSelectedChar.equals(c)){
				this.mSelectedChar=c;
				this.tvCatalog.setText(String.valueOf(this.mSelectedChar));
				if(this.mCatalogAdapter!=null){
					this.mCatalogAdapter.onCatalogSelected(this.mCatalogs[i]);
				}
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.tvCatalog.setVisibility(View.VISIBLE);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				this.tvCatalog.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			return true;
		}
		
		public void setAdapter(CatalogAdapter adapter){
			this.mCatalogAdapter=adapter;
			this.initCatalogs();
		}
		
		public void setTvCatalog(TextView tv){
			this.tvCatalog=tv;
		}
		
	}
	
	public static  interface CatalogAdapter{
		public void onCatalogSelected(char catalog);
		public char[] getCatalogs();
	}
	
	class SearchTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			ContactFragment.this.mAdapter.search(s.toString());
		}
		
	}
}
