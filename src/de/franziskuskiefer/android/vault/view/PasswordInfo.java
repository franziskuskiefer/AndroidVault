package de.franziskuskiefer.android.vault.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import de.franziskuskiefer.android.vault.R;

/**
 * {@link DialogFragment} to display a password entry.
 * This fragment is shown when clicked on an entry in the password list.
 * 
 * @author Franziskus Kiefer
 *
 */
public class PasswordInfo extends DialogFragment implements OnClickListener, OnLongClickListener {
	
    private static final String HIDDEN = "**********";
	private static final String ViewSwitcher = null;
	// Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
	private String pwd;
	private boolean shown = false;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View view = inflater.inflate(R.layout.password_info, null);
	    
	    ((TextView)view.findViewById(R.id.note)).setText(getArguments().getString("note"));
		((TextView)view.findViewById(R.id.username)).setText(getArguments().getString("username"));
	    this.pwd = getArguments().getString("password");
	    ((TextView)view.findViewById(R.id.password)).setText(HIDDEN);
	    
	    view.findViewById(R.id.password).setOnClickListener(this);
	    view.findViewById(R.id.password).setOnLongClickListener(this);

		builder.setView(view)
	    // Add action buttons
	           .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   mListener.onDialogPositiveClick(PasswordInfo.this);
	               }
	           })
	           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   getDialog().cancel();
	               }
	           })
	           .setTitle("Password Info");   
	    return builder.create();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.password){
			if (shown){
				((TextView)v).invalidate();
				((TextView)v).setText(HIDDEN);
			} else {
				((TextView)v).invalidate();
				((TextView)v).setText(this.pwd);
			}
			shown = !shown;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		Log.d("Vault", "id := "+v.getId());
		Log.d("Vault", "pwdId := "+R.id.pwd_switcher);
		if (v.getId() == R.id.password){
			((ViewSwitcher)v.getParent()).showNext();
			((EditText)((View)v.getParent()).findViewById(R.id.password_edit)).setText(((TextView)v).getText());
			return true;
		}
		return false;
	}

}
