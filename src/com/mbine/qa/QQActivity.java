package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.controls.QQuest;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class QQActivity extends Fragment {

	Context mContext;
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_QNO = "qno";
	private static final String TAG_JSON_QID = "seq";
	private static final String TAG_PINFO = "q/packqlist_";
	private static final String TAG_JSON_QSUMMARY = "summary";

	String mUNO = null;
	String mPNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();
	
	ListView mQList = null;

	public QQActivity(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_qp, null);


		Bundle bundle = this.getArguments();
		mUNO = bundle.getString(TAG_UNO);
		mPNO = bundle.getString(TAG_PNO);
		
		mQList = (ListView)view.findViewById(R.id.qlist);

		SetView();
    	return view;
	}

	private void SetView(){
        Communication.post(TAG_PINFO+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
                JSONArray qs;
				try {
					qList.clear();
					qs = json.getJSONArray("q");
					for(int i = 0; i < qs.length(); i++){
                        QQuest quest = new QQuest();
                        try {
							qList.add(quest.MakeQInfo(qs.getJSONObject(i)));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					SimpleAdapter adapter = new SimpleAdapter(getActivity(), qList, R.layout.lv_qlist_nomal, new String[] { TAG_JSON_QSUMMARY }, new int[] { R.id.qlist_name });
	                mQList.setAdapter(adapter);
	                mQList.setOnItemClickListener(new OnItemClickListener() {
	                        @Override
	                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                        	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
	    						Intent intent = new Intent(getActivity(), QQMainActivity.class);
	    						intent.putExtra(TAG_QNO, map.get(TAG_JSON_QID));
	    						intent.putExtra(TAG_UNO, mUNO);
	    						startActivity(intent);
	                        }
	                });
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                
        	}
        });	
	}

}
