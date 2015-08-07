package com.bignerdranch.android.photogallery;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		int resultCode = getResultCode();
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		Notification notification = intent
				.getParcelableExtra(PollService.NOTIFICATION);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}

}
