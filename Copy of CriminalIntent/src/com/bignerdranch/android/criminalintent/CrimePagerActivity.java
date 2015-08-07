package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity {

	private ArrayList<Crime> mCrimes;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		mCrimes = CrimeLab.get(this).getCrimes();
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mCrimes.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				Crime crime = mCrimes.get(arg0);
				return CrimeFragment.newInstance(crime.getID());
			}
		});
		
		UUID itemid = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int idx = 0; idx < mCrimes.size(); idx++) {
			if (mCrimes.get(idx).getID().equals(itemid)) {
				mViewPager.setCurrentItem(idx);
				break;
			}
		}
	}
}
