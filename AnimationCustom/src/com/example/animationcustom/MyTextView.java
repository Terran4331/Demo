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

	public MyTextView(Context context) {// һ�������Ĺ��췽���������ڣ�������new�Զ����view��
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
			// ��̬����Զ���ؼ�����������
			@Override
			public void run() {
				content = new Date().toLocaleString();
				invalidate();// ����ondraw()�������ػ�
				postDelayed(this, 1000);
			}
		}, 1000);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setAntiAlias(true);// �������
		paint.setTextSize(40.0f);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(content, getWidth() / 2.0f, getHeight(), paint);// ���ı�
	}
}
