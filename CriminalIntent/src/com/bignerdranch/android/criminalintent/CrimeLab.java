package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import android.R.bool;
import android.content.Context;

public class CrimeLab {

	private static final String FILENAME = "crimes.json"; 
	private static CrimeLab sCrimeLab;
	private ArrayList<Crime> mCrimes;
	private Context mAppContext;
	private CriminalIntentJSONSerializer mSerializer;

	private CrimeLab(Context context) {
		mAppContext = context;
		mSerializer = new CriminalIntentJSONSerializer(context, FILENAME);
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (mCrimes == null) {
			mCrimes = new ArrayList<Crime>();
		}
	}

	public void addItem(int count){
		for (int i = 0; i < count; i++) {
			Crime crime = new Crime();
			crime.setSolved(i%2==0);
			crime.setTitle("crime #" + i);
			mCrimes.add(crime);
		}
	}
	
	public void deleteItem(Crime crime){
		mCrimes.remove(crime);
	}
	
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}

	public Crime getCrime(UUID id) {
		for (Crime c : mCrimes) {
			if (c.getID().equals(id)) {
				return c;
			}
		}
		return null;
	}

	public static CrimeLab get(Context context) {
		if (sCrimeLab == null) {
			sCrimeLab = new CrimeLab(context.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public boolean saveCrimes(){
		try {
			mSerializer.saveCrimes(mCrimes);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
