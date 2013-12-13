package com.mbine.qa;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class QCActivity extends Fragment {

	Context mContext;
	
	public QCActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
        ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_qc, null);
			return view;
	}
}
