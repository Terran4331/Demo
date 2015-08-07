package com.bignerdranch.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

public class VideoPlayer {
	private MediaPlayer mMediaPlayer;

	public void Play(Context c, SurfaceHolder sfHolder){
		Stop();
		mMediaPlayer = MediaPlayer.create(c, R.raw.apollo_17_stroll);
		mMediaPlayer.setDisplay(sfHolder);
        
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Stop();
            }
        });
        
		mMediaPlayer.start();
	}
	
	public void Stop(){
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}
