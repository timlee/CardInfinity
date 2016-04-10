package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

public class NFCUtil
{
	Context context;
	public NFCUtil(Context context)
	{
		this.context = context;
	}
	
	public void connectAP(String apName)
	{
		WifiConfiguration conf = new WifiConfiguration();
	    conf.SSID =apName;
	    
	    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
	    wifiManager.addNetwork(conf);

	    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
	    for( WifiConfiguration i : list )
	    {
	        if(i.SSID != null && i.SSID.equals(apName))
	        {
	            try 
	            {
	                //wifiManager.disconnect();
	            	wifiManager.enableNetwork(i.networkId, true);
	                System.out.print("i.networkId " + i.networkId + "\n");
	                wifiManager.reconnect();               
	                break;
	            }
	            catch (Exception e) 
	            {
	                e.printStackTrace();
	            }

	        }           
	    }
	}
	
	public void turnOnOffHotspot(boolean isTurnToOn,String apName) 
    {
		 WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		    if(wifiManager.isWifiEnabled())
		    {
		        wifiManager.setWifiEnabled(false);          
		    }       
		    Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();   //Get all declared methods in WifiManager class     
		    boolean methodFound=false;
		    for(Method method: wmMethods)
		    {
		        if(method.getName().equals("setWifiApEnabled"))
		        {
		            methodFound=true;
		            WifiConfiguration netConfig = new WifiConfiguration();
		            netConfig.SSID = apName;
		            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

		            try {
		                boolean apstatus=(Boolean) method.invoke(wifiManager, netConfig,true);          
		                for (Method isWifiApEnabledmethod: wmMethods){
		                    if(isWifiApEnabledmethod.getName().equals("isWifiApEnabled"))
		                    {
		                        while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager))
		                        {
		                        };
		                        for(Method method1: wmMethods)
		                        {
		                            if(method1.getName().equals("getWifiApState"))
		                            {
		                                int apstate;
		                                apstate=(Integer)method1.invoke(wifiManager);
		                            }
		                        }
		                    }
		                }
		                if(apstatus)
		                {
		                    System.out.println("AP SUCCESSdddd");  

		                }
		                else
		                {
		                    System.out.println("AP FAILED");   

		                }

		            } 
		            catch (IllegalArgumentException e) 
		            {
		                e.printStackTrace();
		            } 
		            catch (IllegalAccessException e) 
		            {
		                e.printStackTrace();
		            } 
		            catch (InvocationTargetException e) 
		            {
		                e.printStackTrace();
		            }
		        }      
		    }
   }
	
	
	public String getDeviceIpAddress() 
	{
		String ip="";
		try 
		{
			//Loop through all the network interface devices
			for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements();) 
			{
				NetworkInterface networkInterface = enumeration.nextElement();
				//Loop through all the ip addresses of the network interface devices
			    for (Enumeration<InetAddress> enumerationIpAddr = networkInterface.getInetAddresses(); enumerationIpAddr.hasMoreElements();)
			    {
			    	InetAddress inetAddress = enumerationIpAddr.nextElement();
			    	//Filter out loopback address and other irrelevant ip addresses
			    	System.out.println("yoyo "+inetAddress.getHostAddress());
			    	if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) 
			    	{
				      //Print the device ip address in to the text view
				      ip=inetAddress.getHostAddress();
				      
			    	}
			    }
			}
		} 
		catch (SocketException e) 
		{
			Log.e("ERROR:", e.toString());
		}
		return ip;
	}
	
	
	public void checkFolder()
	{
		File f = new File(Environment.getExternalStorageDirectory().toString()+"/CardInfinite/");
		boolean success = true;
		if(!f.exists())
		{	
			success = f.mkdir();
			if (success) 
			{
			    // Do something on success
				System.out.println("Create CardInfinite Success!!");
			} 
			else 
			{
			    // Do something else on failure 
				System.out.println("Wrong!!");
			}
		}
	}
	
	
	
	public Bitmap decodeFile(File f) 
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
	        int scale = 10;
	        while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) 
	        {
	            scale *= 2;
	        }
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
}
