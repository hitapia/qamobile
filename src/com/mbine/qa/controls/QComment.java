package com.mbine.qa.controls;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QComment extends JsonCom {

	private static final String TAG_JSON_CS = "comments";

	private static final String TAG_JSON_CID = "seq";
	private static final String TAG_JSON_CPID = "qpid";
	private static final String TAG_JSON_CQID = "qid";
	private static final String TAG_JSON_CDEPTH = "depth";
	private static final String TAG_JSON_CPARENT = "parent";
	private static final String TAG_JSON_CMEMBERID = "member";
	private static final String TAG_JSON_CCONTENT = "content";
	private static final String TAG_JSON_CREGDATE = "regdate";

	private static final String TAG_JSON_MNAME = "name";
	private static final String TAG_JSON_MEMAIL = "email";
	private static final String TAG_JSON_MPOINT = "point";

	public ArrayList<HashMap<String,String>> MakeCommentList(JSONObject json) throws JSONException{
        ArrayList<HashMap<String,String>> ret = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> info = new HashMap<String,String>();

        JSONArray qs = json.getJSONArray(TAG_JSON_CS);
        for(int i = 0; i < qs.length(); i++){
        	JSONObject c = qs.getJSONObject(i);
        	info.put(TAG_JSON_CID, c.getString(TAG_JSON_CID));
        	info.put(TAG_JSON_CPID, c.getString(TAG_JSON_CPID));
        	info.put(TAG_JSON_CQID, c.getString(TAG_JSON_CQID));
        	info.put(TAG_JSON_CDEPTH, c.getString(TAG_JSON_CDEPTH));
        	info.put(TAG_JSON_CPARENT, c.getString(TAG_JSON_CPARENT));
        	info.put(TAG_JSON_CMEMBERID, c.getString(TAG_JSON_CMEMBERID));
        	info.put(TAG_JSON_CCONTENT, c.getString(TAG_JSON_CCONTENT));
        	info.put(TAG_JSON_CREGDATE, c.getString(TAG_JSON_CREGDATE));

        	info.put(TAG_JSON_MNAME, c.getString(TAG_JSON_MNAME));
        	info.put(TAG_JSON_MEMAIL, c.getString(TAG_JSON_MEMAIL));
        	info.put(TAG_JSON_MPOINT, c.getString(TAG_JSON_MPOINT));

        	ret.add(info);
        }
        
        return ret;
	}
}
