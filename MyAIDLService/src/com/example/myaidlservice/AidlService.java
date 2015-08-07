package com.example.myaidlservice;

import service.ICat;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AidlService extends Service {

	IBinder mBinder;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mBinder = new ICat.Stub() {
			
			@Override
			public double getWeight() throws RemoteException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getColor() throws RemoteException {
				// TODO Auto-generated method stub
				return new String("black");
			}
		};
	}

	
}
