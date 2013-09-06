package de.franziskuskiefer.android.vault.view;

import net.sqlcipher.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.franziskuskiefer.android.vault.R;

public class TableFragment extends Fragment implements NoticeDialogListener {

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
			View frag = View.inflate(getActivity(), R.layout.pwd_list, null);
			list = (ListView) frag.findViewById(R.id.lv);

			// query db in its own thread
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					Cursor c = ((Vault)getActivity()).getDBController().getCursor("passwords");
					passwordTableAdapter = new PasswordTableAdapter(getActivity(), c, true);
					list.setAdapter(passwordTableAdapter);
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							CrossProcessCursorWrapper clickedRow = (CrossProcessCursorWrapper) list.getItemAtPosition(arg2);
							String username = clickedRow.getString(clickedRow.getColumnIndex("username"));
							String password = clickedRow.getString(clickedRow.getColumnIndex("password"));
							String note = clickedRow.getString(clickedRow.getColumnIndex("note"));
							int id = clickedRow.getInt(clickedRow.getColumnIndex("_id"));
							
							Bundle pwdDialog = new Bundle();
							pwdDialog.putString("note", note);
							pwdDialog.putString("username", username);
							pwdDialog.putString("password", password);
							pwdDialog.putInt("id", id);
							PasswordInfo passwordInfo = new PasswordInfo();
							passwordInfo.setArguments(pwdDialog);
							passwordInfo.setTargetFragment(TableFragment.this, 0);
							passwordInfo.show(getActivity().getSupportFragmentManager(), "PasswordInfo");
						}
					});
				}
			});

			Log.d("Vault", "Create Table Fragment");
			return frag;
		} else {
			View view = inflater.inflate(R.layout.dummy_table_fragment, container, false);
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
	        if (!isVisibleToUser) {
	            Log.d("TableFragment", "Not visible anymore ...");
	        }
	    }
	}
	
	public void notifyDataSetChanged(){
		Log.d("TableFragment", "notifyDataSetChanged");
		// XXX: as this.passwordTableAdapter.notifyDataSetChanged() is not working I change the cursor 
		this.passwordTableAdapter.changeCursor(((Vault)getActivity()).getDBController().getCursor("passwords"));
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		String note = ((PasswordInfo)dialog).getNote();
		String user = ((PasswordInfo)dialog).getUsername();
		String pwd = ((PasswordInfo)dialog).getPwd();
		int id = ((PasswordInfo)dialog).getEntryId();
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Stored changes! "+note , Toast.LENGTH_SHORT).show();
		((Vault)getActivity()).getDBController().changePasswordEntry(id, new String[]{note, user, pwd});
		
		// update UI
		
		// XXX: as this.passwordTableAdapter.notifyDataSetChanged() is not working I change the cursor 
		this.passwordTableAdapter.changeCursor(((Vault)getActivity()).getDBController().getCursor("passwords"));
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Cancelled changes!", Toast.LENGTH_SHORT).show();
	}
	
}
