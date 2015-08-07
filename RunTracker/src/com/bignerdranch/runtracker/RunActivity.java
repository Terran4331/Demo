package com.bignerdranch.runtracker;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class RunActivity extends SingleFragmentActivity {

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		long runId = getIntent().getLongExtra(RunFragment.EXTRA_RUN_ID, -1);
		return RunFragment.newInstance(runId);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (intent != null) {
			long runId = intent.getLongExtra(RunFragment.EXTRA_RUN_ID, -1);
			FragmentManager fm = getSupportFragmentManager();
			RunFragment fragment = (RunFragment)fm.findFragmentById(R.id.fragmentContainer);
			if (fragment != null) {
				fragment.setRun(runId);
			}
		}
	}

}
