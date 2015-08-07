package com.bignerdranch.android.photogallery;

public class GalleryItem {
	private String mCaption;
	private String mId;
	private String mUrl;

	public void setCaption(String caption) {
		mCaption = caption;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String toString() {
		return mCaption;
	}
}
