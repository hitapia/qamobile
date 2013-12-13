package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

public class QCreateActivity extends Activity {

	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_CODE_C = "cs";
	private static final String TAG_CODE_P = "p";
	private static final String TAG_C_NAME = "name";
	private static final String TAG_P_CATEGROY = "category";
	private static final String TAG_CATEGORY = "q/getCategory_";
	private static final String TAG_PINFO = "q/qpinfo_";
	private static final String TAG_CATEGORY_SEL = "선택해주세요.";

	private String mUNO = null;
	private String mPNO = null;
	private String mTitle = null;
	private String mCategory = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	
	Spinner spCategory = null;
	SeekBar sbDiff = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qcreate);
		
		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		if(intent.hasExtra(TAG_PNO))
			mPNO = intent.getStringExtra(TAG_PNO);
		
		spCategory = (Spinner)this.findViewById(R.id.cboCategory);
		sbDiff = (SeekBar)this.findViewById(R.id.sbDiff);

		if(mPNO != null)
			GetPackageInfo();

		GetCategory();
	}

	private void GetPackageInfo(){
        Communication.post(TAG_PINFO + "/" + mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		try {
					JSONObject pinfo = json.getJSONObject(TAG_CODE_P);
					mTitle = "Create In " + pinfo.getString(TAG_C_NAME);
					mCategory = pinfo.getString(TAG_P_CATEGROY);
					Intent intent = getIntent();
					getActionBar().setTitle(intent.getStringExtra(mTitle));
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        });	
	}
	
	private void GetCategory(){
        Communication.post(TAG_CATEGORY, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		try {
        			ArrayList<String> arrayList = new ArrayList<String>();
        			arrayList.add(TAG_CATEGORY_SEL);
					JSONArray clist = json.getJSONArray(TAG_CODE_C);
                    for(int i = 0; i < clist.length(); i++){
                        arrayList.add(clist.getJSONObject(i).getString(TAG_C_NAME));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(QCreateActivity.this, R.layout.uc_spinner_item, arrayList);
                    spCategory.setAdapter(adapter);
                    spCategory.setSelection(adapter.getPosition("Category 2"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        });	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qcreate, menu);
		return true;
	}

}
