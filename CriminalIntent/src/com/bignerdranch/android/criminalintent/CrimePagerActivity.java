package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.support.v4.app.FragmentManager;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CrimePagerActivity extends FragmentActivity implements
		CrimeFragment.CallBack {
	
	private static String TAG = "CrimePagerActivity";

	private ArrayList<Crime> mCrimes;
	private ViewPager mViewPager;

	@Override
	public void onCrimeDetailChanged(Crime crime) {
		// TODO Auto-generated method stub
		
	}

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

		UUID itemid = (UUID) getIntent().getSerializableExtra(
				CrimeFragment.EXTRA_CRIME_ID);
		for (int idx = 0; idx < mCrimes.size(); idx++) {
			if (mCrimes.get(idx).getID().equals(itemid)) {
				mViewPager.setCurrentItem(idx);
				break;
			}
		}

		startActionMode(new ActionMode.Callback() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				//finish();
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				mode.getMenuInflater().inflate(R.menu.crime_list_item_context,
						menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_item_delete_crime:
					CrimeLab.get(CrimePagerActivity.this).deleteItem(
							mCrimes.get(mViewPager.getCurrentItem()));
					// mode.finish();
					finish();
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "CrimePagerActivity onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "CrimePagerActivity onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "CrimePagerActivity onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "CrimePagerActivity onStop");
		super.onStop();
	}
	
	
}
