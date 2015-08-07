package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DateTimeFragment extends DialogFragment {

	public static final String EXTRA_MODIFY_DATE_TIME = "com.bignerdranch.android.criminalintent.mofify_date_time";
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.date_time_title)
		.setPositiveButton(R.string.modify_date_btn, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sendResult(true);
			}
		})
		.setNegativeButton(R.string.modify_time_btn, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sendResult(false);
			}
		})
		.create();
	}

	private void sendResult(Boolean bModifyDate){
		CrimeFragment tgt = (CrimeFragment)getTargetFragment();
		if (tgt != null) {
			Intent i = new Intent();
			i.putExtra(EXTRA_MODIFY_DATE_TIME, bModifyDate);
			tgt.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
		}
	}
}
