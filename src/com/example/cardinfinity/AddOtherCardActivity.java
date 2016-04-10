package com.example.cardinfinity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import util.inputValidation;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddOtherCardActivity extends Activity 
{
	String storagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/otherCardImage/";
	
	Bitmap photoBitMap;
	dbCard cardHelper;
	inputValidation myValid;
	Spinner spinner;
	EditText name,description;
	ImageView photo;
	String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_other_card);
		this.initLayout();
		this.initModule();
		this.inputValid();
	}
	
	private void initLayout()
	{
		type = "Identification"; // default type;
		
		String[] ItemList = getResources().getStringArray(R.array.SpinnerCatagory);
		//將可選内容與ArrayAdapter連接起來 
		ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.spinnertitle,ItemList);
		//對應控件
		spinner = (Spinner) findViewById(R.id.addOtherCard_Spinner);
		//設置下拉列表的風格
		adapter.setDropDownViewResource(R.layout.spinner);
		//將adapter 添加到spinner中  
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new setSpinnerSelected());
		
		name = (EditText)findViewById(R.id.addOtherCard_NameText);
		description = (EditText)findViewById(R.id.addOtherCard_DescriptionText);
		photo = (ImageView)findViewById(R.id.addOtherCard_Photo);
		
		photo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent,100);		
			}
		});
		
		
	}
	private void initModule(){
		cardHelper = new dbCard(new myDB(this));
		myValid = new inputValidation();
	}
	private void inputValid()
	{
		name.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String nameTXT = name.getText().toString();
				if(nameTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(nameTXT))
					{
						Toast.makeText(AddOtherCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						name.setText("");
					}
				}
			}
		});
		description.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String descriptionTXT = description.getText().toString();
				if(descriptionTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(descriptionTXT))
					{
						Toast.makeText(AddOtherCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						description.setText("");
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
	        int scale = 0;
	        /*while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) 
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
		                 
		                 System.out.println(picturePath);
		                 
		                 photoBitMap = decodeFile(new File(picturePath));
		                 photo.setImageBitmap(photoBitMap);
		                // photo.refreshDrawableState();
		            }
		        }
		    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_other_card, menu);
		getActionBar().setTitle("新增卡片");
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
		if (id == android.R.id.home) {
			this.finish();
			return true;
		}
		else if(id == R.id.addOtherCard_complete)
		{
			String uniqueID = UUID.randomUUID().toString();
			if(photoBitMap !=null)
			{
				cardHelper.addOtherCard
				(
						uniqueID, 
						type, 
						name.getText().toString(), 
						uniqueID, 
						description.getText().toString()
				);
				try 
				{
					this.saveImage(storagePath, photoBitMap, uniqueID +".jpg");
					this.finish();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(this, "please upload the image", 10).show();
			}
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	class setSpinnerSelected implements Spinner.OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) 
		{
			switch(position)
			{
			   case 1:
				   type = "Preferential";
			   case 2:
				   type = "Other";
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}
}
