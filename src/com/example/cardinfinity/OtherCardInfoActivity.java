package com.example.cardinfinity;

import java.io.File;

import member.card;
import member.othercard;
import myDataBase.dbCard;
import myDataBase.myDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OtherCardInfoActivity extends Activity {

	ImageView OtherCardInfo_CardImage;
	TextView OtherCardInfo_Name;
	TextView OtherCardInfo_Description;
	
	dbCard cardHelper;
	othercard card;
	Bitmap myBitMap;
	AlertDialog.Builder dialogMenu;
	String cardID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_card);
		
		this.initLayout();
		this.initModule();
		
		Bundle extras = getIntent().getExtras();
		cardID = extras.getString("CardID");
		
		card = cardHelper.getOtherCardByID(cardID);
		
		initData(card);
	}
	
	private void refresh()
	{
		card = cardHelper.getOtherCardByID(cardID);
		initData(card);
		
	}
	
	private void initData(othercard card)
	{
		File image = new File(card.getOtherCardImage());
		if(image.exists()){
			myBitMap = BitmapFactory.decodeFile(image.getAbsolutePath());
			OtherCardInfo_CardImage.setImageBitmap(myBitMap);
		}
		else{
			myBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
			OtherCardInfo_CardImage.setImageBitmap(myBitMap);
		}
		OtherCardInfo_Name.setText(card.getOtherCardName());
		OtherCardInfo_Description.setText(card.getOtherCardDescription());
		
	}
	
	private void initLayout(){
		OtherCardInfo_CardImage = (ImageView)findViewById(R.id.OtherCardInfo_CardImage);
		OtherCardInfo_Name = (TextView)findViewById(R.id.OtherCardInfo_Name);
		OtherCardInfo_Description = (TextView)findViewById(R.id.OtherCardInfo_Description);
		
		
		OtherCardInfo_CardImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(new File("file:/"+card.getOtherCardImage()).toString()), "image/*");
				System.out.println(Uri.parse(new File("/"+card.getOtherCardImage()).toString()));
				startActivity(intent);
			}
		});
	}
	private void initModule()
	{
		cardHelper = new dbCard(new myDB(this));
	}
	private void showDialog(){
		dialogMenu = new AlertDialog.Builder(OtherCardInfoActivity.this);
		dialogMenu.setIcon(R.drawable.delete);
		dialogMenu.setTitle("是否刪除名片");
		dialogMenu.setPositiveButton("是，請刪除", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				cardHelper.deleteOtherByID(card.getOtherCardID());
				Toast.makeText(getApplication(), "刪除成功!", 10).show();
				OtherCardInfoActivity.this.finish();
			}
			
		});
		dialogMenu.setNegativeButton("否", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
			
		});
		
		dialogMenu.show();
	}
	@Override
	  public void onResume() {
		super.onResume();
		refresh();  // reload card item
	  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.other_card, menu);
		getActionBar().setTitle("卡片資料");
		getActionBar().setLogo(R.drawable.ic_arrow_back_white_24dp);
		getActionBar().setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) 
		{
			this.finish();
			return true;
		}
		else if(id == R.id.other_action_delete)
		{
			showDialog();
		}
		
		else if(id == R.id.other_action_update)
		{
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("cardID", cardID);
			intent.putExtras(bundle);
			intent.setClass(OtherCardInfoActivity.this,UpdateOtherCardActivity.class);
			
	        startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
