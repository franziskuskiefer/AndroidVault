package de.franziskuskiefer.android.vault.model;

import net.sqlcipher.Cursor;

public class PasswordTable implements Table {

	private Cursor table = null;
	private int nCol;
	
	public PasswordTable(Cursor entries) {
		table = entries;
		nCol = table.getColumnCount();
	}
	
	public String[] getNextItem(){
		String[] result = new String[nCol];
		if (table.moveToNext()) {
			for(int i = 0; i < nCol; i++){
				result[i] = table.getString(i);
			}
		} else {
			result = null;
		}
		
		return result;
	}

}
