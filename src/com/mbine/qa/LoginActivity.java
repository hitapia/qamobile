package com.mbine.qa;

import org.json.JSONException;
import org.json.JSONObject;

import com.mbine.qa.controls.*;
import com.mbine.qa.tool.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
    JSONObject result;
    Tools tool = new Tools();

    boolean blLogin = false;
    EditText txtEmail;
    EditText txtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        txtEmail = (EditText)findViewById(R.id.login_txtemail);
        txtPassword = (EditText)findViewById(R.id.login_txtpassword);

        txtEmail.setText("hitapia@gmail.com");
        txtPassword.setText("marduke");

        EventList();
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
