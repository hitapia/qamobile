package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.controls.QPackageList;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class QPListActivity extends Activity {
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_PTITLE = "ptitle";
	private static final String TAG_QLIST = "q/qpmain_";
	private static final String TAG_JSON_QID = "seq";
	private static final String TAG_JSON_QNAME = "name";
	private static final String TAG_JSON_QCOUNT = "questcount";
	private static final String TAG_KEYWORD = "keyword";

	JSONArray qs = null;
	Tools tool = new Tools();
	QPackageList pack = new QPackageList();
	String mUNO = null;
	ListView qplist = null;
	String mKeyword = null;
	EditText txtSearch = null;
	Button btnNew = null;
	Button btnSearch = null;
	ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qplist);

	    getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		mKeyword = intent.getStringExtra(TAG_KEYWORD);
		
		GetControls();
		tool.ShowLoading(QPListActivity.this);
		DataBinding();
		tool.ExitLoading();
		getActionBar().setTitle(intent.getStringExtra("Q Package List"));

		txtSearch = (EditText)findViewById(R.id.txtsearch);
		btnSearch = (Button)findViewById(R.id.btn_search);
		btnNew = (Button)findViewById(R.id.btnnew);
		if(!mKeyword.equals("")){
			txtSearch.setText(mKeyword);
			btnNew.setText(mKeyword + " Package 만들기");
		}else{
			btnNew.setVisibility(View.GONE);
		}
		CreateEvent();
	}

	private void CreateEvent(){
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!txtSearch.getText().toString().equals("")){
					mKeyword = txtSearch.getText().toString();
					tool.ShowLoading(QPListActivity.this);
					DataBinding();
					tool.ExitLoading();
				}
			}
		});

		btnNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QPListActivity.this, PCreateActivity.class);
				intent.putExtra(TAG_UNO, mUNO);
                intent.putExtra(TAG_KEYWORD, mKeyword);
				startActivity(intent);
			}
		});
	}

	private void DataBinding(){
		RequestParams params = pack.GetParams();
		params.put(TAG_KEYWORD, mKeyword);
        Communication.post(TAG_QLIST, params, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		qList = pack.MakeList(json);
                SimpleAdapter adapter = new SimpleAdapter(QPListActivity.this, qList, R.layout.lv_plist_normal, new String[] { TAG_JSON_QNAME, TAG_JSON_QCOUNT }, new int[] { R.id.plist_name, R.id.plist_Count });
                qplist.setAdapter(adapter);
                qplist.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
    						Intent intent = new Intent(QPListActivity.this, QPackageActivity.class);
    						intent.putExtra(TAG_PNO, map.get(TAG_JSON_QID));
    						intent.putExtra(TAG_PTITLE, map.get(TAG_JSON_QNAME));
    						intent.putExtra(TAG_UNO, mUNO);
    						startActivity(intent);
                        }
                });
        	}
        });	
	}	

	private void GetControls(){
		qplist = (ListView)findViewById(R.id.qplist);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qplist, menu);
		return true;
	}

}
