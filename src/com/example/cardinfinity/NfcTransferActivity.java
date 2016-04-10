package com.example.cardinfinity;

import java.io.File;
import java.net.ServerSocket;
import java.util.Random;

import member.card;
import myDataBase.dbCard;
import myDataBase.myDB;
import socket.ServerSocketThread;
import util.NFCUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;





public class NfcTransferActivity extends Activity implements CreateNdefMessageCallback,OnNdefPushCompleteCallback{
	
	
	Intent mIntent;
    NfcAdapter nfcAdapter;  
    TextView txtUriPath,txtRealPath;
    ImageView imageView;
    EditText textOut,textOut2;
    public String Path,target;
    public String fName;
	static final int SocketServerPORT = 1737;
	boolean serverCheck = false;
	ServerSocket serverSocket;
	ServerSocketThread serverSocketThread,serverSocketThread1;
	String fileName,inMsg,clientF;
    public NFCUtil util;
    card card;
    int cardID;
    dbCard cardHelper;
    Bitmap myBitMap;
    ImageView NFCCardInfo_CardImage;
	TextView NFCCardInfo_Name;
	TextView NFCCardInfo_EmailPerson;
	TextView NFCCardInfo_Tel;
	TextView NFCCardInfo_Mobile;
	TextView NFCCardInfo_Fax;
	TextView NFCCardInfo_Address;
	TextView NFCCardInfo_Company;
	TextView NFCCardInfo_Department;
	TextView NFCCardInfo_Job;
	TextView NFCCardInfo_URL;
	
	ImageView NFCCardInfo_TelImage;
	ImageView NFCCardInfo_MobileImage;
	ImageView NFCCardInfo_WebImage;
	String apName ="AP";
	String android_id ;
	
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_layout);
        android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
        getActionBar().setTitle("¶Ç¿é­¶­±");
        
        Bundle bundle = getIntent().getExtras();
        
        if(bundle!=null)
        {
        	card = (member.card) bundle.get("card"); 
        	Toast.makeText(this, card.getCardName(), 10).show();

        }
        
        this.initLayout();
        System.out.println("initLayout");
		this.initModule();
		System.out.println("initModule");
		System.out.println("android id = "+android_id);
		//System.out.println("card image "+ card.Card_Image);
		initData(card);
		util = new NFCUtil(this);
		util.turnOnOffHotspot(true,android_id);
		PackageManager pm = this.getPackageManager();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // Check whether NFC is available on device     
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) 
        {
            // NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.",Toast.LENGTH_SHORT).show();           
        } 
        // Check whether device is running Android 4.1 or higher 
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) 
        {
            // Android Beam feature is not supported.            
            Toast.makeText(this, "Android Beam is not supported.",Toast.LENGTH_SHORT).show();                     
        }         
        else 
        {
            // NFC and Android Beam file transfer is supported. 
            Toast.makeText(this, "Android Beam is supported on your device.",Toast.LENGTH_SHORT).show();      
            nfcAdapter.setNdefPushMessageCallback(this, this);
            // Register callback to listen for message-sent success
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
            /*if(null == mFileObserver) 
            {
                mFileObserver = new SDCardFileObserver(Environment.getExternalStorageDirectory().toString()+"/CardInfinite");
                mFileObserver.startWatching(); //start detect
            }*/
        }
        
     
    }
	@Override
    protected void onResume()
    {

    	super.onResume();
    	System.out.println("TRAnsfer ONRESUME");
    	
    }

	
	
	
	private void initModule()
	{
		cardHelper = new dbCard(new myDB(this));
	}
    
    private void initData(card card)
	{
		File image = new File(card.getCardImage());
		if(image.exists()){
			myBitMap = BitmapFactory.decodeFile(image.getAbsolutePath());
			NFCCardInfo_CardImage.setImageBitmap(myBitMap);
		}
		else{
			myBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
			NFCCardInfo_CardImage.setImageBitmap(myBitMap);
		}
		NFCCardInfo_Name.setText(card.getCardName());
		NFCCardInfo_EmailPerson.setText(card.getCardEmail());
		NFCCardInfo_Tel.setText(card.getCardTel());
		NFCCardInfo_Mobile.setText(card.getCardMobile());
		NFCCardInfo_Fax.setText(card.getCardFax());
		NFCCardInfo_Address.setText(card.getCardAddress());
		NFCCardInfo_Company.setText(card.getCardCompany());
		NFCCardInfo_Department.setText(card.getCardDepartment());
		NFCCardInfo_Job.setText(card.getCardJobTitle());
		NFCCardInfo_URL.setText(card.getCardUrl());
		
	}
    private void initLayout()
	{
		NFCCardInfo_CardImage =(ImageView) findViewById(R.id.NFCCardInfo_CardImage);
		NFCCardInfo_Name = (TextView) findViewById(R.id.NFCCardInfo_Name);
		NFCCardInfo_EmailPerson = (TextView) findViewById(R.id.NFCCardInfo_EmailPerson);
		NFCCardInfo_Tel = (TextView) findViewById(R.id.NFCCardInfo_Tel);
		NFCCardInfo_Mobile = (TextView) findViewById(R.id.NFCCardInfo_Mobile);
		NFCCardInfo_Fax = (TextView) findViewById(R.id.NFCCardInfo_Fax);
		NFCCardInfo_Address = (TextView) findViewById(R.id.NFCCardInfo_Address);
		NFCCardInfo_Company = (TextView) findViewById(R.id.NFCCardInfo_Company);
		NFCCardInfo_Department = (TextView) findViewById(R.id.NFCCardInfo_Department);
		NFCCardInfo_Job = (TextView) findViewById(R.id.NFCCardInfo_Job);
		NFCCardInfo_URL = (TextView) findViewById(R.id.NFCCardInfo_URL);
		NFCCardInfo_TelImage =  (ImageView) findViewById(R.id.NFCCardInfo_TelImage);
		NFCCardInfo_MobileImage = (ImageView) findViewById(R.id.NFCCardInfo_MobileImage);
		NFCCardInfo_WebImage = (ImageView) findViewById(R.id.NFCCardInfo_WebImage);
	}
	
	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		
		serverSocketThread = new ServerSocketThread(card.getCardImage());
        serverSocketThread.start();
		System.out.println("NDEF CREAT!!!!!!!!!!!!");
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		// TODO Auto-generated method stub
		
		
		
		String serverIp ="";
		
		serverIp=util.getDeviceIpAddress();
				
		
		System.out.println("serverip is : "+serverIp);
		byte[] bytesOut = serverIp.getBytes();	
		
		NdefRecord ndefIP = new NdefRecord( NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[] {},bytesOut);//transfer device ip  0
		NdefRecord image=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardImage().getBytes());//1
		System.out.println("card image is : "+card.getCardImage());
		NdefRecord cardName=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardName().getBytes());//2
		NdefRecord cardEmail=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardEmail().getBytes());//3
		NdefRecord cardTel=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardTel().getBytes());//4
		NdefRecord cardMobile=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardMobile().getBytes());//5
		NdefRecord cardFax=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardFax().getBytes());//6
		NdefRecord cardCompany=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardCompany().getBytes());//7
		NdefRecord cardJob=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardJobTitle().getBytes());//8
		System.out.println("name "+card.getCardName());
		NdefRecord cardAddress=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardAddress().getBytes());//9
		NdefRecord cardUrl=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardUrl().getBytes());		//10
		NdefRecord cardDepartment=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardDepartment().getBytes());		//11
		NdefRecord cardID=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},card.getCardID().getBytes());//12
		NdefRecord AP=new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),new byte[]{},android_id.getBytes());//13
		NdefMessage ndefMessageout=new NdefMessage(new NdefRecord[]{ndefIP,image,cardName,cardEmail,cardTel,cardMobile,cardFax,cardCompany,cardJob,cardAddress,cardUrl,cardDepartment,cardID,AP});
		return ndefMessageout;
		
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
		return super.onOptionsItemSelected(item);
	}

}
