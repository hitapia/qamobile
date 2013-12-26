package com.mbine.qa;

import org.json.JSONException;

import com.mbine.qa.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Bang
 *
 */
/**
 * @author Bang
 *
 */
public class QMainActivity extends Activity {

	String mUNO = null;
	private static final String TAG_UNO = "uno";
	private static final String TAG_KEYWORD = "keyword";
	
	Button btnPList = null;
	Button btnQList = null;
	Button btnAList = null;
	
	Button btnMyReg = null;
	Button btnMyProg = null;
	Button btnMyAvg = null;
	Button btnSearch = null;
	EditText txtSearch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qmain);
		
		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		
		GetControls();
		CreateEvent();
	}
	
	/**
	 * Control Mapping
	 */
	private void GetControls(){
		btnPList = (Button)findViewById(R.id.btn_pmain_cntp);
		btnQList = (Button)findViewById(R.id.btn_pmain_cntq);
		btnAList = (Button)findViewById(R.id.btn_pmain_cnta);

		btnMyAvg = (Button)findViewById(R.id.btn_qmain_avga);
		btnMyProg = (Button)findViewById(R.id.btn_qmain_prog);
		btnMyReg = (Button)findViewById(R.id.btn_qmain_regq);
		btnSearch = (Button)findViewById(R.id.btn_search);
		
		txtSearch = (EditText)findViewById(R.id.txtsearch);
	}
	
	/**
	 * Create Event Handlers
	 */
	private void CreateEvent(){
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!txtSearch.getText().toString().equals("")){
                        Intent intent = new Intent(QMainActivity.this, QPListActivity.class);
                        intent.putExtra(TAG_UNO, mUNO);
                        intent.putExtra(TAG_KEYWORD, txtSearch.getText().toString());
                        txtSearch.setText("");
                        startActivity(intent);
				}
			}
		});

		btnPList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QMainActivity.this, QPListActivity.class);
				intent.putExtra(TAG_UNO, mUNO);
                intent.putExtra(TAG_KEYWORD, "");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            //openSearch();
	            return true;
	        case R.id.action_settings:
	            //openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
