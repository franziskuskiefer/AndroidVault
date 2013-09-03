package de.franziskuskiefer.android.vault.view;

import android.app.Dialog;
import android.app.DialogFragment;

/**
 * Simple listener interface used by {@link Dialog}s to return their result.
 * 
 * @author Franziskus Kiefer
 *
 */
public interface NoticeDialogListener {
	
	public void onDialogPositiveClick(DialogFragment dialog);

	public void onDialogNegativeClick(DialogFragment dialog);
	
}
