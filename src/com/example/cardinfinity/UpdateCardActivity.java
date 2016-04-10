package com.example.cardinfinity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import util.DateUtil;
import util.inputValidation;
import edu.sfsu.cs.orange.ocr.CaptureActivity;
import myDataBase.dbCard;
import myDataBase.myDB;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateCardActivity extends Activity {

	final static String storagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/cardImage/";	;
	
	dbCard cardHelper;
	DateUtil date;
	inputValidation myValid;
	
	String cardID;
	member.card card;
	Bitmap myBitMap;
	
	ImageView UpdateCard_Photo;
	EditText UpdateCard_NameText , UpdateCard_MobileText,UpdateCard_FaxText , UpdateCard_WorkPhoneText ,
			 UpdateCard_EmailPersonText , UpdateCard_AddressText , UpdateCard_UrlText , 
			 UpdateCard_OrganizeNameText , UpdateCard_OrganizeDepartmentText , UpdateCard_OrganizeJobTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_card);
		this.initModule();
		this.initLayout();
		this.inputValid();
		
		Bundle bundle = getIntent().getExtras();
		cardID = bundle.getString("cardID");
		card = cardHelper.getCardByID(cardID);
		if(card!=null)
		{
			giveCardValue(card);
			
		}

	}
	private void initLayout()
	{
		UpdateCard_NameText = (EditText) findViewById(R.id.UpdateCard_NameText);
		UpdateCard_MobileText =(EditText) findViewById(R.id.UpdateCard_MobileText);
		UpdateCard_FaxText = (EditText) findViewById(R.id.UpdateCard_FaxText);
		UpdateCard_WorkPhoneText =(EditText) findViewById(R.id.UpdateCard_WorkPhoneText);
		UpdateCard_EmailPersonText =(EditText) findViewById(R.id.UpdateCard_EmailPersonText);
		UpdateCard_AddressText =(EditText) findViewById(R.id.UpdateCard_AddressText);
		UpdateCard_UrlText =(EditText)findViewById(R.id.UpdateCard_UrlText);
		UpdateCard_OrganizeNameText =(EditText) findViewById(R.id.UpdateCard_OrganizeNameText);
		UpdateCard_OrganizeDepartmentText =(EditText) findViewById(R.id.UpdateCard_OrganizeDepartmentText); 
		UpdateCard_OrganizeJobTitleText =(EditText) findViewById(R.id.UpdateCard_OrganizeJobTitleText);
		UpdateCard_Photo = (ImageView) findViewById(R.id.UpdateCard_Photo);
		
		
		UpdateCard_Photo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent,100);		
			}
		});
	}
	private void giveCardValue(member.card card)
	{
		UpdateCard_NameText.setText(card.getCardName());
		UpdateCard_MobileText.setText(card.getCardMobile());
		UpdateCard_WorkPhoneText.setText(card.getCardTel());
		UpdateCard_EmailPersonText.setText(card.getCardEmail());
		UpdateCard_AddressText.setText(card.getCardAddress());
		UpdateCard_FaxText.setText(card.getCardFax());
		UpdateCard_UrlText.setText(card.getCardUrl());
		UpdateCard_OrganizeNameText.setText(card.getCardCompany());
		UpdateCard_OrganizeDepartmentText.setText(card.getCardDepartment());
		UpdateCard_OrganizeJobTitleText.setText(card.getCardJobTitle());
	
		File image = new File(card.getCardImage());
		if(image.exists()){
			myBitMap = BitmapFactory.decodeFile(image.getAbsolutePath());
			UpdateCard_Photo.setImageBitmap(myBitMap);
		}
		else{
			myBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
			UpdateCard_Photo.setImageBitmap(myBitMap);
		}
		
	}
	private void inputValid()
	{
		UpdateCard_NameText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_NameTextTXT = UpdateCard_NameText.getText().toString();
				if(UpdateCard_NameTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_NameTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_NameText.setText("");
					}
				}
				
			}
		});
		UpdateCard_MobileText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_MobileTextTXT = UpdateCard_MobileText.getText().toString();
				if(UpdateCard_MobileTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_MobileTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_MobileText.setText("");
					}
				}
				
			}
		});
		UpdateCard_UrlText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_UrlTextTXT = UpdateCard_UrlText.getText().toString();
				if(UpdateCard_UrlTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkURL(UpdateCard_UrlTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_UrlText.setText("");
					}
				}
				
			}
		});
		UpdateCard_WorkPhoneText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_WorkPhoneTextTXT = UpdateCard_WorkPhoneText.getText().toString();
				if(UpdateCard_WorkPhoneTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_WorkPhoneTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_WorkPhoneText.setText("");
					}
				}
				
			}
		});
		UpdateCard_EmailPersonText.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_EmailPersonTextTXT =UpdateCard_EmailPersonText.getText().toString();
				if(UpdateCard_EmailPersonTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkEmail(UpdateCard_EmailPersonTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_EmailPersonText.setText("");
					}
				}
				
			}
		});
		UpdateCard_AddressText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_AddressTextTXT = UpdateCard_AddressText.getText().toString();
				if(UpdateCard_AddressTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_AddressTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_AddressText.setText("");
					}
				}
				
			}
		});
		UpdateCard_OrganizeNameText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_OrganizeNameTextTXT = UpdateCard_OrganizeNameText.getText().toString();
				if(UpdateCard_OrganizeNameTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_OrganizeNameTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_OrganizeNameText.setText("");
					}
				}
				
			}
		});
		UpdateCard_OrganizeDepartmentText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_OrganizeDepartmentTextTXT = UpdateCard_OrganizeDepartmentText.getText().toString();
				if(UpdateCard_OrganizeDepartmentTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_OrganizeDepartmentTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_OrganizeDepartmentText.setText("");
					}
				}
				
			}
		});
		UpdateCard_OrganizeJobTitleText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_OrganizeJobTitleTextTXT = UpdateCard_OrganizeJobTitleText.getText().toString();
				if(UpdateCard_OrganizeJobTitleTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_OrganizeJobTitleTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_OrganizeJobTitleText.setText("");
					}
				}
				
			}
		});
		UpdateCard_FaxText.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String UpdateCard_FaxTextTXT = UpdateCard_FaxText.getText().toString();
				if(UpdateCard_FaxTextTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(UpdateCard_FaxTextTXT))
					{
						Toast.makeText(UpdateCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						UpdateCard_FaxText.setText("");
					}
				}
			}
		});
	}
	private void saveImage(String path ,Bitmap image , String imageName) throws IOException{
		
		FileOutputStream outStream;
		//使用者未上傳名片
		if(image.sameAs(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.man)))
		{
			outStream = new FileOutputStream(new File(path , "default_image.jpg"));
	        image.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
	        /* 100 to keep full quality of the image */
	        outStream.flush();
	        outStream.close();
	        return;
		}
		File save = new File(path, imageName);
		outStream = new FileOutputStream(save);
		image.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
		/* 100 to keep full quality of the image */
		outStream.flush();
		outStream.close();
	}
	private void update(member.card card) throws IOException
	{
		cardHelper.updateByID(cardID, card);
		this.saveImage(storagePath, myBitMap , "IMG_" + date.getDate() + "_" + card.getCardID()+".jpg");
		
	}
	private void initModule()
	{
		cardHelper = new dbCard(new myDB(this));
		date = new DateUtil();
		myValid = new inputValidation();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) 
	    { 
	        super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	        switch(requestCode) { 
	        case 100:
	            if(resultCode == RESULT_OK)
	            {  
	            	 Uri selectedImage = imageReturnedIntent.getData();
	                 String[] filePathColumn = { MediaStore.Images.Media.DATA };
	                 Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	                 cursor.moveToFirst();
	                 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                 String picturePath = cursor.getString(columnIndex);
	                 cursor.close();
	                 myBitMap = decodeFile(new File(picturePath));
	                 UpdateCard_Photo.setImageBitmap(myBitMap);
	                // photo.refreshDrawableState();
	            }
	        }
	    }
	private Bitmap decodeFile(File f) 
	{
	    try 
	    {
	        // Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f), null, o);

	        // The new size we want to scale to
	        final int REQUIRED_SIZE=70;

	        // Find the correct scale value. It should be the power of 2.
	        int scale = 3;
	       /* while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) 
	        {
	            scale *= 2;
	        }*/
	        // Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } 
	    catch (FileNotFoundException e) 
	    {
	    	System.out.println(e);
	    }
	    return null;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_card, menu);
		getActionBar().setTitle("更新卡片");
		getActionBar().setLogo(R.drawable.ic_arrow_back_white_24dp);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == android.R.id.home)
		{
			this.finish();
			return true;
		}
		else if(id == R.id.update_complete)
		{
			 member.card updateCard = new member.card();
			 updateCard.setCardID(card.getCardID());
			 updateCard.setCardName(UpdateCard_NameText.getText().toString());
			 updateCard.setCardMobilePhone(UpdateCard_MobileText.getText().toString());
			 updateCard.setCardTel(UpdateCard_WorkPhoneText.getText().toString());
			 updateCard.setCardAddress(UpdateCard_AddressText.getText().toString());
			 updateCard.setCardFax(UpdateCard_FaxText.getText().toString());
			 updateCard.setCardEmail(UpdateCard_EmailPersonText.getText().toString());
			 updateCard.setCardUrl(UpdateCard_UrlText.getText().toString());
			 updateCard.setCardCompany(UpdateCard_OrganizeNameText.getText().toString());
			 updateCard.setCardDepartment(UpdateCard_OrganizeDepartmentText.getText().toString());
			 updateCard.setCardJobTitle(UpdateCard_OrganizeJobTitleText.getText().toString());
			 if(!myBitMap.sameAs(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.man)))
				 updateCard.setCardImage("IMG_" + date.getDate() + "_" + card.getCardID()+".jpg" );
			 else
				 updateCard.setCardImage("default_image.jpg");
			 try {
				update(updateCard);
				Toast.makeText(this, "update success!", 10).show();
				this.finish();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
