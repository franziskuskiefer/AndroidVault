package de.franziskuskiefer.android.vault.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import de.franziskuskiefer.android.vault.view.Setup;
import de.franziskuskiefer.android.vault.view.Vault;

public class MainActivity extends Activity implements Util, OnSharedPreferenceChangeListener{

	private SharedPreferences packagePrefs;
	private DatabaseController dbController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		packagePrefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		packagePrefs.registerOnSharedPreferenceChangeListener(this);

		// start vault or setup
		if (EMULATOR_TESTING){ // or testing
//			openDB(EMULATOR_TEST_KEY);
			Intent myIntent = new Intent(getBaseContext(), Vault.class);
			myIntent.putExtra("key", EMULATOR_TEST_KEY);
			startActivity(myIntent);
		} else if (packagePrefs.contains("key")) { // FIXME: get real key first!
//			openDB(EMULATOR_TEST_KEY);
			Intent myIntent = new Intent(getBaseContext(), Vault.class);
			myIntent.putExtra("key", TEST_KEY);
			startActivity(myIntent);
		} else {
			Intent myIntent = new Intent(getBaseContext(), Setup.class);
			startActivity(myIntent);
		}
		

	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d("MainActivity", "onPause");
	}
	
	@Override
	protected void onResume() {
		super.onPause();
		
		finish();
	}

	/**
	 * SharedPreferences Listener
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
	}
	
}
