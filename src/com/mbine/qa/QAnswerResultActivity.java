package com.mbine.qa;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.tool.Communication;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class QAnswerResultActivity extends Activity {

	private static final String TAG_UNO = "uno";
	private static final String TAG_PNO = "pno";
	private static final String TAG_QNO = "qno";

	String mUNO = null;
	String mQNO = null;
	String mPNO = null;
	String mAResult = null;
	String isCor = null;
	LinearLayout mResultBg = null;
	TextView txtResult  = null;
	Button btnNext = null, btnInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qanswer_result);

		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		mPNO = intent.getStringExtra(TAG_PNO);
		mQNO = intent.getStringExtra(TAG_QNO);
		isCor = intent.getStringExtra("AResult");

		mResultBg = (LinearLayout)this.findViewById(R.id.answerbg);
		btnNext = (Button)findViewById(R.id.btnNext);
		btnInfo = (Button)findViewById(R.id.btnShowInfo);
		txtResult = (TextView)findViewById(R.id.result);

		btnInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent intent = new Intent(QAnswerResultActivity.this, QQMainActivity.class);
                intent.putExtra(TAG_PNO, mPNO);
                intent.putExtra(TAG_UNO, mUNO);
                intent.putExtra(TAG_QNO, mQNO);
                startActivity(intent);
                finish();
			}
		});

		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent intent = new Intent(QAnswerResultActivity.this, QAnswerActivity.class);
                intent.putExtra(TAG_PNO, mPNO);
                intent.putExtra(TAG_UNO, mUNO);
                intent.putExtra(TAG_QNO, mQNO);
                startActivity(intent);
                finish();
			}
		});
		ShowSet();
	}
	
	@SuppressLint("ResourceAsColor")
	private void ShowSet(){
		if(isCor.equals("Y")){
			txtResult.setText("정답입니다.");
			mResultBg.setBackgroundColor(R.color.an_co);
		}else{
			txtResult.setText("틀렸습니다.");
			mResultBg.setBackgroundColor(R.color.an_inco);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qanswer_result, menu);
		return true;
	}

}
