/**
 * 
 */
package com.mbine.qa.controls;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bang
 *
 */
public class QAnswer {

	private static final String TAG_JSON_AID = "qid";
	private static final String TAG_JSON_AEMAIL = "email";
	private static final String TAG_JSON_ASUMMARY = "summary";
	private static final String TAG_JSON_AISCORRECT = "iscorrect";
	private static final String TAG_JSON_AREGDATE = "regdate";
	private static final String TAG_JSON_AMEMBER = "member";
	private static final String TAG_JSON_AITEM = "item";

	public HashMap<String,String> MakeAInfoByPackage(JSONObject c, String _loguser) throws JSONException{
        HashMap<String,String> info = new HashMap<String,String>();

        info.put(TAG_JSON_AID, c.getString(TAG_JSON_AID));
        info.put(TAG_JSON_AEMAIL, c.getString(TAG_JSON_AEMAIL));
        info.put(TAG_JSON_ASUMMARY, c.getString(TAG_JSON_ASUMMARY));
        info.put(TAG_JSON_AREGDATE, c.getString(TAG_JSON_AREGDATE));
        info.put(TAG_JSON_AITEM, c.getString(TAG_JSON_AITEM));
        
        if(c.getString(TAG_JSON_AMEMBER).equals(_loguser)){
        	info.put(TAG_JSON_AMEMBER, "나");
        }else{
        	info.put(TAG_JSON_AMEMBER, c.getString(TAG_JSON_AMEMBER));
        }

        String mem = null;
        if(c.getString(TAG_JSON_AISCORRECT).equals("Y")){
        	mem = "정답";
        }else{
        	mem = "오답";
        }
        info.put(TAG_JSON_AISCORRECT, mem);
        
        return info;
	}
}
