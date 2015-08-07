package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

public abstract class SingleFragmentActivity extends FragmentActivity {

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
