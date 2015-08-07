package com.example.myaidlclient;

import java.util.List;

import service.ICat;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private ICat mCat;

	public static Intent getExplicitIntent(Context context,
			Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
				0);
		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}
		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);
		// Set the component to be explicit
		explicitIntent.setComponent(component);
		return explicitIntent;
	}

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mCat = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.d("MainActivity", "onServiceConnected");
			mCat = ICat.Stub.asInterface(service);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent();
		// intent.setAction("org.crazyit.aidl.action.AIDL_SERVICE");
		// intent.setClassName("org.crazyit.service", "AidlService");
		intent.setPackage("com.example.myaidlservice");
		boolean success = getApplicationContext().bindService(intent,
				connection, BIND_AUTO_CREATE);

		Button button = (Button) findViewById(R.id.call_service_btn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if (mCat != null) {
						Toast.makeText(
								MainActivity.this,
								"Color:\t" + mCat.getColor() + "\nWeight:\t"
										+ Double.toString(mCat.getWeight()),
								Toast.LENGTH_LONG).show();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getApplicationContext().unbindService(connection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
