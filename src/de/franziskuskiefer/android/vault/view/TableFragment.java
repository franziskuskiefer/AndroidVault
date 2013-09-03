package de.franziskuskiefer.android.vault.view;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.franziskuskiefer.android.vault.R;

public class TableFragment extends Fragment {

	private ListView list;
	private PasswordTableAdapter passwordTableAdapter;

	@Override
	public void onResume() {
		super.onResume();
//		passwordTableAdapter.notifyDataSetChanged();
		Log.d("Vault", "TableFragment.onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		Log.d("Vault", "TableFragment.onPause");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		String category = getArguments().getString("category");
		if (category.equalsIgnoreCase("passwords")){
			View fuuu = View.inflate(getActivity(), R.layout.test_list, null);
			Log.d("Vault", "Create Table Fragment");
			
			list = (ListView) fuuu.findViewById(R.id.lv);
//			list = new ListView(getActivity());
//			View header = View.inflate(getActivity(), R.layout.password_list_view, null);
//			list.addHeaderView(header, null, false);

			// query db
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					Cursor c = ((Vault)getActivity()).getDBController().getCursor("passwords");
					passwordTableAdapter = new PasswordTableAdapter(getActivity(), c, true);
					list.setAdapter(passwordTableAdapter);
				}
			});

			return fuuu;
		} else {
			View view = inflater.inflate(R.layout.test_fragment, container, false);
			((TextView)view.findViewById(R.id.key_string)).setText("lülala");
			return view;
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);

	    // Make sure that we are currently visible
	    if (this.isVisible()) {
	    	Log.d("TableFragment", "Visible again ... do refresh");
	        // If we are becoming invisible, then...
	        if (!isVisibleToUser) {
	            Log.d("TableFragment", "Not visible anymore ...");
	            // TODO stop audio playback
	        }
	    }
	}
	
	public void notifyDataSetChanged(){
		Log.d("TableFragment", "notifyDataSetChanged");
		// XXX: as this.passwordTableAdapter.notifyDataSetChanged() is not working I change the cursor 
		this.passwordTableAdapter.changeCursor(((Vault)getActivity()).getDBController().getCursor("passwords"));
	}
	
}
