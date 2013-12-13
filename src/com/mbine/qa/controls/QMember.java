package com.mbine.qa.controls;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class QMember extends JsonCom {

	private static final String TAG_JSON_MID = "seq";
	private static final String TAG_JSON_MNAME = "name";
	private static final String TAG_JSON_MEMAIL = "email";
	private static final String TAG_JSON_MPOINT = "point";
	private static final String TAG_JSON_MSNSTYPE = "snstype";

	public ArrayList<HashMap<String,String>> MakePMember(JSONObject c) throws JSONException{
        ArrayList<HashMap<String,String>> ret = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> info = new HashMap<String,String>();

        info.put(TAG_JSON_MID, c.getString(TAG_JSON_MID));
        info.put(TAG_JSON_MNAME, c.getString(TAG_JSON_MNAME));
        info.put(TAG_JSON_MEMAIL, c.getString(TAG_JSON_MEMAIL));
        info.put(TAG_JSON_MPOINT, c.getString(TAG_JSON_MPOINT));
        info.put(TAG_JSON_MSNSTYPE, c.getString(TAG_JSON_MSNSTYPE));
        
        ret.add(info);
        return ret;
	}
}
