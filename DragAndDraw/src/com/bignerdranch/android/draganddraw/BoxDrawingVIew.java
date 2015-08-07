package com.bignerdranch.android.draganddraw;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BoxDrawingVIew extends View {

	private Paint mRectPaint;
	private Box mCurrentBox;
	private ArrayList<Box> mBoxSet = new ArrayList<Box>();

	public BoxDrawingVIew(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mRectPaint = new Paint();
		mRectPaint.setColor(0x22ff0000);
	}

	public ArrayList<Box> getBoxSet() {
		return mBoxSet;
	}

	public void setBoxSet(ArrayList<Box> boxSet) {
		mBoxSet = boxSet;
		if (mBoxSet == null) {
			mBoxSet = new ArrayList<Box>();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		PointF point = new PointF(event.getX(), event.getY());
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mCurrentBox = new Box(point);
			mBoxSet.add(mCurrentBox);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mCurrentBox != null) {
				mCurrentBox.setCurrentPoint(point);
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mCurrentBox = null;
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (mBoxSet != null) {
			for (Box box : mBoxSet) {
				float left = Math.min(box.getOrignPoint().x,
						box.getCurrentPoint().x);
				float top = Math
						.min(box.getOrignPoint().y, box.getCurrentPoint().y);
				float right = Math.max(box.getOrignPoint().x,
						box.getCurrentPoint().x);
				float bottom = Math.max(box.getOrignPoint().y,
						box.getCurrentPoint().y);
				canvas.drawRect(left, top, right, bottom, mRectPaint);
			}
		}
	}

}
