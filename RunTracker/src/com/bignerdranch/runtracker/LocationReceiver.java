package com.bignerdranch.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class LocationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Location location = (Location) intent
				.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
		if (location != null) {
			RunManager.get(context).addLocation(location);
		}
	}

}
