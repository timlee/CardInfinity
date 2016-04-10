package socket;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import Encryption.RSAEncryption;
import android.os.Environment;

public class ClientRxThread extends Thread 
{
	  String dstAddress;
	  int dstPort;
	  String fName;
	  RSAEncryption rsa;
	  byte[] rsaPublicKey , rsaPrivateKey;

	  public ClientRxThread(String address, int port,String fileName)
	  {
		  this.dstAddress = address;
		  this.dstPort = port;
		  this.fName = fileName;
	  }


	@Override
	public void run() 
	{
		  Socket socket = null;
	   try
	   {
		   System.out.println("inMsg in client = "+dstAddress);
		   System.out.println("inMsg  LENGTH in client = "+dstAddress.length());
		   if(dstAddress.length() == 0)
		   {
			   System.out.println("test1");
			   dstAddress="192.168.43.1";
		   }
		   socket = new Socket(dstAddress, dstPort);
		   socket.setSoTimeout(150000);	 
		   
		   
		   transferKey(socket);
		   System.out.println("transferKey");
		   this.sleep(1000);
			//¦¬
		   receiveImageKey rIK = new receiveImageKey(socket.getInputStream() , rsaPrivateKey , fName);
		   System.out.println("file name = "+ fName);
		   rIK.run();
		   System.out.println("i am running");
		   	       
	   } 
	   catch (Exception e) 
	   {
		   e.printStackTrace();	    
	   } 
	   
	}
	
	public void transferKey(Socket socket) throws IOException
	{
		
		 rsa = new RSAEncryption();
		 rsaPublicKey = rsa.getPublicKey();
		 rsaPrivateKey = rsa.getPrivateKey();
		 
		
		
		byte[] stringLength = intToByteArray(rsaPublicKey.length+4);
		byte[] transfer = new byte[0];
		
		transfer = MergeTwoArray(stringLength , rsaPublicKey);
		

		BufferedOutputStream oos = new BufferedOutputStream(socket.getOutputStream()); 
	    oos.write(transfer);
	    oos.flush();
	}
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	public byte[] MergeTwoArray(byte[] array1,byte[] array2)
	{
		  byte[] array1and2 = new byte[array1.length + array2.length];
		  System.arraycopy(array1, 0, array1and2, 0, array1.length);
		  System.arraycopy(array2, 0, array1and2, array1.length,array2.length);
		  return array1and2;
	}
}
