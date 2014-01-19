/**
 * 
 */
package com.mbine.qa.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.ContextWrapper;

public class FileManager {
	private Context mContext;
	
	public FileManager(Context context){
		mContext = context;
	}
	

	public void DownloadFromUrl(String DownloadUrl, String fileName) {
		try {
		   ContextWrapper cw = new ContextWrapper(mContext);
		   File root = cw.getDir("media", Context.MODE_PRIVATE);

           File dir = new File (root.getAbsolutePath() + "/photo");
           if(dir.exists()==false) {
                dir.mkdirs();
           }

           URL url = new URL(DownloadUrl); //you can write here any link
           File file = new File(dir, fileName);

           URLConnection ucon = url.openConnection();
           InputStream is = ucon.getInputStream();
           BufferedInputStream bis = new BufferedInputStream(is);

           ByteArrayBuffer baf = new ByteArrayBuffer(5000);
           int current = 0;
           while ((current = bis.read()) != -1) {
              baf.append((byte) current);
           }

           FileOutputStream fos = new FileOutputStream(file);
           fos.write(baf.toByteArray());
           fos.flush();
           fos.close();

		} catch (IOException e) { }
	}
}
