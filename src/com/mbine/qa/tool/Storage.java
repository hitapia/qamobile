/**
 * 
 */
package com.mbine.qa.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * @author Bang
 *
 */
public class Storage {
	
	private final String PREF_NAME = "com.mbine.qa";
	static Context mContext;
	public File mediaStorageDir;
	
	public Storage(Context c) {
        mContext = c;
        mediaStorageDir = new File(Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + mContext.getPackageName()
            + "/Files"); 
        if (!mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        } 
    }

	public boolean copyFile(File file , String save_file){
        boolean result;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
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
