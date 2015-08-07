package com.example.remotecontrol;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class RemoteControlActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		return new RemoteControlFragment();
	}

}
