package com.mbine.qa.tool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.mbine.qa.R;

@SuppressWarnings("deprecation")
public class Tools {
	private ProgressDialog mDialog;
	public String upResult = "";

	public void showPop(Activity act, String comment, String ok){
        AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                }
        });
        alert.setMessage(comment);
        alert.show();
	}

	public void ShowLoading(Context context){
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
	}
	
	public void ExitLoading(){
		if(mDialog != null)
			mDialog.dismiss();
	}
	
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public String DiffTimeNow(String datetime){
		SimpleDateFormat formatter ; 
	    Date curDate ;
	    Date oldDate ; 
	    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    curDate = Calendar.getInstance().getTime();
	    String r = "";
	    try {
			oldDate = (Date)formatter.parse(datetime);
			long oldMillis=oldDate.getTime();
			long curMillis=curDate.getTime();
			//Log.d("CaseListAdapter", "Date-Milli:Now:"+curDate.toString()+":" +curMillis +" old:"+oldDate.toString()+":" +oldMillis);
			CharSequence text=DateUtils.getRelativeTimeSpanString(oldMillis, curMillis,0);
			r = text.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return r;
	}

	public void post(String url, List<NameValuePair> nameValuePairs) {
		upResult = "";
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpPost httpPost = new HttpPost(url);
	    try {
	        MultipartEntity entity = new MultipartEntity();

	        for(int index=0; index < nameValuePairs.size(); index++) {
	            if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
	                entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
	            } else {
	                entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
	            }
	        }
	        httpPost.setEntity(entity);
	        HttpResponse response = httpClient.execute(httpPost, localContext);
	        upResult =  EntityUtils.toString(response.getEntity());
	    } catch (IOException e) {
	        e.printStackTrace();
	        upResult =  "";
	    }
	}
	
	
	public Bitmap getBitmap(String _file){
		File imgFile = new  File(_file);
		if(imgFile.exists()){
		    return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return null;
	}
	
	public String Shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString().replace(" ","");
    }
	
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 160;
        int targetHeight = 160;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2
        		, (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight())
          , new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
	 }
}
