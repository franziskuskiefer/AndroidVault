package de.franziskuskiefer.android.vault.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
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
public class PasswordInfo extends DialogFragment implements OnClickListener, OnLongClickListener, OnCheckedChangeListener {
	
	NoticeDialogListener mListener;
	
    private static final String HIDDEN = "**********";
    private boolean shown = false;
	private String note, username, pwd;
	private int id;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) getTargetFragment();
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
	    
	    this.note = getArguments().getString("note");
	    this.username = getArguments().getString("username");
	    this.pwd = getArguments().getString("password");
	    this.id = getArguments().getInt("id");;
	    
		((TextView)view.findViewById(R.id.note)).setText(note);
		((TextView)view.findViewById(R.id.username)).setText(username);
	    ((TextView)view.findViewById(R.id.password)).setText(HIDDEN);
	    
	   ((EditText)view.findViewById(R.id.note_edit)).setText(note);
 	   ((EditText)view.findViewById(R.id.username_edit)).setText(username);
 	   ((EditText)view.findViewById(R.id.password_edit)).setText(pwd);
	    
	    view.findViewById(R.id.note).setOnLongClickListener(this);
	    view.findViewById(R.id.username).setOnLongClickListener(this);

	    view.findViewById(R.id.password).setOnClickListener(this);
	    view.findViewById(R.id.password).setOnLongClickListener(this);
	    
	    ((CheckBox)view.findViewById(R.id.show_pwd_chkbox)).setOnCheckedChangeListener(this);

		builder.setView(view)
	    // Add action buttons
	           .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   note = ((EditText)((AlertDialog)dialog).findViewById(R.id.note_edit)).getText().toString();
	            	   username = ((EditText)((AlertDialog)dialog).findViewById(R.id.username_edit)).getText().toString();
	            	   pwd = ((EditText)((AlertDialog)dialog).findViewById(R.id.password_edit)).getText().toString();
	            	   mListener.onDialogPositiveClick(PasswordInfo.this);
	               }
	           })
	           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   getDialog().cancel();
	                   mListener.onDialogNegativeClick(PasswordInfo.this);
	               }
	           })
	           .setTitle("Password Info");   
	    return builder.create();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.password){
			View parent = (View)v.getParent().getParent();
			CheckBox checkBox = (CheckBox)parent.findViewById(R.id.show_pwd_chkbox);
			checkBox.toggle();
			shown = !shown;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == R.id.password){
			Log.d("Vault", "onLongClick --- password");
			((ViewSwitcher)v.getParent()).showNext();
			EditText pwdEdit = (EditText)((View)v.getParent()).findViewById(R.id.password_edit);
			pwdEdit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			return true;
		} else if (v.getId() == R.id.note){
			Log.d("Vault", "onLongClick --- note");
			((ViewSwitcher)v.getParent()).showNext();
			EditText noteEdit = (EditText)((View)v.getParent()).findViewById(R.id.note_edit);
			noteEdit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			return true;
		} else if (v.getId() == R.id.username){
			Log.d("Vault", "onLongClick --- username");
			((ViewSwitcher)v.getParent()).showNext();
			EditText userEdit = (EditText)((View)v.getParent()).findViewById(R.id.username_edit);
			userEdit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			return true;
		}
		return false;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		TextView pwdTextField = (TextView) ((View)buttonView.getParent().getParent()).findViewById(R.id.password);
		EditText pwdEditField = (EditText) ((View)buttonView.getParent().getParent()).findViewById(R.id.password_edit);
		if (isChecked){
			this.shown = true;
			showPwdText(pwdTextField);
			showPwdEdit(pwdEditField);
		} else {
			this.shown = false;
			hidePwdText(pwdTextField);
			hidePwdEdit(pwdEditField);
		}
	}
	
	private void showPwdEdit(View v){
		((EditText)v).setInputType(InputType.TYPE_CLASS_TEXT);
		((EditText)v).setTransformationMethod(null);
		((EditText)v).invalidate();
	}
	
	private void hidePwdEdit(View v){
		((EditText)v).setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		((EditText)v).setTransformationMethod(PasswordTransformationMethod.getInstance());
		((EditText)v).invalidate();
	}
	
	private void showPwdText(View v){
		((TextView)v).invalidate();
		((TextView)v).setText(this.pwd);
	}
	
	private void hidePwdText(View v){
		((TextView)v).invalidate();
		((TextView)v).setText(HIDDEN);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPwd() {
		return pwd;
	}
	
	public String getNote() {
		return note;
	}
	
	public int getEntryId() {
		return id;
	}

}
