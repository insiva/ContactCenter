package com.matteo.cc.sip.ui.view;

import com.matteo.cc.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class DtmfNumberView extends View {
	static final int PADDING = 20;
	static final float NUMBER_SIZE_TIMES_BY_RADIUS = 1.1f;
	static final float STROKE_SIZE = 3f;
	private int COLOR_NORMAL, COLOR_PRESSED;

	// private int mMostHeight;
	private char mDtmfValue;
	private float mRadius;
	private float mWidth, mHeight;

	private float mNumberSize;
	private Paint mPaint;
	private Rect mBounds;
	private boolean mPressing = false;

	public DtmfNumberView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.matteo);
		this.mDtmfValue = a.getString(R.styleable.matteo_dtmfNumber).charAt(0);
		a.recycle();
		COLOR_NORMAL = context.getResources().getColor(R.color.green);
		COLOR_PRESSED = context.getResources().getColor(R.color.halfgreen);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.mPaint = new Paint();
		this.mBounds = new Rect();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width = widthSize;
		int height = heightSize;
		if (heightMode != MeasureSpec.EXACTLY) {
			height = heightSize < width ? heightSize : width;
		}
		this.setMeasuredDimension(width, height);
		this.mWidth = width;
		this.mHeight = height;
		float diameter = this.mWidth < this.mHeight ? this.mWidth
				: this.mHeight - 2 * PADDING;
		this.mRadius = diameter / 2;
		this.mNumberSize = this.mRadius * NUMBER_SIZE_TIMES_BY_RADIUS;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		this.mPaint.setAntiAlias(true);
		this.mPaint.setColor(COLOR_NORMAL);
		this.mPaint.setStyle(Paint.Style.STROKE);
		this.mPaint.setStrokeWidth(STROKE_SIZE);
		float cx = this.mWidth / 2;
		float cy = this.mHeight / 2;
		canvas.drawCircle(cx, cy, this.mRadius, this.mPaint);
		if (this.mPressing) {
			this.mPaint.setStyle(Style.FILL);
			this.mPaint.setColor(COLOR_PRESSED);
			canvas.drawCircle(cx, cy, this.mRadius - STROKE_SIZE / 2,
					this.mPaint);
		}
		this.mPaint.setTextSize(this.mNumberSize);
		this.mPaint.setShader(null);
		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(COLOR_NORMAL);
		this.mPaint.getTextBounds(String.valueOf(this.mDtmfValue), 0, 1,
				this.mBounds);
		float centerTextX = (float) (this.mBounds.right + this.mBounds.left) / 2;
		float centerTextY = (float) (this.mBounds.bottom + this.mBounds.top) / 2;
		float x = cx - centerTextX;
		float y = cy - centerTextY;
		canvas.drawText(String.valueOf(this.mDtmfValue), x, y, this.mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean pressing = this.mPressing;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			pressing = true;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_HOVER_EXIT:
			pressing = false;
			break;

		default:
			break;
		}
		if (pressing != this.mPressing) {
			this.mPressing = pressing;
			this.invalidate();
		}
		return super.onTouchEvent(event);
	}

	public char getDtmf() {
		return this.mDtmfValue;
	}

}
