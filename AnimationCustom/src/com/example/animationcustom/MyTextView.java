package com.example.animationcustom;

import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class MyTextView extends View {

	String content = "abc";
	private Paint paint;

	public MyTextView(Context context) {// 一个参数的构造方法，适用在，代码中new自定义的view类
		super(context);
		paint = new Paint();
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
	}

	public void start() {
		postDelayed(new Runnable() {
			// 动态获得自定义控件的文字内容
			@Override
			public void run() {
				content = new Date().toLocaleString();
				invalidate();// 调用ondraw()方法，重绘
				postDelayed(this, 1000);
			}
		}, 1000);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setAntiAlias(true);// 消除锯齿
		paint.setTextSize(40.0f);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(content, getWidth() / 2.0f, getHeight(), paint);// 画文本
	}
}
