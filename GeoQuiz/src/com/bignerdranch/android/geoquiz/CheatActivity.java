package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

	public static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
	public static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
	
	private Button mShowAnswer;
	private boolean mAnswerIsTrue;
	private TextView mAnswerTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		SetAnswerShownResult(false);
		
		Intent i = getIntent();
		mAnswerIsTrue = i.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
		
		//answer text
		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
		
		//show answer button
		mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
		mShowAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mAnswerIsTrue){
					mAnswerTextView.setText(R.string.true_button);
				}
				else{
					mAnswerTextView.setText(R.string.false_button);
				}
				SetAnswerShownResult(true);
			}
		});
	}

	protected void SetAnswerShownResult(boolean isAnswerShown){
		if(isAnswerShown){
			Intent i = new Intent();
			i.putExtra(EXTRA_ANSWER_SHOWN, true);
			setResult(RESULT_OK, i);
		}
	}
}

