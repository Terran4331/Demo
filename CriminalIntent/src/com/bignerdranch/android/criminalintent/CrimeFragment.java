package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import android.R.bool;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class CrimeFragment extends Fragment {

	public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
	public static final String EXTRA_DATE_ID = "com.bignerdranch.android.criminalintent.date_id";
	public static final String EXTRA_TIME_ID = "com.bignerdranch.android.criminalintent.time_id";
	public static final String EXTRA_DATE_TIME_ID = "com.bignerdranch.android.criminalintent.date_time_id";
	public static final String EXTRA_REPORT_SUBJECT = "com.bignerdranch.android.criminalintent.report_subject";

	public static int EXTRA_REQUEST_DATE = 0;
	public static int EXTRA_REQUEST_TIME = 1;
	public static int EXTRA_REQUEST_DATE_TIME = 2;
	public static int REQUEST_PHOTO = 3;
	public static int REQUEST_SUSPECT = 4;
	public static int REQUEST_CALL_CONTACT = 5;

	private static final String DIALOG_IMAGE = "image";

	private Crime mCrime;
	private Button mBtnDate;
	private ImageView mImgView;
	private Button mSuspectBtn;
	
	private CallBack mCallBack = null;

	public interface CallBack {
		void onCrimeDetailChanged(Crime crime);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mCallBack = (CallBack)activity;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mCallBack = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle argsBundle = getArguments();
		UUID idUuid = (UUID) argsBundle.getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(idUuid);
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_crime, container, false);

		mImgView = (ImageView) v.findViewById(R.id.crime_image_view);
		mImgView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCrime.getPhoto() != null) {
					FragmentManager fm = getActivity()
							.getSupportFragmentManager();
					String path = getActivity().getFileStreamPath(
							mCrime.getPhoto().getFilename()).getAbsolutePath();
					ImageFragment.newInstance(path,
							mCrime.getPhoto().getRotation()).show(fm,
							DIALOG_IMAGE);
				}
			}
		});

		if (mCrime.getPhoto() != null) {
			registerForContextMenu(mImgView);
		} else {
			unregisterForContextMenu(mImgView);
		}

		ImageButton cameraButton = (ImageButton) v
				.findViewById(R.id.camera_button);
		PackageManager pmManager = getActivity().getPackageManager();
		boolean bHasCamera = pmManager
				.hasSystemFeature(PackageManager.FEATURE_CAMERA)
				|| pmManager
						.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
				|| Camera.getNumberOfCameras() > 0;
		if (bHasCamera) {
			cameraButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),
							CrimeCameraActivity.class);
					startActivityForResult(intent, REQUEST_PHOTO);
				}
			});
		} else {
			cameraButton.setEnabled(false);
		}

		EditText title = (EditText) v.findViewById(R.id.crime_title);
		title.setText(mCrime.getTitle());
		title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mCrime.setTitle(s.toString());
				if (mCallBack != null) {
					mCallBack.onCrimeDetailChanged(mCrime);
				}
			}
		});

		mBtnDate = (Button) v.findViewById(R.id.crime_date);
		updateDate();
		mBtnDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateTimeFragment dtFragment = new DateTimeFragment();
				FragmentManager fmFragmentManager = getActivity()
						.getSupportFragmentManager();
				dtFragment.setTargetFragment(CrimeFragment.this,
						EXTRA_REQUEST_DATE_TIME);
				dtFragment.show(fmFragmentManager, EXTRA_DATE_TIME_ID);
			}
		});

		CheckBox checkBox = (CheckBox) v.findViewById(R.id.crime_solved);
		checkBox.setChecked(mCrime.isSolved());
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mCrime.setSolved(isChecked);
				if (mCallBack != null) {
					mCallBack.onCrimeDetailChanged(mCrime);
				}
			}
		});

		// choise suspect
		mSuspectBtn = (Button) v.findViewById(R.id.crime_suspect_button);
		mSuspectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, REQUEST_SUSPECT);
			}
		});

		if (mCrime.getSuspect() != null) {
			mSuspectBtn.setText(mCrime.getSuspect());
		}

		// send report
		Button sendReportButton = (Button) v
				.findViewById(R.id.crime_send_report_button);
		sendReportButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(EXTRA_REPORT_SUBJECT,
						getString(R.string.crime_report_subject));
				String date = (String) DateFormat.format("EEE,  MMM DD",
						mCrime.getDate());
				String suspect = mCrime.getSuspect();
				String textString = getString(
						R.string.crime_report_content,
						mCrime.getTitle(),
						date,
						(suspect == null) ? getString(R.string.crime_report_no_suspect)
								: suspect,
						mCrime.isSolved() ? getString(R.string.crime_report_solved)
								: getString(R.string.crime_report_not_solved));
				intent.putExtra(Intent.EXTRA_TEXT, textString);
				intent = Intent.createChooser(intent,
						getString(R.string.crime_report_subject));

				if (getActivity().getPackageManager()
						.queryIntentActivities(intent, 0).size() > 0) {
					startActivity(intent);
				}
			}
		});

		// call contact
		Button callButton = (Button) v
				.findViewById(R.id.crime_call_contact_button);
		callButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, REQUEST_CALL_CONTACT);
			}
		});

		return v;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}

	public static CrimeFragment newInstance(UUID crimdID) {
		CrimeFragment fragment = new CrimeFragment();
		Bundle args = new Bundle();
		args.putSerializable(CrimeFragment.EXTRA_CRIME_ID, crimdID);
		fragment.setArguments(args);
		return fragment;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void deleteCrimePhoto() {
		Photo oldPhoto = mCrime.getPhoto();
		if (oldPhoto != null) {
			String oldName = oldPhoto.getFilename();
			if (oldName != null && !oldName.isEmpty()) {
				getActivity().deleteFile(oldName);// delete old photo
			}
			mCrime.setPhoto(null);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == EXTRA_REQUEST_DATE) {
			if (data == null) {
				return;
			}
			Date date = (Date) data
					.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
			if (mCallBack != null) {
				mCallBack.onCrimeDetailChanged(mCrime);
			}
		} else if (requestCode == EXTRA_REQUEST_TIME) {
			if (data == null) {
				return;
			}
			Date date = (Date) data
					.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateDate();
			if (mCallBack != null) {
				mCallBack.onCrimeDetailChanged(mCrime);
			}
		} else if (requestCode == EXTRA_REQUEST_DATE_TIME) {
			if (data == null) {
				return;
			}
			boolean bModifyDate = data.getBooleanExtra(
					DateTimeFragment.EXTRA_MODIFY_DATE_TIME, true);
			if (bModifyDate) {
				// modify date
				DatePickerFragment dateFragment = DatePickerFragment
						.newInstance(mCrime.getDate());
				dateFragment.setTargetFragment(CrimeFragment.this,
						EXTRA_REQUEST_DATE);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dateFragment.show(fm, EXTRA_DATE_ID);
			} else {
				// modify time
				TimePickerFragment timeFragment = TimePickerFragment
						.newInstance(mCrime.getDate());
				timeFragment.setTargetFragment(CrimeFragment.this,
						EXTRA_REQUEST_TIME);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				timeFragment.show(fm, EXTRA_TIME_ID);
			}
		} else if (requestCode == REQUEST_PHOTO) {
			// photo
			if (data == null) {
				return;
			}
			String fileName = data
					.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO);
			if (fileName != null && !fileName.isEmpty()) {
				deleteCrimePhoto();
				int photoRotation = 0;
				switch (getActivity().getWindowManager().getDefaultDisplay()
						.getRotation()) {
				case Surface.ROTATION_0:
					photoRotation = 90;
					break;
				case Surface.ROTATION_90:
					photoRotation = 0;
					break;
				case Surface.ROTATION_180:
					photoRotation = 270;
					break;
				case Surface.ROTATION_270:
					photoRotation = 180;
					break;

				default:
					break;
				}
				Photo photo = new Photo(fileName, photoRotation);
				mCrime.setPhoto(photo);
				mImgView.setImageDrawable(PictureUtils.getScaledDrawable(
						getActivity(), getActivity()
								.getFileStreamPath(fileName).getAbsolutePath()));
				mImgView.setRotation(photoRotation);
				registerForContextMenu(mImgView);
			}
		} else if (requestCode == REQUEST_SUSPECT) {
			// choise suspect
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			String[] queryField = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
			Cursor csCursor = getActivity().getContentResolver().query(uri,
					queryField, null, null, null);
			if (csCursor.getCount() > 0) {
				csCursor.moveToFirst();
				String displayName = csCursor.getString(0);
				mCrime.setSuspect(displayName);
				csCursor.close();
				mSuspectBtn.setText(displayName);
			}

		} else if (requestCode == REQUEST_CALL_CONTACT) {
			// call suspect
			if (data == null) {
				return;
			}

			Uri contactUri = data.getData();
			// specify the query field to return
			String[] queryField = { ContactsContract.Contacts._ID };
			Cursor cursor = getActivity().getContentResolver().query(
					contactUri, queryField, null, null, null);
			String contactPhoneNumber = null;
			// the first number is the id
			if (cursor.getCount() > 0) {
				// get the PhoneNumber for the suspect
				cursor.moveToFirst();
				int contactId = cursor.getInt(0);
				String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER };
				cursor = getActivity().getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						projection,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);

				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					contactPhoneNumber = cursor.getString(0);
				}
			}

			if (cursor != null)
				cursor.close();

			if (contactPhoneNumber != null) {
				String uriString = "tel:" + contactPhoneNumber;
				Uri numberUri = Uri.parse(uriString);
				Intent callIntent = new Intent(Intent.ACTION_DIAL, numberUri);
				startActivity(callIntent);
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (mCrime.getPhoto() != null) {
			String fileName = mCrime.getPhoto().getFilename();
			if (fileName != null && !fileName.isEmpty()) {
				mImgView.setImageDrawable(PictureUtils.getScaledDrawable(
						getActivity(), getActivity()
								.getFileStreamPath(fileName).getAbsolutePath()));
				mImgView.setRotation(mCrime.getPhoto().getRotation());
			}
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		PictureUtils.cleanImageView(mImgView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.crime_image_view) {
			getActivity().getMenuInflater().inflate(
					R.menu.crime_delete_photo_context, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.delete_photo:
			if (mCrime.getPhoto() != null) {
				deleteCrimePhoto();
				mImgView.setImageDrawable(null);
				unregisterForContextMenu(mImgView);
			}
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void updateDate() {
		mBtnDate.setText(mCrime.getDate().toString());
	}
}
