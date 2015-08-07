package com.bignerdranch.runtracker;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class RunListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		return new RunListFragment();
	}

}
