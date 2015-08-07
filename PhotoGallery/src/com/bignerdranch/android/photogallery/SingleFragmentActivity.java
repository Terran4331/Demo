package com.bignerdranch.android.photogallery;

import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class SingleFragmentActivity extends ActionBarActivity {

	protected abstract Fragment CreateFragment();

	protected int getLayoutRsID() {
		return R.layout.activity_fragment;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(getLayoutRsID());
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if (fragment == null) {
			fragment = CreateFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment)
					.commit();
		}

	}

}
