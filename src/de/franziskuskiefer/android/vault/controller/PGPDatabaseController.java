package de.franziskuskiefer.android.vault.controller;

import net.sqlcipher.Cursor;
import de.franziskuskiefer.android.vault.model.Database;

/**
 * 
 * Controller for the pgp key table
 * 
 * TODO: copy of pwd table atm.
 * 
 * @author Franziskus Kiefer
 *
 */
class PGPDatabaseController extends DatabaseTableController implements Util {
	
	public static final String PGP_DATABASE = "pgp";
	
	private static final String NOTE = "note";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	
	public void createTable(Database db) {
		db.createTable(PGP_DATABASE, NOTE, USERNAME, PASSWORD);
		db.printTable(PGP_DATABASE);
	}

	public String getName() {
		return PGP_DATABASE;
	}

	public Cursor getCursor(Database db) {
		return db.getCursor(PGP_DATABASE);
	}

	public void changeEntry(Database db, int id, String[] args) {
		db.update(PGP_DATABASE, id, new String[]{NOTE, USERNAME, PASSWORD}, args);
	}
	
	public void addEntry(Database db, String[] args){
		db.insert(PGP_DATABASE, new String[]{NOTE, USERNAME, PASSWORD}, args);
	}
	
}