/**
 * 
 */
package com.mbine.qa.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Bang
 *
 */
public class Storage {
	
	private final String PREF_NAME = "com.mbine.qa";
	static Context mContext;
	
	public Storage(Context c) {
        mContext = c;
    }
	public void put(String key, String val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.commit();
    }

	public String pull(String key, String val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
