package com.example.animationfade;

import com.example.animationfade.R.animator;

import android.R.mipmap;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {
	private static final String TAG = "MAIN_ACTIVITY_TAG";

	private RelativeLayout mRelativeLayout;
	private Button mRedBtnX, mRedBtnY, mRedBtnZ, mBlueBtn;
	private Button mBlueBtnValueAnim, mBlueBtnPaowuxian, mGreenBtn,
			mGreenBtnXml, mGreenBtnXmlSet;
	private AnimationDrawable mAnimationDrawable;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mRedBtnX = (Button) findViewById(R.id.red_btn_x);
		mRedBtnX.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360.0f)
						.setDuration(1000).start();
			}
		});

		mRedBtnY = (Button) findViewById(R.id.red_btn_y);
		mRedBtnY.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ObjectAnimator.ofFloat(v, "rotationY", 0.0f, 360.0f)
						.setDuration(1000).start();
			}
		});

		mRedBtnZ = (Button) findViewById(R.id.red_btn_z);
		mRedBtnZ.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRedBtnZ.animate().rotation(360.0f).setDuration(1000)
						.setListener(new AnimatorListenerAdapter() {

							@Override
							public void onAnimationEnd(Animator animation) {
								// TODO Auto-generated method stub
								super.onAnimationEnd(animation);
								mRedBtnZ.setRotation(0.0f);
							}

						});
			}
		});

		mBlueBtn = (Button) findViewById(R.id.green_btn_holder);
		mBlueBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub

				// ObjectAnimator animator = ObjectAnimator.ofFloat(v, "", 1.0f,
				// 0.0f, 1.0f).setDuration(1000);
				// animator.addUpdateListener(new AnimatorUpdateListener() {
				//
				// @Override
				// public void onAnimationUpdate(ValueAnimator animation) {
				// // TODO Auto-generated method stub
				// float cValue = (Float) animation.getAnimatedValue();
				// v.setAlpha(cValue);
				// v.setScaleX(cValue);
				// v.setScaleY(cValue);
				// }
				// });
				// animator.start();

				PropertyValuesHolder alphaHoder = PropertyValuesHolder.ofFloat(
						"alpha", 1.0f, 0.0f, 1.0f);
				PropertyValuesHolder xHoder = PropertyValuesHolder.ofFloat(
						"scaleX", 1.0f, 0.0f, 1.0f);
				PropertyValuesHolder yHoder = PropertyValuesHolder.ofFloat(
						"scaleY", 1.0f, 0.0f, 1.0f);
				ObjectAnimator
						.ofPropertyValuesHolder(v, alphaHoder, xHoder, yHoder)
						.setDuration(1000).start();
			}
		});

		mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
		mBlueBtnValueAnim = (Button) findViewById(R.id.blue_btn_value_anim);
		mBlueBtnValueAnim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ValueAnimator valueAnimator = ValueAnimator.ofFloat(
						0f,
						mRelativeLayout.getHeight()
								- mBlueBtnValueAnim.getTop()
								- mBlueBtnValueAnim.getHeight(), 0f);
				valueAnimator.setInterpolator(new LinearInterpolator());
				valueAnimator.setTarget(mBlueBtnValueAnim);
				valueAnimator.setDuration(1000).start();
				valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// TODO Auto-generated method stub
						mBlueBtnValueAnim.setTranslationY((Float) animation
								.getAnimatedValue());
					}
				});
			}
		});

		mBlueBtnPaowuxian = (Button) findViewById(R.id.blue_btn_paowuxian);
		mBlueBtnPaowuxian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int time = 1000;
				ValueAnimator valueAnimator = new ValueAnimator();
				valueAnimator.setObjectValues(new PointF(0f, 0f));// just for
																	// Evaluator
																	// type
				valueAnimator.setInterpolator(new LinearInterpolator());
				valueAnimator.setDuration(time);
				valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {

					@Override
					public PointF evaluate(float fraction, PointF startValue,
							PointF endValue) {
						// TODO Auto-generated method stub
						PointF pointF = new PointF();
						pointF.x = (mRelativeLayout.getWidth() - mBlueBtnPaowuxian
								.getLeft()) * fraction;// horizontal moving
						// distance

						float jia_su_du = 2.0f
								* (mRelativeLayout.getHeight() - mBlueBtnPaowuxian
										.getTop()) / (time * time);

						pointF.y = jia_su_du * (fraction * time)
								* (fraction * time) / 2.0f;// vertical moving
															// distance
						return pointF;
					}
				});
				valueAnimator.addListener(new AnimatorListenerAdapter() {

					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						super.onAnimationEnd(animation);
						mBlueBtnPaowuxian.setTranslationX(0f);
						mBlueBtnPaowuxian.setTranslationY(0f);
						Toast.makeText(MainActivity.this,
								R.string.paowuxian_anim_end, Toast.LENGTH_SHORT)
								.show();
					}

				});
				valueAnimator.start();
				valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// TODO Auto-generated method stub
						PointF pointF = (PointF) animation.getAnimatedValue();
						mBlueBtnPaowuxian.setTranslationX(pointF.x);
						mBlueBtnPaowuxian.setTranslationY(pointF.y);
					}
				});
			}
		});

		mGreenBtn = (Button) findViewById(R.id.green_btn);
		mGreenBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ObjectAnimator animator0 = ObjectAnimator.ofFloat(v, "x", 0,
						mRelativeLayout.getWidth() - mGreenBtn.getWidth());
				animator0.setDuration(100);
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "x",
						mRelativeLayout.getWidth() - mGreenBtn.getWidth(), 0);
				animator1.setDuration(1000);
				ObjectAnimator animator2 = ObjectAnimator.ofFloat(v,
						"rotation", 0f, -1080f);
				animator2.setDuration(1000);
				animator2.setInterpolator(new DecelerateInterpolator());

				AnimatorSet set = new AnimatorSet();
				set.play(animator0).before(animator1);
				set.play(animator1).with(animator2);
				set.start();
			}
		});

		mGreenBtnXml = (Button) findViewById(R.id.green_btn_xml);
		mGreenBtnXml.setPivotY(0f);
		mGreenBtnXml.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animator animator = AnimatorInflater.loadAnimator(
						MainActivity.this, R.animator.animator);
				animator.setTarget(mGreenBtnXml);
				animator.start();
			}
		});

		mGreenBtnXmlSet = (Button) findViewById(R.id.green_btn_xml_set);
		mGreenBtnXmlSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animator animator = AnimatorInflater.loadAnimator(
						MainActivity.this, R.animator.animator_set);
				animator.setTarget(mGreenBtnXmlSet);
				mGreenBtnXmlSet.setPivotX(0f);
				mGreenBtnXmlSet.setPivotY(0f);
				animator.start();
			}
		});

		final LinearLayout layoutAddDlt = (LinearLayout) findViewById(R.id.layout_transition);
		LayoutTransition layoutTransition = new LayoutTransition();
		layoutTransition.setAnimator(LayoutTransition.APPEARING,
				ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f));
		layoutAddDlt.setLayoutTransition(layoutTransition);

		final Button btnAdd = (Button) findViewById(R.id.add_button);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (layoutAddDlt.getChildCount() < 3) {
					Button button = new Button(MainActivity.this);
					button.setBackgroundColor(0x7fffff00);
					button.setWidth(200);
					button.setHeight(btnAdd.getHeight());
					button.setText(R.string.custom_layout_transition);
					layoutAddDlt.addView(button, 1);
				}
			}
		});
		final Button btnDlt = (Button) findViewById(R.id.dlt_button);
		btnDlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (layoutAddDlt.getChildCount() >= 3) {
					layoutAddDlt.removeViewAt(1);
				}
			}
		});

		final Button btnViewAnim = (Button) findViewById(R.id.view_anim_btn);
		btnViewAnim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.view_animation);
				btnViewAnim.startAnimation(animation);
				/*AnimationSet animationSet = new AnimationSet(false);
				TranslateAnimation animation2 = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
				animation2.setDuration(durationMillis);
				ScaleAnimation animation3 = new ScaleAnimation(fromX, toX, fromY, toY);
				animation3.setDuration(durationMillis);
				animationSet.addAnimation(animation2);
				animationSet.addAnimation(animation3);
				btnViewAnim.startAnimation(animationSet);*/
			}
		});

		ImageView imageView = (ImageView) findViewById(R.id.drawable_anim_image_view);
		// mAnimationDrawable = (AnimationDrawable)imageView.getBackground();
		mAnimationDrawable = (AnimationDrawable) imageView.getDrawable();

		final Button btnCamera = (Button) findViewById(R.id.camera_btn);
		btnCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation = new Animation() {

					@Override
					protected void applyTransformation(float interpolatedTime,
							Transformation t) {
						// TODO Auto-generated method stub
						super.applyTransformation(interpolatedTime, t);
						/*Matrix matrix = t.getMatrix();
						matrix.postScale(1.0f + interpolatedTime, 1.0f + interpolatedTime);*/
						Camera camera = new Camera();
						Matrix matrix = t.getMatrix();
						camera.rotateY(interpolatedTime * 360);
						camera.getMatrix(matrix);
						float mCenterX = btnCamera.getWidth() / 2.0f;
						float mCenterY = btnCamera.getHeight() / 2.0f;
						matrix.preTranslate(-mCenterX, -mCenterY);
						matrix.postTranslate(mCenterX, mCenterY);
						camera.restore();
					}

				};
				animation.setDuration(1000);
				btnCamera.startAnimation(animation);
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mAnimationDrawable.start();
		}
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
