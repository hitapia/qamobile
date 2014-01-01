package com.mbine.qa.tool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

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
}
