package com.mbine.qa;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.controls.QMember;
import com.mbine.qa.controls.QPackageList;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Storage;
import com.mbine.qa.tool.Tools;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class DMyPageActivity extends BaseActivity {

	private static final String TAG_JSON_MID = "seq";
	private static final String TAG_JSON_MNAME = "name";
	private static final String TAG_JSON_MEMAIL = "email";
	private static final String TAG_JSON_MPOINT = "point";
	private static final String TAG_JSON_MSNSTYPE = "snstype";
	private static final String TAG_JSON_MAVATAR = "avatar";
	private static final String TAG_JSON_MDEVICECODE = "devicecode";
	private static final String TAG_JSON_MREGDATE = "regdate";
	
	private static final String TEMP_PHOTO_FILE = "temp.jpg";
	private static final String TAG_MAININFO = "member/getuserinfo_";
	private static final String TAG_SEL_UNO = "checkuserno";

	Tools tool = new Tools();
	Storage storage = null;
	QPackageList pack = new QPackageList();
    HashMap<String, String> mMemberInfo = null;
    
    TextView mUName = null, mUPoint = null;
    ImageButton mImgChange = null;
    ImageView mUserAvatar = null;
    String mMyNo = null;
    String mUserNo = null;
    
    private static final int PICK_IMAGE = 1;
	private static final int TAG_PHOTO_REQ = 99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storage = new Storage(this);
        mMyNo = storage.pull(TAG_UNO, "");
        
		Intent intent = this.getIntent();
		if(intent.hasExtra(TAG_SEL_UNO)){
			mUserNo = intent.getStringExtra(TAG_SEL_UNO);
		}else{
			mUserNo = mMyNo;
		}

        GetControls();
        GetData();
	}
	
	private void GetData(){
		tool.ShowLoading(DMyPageActivity.this);
		RequestParams params = pack.GetParams();
        Communication.post(TAG_MAININFO + "/" + mUserNo, params, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
        		mMemberInfo = new HashMap<String, String>();
        		QMember member = new QMember();
        		try {
					mMemberInfo = member.MakeOneMember(json.getJSONArray("u").getJSONObject(0));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                tool.ExitLoading();
        	}
        });	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAG_PHOTO_REQ) {
	          if (resultCode == RESULT_OK) {
	            String photourl = data.getStringExtra("photourl"); 
	            tool.showPop(DMyPageActivity.this, photourl, "ok");
	          }
	      }
	}

	private void GetControls(){
		mUName = (TextView)findViewById(R.id.u_username);
		mUPoint = (TextView)findViewById(R.id.u_userpoint);
		mUserAvatar = (ImageView)findViewById(R.id.u_avatar);
		mImgChange = (ImageButton)findViewById(R.id.u_avatarup);
		if(!mMyNo.equals(mUserNo)){
			mImgChange.setVisibility(View.GONE);
		}else{
                mImgChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
    						Intent photo = new Intent(DMyPageActivity.this, ImageCropActivity.class);
                        	startActivityForResult(photo, TAG_PHOTO_REQ);
                        }
                });
		}
	}

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_dmy_page;
	}
}
