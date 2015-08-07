package com.bignerdranch.android.nerdlauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppFragment extends ListFragment {

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		PackageManager pManager = getActivity().getPackageManager();
		List<ResolveInfo> activities = pManager
				.queryIntentActivities(intent, 0);
		Collections.sort(activities, new Comparator<ResolveInfo>() {

			@Override
			public int compare(ResolveInfo lhs, ResolveInfo rhs) {
				// TODO Auto-generated method stub
				PackageManager pm = getActivity().getPackageManager();
				return String.CASE_INSENSITIVE_ORDER.compare(lhs.loadLabel(pm)
						.toString(), rhs.loadLabel(pm).toString());
			}

		});

		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
				getActivity(), 0, activities) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				ResolveInfo info = (ResolveInfo) getItem(position);
				if (convertView == null) {
					convertView = getActivity().getLayoutInflater().inflate(
							R.layout.app_list_item, null);
				}
				TextView label = (TextView) convertView
						.findViewById(R.id.app_label);
				//label.setText(info.activityInfo.packageName);
				
				label.setText(info.loadLabel(getActivity().getPackageManager()));
				ImageView icon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				icon.setBackground(info.loadIcon(getActivity()
						.getPackageManager()));
				return convertView;
			}

		};

		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		ResolveInfo info = (ResolveInfo) getListAdapter().getItem(position);
		ActivityInfo activityInfo = info.activityInfo;

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName(activityInfo.packageName, activityInfo.name);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

}
