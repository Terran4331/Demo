package com.example.remotecontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RemoteControlFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TableLayout table = (TableLayout)inflater.inflate(R.layout.activity_remote_control, container, false);
		for (int i = 2; i < table.getChildCount(); i++) {
			TableRow row = (TableRow)table.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				Button btn = (Button)row.getChildAt(j);
				if (i != 5) {
					btn.setText("" + (1 + 3*(i - 2) + j));
				}
				else{
					if (j == 0) {
						btn.setText("delete");
					}
					else if (j == 1) {
						btn.setText("" + 0);
					}
					else if (j == 2) {
						btn.setText("enter");
					}
				}
			}
		}
		return table;
	}

}
