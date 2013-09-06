package de.franziskuskiefer.android.vault.model;

import java.io.File;
import java.util.Arrays;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

public class Database {

	private final static String DB_FILE_NAME = "vault.db";
	
	private Context context;
	private SQLiteDatabase database;
	private File databaseFile;
	
	public static void dropDatabase(Context c){
		c.getDatabasePath(DB_FILE_NAME).delete();
	}
	
	public Database(Context c, String key) {
		this.context = c;
		
		SQLiteDatabase.loadLibs(this.context);
		databaseFile = context.getDatabasePath(DB_FILE_NAME);
		if (databaseFile.exists())
			initDB(key);
		else
			createDB(key);
	}
	
	private void initDB(String key) {
		this.database = SQLiteDatabase.openOrCreateDatabase(databaseFile, key, null);
		Log.d("Database", "opened database");
	}

	private void createDB(String key) {
		databaseFile.mkdirs();
		databaseFile.delete();
		this.database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "password", null);
		String rekeyCommand = String.format("PRAGMA rekey  = \"%s\";", key);
		this.database.rawExecSQL(rekeyCommand);
		Log.d("Database", "created new database");
	}
	
	public void createTable(String... args) {
		Log.d("Database", "Create table if not existent");
		String vals = "(_id INTEGER PRIMARY KEY, ";
		int i = 1;
		for (; i < args.length-1; i++) {
			vals += args[i] + ", ";
		}
		vals += args[i] + ")";
		database.execSQL("create table if not exists "+args[0]+vals);
	}
	
	public void insert(String tableName, String[] scheme, String[] args) {
		if (scheme.length == args.length){
			String schema = " (", qmarks = " (";
			int i = 0;
			for (; i < args.length-1; i++) {
				schema += scheme[i] + ", ";
				qmarks += "?, ";
			}
			schema += scheme[i] + ") ";
			qmarks += "?) ";
			Log.d("Database", "insert into "+ tableName + schema +" values " + qmarks+"\nARGS: "+Arrays.toString(args));
			database.execSQL("insert into "+ tableName + schema +" values " + qmarks, args);
		}
	}
	
	public void printTable(String name){
		printScheme(name);
		printContent(name);
	}

	private void printContent(String name) {
		Cursor result = database.rawQuery("SELECT * FROM "+name, new String[]{});
		Log.d("Database", "result from table: "+name);
		int nCol = result.getColumnCount();
		while (result.moveToNext()) {
			for (int i = 0; i < nCol; i++) {
				Log.d("Database", "result["+i+"]: "+result.getString(i));
			}
		}
		result.close();
	}

	public void printScheme(String name){
		Cursor scheme = database.rawQuery("PRAGMA table_info("+name+")", null);
		Log.d("Database", "scheme: ");
		while(scheme.moveToNext()){
			Log.d("Database", scheme.getString(1));
		}
		scheme.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		// close database
		this.database.close();
	}

	public Table getTable(String name){
		Log.d("Database", "Getting entries from table '"+name+"'");
		Cursor entries = database.rawQuery("SELECT * FROM "+name, new String[]{});
		
		Table result = null;
		
		if (name.equalsIgnoreCase("passwords")){
			result = new PasswordTable(entries);
		}
		
		return result;
	}
	
	public Cursor getCursor(String name){
		Log.d("Database", "Getting cursor for table '"+name+"'");
		return database.rawQuery("SELECT * FROM "+name, new String[]{});
	}

	public void update(String table, int id, String[] scheme, String[] args) {
		Log.d("Database", "Updating table entry");
		if (scheme.length == args.length && scheme.length > 0){
			String uString = scheme[0] + "='" + args[0];
			for (int i = 1; i < args.length; i++) {
				uString += "', " + scheme[i] + "='" + args[i] ;
			}
			uString += "' ";
			database.execSQL("update "+table+" set " + uString + " where _id=" + id);
		}
	}
}
