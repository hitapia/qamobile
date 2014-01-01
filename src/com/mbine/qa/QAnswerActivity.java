package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.controls.QCommon;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class QAnswerActivity extends Activity {

	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_QNO = "qno";
	private static final String TAG_PINFO = "q/info_q_";
	private static final String TAG_ANSWER = "q/answer_";
	private static final String TAG_JSON_QSUMMARY = "summary";
	private static final String TAG_JSON_QUSED = "used";
	private static final String TAG_JSON_ASEQ = "seq";
	private static final String TAG_JSON_ACORRECT = "iscorrect";

	String mUNO = null;
	String mQNO = null;
	String mPNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	QCommon mCom = new QCommon();
	
	String ans = null;
	String isCor = null;
	TextView mSummary = null;
	Button mSubmit = null;
	ListView mList = null;
	HashMap<String,String> qItems = new HashMap<String, String>();
	HashMap<String,String> qItemCor = new HashMap<String, String>();
	ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qanswer);

		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		mPNO = intent.getStringExtra(TAG_PNO);
		mQNO = intent.getStringExtra(TAG_QNO);
		
		this.setTitle("문제 풀기");

		tool.ShowLoading(QAnswerActivity.this);

		mSummary = (TextView)findViewById(R.id.pinfo_summry);
		mList = (ListView)findViewById(R.id.itemlist);
		mSubmit = (Button)findViewById(R.id.btnAnswer);
        mList.setClickable(true);
        mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		mSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ans == null || isCor == null){
					tool.showPop(QAnswerActivity.this, "답변을 선택해주세요.", "OK");
				}else{
					RequestParams params = pack.GetParams();
					params.put("useranswer", ans);
			        Communication.post(TAG_ANSWER+"/"+mQNO, params, new JsonHttpResponseHandler() {
			        	@Override
			        	public void onSuccess(JSONObject json) {
							Intent intent = new Intent(QAnswerActivity.this, QAnswerResultActivity.class);
							intent.putExtra("QNO", mQNO);
							intent.putExtra("PNO", mPNO);
							intent.putExtra("UNO", mUNO);
							intent.putExtra("AResult", isCor);
							startActivity(intent);
							finish();
			        	}
			        });
					
				}
			}
		});
        SetView();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                        int arg2, long arg3) {
                	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
                	arg1.setSelected(true);
                	//mList.setItemChecked(arg2, true);
                	isCor = map.get(TAG_JSON_ACORRECT);
                	ans = map.get(TAG_JSON_ASEQ);
                }
        });

        tool.ExitLoading();
	}

	private void SetQInfo(JSONObject data) throws JSONException{
		mSummary.setText(data.getString(TAG_JSON_QSUMMARY));
	}

	private void SetView(){
        Communication.post(TAG_PINFO+"/"+mQNO, mCom.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		JSONArray q;
				try {
					q = json.getJSONArray("q");
	        		SetQInfo(q.getJSONObject(0));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                JSONArray qs;
				try {
					qs = json.getJSONArray("a");
					aList.clear();
					for(int i = 0; i < qs.length(); i++){
						HashMap<String, String> com = new HashMap<String, String>();
						com.put(TAG_JSON_QSUMMARY, qs.getJSONObject(i).getString(TAG_JSON_QSUMMARY));
						com.put(TAG_JSON_QUSED, qs.getJSONObject(i).getString(TAG_JSON_QUSED));
						com.put(TAG_JSON_ACORRECT, qs.getJSONObject(i).getString(TAG_JSON_ACORRECT));
						com.put(TAG_JSON_ASEQ, qs.getJSONObject(i).getString(TAG_JSON_ASEQ));
						aList.add(com);
					}
					SimpleAdapter adapter = new SimpleAdapter(QAnswerActivity.this, aList, R.layout.lv_qitem_only, new String[] { TAG_JSON_QSUMMARY }, new int[] { R.id.plist_name});
					mList.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qanswer, menu);
		return true;
	}

}
