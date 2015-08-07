package com.bignerdranch.android.criminalintent;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo implements Serializable { 
    private static final long serialVersionUID = 1L;

    private static final String JSON_FILENAME = "filename";
    private static final String JSON_ROTATION = "rotation";

    private String mFilename;
    private int mRotation;

    /** create a Photo representing an existing file on disk */
    public Photo(String filename, int rotation) {
        mFilename = filename;
        mRotation = rotation;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);     
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        json.put(JSON_ROTATION, mRotation);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }
    
    public int getRotation(){
    	return mRotation;
    }
    
}

