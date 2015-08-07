package com.example.animationcustom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {

	private ArcView mArcViewApp, mArcViewTag;
	private TextView mTextViewAppUpTag;
	private MyTextView mMyTextView;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		mMyTextView.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mArcViewApp = (ArcView) findViewById(R.id.app_mng_arc_view);
		mArcViewTag = (ArcView) findViewById(R.id.app_up_tag_arc_view);
		mTextViewAppUpTag = (TextView) findViewById(R.id.app_up_tag_num);
		mMyTextView = (MyTextView)findViewById(R.id.my_text_view);

		mArcViewApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PropertyValuesHolder startAngleHolder = PropertyValuesHolder
						.ofFloat("startAngle", -45.0f, -25.0f);
				PropertyValuesHolder sweepAngleHolder = PropertyValuesHolder
						.ofFloat("sweepAngle", 360.0f, 320.0f);
				ObjectAnimator appMngAnimator = ObjectAnimator
						.ofPropertyValuesHolder(mArcViewApp, startAngleHolder,
								sweepAngleHolder).setDuration(300);
				appMngAnimator.addListener(new AnimatorListenerAdapter() {

					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						super.onAnimationEnd(animation);
						ObjectAnimator tagAnimator = ObjectAnimator.ofFloat(
								mArcViewTag, "sweepAngle", 0.0f, 360.0f)
								.setDuration(600);
						tagAnimator.addListener(new AnimatorListenerAdapter() {

							@Override
							public void onAnimationStart(Animator animation) {
								// TODO Auto-generated method stub
								super.onAnimationStart(animation);
								mTextViewAppUpTag.setVisibility(View.GONE);
							}

							@Override
							public void onAnimationEnd(Animator animation) {
								// TODO Auto-generated method stub
								super.onAnimationEnd(animation);
								mTextViewAppUpTag.setVisibility(View.VISIBLE);
							}

						});
						mArcViewTag.setVisibility(View.VISIBLE);
						tagAnimator.start();
					}

				});
				appMngAnimator.start();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
