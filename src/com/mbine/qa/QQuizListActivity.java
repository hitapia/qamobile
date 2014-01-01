package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.adapter.QuizList;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.*;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class QQuizListActivity extends BaseActivity {
	
	private static final String TAG_LISTINFO = "q/getmaininfo_";
	private static final String TAG_BUNDLETAG = "kind";
	private static final String TAG_TITLE_TODAY = "Today";
	private static final String TAG_TITLE_POPULAR = "Popular Quiz";
	private static final String TAG_TITLE_FAVORITE = "My Favorites";
	private static final String TAG_TITLE_ALL = "All Quiz";
	private static final String TAG_FNO = "fno";

	QPackage pack = new QPackage();
	Tools tool = new Tools();
	String mShowKind = "today";
	String mFavNo = "";
	ListView mQList = null;
	ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mShowKind = intent.getStringExtra(TAG_BUNDLETAG);
		if(intent.hasExtra(TAG_FNO)){
			mFavNo = intent.getStringExtra(TAG_FNO);
		}

		GetControls();
		SetTitle();
		GetData();
	}
	
	private void SetTitle(){
		if(mShowKind.equals("today"))
			getActionBar().setTitle(TAG_TITLE_TODAY);
		if(mShowKind.equals("popular"))
			getActionBar().setTitle(TAG_TITLE_POPULAR);
		if(mShowKind.equals("favorite"))
			getActionBar().setTitle(TAG_TITLE_FAVORITE);
		if(mShowKind.equals("all"))
			getActionBar().setTitle(TAG_TITLE_ALL);
	}

	@Override
    protected int getLayoutResourceId() {
        return R.layout.activity_qquiz_list;
    }
	
	private void GetControls(){
		mQList = (ListView)findViewById(R.id.alist);
	}
	
	private void GetData(){
		tool.ShowLoading(QQuizListActivity.this);
		RequestParams params = pack.GetParams();
		params.put("kind", "list");
		params.put("show", mShowKind);
		params.put("pno", mFavNo);
        Communication.post(TAG_LISTINFO, params, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
                JSONArray qs;
				try {
					qList.clear();
					qs = json.getJSONArray(mShowKind);

					for(int i = 0; i < qs.length(); i++){
						HashMap<String, String> map = new HashMap<String,String>();
						map.put(TAG_DB_SEQ, qs.getJSONObject(i).getString(TAG_DB_SEQ));
						map.put(TAG_DB_SUMMARY, qs.getJSONObject(i).getString(TAG_DB_SUMMARY));
						map.put(TAG_DB_PNO, qs.getJSONObject(i).getString(TAG_DB_PNO));
						map.put(TAG_DB_PNAME, qs.getJSONObject(i).getString(TAG_DB_PNAME));
						map.put(TAG_DB_INCORRECT, qs.getJSONObject(i).getString(TAG_DB_INCORRECT));
						map.put(TAG_DB_CORRECT, qs.getJSONObject(i).getString(TAG_DB_CORRECT));
                        qList.add(map);
					}

	                mQList.setAdapter(new QuizList(QQuizListActivity.this, R.layout.lv_a_with_info, qList));
	                mQList.setOnItemClickListener(new OnItemClickListener() {
	                        @Override
	                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                        	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
	    						Intent intent = new Intent(QQuizListActivity.this, QAnswerActivity.class);
	    						intent.putExtra(TAG_QNO, map.get(TAG_DB_SEQ));
	    						intent.putExtra(TAG_PNO, map.get(TAG_DB_PNO));
	    						startActivity(intent);
	                        }
	                });
				} catch (JSONException e) {
					e.printStackTrace();
				}
                tool.ExitLoading();
        	}
        });	
	}
}
