package com.example.cardinfinity;

import java.util.UUID;
import myDataBase.dbCard;
import myDataBase.myDB;
import socket.WifiBroadcastReceiver;
import util.NFCUtil;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;

public class NfcReceiveActivity extends Activity implements CreateNdefMessageCallback,OnNdefPushCompleteCallback{
	
	NfcAdapter nfcAdapter;  
	String inMsg,clientF;
	public BroadcastReceiver broadcastReceiver;
	static final int SocketServerPORT = 1737;
	private FileObserver mFileObserver;
	public static String imagePath = Environment.getExternalStorageDirectory().toString()+"/CardInfinite/cardImage/";	
	public String uniqueID;
	String cardID;
	NFCUtil nfcutil;
	String apName;
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcreceive_layout);
        
        getActionBar().setTitle("等待接收頁面");
        
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
            if(null == mFileObserver) 
            {
                mFileObserver = new SDCardFileObserver(imagePath);
                mFileObserver.startWatching(); //start detect
            }
        }

        
     
    }
	
	class SDCardFileObserver extends FileObserver
    {
        
        public SDCardFileObserver(String path, int mask) 
        {
            super(path, mask);
        }

        public SDCardFileObserver(String path) 
        {
            super(path);
        }

        @Override
        public void onEvent(int event, String path)
        {

            event &= FileObserver.ALL_EVENTS;
            switch (event) {

            	
            case FileObserver.CLOSE_NOWRITE:
            	System.out.println("2");
            	break;
            	
            case FileObserver.CLOSE_WRITE:
            	Intent intent = new Intent(NfcReceiveActivity.this,CardInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CardID", uniqueID);
                intent.putExtras(bundle);
                startActivity(intent);
                NfcReceiveActivity.this.finish();
            	break;
            case FileObserver.MODIFY:
            	System.out.println("4");
            	break;
            	
            case FileObserver.MOVED_TO:
            	System.out.println("5");
            	break;            
            }
        }
        
    }
	
	
	@Override
    protected void onResume()
    {

    	super.onResume();
    	
    	Intent intent = getIntent();
		String action = intent.getAction();
		if(action ==null)
		{
			action ="test";
		}
		System.out.println("at recieve's onresume");
		System.out.println("mt action is "+action);
		
    	try{
			if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
			{
				
				Parcelable[] parcelables =intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				NdefMessage inNdefMessage = (NdefMessage)parcelables[0];
				NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
				NdefRecord NdefRecord_0 = inNdefRecords[0];
				inMsg = new String(NdefRecord_0.getPayload());
				System.out.println("inMsg in onResume = "+inMsg);
				processIntent(getIntent());
				
				if(broadcastReceiver!=null)
				{
					unregisterReceiver(broadcastReceiver);
				}
				broadcastReceiver = new WifiBroadcastReceiver(inMsg,SocketServerPORT,uniqueID,this,apName = "\""+ apName+"\"");
		        IntentFilter intentFilter = new IntentFilter();
		        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));//--
				
		        nfcutil = new NFCUtil(this);
		        nfcutil.connectAP(apName);
		        //------------------------------------------------------------------------
		        
		        
		        
		        
		        //-----------------------------------------------------------------------
		        
		        
		        Toast.makeText(this, "insert success!", 10).show();

			}
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    }
	
	@Override
    protected void onPause()
    {
		super.onPause();
		System.out.println("onpause");
    }
	
	@Override
    protected void onDestroy()
    {
		super.onDestroy();
		System.out.println(this.getClass().getName()+"ondestroy at recieve");
		System.out.println("on destroy close");
		if(broadcastReceiver!=null)
		{
			unregisterReceiver(broadcastReceiver);
		}
		System.out.println("broadcast close ");

    }
	
	void processIntent(Intent intent) 
    {	
    	
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        
        //String IP = new String(msg.getRecords()[0].getPayload());//get ip
        //clientF = new String(msg.getRecords()[1].getPayload())+".jpg";
        //cardID = new String(msg.getRecords()[12].getPayload());
        uniqueID = UUID.randomUUID().toString()+".jpg";
        String cardName = new String(msg.getRecords()[2].getPayload());
        String cardCompany = new String(msg.getRecords()[7].getPayload());
        String cardDepartment = new String(msg.getRecords()[11].getPayload());
        String cardJobTitle = new String(msg.getRecords()[8].getPayload());
        String cardEmail = new String(msg.getRecords()[3].getPayload());
        String cardTel = new String(msg.getRecords()[4].getPayload());
        String cardMobilePhone = new String(msg.getRecords()[5].getPayload());
        String cardAddress = new String(msg.getRecords()[9].getPayload());
        String cardFax = new String(msg.getRecords()[6].getPayload());
        //String cardImage = new String(msg.getRecords()[2].getPayload());
        String cardUrl = new String(msg.getRecords()[10].getPayload());
        dbCard addCard= new dbCard(new myDB(this));
        apName = new String (msg.getRecords()[13].getPayload());
        addCard.addCard(uniqueID,cardName, cardCompany,cardDepartment ,cardJobTitle, cardEmail, cardTel, cardMobilePhone, cardAddress, cardFax,uniqueID, cardUrl, "Businesscard");
        

    }
	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		// TODO Auto-generated method stub
		return null;
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
