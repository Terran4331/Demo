package com.bignerdranch.runtracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public abstract class SQLCursorLoader extends AsyncTaskLoader<Cursor> {

	protected Context mContext;
	private Cursor mCursor;
	
	protected abstract Cursor getCursor();
	
	public SQLCursorLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public Cursor loadInBackground() {
		// TODO Auto-generated method stub
		mCursor = getCursor(); 
		if (mCursor != null) {
			mCursor.getCount();
		}
		return mCursor;
	}

	@Override
	public void onCanceled(Cursor data) {
		// TODO Auto-generated method stub
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
		onStopLoading();
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		// TODO Auto-generated method stub
		cancelLoad();
	}

	@Override
	public void deliverResult(Cursor data) {
		// TODO Auto-generated method stub
		Cursor oldCursor = mCursor;
		if (isStarted()) {
			super.deliverResult(data);
		}
		if (oldCursor != null && oldCursor != data && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}
	
}
