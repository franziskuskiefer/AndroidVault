package de.franziskuskiefer.android.vault.controller;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

public abstract class BasicFragmentActivity extends FragmentActivity {

	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d("MainActivity", "(FA)onPause ...");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		Log.d("MainActivity", "(FA)onStop ...");
	}
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		
		Log.d("MainActivity", "(FA)onUserLeaveHint ...");
	}
}
