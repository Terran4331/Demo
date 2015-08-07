package com.bignerdranch.android.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.Uri;

public class FlickrFetchr {

	private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
	private static final String API_KEY = "0642b64ca94f2a69b4cb197dec7f026c";
	private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
	private static final String PARAM_EXTRAS = "extras";
	private static final String XML_PHOTO = "photo";

	public byte[] getUrlBytes(String spec) {
		HttpURLConnection connection = null;
		byte[] content = null;
		try {
			URL url = new URL(spec);
			connection = (HttpURLConnection) url.openConnection();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = connection.getInputStream();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				
				int read = 0;
				byte[] buffer = new byte[1024];
				while ((read = is.read(buffer)) > 0) { 
					outputStream.write(buffer, 0, read);
				}
				outputStream.close();
				content = outputStream.toByteArray();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return content;
	}

	private String getUrl(String spec) {
		byte[] content = getUrlBytes(spec);
		if (content != null) {
			return new String(content);
		} else {
			return new String();
		}
	}

	private void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) {
		int type;
		try {
			type = parser.next();
			while (type != parser.END_DOCUMENT) {
				//if (type == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
				String nameString = parser.getName();
				if (type == XmlPullParser.START_TAG && nameString.equals(XML_PHOTO)) {
					GalleryItem item = new GalleryItem();
					item.setCaption(parser.getAttributeValue(null, "title"));
					item.setId(parser.getAttributeValue(null, "id"));
					item.setUrl(parser.getAttributeValue(null, "url_s"));
					items.add(item);
				}
				type = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<GalleryItem> fetchItems() {
		String url = Uri.parse(ENDPOINT).buildUpon()
				.appendQueryParameter("method", METHOD_GET_RECENT)
				.appendQueryParameter("api_key", API_KEY)
				.appendQueryParameter("extras", "url_s").build().toString();
		String content = getUrl(url);

		ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(new StringReader(content));
			parseItems(items, parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
}
