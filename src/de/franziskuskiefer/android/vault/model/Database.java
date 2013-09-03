package de.franziskuskiefer.android.vault.model;

import java.io.File;

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
		// FIXME: only for testing -> drop tables every start
//		database.execSQL("DROP TABLE passwords");
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
		Log.d("Database", "Create table if not exists");
		String vals = "(_id INTEGER PRIMARY KEY, ";
		int i = 1;
		for (; i < args.length-1; i++) {
			vals += args[i] + ", ";
		}
		vals += args[i] + ")";
		database.execSQL("create table if not exists "+args[0]+vals);
	}
	
//	public void insert(String... args) {
//		String vals = "(";
//		int i = 1;
//		for (; i < args.length-1; i++) {
//			vals += args[i] + ", ";
//		}
//		vals += args[i] + ")";
//		database.execSQL("insert into "+args[0] + " values " +vals);
//	}
	
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
			database.execSQL("insert into "+ tableName + schema +" values " + qmarks, args);
		}
	}
	
	public void printTable(String name){
		Cursor result = database.rawQuery("SELECT * FROM "+name, new String[]{});
		Log.d("Database", "result from table: "+name);
		Cursor scheme = database.rawQuery("PRAGMA table_info("+name+")", null);
		Log.d("Database", "scheme: ");
		while(scheme.moveToNext()){
			Log.d("Database", scheme.getString(1));
		}
		int nCol = result.getColumnCount();
		while (result.moveToNext()) {
			for (int i = 0; i < nCol; i++) {
				Log.d("Database", "result["+i+"]: "+result.getString(i));
			}
		}
		result.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		// close database
		this.database.close();
	}

//	public Vector<String> getEntries(String table) {
//		Log.d("Database", "Getting entries from table '"+table+"'");
//		Cursor entries = database.rawQuery("SELECT * FROM "+table, new String[]{});
//		Log.d("Database", "Count: "+entries.getCount());
//		Vector<String> result = new Vector<String>();
//		while (entries.moveToNext()) {
//			Log.d("Database", "Entry" + entries.getString(0) + " - "+entries.getString(1));
//			result.add(entries.getString(0));
//			result.add(entries.getString(1));
//		}
//		entries.close();
//		
//		return result;
//	}
	
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
}
