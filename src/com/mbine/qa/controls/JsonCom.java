package com.mbine.qa.controls;

import com.loopj.android.http.RequestParams;

public class JsonCom {

	protected static final String TAG_MOBILECODE = "kmobile";
	protected static final String TAG_MOBILECODEVALUE = "1";
	protected static final String TAG_USERSEQ = "userseq";
	protected static final String TAG_RESULTOKCODE = "1";

	protected RequestParams GetParams(){
		RequestParams params = new RequestParams();
		params.put(TAG_MOBILECODE, TAG_MOBILECODEVALUE);
		params.put(TAG_USERSEQ, "1");
		return params;
	}
}
