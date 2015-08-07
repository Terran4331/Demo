package com.example.dictresolver;

import com.example.dictresolver.Words.Word;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.UserDictionary.Words;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private ContentResolver mContentResolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContentResolver = getContentResolver();
		mContentResolver.registerContentObserver(
				com.example.dictresolver.Words.Word.DICT_CONTENT_URI, true,
				new DictContentObserver(this, new Handler()));

		Button button = (Button) findViewById(R.id.search);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor cursor = mContentResolver.query(
						com.example.dictresolver.Words.Word.DICT_CONTENT_URI,
						null, null, null, null);
				int row = cursor.getCount();

				ListView listView = new ListView(MainActivity.this);
				listView.setAdapter(new CursorAdapter(MainActivity.this, cursor) {

					@Override
					public View newView(Context arg0, Cursor arg1,
							ViewGroup arg2) {
						// TODO Auto-generated method stub
						LayoutInflater inflater = (LayoutInflater) arg0
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View view = inflater.inflate(
								android.R.layout.simple_list_item_1, arg2,
								false);
						return view;
					}

					@Override
					public void bindView(View arg0, Context arg1, Cursor arg2) {
						// TODO Auto-generated method stub
						TextView textView = (TextView) arg0;
						textView.setText(arg2.getString(arg2
								.getColumnIndex("word"))
								+ ": "
								+ arg2.getString(arg2.getColumnIndex("detail")));
					}
				});

				new AlertDialog.Builder(MainActivity.this).setView(listView)
						.setPositiveButton("È·¶¨", null).create().show();
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
