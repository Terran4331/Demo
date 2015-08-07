package com.example.animationpagerzoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PageFragment extends Fragment {

	private static final String TAG = "PAGEFRAGMENT";
	private View view;
	private int mColor;
	
	public static PageFragment newInstance(int color){
		PageFragment fragment = new PageFragment();
		fragment.mColor = color;
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateView");
		view = inflater.inflate(R.layout.fragment_page, container, false).findViewById(R.id.fragment_page_view);
		view.setBackgroundColor(mColor);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ChildActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
			}
		});
		
		return view;
	}

}
