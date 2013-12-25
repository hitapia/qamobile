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
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PCreateActivity extends Activity {

	String mUNO = null;
	String mKeyword = null;
	private static final String TAG_RESULT = "result";
	private static final String TAG_RESULTOKCODE = "1";
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_KEYWORD = "keyword";
	private static final String TAG_SUBMIT = "q/registerqp_";
	private static final String TAG_PARAM_COMMENT = "comment";
	private static final String TAG_PARAM_SUMMARY = "summary";
	private static final String TAG_PARAM_CATEGORY = "category";
	private static final String TAG_CATEGORY = "q/getCategory_";
	private static final String TAG_CATEGORY_SEL = "분류 선택";
	private static final String TAG_CODE_C = "cs";
	private static final String TAG_C_NAME = "name";
	private static final String TAG_C_NAMECODE = "seq";
	Button btnCreate = null;
	EditText txtSummary = null;
	EditText txtKeyword = null;
	Spinner spCategory = null;
	HashMap<String,String> mCategoryList = new HashMap<String, String>();

	QPackage pack = new QPackage();
	Tools tool = new Tools();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pcreate);
		
		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		mKeyword = intent.getStringExtra(TAG_KEYWORD);

		spCategory = (Spinner)this.findViewById(R.id.cboCategory);
		btnCreate = (Button)findViewById(R.id.btn_create);
		txtSummary = (EditText)findViewById(R.id.summary);
		txtKeyword = (EditText)findViewById(R.id.txtKeyword);
		
		txtKeyword.setText(mKeyword);

		GetCategory();
		btnCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(txtKeyword.getText().toString().equals("") || txtSummary.getText().toString().equals("")){
					tool.showPop(PCreateActivity.this, "모든 입력값에 입력해야합니다 ", "OK");
					return;
				}
				String category = mCategoryList.get(spCategory.getSelectedItem().toString()).toString();
				if (category.equals("")) {
					tool.showPop(PCreateActivity.this, "분류를 선택해주세요.", "OK");
					return;
				}

				RequestParams params = pack.GetParams();
				params.put(TAG_PARAM_COMMENT, chkField(txtSummary));
				params.put(TAG_PARAM_SUMMARY, chkField(txtKeyword));
				params.put(TAG_PARAM_CATEGORY, mCategoryList.get(spCategory.getSelectedItem().toString()).toString());

		        Communication.post(TAG_SUBMIT, params, new JsonHttpResponseHandler() {
		        	@Override
		        	public void onSuccess(JSONObject json) {
		        		try {
		        			if(json.getString(TAG_RESULT).equals(TAG_RESULTOKCODE)){
								Intent intent = new Intent(PCreateActivity.this, QPackageActivity.class);
								intent.putExtra(TAG_PNO, json.getString("q"));
								startActivity(intent);
								finish();
		        			}else{
		        				tool.showPop(PCreateActivity.this, "Error!!!", "OK");
		        			}
						} catch (JSONException e) {
							e.printStackTrace();
						}
		        	}
		        });	
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
        			mCategoryList.put(TAG_CATEGORY_SEL, "");
					JSONArray clist = json.getJSONArray(TAG_CODE_C);
                    for(int i = 0; i < clist.length(); i++){
                        arrayList.add(clist.getJSONObject(i).getString(TAG_C_NAME));
                        mCategoryList.put(clist.getJSONObject(i).getString(TAG_C_NAME), clist.getJSONObject(i).getString(TAG_C_NAMECODE));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PCreateActivity.this, R.layout.uc_spinner_item, arrayList);
                    spCategory.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
        });	
	}
	
	private String chkField(EditText ed){
		return (ed.getText() == null) ? "" : ed.getText().toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pcreate, menu);
		return true;
	}

}
