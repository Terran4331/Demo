package com.bignerdranch.android.criminalintent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {

	public static final String EXTRA_PHOTO = "com.bignerdranch.android.criminalintent.extra_photo";

	@SuppressWarnings("deprecation")
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	@SuppressWarnings("deprecation")
	private Camera.PictureCallback mJpgCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			// get jpeg name
			String fileName = UUID.randomUUID().toString() + ".jpg";

			boolean bSuc = false;
			// save jpeg
			try {
				FileOutputStream oStream = getActivity().openFileOutput(
						fileName, Context.MODE_PRIVATE);
				oStream.write(data);

				// notify crimefragment
				Intent intent = new Intent();
				intent.putExtra(EXTRA_PHOTO, fileName);
				getActivity().setResult(Activity.RESULT_OK, intent);
				bSuc = true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (!bSuc) {
					getActivity().setResult(Activity.RESULT_CANCELED);
				}
				getActivity().finish();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		} else {
			mCamera = Camera.open();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_crime_camera, container,
				false);

		mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_surface_view);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if (mCamera != null) {
					mCamera.stopPreview();
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if (mCamera != null) {
					try {
						mCamera.setPreviewDisplay(holder);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mCamera.release();
						mCamera = null;
					}
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
				if (mCamera != null) {
					try {
						Parameters params = mCamera.getParameters();
						Size szPreview = getBestSupportedSize(
								params.getSupportedPreviewSizes(), width,
								height);
						params.setPreviewSize(szPreview.width, szPreview.height);
						Size szCreate = getBestSupportedSize(
								params.getSupportedPictureSizes(), width,
								height);
						mCamera.setParameters(params);
						mCamera.startPreview();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						mCamera.release();
						mCamera = null;
					}
				}
			}

			private Size getBestSupportedSize(List<Size> sizes, int width,
					int height) {
				Size bestSize = sizes.get(0);
				int largest = bestSize.width * bestSize.height;
				for (Size size : sizes) {
					int area = size.width * size.height;
					if (area > largest) {
						largest = area;
						bestSize = size;
					}
				}
				return bestSize;
			}
		});

		Button takeBtn = (Button) view.findViewById(R.id.take_button);
		takeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.takePicture(null, null, mJpgCallback);
			}
		});

		return view;
	}
/*
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}*/

}
