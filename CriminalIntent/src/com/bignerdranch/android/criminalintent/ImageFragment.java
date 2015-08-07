package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {

	private ImageView mImgView;
	private static final String EXTRA_JPG_PATH = "com.bignerdranch.android.criminalintent.extra_jpg_path";
	private static final String EXTRA_JPG_ROTATION = "com.bignerdranch.android.criminalintent.extra_jpg_rotation";

	public static ImageFragment newInstance(String path, int rotation) {
		ImageFragment fragment = new ImageFragment();
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_JPG_PATH, path);
		args.putInt(EXTRA_JPG_ROTATION, rotation);
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return fragment;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mImgView = new ImageView(getActivity());
		String path = (String) getArguments().getSerializable(EXTRA_JPG_PATH);
		mImgView.setImageDrawable(PictureUtils.getScaledDrawable(getActivity(),
				path));
		mImgView.setRotation(getArguments().getInt(EXTRA_JPG_ROTATION));
		return mImgView;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImgView);
    }
}
