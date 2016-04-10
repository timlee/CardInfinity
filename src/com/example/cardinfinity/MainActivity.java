package com.example.cardinfinity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import myDataBase.dbCard;
import myDataBase.myDB;
import fragment.bussinessCard;
import fragment.identityCard;
import fragment.otherCard;
import fragment.preferentialCard;
import sharedPref.sessionManager;
import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Choreographer.FrameCallback;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity{
	
	//action bar
	private ActionBar bar;
	
	//layout
	LinearLayout mainContent;
	ScrollView mainScrollView;
	RelativeLayout drawerContainer;
	
	//drawer
	ListView lstDrawer;
	DrawerLayout layDrawer;
	ActionBarDrawerToggle mDrawerToggle;
	int[] drawer_item_icon = {R.drawable.businesscard , R.drawable.identitycard ,R.drawable.preferentialcard ,R.drawable.othercard };
	String[] drawer_item_title = {"名片夾","身分夾","優惠夾","其他類"};
	
	//dialog
	AlertDialog.Builder dialogMenu;
	String[] dialogMenuItemList = {"手動輸入名片","手動輸入卡片"};
	AlertDialog.Builder transferMenu;
	String[] transferDialogMenuItemList = {"接收名片"};
	
	//sessinManager
	sessionManager sessionHelper;
	
	//card
	dbCard cardHelper;
	
	//tab
	String[] tabTitle = {"  手動新增","   拍照新增","  交換名片"};
	int[] tabIcon = {R.drawable.plus,R.drawable.camera,R.drawable.change};
	
	SharedPreferences pref;
	
	private boolean isFirstToActivity =true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.initModule();
		this.initLayout();
		this.initDrawer();
		this.initDrawerItem();
		this.createFile();
		
		addTab(); //新增tab內容
		if(cardHelper.getCardCount()!=0)
		{
			setFragment(new bussinessCard());
		}
		else{
			Toast.makeText(this, "無卡片資料", 15).show();
		}
	}

	// 新增tab
	private void addTab()
	{
		for(int i=0;i<tabTitle.length;i++)
		{
			bar.addTab(bar.newTab().setText(tabTitle[i])
								   .setTag(i)
					               .setIcon(tabIcon[i])
					               .setTabListener(new tabListener()));
		}
		
	}
	
	private void setFragment(Fragment fragment)
	{
		//mainContent.removeAllViews(); // refresh maincontent
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		fragmentTransaction
		.replace(R.id.Main_Content, fragment)
		.addToBackStack(null)
		.commit();
	}
	private void initLayout()
	{
		getActionBar().setTitle("名片夾");
		
		mainContent = (LinearLayout) findViewById(R.id.Main_Content);
		layDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		lstDrawer = (ListView) findViewById(R.id.left_drawer);
		drawerContainer = (RelativeLayout) findViewById(R.id.Main_DrawerContainer);
		mainScrollView = (ScrollView) findViewById(R.id.Main_ScrollView);
	}
	//初始化模組
	private void initModule()
	{
		sessionHelper = new sessionManager(getApplicationContext());
		cardHelper = new dbCard(new myDB(getApplicationContext()));
	}
	//初始化drawer
	private void initDrawer()
	{
		//setContentView(R.layout.main_drawer);
		bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, 
                layDrawer,
                R.drawable.ic_list_white_36dp, 
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
            	mainScrollView.bringToFront();
                mainScrollView.requestLayout();
            	super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            	super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
            	super.onDrawerSlide(drawerView, slideOffset);
            	drawerContainer.bringToFront();
            	drawerContainer.requestLayout();
            }
        };

        layDrawer.setDrawerListener(mDrawerToggle);
	}
	//新增drawer item
	private void initDrawerItem()
	{
		 List<HashMap<String,String>> lstData = new ArrayList<HashMap<String,String>>();
		  
		 for (int i = 0; i < drawer_item_title.length; i++) 
		 {
		   HashMap<String, String> mapValue = new HashMap<String, String>();
		   mapValue.put("icon", Integer.toString(drawer_item_icon[i]));
		   mapValue.put("title", drawer_item_title[i]);
		   lstData.add(mapValue);
		 }
		 
		  SimpleAdapter adapter = new SimpleAdapter
				  (		
						this, 
						lstData, // use List<HashMap<String , String>>
						R.layout.drawer_list_item, 
						new String[]{"icon", "title"}, // tag name
						new int[]{R.id.drawer_item_icon, R.id.drawer_item_title} // tag list
				  );
	    lstDrawer.setAdapter(adapter);
	    lstDrawer.setOnItemClickListener(new drawerItemClickListener());
	}
	private void createFile()
	{
		File checkCardInfinite = new File(Environment.getExternalStorageDirectory() + "/CardInfinite"); 
		File checkCardInfiniteImage = new File(Environment.getExternalStorageDirectory() + "/CardInfinite/cardImage");
		File checkCardInfinitePreferential = new File(Environment.getExternalStorageDirectory() + "/CardInfinite/Preferential");
		File checkCardInfiniteIdentification = new File(Environment.getExternalStorageDirectory() + "/CardInfinite/Identification");
		File checkCardInfiniteOther = new File(Environment.getExternalStorageDirectory() + "/CardInfinite/Other");
		//check 是否有這資料夾，若無則創造
		if(!checkCardInfinite.isDirectory())
		{
			// create a File object for the parent directory
			File main = new File("/sdcard/CardInfinite");
			main.mkdirs();
			if(!checkCardInfiniteImage.isDirectory())
			{
				File wallpaperDirectory = new File("/sdcard/CardInfinite/cardImage");
				// have the object build the directory structure, if needed.
				wallpaperDirectory.mkdirs();
			}
			if(!checkCardInfiniteIdentification.isDirectory())
			{
				File wallpaperDirectory = new File("/sdcard/CardInfinite/otherCardImage");
				// have the object build the directory structure, if needed.
				wallpaperDirectory.mkdirs();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//getActionBar().setTitle("名片夾");
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//set tab background color
		ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
		getActionBar().setStackedBackgroundDrawable(colorDrawable);
		return true;
	}
	 @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        mDrawerToggle.syncState();
	    }
	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	        }
		
		
		switch(item.getItemId())
		{
			case R.id.logOut:
				 sessionHelper.logOut();
				 Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		         startActivity(intent);
		         finish();
		}
		
		
		return super.onOptionsItemSelected(item);
	}
	// tab listener
	private class tabListener implements TabListener
	{
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) 
		{
			if(isFirstToActivity)
			{
				isFirstToActivity = false;
			}
			else
			{
				int position = (int) tab.getTag();
				Intent intent;
				switch(position)
				{
				case 0: //
					showDialog();
					break;
				case 1:
					 intent = new Intent(MainActivity.this,edu.sfsu.cs.orange.ocr.CaptureActivity.class);
			         startActivity(intent);
			         break;
				case 2:
					showTransferDialog();
					break;
				}
			}
		}
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) 
		{
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) 
		{
			int position = (int) tab.getTag();
			Intent intent;
			switch(position)
			{
				case 0:
					showDialog();
					break;
				case 1:
					 intent = new Intent(MainActivity.this,edu.sfsu.cs.orange.ocr.CaptureActivity.class);
			         startActivity(intent);
					break;
				case 2:
					showTransferDialog();
					break;
			}
		}
		
	}
	//
	private void showDialog(){
		dialogMenu = new AlertDialog.Builder(MainActivity.this);
		dialogMenu.setIcon(R.drawable.plus);
		dialogMenu.setTitle("添加名片");
		dialogMenu.setItems(dialogMenuItemList, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent;	
				switch(which)
					{
						case 0:
							intent = new Intent();
							intent.setClass(MainActivity.this,AddBusinessCardActivity.class);
							startActivity(intent);
							break;
						case 1:
							intent = new Intent();
							intent.setClass(MainActivity.this,AddOtherCardActivity.class);
							startActivity(intent);
							break;
					}
			}
		});
		dialogMenu.show();
	}
	
	private void showTransferDialog(){
		transferMenu = new AlertDialog.Builder(MainActivity.this);
		transferMenu.setIcon(R.drawable.plus);
		transferMenu.setTitle("名片傳輸");
		transferMenu.setItems(transferDialogMenuItemList, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent;		
					switch(which)
					{
						case 0: //receive
							intent = new Intent();
							intent.setClass(MainActivity.this,NfcReceiveActivity.class);
							startActivity(intent);
							break;
					}
			}
		});
		transferMenu.show();
	}
	
	
	// drawer list click listener
	private class drawerItemClickListener implements ListView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
		
	}
	private void selectItem(int position){
		
		switch(position)
		{
			case 0:
				setFragment(new bussinessCard());
				getActionBar().setTitle("名片夾");
				break;
			case 1:
				setFragment(new identityCard());
				getActionBar().setTitle("身分夾");
				break;
			case 2:
				setFragment(new preferentialCard());
				getActionBar().setTitle("優惠夾");
				break;
			case 3:
				setFragment(new otherCard());
				getActionBar().setTitle("其他類");
				break;
		}
		
		
		// Highlight the selected item, update the title, and close the drawer
		lstDrawer.setItemChecked(position, true);
	    setTitle(drawer_item_title[position]);
	    layDrawer.closeDrawer(lstDrawer);
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    System.gc();
	}
	
}
