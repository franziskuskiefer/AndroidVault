package de.franziskuskiefer.android.vault.controller;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import de.franziskuskiefer.android.vault.avtivities.MainActivity;

public abstract class BasicFragmentActivity extends FragmentActivity {

	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d("MainActivity", "(FA)onPause ...");
		new AppPreferences(getApplicationContext()).saveAppStatus(false);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		Log.d("MainActivity", "(FA)onStop ...");
		new AppPreferences(getApplicationContext()).saveAppStatus(false);
	}
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		
		Log.d("MainActivity", "(FA)onUserLeaveHint ...");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d("MainActivity", "(FA)onResume ...");
		
		boolean appRunning = new AppPreferences(getApplicationContext()).getAppStatus();
		Log.d("MainActivity", "(FA)appStatus: "+appRunning);
		if (!appRunning){
			Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
			startActivity(myIntent);
		}
	}
}
