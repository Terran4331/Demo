package com.bignerdranch.runtracker;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class RunDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_TABLE_RUN = "run";
	private static final String DB_TABLE_RUN_COLUMN_ID = "_id";
	private static final String DB_TABLE_RUN_COLUMN_START_DATE = "start_date";

	private static final String DB_TABLE_LOCATION = "location";
	private static final String DB_TABLE_LOCATION_COLUMN_ID = "run_id";
	private static final String DB_TABLE_LOCATION_COLUMN_TIMESTAMP = "timestamp";
	private static final String DB_TABLE_LOCATION_COLUMN_LATITUDE = "latitude";
	private static final String DB_TABLE_LOCATION_COLUMN_LONGITUDE = "longitude";
	private static final String DB_TABLE_LOCATION_COLUMN_ALTITUDE = "altitude";
	private static final String DB_TABLE_LOCATION_COLUMN_PROVIDER = "provider";

	public RunDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table run (_id integer primary key autoincrement, start_date integer)");
		db.execSQL("create table location (run_id integer references run(_id), timestamp integer, latitude real, longitude read, altitude real, provider varchar(100))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public RunCursor getRunCursor() {
		Cursor cursor = getReadableDatabase().query(DB_TABLE_RUN, null, null,
				null, null, null, DB_TABLE_RUN_COLUMN_START_DATE + " asc");
		return new RunCursor(cursor);
	}

	public LocationCursor getLastLocationCursor(long runID) {
        Cursor wrapped = getReadableDatabase().query(DB_TABLE_LOCATION, 
                null, // all columns 
                DB_TABLE_LOCATION_COLUMN_ID + " = ?", // limit to the given run
                new String[]{ String.valueOf(runID) }, 
                null, // group by
                null, // having
                DB_TABLE_LOCATION_COLUMN_TIMESTAMP + " desc", // order by latest first
                "1"); // limit 1
        return new LocationCursor(wrapped);

	}

	public LocationCursor getLocationCursor(long runID) {
        Cursor wrapped = getReadableDatabase().query(DB_TABLE_LOCATION, 
                null, // all columns 
                DB_TABLE_LOCATION_COLUMN_ID + " = ?", // limit to the given run
                new String[]{ String.valueOf(runID) }, 
                null, // group by
                null, // having
                DB_TABLE_LOCATION_COLUMN_TIMESTAMP + " asc" // order by latest first
                );
        return new LocationCursor(wrapped);

	}

	public long insertRun(Run run) {
		ContentValues values = new ContentValues();
		values.put(DB_TABLE_RUN_COLUMN_START_DATE, run.getStartDate().getTime());
		return getWritableDatabase().insert(DB_TABLE_RUN, null, values);
	}

	public void insertLocation(Location loc, long runID) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DB_TABLE_LOCATION_COLUMN_ID, runID);
		contentValues.put(DB_TABLE_LOCATION_COLUMN_TIMESTAMP, loc.getTime());
		contentValues.put(DB_TABLE_LOCATION_COLUMN_LATITUDE, loc.getLatitude());
		contentValues.put(DB_TABLE_LOCATION_COLUMN_LONGITUDE,
				loc.getLongitude());
		contentValues.put(DB_TABLE_LOCATION_COLUMN_ALTITUDE, loc.getAltitude());
		contentValues.put(DB_TABLE_LOCATION_COLUMN_PROVIDER, loc.getProvider());
		getWritableDatabase().insert(DB_TABLE_LOCATION, null, contentValues);
	}

	public class RunCursor extends CursorWrapper {

		public RunCursor(Cursor cursor) {
			super(cursor);
			// TODO Auto-generated constructor stub
		}

		public Run getRun() {
			if (isBeforeFirst() || isAfterLast()) {
				return null;
			}
			long runid = getLong(getColumnIndex(DB_TABLE_RUN_COLUMN_ID));
			long startdate = getLong(getColumnIndex(DB_TABLE_RUN_COLUMN_START_DATE));
			Run run = new Run();
			run.setId(runid);
			run.setStartDate(new Date(startdate));
			return run;
		}
	}

	public class LocationCursor extends CursorWrapper {

		public LocationCursor(Cursor cursor) {
			super(cursor);
			// TODO Auto-generated constructor stub
		}

		public Location getLocation(){
            if (isBeforeFirst() || isAfterLast())
                return null;
            // first get the provider out so we can use the constructor
            String provider = getString(getColumnIndex(DB_TABLE_LOCATION_COLUMN_PROVIDER));
            Location loc = new Location(provider);
            // populate the remaining properties
            loc.setLongitude(getDouble(getColumnIndex(DB_TABLE_LOCATION_COLUMN_LONGITUDE)));
            loc.setLatitude(getDouble(getColumnIndex(DB_TABLE_LOCATION_COLUMN_LATITUDE)));
            loc.setAltitude(getDouble(getColumnIndex(DB_TABLE_LOCATION_COLUMN_ALTITUDE)));
            loc.setTime(getLong(getColumnIndex(DB_TABLE_LOCATION_COLUMN_TIMESTAMP)));
            return loc;
		}
	}
}
