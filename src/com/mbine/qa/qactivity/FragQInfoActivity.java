package com.mbine.qa.qactivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbine.qa.LoginActivity;
import com.mbine.qa.QCreateActivity;
import com.mbine.qa.QPListActivity;
import com.mbine.qa.QPackageActivity;
import com.mbine.qa.R;
import com.mbine.qa.R.layout;
import com.mbine.qa.R.menu;
import com.mbine.qa.controls.JsonCom;
import com.mbine.qa.controls.QCommon;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class FragQInfoActivity extends Fragment {

	Context mContext;
	private static final String TAG_UNO = "uno";
	private static final String TAG_QNO = "qno";
	private static final String TAG_PINFO = "q/info_q_";
	private static final String TAG_JSON_QEMAIL = "member";
	private static final String TAG_JSON_PCONTENT = "content";
	private static final String TAG_JSON_QSUMMARY = "summary";
	private static final String TAG_JSON_QUSED = "used";
	private static final String TAG_JSON_QCORRECT = "correct";
	private static final String TAG_JSON_QINCORRECT = "incorrect";
	private static final String TAG_JSON_QPOINT = "point";
	private static final String TAG_JSON_QCOMMENT = "comment";
	private static final String TAG_JSON_QHINT = "hint";
	private static final String TAG_JSON_QDIFFICULT = "difficult";
	private static final String TAG_JSON_QREGDATE = "regdate";
	private static final String TAG_JSON_QCATEGORY = "category";
	private static final String TAG_JSON_NAME = "name";

	String mUNO = null;
	String mQNO = null;
	Tools tool = new Tools();
	QCommon mCom = new QCommon();
	
	TextView mSummary = null, mCategory = null, mMember = null, mDiff = null, mComment = null, mhint = null;
	RelativeLayout mTags = null;
	Button mBtnCor = null;
	Button mBtnInCor = null;
	ListView mList = null;
	ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
	
	public FragQInfoActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
        ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_frag_qinfo, container, false);

			Bundle bundle = this.getArguments();
			mUNO = bundle.getString(TAG_UNO);
			mQNO = bundle.getString(TAG_QNO);
			
			mSummary = (TextView)view.findViewById(R.id.pinfo_summry);
			mBtnCor = (Button)view.findViewById(R.id.btn_cor);
			mBtnInCor = (Button)view.findViewById(R.id.btn_incor);
			mCategory = (TextView)view.findViewById(R.id.txt_category);
			mMember = (TextView)view.findViewById(R.id.txt_member);
			mDiff = (TextView)view.findViewById(R.id.txt_difficulty);
			mComment = (TextView)view.findViewById(R.id.txt_comment);
			mhint = (TextView)view.findViewById(R.id.txt_hint);
			mList = (ListView)view.findViewById(R.id.itemlist);

			/*
			mBtnCor.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});

			mBtnInCor.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			*/
			SetView();
			return view;
	}
	
	private void SetQInfo(JSONObject data) throws JSONException{
		mSummary.setText(data.getString(TAG_JSON_QSUMMARY));
		mBtnCor.setText(data.getString(TAG_JSON_QCORRECT));
		mBtnInCor.setText(data.getString(TAG_JSON_QINCORRECT));
		mDiff.setText(data.getString(TAG_JSON_QDIFFICULT));
		mComment.setText(data.getString(TAG_JSON_QCOMMENT));
		mhint.setText(data.getString(TAG_JSON_QHINT));
	}

	private void SetView(){
        Communication.post(TAG_PINFO+"/"+mQNO, mCom.GetParams(), new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		JSONArray q, p, m, c;
				try {
					q = json.getJSONArray("q");
	        		SetQInfo(q.getJSONObject(0));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					p = json.getJSONArray("p");
	        		JSONObject qpackage = p.getJSONObject(0);
	        		mMember.setText(qpackage.getString(TAG_JSON_QEMAIL));
	        		getActivity().getActionBar().setTitle(qpackage.getString(TAG_JSON_NAME));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					m = json.getJSONArray("m");
	        		JSONObject member = m.getJSONObject(0);
	        		mMember.setText(member.getString(TAG_JSON_QEMAIL));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					c = json.getJSONArray("c");
	        		JSONObject category = c.getJSONObject(0);
	        		mCategory.setText(category.getString(TAG_JSON_NAME));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                JSONArray qs;
				try {
					qs = json.getJSONArray("a");
					for(int i = 0; i < qs.length(); i++){
						HashMap<String, String> com = new HashMap<String, String>();
						com.put(TAG_JSON_QSUMMARY, qs.getJSONObject(i).getString(TAG_JSON_QSUMMARY));
						com.put(TAG_JSON_QUSED, qs.getJSONObject(i).getString(TAG_JSON_QUSED));
						aList.add(com);
					}
					SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.lv_qitem_normal, new String[] { TAG_JSON_QSUMMARY, TAG_JSON_QUSED }, new int[] { R.id.plist_name, R.id.plist_Count});
					mList.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });	
	}
}
