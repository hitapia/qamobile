package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbine.qa.adapter.Answerlist;
import com.mbine.qa.controls.QAnswer;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.controls.QQuest;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Storage;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
public class QMActivity extends Fragment {

	Context mContext;
	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_QNO = "qno";
	private static final String TAG_AINFO = "q/alistbypno_";
	private static final String TAG_JSON_AID = "qid";
	private static final String TAG_JSON_AEMAIL = "email";
	private static final String TAG_JSON_ASUMMARY = "summary";
	private static final String TAG_JSON_AISCORRECT = "iscorrect";
	private static final String TAG_JSON_AREGDATE = "regdate";
	private static final String TAG_JSON_AMEMBER = "member";
	private static final String TAG_JSON_AITEM = "item";

	String mUNO = null;
	String mPNO = null;
	String mQNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
	
	Button mBtnAdd = null;
	View view = null;
	ListView mAList = null;
	
	public QMActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
        ViewGroup container, Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.activity_qm, null);

			Bundle bundle = this.getArguments();
			Storage str = new Storage(getActivity());
			mUNO = str.pull(TAG_UNO, "");
			mPNO = bundle.getString(TAG_PNO);

			GetControls();
			SetEvents();

			SetView();
			return view;
	}

	private void GetControls(){
		mAList = (ListView)view.findViewById(R.id.alist);
        mBtnAdd = (Button)view.findViewById(R.id.btnAddinP);
	}
	
	private void SetEvents(){
		mBtnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), QCreateActivity.class);
				intent.putExtra(TAG_UNO, mUNO);
				intent.putExtra(TAG_PNO, mPNO);
				startActivity(intent);
			}
		});
	}
	private void SetView(){
        Communication.post(TAG_AINFO+"/"+mPNO, pack.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
                JSONArray qs;
				try {
					aList.clear();
					qs = json.getJSONArray("a");
					for(int i = 0; i < qs.length(); i++){
                        QAnswer answer = new QAnswer();
                        try {
							aList.add(answer.MakeAInfoByPackage(qs.getJSONObject(i), mUNO));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					/*
					SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.lv_a_with_info
                        , new String[] { TAG_JSON_AEMAIL, TAG_JSON_ASUMMARY, TAG_JSON_AISCORRECT, TAG_JSON_AREGDATE, TAG_JSON_AITEM }
                        , new int[] { R.id.regemail, R.id.qsummary, R.id.ans_iscor, R.id.regdate, R.id.aitem });
                        */
	                mAList.setAdapter(new Answerlist(getActivity(), R.layout.lv_a_with_info, aList));
	                mAList.setOnItemClickListener(new OnItemClickListener() {
	                        @Override
	                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                        	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
	    						Intent intent = new Intent(getActivity(), QQMainActivity.class);
	    						intent.putExtra(TAG_QNO, map.get(TAG_JSON_AID));
	    						intent.putExtra(TAG_PNO, mPNO);
	    						intent.putExtra(TAG_UNO, mUNO);
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
