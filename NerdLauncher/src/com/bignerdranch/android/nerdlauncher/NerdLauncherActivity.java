package com.bignerdranch.android.nerdlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;


public class NerdLauncherActivity extends SingleFragmentActivity {

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		return new AppFragment();
		//return new TaskFragment();
	}

	@Override
	protected int getLayoutRsID() {
		// TODO Auto-generated method stub
		return R.layout.activity_fragment;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nerd_launcher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_app) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
			if (fragment != null) {
				transaction.remove(fragment);
			}
			Fragment newFragment = new AppFragment();
			transaction.add(R.id.fragmentContainer, newFragment);
			transaction.commit();
			return true;
		}
		else if (id == R.id.action_task) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
			if (fragment != null) {
				transaction.remove(fragment);
			}
			Fragment newFragment = new TaskFragment();
			transaction.add(R.id.fragmentContainer, newFragment);
			transaction.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
