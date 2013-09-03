package de.franziskuskiefer.android.vault.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import de.franziskuskiefer.android.vault.R;

public class NewPasswordEntry extends DialogFragment {

	private String pwd, username;
	
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

	private EditText pwdField;

	private EditText usernameField;
    
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
	    View view = inflater.inflate(R.layout.dialog_new_entry, null);
	    pwdField = (EditText)view.findViewById(R.id.password);
	    usernameField = (EditText)view.findViewById(R.id.username);
		builder.setView(view)
	    // Add action buttons
	           .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   pwd = pwdField.getText().toString();
	            	   username = usernameField.getText().toString();
	            	   mListener.onDialogPositiveClick(NewPasswordEntry.this);
	               }
	           })
	           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPwd() {
		return pwd;
	}
	
}
