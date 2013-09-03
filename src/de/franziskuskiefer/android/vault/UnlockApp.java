package de.franziskuskiefer.android.vault;

import java.io.File;

import android.app.Activity;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class UnlockApp extends Activity {

	public void InitializeSQLCipher(Context context) {
		SQLiteDatabase.loadLibs(context);
		File databaseFile = context.getDatabasePath("keystore.db");
		databaseFile.mkdirs();
		databaseFile.delete();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
		database.execSQL("create table t1(a, b)");
		database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money", "two for the show"});
		database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"three for them", "four for me"});

		// test with real key
//		rawKeyTest(database, databaseFile);
		database.rawExecSQL(rekeyCommand);
		Cursor result = database.rawQuery("SELECT * FROM t1", new String[]{});
		while (result.moveToNext()) {
			System.out.println("result[0]: "+result.getString(0));
			System.out.println("result[1]: "+result.getString(1));
		}
		result.close();
		
		// close db
		database.close();
		
		System.out.println("finished database test :)");
	}
	
	
	String password = "x\'2DD29CA851E7B56E4697B0E1F08507293D761A05CE4D1B628663F411A8086D99\'";
	String rekeyCommand = String.format("PRAGMA rekey  = \"%s\";", password);
//    File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
//    SQLiteDatabase database;
//
//    protected void internalSetUp() {
//        ZeteticApplication.getInstance().prepareDatabaseEnvironment();
//        File databasePath = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
//        database = createDatabase(databasePath);
//        setUp();
//    }
//    
    public boolean rawKeyTest(SQLiteDatabase database, File databaseFile) {
        database.rawExecSQL(rekeyCommand);
        database.close();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
        return database != null;
    }
//
//    @Override
//    public String getName() {
//        return "Raw Rekey Test";
//    }

}
