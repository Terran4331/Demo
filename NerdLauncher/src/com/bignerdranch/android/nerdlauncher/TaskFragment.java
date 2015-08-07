package com.bignerdranch.android.nerdlauncher;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.AppTask;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskFragment extends ListFragment {

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.L)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ActivityManager am = (ActivityManager)getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
		//List<RunningTaskInfo> tasks = am.getRunningTasks(100);
		List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
		
		ArrayAdapter<RunningAppProcessInfo> adapter = new ArrayAdapter<RunningAppProcessInfo>(
				getActivity(), 0, processes) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				RunningAppProcessInfo info = (RunningAppProcessInfo) getItem(position);

				if (convertView == null) {
					convertView = getActivity().getLayoutInflater().inflate(
							R.layout.task_list_item, null);
				}
				
				TextView label = (TextView) convertView
						.findViewById(R.id.task_label);
				label.setText(info.processName);
				
				/*ImageView icon = (ImageView) convertView
						.findViewById(R.id.task_icon);*/
				
				return convertView;
			}

		};

		setListAdapter(adapter);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		/*super.onListItemClick(l, v, position, id);
		RunningTaskInfo info = (RunningTaskInfo) getListAdapter().getItem(position);
		ActivityManager am = (ActivityManager)getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
		am.moveTaskToFront(info.id, 0);*/
	}

}
