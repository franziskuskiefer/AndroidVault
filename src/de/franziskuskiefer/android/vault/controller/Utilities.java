package de.franziskuskiefer.android.vault.controller;

import android.util.Log;

public class Utilities {

	public static String makeFragmentName(int viewId, int position) {
		String tag = "android:switcher:" + viewId + ":" + position;
		Log.d("Vault", "Tag: "+tag);
		return tag;
	}
	
}
