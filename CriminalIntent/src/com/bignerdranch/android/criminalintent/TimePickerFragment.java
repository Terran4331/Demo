package com.bignerdranch.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment {

	public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";
	
	private Date mOldDate;
	private TimePicker mTimePicker;
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		mOldDate = (Date)getArguments().getSerializable(EXTRA_TIME);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mOldDate);
		
		mTimePicker = (TimePicker)view.findViewById(R.id.dialog_date_timepicker);
		mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		
		return new AlertDialog.Builder(getActivity())
		.setView(view)
		.setTitle(R.string.time_picker_title)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				CrimeFragment fragment = (CrimeFragment)getTargetFragment();
				if (fragment != null) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(mOldDate);
					calendar.set(calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
					calendar.set(calendar.MINUTE, mTimePicker.getCurrentMinute());
					Intent i = new Intent();
					i.putExtra(EXTRA_TIME, calendar.getTime());
					fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
				}
			}
		})
		.create();
	}

	public static TimePickerFragment newInstance(Date date){
		TimePickerFragment fragment = new TimePickerFragment();
		Bundle argsBundle = new Bundle();
		argsBundle.putSerializable(EXTRA_TIME, date);
		fragment.setArguments(argsBundle);
		return fragment;
	}
}
