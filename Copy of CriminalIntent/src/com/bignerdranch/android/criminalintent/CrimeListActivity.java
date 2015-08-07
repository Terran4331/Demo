package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity{

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		return new CrimeListFragment();
	}

}
