package socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import Encryption.AESEncryption;
import Encryption.RSAEncryption;
import android.os.Environment;

public class receiveKey extends Thread{
	
	InputStream is;
	Socket socket;
	String path;
	
	public receiveKey(InputStream is , Socket socket,String path)
	{
		this.is = is;
		this.socket = socket;
		this.path = path;
	}
	public receiveKey(InputStream is)
	{
		this.is = is;
	}
	
	
	@Override
	public void run() 
	{
		
		   BufferedInputStream ois = new BufferedInputStream(is);
		   byte[] bytes = new byte[500000];
		   byte[] data=new byte[0];
		   try 
		   {	
			   int TotalCount=0;
			   int Count=ois.read(bytes);
			   TotalCount+=Count;
			   data = MergeTwoArray(data,bytes,Count);			   
			   int value = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
			   
			   while(TotalCount<value)
			   {			   
				   Count=ois.read(bytes);
				   TotalCount+=Count;
				   data = MergeTwoArray(data,bytes,Count);			   
			   }
			   
			   byte[] publicKey = new byte[value-4];		   
			   for(int i=0;i<publicKey.length;i++)
			   {
				   publicKey[i]=data[i+4];		   
			   }		 
			   
			   RSAEncryption  rsa = new RSAEncryption();
			   AESEncryption aes = new AESEncryption();
			   
			   
			   System.out.println("path = "+ path);
			   
			   byte[] image = getImageByte(path);
			   byte[] encryptImage = aes.encrypt(image);
			   byte[] aesKey = aes.getKeyByte();
			   byte[] encryptAESKey = rsa.encrypt(aesKey, publicKey);
			   
			   
			   

			   //°e¦^¥h
			   transferImageKey(socket , encryptImage , encryptAESKey);
		   } 
		   catch (Exception e) 
		   {
			   System.out.println(e.toString());
			   e.printStackTrace();
		   } 
	}
	
	public byte[] getImageByte(String path) throws IOException
	{
		File file = new File(path); 
		byte[] bytes = new byte[((int)file.length())];
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		bis.read(bytes,0, (int)file.length());
		return bytes;
	}

	
	public byte[] MergeTwoArray(byte[] array1,byte[] array2,int vaildDataLength)
	{
		  byte[] array1and2 = new byte[array1.length + vaildDataLength];
		  System.arraycopy(array1, 0, array1and2, 0, array1.length);
		  System.arraycopy(array2, 0, array1and2, array1.length,vaildDataLength);
		  return array1and2;
	}
	
	public void transferImageKey(Socket socket , byte[] encryptImage , byte[] encryptAESKey) throws IOException
	{
		byte[] stringLength = intToByteArray(encryptImage.length + encryptAESKey.length +8);
		byte[] transfer = new byte[0];
		
		
		transfer = MergeTwoArray(stringLength , intToByteArray(encryptImage.length));
		transfer = MergeTwoArray( transfer,encryptImage);
		transfer = MergeTwoArray(transfer , encryptAESKey);
		
		BufferedOutputStream oos = new BufferedOutputStream(socket.getOutputStream()); 
	    oos.write(transfer);
	    oos.flush();
	}
	public byte[] MergeTwoArray(byte[] array1,byte[] array2)
	{
		  byte[] array1and2 = new byte[array1.length + array2.length];
		  System.arraycopy(array1, 0, array1and2, 0, array1.length);
		  System.arraycopy(array2, 0, array1and2, array1.length,array2.length);
		  return array1and2;
	}
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}

}
