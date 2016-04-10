package com.example.cardinfinity;

import edu.sfsu.cs.orange.ocr.CaptureActivity;
import googleContact.GetAccessToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;

import myDataBase.dbCard;
import myDataBase.myDB;
import phoneContact.addPhoneContactBuilder;
import util.DateUtil;
import util.getScreen;
import util.inputValidation;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AddBusinessCardActivity extends Activity {
	
	String storagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/cardImage/";	
	
	/*module*/
	getScreen screen;
	addPhoneContactBuilder addPhoneContactHelper;
	dbCard cardHelper;
	DateUtil date;
	inputValidation myValid;
	
	
	/*layout*/
	LinearLayout NameAndPhotoLayout;
	Spinner spinner;
	EditText name , mobile , workPhone , emailPerson ,address,personUrl , organizeName , organizeDepartment , organizeJobTitle ,fax;
	ImageView photo;
	RadioButton yesAddToPhone , noAddToPhone;
	RadioButton yesAddToGoogle , noAddToGoogle;
	Bitmap photoBitMap = null;
	
	//google contact information
	private static String CLIENT_ID = "420818563767-h195lf01sop6b0kkhruofdn7f7n38umf.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "VJKsN7OndoENsLsJEO8xE_gP";
	private static String REDIRECT_URI = "http://localhost";
	private static String GRANT_TYPE = "authorization_code";
	private static String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
	private static String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static String OAUTH_SCOPE = "https://www.google.com/m8/feeds";
	WebView web;
	Button auth;
	SharedPreferences pref;
	Dialog auth_dialog;
	
	/*OCR DATA*/
	private String engName[] = {"Chairman","Supervisor","Director","Cheif","Officer","President","Manager","Assistant","Adviser","Consultant",
			"Excutive","Leader","Specialist","Auditor","Engineer","Secretary","Management","Representative","Administrator","Accountant","Analyst","Dept."};
	private String chiTraName[] = {"長","事","總","理","顧問","主任","領班","專員",
			"稽核","師","秘書","幹部","代表","會計","員"};
	String ocrData;
	String ocrName;
	String ocrJob;
	String ocrAddress;
	String ocrCompany;
	String ocrMobile;
	String ocrFax;
	String ocrTel;
	String ocrMail;
	String ocrUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_business_card);
		this.initModule();
		this.initLayout();
		this.inputValid();
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			String from = bundle.getString("from");
			if(from.equals("CaptureActivity")) //如果從CaptureActivity傳值過來
			{
				ocrData = bundle.getString("lastResult");
				processDataFromCaptureActivity(ocrData);
			}
		}
		
		pref = getApplicationContext().getSharedPreferences("AppPref", MODE_PRIVATE);
	}
	
	private void processDataFromCaptureActivity(String data)
	{
		processEng(data); // 賦值
		
		name.setText(ocrName); 
		mobile.setText(ocrMobile); 
		workPhone.setText(ocrTel);
		fax.setText(ocrFax);
		emailPerson.setText(ocrMail);
		personUrl.setText(ocrUrl);
		address.setText(ocrAddress); 
		//personUrl.setText(); 
		organizeName.setText(ocrCompany); 
		//organizeDepartment.setText(); 
		organizeJobTitle.setText(ocrJob); 
		
		Intent intent = getIntent();
		byte [] receivedImageByteArray = intent.getByteArrayExtra(CaptureActivity.EXTRA_BITMAP_BYTE_STREAM);
		photoBitMap = BitmapFactory.decodeByteArray(receivedImageByteArray, 0, receivedImageByteArray.length);
		photo.setImageBitmap(photoBitMap);
		
	}
	public void processEng(String lastResult) 
	{
		System.out.println(lastResult);
		String[] outputText = lastResult.split("\n");

		for (int i = 0; i < outputText.length; i++) {

			String temp = outputText[i];	
			
			int templength = temp.split(" ").length;
			if(templength>1 && templength < 4){
				for(int j = 0; j < engName.length; j++){
					if(temp.contains(engName[j])){
						if(ocrJob == ""){
							ocrJob = temp;
						}
						else{
							ocrJob = ocrJob+temp;
						}		
					}
					else{
						if(ocrName ==""){
							ocrName = temp;
						}
					}
				}	
			}
			if (temp.contains("Ltd.") || temp.contains("LTD.")) {
				ocrCompany = temp;
			}
			else if (temp.contains("Rd.")|| temp.contains("Dist") || temp.contains("City")) {
				if(ocrAddress == ""){
					ocrAddress = temp;
				}
				else {
					ocrAddress = ocrAddress + temp;
				}					
			}
			else if (temp.contains("Mobile")|| temp.contains("CELL") || temp.contains("Phone")) {
				
				int markIndex = temp.indexOf(":");
				temp = temp.substring(markIndex);
				if(temp.contains("(")){
					int tempindex = temp.indexOf(")");
					temp = temp.substring(tempindex);
					ocrMobile = temp;
				}
				if(temp.contains("+")){
					int tempindex = temp.indexOf("+");
					temp = temp.substring(tempindex);
					ocrMobile = temp;
				}
				ocrMobile = temp;
			}
			else if (temp.contains("Fax")) {
				int markIndex = temp.indexOf(":");
				temp = temp.substring(markIndex);
				if(temp.contains("(")){
					int tempindex = temp.indexOf(")");
					temp = temp.substring(tempindex);
					ocrFax = temp;
				}
				if(temp.contains("+")){
					int tempindex = temp.indexOf("+");
					temp = temp.substring(tempindex);
					ocrFax = temp;
				}
				ocrFax= temp;
			}
			else if (temp.contains("Tel")) {
				int markIndex = temp.indexOf(":");
				temp = temp.substring(markIndex);
				if(temp.contains("(")){
					int tempindex = temp.indexOf(")");
					temp = temp.substring(tempindex-1);
					ocrTel = temp;
				}
				if(temp.contains("+")){
					int tempindex = temp.indexOf("+");
					temp = temp.substring(tempindex);
					ocrTel = temp;
				}
				ocrTel = temp;
			}
			else if (temp.contains("Mail")|| temp.contains("@")) {				
				ocrMail = temp;
			}
			else if (temp.contains("com")) {
				ocrUrl = temp;
			}
		}
	}
	
	/*public void processChiTra(String lastResult) {
		String[] outputText = lastResult.split("\n");

		for (int i = 0; i < outputText.length; i++) {

			String temp = outputText[i];
			String address = "";
			
			for(int j = 0; j < chiTraName.length; j++){
				if(temp.contains(chiTraName[j])){
					System.out.println("職稱 : "+temp);
				}
			}

			if (temp.contains("公司") ) {
				System.out.println("公司:" + temp);
			}
			if (temp.contains("市") || temp.contains("區") || temp.contains("路")) {
				address = address + temp;
				System.out.println("地址:" + address);
			}
			if (temp.contains("行動") ) {
				temp = temp.replace(":", "");
				temp = temp.replace("行動", "");
				System.out.println("手機:" + temp);
			}
			if (temp.contains("傳真")) {
				temp = temp.replace(":", "");
				temp = temp.replace("傳真", "");
				System.out.println("Fax:" + temp);
			}
			if (temp.contains("電話")) {
				temp = temp.replace(":", "");
				temp = temp.replace("電話", "");
				System.out.println("Tel:" + temp);
			}
			if (temp.contains("E-Mail")) {
				temp = temp.replace(":", "");
				temp = temp.replace("E-Mail", "");
				System.out.println("郵件:" + temp);
			}
		}

	}*/

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
	private void initModule()
	{
		screen = new getScreen(AddBusinessCardActivity.this);
		addPhoneContactHelper = new addPhoneContactBuilder(AddBusinessCardActivity.this);
		cardHelper = new dbCard(new myDB(this));
		date = new DateUtil();
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
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						name.setText("");
					}
				}
				
			}
		});
		mobile.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String mobileTXT = mobile.getText().toString();
				if(mobileTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(mobileTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						mobile.setText("");
					}
				}
				
			}
		});
		personUrl.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String personUrlTXT = personUrl.getText().toString();
				if(personUrlTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkURL(personUrlTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						personUrl.setText("");
					}
				}
				
			}
		});
		workPhone.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String workPhoneTXT = workPhone.getText().toString();
				if(workPhoneTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(workPhoneTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						workPhone.setText("");
					}
				}
				
			}
		});
		emailPerson.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String emailPersonTXT = emailPerson.getText().toString();
				if(emailPersonTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkEmail(emailPersonTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						emailPerson.setText("");
					}
				}
				
			}
		});
		address.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String addressTXT = address.getText().toString();
				if(addressTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(addressTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						address.setText("");
					}
				}
				
			}
		});
		organizeName.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String organizeNameTXT = organizeName.getText().toString();
				if(organizeNameTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(organizeNameTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						organizeName.setText("");
					}
				}
				
			}
		});
		organizeDepartment.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String organizeDepartmentTXT = organizeDepartment.getText().toString();
				if(organizeDepartmentTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(organizeDepartmentTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						organizeDepartment.setText("");
					}
				}
				
			}
		});
		organizeJobTitle.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String organizeJobTitleTXT = organizeJobTitle.getText().toString();
				if(organizeJobTitleTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(organizeJobTitleTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						organizeJobTitle.setText("");
					}
				}
				
			}
		});
		fax.setOnFocusChangeListener(new OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				String faxTXT = fax.getText().toString();
				if(faxTXT.equals("")){return;}
				if(!hasFocus)
				{
					if(!myValid.checkIllegal(faxTXT))
					{
						Toast.makeText(AddBusinessCardActivity.this, "格式輸入錯誤! 請重新輸入", 5).show();
						fax.setText("");
					}
				}
				
			}
		});
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
	private void initLayout()
	{
		//NameAndPhotoLayout setting
		NameAndPhotoLayout = (LinearLayout) findViewById(R.id.addBusinessCard_NameAndPhotoLayout);
		LinearLayout.LayoutParams paramsNameAndPhotoLayout = (LinearLayout.LayoutParams)NameAndPhotoLayout.getLayoutParams();
		paramsNameAndPhotoLayout.height = screen.getScreenHeight() /6;
		//Edit text 
		name = (EditText) findViewById(R.id.addBusinessCard_NameText);
		mobile = (EditText) findViewById(R.id.addBusinessCard_MobileText);
		workPhone = (EditText) findViewById(R.id.addBusinessCard_WorkPhoneText);
		emailPerson = (EditText) findViewById(R.id.addBusinessCard_EmailPersonText);
		address = (EditText) findViewById(R.id.addBusinessCard_AddressText);
		fax = (EditText)findViewById(R.id.addBusinessCard_FaxText);
		personUrl = (EditText) findViewById(R.id.addBusinessCard_UrlText);
		organizeName = (EditText) findViewById(R.id.addBusinessCard_OrganizeNameText);
		organizeDepartment = (EditText) findViewById(R.id.addBusinessCard_OrganizeDepartmentText);
		organizeJobTitle = (EditText) findViewById(R.id.addBusinessCard_OrganizeJobTitleText);
		//ImageView
		photo = (ImageView) findViewById(R.id.addBusinessCard_Photo);
		photo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent,100);		
			}
		});
		photoBitMap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.man);
		//RadioButton
		yesAddToPhone = (RadioButton) findViewById(R.id.addBusinessCard_YesAddToPhone);
		noAddToPhone = (RadioButton) findViewById(R.id.addBusinessCard_NoAddToPhone);
		
		yesAddToGoogle = (RadioButton)findViewById(R.id.addBusinessCard_YesAddToGoogle);
		noAddToGoogle = (RadioButton)findViewById(R.id.addBusinessCard_NoAddToGoogle);
		
	}
	private void addToPhoneContact(String name , String mobile ,String workPhone , 
									String emailPerson , String address , String organizeName , 
									 String organizeJobTitle ,Bitmap photo){
		addPhoneContactHelper.init()
							 .addName(name)
							 .addPhoneNumber(workPhone , Phone.TYPE_WORK)
							 .addPhoneNumber(mobile, Phone.TYPE_MOBILE)
							 .addAddress(address, StructuredPostal.TYPE_HOME)
							 .addContactEmail(emailPerson, Email.TYPE_HOME)
							 .addOrganization(organizeName, organizeJobTitle, Organization.TYPE_WORK)
							 .addPhoto(photo)
							 .exec();
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
	                 photoBitMap = decodeFile(new File(picturePath));
	                 photo.setImageBitmap(photoBitMap);
	                // photo.refreshDrawableState();
	            }
	        }
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_business_card, menu);
		getActionBar().setTitle("新增名片");
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
		switch(id)
		{
			case android.R.id.home://上一頁
				this.finish();
				break;
			case R.id.addBusinessCard_complete://新增聯絡人
				String uniqueID = UUID.randomUUID().toString();
				if(name.getText().toString().equals("") || mobile.getText().toString().equals("")){Toast.makeText(this, "名字、行動電話必須輸入", 5).show();break;}
				if(photoBitMap.sameAs(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.man)))
				{
					//使用者未上傳名片
					cardHelper.addCard(
					uniqueID,
					name.getText().toString(), 
					organizeName.getText().toString(), 
					organizeDepartment.getText().toString(),
					organizeJobTitle.getText().toString(), 
					emailPerson.getText().toString(), 
					workPhone.getText().toString(),
					mobile.getText().toString(), 
					address.getText().toString(),
					fax.getText().toString(),
					"default_image.jpg", 
					personUrl.getText().toString(),
					"BussinessCard");
				}
				else
				{
					//使用者有上傳名片
					cardHelper.addCard(
					uniqueID,
					name.getText().toString(), 
					organizeName.getText().toString(), 
					organizeDepartment.getText().toString(),
					organizeJobTitle.getText().toString(), 
					emailPerson.getText().toString(), 
					workPhone.getText().toString(),
					mobile.getText().toString(), 
					address.getText().toString(),
					fax.getText().toString(),
					"IMG_" + date.getDate() + "_" + uniqueID+".jpg", 
					personUrl.getText().toString(),
					"BussinessCard");
				}
			try 
			{
				//保存名片圖檔在手機
				this.saveImage(storagePath, photoBitMap , "IMG_" + date.getDate() + "_" + uniqueID +".jpg");
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
				
			if(yesAddToPhone.isChecked()) // 新增到手機聯絡人
			{
				this.addToPhoneContact(name.getText().toString(), 
					mobile.getText().toString(), 
					workPhone.getText().toString(), 
					emailPerson.getText().toString(), 
					address.getText().toString(), 
					organizeName.getText().toString(), 
					organizeJobTitle.getText().toString(), 
					photoBitMap);
			}
			
			
			if(yesAddToGoogle.isChecked()) // 新增到Google Contact
				{
					
					final Context context = AddBusinessCardActivity.this;
					auth_dialog = new Dialog(AddBusinessCardActivity.this);
					auth_dialog.setContentView(R.layout.auth_dialog);
					web = (WebView) auth_dialog.findViewById(R.id.webv);
					web.getSettings().setJavaScriptEnabled(true);
					web.loadUrl(OAUTH_URL + "?redirect_uri=" + REDIRECT_URI
							+ "&response_type=code&client_id=" + CLIENT_ID
							+ "&scope=" + OAUTH_SCOPE);
					web.setWebViewClient(new WebViewClient() {
						boolean authComplete = false;
						Intent resultIntent = new Intent();
						@Override
						public void onPageStarted(WebView view, String url,
								Bitmap favicon) {
							super.onPageStarted(view, url, favicon);
						}
						String authCode;
						@Override
						public void onPageFinished(WebView view, String url) {
							super.onPageFinished(view, url);
							if (url.contains("?code=") && authComplete != true) {
								Uri uri = Uri.parse(url);
								authCode = uri.getQueryParameter("code");
								Log.i("", "CODE : " + authCode);
								authComplete = true;
								resultIntent.putExtra("code", authCode);
								AddBusinessCardActivity.this.setResult(Activity.RESULT_OK,
										resultIntent);
								setResult(Activity.RESULT_CANCELED, resultIntent);

								SharedPreferences.Editor edit = pref.edit();
								edit.putString("Code", authCode);
								edit.commit();
								auth_dialog.dismiss();
								new TokenGet(context).execute(authCode);
								// Toast.makeText(getApplicationContext(),
								// "Authorization Code is: " + authCode,
								// Toast.LENGTH_SHORT).show();
							} else if (url.contains("error=access_denied")) {
								Log.i("", "ACCESS_DENIED_HERE");
								resultIntent.putExtra("code", authCode);
								authComplete = true;
								setResult(Activity.RESULT_CANCELED, resultIntent);
								Toast.makeText(getApplicationContext(),
										"Error Occured", Toast.LENGTH_SHORT).show();
								auth_dialog.dismiss();
							}
						}
					});
					auth_dialog.show();
					auth_dialog.setTitle("Google Authorized");
					auth_dialog.setCancelable(true);
				}
				if(noAddToGoogle.isChecked())
				{
					SharedPreferences.Editor edit = pref.edit();
					edit.remove("Code");
					edit.commit();
					this.finish();
				}
				
				
				Toast.makeText(getApplicationContext(), "insert success!", 10).show();
				//this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    System.gc();
	}
	// using async task to get token
		private class TokenGet extends AsyncTask<String, String, JSONObject> {
			private ProgressDialog pDialog;
			String Code;
			private Context context;

			public TokenGet(Context context) {
				this.context = context;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(AddBusinessCardActivity.this);
				pDialog.setMessage("Contacting Google ...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				Code = pref.getString("Code", "");
				pDialog.show();
			}

			@Override
			protected JSONObject doInBackground(String... args) {
				GetAccessToken jParser = new GetAccessToken();
				JSONObject json = jParser.gettoken(TOKEN_URL, Code, CLIENT_ID,
						CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE);
				System.out.println(json);
				return json;
			}

			@Override
			protected void onPostExecute(JSONObject json) {
				pDialog.dismiss();
				if (json != null) {

					try {

						String tok = json.getString("access_token");
						String expire = json.getString("expires_in");
						String refresh = json.getString("refresh_token");

						Log.d("Token Access", tok);
						Log.d("Expire", expire);
						Log.d("Refresh", refresh);
						// auth.setText("Authenticated");
						// Access.setText("Access Token:" + tok + "nExpires:" +
						// expire
						// + "nRefresh Token:" + refresh);
						new GetGoogleContacts(context).execute(tok);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getApplicationContext(), "Network Error",
							Toast.LENGTH_SHORT).show();
					pDialog.dismiss();
				}
			}
		}
		//using async task insert to Google Contact
		private class GetGoogleContacts extends
				AsyncTask<String, String, ContactsService> {

			private ProgressDialog pDialog;
			private Context context;

			public GetGoogleContacts(Context context) {
				this.context = context;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Authenticated. Getting Google Contacts ...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});
				pDialog.show();
			}

			@Override
			protected ContactsService doInBackground(String... args) {
				String accessToken = args[0];
				ContactsService contactsService = new ContactsService("ContactsAdd");
				contactsService.setHeader("Authorization", "Bearer " + accessToken);
				contactsService.setHeader("GData-Version", "3.0");
				// Create the entry to insert.
				ContactEntry contact = new ContactEntry();
				// Set the contact's name.
				Name n = new Name();
				final String NO_YOMI = null;
				n.setFullName(new FullName(name.getText().toString(), NO_YOMI));
				contact.setName(n);
				contact.setContent(new PlainTextConstruct("Notes"));
				// Set contact's phone numbers.
				PhoneNumber primaryPhoneNumber = new PhoneNumber();
				primaryPhoneNumber.setPhoneNumber(mobile.getText().toString());
				primaryPhoneNumber.setRel("http://schemas.google.com/g/2005#work");
				primaryPhoneNumber.setPrimary(true);
				contact.addPhoneNumber(primaryPhoneNumber);
				PhoneNumber secondaryPhoneNumber = new PhoneNumber();
				secondaryPhoneNumber.setPhoneNumber(workPhone.getText()
						.toString());
				secondaryPhoneNumber
						.setRel("http://schemas.google.com/g/2005#home");
				contact.addPhoneNumber(secondaryPhoneNumber);

				try {
					// Ask the service to insert the new entry
					URL postUrl = new URL(
							"https://www.google.com/m8/feeds/contacts/default/full");
					ContactEntry createdContact = contactsService.insert(postUrl,
							contact);

				} catch (Exception e) {
					pDialog.dismiss();
					e.printStackTrace();
					//Toast.makeText(context, "Failed to get Contacts",
							//Toast.LENGTH_SHORT).show();
				}
				return contactsService;
			}

			@Override
			protected void onPostExecute(ContactsService myService) {
				pDialog.dismiss();
				Toast.makeText(context, "success to add contacts",
						Toast.LENGTH_SHORT).show();
				AddBusinessCardActivity.this.finish();
			}

		}
		
}