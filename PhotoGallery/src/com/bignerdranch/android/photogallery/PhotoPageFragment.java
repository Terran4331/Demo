package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PhotoPageFragment extends Fragment {

	private ProgressBar mProgressBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_photo_url, container,
				false);
		mProgressBar = (ProgressBar)view.findViewById(R.id.photo_url_progress_bar);
		mProgressBar.setMax(100);
		
		WebView webView = (WebView) view.findViewById(R.id.photo_url_web_view);
		Uri uri = getActivity().getIntent().getData();
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return false;
			}

		});
		
		webView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.INVISIBLE);
				} else {
					mProgressBar.setProgress(newProgress);
					mProgressBar.setVisibility(View.VISIBLE);
				}
			}
			
		});
		
		webView.loadUrl(uri.toString());
		
		return view;
	}

}
