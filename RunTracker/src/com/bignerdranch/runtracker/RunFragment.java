package com.bignerdranch.runtracker;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RunFragment extends Fragment {

	public static final String EXTRA_RUN_ID = "run_id";
	public static final String LOCATION_ACTION = "com.bignerdranch.android.runtracker.LOCATION_ACTION";

	private long mRunID;
	private TextView mRunIdTextView;
	private TextView mLatitudeTextView, mlongitudeTextView, mAltitudeTextView,
			mElapseTimeTextView;
	private Button mStartBtn, mStopBtn, mMapBtn;
	private BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// Toast.makeText(getActivity(), "onReceive", Toast.LENGTH_SHORT)
			// .show();

			Location location = (Location) intent
					.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
			updateLocation(location);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mRunID = getArguments().getLong(EXTRA_RUN_ID, -1);
	}

	public static RunFragment newInstance(long runID) {
		RunFragment fragment = new RunFragment();
		Bundle args = new Bundle();
		args.putLong(EXTRA_RUN_ID, runID);
		fragment.setArguments(args);
		return fragment;
	}

	private void updateButtonStatus() {
		boolean bLocation = RunManager.get(getActivity()).isLocationEnable();
		long currentRun = RunManager.get(getActivity()).getCurrentRun();
		boolean bEnable = ((currentRun != mRunID) || (!bLocation));
		mStartBtn.setEnabled(bEnable);
		mStopBtn.setEnabled(!bEnable);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_run, container, false);
		mRunIdTextView = (TextView) view.findViewById(R.id.run_id_text_view);
		mElapseTimeTextView = (TextView) view
				.findViewById(R.id.run_durationTextView);
		mLatitudeTextView = (TextView) view
				.findViewById(R.id.run_latitudeTextView);
		mlongitudeTextView = (TextView) view
				.findViewById(R.id.run_longitudeTextView);
		mAltitudeTextView = (TextView) view
				.findViewById(R.id.run_altitudeTextView);

		mStartBtn = (Button) view.findViewById(R.id.run_startButton);
		mStartBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RunManager.get(getActivity()).startLocation(true, mRunID);
				IntentFilter filter = new IntentFilter(LOCATION_ACTION);
				getActivity().registerReceiver(mLocationReceiver, filter);
				updateButtonStatus();
			}
		});

		mStopBtn = (Button) view.findViewById(R.id.run_stopButton);
		mStopBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RunManager.get(getActivity()).stopLocation();
				updateButtonStatus();
			}
		});

		mMapBtn = (Button)view.findViewById(R.id.run_mapButton);
		mMapBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), RunMapActivity.class);
				intent.putExtra(RunMapFragment.EXTRA_RUN_ID, mRunID);
				startActivity(intent);
			}
		});
		
		updateButtonStatus();

		if (mRunID != -1) {
			Location location = RunManager.get(getActivity()).getLastLocation(
					mRunID);
			updateLocation(location);
			mRunIdTextView.setText(new Long(mRunID).toString());
		}

		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter filter = new IntentFilter(LOCATION_ACTION);
		if (mRunID == RunManager.get(getActivity()).getCurrentRun()) {
			getActivity().registerReceiver(mLocationReceiver, filter);
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		if (mRunID == RunManager.get(getActivity()).getCurrentRun()) {
			getActivity().unregisterReceiver(mLocationReceiver);
		}
		super.onStop();
	}

	private void updateLocation(Location location) {
		if (location != null) {
			mLatitudeTextView.setText(Double.toString(location.getLatitude()));
			mlongitudeTextView
					.setText(Double.toString(location.getLongitude()));
			mAltitudeTextView.setText(Double.toString(location.getAltitude()));
			Date date = new Date(location.getTime());
			mElapseTimeTextView.setText(date.toString());
		}
	}
	
	public void setRun(long runId){
		mRunID = runId;
		updateButtonStatus();
		Location location = RunManager.get(getActivity()).getLastLocation(
				mRunID);
		updateLocation(location);
		mRunIdTextView.setText(Long.toString(mRunID));
		
		IntentFilter filter = new IntentFilter(LOCATION_ACTION);
		if (mRunID == RunManager.get(getActivity()).getCurrentRun()) {
			getActivity().registerReceiver(mLocationReceiver, filter);
		}
		else {
			getActivity().unregisterReceiver(mLocationReceiver);
		}
	}
}
