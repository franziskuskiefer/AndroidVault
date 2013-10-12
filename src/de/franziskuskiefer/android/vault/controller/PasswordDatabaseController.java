package de.franziskuskiefer.android.vault.controller;

import net.sqlcipher.Cursor;
import de.franziskuskiefer.android.vault.model.Database;

/**
 * 
 * Controller for the password table
 * 
 * @author Franziskus Kiefer
 *
 */
class PasswordDatabaseController extends DatabaseTableController implements Util {
	
	public static final String PASSWORD_DATABASE = "passwords";
	
	private static final String NOTE = "note";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	
	public void createTable(Database db) {
		db.createTable(PASSWORD_DATABASE, NOTE, USERNAME, PASSWORD);
		db.printTable(PASSWORD_DATABASE);
	}

	public String getName() {
		return PASSWORD_DATABASE;
	}

	public Cursor getCursor(Database db) {
		return db.getCursor(PASSWORD_DATABASE);
	}

	public void changeEntry(Database db, int id, String[] args) {
		db.update(PASSWORD_DATABASE, id, new String[]{NOTE, USERNAME, PASSWORD}, args);
	}
	
	public void addEntry(Database db, String[] args){
		db.insert(PASSWORD_DATABASE, new String[]{NOTE, USERNAME, PASSWORD}, args);
	}
	
}