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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class DFavActivity extends BaseActivity {

    private static final String TAG_LISTINFO = "q/getFavlist_";

	Tools tool = new Tools();
    QPackage pack = new QPackage();
	ListView mFList;
    SimpleAdapter adapter = null;
    ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        this.setTitle("Category");

        GetControls();
        GetData();
	}

    private void GetControls(){
    	mFList = (ListView)findViewById(R.id.flist);
    }
    
    private void GetData(){
    	tool.ShowLoading(DFavActivity.this);
    	Communication.post(TAG_LISTINFO, pack.GetParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
            JSONArray qs;
                            try {
                                    qList.clear();
                                    qs = json.getJSONArray("flist");

                                    for(int i = 0; i < qs.length(); i++){
                                            HashMap<String, String> map = new HashMap<String,String>();
                                            map.put(TAG_DB_PNO, qs.getJSONObject(i).getString(TAG_DB_PNO));
                                            map.put(TAG_DB_PNAME, qs.getJSONObject(i).getString(TAG_DB_PNAME));
                                            map.put(TAG_DB_SUMMARY, qs.getJSONObject(i).getString(TAG_DB_SUMMARY));
                                            map.put(TAG_DB_COUNT, qs.getJSONObject(i).getString(TAG_DB_COUNT));
                    qList.add(map);
                                    }

                    adapter = new SimpleAdapter(DFavActivity.this, qList, R.layout.lv_title_summary_count,
                                    new String[] { TAG_DB_PNAME, TAG_DB_COUNT, TAG_DB_SUMMARY }, 
                                    new int[] { R.id.title, R.id.count, R.id.summary });
                    mFList.setAdapter(adapter);
                    mFList.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
                                Intent intent = new Intent(DFavActivity.this, QPackageActivity.class);
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

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_dfav;
	}

}
