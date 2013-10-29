package de.franziskuskiefer.android.vault.controller;

import android.util.Log;
import net.sqlcipher.Cursor;
import de.franziskuskiefer.android.vault.model.Database;

public abstract class DatabaseTableController {
	
	// singleton pwdInstance
	private static PasswordDatabaseController pwdInstance = new PasswordDatabaseController();
	
	// singleton pgpInstance
	private static PGPDatabaseController pgpInstance = new PGPDatabaseController();

	// register with DatabaseController
	public static void registerTables() {
		Log.d("Database", "register db pwd controller ...");
		DatabaseController.registerController(pwdInstance.getName(), pwdInstance);
		DatabaseController.registerController(pgpInstance.getName(), pgpInstance);
	}

	public abstract void createTable(Database db);

	public abstract String getName();

	public abstract Cursor getCursor(Database db);

	public abstract void changeEntry(Database db, int id, String[] args);

	public abstract void addEntry(Database db, String[] args);

}
