package de.franziskuskiefer.android.vault.controller;

import java.util.Vector;

import net.sqlcipher.Cursor;

import android.content.Context;
import android.util.Log;
import de.franziskuskiefer.android.vault.model.Database;
import de.franziskuskiefer.android.vault.model.PasswordTable;
import de.franziskuskiefer.android.vault.model.Table;

public class DatabaseController {
	
	private Database db = null;
	private PasswordDatabaseController pwdTable = null;
	private Vector<String> tables = new Vector<String>();
	
	public DatabaseController() {
		// nothing here
	}
	
	public DatabaseController(Context ctx, String key) {
		init(ctx, key);
	}
	
	public void init(Context ctx, String key) {
		this.db = new Database(ctx, key); 
		
		// Pwd Table
		initPwdTable();
		
		Log.d("Vault", "initialized db ...");
	}
	
	private void initPwdTable() {
		pwdTable = new PasswordDatabaseController();
		pwdTable.createTable(this.db);
		tables.add(pwdTable.getName());
	}

	public void addPasswordEntry(String user, String pwd){
		pwdTable.addPasswordEntry(this.db, user, pwd);
	}
	
	public Table getEntries(String table){
		if(table.equals("passwords")){
			 return pwdTable.getEntries(this.db);
		}
		
		return null;
	}
	
	/**
	 * 
	 * Controller for the password table
	 * 
	 * @author franziskus
	 *
	 */
	private class PasswordDatabaseController implements Util {
		
		private static final String USERNAME = "username";
		private static final String PASSWORD = "password";
		private static final String PASSWORD_DATABASE = "passwords";
		
		public void createTable(Database db) {
			db.createTable(PASSWORD_DATABASE, USERNAME, PASSWORD);
			db.printTable(PASSWORD_DATABASE);
		}

		public PasswordTable getEntries(Database db) {
			return (PasswordTable) db.getTable(PASSWORD_DATABASE);
		}

		public String getName() {
			return PASSWORD_DATABASE;
		}

		public void addPasswordEntry(Database db, String user, String pwd){
			db.insert(PASSWORD_DATABASE, new String[]{USERNAME, PASSWORD}, new String[]{user, pwd});
		}

		public Cursor getCursor() {
			return db.getCursor(PASSWORD_DATABASE);
		}
		
	}

	public Vector<String> getTableNames() {
		return tables;
	}

	public Cursor getCursor(String string) {
		if(string.equalsIgnoreCase("passwords")){
			return pwdTable.getCursor();
		} else {
			return null;
		}
	}
	
	public static void resetDB(Context c){
		Database.dropDatabase(c);
	}
	
}
