package de.franziskuskiefer.android.vault.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import net.sqlcipher.Cursor;
import android.content.Context;
import android.util.Log;
import de.franziskuskiefer.android.vault.model.Database;

public class DatabaseController {
	
	private static HashMap<String, DatabaseTableController> controllers = new HashMap<String, DatabaseTableController>();
	
	public static void registerController(String name, DatabaseTableController controller){
		controllers.put(name, controller);
	}
	
	private Database db = null;
	
	public DatabaseController(Context ctx, String key) {
		init(ctx, key);
	}
	
	public void init(Context ctx, String key) {
		this.db = new Database(ctx, key); 
		
		initTables();
		
		Log.d("Vault", "initialized db ...");
	}
	
	private void initTables() {
		DatabaseTableController.registerTables();
		
		Iterator<Entry<String, DatabaseTableController>> iterator = controllers.entrySet().iterator();
		while (iterator.hasNext()) {
			((DatabaseTableController)iterator.next().getValue()).createTable(this.db);
		}
	}

	public void addPasswordEntry(String[] args){
		controllers.get(PasswordDatabaseController.PASSWORD_DATABASE).addEntry(this.db, args);
	}
	
	public Vector<String> getTableNames() {
		Vector<String> result = new Vector<String>();
		Iterator<Entry<String, DatabaseTableController>> iterator = controllers.entrySet().iterator();
		while (iterator.hasNext()) {
			result.add(((DatabaseTableController)iterator.next().getValue()).getName());
		}
		return result;
	}

	public Cursor getCursor(String string) {
		if(string.equalsIgnoreCase(PasswordDatabaseController.PASSWORD_DATABASE)){
			return controllers.get(PasswordDatabaseController.PASSWORD_DATABASE).getCursor(this.db);
		} else {
			return null;
		}
	}
	
	public static void resetDB(Context c){
		Database.dropDatabase(c);
	}

	public void changePasswordEntry(int id, String[] args) {
		controllers.get(PasswordDatabaseController.PASSWORD_DATABASE).changeEntry(this.db, id, args);
	}
	
}
