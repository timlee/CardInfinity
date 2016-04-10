package edu.sfsu.cs.orange.ocr;

import com.example.cardinfinity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class OCRMainActivity extends Activity {
	
	public static final int requestCode = 0;
	
//	private OcrResultProcess ocrResultProcess;
	private static final String TAG = OCRMainActivity.class.getSimpleName();
	private static boolean isFirstLaunch;
	private SharedPreferences prefs;
	
	private String lastResult;
	private String ocrLanguage;
	private String engName[] = {"Chairman","Supervisor","Director","Cheif","Officer","President","Manager","Assistant","Adviser","Consultant",
			"Excutive","Leader","Specialist","Auditor","Engineer","Secretary","Management","Representative","Administrator","Accountant","Analyst"};
//	private String engName[];
	private String chiTraName[] = {"長","事","總","理","顧問","主任","領班","專員",
			"稽核","師","秘書","幹部","代表","會計","員"};

	private Button btn_camera;
	private Button btn_select;
	private EditText txtName;
	private EditText txtTel;
	private EditText txtMobile;
	private EditText txtFax;
	private EditText txtAddress;
	private EditText txtMail;
	private EditText txtJob;
	private EditText txtCompany;
	private ImageView lastImage;
	Intent intent;
	
	String name = "";
	String job = "";
	String address = "";
	String company = "";
	String mobile = "";
	String fax = "";
	String tel = "";
	String mail = "";
	@Override
	public void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		
		setContentView(R.layout.activity_ocr_main);
		
		btn_camera = (Button) findViewById(R.id.btn_camera);
		btn_camera.setOnClickListener(button_listener);
		btn_select = (Button) findViewById(R.id.btn_select);
		btn_select.setOnClickListener(button_listener);
		txtName = (EditText) findViewById(R.id.txtName);
		txtTel = (EditText) findViewById(R.id.txtTel);
		txtMobile = (EditText) findViewById(R.id.txtMobile);
		txtFax = (EditText) findViewById(R.id.txtFax);
		txtCompany = (EditText) findViewById(R.id.txtCompany);
		txtAddress = (EditText) findViewById(R.id.txtAddress);
		txtMail = (EditText) findViewById(R.id.txtMail);
		txtJob = (EditText) findViewById(R.id.txtJob);	
		lastImage = (ImageView) findViewById(R.id.lastImage);
		
//		try{
//	        //建立FileReader物件，並設定讀取的檔案為SD卡中的output.txt檔案
//	        FileReader fr = new FileReader("engJob.txt");
//	        System.out.print("success");
//	        //將BufferedReader與FileReader做連結
//	        BufferedReader br = new BufferedReader(fr);
//	        int i=0;
//	        String temp = br.readLine(); //readLine()讀取一整行
//	        while (temp!=null){
//	        	engName[i] = temp;
//	             temp=br.readLine();
//	             i++;
//	        }
//	        for (int j=0;j<engName.length;j++){
//	        	System.out.println(engName[j]);
//	        }
//	    }catch(Exception e){
//	        e.printStackTrace();
//	    }
		intent = getIntent();
		Bundle bundle = getIntent().getExtras();
		lastResult = bundle.getString("lastResult");
//		ocrResultProcess.processResult(lastResult,"eng");
		byte [] receivedImageByteArray = intent.getByteArrayExtra(CaptureActivity.EXTRA_BITMAP_BYTE_STREAM);
		Bitmap bmp = BitmapFactory.decodeByteArray(receivedImageByteArray, 0, receivedImageByteArray.length);
		lastImage.setImageBitmap(bmp);
		processResult(lastResult,"eng");
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		System.out.println("enter onActivityResult method");
//		if (requestCode == SHOW_RESULT) {
//			if (resultCode == RESULT_OK) {
//				
//			}
//		}
//	}
	
	private OnClickListener button_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == btn_camera) {// 照相進行辨識
				System.out.println("enter camera");
				Intent intent = new Intent();
				intent.setClass(OCRMainActivity.this, CaptureActivity.class);
				startActivity(intent);
				OCRMainActivity.this.finish();
			} else if (v == btn_select) {// 選擇相片進行辨識
				System.out.println("enter album");
			}

		}
	};
	
	public void processResult (String lastResult,String ocrLanguage){
		if (ocrLanguage == "eng"){
			processEng(lastResult);
		}
//		else if(ocrLanguage == "zh_TW");{
//			processChiTra(lastResult);
//		}
	}
	
	public void processEng(String lastResult) 
	{
		System.out.println(lastResult);
		String[] outputText = lastResult.split("\n");
		String result = "";

		for (int i = 0; i < outputText.length; i++) {

			String temp = outputText[i];	
			
			int templength = temp.split(" ").length;
			if(templength == 2 || templength == 3){
				for(int j = 0; j < engName.length; j++){
					if(temp.contains(engName[j])){
						if(job == ""){
							job = temp;
						}
						else{
							job = job+temp;
						}		
					}
					else{
						name = temp;
					}
				}	
			}
			if (temp.contains("Ltd.")) {
				company = temp;
			}
			if (temp.contains("Rd.")|| temp.contains("Dist") || temp.contains("City")) {
				if(address == ""){
					address = temp;
				}
				else {
					address = address + temp;
				}					
			}
			if (temp.contains("Mobile") || temp.contains("CELL PHONE")) {
				temp = temp.replace(":", "");
				temp = temp.replace("Mobile", "");
				temp = temp.replace("CELL PHONE", "");
				mobile = temp;
			}
			if (temp.contains("Fax")) {
				temp = temp.replace(":", "");
				temp = temp.replace("Fax", "");
				fax = temp;
			}
			if (temp.contains("Tel")) {
				temp = temp.replace(":", "");
				temp = temp.replace("Tel", "");
				tel = temp;
			}
			if (temp.contains("Mail")) {
				temp = temp.replace(":", "");
				temp = temp.replace("E-Mail", "");
				mail = temp;
			}
		}
	}

	public void processChiTra(String lastResult) {
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

	}


}


