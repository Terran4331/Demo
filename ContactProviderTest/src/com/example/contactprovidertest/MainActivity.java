package com.example.contactprovidertest;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.show_cnt_btn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ArrayList<String> names = new ArrayList<String>();
				final ArrayList<ArrayList<String>> details = new ArrayList<ArrayList<String>>();
				Cursor cursor = getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI, null, null,
						null, null);
				while (cursor.moveToNext()) {
					// id
					String cntId = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));
					// name
					names.add(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
					// detail
					ArrayList<String> detail = new ArrayList<String>();
					Cursor tCursor = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
									+ cntId, null, null);
					while (tCursor.moveToNext()) {
						String tel = tCursor.getString(tCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						detail.add("电话：" + tel);
					}
					tCursor.close();
					details.add(detail);
				}
				cursor.close();

				ExpandableListView listView = new ExpandableListView(
						MainActivity.this);
				ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

					@Override
					public boolean isChildSelectable(int groupPosition,
							int childPosition) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean hasStableIds() {
						// TODO Auto-generated method stub
						return false;
					}

					private TextView getTextView() {
						TextView textView = new TextView(MainActivity.this);
						AbsListView.LayoutParams params = new AbsListView.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT, 64);
						textView.setLayoutParams(params);
						textView.setTextSize(20);
						textView.setPadding(36, 0, 0, 0);
						return textView;
					}

					@Override
					public View getGroupView(int groupPosition,
							boolean isExpanded, View convertView,
							ViewGroup parent) {
						// TODO Auto-generated method stub
						TextView view = getTextView();
						view.setText(getGroup(groupPosition).toString());
						return view;
					}

					@Override
					public long getGroupId(int groupPosition) {
						// TODO Auto-generated method stub
						return groupPosition;
					}

					@Override
					public int getGroupCount() {
						// TODO Auto-generated method stub
						int groupCount = names.size();
						return groupCount;
					}

					@Override
					public Object getGroup(int groupPosition) {
						// TODO Auto-generated method stub
						return names.get(groupPosition);
					}

					@Override
					public int getChildrenCount(int groupPosition) {
						// TODO Auto-generated method stub
						return details.get(groupPosition).size();
					}

					@Override
					public View getChildView(int groupPosition,
							int childPosition, boolean isLastChild,
							View convertView, ViewGroup parent) {
						// TODO Auto-generated method stub
						TextView view = getTextView();
						view.setText(details.get(groupPosition)
								.get(childPosition).toString());
						return view;
					}

					@Override
					public long getChildId(int groupPosition, int childPosition) {
						// TODO Auto-generated method stub
						return childPosition;
					}

					@Override
					public Object getChild(int groupPosition, int childPosition) {
						// TODO Auto-generated method stub
						return details.get(groupPosition).get(childPosition);
					}
				};
				listView.setAdapter(adapter);
				new AlertDialog.Builder(MainActivity.this).setView(listView)
						.setPositiveButton("确定", null).create().show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
