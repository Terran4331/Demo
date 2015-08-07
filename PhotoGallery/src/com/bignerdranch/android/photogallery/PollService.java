package com.bignerdranch.android.photogallery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PollService extends IntentService {
	private static final String TAG = "IntentService";
	public static final String NOTIFICATION = "NOTIFICATION";
	public static final String ACTION_SHOW_NOTIFICATION = "com.bignerdranch.android.photogallery.show_notification";
	public static final String PERMISSION = "com.bignerdranch.android.photogallery.PRIVATE";

	public PollService() {
		super(TAG);
	}

	public PollService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, PhotoGalleryActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		
		Notification notification = new NotificationCompat.Builder(this)
				.setTicker(getString(R.string.notify_ticker))
				.setSmallIcon(android.R.drawable.ic_menu_report_image)
				.setContentTitle(getString(R.string.notify_title))
				.setContentText(getString(R.string.notify_content))
				.setContentIntent(pi)
				.setAutoCancel(true).build();

		Intent intent2 = new Intent(ACTION_SHOW_NOTIFICATION);
		intent2.putExtra(NOTIFICATION, notification);
		sendOrderedBroadcast(intent2, PERMISSION, null, null, Activity.RESULT_OK, null, null);
	}

	public static void setServiceAlarm(Context context, boolean isOn) {

		Intent intent = new Intent(context, PollService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

		AlarmManager am = (AlarmManager) context
				.getSystemService(ALARM_SERVICE);
		if (isOn) {
			am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
					10 * 1000, pi);
		} else {
			am.cancel(pi);
			pi.cancel();
		}

	}

	public static boolean isServiceOn(Context context) {
		Intent intent = new Intent(context, PollService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}
}
