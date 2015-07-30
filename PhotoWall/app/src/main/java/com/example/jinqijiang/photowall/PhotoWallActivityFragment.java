package com.example.jinqijiang.photowall;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoWallActivityFragment extends Fragment {

    private GridView mPhotoWall;
    private PhotoWallAdapter mWallAdapter;

    public PhotoWallActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_wall, container, false);
        mPhotoWall = (GridView) view.findViewById(R.id.photo_wall);
        if (mWallAdapter == null) {
            mWallAdapter = new PhotoWallAdapter(this.getActivity(), 0, Images.imageThumbUrls);
        }
        mPhotoWall.setAdapter(mWallAdapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWallAdapter != null)
        {
            mWallAdapter.closeDiskCache();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mWallAdapter != null)
        {
            mWallAdapter.flushDiskCache();
        }
    }

}
