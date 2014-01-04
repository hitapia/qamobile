package com.mbine.qa;

import org.json.JSONException;
import org.json.JSONObject;

import com.mbine.qa.controls.*;
import com.mbine.qa.tool.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private static final String TAG_UNO = "uno";
    JSONObject result;
    Tools tool = new Tools();

    boolean blLogin = false;
    EditText txtEmail;
    EditText txtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CheckLogged();

		setContentView(R.layout.activity_login);

        txtEmail = (EditText)findViewById(R.id.login_txtemail);
        txtPassword = (EditText)findViewById(R.id.login_txtpassword);

        txtEmail.setText("hitapia@gmail.com");
        txtPassword.setText("marduke");
        
        SetActionBar();

        EventList();
	}
	
	private void CheckLogged(){
		Storage str = new Storage(this);
		String mUNO = str.pull(TAG_UNO, "");
		if(mUNO != null && !mUNO.equals("")){
			Intent portal = new Intent(LoginActivity.this, QMainActivity.class);
			startActivity(portal);
			finish();
		}
	}
	
	private void SetActionBar(){
        // 액션바를 가져옴
        ActionBar bar = getActionBar();

        // 뷰를 가져옴..
        View v = getLayoutInflater().inflate(R.layout.actionbar_title, null);

        // 액션바에 커스텀뷰를 설정
        bar.setCustomView(v, new ActionBar.LayoutParams(
        ActionBar.LayoutParams.MATCH_PARENT,
        ActionBar.LayoutParams.MATCH_PARENT));

        //커스텀뷰를 써야하므로 옵션에서 설정
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

    private void EventList(){
    	findViewById(R.id.login_btnLogin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Validate()){
					tool.showPop(LoginActivity.this, "Check Input Fields.", "OK");
				}else{
					try {
						tool.ShowLoading(LoginActivity.this);
						Login(txtEmail.getText().toString(), txtPassword.getText().toString());
					} catch (JSONException e) {
						e.printStackTrace();
						tool.showPop(LoginActivity.this, "Server Error!", "OK");
					}
				}
			}
		});
    }

    public void Login(String email, String password) throws JSONException {
    	RequestParams params = new RequestParams();
    	params.put("email", email);
    	params.put("pass", password);

        Communication.post("member/login_", params, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject data) {
                try {
					String result = data.getString("result");
					if(result.equals("1")){
						Storage str = new Storage(LoginActivity.this);
						str.put("uno", data.getString("userseq"));
						str.put("uemail", data.getString("useremail"));
						tool.ExitLoading();
						Intent portal = new Intent(LoginActivity.this, QMainActivity.class);
						startActivity(portal);
						finish();
					}else{
						tool.showPop(LoginActivity.this, "Login Fail!", "OK");
					}
				} catch (JSONException e) { }
                tool.ExitLoading();
        	}
        });
    }
    
    private boolean Validate(){
    	if(txtEmail.getText().toString().equals("")) { return false; }
    	if(txtPassword.getText().toString().equals("")) { return false; }

    	return true;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
