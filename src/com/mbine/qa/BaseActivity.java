/**
 * 
 */
package com.mbine.qa;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mbine.qa.adapter.NavDrawerItem;
import com.mbine.qa.adapter.NavDrawerListAdapter;
import com.mbine.qa.controls.QPackage;
import com.mbine.qa.tool.Communication;
import com.mbine.qa.tool.Storage;
import com.mbine.qa.tool.Tools;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author Bang
 *
 */
public abstract class BaseActivity extends Activity {

	protected static final String BASE_URL = "http://www.quizc.com/";
	protected static final String TAG_DB_SEQ = "seq";
	protected static final String TAG_DB_SUMMARY = "summary";
	protected static final String TAG_DB_MEMBER = "member";
	protected static final String TAG_DB_COUNT = "cnt";
	protected static final String TAG_DB_USED = "USED";
	protected static final String TAG_DB_CORRECT = "correct";
	protected static final String TAG_DB_INCORRECT = "incorrect";
	protected static final String TAG_DB_ISCORRECT = "iscorrect";
	protected static final String TAG_DB_REGDATE = "regdate";
	protected static final String TAG_DB_NAME = "name";
	protected static final String TAG_DB_CATEGORYNO = "cno";
	protected static final String TAG_DB_CATEGORYNAME = "cname";
	protected static final String TAG_DB_PNO = "pno";
	protected static final String TAG_DB_PNAME = "pname";
	protected static final String TAG_QNO = "qno";
	protected static final String TAG_PNO = "pno";
	protected static final String TAG_UNO = "uno";
	protected static final String TAG_AVATARURL = "data/avatar/";
	protected static final String TAG_IMAGE_UPLOADAVATARURL = "scripts/upload_avatar.php";
	protected static final String TAG_USERINFO = "member/getuserinfo_";

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter draw_adapter;
	DrawerLayout mDrawerLayout = null;
	ListView DrawList = null;
    ActionBarDrawerToggle mDrawerToggle;
    LinearLayout Drawer = null, mUInfo;
    ImageView mImg = null;
    TextView mUserName = null, mUserPoint = null;
    protected Bitmap DefaultAvatar;
    
    protected String mPNO = null;
    protected String mQNO = null;
    protected String mUNO = null;

    Tools tool = new Tools();
    Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this.getApplicationContext();
		super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        Drawer = (LinearLayout)findViewById(R.id.list_slidermenu);
		
        //Default Avatar Setting
		BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.default_avatar);
		DefaultAvatar = drawable.getBitmap();

		SetActionBar();

	    DrawList.setOnItemClickListener(new SlideMenuClickListener());
	}
	
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
            displayView(arg2);
			
		}
	}
	
	private void displayView(int position) {
        // update the main content by replacing fragments
		Class cls = null;
        switch (position) {
        case 0:
        	cls = QMainActivity.class;
            break;
        case 1:
        	cls = DFriendsActivity.class;
            break;
        case 2:
        	cls = DFavActivity.class;
            break;
        case 3:
        	cls = DSettingActivity.class;
            break;
        default:
            break;
        }
        Intent move = new Intent(mContext, cls);
        startActivity(move);
        //finish();
    }

	protected abstract int getLayoutResourceId();
	
	protected void SetAvatar(){
		mUserName = (TextView) findViewById(R.id.username);
		mUserPoint = (TextView) findViewById(R.id.userpoint);
		mImg = (ImageView) findViewById(R.id.avatar);
		Storage str = new Storage(mContext);
		String avatar = str.pull("avatar", "");
		boolean fcheck = false;
		if(avatar != null && !avatar.equals("")){
			File f = new File(str.mediaStorageDir.getPath() + "/" + avatar);
			if(f.isFile()){
                mImg.setImageBitmap(tool.getRoundedShape(tool.getBitmap(f.getAbsolutePath())));
                fcheck = true;
			}
		}
		if(!fcheck){
			GetUserAvatar();
		}
		mUInfo = (LinearLayout) findViewById(R.id.userinfo);
		
		mUInfo.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
                Intent move = new Intent(mContext, DMyPageActivity.class);
                startActivity(move);
		    }
		});
	}
	
	private void GetUserAvatar(){
        mImg.setImageBitmap(tool.getRoundedShape(DefaultAvatar));
	}

	protected void SetActionBar(){
        
		SetAvatar();
        navDrawerItems = new ArrayList<NavDrawerItem>();
        
        navDrawerItems.add(new NavDrawerItem(getResources().getString(R.string.menu_home), R.drawable.drawer_nav_icon01));
        navDrawerItems.add(new NavDrawerItem(getResources().getString(R.string.menu_friend), R.drawable.drawer_nav_icon02));
        navDrawerItems.add(new NavDrawerItem(getResources().getString(R.string.menu_favorite), R.drawable.drawer_nav_icon03));
        navDrawerItems.add(new NavDrawerItem(getResources().getString(R.string.menu_setting), R.drawable.drawer_nav_icon04));
        draw_adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);

        ImageView img = (ImageView)findViewById(R.id.img_point);
        img.setImageResource(R.drawable.drawer_top_point);

        DrawList = (ListView)findViewById(R.id.leftmenu);
        DrawList.setAdapter(draw_adapter);
       
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
	}

	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(Drawer);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            //openSearch();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
