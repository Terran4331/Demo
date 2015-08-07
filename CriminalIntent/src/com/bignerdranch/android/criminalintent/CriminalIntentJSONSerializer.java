package com.bignerdranch.android.criminalintent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class CriminalIntentJSONSerializer {

	private Context mContext;
	private String mFile;

	public CriminalIntentJSONSerializer(Context context, String file) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mFile = file;
	}

	public void saveCrimes(ArrayList<Crime> crimes) throws IOException {
		Writer writer = null;
		try {
			JSONArray array = new JSONArray();
			for (Crime crime : crimes) {
				JSONObject jsonObject = crime.toJson();
				array.put(jsonObject);
			}
			OutputStream outputStream = mContext.openFileOutput(mFile,
					Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(outputStream);
			String tempString = array.toString();
			writer.write(tempString);


			//output to external sdcard
			/*if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File f = Environment.getExternalStorageDirectory();
				File fileDir = new File(f, mFile);
				FileOutputStream osFileOutputStream = new FileOutputStream(fileDir);
				osFileOutputStream.write(tempString.getBytes());
				osFileOutputStream.close();
			}*/
		} 
		catch(Exception e) {
		}
		finally {
			// TODO: handle exception
			if (writer != null) {
				writer.close();
			}
		}
	}

	public ArrayList<Crime> loadCrimes() throws IOException {
		ArrayList<Crime> crimeArrayList = new ArrayList<Crime>();
		BufferedReader bufferedReader = null;
		try {  
			InputStream inputStream = mContext.openFileInput(mFile);
			//input from external sdcard
			/*InputStream inputStream = null;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File f = Environment.getExternalStorageDirectory();
				File fileDir = new File(f, mFile);
				inputStream = new FileInputStream(fileDir);
			}*/
			
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			StringBuilder strBuilder = new StringBuilder();
			String lineString = null;
			while ((lineString = bufferedReader.readLine()) != null) {
				strBuilder.append(lineString);
			}

			JSONArray array = (JSONArray) new JSONTokener(strBuilder.toString())
					.nextValue();
			for (int i = 0; i < array.length(); i++) {
				crimeArrayList.add(new Crime(array.getJSONObject(i)));
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return crimeArrayList;
	}
}
