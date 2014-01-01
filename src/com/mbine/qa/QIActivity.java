package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.mbine.qa.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbine.qa.controls.*;
import com.mbine.qa.tool.*;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class QIActivity extends Fragment {

	Context mContext;
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_QNO = "qno";
	private static final String TAG_PINFO = "q/packInfo_";
	private static final String TAG_PMYINFO = "a/mypackinfo_";
	private static final String TAG_NEXTQ = "q/getNextQPackage_";
	private static final String TAG_JSON_PCONTENT = "content";
	private static final String TAG_JSON_PNAME = "name";

	String mUNO = null;
	String mPNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	
	TextView mSummary = null;
	TextView mProgAn = null;
	TextView mProfCo = null;
	TextView mProfReg = null;
	Button mBtnAdd = null;
	Button mBtnStart = null;
	View view = null;
	boolean get1 = false, get2 = false;
	
	public QIActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
        ViewGroup container, Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.activity_qi, container, false);
			
			GetControls();

			Bundle bundle = this.getArguments();
			mUNO = bundle.getString(TAG_UNO);
			mPNO = bundle.getString(TAG_PNO);

			mBtnStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					GetNextQ();
				}
			});

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
			SetMyView();
			return view;
	}
	
	private void GetControls(){
        mSummary = (TextView)view.findViewById(R.id.pinfo_summry);
        mBtnAdd = (Button)view.findViewById(R.id.btnAddinP);
        mBtnStart = (Button)view.findViewById(R.id.btnstartanswer);
        mProgAn = (TextView)view.findViewById(R.id.avg_answer);
        mProfCo = (TextView)view.findViewById(R.id.avg_cor);
        mProfReg = (TextView)view.findViewById(R.id.avg_reg);
	}

	private void GetNextQ(){
        Communication.post(TAG_NEXTQ+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		try {
        			String next = json.getString("next").toString();
        			if(next.equals("")){
        				tool.showPop(getActivity(), "모든 문제를 풀었습니다!", "OK");
        			}else{
                        Intent intent = new Intent(getActivity(), QAnswerActivity.class);
                        intent.putExtra(TAG_UNO, mUNO);
                        intent.putExtra(TAG_PNO, mPNO);
                        intent.putExtra(TAG_QNO, next);
                        startActivity(intent);
        			}
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        });	
	}

	private void SetMyView(){
        Communication.post(TAG_PMYINFO+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		try {
					mProgAn.setText(json.getString("pmyan").toString() + "%");
					mProfReg.setText(json.getString("pmyq").toString() + "%");
					mProfCo.setText(json.getString("pmya").toString() + "%");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		get1 = true;
        	}
        });	
	}

	private void SetView(){
        Communication.post(TAG_PINFO+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		HashMap<String, ArrayList<HashMap<String, String>>> pinfo = pack.Info(json);
        		ArrayList<HashMap<String,String>> pbasic = pinfo.get("p");

        		mSummary.setText(((HashMap<String,String>)pbasic.get(0)).get(TAG_JSON_PCONTENT));
                ((QPackageActivity)getActivity()).getActionBar().setTitle(((HashMap<String,String>)pbasic.get(0)).get(TAG_JSON_PNAME));
                get2 = true;
        	}
        });	
	}
}
