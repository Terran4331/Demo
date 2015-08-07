package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {

	public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
	public static final String EXTRA_DATE_ID = "com.bignerdranch.android.criminalintent.date_id";
	public static final String EXTRA_TIME_ID = "com.bignerdranch.android.criminalintent.time_id";
	public static final String EXTRA_DATE_TIME_ID = "com.bignerdranch.android.criminalintent.date_time_id";
	public static int EXTRA_REQUEST_DATE = 0;
	public static int EXTRA_REQUEST_TIME = 1;
	public static int EXTRA_REQUEST_DATE_TIME = 2;
	private Crime mCrime;
	private Button mBtnDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle argsBundle = getArguments();
		UUID idUuid = (UUID) argsBundle.getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(idUuid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_crime, container, false);

		EditText title = (EditText) v.findViewById(R.id.crime_title);
		title.setText(mCrime.getTitle());
		title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mCrime.setTitle(s.toString());
			}
		});

		mBtnDate = (Button) v.findViewById(R.id.crime_date);
		updateDate();
		mBtnDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateTimeFragment dtFragment = new DateTimeFragment();
				FragmentManager fmFragmentManager = getActivity()
						.getSupportFragmentManager();
				dtFragment.setTargetFragment(CrimeFragment.this,
						EXTRA_REQUEST_DATE_TIME);
				dtFragment.show(fmFragmentManager, EXTRA_DATE_TIME_ID);
			}
		});

		CheckBox checkBox = (CheckBox) v.findViewById(R.id.crime_solved);
		checkBox.setChecked(mCrime.isSolved());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mCrime.setSolved(isChecked);
			}
		});

		return v;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	public static CrimeFragment newInstance(UUID crimdID) {
		CrimeFragment fragment = new CrimeFragment();
		Bundle args = new Bundle();
		args.putSerializable(CrimeFragment.EXTRA_CRIME_ID, crimdID);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == EXTRA_REQUEST_DATE) {
			Date date = (Date) data
					.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		} 
		else if (requestCode == EXTRA_REQUEST_TIME) {
			Date date = (Date) data
					.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateDate();
		} 
		else if (requestCode == EXTRA_REQUEST_DATE_TIME) {
			boolean bModifyDate = data.getBooleanExtra(
					DateTimeFragment.EXTRA_MODIFY_DATE_TIME, true);
			if (bModifyDate) {
				// modify date
				DatePickerFragment dateFragment = DatePickerFragment
						.newInstance(mCrime.getDate());
				dateFragment.setTargetFragment(CrimeFragment.this,
						EXTRA_REQUEST_DATE);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dateFragment.show(fm, EXTRA_DATE_ID);
			} else {
				// modify time
				TimePickerFragment timeFragment = TimePickerFragment
						.newInstance(mCrime.getDate());
				timeFragment.setTargetFragment(CrimeFragment.this,
						EXTRA_REQUEST_TIME);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				timeFragment.show(fm, EXTRA_TIME_ID);
			}
		}
	}

	private void updateDate() {
		mBtnDate.setText(mCrime.getDate().toString());
	}
}
