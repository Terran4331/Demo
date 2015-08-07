package com.bignerdranch.android.photogallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class VisibleFragment extends Fragment {

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@SuppressLint("ShowToast")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			setResultCode(Activity.RESULT_CANCELED);
			Toast.makeText(context, R.string.notify_toast, Toast.LENGTH_LONG)
					.show();
		}
	};

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter(
				PollService.ACTION_SHOW_NOTIFICATION);
		getActivity().registerReceiver(mBroadcastReceiver, filter, PollService.PERMISSION, null);
	}

}
