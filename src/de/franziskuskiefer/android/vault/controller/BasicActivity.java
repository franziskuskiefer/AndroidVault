package de.franziskuskiefer.android.vault.controller;

import android.app.Activity;
import android.util.Log;

public class BasicActivity extends Activity {

	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d("MainActivity", "(A)onPause ...");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		Log.d("MainActivity", "(A)onStop ...");
	}
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		
		Log.d("MainActivity", "(A)onUserLeaveHint ...");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d("MainActivity", "(A)onResume ...");
	}
}
