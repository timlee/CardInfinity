package socket;
import java.util.List;

import util.NFCUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class WifiBroadcastReceiver extends BroadcastReceiver{
	
	String dstAddress;
	int dstPort;
	String fName;
	Context mContext;
	public static boolean stateCheck = false;
	String ssid="";
	public NFCUtil util;
	String apName;
	
	public WifiBroadcastReceiver(String address, int port,String fileName,Context mContext,String apName)
	{
		this.dstAddress = address;
		this.dstPort = port;
		this.fName = fileName;
		this.mContext = mContext;
		this.apName = apName;
		
		
	}

    @Override
    public void onReceive(Context context, Intent intent) 
    {	

    	NFCUtil util = new NFCUtil(context);
    	util.connectAP(apName);
    	System.out.println("apName "+apName);
    	//-----------------------------
    	
    	NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
    	if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
    	{
    		ConnectivityManager myConnManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    	    NetworkInfo myNetworkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	    WifiManager myWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	    WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
    	    System.out.println("connetx "+myNetworkInfo.isConnected());
    	    if (myNetworkInfo.isConnected() && myWifiInfo.getSSID().equals(apName))
    	    {
    	    	final ClientRxThread clientRxThread =new ClientRxThread(dstAddress,dstPort,fName);
            	final Handler handler = new Handler();
				System.out.println("IN HANDER");

    	        handler.postDelayed(new Runnable() 
    	        {
    	        	@Override
    	        	public void run() 
    	        	{
    	        		//Do something after 3000ms      			
    	        		clientRxThread.start();  	        			
    	        	}
    	        }, 3000);
    	    	
    	    	
    	    }
    	}
    	  
    	//-----------------------------
    	
    }

}