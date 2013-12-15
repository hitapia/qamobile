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
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class QCActivity extends Fragment {

	Context mContext;
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_PCINFO = "q/getcommentqps_";
	private static final String TAG_JSON_CMEMBER = "name";
	private static final String TAG_JSON_CCOMMENT = "content";

	String mUNO = null;
	String mPNO = null;
	String mQNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();
	
	ListView mQList = null;
	
	public QCActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
        ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_qc, null);

			Bundle bundle = this.getArguments();
			mUNO = bundle.getString(TAG_UNO);
			mPNO = bundle.getString(TAG_PNO);
			
			mQList = (ListView)view.findViewById(R.id.qclist);

			SetView();
			return view;
	}

	private void SetView(){
        Communication.post(TAG_PCINFO+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
                JSONArray qs;
				try {
					qs = json.getJSONArray("comments");
					for(int i = 0; i < qs.length(); i++){
						HashMap<String, String> com = new HashMap<String, String>();
						com.put(TAG_JSON_CMEMBER, qs.getJSONObject(i).getString(TAG_JSON_CMEMBER));
						com.put(TAG_JSON_CCOMMENT, qs.getJSONObject(i).getString(TAG_JSON_CCOMMENT));
                        qList.add(com);
					}
					SimpleAdapter adapter = new SimpleAdapter(getActivity(), qList, R.layout.lv_comments, new String[] { TAG_JSON_CCOMMENT, TAG_JSON_CMEMBER }, new int[] { R.id.com_comment, R.id.com_name});
	                mQList.setAdapter(adapter);
	                mQList.setOnItemClickListener(new OnItemClickListener() {
	                        @Override
	                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                        	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
	                        	/*
	    						Intent intent = new Intent(QPListActivity.this, QPackageActivity.class);
	    						intent.putExtra(TAG_PNO, map.get(TAG_JSON_QID));
	    						intent.putExtra(TAG_PTITLE, map.get(TAG_JSON_QNAME));
	    						intent.putExtra(TAG_UNO, mUNO);
	    						startActivity(intent);
	    						*/
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
