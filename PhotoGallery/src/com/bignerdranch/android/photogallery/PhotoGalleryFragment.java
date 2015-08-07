package com.bignerdranch.android.photogallery;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoGalleryFragment extends VisibleFragment {

	private static final String THUMBNAIL_DOWNLOADER = "thumbnail_downloader";
	
	private ThumbnailDownloader mThumbnailDownloader;
	private GridView mGridView;
	private ArrayList<GalleryItem> mItems;

	private class PhotoGalleryAdapter extends ArrayAdapter<GalleryItem>{

		public PhotoGalleryAdapter(Context context, int resource,
				ArrayList<GalleryItem> items) {
			super(context, resource, items);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.photo, parent, false);
			}
			ImageView photo = (ImageView)convertView.findViewById(R.id.photo_image_view);
			if(photo != null){
				photo.setImageResource(R.drawable.brian_up_close);
				GalleryItem item = (GalleryItem)getItem(position);
				String url = item.getUrl();
				if (url != null) {
					//url is not null
					Bitmap bitmap = mThumbnailDownloader.getPhotoFromCache(url);
					if (bitmap != null) {
						//exist photo in cache
						photo.setImageBitmap(bitmap);
						mThumbnailDownloader.removeRequest(photo);
					} else {
						mThumbnailDownloader.requestPhoto(photo, url);
					}
				}
			}
			return convertView;
		}
		
	}
	
	private class FlickrFetchTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{

		@Override
		protected ArrayList<GalleryItem> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<GalleryItem> items = new FlickrFetchr().fetchItems();
			return items;
		}

		@Override
		protected void onPostExecute(ArrayList<GalleryItem> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mItems = result;
			setAdapter();
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new FlickrFetchTask().execute();
		
		Handler handler = new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == ThumbnailDownloader.MSG_PHOTO_DOWNLOADED) {
					ImageView view = (ImageView)msg.obj;
					if (view != null) {
//						Bitmap bm = new Bitmap();
//						view.setImageBitmap(bm);
					}
				}
				return false;
			}
		});
		mThumbnailDownloader = new ThumbnailDownloader(THUMBNAIL_DOWNLOADER, handler);
		mThumbnailDownloader.initPhotoCache();
		mThumbnailDownloader.start();
		mThumbnailDownloader.getLooper();

		if (!PollService.isServiceOn(getActivity())) {
			PollService.setServiceAlarm(getActivity(), true);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mThumbnailDownloader.quit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mGridView = (GridView) inflater.inflate(
				R.layout.fragment_photo_gallery, container, false);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String url = "http://www.baidu.com";
				Uri uri = Uri.parse(url);
				Intent i = new Intent(getActivity(), PhotoPageActivity.class);
				i.setData(uri);
				//Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				//startActivity(intent);
				startActivity(i);
			}
			
		});
		return mGridView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mThumbnailDownloader.stopDownload();
	}

	private void setAdapter(){
		if (getActivity() != null && mGridView != null) {
			if (mItems != null) {
				PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(getActivity(), android.R.layout.simple_gallery_item, mItems);
				mGridView.setAdapter(adapter);
				mThumbnailDownloader.predownloadPhoto(mItems);
			} else {
				mGridView.setAdapter(null);
			}
		}
	}
}
