package com.example.dict;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.AndroidCharacter;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	MyDatabaseHelper mDatabaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDatabaseHelper = new MyDatabaseHelper(this, "myDict.db3", null, 1);

		Button addButton = (Button) findViewById(R.id.add);
		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String word = ((EditText) findViewById(R.id.word)).getText()
						.toString();
				if (word.length() == 0) {
					return;
				}
				
				String detail = ((EditText) findViewById(R.id.detail))
						.getText().toString();
				
				Cursor cursor = mDatabaseHelper.getReadableDatabase().query(
						"dict", null, "word='" + word + "'", null, null, null, null);
				if (cursor.moveToNext()) {
					mDatabaseHelper.getReadableDatabase().execSQL(
							"update dict set detail='" + detail + "' where word='" + word +"'");
					return;
				}
				
				mDatabaseHelper.getReadableDatabase().execSQL(
						"insert into dict values(null, ?, ?)",
						new String[] { word, detail });
				
				getContentResolver().notifyChange(Words.Word.DICT_CONTENT_URI, null);
			}
		});

		Button searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String key = ((EditText) findViewById(R.id.search_key))
						.getText().toString();
				Cursor cursor = mDatabaseHelper.getReadableDatabase().query(
						"dict", null, (key.length() == 0)?null:("word='" + key + "'"), null, null, null, null);

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
