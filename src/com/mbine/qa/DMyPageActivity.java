package com.mbine.qa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
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

	String photourl  = null;
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
        GetUserInfo();
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
                  photourl = data.getStringExtra("photourl"); 
                  if(photourl != null)
                	  upload();
	          }
	      }
	}
	
	private void upload(){
          tool.ShowLoading(DMyPageActivity.this);
          new UploadTask().execute();
	}

	private class UploadTask extends AsyncTask<Void, Void, String>{
		 
		@Override
		protected void onPostExecute(String result) {
		  result = tool.upResult;
          if(result.equals("")){
        	  tool.showPop(DMyPageActivity.this, "업로드 과정에 문제가 발생했습니다.", "OK");
          }else{
        	  mUserAvatar.setImageBitmap(tool.getRoundedShape(tool.getBitmap(photourl)));
          }
          tool.ExitLoading();
		}

		@Override
		protected String doInBackground(Void... params) {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        nameValuePairs.add(new BasicNameValuePair("mno", mUserNo));
	        nameValuePairs.add(new BasicNameValuePair("file", photourl));
			String r = tool.post(BASE_URL + TAG_IMAGE_UPLOADAVATARURL + "/avatar", nameValuePairs);
			try {
				JSONObject json = new JSONObject(r);
				String newFileName = json.getString("new");
                File p = new File(photourl);
                storage.copyFile(p, storage.mediaStorageDir+"/"+newFileName);
				storage.put("avatar", newFileName);
				ImageView mImgAvatar = (ImageView) findViewById(R.id.avatar);
				mImgAvatar.setImageBitmap(tool.getRoundedShape(tool.getBitmap(p.getAbsolutePath())));
			} catch (JSONException e) {
				e.printStackTrace();
			} 
			return null;
		}
	}
	
	private void GetUserInfo(){
		SetUserAvatar();
	}
	
	private void SetUserAvatar(){
        String nowavatar = storage.pull("avatar", "");
        if(!nowavatar.equals(mMemberInfo.get(TAG_JSON_MAVATAR)) && !mMemberInfo.get(TAG_JSON_MAVATAR).equals("null")){
        	tool.DownloadFromUrl(BASE_URL + TAG_AVATARURL + mMemberInfo.get(TAG_JSON_MAVATAR)
        			, new File(storage.mediaStorageDir.getPath() + mMemberInfo.get(TAG_JSON_MAVATAR)));
        }
        boolean isAvatar = false;
        if(nowavatar != null && !nowavatar.equals("")){
        	File f = new File(storage.mediaStorageDir.getPath() + "/" + nowavatar);
        	if(f.isFile()){
                isAvatar = true;
                mUserAvatar.setImageBitmap(tool.getRoundedShape(tool.getBitmap(f.getAbsolutePath())));
        	}
        }
        if(!isAvatar)
        	mUserAvatar.setImageBitmap(tool.getRoundedShape(DefaultAvatar));
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
