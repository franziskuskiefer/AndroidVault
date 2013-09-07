package de.franziskuskiefer.android.vault.avtivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import de.franziskuskiefer.android.vault.controller.BasicActivity;
import de.franziskuskiefer.android.vault.controller.Util;

public class MainActivity extends BasicActivity implements Util, OnSharedPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// start vault or setup
		if (EMULATOR_TESTING){ // or testing
			Intent myIntent = new Intent(getBaseContext(), Vault.class);
			myIntent.putExtra("key", EMULATOR_TEST_KEY);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
