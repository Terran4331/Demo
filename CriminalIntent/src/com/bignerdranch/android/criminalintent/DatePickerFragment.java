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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {

	private Date mOldDate;
	private DatePicker mDatePicker;
	public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

	public static DatePickerFragment newInstance(Date date) {
		DatePickerFragment fragment = new DatePickerFragment();
		Bundle argsBundle = new Bundle();
		argsBundle.putSerializable(EXTRA_DATE, date);
		fragment.setArguments(argsBundle);
		return fragment;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_date, null);
		mOldDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mOldDate);
		mDatePicker = (DatePicker)view.findViewById(R.id.dialog_date_datepicker);
		mDatePicker.init(calendar.get(Calendar.YEAR)
				, calendar.get(Calendar.MONTH)
				, calendar.get(Calendar.DAY_OF_MONTH)
				, new OnDateChangedListener() {
					
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO Auto-generated method stub
					}
				});

		return new AlertDialog.Builder(getActivity()).setView(view)
				.setTitle(R.string.date_picker_title)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sendResult(Activity.RESULT_OK);
					}
				})
				.create();
	}

	private void sendResult(int resultCode){
		CrimeFragment fragment = (CrimeFragment)getTargetFragment();
		if (fragment != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(mOldDate);
			calendar.set(calendar.YEAR, mDatePicker.getYear());
			calendar.set(calendar.MONTH, mDatePicker.getMonth());
			calendar.set(calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
			Intent i = new Intent();
			i.putExtra(EXTRA_DATE, calendar.getTime());
			fragment.onActivityResult(getTargetRequestCode(), resultCode, i);
		}
	}
}
