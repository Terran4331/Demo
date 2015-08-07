package com.example.dict;

import android.R.integer;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DictProvider extends ContentProvider {

	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int WORDS = 1;
	private static final int WORD = 2;
	private static MyDatabaseHelper mDatabaseHelper;
	static {
		matcher.addURI(Words.AUTHORITY, "words", WORDS);
		matcher.addURI(Words.AUTHORITY, "word/#", WORD);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDatabaseHelper = new MyDatabaseHelper(this.getContext(), "myDict.db3",
				null, 1);
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		switch (matcher.match(uri)) {
		case WORDS:
			
			Cursor cursor = mDatabaseHelper.getReadableDatabase().query("dict",
					projection, null, selectionArgs, null, null, sortOrder);
			return cursor;
		default:
			break;
		}
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
