package com.mbine.qa.controls;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

public class QPackage extends JsonCom {

	private static final String TAG_JSON_P = "p";
	private static final String TAG_JSON_Q = "q";
	private static final String TAG_JSON_M = "m";
	private static final String TAG_JSON_PID = "seq";
	private static final String TAG_JSON_PMEMBER = "member";
	private static final String TAG_JSON_PQCOUNT = "questcount";
	private static final String TAG_JSON_PUSED = "used";
	private static final String TAG_JSON_PREGDATE = "regdate";
	private static final String TAG_JSON_PCATEGORY = "category";
	private static final String TAG_JSON_PVUP = "vup";
	private static final String TAG_JSON_PVDOWN = "vdown";
	private static final String TAG_JSON_PCONTENT = "content";
	private static final String TAG_JSON_PNAME = "name";

	JSONObject json = null;

	public HashMap<String, ArrayList<HashMap<String, String>>> Info(JSONObject json){
        HashMap<String, ArrayList<HashMap<String, String>>> qInfo 
                = new HashMap<String, ArrayList<HashMap<String, String>>>();
        String result = null;
		try {
			result = json.getString("result");
		} catch (JSONException e1) {
			return null;
		}
        if(result.equals(TAG_RESULTOKCODE)){
        	 try {
                QQuest quest = new QQuest();
                QMember member = new QMember();
                JSONArray jp = json.getJSONArray(TAG_JSON_P);
                qInfo.put("p", MakePInfo(jp.getJSONObject(0)));

                JSONArray jm = json.getJSONArray(TAG_JSON_M);
                qInfo.put("m", member.MakePMember(jm.getJSONObject(0)));

                JSONArray jq = json.getJSONArray(TAG_JSON_Q);
                ArrayList<HashMap<String,String>> qlist = new ArrayList<HashMap<String,String>>();
                for(int i = 0; i < jq.length(); i++){
                	JSONObject q = jq.getJSONObject(i);
                	qlist.add(quest.MakeQInfo(q));
                }
                qInfo.put("q", qlist);

        	 } catch (JSONException e) {
        		 e.printStackTrace();
        	 }
        }
        return qInfo;
	}
	
	
	private ArrayList<HashMap<String,String>> MakePInfo(JSONObject c) throws JSONException{
        ArrayList<HashMap<String,String>> ret = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> info = new HashMap<String,String>();

        info.put(TAG_JSON_PID, c.getString(TAG_JSON_PID));
        info.put(TAG_JSON_PNAME, c.getString(TAG_JSON_PNAME));
        info.put(TAG_JSON_PCONTENT, c.getString(TAG_JSON_PCONTENT));
        info.put(TAG_JSON_PQCOUNT, c.getString(TAG_JSON_PQCOUNT));
        info.put(TAG_JSON_PCATEGORY, c.getString(TAG_JSON_PCATEGORY));
        info.put(TAG_JSON_PVUP, c.getString(TAG_JSON_PVUP));
        info.put(TAG_JSON_PVDOWN, c.getString(TAG_JSON_PVDOWN));
        info.put(TAG_JSON_PUSED, c.getString(TAG_JSON_PUSED));
        info.put(TAG_JSON_PREGDATE, c.getString(TAG_JSON_PREGDATE));
        info.put(TAG_JSON_PMEMBER, c.getString(TAG_JSON_PMEMBER));
        
        ret.add(info);
        return ret;
	}

	
	@Override
    public RequestParams GetParams() {
		return super.GetParams();
	}
}
