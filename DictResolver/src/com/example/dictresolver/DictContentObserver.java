package com.example.dictresolver;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.widget.Toast;

public class DictContentObserver extends ContentObserver {

	private Context mContext;

	public DictContentObserver(Context contex, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		mContext = contex;
	}

	public DictContentObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		Toast.makeText(mContext, "Dict changed", Toast.LENGTH_LONG).show();
	}

}
