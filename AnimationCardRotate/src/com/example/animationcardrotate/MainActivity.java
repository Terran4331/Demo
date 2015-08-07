package com.example.animationcardrotate;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) 
public class MainActivity extends FragmentActivity{

	private boolean mShowingBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getFragmentManager().beginTransaction()
				.add(R.id.container, new FrontFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void clipCard(){
		if (mShowingBack) {
			getFragmentManager().popBackStack();
			mShowingBack = false;
			return;
		}
		mShowingBack = true;
		
		//right_in表示反面进入时的动画，从180度转到0度
		//right_out表示正面移出，从0度转到-180度
		//left_in表示正面进入，从-180度转到0度
		//left_out表示反面移出，从0度转到180度
		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.animator.right_in,
						R.animator.right_out, R.animator.left_in,
						R.animator.left_out)
				/*.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)*/
				.replace(R.id.container, new BackFragment())
				.addToBackStack(null).commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class FrontFragment extends Fragment {
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.fragment_front, container,
					false);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clipCard();
				}
			});
			
			return view;
		}

	}

	public class BackFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.fragment_back, container,
					false);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clipCard();
				}
			});
			
			return view;
		}

	}
}
