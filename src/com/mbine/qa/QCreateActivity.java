package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class QCreateActivity extends Activity {

	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_CODE_C = "cs";
	private static final String TAG_CODE_P = "p";
	private static final String TAG_C_NAME = "name";
	private static final String TAG_P_CATEGROY = "category";
	private static final String TAG_CATEGORY = "q/getCategory_";
	private static final String TAG_PINFO = "q/qpinfo_";
	private static final String TAG_SUBMIT = "q/register_";
	private static final String TAG_CATEGORY_SEL = "선택해주세요.";
	
	private static final String TAG_PARAM_PNO = "pno";
	private static final String TAG_PARAM_REF = "ref";
	private static final String TAG_PARAM_COMMENT = "comment";
	private static final String TAG_PARAM_HINT = "hint";
	private static final String TAG_PARAM_SUMMARY = "summary";
	private static final String TAG_PARAM_TAG = "tag";
	private static final String TAG_PARAM_DIFFICULT = "difficult";
	private static final String TAG_PARAM_CATEGORY = "category";
	private static final String TAG_PARAM_ITEMSUMMARY = "itemsummary";
	private static final String TAG_PARAM_ITEMCORRECT = "itemcorrect";

	private String mUNO = null;
	private String mPNO = null;
	private String mTitle = null;
	private String mCategory = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	
	Spinner spCategory = null;
	SeekBar sbDiff = null;
	Button btnCreate = null;
	Button btnCreateNext = null;
	
	TextView txtSummary = null;
	TextView txtItem1 = null;
	TextView txtItem2 = null;
	TextView txtItem3 = null;
	TextView txtItem4 = null;

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
		btnCreate = (Button)this.findViewById(R.id.btn_create);
		btnCreateNext = (Button)this.findViewById(R.id.btn_createnext);
		txtSummary = (Button)this.findViewById(R.id.txtSummary);
		txtItem1 = (Button)this.findViewById(R.id.qitem1);
		txtItem2 = (Button)this.findViewById(R.id.qitem2);
		txtItem3 = (Button)this.findViewById(R.id.qitem3);
		txtItem4 = (Button)this.findViewById(R.id.qitem4);

		if(mPNO != null)
			GetPackageInfo();

		SetEvent();
		GetCategory();
	}
	
	private void SetEvent(){
		btnCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Validate()){
					tool.showPop(QCreateActivity.this, "Check Input Fields.", "OK");
				}else{
					tool.ShowLoading(QCreateActivity.this);
					Submit();
				}
			}
		});
	}
	
	private void Submit(){
		RequestParams params = pack.GetParams();
		params.put(TAG_PARAM_PNO, mPNO);
		params.put(TAG_PARAM_REF, "");
		params.put(TAG_PARAM_COMMENT, ((EditText)this.findViewById(R.id.txtcom)).getText());
		params.put(TAG_PARAM_HINT, ((EditText)this.findViewById(R.id.txthint)).getText());
		params.put(TAG_PARAM_SUMMARY, ((EditText)this.findViewById(R.id.txtSummary)).getText());
		params.put(TAG_PARAM_TAG, ((EditText)this.findViewById(R.id.txttags)).getText());
		params.put(TAG_PARAM_DIFFICULT, ((SeekBar)this.findViewById(R.id.cboCategory)).getProgress());
		params.put(TAG_PARAM_CATEGORY, ((Spinner)this.findViewById(R.id.cboCategory)).getSelectedItem().toString());
		params.put(TAG_PARAM_ITEMSUMMARY, mPNO);
		params.put(TAG_PARAM_ITEMCORRECT, mPNO);
		
	/*
	 * 
	private static final String TAG_PARAM_PNO = "pno";
	private static final String TAG_PARAM_REF = "ref";
	private static final String TAG_PARAM_COMMENT = "comment";
	private static final String TAG_PARAM_HINT = "hint";
	private static final String TAG_PARAM_SUMMARY = "summary";
	private static final String TAG_PARAM_TAG = "tag";
	private static final String TAG_PARAM_DIFFICULT = "difficult";
	private static final String TAG_PARAM_CATEGORY = "category";
	private static final String TAG_PARAM_ITEMSUMMARY = "itemsummary";
	private static final String TAG_PARAM_ITEMCORRECT = "itemcorrect";	
	 */
        Communication.post(TAG_SUBMIT, pack.GetParams(), new JsonHttpResponseHandler() {
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
	
    private boolean Validate(){
    	if(txtSummary.getText().toString().equals("")) { return false; }
    	String item1 = txtItem1.getText().toString();
    	String item2 = txtItem2.getText().toString();
    	String item3 = txtItem3.getText().toString();
    	String item4 = txtItem4.getText().toString();
    	int noCnt = 0;
    	if(item1.equals("")) { noCnt++; }
    	if(item2.equals("")) { noCnt++; }
    	if(item3.equals("")) { noCnt++; }
    	if(item4.equals("")) { noCnt++; }
    	if(noCnt > 3){
    		return false;
    	}

    	return true;
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
