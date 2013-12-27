package com.mbine.qa.controls;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QQuest extends JsonCom {

	private static final String TAG_JSON_QID = "seq";
	private static final String TAG_JSON_QEMAIL = "member";
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
	private static final String TAG_JSON_QMEMBER = "regname";

	private static final String TAG_JSON_AMEMBER = "ans_member";
	private static final String TAG_JSON_ACOR = "ans_cor";

	public HashMap<String,String> MakeQInfo(JSONObject c, String _loguser) throws JSONException{
        HashMap<String,String> info = new HashMap<String,String>();

        info.put(TAG_JSON_QID, c.getString(TAG_JSON_QID));
        info.put(TAG_JSON_QSUMMARY, c.getString(TAG_JSON_QSUMMARY));
        info.put(TAG_JSON_QUSED, c.getString(TAG_JSON_QUSED));
        info.put(TAG_JSON_QCORRECT, c.getString(TAG_JSON_QCORRECT));
        info.put(TAG_JSON_QINCORRECT, c.getString(TAG_JSON_QINCORRECT));
        info.put(TAG_JSON_QPOINT, c.getString(TAG_JSON_QPOINT));
        info.put(TAG_JSON_QCOMMENT, c.getString(TAG_JSON_QCOMMENT));
        info.put(TAG_JSON_QHINT, c.getString(TAG_JSON_QHINT));
        info.put(TAG_JSON_QDIFFICULT, c.getString(TAG_JSON_QDIFFICULT));
        info.put(TAG_JSON_QREGDATE, c.getString(TAG_JSON_QREGDATE));
        info.put(TAG_JSON_QCATEGORY, c.getString(TAG_JSON_QCATEGORY));
        
        JSONArray m = c.getJSONArray(TAG_JSON_QEMAIL);
        
        String mem = null;
        if(m.getJSONObject(0).getString("seq").equals(_loguser)){
        	mem = "OWNER";
            info.put(TAG_JSON_QMEMBER, "나");
        }else{
        	if(c.getString(TAG_JSON_ACOR).equals("Y")){
                mem = "정답";
        	}else{
                mem = "오답";
        	}
            info.put(TAG_JSON_QMEMBER, m.getJSONObject(0).getString("email"));
        }
        info.put(TAG_JSON_ACOR, mem);
        
        return info;
	}
}
