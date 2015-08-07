package com.bignerdranch.android.photogallery;

import android.support.v4.app.Fragment;

public class PhotoPageActivity extends SingleFragmentActivity {

	@Override
	protected Fragment CreateFragment() {
		// TODO Auto-generated method stub
		return new PhotoPageFragment();
	}

}
