package com.mbine.qa.qactivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbine.qa.QCreateActivity;
import com.mbine.qa.R;
import com.mbine.qa.R.id;
import com.mbine.qa.R.layout;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class FragQCommentActivity extends Fragment {

	Context mContext;
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_PINFO = "q/packInfo_";
	private static final String TAG_JSON_PCONTENT = "content";

	String mUNO = null;
	String mPNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	
	TextView mSummary = null;
	Button mBtnAdd = null;
	
	public FragQCommentActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
        ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_qi, container, false);

			Bundle bundle = this.getArguments();
			mUNO = bundle.getString(TAG_UNO);
			mPNO = bundle.getString(TAG_PNO);
			
			mSummary = (TextView)view.findViewById(R.id.pinfo_summry);
			mBtnAdd = (Button)view.findViewById(R.id.btnAddinP);

			mBtnAdd.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), QCreateActivity.class);
					intent.putExtra(TAG_UNO, mUNO);
					intent.putExtra(TAG_PNO, mPNO);
					startActivity(intent);
				}
			});

			SetView();
			return view;
	}

	private void SetView(){
        Communication.post(TAG_PINFO+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		HashMap<String, ArrayList<HashMap<String, String>>> pinfo = pack.Info(json);
        		ArrayList<HashMap<String,String>> pbasic = pinfo.get("p");

        		mSummary.setText(((HashMap<String,String>)pbasic.get(0)).get(TAG_JSON_PCONTENT));
        	}
        });	
	}
}
