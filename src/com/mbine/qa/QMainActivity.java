package com.mbine.qa;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.R;
import com.mbine.qa.controls.QPackageList;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Tools;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class QMainActivity extends BaseActivity {

	String mUNO = null;
	private static final String TAG_MAININFO = "q/getmaininfo_";
	private static final String TAG_KEYWORD = "keyword";
	private static final String TAG_LISTTYPE = "gotype";
	
	Tools tool = new Tools();
	Button btnMake = null;
	ListView mMenuList = null;
	QPackageList pack = new QPackageList();
	SimpleAdapter mMainMenuAdapter = null;
	ArrayList<HashMap<String, String>> mMainMenuItems = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mUNO = intent.getStringExtra(TAG_UNO);
		
		GetControls();
		CreateEvent();
		GetData();
	}

	@Override
    protected int getLayoutResourceId() {
        return R.layout.activity_qmain;
    }
	
	/**
	 * Control Mapping
	 */
	private void GetControls(){
		btnMake = (Button)findViewById(R.id.btn_make);
		mMenuList = (ListView)findViewById(R.id.main_menu);
	}
	
	private void GetData(){
		tool.ShowLoading(QMainActivity.this);
		RequestParams params = pack.GetParams();
		params.put("kind", "cnt");
		params.put("show", "");
		params.put("pno", "");
        Communication.post(TAG_MAININFO, params, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject json) {
                mMainMenuItems = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> menuToday = new HashMap<String, String>();
                menuToday.put("menukey", "1");
                menuToday.put("menuname", "Today");
                try {
					menuToday.put("menucount", json.getString("today").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
                HashMap<String, String> menuFavor = new HashMap<String, String>();
                menuFavor.put("menukey", "2");
                menuFavor.put("menuname", "Favorites");
                try {
					menuFavor.put("menucount", json.getString("favorite").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
                HashMap<String, String> menuPopular = new HashMap<String, String>();
                menuPopular.put("menukey", "3");
                menuPopular.put("menuname", "Popular");
                try {
					menuPopular.put("menucount", json.getString("popular").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
                HashMap<String, String> menuCategory = new HashMap<String, String>();
                menuCategory.put("menukey", "4");
                menuCategory.put("menuname", "Category");
                try {
					menuCategory.put("menucount", json.getString("category").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
                HashMap<String, String> menuAll = new HashMap<String, String>();
                menuAll.put("menukey", "5");
                menuAll.put("menuname", "All");
                try {
					menuAll.put("menucount", json.getString("all").toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
                
                mMainMenuItems.add(menuToday);
                mMainMenuItems.add(menuFavor);
                mMainMenuItems.add(menuPopular);
                mMainMenuItems.add(menuCategory);
                mMainMenuItems.add(menuAll);

                MenuBinding();

                tool.ExitLoading();
        	}
        });	
	}
	
	private void MenuBinding(){
        mMainMenuAdapter = new SimpleAdapter(QMainActivity.this
        		,mMainMenuItems, R.layout.lv_main_menu
        		,new String[] { "menuname", "menucount" }
        		,new int[] { R.id.title, R.id.counter });

        mMenuList = (ListView)findViewById(R.id.main_menu);
        mMenuList.setAdapter(mMainMenuAdapter);
        mMenuList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                	HashMap<String,String> map = (HashMap<String,String>)arg0.getItemAtPosition(arg2);
                    int num = Integer.parseInt(map.get("menukey"));
                    Intent intent = null;
                	switch(num){
                        case 1: //today
                        	intent = new Intent(QMainActivity.this, QQuizListActivity.class);
                        	intent.putExtra("kind", "today");
                        	break;
                        case 2: //favorite
                        	intent = new Intent(QMainActivity.this, QFavListActivity.class);
                        	intent.putExtra("kind", "favorite");
                        	break;
                        case 3: //popular
                        	intent = new Intent(QMainActivity.this, QQuizListActivity.class);
                        	intent.putExtra("kind", "popular");
                        	break;
                        case 4: //category
                        	intent = new Intent(QMainActivity.this, QCategoryListActivity.class);
                        	intent.putExtra("kind", "category");
                        	break;
                        case 5: //all
                        	intent = new Intent(QMainActivity.this, QQuizListActivity.class);
                        	intent.putExtra("kind", "all");
                        	break;
                        default:
                        	break;
                	}
                	if(intent != null)
                        startActivity(intent);
                }
        });
	}
	
	/**
	 * Create Event Handlers
	 */
	
	private void GoListforMake(){
        Intent intent = new Intent(QMainActivity.this, QPListActivity.class);
        intent.putExtra(TAG_UNO, mUNO);
        intent.putExtra(TAG_KEYWORD, "");
        intent.putExtra(TAG_LISTTYPE, "make");
        startActivity(intent);
	}

	private void CreateEvent(){
		btnMake.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GoListforMake();
			}
		});
	}
}
