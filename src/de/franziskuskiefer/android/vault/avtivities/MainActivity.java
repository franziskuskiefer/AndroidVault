package de.franziskuskiefer.android.vault.avtivities;

import java.security.SecureRandom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import de.franziskuskiefer.android.vault.controller.AppPreferences;
import de.franziskuskiefer.android.vault.controller.Util;

public class MainActivity extends Activity implements Util, OnSharedPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check salt and generate new if necessary
		AppPreferences prefs = new AppPreferences(getApplicationContext());
		String s = prefs.getSalt();
		if (s == null || s.isEmpty() || s.equals("")){
			byte[] salt = new SecureRandom().generateSeed(4);
			s = Base64.encodeToString(salt, Base64.DEFAULT);
			prefs.setSalt(s);
		}
		
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
	}
	
}
