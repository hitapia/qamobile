package com.mbine.qa.controls;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

public class QPackageList extends JsonCom {

	private static final String TAG_JSON_QS = "c";
	private static final String TAG_JSON_QID = "seq";
	private static final String TAG_JSON_QMEMBER = "member";
	private static final String TAG_JSON_QCOUNT = "questcount";
	private static final String TAG_JSON_QUSED = "used";
	private static final String TAG_JSON_QREGDATE = "regdate";
	private static final String TAG_JSON_QCATEGORY = "category";
	private static final String TAG_JSON_QVUP = "vup";
	private static final String TAG_JSON_QVDOWN = "vdown";
	private static final String TAG_JSON_QCONTENT = "content";
	private static final String TAG_JSON_QNAME = "name";
	
	JSONObject json = null;
	
	@Override
    public RequestParams GetParams() {
		return super.GetParams();
	}
	
	public ArrayList<HashMap<String,String>> MakeList(JSONObject json){
        ArrayList<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();
        String result = null;
		try {
			result = json.getString("result");
		} catch (JSONException e1) {
			return null;
		}
        if(result.equals(TAG_RESULTOKCODE)){
                try {
                        JSONArray qs = json.getJSONArray(TAG_JSON_QS);
                        for(int i = 0; i < qs.length(); i++){
                                JSONObject c = qs.getJSONObject(i);

                                String id = c.getString(TAG_JSON_QID);
                                String content = c.getString(TAG_JSON_QCONTENT);
                                String name = c.getString(TAG_JSON_QNAME);
                                String count = c.getString(TAG_JSON_QCOUNT);
                                String categoryseq = c.getString(TAG_JSON_QCATEGORY);

                                /*
                                String vup = c.getString(TAG_JSON_QVUP);
                                String vdown = c.getString(TAG_JSON_QVDOWN);
                                String used = c.getString(TAG_JSON_QUSED);
                                String regdate = c.getString(TAG_JSON_QREGDATE);
                                String memberseq = c.getString(TAG_JSON_QMEMBER);
                                */

                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put(TAG_JSON_QID, id);
                                map.put(TAG_JSON_QNAME, name);
                                map.put(TAG_JSON_QCONTENT, content);
                                map.put(TAG_JSON_QCOUNT, count);
                                map.put(TAG_JSON_QCATEGORY, categoryseq);

                                qList.add(map);
                        }
                } catch (JSONException e) {
                        e.printStackTrace();
                }
        }else{
        }
        return qList;
	}
}
