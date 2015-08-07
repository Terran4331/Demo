package com.bignerdranch.runtracker;

import java.util.zip.Inflater;

import com.bignerdranch.runtracker.RunDatabaseHelper.RunCursor;

import android.R.array;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RunListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	public static final int REQUEST_RUNACTIVITY = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getLoaderManager().initLoader(0, null, this);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.run, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.add_run) {
			long runID = RunManager.get(getActivity()).addRun();
			Intent intent = new Intent(getActivity(), RunActivity.class);
			intent.putExtra(RunFragment.EXTRA_RUN_ID, runID);
			startActivityForResult(intent, REQUEST_RUNACTIVITY);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), RunActivity.class);
		intent.putExtra(RunFragment.EXTRA_RUN_ID, id);
		startActivityForResult(intent, REQUEST_RUNACTIVITY);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_RUNACTIVITY) {
			getLoaderManager().restartLoader(0, null, this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new RunListCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		final RunCursor runCursor = (RunCursor) arg1;
		CursorAdapter adapter = new CursorAdapter(getActivity(), runCursor) {

			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(
						android.R.layout.simple_list_item_1, arg2, false);
				return view;
			}

			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				// TODO Auto-generated method stub
				Run run = runCursor.getRun();
				TextView view = (TextView) arg0;
				view.setText(run.getStartDate().toString());
			}
		};
		setListAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		setListAdapter(null);
	}
	
	private static class RunListCursorLoader extends SQLCursorLoader{

		public RunListCursorLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Cursor getCursor() {
			// TODO Auto-generated method stub
			return RunManager.get(mContext).getRunCursor();
		}
		
	}
}
