package de.franziskuskiefer.android.vault.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    public static final String KEY_APP_RUNNING = "vaultopen";
    public static final String KEY_SALT = "salt";
    
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
    
    private SharedPreferences prefs;
    private Editor editor;

    public AppPreferences(Context context) {
        this.prefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.editor = prefs.edit();
    }

    public boolean getAppStatus() {
        return prefs.getBoolean(KEY_APP_RUNNING, false);
    }

    public void saveAppStatus(boolean value) {
    	editor.putBoolean(KEY_APP_RUNNING, value);
        editor.commit();
    }
    
    public String getSalt(){
    	return prefs.getString(KEY_SALT, "");
    }
    
    public void setSalt(String s){
    	editor.putString(KEY_SALT, s);
        editor.commit();
    }
}