package socket;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

import Encryption.AESEncryption;
import Encryption.RSAEncryption;
import android.os.Environment;



public class receiveImageKey implements Runnable{
	
	InputStream is;
	byte[] privateKey;
	String fName;
	
	public receiveImageKey(InputStream is , byte[] privateKey,String fName)
	{
		this.is = is;
		this.privateKey = privateKey;
		this.fName = fName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			System.out.println("test1 ");
		   File file = new File(Environment.getExternalStorageDirectory()+"/CardInfinite/cardImage/",fName);
		   BufferedInputStream ois = new BufferedInputStream(is);
		   byte[] bytes = new byte[500000];
		   byte[] data=new byte[0];
		   FileOutputStream fos = null;
		   try 
		   {
			   System.out.println("test2 ");
			   int TotalCount=0;
			   int Count=ois.read(bytes);
			   System.out.println("Count = " + Count);
			   TotalCount+=Count;
			   data = MergeTwoArray(data,bytes,Count);			   
			   int value = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
			   int encryptImageLength = ((bytes[4] & 0xFF) << 24) | ((bytes[5] & 0xFF) << 16) | ((bytes[6] & 0xFF) << 8) | (bytes[7] & 0xFF);
			   
			   System.out.println("data length = "+ data.length);
			   while(TotalCount<value)
			   {			   
				   System.out.println("TotalCount = "+ TotalCount);
				   Count=ois.read(bytes);
				   TotalCount+=Count;
				   data = MergeTwoArray(data,bytes,Count);			   
			   }
			   System.out.println("TotalCount = "+ TotalCount);
			  
			   int encryptAESKeyLength = data.length - 8 - encryptImageLength;
			   byte[] encryptImage = new byte[encryptImageLength];
			   byte[] encryptAESKey = new byte[encryptAESKeyLength];
			   for(int i =0 ; i < encryptImage.length; i++)
			   {
				   encryptImage[i] = data[i+8];
			   }
			   int offset = encryptImageLength + 8;
			   for(int i =0 ; i<encryptAESKeyLength ;i++)
			   {
				   encryptAESKey[i] = data[i+offset];
			   }
			   
			   RSAEncryption rsa = new RSAEncryption();
			   AESEncryption aes = new AESEncryption();
			   
			   
			  byte[] aesKey = rsa.decrypt(encryptAESKey, privateKey);
			  byte[] decryptImage = aes.decrypt(encryptImage, aesKey);
			   
			   
			 
			   fos = new FileOutputStream(file);
			   fos.write(decryptImage);
			   
			   
		   } 
		   catch (Exception e) 
		   {
			   System.out.println(e.toString());
			   e.printStackTrace();
		   } 
		   finally 
		   {
			   if(fos!=null)
			   {
				   fos.close();
			   }
	     
		   }

	   } 
	   catch (Exception e) 
	   {
		   e.printStackTrace();	    
	   } 
	}

	
	
	
	public byte[] MergeTwoArray(byte[] array1,byte[] array2,int vaildDataLength)
	{
		  byte[] array1and2 = new byte[array1.length + vaildDataLength];
		  System.arraycopy(array1, 0, array1and2, 0, array1.length);
		  System.arraycopy(array2, 0, array1and2, array1.length,vaildDataLength);
		  return array1and2;
	}
}
