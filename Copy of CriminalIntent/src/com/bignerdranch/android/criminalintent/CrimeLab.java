package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import android.content.Context;

public class CrimeLab {

	private static CrimeLab sCrimeLab;
	private ArrayList<Crime> mCrimes;
	private Context mAppContext;

	public CrimeLab(Context context) {
		mAppContext = context;
		mCrimes = new ArrayList<Crime>();
		/*for (int i = 0; i < 20; i++) {
			Crime crime = new Crime();
			crime.setSolved(i%2==0);
			crime.setTitle("crime #" + i);
			mCrimes.add(crime);
		}*/
	}

	public void addItem(int count){
		for (int i = 0; i < count; i++) {
			Crime crime = new Crime();
			crime.setSolved(i%2==0);
			crime.setTitle("crime #" + i);
			mCrimes.add(crime);
		}
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
}
