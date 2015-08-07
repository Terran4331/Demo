package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends Fragment {

	private ArrayList<Crime> mCrimes;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.list_crime, container, false);
		mListView = (ListView)view.findViewById(R.id.crimes_listview);
		
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		mListView.setAdapter(adapter);
		
		View emptyView = view.findViewById(R.id.empty_list_item);
		mListView.setEmptyView(emptyView);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Crime crime = ((CrimeAdapter) mListView.getAdapter()).getItem(position);
	    		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
	    		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());
	    		startActivity(i);
	        }
	    });
		
		Button emptyButton = (Button)view.findViewById(R.id.empty_button);
		emptyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CrimeLab.get(getActivity()).addItem(20);
				updateData();
			}
		});
		
		return view;
	}
 
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateData();
	}

	private void updateData(){
		CrimeAdapter adapter = (CrimeAdapter) mListView.getAdapter();
		adapter.notifyDataSetChanged();
	}
	
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), R.layout.list_item_crime, crimes);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Crime crime = getItem(position);
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_crime, null);
			}
			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(crime.getDate().toString());

			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(crime.getTitle());

			CheckBox solvedBox = (CheckBox) convertView
					.findViewById(R.id.crime_list_item_solvedCheckbox);
			solvedBox.setChecked(crime.isSolved());

			return convertView;

		}
	}

}
