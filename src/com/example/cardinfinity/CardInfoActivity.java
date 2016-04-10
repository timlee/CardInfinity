package com.example.cardinfinity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import member.card;
import myDataBase.dbCard;
import myDataBase.myDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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

public class CardInfoActivity extends Activity {

	dbCard cardHelper;
	card card;
	Bitmap myBitMap;
	AlertDialog.Builder dialogMenu;
	String cardID;
	
	ImageView CardInfo_CardImage;
	TextView CardInfo_Name;
	TextView CardInfo_EmailPerson;
	TextView CardInfo_Tel;
	TextView CardInfo_Mobile;
	TextView CardInfo_Fax;
	TextView CardInfo_Address;
	TextView CardInfo_Company;
	TextView CardInfo_Department;
	TextView CardInfo_Job;
	TextView CardInfo_URL;
	
	ImageView CardInfo_TelImage;
	ImageView CardInfo_MobileImage;
	ImageView CardInfo_WebImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_info);
		
		this.initLayout();
		this.initModule();
		
		Bundle extras = getIntent().getExtras();
		cardID = extras.getString("CardID");
		
		System.out.println(cardID);
		
		card = cardHelper.getCardByID(cardID);
		
		initData(card);
		
		
	}
	
	
	private void initData(card card)
	{
		System.out.println(card.getCardImage());
		File image = new File(card.getCardImage());
		if(image.exists()){
			myBitMap = BitmapFactory.decodeFile(image.getAbsolutePath());
			CardInfo_CardImage.setImageBitmap(myBitMap);
		}
		else{
			myBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
			CardInfo_CardImage.setImageBitmap(myBitMap);
		}
		CardInfo_Name.setText(card.getCardName());
		CardInfo_EmailPerson.setText(card.getCardEmail());
		CardInfo_Tel.setText(card.getCardTel());
		CardInfo_Mobile.setText(card.getCardMobile());
		CardInfo_Fax.setText(card.getCardFax());
		CardInfo_Address.setText(card.getCardAddress());
		CardInfo_Company.setText(card.getCardCompany());
		CardInfo_Department.setText(card.getCardDepartment());
		CardInfo_Job.setText(card.getCardJobTitle());
		CardInfo_URL.setText(card.getCardUrl());
		
	}
	private void initLayout()
	{
		CardInfo_CardImage =(ImageView) findViewById(R.id.CardInfo_CardImage);
		CardInfo_Name = (TextView) findViewById(R.id.CardInfo_Name);
		CardInfo_EmailPerson = (TextView) findViewById(R.id.CardInfo_EmailPerson);
		CardInfo_Tel = (TextView) findViewById(R.id.CardInfo_Tel);
		CardInfo_Mobile = (TextView) findViewById(R.id.CardInfo_Mobile);
		CardInfo_Fax = (TextView) findViewById(R.id.CardInfo_Fax);
		CardInfo_Address = (TextView) findViewById(R.id.CardInfo_Address);
		CardInfo_Company = (TextView) findViewById(R.id.CardInfo_Company);
		CardInfo_Department = (TextView) findViewById(R.id.CardInfo_Department);
		CardInfo_Job = (TextView) findViewById(R.id.CardInfo_Job);
		CardInfo_URL = (TextView) findViewById(R.id.CardInfo_URL);
		CardInfo_TelImage =  (ImageView) findViewById(R.id.CardInfo_TelImage);
		CardInfo_MobileImage = (ImageView) findViewById(R.id.CardInfo_MobileImage);
		CardInfo_WebImage = (ImageView) findViewById(R.id.CardInfo_WebImage);
		
		CardInfo_CardImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(new File("file:/"+card.getCardImage()).toString()), "image/*");
				System.out.println(Uri.parse(new File("/"+card.getCardImage()).toString()));
				startActivity(intent);
			}
		});
		
		CardInfo_TelImage.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String number = (String) CardInfo_Tel.getText();
				Intent callIntent = new Intent(Intent.ACTION_DIAL); 
				callIntent.setData(Uri.parse("tel:" + number));
		        startActivity(callIntent);
			}
		});
		
		CardInfo_MobileImage.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String number = (String) CardInfo_Mobile.getText();
				Intent callIntent = new Intent(Intent.ACTION_DIAL); 
				callIntent.setData(Uri.parse("tel:" + number));
		        startActivity(callIntent);
			}
		});
		CardInfo_WebImage.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) 
			{
				String url = (String) CardInfo_URL.getText();
				if (!url.startsWith("http://") && !url.startsWith("https://"))
					   url = "http://" + url;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
			}
		});
	}
	private void refresh()
	{
		card = cardHelper.getCardByID(cardID);
		initData(card);
		
	}
	private void initModule()
	{
		cardHelper = new dbCard(new myDB(this));
	}
	private void showDialog(){
		dialogMenu = new AlertDialog.Builder(CardInfoActivity.this);
		dialogMenu.setIcon(R.drawable.delete);
		dialogMenu.setTitle("是否刪除名片");
		dialogMenu.setPositiveButton("是，請刪除", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				cardHelper.deleteByID(card.getCardID());
				Toast.makeText(getApplication(), "刪除成功!", 10).show();
				CardInfoActivity.this.finish();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card_info, menu);
		getActionBar().setTitle(card.getCardName());
		getActionBar().setLogo(R.drawable.ic_arrow_back_white_24dp);
		getActionBar().setHomeButtonEnabled(true);
		return true;
	}
	@Override
	  public void onResume() {
		super.onResume();
		refresh();  // reload card item
	  }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent = new Intent();
		if(id == android.R.id.home)
		{
			this.finish();
			return true;
		}
		else if(id == R.id.action_delete)
		{
			showDialog();
		}
		else if (id == R.id.action_transfer)
		{
			
			Bundle bundle = new Bundle();
			bundle.putSerializable("card", card);
			intent.setClass(CardInfoActivity.this,NfcTransferActivity.class);
			intent.putExtras(bundle);
	        startActivity(intent);
	  
		}
		else if(id == R.id.action_update)
		{
			Bundle bundle = new Bundle();
			bundle.putString("cardID", cardID);
			intent.putExtras(bundle);
			intent.setClass(CardInfoActivity.this,UpdateCardActivity.class);
			
	        startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
