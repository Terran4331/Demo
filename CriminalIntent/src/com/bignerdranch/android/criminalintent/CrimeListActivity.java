package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CrimeListActivity extends SingleFragmentActivity implements
		CrimeListFragment.CallBack, CrimeFragment.CallBack {

	@Override
	protected int getLayoutRsID() {
		// TODO Auto-generated method stub
		return R.layout.activity_masterdetail;
	}

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		return new CrimeListFragment();
	}

	@Override
	public void onItemSelected(Crime crime) {
		// TODO Auto-generated method stub
		if (findViewById(R.id.crime_detail_container) != null) {
			//show detail in right panel
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			Fragment detailFragment = fm.findFragmentById(R.id.crime_detail_container); 
			if (detailFragment != null) {
				transaction.remove(detailFragment);
			}
			CrimeFragment fragment = (CrimeFragment)CrimeFragment.newInstance(crime.getID());
			transaction.add(R.id.crime_detail_container, fragment);
			transaction.commit();
		} else {
			//show detail in new activity
			Intent i = new Intent(this, CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());
			startActivity(i);
		}
	}

	@Override
	public void onCrimeDetailChanged(Crime crime) {
		// TODO Auto-generated method stub
		if (findViewById(R.id.crime_detail_container) != null) {
			//if detail is showed in right panel, update list right now
			FragmentManager fm = getSupportFragmentManager();
			CrimeListFragment listFragment = (CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);
			if (listFragment != null) {
				listFragment.updateData();
			}
		}
	}

}
