package com.bignerdranch.android.photogallery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ThumbnailDownloader<Token> extends HandlerThread {

	public static final int MSG_PHOTO_DOWNLOADED = 0;
	public static final int MSG_PHOTO_PREDOWNLOAD = 1;

	private boolean mCacheInited = false;
	private int mPredownloadIdx = 0;
	private ArrayList<String> mPreDownloadPhotos = new ArrayList<String>();
	private Handler mResponseHandler;
	private Handler mHandler;
	private Map<Token, String> mRequestMap = Collections
			.synchronizedMap(new HashMap<Token, String>());
	LruCache<String, Bitmap> mPhotoCache;

	public ThumbnailDownloader(String name, Handler responseHandler) {
		super(name);
		mResponseHandler = responseHandler;
		// TODO Auto-generated constructor stub
	}

	public void initPhotoCache() {
		if (mCacheInited) {
			return;
		}

		int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
		mPhotoCache = new LruCache<String, Bitmap>(cacheSize) {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getByteCount() / 1024;
			}
		};

		mCacheInited = true;
	}

	public Bitmap getPhotoFromCache(String url) {
		return mPhotoCache.get(url);
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onLooperPrepared() {
		// TODO Auto-generated method stub
		super.onLooperPrepared();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == MSG_PHOTO_DOWNLOADED) {
					final Token keyToken = (Token) msg.obj;
					final String url = mRequestMap.get(keyToken);
					if (url == null) {
						return;
					}

					Bitmap photoBitmap = mPhotoCache.get(url);
					if (photoBitmap == null) {
						// download photo
						byte[] photoData = null;
						photoData = new FlickrFetchr().getUrlBytes(url);
						if (photoData == null || photoData.length <= 0) {
							return;
						}
						
						photoBitmap = BitmapFactory.decodeByteArray(
								photoData, 0, photoData.length);
						if (photoBitmap == null) {
							return;
						}
						
						mPhotoCache.put(url, photoBitmap);// add to cache
					}

					final Bitmap tempBitmap = photoBitmap;
					
					mResponseHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mRequestMap.get(keyToken) != url) {
								return;
							}
							mRequestMap.remove(keyToken);
							ImageView view = (ImageView)keyToken;
							view.setImageBitmap(tempBitmap);
						}
					});
				}
				else if (msg.what == MSG_PHOTO_PREDOWNLOAD) {
					//pre download
					do {
						final String url = (String) msg.obj;
						if (url == null) {
							break;
						}

						if (mPhotoCache.get(url) != null) {
							break;
						}

						// predownload photo
						byte[] photoData = null;
						photoData = new FlickrFetchr().getUrlBytes(url);
						if (photoData == null || photoData.length <= 0) {
							break;
						}
						
						final Bitmap photoBitmap = BitmapFactory.decodeByteArray(
								photoData, 0, photoData.length);
						if (photoBitmap == null) {
							break;
						}
						
						mPhotoCache.put(url, photoBitmap);// add to cache
					} while (false);
					
					if (mPredownloadIdx < mPreDownloadPhotos.size() - 1) {
						mPredownloadIdx++;
						mHandler.obtainMessage(MSG_PHOTO_PREDOWNLOAD, mPreDownloadPhotos.get(mPredownloadIdx)).sendToTarget();
					}
				}
			}

		};
	}

	public void requestPhoto(Token key, String url) {
		if (url != null) {
			mRequestMap.put(key, url);
			mHandler.obtainMessage(MSG_PHOTO_DOWNLOADED, key).sendToTarget();
		}
	}
	
	public void predownloadPhoto(ArrayList<GalleryItem> items){
		mHandler.removeMessages(MSG_PHOTO_PREDOWNLOAD);
		mPreDownloadPhotos.clear();
		if (items.size() > 0) {
			for (GalleryItem galleryItem : items) {
				mPreDownloadPhotos.add(galleryItem.getUrl());
			}
			mPredownloadIdx = 0;
			mHandler.obtainMessage(MSG_PHOTO_PREDOWNLOAD, mPreDownloadPhotos.get(0)).sendToTarget();
		}
	}

	public void removeRequest(Token key) {
		mRequestMap.remove(key);
	}

	public void stopDownload() {
		mHandler.removeMessages(MSG_PHOTO_DOWNLOADED);
		mRequestMap.clear();
	}
}
