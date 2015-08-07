package com.bignerdranch.runtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;

import com.bignerdranch.runtracker.RunDatabaseHelper.LocationCursor;
import com.bignerdranch.runtracker.RunDatabaseHelper.RunCursor;

public class RunManager {

	private static final String PREF_FILES = "runs";
	private static final String PREF_CURRENT_RUN_ID = "RunManager.runid";
	private static final String DB_NAME = "runs.sqlite";
	private static final String LOCATION_PROVIDER = "com.bignerdranch.android.runtracker.LOCATION_PROVIDER";
	private static final String TEST_PROVIDER = "TEST_PROVIDER";
	private static RunManager sRunManager;
	private Context mContext;
	private RunDatabaseHelper mDatabaseHelper;
	private long mCurrentRunID;
	private SharedPreferences mPreferences;

	private RunManager(Context context) {
		mContext = context;
		mDatabaseHelper = new RunDatabaseHelper(context, DB_NAME, null, 1);
		mPreferences = mContext.getSharedPreferences(PREF_FILES,
				Context.MODE_PRIVATE);
		mCurrentRunID = mPreferences.getLong(PREF_CURRENT_RUN_ID, -1);
	}

	public static RunManager get(Context context) {
		if (sRunManager == null) {
			sRunManager = new RunManager(context);
		}
		return sRunManager;
	}

	public long addRun() {
		Run run = new Run();
		mCurrentRunID = mDatabaseHelper.insertRun(run);
		return mCurrentRunID;
	}

	public long getCurrentRun() {
		return mCurrentRunID;
	}

	public Location getLastLocation(long runID) {
		LocationCursor cursor = mDatabaseHelper.getLastLocationCursor(runID);
		cursor.moveToFirst();
		Location location = cursor.getLocation();
		cursor.close();
		return location;
	}

	public void addLocation(Location loc) {
		if (mCurrentRunID != -1) {
			mDatabaseHelper.insertLocation(loc, mCurrentRunID);

			Intent i = new Intent(mContext, RunActivity.class);
			i.putExtra(RunFragment.EXTRA_RUN_ID, mCurrentRunID);
			PendingIntent intent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			
			String runID = Long.toString(mCurrentRunID);
			Notification notification = new NotificationCompat.Builder(mContext)
					.setContentTitle("runtracker location changed")
					.setContentText((CharSequence)(runID))
					.setSmallIcon(android.R.drawable.ic_menu_report_image)
					.setAutoCancel(true).setContentIntent(intent).build();

			NotificationManager nmManager = (NotificationManager) mContext
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nmManager.notify(0, notification);
		}
	}

	public void startLocation(boolean bCreate, long runID) {
		String provider = LocationManager.GPS_PROVIDER;

		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.getProvider(TEST_PROVIDER) != null
				&& locationManager.isProviderEnabled(TEST_PROVIDER)) {
			provider = TEST_PROVIDER;
		}

		Location lastKnow = locationManager.getLastKnownLocation(provider);
		if (lastKnow != null) {
			lastKnow.setTime(System.currentTimeMillis());
			Intent broadcastIntent = new Intent(RunFragment.LOCATION_ACTION);
			broadcastIntent.putExtra(locationManager.KEY_LOCATION_CHANGED,
					lastKnow);
		}

		PendingIntent intent = getLocationPendingIntent(true);
		locationManager.requestLocationUpdates(provider, 2000, 0, intent);

		mCurrentRunID = runID;

		mPreferences.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunID)
				.commit();
	}

	public void stopLocation() {
		PendingIntent intent = getLocationPendingIntent(false);
		if (intent != null) {
			LocationManager locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(intent);
			intent.cancel();
		}

		mCurrentRunID = -1;
		mPreferences.edit().remove(PREF_CURRENT_RUN_ID).commit();
	}

	private PendingIntent getLocationPendingIntent(boolean bCreate) {
		Intent i = new Intent(RunFragment.LOCATION_ACTION);
		PendingIntent intent = PendingIntent.getBroadcast(mContext, 0, i,
				bCreate ? 0 : PendingIntent.FLAG_NO_CREATE);
		return intent;
	}

	public boolean isLocationEnable() {
		return (getLocationPendingIntent(false) != null);
	}

	public RunCursor getRunCursor() {
		return mDatabaseHelper.getRunCursor();
	}

	public LocationCursor getLocationCursor(long runId){
		return mDatabaseHelper.getLocationCursor(runId);
	}
}
