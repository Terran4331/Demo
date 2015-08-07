package com.example.animationcustom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

	private Paint mBodyPaint, mBorderPaint;
	private float mStartAngle = 0.0f, mSweepAngle = 360.0f;
	private boolean mFill = false;
	RectF mBoundRectF = new RectF(), mBodyRectF = new RectF();

	public ArcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ArcView);

		int bodyColor = a.getColor(R.styleable.ArcView_bodyColor, 0xffffff);
		mFill = a.getBoolean(R.styleable.ArcView_drawBody, false);
		int color = a.getColor(R.styleable.ArcView_borderColor, 0xff43a3ee);
		float width = a.getDimension(R.styleable.ArcView_borderWidth, 2.0f);
		mStartAngle = a.getFloat(R.styleable.ArcView_startAngle, 0.0f);
		mSweepAngle = a.getFloat(R.styleable.ArcView_sweepAngle, 360.0f);

		mBodyPaint = new Paint();
		mBodyPaint.setColor(bodyColor);
		mBodyPaint.setAntiAlias(true);
		mBodyPaint.setStyle(Style.FILL);

		mBorderPaint = new Paint();
		mBorderPaint.setStrokeWidth(width);
		mBorderPaint.setColor(color);
		mBorderPaint.setStyle(Style.STROKE);
		mBorderPaint.setAntiAlias(true);

		a.recycle();
	}

	public void setLineWidth(float width) {
		mBorderPaint.setStrokeWidth(width);
		invalidate();
	}

	public void setColor(int color) {
		mBorderPaint.setColor(color);
		invalidate();
	}

	public float getStartAngle() {
		return mStartAngle;
	}

	public void setStartAngle(float angle) {
		mStartAngle = angle;
		invalidate();
	}

	public float getSweepAngle() {
		return mSweepAngle;
	}

	public void setSweepAngle(float angle) {
		mSweepAngle = angle;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float lineWidth = mBorderPaint.getStrokeWidth(), halfWidth = lineWidth / 2.0f;
		if (mFill) {
			mBodyRectF.left = lineWidth;
			mBodyRectF.top = lineWidth;
			mBodyRectF.right = getWidth() - lineWidth;
			mBodyRectF.bottom = getHeight() - lineWidth;
			canvas.drawArc(mBodyRectF, mStartAngle, mSweepAngle, true,
					mBodyPaint);
		}

		mBoundRectF.left = halfWidth;
		mBoundRectF.top = halfWidth;
		mBoundRectF.right = getWidth() - halfWidth;
		mBoundRectF.bottom = getHeight() - halfWidth;
		canvas.drawArc(mBoundRectF, mStartAngle, mSweepAngle, false,
				mBorderPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int width;
		final int specMode = MeasureSpec.getMode(measureSpec);
		final int specSize = MeasureSpec.getSize(measureSpec);

		if (MeasureSpec.EXACTLY == specMode) {
			width = specSize;
		} else {
			width = getPaddingLeft() + getPaddingRight() + 1;
			if (MeasureSpec.AT_MOST == specMode) {
				width = Math.min(width, specSize);
			}
		}

		return width;
	}

	private int measureHeight(int measureSpec) {
		int height;
		final int specMode = MeasureSpec.getMode(measureSpec);
		final int specSize = MeasureSpec.getSize(measureSpec);

		if (MeasureSpec.EXACTLY == specMode) {
			height = specSize;
		} else {
			height = getPaddingTop() + getPaddingBottom() + 1;
			if (MeasureSpec.AT_MOST == specMode) {
				height = Math.min(height, specSize);
			}
		}

		return height;
	}
}