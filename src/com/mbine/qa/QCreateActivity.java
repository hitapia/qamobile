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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class QCreateActivity extends Activity {

	private static final String TAG_RESULT = "result";
	private static final String TAG_RESULTOKCODE = "1";
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_CODE_C = "cs";
	private static final String TAG_CODE_P = "p";
	private static final String TAG_C_NAME = "name";
	private static final String TAG_C_NAMECODE = "seq";
	private static final String TAG_P_CATEGROY = "category";
	private static final String TAG_CATEGORY = "q/getCategory_";
	private static final String TAG_PINFO = "q/qpinfo_";
	private static final String TAG_SUBMIT = "q/register_";
	private static final String TAG_CATEGORY_SEL = "분류 선택";
	
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
	HashMap<String,String> mCategoryList = new HashMap<String, String>();
	
	Spinner spCategory = null;
	SeekBar sbDiff = null;
	Button btnCreate = null;
	Button btnCreateNext = null;
	
	EditText txtSummary = null;
	EditText txtComment = null;
	EditText txtTags = null;
	EditText txtHint = null;
	EditText txtItem1 = null;
	EditText txtItem2 = null;
	EditText txtItem3 = null;
	EditText txtItem4 = null;
	Button btn1 = null;
	Button btn2 = null;
	Button btn3 = null;
	Button btn4 = null;

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
		txtSummary = (EditText)this.findViewById(R.id.txtSummary);
		txtTags = (EditText)this.findViewById(R.id.txttags);
		txtComment = (EditText)this.findViewById(R.id.txtcom);
		txtHint = (EditText)this.findViewById(R.id.txthint);
		txtItem1 = (EditText)this.findViewById(R.id.qitem1);
		txtItem2 = (EditText)this.findViewById(R.id.qitem2);
		txtItem3 = (EditText)this.findViewById(R.id.qitem3);
		txtItem4 = (EditText)this.findViewById(R.id.qitem4);
		btn1 = (Button)this.findViewById(R.id.tgl1);
		btn2 = (Button)this.findViewById(R.id.tgl2);
		btn3 = (Button)this.findViewById(R.id.tgl3);
		btn4 = (Button)this.findViewById(R.id.tgl4);

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
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { SetAllInCo(btn1); }
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { SetAllInCo(btn2); }
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { SetAllInCo(btn3); }
		});
		btn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { SetAllInCo(btn4); }
		});
	}
	
	private void SetAllInCo(Button btn){
        btn1.setText("오답");
        btn2.setText("오답");
        btn3.setText("오답");
        btn4.setText("오답");
        btn.setText("정답");
	}
	
	private String chkField(EditText ed){
		return (ed.getText() == null) ? "" : ed.getText().toString();
	}
	
	private void Submit(){
		RequestParams params = pack.GetParams();
		params.put(TAG_PARAM_PNO, mPNO);
		params.put(TAG_PARAM_REF, "");
		params.put(TAG_PARAM_COMMENT, chkField(txtComment));
		params.put(TAG_PARAM_HINT, chkField(txtHint));
		params.put(TAG_PARAM_SUMMARY, chkField(txtSummary));
		params.put(TAG_PARAM_TAG, chkField(txtTags));
		int p = sbDiff.getProgress();
		params.put(TAG_PARAM_DIFFICULT, String.valueOf(p));
		params.put(TAG_PARAM_CATEGORY, mCategoryList.get(spCategory.getSelectedItem().toString()).toString());
		
		ArrayList<String> items = new ArrayList<String>();
		items.add(chkField((EditText)this.findViewById(R.id.qitem1)));
		items.add(chkField((EditText)this.findViewById(R.id.qitem2)));
		items.add(chkField((EditText)this.findViewById(R.id.qitem3)));
		items.add(chkField((EditText)this.findViewById(R.id.qitem4)));
		params.put(TAG_PARAM_ITEMSUMMARY, items);

		ArrayList<String> itemCors = new ArrayList<String>();
		itemCors.add((btn1.getText().toString() == "정답") ? "Y" : "N");
		itemCors.add((btn2.getText().toString() == "정답") ? "Y" : "N");
		itemCors.add((btn3.getText().toString() == "정답") ? "Y" : "N");
		itemCors.add((btn4.getText().toString() == "정답") ? "Y" : "N");
		params.put(TAG_PARAM_ITEMCORRECT, itemCors);

        Communication.post(TAG_SUBMIT, params, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		try {
        			if(json.getString(TAG_RESULT).equals(TAG_RESULTOKCODE)){
						Intent intent = new Intent(QCreateActivity.this, QQMainActivity.class);
						intent.putExtra("QNO", json.getString("q"));
						startActivity(intent);
						finish();
        			}else{
        				tool.showPop(QCreateActivity.this, "Error!!!", "OK");
        			}
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
                        mCategoryList.put(clist.getJSONObject(i).getString(TAG_C_NAME), clist.getJSONObject(i).getString(TAG_C_NAMECODE));
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
