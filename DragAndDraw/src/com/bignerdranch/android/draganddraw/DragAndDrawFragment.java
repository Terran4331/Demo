package com.bignerdranch.android.draganddraw;

import java.util.ArrayList;

import android.R.array;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DragAndDrawFragment extends Fragment {

	public static final String BOX_ARRAY = "com.bignerdranch.android.draganddraw.box_array";
	private BoxDrawingVIew mBoxDrawingVIew;
	private ArrayList<Box> mBoxSet;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mBoxSet = (ArrayList<Box>)savedInstanceState.getSerializable(BOX_ARRAY);
		} else {
			mBoxSet = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mBoxDrawingVIew = (BoxDrawingVIew)inflater.inflate(R.layout.activity_drag_and_draw, container, false);
		mBoxDrawingVIew.setBoxSet(mBoxSet);
		return mBoxDrawingVIew;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (outState != null && mBoxDrawingVIew != null) {
			outState.putSerializable(BOX_ARRAY, mBoxDrawingVIew.getBoxSet());
		}
	}

}
