package com.example.cardinfinity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import myDataBase.dbCard;
import myDataBase.myDB;
import util.DateUtil;
import util.inputValidation;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateOtherCardActivity extends Activity {
	
final static String storagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/otherCardImage/";	;
	
	dbCard cardHelper;
	DateUtil date;
	inputValidation myValid;
	
	String cardID;
	member.othercard card;
	Bitmap myBitMap;
	
	ImageView UpdateOtherCard_Photo;
	EditText UpdateOtherCard_NameText;
	EditText UpdateOtherCard_DescriptionText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_other_card);
		
		this.initModule();
		this.initLayout();
		
		Bundle bundle = getIntent().getExtras();
		cardID = bundle.getString("cardID");
		card = cardHelper.getOtherCardByID(cardID);
		if(card!=null)
		{
			giveCardValue(card);
			
		}
	}
	private void initLayout()
	{
		UpdateOtherCard_NameText = (EditText) findViewById(R.id.updateOtherCard_NameText);
		UpdateOtherCard_DescriptionText = (EditText) findViewById(R.id.updateOtherCard_DescriptionText);
		UpdateOtherCard_Photo = (ImageView) findViewById(R.id.updateOtherCard_Photo);
		
		
		UpdateOtherCard_Photo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent,100);		
			}
		});
	}
	private void giveCardValue(member.othercard card)
	{
		UpdateOtherCard_NameText.setText(card.getOtherCardName());
		UpdateOtherCard_DescriptionText.setText(card.getOtherCardDescription());
		File image = new File(card.getOtherCardImage());
		if(image.exists()){
			myBitMap = BitmapFactory.decodeFile(image.getAbsolutePath());
			UpdateOtherCard_Photo.setImageBitmap(myBitMap);
		}
		else{
			myBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
			UpdateOtherCard_Photo.setImageBitmap(myBitMap);
		}
		
	}
	
	private void initModule()
	{
		cardHelper = new dbCard(new myDB(this));
		date = new DateUtil();
		myValid = new inputValidation();
	}
	
	private void update(member.othercard card) throws IOException
	{
		cardHelper.updateOtherByID(cardID, card);
		this.saveImage(storagePath, myBitMap , "IMG_" + date.getDate() + "_" + card.getOtherCardID()+".jpg");
		
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
                 UpdateOtherCard_Photo.setImageBitmap(myBitMap);
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
		getMenuInflater().inflate(R.menu.update_other_card, menu);
		getActionBar().setTitle("更新卡片");
		getActionBar().setLogo(R.drawable.ic_arrow_back_white_24dp);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == android.R.id.home)
		{
			this.finish();
			return true;
		}
		else if(id == R.id.updateOtherCard_complete)
		{
			 member.othercard updateCard = new member.othercard();
			 updateCard.setOtherCardID(card.getOtherCardID());
			 updateCard.setOtherCardType(card.getOtherCardType());
			 updateCard.setOtherCardName(UpdateOtherCard_NameText.getText().toString());
			 updateCard.setOtherCardDescription(UpdateOtherCard_DescriptionText.getText().toString());
			 if(!myBitMap.sameAs(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.man)))
				 updateCard.setOtherCardImage("IMG_" + date.getDate() + "_" + card.getOtherCardID()+".jpg" );
			 else
				 updateCard.setOtherCardImage("default_image.jpg");
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
