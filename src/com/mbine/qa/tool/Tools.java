package com.mbine.qa.tool;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Tools {
	private ProgressDialog mDialog;

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
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Calendar calold = Calendar.getInstance();
	    Calendar calnow = Calendar.getInstance();
		Date date;
		try {
			date = format.parse(datetime);
			calold.set(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes(), date.getSeconds());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	    calnow.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
	    long milliseconds1 = calnow.getTimeInMillis();
	    long milliseconds2 = calold.getTimeInMillis();
	    long diff = milliseconds2 - milliseconds1;
	    long diffSeconds = diff / 1000;
	    long diffMinutes = diff / (60 * 1000);
	    long diffHours = diff / (60 * 60 * 1000);
	    long diffDays = diff / (24 * 60 * 60 * 1000);
	    if(diffDays > 0){ return diffDays + "일전"; }
	    if(diffHours > 0){ return diffDays + "시간전"; }
	    if(diffMinutes > 0){ return diffDays + "분전"; }
	    if(diffSeconds > 0){ return diffDays + "초전"; }
	    return "방금";
	}
}
