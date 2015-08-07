package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "Date";
	private static final String JSON_PHOTO = "Photo";
	private static final String JSON_PHOTO_ROTATION = "Photo_rotation";
	private static final String JSON_SUSPECT = "Suspect";

	private UUID mID;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	private String mSuspect;

	public Crime() {
		mID = UUID.randomUUID();
		mDate = new Date();
	}

	public Crime(JSONObject json) throws JSONException {
		mID = UUID.fromString(json.getString(JSON_ID));
		mTitle = json.getString(JSON_TITLE);
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		if (json.has(JSON_PHOTO)) {
			if (json.has(JSON_PHOTO_ROTATION)) {
				mPhoto = new Photo(json.getString(JSON_PHOTO), json.getInt(JSON_PHOTO_ROTATION));
			} else {
				mPhoto = new Photo(json.getString(JSON_PHOTO), 0);
			}
		}
		if (json.has(JSON_SUSPECT)) {
			mSuspect = json.getString(JSON_SUSPECT);
		}
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public Photo getPhoto() {
		return mPhoto;
	}

	public void setPhoto(Photo photo) {
		mPhoto = photo;
	}

	public UUID getID() {
		return mID;
	}

	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_ID, mID.toString());
		jsonObject.put(JSON_TITLE, mTitle);
		jsonObject.put(JSON_DATE, mDate.getTime());
		jsonObject.put(JSON_SOLVED, mSolved);
		if (mPhoto != null) {
			jsonObject.put(JSON_PHOTO, mPhoto.getFilename());
			if (mPhoto.getRotation() != 0) {
				jsonObject.put(JSON_PHOTO_ROTATION, mPhoto.getRotation());
			}
		}
		if (mSuspect != null) {
			jsonObject.put(JSON_SUSPECT, mSuspect);
		}
		return jsonObject;
	}

}
