package com.bignerdranch.android.hellomoon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloMoonFragment extends Fragment {

	private SurfaceHolder msfHolder;
	private VideoPlayer mPlayer = new VideoPlayer();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hello_moon, container,
				false);

		SurfaceView sfView = (SurfaceView)view.findViewById(R.id.hellomoon_video);
		msfHolder = sfView.getHolder();
		
		Button playButton = (Button) view
				.findViewById(R.id.hellomoon_playButton);
		playButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPlayer.Play(getActivity(), msfHolder);
			}
		});

		Button stopButton = (Button) view
				.findViewById(R.id.hellomoon_stopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPlayer.Stop();
			}
		});

		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mPlayer.Stop();
	}

}
