package de.franziskuskiefer.android.vault.view;

import de.franziskuskiefer.android.vault.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Custom {@link CursorAdapter} to display password tables of the form:
 * FIXME: Note | Username
 * 
 * @author Franziskus Kiefer
 *
 */
public class PasswordTableAdapter extends CursorAdapter {

	LayoutInflater inflater = null;

	public PasswordTableAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.inflater = LayoutInflater.from(context);
		Log.d("Database", "Started PasswordTableAdapter");
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		Log.d("Database", "bindView");
		Log.d("Database", "username: "+arg2.getString(1));
		Log.d("Database", "pwd: "+arg2.getString(2));
		((TextView)arg0.findViewById(R.id.contextfield)).setText("ToDo");
		((TextView)arg0.findViewById(R.id.usernamefield)).setText(arg2.getString(1));
		((TextView)arg0.findViewById(R.id.passwordfield)).setText(arg2.getString(2));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		this.inflater = LayoutInflater.from(arg0);
		Log.d("Database", "Created newView in PasswordTableAdapter");
		return this.inflater.inflate(R.layout.pwd_table_list_item, null);
	}

}
