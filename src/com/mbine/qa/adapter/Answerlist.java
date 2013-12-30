package com.mbine.qa.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.mbine.qa.R;
import com.mbine.qa.tool.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.*;
import android.widget.*;

@SuppressLint("ResourceAsColor")
public class Answerlist extends ArrayAdapter<HashMap<String, String>> {
	private static final String TAG_JSON_AEMAIL = "email";
	private static final String TAG_JSON_ASUMMARY = "summary";
	private static final String TAG_JSON_AISCORRECT = "iscorrect";
	private static final String TAG_JSON_AREGDATE = "regdate";
	private static final String TAG_JSON_AMEMBER = "member";
	private static final String TAG_JSON_AITEM = "item";
	private static final String TAG_JSON_ISCORNAME = "정답";

	Tools tool = new Tools();
	Context mContext;
    int mLayout;  
    ArrayList<HashMap<String, String>> mSource;
    TextView txtSummary, txtIsCor, txtEmail, txtRegdate, txtAItem;
	public Answerlist(Context context, int resource, ArrayList<HashMap<String,String>> aList) {
		super(context, resource, aList);
		mContext = context;
		mLayout = resource;
		mSource = aList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {         
		if (convertView == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = (View) inflater.inflate(R.layout.lv_a_with_info, null);
        }
		// TAG_JSON_AEMAIL, TAG_JSON_ASUMMARY, TAG_JSON_AISCORRECT, TAG_JSON_AREGDATE, TAG_JSON_AITEM 
        txtSummary = (TextView) convertView.findViewById(R.id.qsummary);
        txtEmail = (TextView) convertView.findViewById(R.id.regemail);
        txtRegdate = (TextView) convertView.findViewById(R.id.regdate);
        txtAItem = (TextView) convertView.findViewById(R.id.aitem);
        txtIsCor = (TextView) convertView.findViewById(R.id.ans_iscor);
        
        txtSummary.setText(mSource.get(position).get(TAG_JSON_ASUMMARY).toString());
        txtEmail.setText(mSource.get(position).get(TAG_JSON_AEMAIL).toString());
        txtRegdate.setText(mSource.get(position).get(TAG_JSON_AREGDATE).toString());
        txtAItem.setText(mSource.get(position).get(TAG_JSON_AITEM).toString());
        txtIsCor.setText(mSource.get(position).get(TAG_JSON_AISCORRECT).toString());

        txtAItem.setBackgroundResource((txtIsCor.getText().equals(TAG_JSON_ISCORNAME)) ? R.drawable.blueborder : R.drawable.redborder);
        txtIsCor.setBackgroundResource((txtIsCor.getText().equals(TAG_JSON_ISCORNAME)) ? R.color.dblue : R.color.dred);

		return convertView;  
	}
}