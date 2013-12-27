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
import com.mbine.qa.tool.Storage;
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
	private static final String TAG_JSON_ACOR = "ans_cor";
	private static final String TAG_JSON_QMEMBER = "regname";
	private static final String TAG_JSON_QCORRECT = "correct";
	private static final String TAG_JSON_QINCORRECT = "incorrect";


	String mUNO = null;
	String mPNO = null;
	String mQNO = null;
	Tools tool = new Tools();
	QPackage pack = new QPackage();
	ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();
	
	ListView mQList = null;
	Button mBtnAdd = null;
	View view = null;

	public QQActivity(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_qp, null);

		GetControls();
		SetEvents();

		Bundle bundle = this.getArguments();

		Storage str = new Storage(getActivity());
		mUNO = str.pull(TAG_UNO, "");
		mPNO = bundle.getString(TAG_PNO);

		SetView();

    	return view;
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
	
	private void GetControls(){
		mQList = (ListView)view.findViewById(R.id.qlist);
		mBtnAdd = (Button)view.findViewById(R.id.btnAddinP);
	}

	private void SetView(){
		tool.ShowLoading(getActivity());
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
							qList.add(quest.MakeQInfo(qs.getJSONObject(i), mUNO));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					SimpleAdapter adapter = new SimpleAdapter(getActivity(), qList, R.layout.lv_q_with_info
                        , new String[] { TAG_JSON_QSUMMARY, TAG_JSON_QMEMBER, TAG_JSON_ACOR, TAG_JSON_QCORRECT, TAG_JSON_QINCORRECT }
                        , new int[] { R.id.qlist_name, R.id.regname, R.id.ans_iscor, R.id.txtcor, R.id.txtincor });
	                mQList.setAdapter(adapter);
	                mQList.setOnItemClickListener(new OnItemClickListener() {
	                        @Override
	                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                        	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
	    						Intent intent = new Intent(getActivity(), QQMainActivity.class);
	    						intent.putExtra(TAG_QNO, map.get(TAG_JSON_QID));
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
