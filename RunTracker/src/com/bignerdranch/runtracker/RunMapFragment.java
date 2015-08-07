package com.bignerdranch.runtracker;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.runtracker.RunDatabaseHelper.LocationCursor;
import com.google.android.gms.internal.mc;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RunMapFragment extends SupportMapFragment implements
		LoaderCallbacks<Cursor> {

	public static final String EXTRA_RUN_ID = "runid";

	private long mRunId;
	private LocationCursor mCursor;
	private GoogleMap mGoogleMap;
	private LocationReceiver mReceiver = new LocationReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			super.onReceive(context, intent);
			Location location = (Location) intent
					.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
			if (location != null) {
				mGoogleMap.clear();
				reloadMapLocation();
			}
		}
		
	};

	public void reloadMapLocation(){
		LoaderManager lm = getLoaderManager();
		lm.restartLoader(0, null, this);
	}
	
	public static RunMapFragment newInstance(long runId) {
		RunMapFragment fragment = new RunMapFragment();
		Bundle args = new Bundle();
		args.putLong(EXTRA_RUN_ID, runId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle argsBundle = getArguments();
		if (argsBundle != null) {
			mRunId = argsBundle.getLong(EXTRA_RUN_ID, -1);
			LoaderManager lm = getLoaderManager();
			lm.initLoader(0, null, this);
		} else {
			mRunId = -1;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mGoogleMap = getMap();
		mGoogleMap.setMyLocationEnabled(true);
		return view;
	}

	private static class LocationCursorLoader extends SQLCursorLoader {

		private long mRunId;

		public LocationCursorLoader(Context context, long runId) {
			super(context);
			// TODO Auto-generated constructor stub
			mRunId = runId;
		}

		@Override
		protected Cursor getCursor() {
			// TODO Auto-generated method stub
			return RunManager.get(mContext).getLocationCursor(mRunId);
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter filter = new IntentFilter(RunFragment.LOCATION_ACTION);
		getActivity().registerReceiver(mReceiver, filter);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		getActivity().unregisterReceiver(mReceiver);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new LocationCursorLoader(getActivity(), mRunId);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		mCursor = (LocationCursor) arg1;
		updateUI();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
			mCursor = null;
		}
	}

	private void updateUI() {
		if (mCursor == null || mGoogleMap == null) {
			return;
		}

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		PolylineOptions line = new PolylineOptions();
		mCursor.moveToFirst();
		while (!mCursor.isAfterLast()) {
			Location loc = mCursor.getLocation();
			LatLng point = new LatLng(loc.getLatitude(), loc.getLongitude());
			line.add(point);
			builder.include(point);
			
			if (mCursor.isFirst()) {
				String startDate = new Date(loc.getTime()).toString();
				MarkerOptions startMarker = new MarkerOptions().position(point)
						.title(getString(R.string.start_marker))
						.snippet(getString(R.string.start_format, startDate));
				mGoogleMap.addMarker(startMarker);
			} else if (mCursor.isLast()) {
				String stopDate = new Date(loc.getTime()).toString();
				MarkerOptions startMarker = new MarkerOptions().position(point)
						.title(getString(R.string.stop_marker))
						.snippet(getString(R.string.stop_format, stopDate));
				mGoogleMap.addMarker(startMarker);
			}

			mCursor.moveToNext();
		}
		mGoogleMap.addPolyline(line);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		LatLngBounds bounds = builder.build();
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,
				display.getWidth(), display.getHeight(), 15);
		mGoogleMap.moveCamera(cameraUpdate);
	}
}
