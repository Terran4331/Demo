package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;

public class Box {
	private PointF mOrignPoint;
	private PointF mCurrentPoint;

	public Box(PointF point) {
		mOrignPoint = mCurrentPoint = point;
	}

	public PointF getOrignPoint() {
		return mOrignPoint;
	}

	public void setOrignPoint(PointF point) {
		mOrignPoint = point;
	}

	public PointF getCurrentPoint() {
		return mCurrentPoint;
	}

	public void setCurrentPoint(PointF point) {
		mCurrentPoint = point;
	}
}
