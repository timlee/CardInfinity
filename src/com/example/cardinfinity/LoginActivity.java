package com.example.cardinfinity;

import java.util.ArrayList;

import sharedPref.sessionManager;
import util.buttonTransparent;
import util.getScreen;
import util.inputValidation;
//import myDataBase.dbSchema;
import myDataBase.dbUser;
import myDataBase.myDB;
import member.user;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private dbUser user = null;
	private myDB dbHelper = null;
	private inputValidation myValid;
	private getScreen screen;
	private sessionManager sessionHelper;
	
	LinearLayout Login_MainContent;
	LinearLayout Login_TitleContent;
	LinearLayout Login_Content;
	LinearLayout Login_Forget;
	EditText Login_Account;
	EditText Login_Password;
	Button Login_LoginBtn;
	Button Login_JoinBtn;
	TextView Forget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		this.moduleInit();
		this.layoutInit();
		this.listenerInit();
		
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {  
		    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		
		//check user is Logged
		if(sessionHelper.isLogin()){
			 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	         startActivity(intent);
	         finish();
		}
	}

	private void moduleInit(){
		dbHelper = new myDB(LoginActivity.this);
		dbHelper.openDB(); // open db
		user = new dbUser(dbHelper);
		myValid = new inputValidation();
		screen = new getScreen(LoginActivity.this);
		sessionHelper = new sessionManager(getApplicationContext());
	}
	private void layoutInit(){
				
		/*Login Main ScrollView*/
		Login_MainContent = (LinearLayout) findViewById(R.id.Login_MainContent);
		
		/*LoginMainContent Setting*/
		Login_Content = (LinearLayout) findViewById(R.id.Login_Content);
		LinearLayout.LayoutParams paramsLoginContent = (LinearLayout.LayoutParams)Login_Content.getLayoutParams();
		int topMargin = (screen.getScreenHeight() / 12) ;
		int bottomMargin = (screen.getScreenHeight() / 12);
		paramsLoginContent.setMargins(0, topMargin, 0, bottomMargin);
		paramsLoginContent.width = (int) (screen.getScreenWidth() * 0.95);
		
		/*TitleContent Setting*/
		Login_TitleContent = (LinearLayout) findViewById(R.id.Login_TitleContent);
		LinearLayout.LayoutParams paramsLoginTitleContent = (LinearLayout.LayoutParams)Login_TitleContent.getLayoutParams();
		paramsLoginTitleContent.setMargins(0, screen.getScreenHeight() / 12, 0, 0);
		Login_TitleContent.setLayoutParams(paramsLoginTitleContent);
		
		/*ForgetLayout Setting*/
		Login_Forget = (LinearLayout)findViewById(R.id.Login_ForgetLayout);
		Login_Forget.setPadding( (int) (screen.getScreenWidth() * 0.08), 25, 0, 0);
		
		/*Forget TextView Setting*/
		Forget = (TextView) findViewById(R.id.Forget);
		SpannableString content = new SpannableString("忘記密碼?");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		Forget.setText(content);
		
		/*EditText Setting*/
		Login_Account = (EditText) findViewById(R.id.Login_Account);
		Login_Password = (EditText) findViewById(R.id.Login_Password);
		Login_Account.getLayoutParams().width =(int) (screen.getScreenWidth() * 0.75);
		Login_Password.getLayoutParams().width =(int) (screen.getScreenWidth() * 0.75);
		
		/*Button Setting*/
		Login_LoginBtn = (Button) findViewById(R.id.Login_LoginBtn);
		Login_JoinBtn = (Button) findViewById(R.id.Login_JoinBtn);
		Login_LoginBtn.getLayoutParams().width =(int) (screen.getScreenWidth() * 0.35);
		Login_JoinBtn.getLayoutParams().width =(int) (screen.getScreenWidth() * 0.35);
		
		Login_LoginBtn.setOnTouchListener(new buttonTransparent());
		Login_JoinBtn.setOnTouchListener(new buttonTransparent());
	
		
	}
	private void listenerInit(){
		
		Login_LoginBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				String accountText = Login_Account.getText().toString();
				String passwordText = Login_Password.getText().toString();
				if(!accountText.equals("") && !passwordText.equals(""))
				{
					if(myValid.checkNumberAndEng(accountText) && myValid.checkNumberAndEng(passwordText))
					{
						user result = user.checkUser(accountText, passwordText);
						//login success
						if(result != null){
							sessionHelper.setLogin(true);
							sessionHelper.setUserName(result.getName());
							 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					         startActivity(intent);
					         finish();
						}
						//login fail
						else{
							Toast.makeText(getApplicationContext(), "登入失敗，請重新輸入", 10).show();
							Login_Account.setText("");
							Login_Password.setText("");
						}
					}
					//format error
					else
					{
						Toast.makeText(getApplicationContext(),"欄位有非法字元，請重新輸入!" ,10).show();
						Login_Account.setText("");
						Login_Password.setText("");
					}
				}
				//format error
				else{
					Toast.makeText(getApplicationContext(), "欄位不能為空，請重新輸入!", 10).show();
					Login_Account.setText("");
					Login_Password.setText("");
				}
			}
		});
		Login_JoinBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		Forget.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this,ForgetPasswordActivity.class);
				startActivity(intent);
			}
		});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		getActionBar().setTitle("登入頁面");
		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent))); // invisible icon
		getActionBar().setHomeButtonEnabled(false);
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
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	    System.gc();
	}
}
