package com.example.cardinfinity;

import myDataBase.dbUser;
import myDataBase.myDB;
import util.getScreen;
import util.inputValidation;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private dbUser user = null;
	private myDB dbHelper = null;
	private inputValidation myValid;
	private getScreen screen;
	
	LinearLayout Register_Content;
	
	TextView RegisterAccount_Message;
	TextView RegisterPassword_Message;
	TextView RegisterName_Message;
	TextView RegisterAge_Message;
	TextView RegisterRePassword_Message;
	TextView RegisterEMail_Message;
	
	EditText Register_AccountText;
	EditText Register_PasswordText;
	EditText Register_NameText;
	EditText Register_AgeText;
	EditText Register_RePasswordText;
	EditText Register_EMailText;
	
	int maxLength = 8;
    int restLength = maxLength;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		this.moduleInit();
		this.layoutInit();
		this.listenerInit();
	}
	private void moduleInit(){
		dbHelper = new myDB(RegisterActivity.this);
		dbHelper.openDB(); // open db
		user = new dbUser(dbHelper);
		myValid = new inputValidation();
		screen = new getScreen(RegisterActivity.this);
	}
	private void layoutInit(){
		
		//find Layout
		Register_Content = (LinearLayout) findViewById(R.id.Register_ContentChild);

		//find EditText
		Register_AccountText = (EditText) findViewById(R.id.Register_AccountText);
		Register_PasswordText = (EditText) findViewById(R.id.Register_PasswordText);
		Register_NameText = (EditText) findViewById(R.id.Register_NameText);
		Register_AgeText = (EditText) findViewById(R.id.Register_AgeText);
		Register_RePasswordText = (EditText) findViewById(R.id.Register_RePasswordText);
		Register_EMailText = (EditText) findViewById(R.id.Register_EmailText);
		
		//find Message Textview
		RegisterAccount_Message = (TextView) findViewById(R.id.RegisterAccount_Message);
		RegisterPassword_Message = (TextView) findViewById(R.id.RegisterPassword_Message);
		RegisterRePassword_Message = (TextView) findViewById(R.id.RegisterRePassword_Message);
		RegisterName_Message = (TextView) findViewById(R.id.RegisterName_Message);
		RegisterAge_Message = (TextView) findViewById(R.id.RegisterAge_Message);
		RegisterEMail_Message = (TextView) findViewById(R.id.RegisterEMail_Message);
		
		//EditText setting
		int editTextLength =  (int) (screen.getScreenWidth() * 0.75);
		Register_AccountText.getLayoutParams().width =editTextLength;
		Register_PasswordText.getLayoutParams().width =editTextLength;
		Register_NameText.getLayoutParams().width =editTextLength;
		Register_AgeText.getLayoutParams().width =editTextLength;
		Register_RePasswordText.getLayoutParams().width =editTextLength;
		Register_EMailText.getLayoutParams().width =editTextLength;
	}
	private void listenerInit(){
		// clear focus
		Register_Content.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				        View temp = getCurrentFocus();
				        if ( temp instanceof EditText)
				        {
				            Rect outRect = new Rect();
				            v.getGlobalVisibleRect(outRect);    
				            v.clearFocus();
				            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				        }
				   }
			});
		//�b���榡�T�{
		Register_AccountText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					String accountTXT = Register_AccountText.getText().toString();
					if(!(accountTXT.length() == 0)){
						if(myValid.checkNumberAndEng(accountTXT))
						{
							if(user.checkUserAccountIsUnique(accountTXT)){
								RegisterAccount_Message.setTextColor(getResources().getColor(R.color.green));
								RegisterAccount_Message.setText("OK!");
							}
							else{
								RegisterAccount_Message.setTextColor(getResources().getColor(R.color.red));
								RegisterAccount_Message.setText("�b�����ơA���s��J");
								Register_AccountText.setText("");
							}
							
						}
						else
						{
							RegisterAccount_Message.setTextColor(getResources().getColor(R.color.red));
							RegisterAccount_Message.setText("�榡���~�A�Э��s��J!");
							Register_AccountText.setText("");
						}
					}
					else
					{
						RegisterAccount_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterAccount_Message.setText("��쬰��!");
					}

				}
			}
		});
		//�K�X�r�����ܤήɦ^�_
		Register_PasswordText.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String PasswordTXT = Register_PasswordText.getText().toString();
				restLength = (maxLength - PasswordTXT.length());
				if(!myValid.checkNumberAndEng(PasswordTXT)){
					if(PasswordTXT.length() == 0){
						RegisterPassword_Message.setTextColor(getResources().getColor(R.color.green));
						RegisterPassword_Message.setText("�ٯ��J" + maxLength + "�Ӧr��");
					}
					else{
						RegisterPassword_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterPassword_Message.setText("�榡���~�A�Э��s��J!");
					}
					
				}
				else{
					if(restLength >= 0){
						RegisterPassword_Message.setTextColor(getResources().getColor(R.color.green));
						RegisterPassword_Message.setText("�ٯ��J" + restLength + "�Ӧr��");
					}
					else{
						RegisterPassword_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterPassword_Message.setText("�W�L" + (PasswordTXT.length() - maxLength) + "�Ӧr��");
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//�K�X�r���榡�T�{
		Register_PasswordText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					String PasswordTXT = Register_PasswordText.getText().toString();
					if(! (PasswordTXT.length() == 0))
					{
						if(myValid.checkNumberAndEng(PasswordTXT) && myValid.checkStringLength(PasswordTXT, maxLength))
						{
							RegisterPassword_Message.setTextColor(getResources().getColor(R.color.green));
							RegisterPassword_Message.setText("OK!");
						}
						else
						{
							RegisterPassword_Message.setTextColor(getResources().getColor(R.color.red));
							RegisterPassword_Message.setText("�榡���~�A�Э��s��J!");
						}
					}
					else{
						RegisterPassword_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterPassword_Message.setText("��쬰��!");
					}
					
				}
			}
		});
		//�G���K�X���ܤήɦ^�_
		Register_RePasswordText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String RePasswordTXT = Register_RePasswordText.getText().toString();
				restLength = (maxLength - RePasswordTXT.length());
				if(!myValid.checkNumberAndEng(RePasswordTXT)){
					if(RePasswordTXT.length() == 0){
						RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.green));
						RegisterRePassword_Message.setText("�ٯ��J" + maxLength + "�Ӧr��");
					}
					else{
						RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterRePassword_Message.setText("�榡���~�A�Э��s��J!");
					}
					
				}
				else{
					if(restLength >= 0){
						RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.green));
						RegisterRePassword_Message.setText("�ٯ��J" + restLength + "�Ӧr��");
					}
					else{
						RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterRePassword_Message.setText("�W�L" + (RePasswordTXT.length() - maxLength) + "�Ӧr��");
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		//�G���K�X�r���榡�T�{
		Register_RePasswordText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					String RePasswordTXT = Register_RePasswordText.getText().toString();
					String PasswordTXT = Register_PasswordText.getText().toString();
					if(! (RePasswordTXT.length() == 0)) //��줣����
					{
						if(myValid.checkNumberAndEng(RePasswordTXT) && myValid.checkStringLength(RePasswordTXT, maxLength))//�榡�����T��
						{
							if(!PasswordTXT.equals(RePasswordTXT))//�⦸�K�X���P��
							{
								RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.red));
								RegisterRePassword_Message.setText("�⦸�K�X���P�A�Э��s��J!");
							}
							else // �⦸�K�X�ۦP��
							{
								RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.green));
								RegisterRePassword_Message.setText("OK!");
							}
						}
						else //�榡�D���T
						{
							RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.red));
							RegisterRePassword_Message.setText("�榡���~�A�Э��s��J!");
						}
					}
					else //��쬰��
					{
						RegisterRePassword_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterRePassword_Message.setText("��쬰��!");
					}
					
				}
			}
		});
		//�W�r�榡�T�{
		Register_NameText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String NameTXT = Register_NameText.getText().toString();
				if(!hasFocus){
					if(NameTXT.length() != 0){
						if(myValid.checkIllegal(NameTXT))
						{
							RegisterName_Message.setTextColor(getResources().getColor(R.color.green));
							RegisterName_Message.setText("OK!");
						}
						else
						{
							RegisterName_Message.setTextColor(getResources().getColor(R.color.red));
							RegisterName_Message.setText("�榡���~�A�Э��s��J");
							Register_NameText.setText("");
						}
					}
					else{
						RegisterName_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterName_Message.setText("��쬰��");
					}
					
				}
			}
		});
		//�ͤ����榡�T�{
		Register_AgeText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String AgeTXT = Register_AgeText.getText().toString();
				if(!hasFocus){
					if(AgeTXT.length() != 0)
					{
						if(myValid.checkDate(AgeTXT))//�榡�����T��
						{
							RegisterAge_Message.setTextColor(getResources().getColor(R.color.green));
							RegisterAge_Message.setText("OK!");
						}
						else //�榡�D���T
						{
							RegisterAge_Message.setTextColor(getResources().getColor(R.color.red));
							RegisterAge_Message.setText("�榡���~");
							Register_AgeText.setText("");
						}
					}
					else
					{
						RegisterAge_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterAge_Message.setText("��쬰��!");
					}
				}
			}
		});
		//EMAIL�榡�T�{
		Register_EMailText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String EMailTXT = Register_EMailText.getText().toString();
				if(!hasFocus){
					if(EMailTXT.length() != 0)
					{
						if(myValid.checkEmail(EMailTXT))//�榡�����T��
						{
							RegisterEMail_Message.setTextColor(getResources().getColor(R.color.green));
							RegisterEMail_Message.setText("OK!");
						}
						else //�榡�D���T
						{
							RegisterEMail_Message.setTextColor(getResources().getColor(R.color.red));
							RegisterEMail_Message.setText("�榡���~");
							Register_EMailText.setText("");
						}
					}
					else
					{
						RegisterEMail_Message.setTextColor(getResources().getColor(R.color.red));
						RegisterEMail_Message.setText("��쬰��!");
					}
				}
			}
		});

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		getActionBar().setTitle("���U����");
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
		else if(id == R.id.register_complete){
			String AccountText = Register_AccountText.getText().toString();
			String PasswordText = Register_PasswordText.getText().toString();
			String RePasswordText = Register_RePasswordText.getText().toString();
			String AgeText = Register_AgeText.getText().toString();;
			String EMailText = Register_EMailText.getText().toString();
			String NameText = Register_NameText.getText().toString();
			
			if
			(  AccountText.length()!=0 && 
			   PasswordText.length()!=0 &&	   
			   RePasswordText.length()!=0 &&
			   AgeText.length()!=0 &&	   
			   EMailText.length()!=0 &&
			   NameText.length()!=0 &&   
			   myValid.checkNumberAndEng(AccountText) &&
			   myValid.checkNumberAndEng(PasswordText) &&   
			   myValid.checkNumberAndEng(RePasswordText) &&
			   PasswordText.equals(RePasswordText)&&   
			   myValid.checkIllegal(NameText)&& 
			   myValid.checkDate(AgeText) && 
			   myValid.checkEmail(EMailText) 
			)
			{
				if(user.checkUserAccountIsUnique(AccountText))
				{
					user.addUser(AccountText, RePasswordText, NameText, AgeText, EMailText, "user");
					Toast.makeText(getApplicationContext(), "���U���\!", 10).show();
					this.finish();
					return true;
				}
				else{
					Toast.makeText(getApplicationContext(), "�b�����ơA�Э��s��J", 10).show();
				}
				
			}
			else{
				Toast.makeText(getApplicationContext(), "���|�������A�Э��s��J", 10).show();
			}
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    System.gc();
	}
}