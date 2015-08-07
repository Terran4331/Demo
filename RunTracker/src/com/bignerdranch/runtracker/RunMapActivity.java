package com.bignerdranch.runtracker;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class RunMapActivity extends SingleFragmentActivity {

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		if (i != null) {
			return RunMapFragment.newInstance(i.getLongExtra(RunMapFragment.EXTRA_RUN_ID, -1));
		} else {
			return new RunMapFragment();
		}
	}

}
