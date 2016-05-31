package com.matteo.cc.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matteo.cc.R;
import com.matteo.cc.content.ContentManager;
import com.matteo.cc.entity.ContactInfo;
import com.matteo.cc.ui.base.BaseFragment;
import com.matteo.cc.ui.view.XListView;
import com.matteo.cc.ui.view.XTextView;
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
	
	private ContactAdapter mContactAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment=View.inflate(this.getActivity(), R.layout.fragment_contact, null);
		ViewUtils.inject(this, fragment);
		this.init();
		return fragment;
	}
	
	private void init(){
		this.mContactAdapter=new ContactAdapter();
		this.lvContacts.setAdapter(this.mContactAdapter);
		this.cvCatalog.setAdapter(this.mContactAdapter);
		this.cvCatalog.setTvCatalog(this.tvCatalog);
		//String string=;
		//XString s=new XString("Z TE 客服", "Z TE Ke 客 Fu 服");
		//this.tvCatalog.setText(s.toPinyin());
	}
	
	class ContactAdapter extends BaseAdapter implements CatalogAdapter{
		
		private List<ContactInfo> mContacts;
		private char[] mCatalogs;
		
		public ContactAdapter(){
			this.mContacts=ContentManager.get().getContacts();
			List<Character> catalogList=new ArrayList<Character>();
			Character lastCatalog=null;
			for (ContactInfo contact : this.mContacts) {
				if(lastCatalog==null||lastCatalog!=contact.mFirstChar){
					lastCatalog=contact.mFirstChar;
					catalogList.add(lastCatalog);
				}
			}
			int i=0;
			this.mCatalogs=new char[catalogList.size()];
			for (Character catalog : catalogList) {
				this.mCatalogs[i++]=catalog;
			}
		}

		@Override
		public int getCount() {
			return this.mContacts.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactItem ci=null;
			if(convertView==null){
				ci=new ContactItem(getActivity());
				convertView=ci;
			}else{
				ci=(ContactItem)convertView;
			}
			ci.setContact(this.mContacts, position);
			return convertView;
		}

		@Override
		public void onCatalogSelected(char catalog) {
			int i=0;
			boolean equaled=false;
			for (ContactInfo contact : this.mContacts) {
				if(contact.mFirstChar==catalog){
					equaled=true;
					break;
				}
				i++;
			}
			if(equaled){
				ContactFragment.this.lvContacts.setSelection(i);
			}
		}

		@Override
		public char[] getCatalogs() {
			return this.mCatalogs;
		}
		
	}
	
	class ContactItem extends LinearLayout implements OnClickListener{
		
		@ViewInject(R.id.tvCatalog)
		private TextView tvCatalog;
		@ViewInject(R.id.tvName)
		private XTextView tvName;
		@ViewInject(R.id.llContact)
		private LinearLayout llContact;
		@ViewInject(R.id.tvNumbers)
		private XTextView tvNumbers;
		
		private ContactInfo mContact;
		
		public ContactItem(Context context) {
			super(context);
			View.inflate(getActivity(), R.layout.layout_contact_item, this);
			ViewUtils.inject(this);
			this.setOrientation(LinearLayout.VERTICAL);
			this.llContact.setOnClickListener(this);
		}
		
		public void setContact(List<ContactInfo> contacts,int position){
			this.mContact=contacts.get(position);
			if(position==0){
				this.tvCatalog.setVisibility(View.VISIBLE);
			}else{
				ContactInfo lastContact=contacts.get(position-1);
				if(lastContact.mFirstChar!=this.mContact.mFirstChar){
					this.tvCatalog.setVisibility(View.VISIBLE);
				}else{
					this.tvCatalog.setVisibility(View.GONE);
				}
			}
			this.tvCatalog.setText(String.valueOf(this.mContact.mFirstChar));
			this.tvName.setText(this.mContact.mName);
			this.tvNumbers.setText(this.mContact.getDisplayNumbers());
		}

		@Override
		public void onClick(View v) {
			
		}
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
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
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
			this.mCatalogs=this.mCatalogAdapter.getCatalogs();
		}
		
		public void setTvCatalog(TextView tv){
			this.tvCatalog=tv;
		}
		
	}
	
	public static  interface CatalogAdapter{
		public void onCatalogSelected(char catalog);
		public char[] getCatalogs();
	}
}
