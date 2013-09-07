package de.franziskuskiefer.android.vault.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import de.franziskuskiefer.android.vault.R;

/**
 * {@link DialogFragment} to choose the class of the new entry.
 * This fragment is shown when the "New Entry" menu is opened.
 * 
 * @author Franziskus Kiefer
 *
 */
public class EntryClassChooser extends DialogFragment {

	int selected = -1;
	NoticeDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
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
