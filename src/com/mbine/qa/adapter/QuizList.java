/**
 * 
 */
package com.mbine.qa.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mbine.qa.R;
import com.mbine.qa.tool.Tools;

/**
 * @author Bang
 *
 */
public class QuizList extends ArrayAdapter<HashMap<String, String>> {
	private static final String TAG_JSON_QSUMMARY = "summary";
	private static final String TAG_JSON_QINCORRECT = "incorrect";
	private static final String TAG_JSON_QCORRECT = "correct";
	private static final String TAG_JSON_QPNO = "pno";
	private static final String TAG_JSON_QPNAME = "pname";

	Tools tool = new Tools();
	Context mContext;
    int mLayout;  
    ArrayList<HashMap<String, String>> mSource;
    TextView txtSummary, txtAvg, txtPName;

	public QuizList(Context context, int resource, ArrayList<HashMap<String,String>> aList) {
		super(context, resource, aList);
		mContext = context;
		mLayout = resource;
		mSource = aList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {         
		if (convertView == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = (View) inflater.inflate(R.layout.lv_q_hide_list, null);
        }
		// TAG_JSON_AEMAIL, TAG_JSON_ASUMMARY, TAG_JSON_AISCORRECT, TAG_JSON_AREGDATE, TAG_JSON_AITEM 
        txtSummary = (TextView) convertView.findViewById(R.id.summary);
        txtPName = (TextView) convertView.findViewById(R.id.txtpanme);
        txtAvg = (TextView) convertView.findViewById(R.id.txtavg);
        
        int cor = Integer.parseInt(mSource.get(position).get(TAG_JSON_QCORRECT).toString());
        int incor = Integer.parseInt(mSource.get(position).get(TAG_JSON_QINCORRECT).toString());
        String avg = "0";
        if(cor > 0 && incor > 0){
        	avg = String.valueOf((int)(cor * 100 / (cor + incor)));
        }
        avg += "%";
        
        txtPName.setText(mSource.get(position).get(TAG_JSON_QPNAME).toString());
        txtSummary.setText(tool.Shuffle(mSource.get(position).get(TAG_JSON_QSUMMARY).toString()));
        txtAvg.setText(avg);

		return convertView;  
	}
}