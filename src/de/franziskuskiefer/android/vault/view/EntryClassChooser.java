package de.franziskuskiefer.android.vault.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import de.franziskuskiefer.android.vault.R;

public class EntryClassChooser extends DialogFragment {

	// selected entry
	int selected = -1;

	// Use this instance of the interface to deliver action events
	NoticeDialogListener mListener;

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
		builder.setTitle(R.string.choose_category);
		builder.setItems(R.array.categories_array, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				selected = which;
				mListener.onDialogPositiveClick(EntryClassChooser.this);
			}
		});
		return builder.create();
	}

	public int getSelected() {
		return selected;
	}
	
}
