package com.mbine.qa.tool;

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
		mDialog.dismiss();
	}
}
