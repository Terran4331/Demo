package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends Activity {

	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private static final String KEY_IS_CHEATER = "is_cheater";
	
	private Button mTrueButton;
	private Button mFalseButton;
	private Button mCheatButton;
	private Button mNextButton;
	private TextView mQuestionTextView;
	private int mCurrentIndex = 0;
	private boolean[] mIsCheater = new boolean[]{
			false,false,false
	};

	private TrueFalse[] mQuestionBank = new TrueFalse[] {
			new TrueFalse(R.string.question_oceans, true),
			new TrueFalse(R.string.question_america, false),
			new TrueFalse(R.string.question_asia, false),
	};

	//update question
	private void updateQuestion(){
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);
	}
	
	private void checkAnswer(boolean userPressedTrue){
		boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
		int messageResId = 0;
		if(mIsCheater[mCurrentIndex]){
			messageResId = R.string.judgment_toast;
		}
		else{
			if(answerIsTrue == userPressedTrue){
				messageResId = R.string.correct_toast;
			}
			else{
				messageResId = R.string.incorrect_toast;
			}	
		}
		
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	
	//oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        if(savedInstanceState != null){
        	mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        	mIsCheater = savedInstanceState.getBooleanArray(KEY_IS_CHEATER);
        }
        
        //question text view
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        
        //question 0 first
        updateQuestion();
        
        //true button
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkAnswer(true);
			}
		});
        
        //false button
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkAnswer(false);
			}
		});
        
        //cheat button
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuizActivity.this, CheatActivity.class);
				i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, mQuestionBank[mCurrentIndex].isTrueQuestion());
				startActivityForResult(i, mCurrentIndex);
			}
		});
        
        //next button
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCurrentIndex = (mCurrentIndex + 1)%mQuestionBank.length;
				updateQuestion();
			}
		});
    }


    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_INDEX, mCurrentIndex);
		outState.putBooleanArray(KEY_INDEX, mIsCheater);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg2 == null){
			return;
		}
		if(mIsCheater[arg0] == false){
			mIsCheater[arg0] = arg2.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);	
		}
	}

}
