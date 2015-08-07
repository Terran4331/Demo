package com.bignerdranch.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {
	private MediaPlayer mMediaPlayer;

	public void Play(Context c){
		Stop();
		mMediaPlayer = MediaPlayer.create(c, R.raw.one_small_step);

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
