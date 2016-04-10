package com.example.cardinfinity;

import myDataBase.dbUser;
import myDataBase.myDB;
import util.buttonTransparent;
import util.getScreen;
import util.inputValidation;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity {

	LinearLayout ForgetPassword_Content;
	LinearLayout Forget_ResultLayout;
	
	Button ForgetBtn;
	
	EditText Forget_Account;
	EditText Forget_Name;
	EditText Forget_EMail;
	
	TextView Forget_Result;
	
	dbUser user;
	getScreen screen;
	myDB dbHelper ;
	inputValidation myValid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		this.moduleInit();
		this.layoutInit();
		this.listenerInit();
	}
	private void layoutInit(){
		/*Layout Setting*/
		ForgetPassword_Content = (LinearLayout) findViewById(R.id.ForgetPassword_Content);
		int topPadding = (int) (screen.getScreenHeight() * 0.15);
		ForgetPassword_Content.setPadding(0, topPadding, 0, 0);
		/*ForgetResult Layout Setting*/
		Forget_ResultLayout = (LinearLayout) findViewById(R.id.Forget_ResultLayout);
		Forget_ResultLayout.setVisibility(View.INVISIBLE);
		/*TextView Setting*/
		Forget_Result = (TextView) findViewById(R.id.Forget_Result);
		/*Button Setting*/
		ForgetBtn = (Button) findViewById(R.id.Forget_ForgetBtn);
		ForgetBtn.setOnTouchListener(new buttonTransparent());
		/*EditText Setting*/
		Forget_Account = (EditText) findViewById(R.id.Forget_AccountText);
		Forget_Name = (EditText) findViewById(R.id.Forget_NameText);
		Forget_EMail = (EditText) findViewById(R.id.Forget_EMailText);
		
	}
	private void moduleInit(){
		screen = new getScreen(ForgetPasswordActivity.this);
		dbHelper = new myDB(getApplicationContext());
		user = new dbUser(dbHelper);
		myValid = new inputValidation();
	}
	private void listenerInit(){
		ForgetBtn.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				
				String account = Forget_Account.getText().toString();
				String name = Forget_Name.getText().toString();
				String email = Forget_EMail.getText().toString();
				
				if(account.length() != 0 && name.length() !=0 && email.length() != 0)
				{
					if(myValid.checkIllegal(account) && myValid.checkIllegal(name) && myValid.checkEmail(email))
					{
						String result = user.getForgetPassword(account, name, email);
						Forget_Result.setText(result);
						Forget_ResultLayout.setVisibility(View.VISIBLE);
						
					}
					else
					{
						Forget_ResultLayout.setVisibility(View.VISIBLE);
						Forget_Result.setText("");
						Toast.makeText(getApplicationContext(), "輸入字元格式錯誤，請重新輸入!", 10).show();
						Forget_Account.setText("");
						Forget_Name.setText("");
						Forget_EMail.setText("");
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "欄位不能為空!", 10).show();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forget_password, menu);
		getActionBar().setTitle("忘記密碼");
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
		if(id == android.R.id.home){
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	    System.gc();
	}
}
