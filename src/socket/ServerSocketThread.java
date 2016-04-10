package socket;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends Thread 
{
	
	
	ServerSocket serverSocket;
	ServerSocketThread serverSocketThread;
	static final int SocketServerPORT = 1737;
	public static String path;
	
	public ServerSocketThread(String path)
	{
		this.path = path;
	}
	
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	@Override
	public void run() 
	{
		 Socket socket = null;
		   
		 try 
		 {
			 serverSocket = new ServerSocket(SocketServerPORT);		 		 
			 while (true) 
			 {
				 socket = serverSocket.accept();
				 System.out.println("serversocket accecpt ");
				 FileTxThread fileTxThread = new FileTxThread(socket,path);
				 fileTxThread.start();
			 }
		 } 
		 catch (IOException e) 
		 {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 } 
		 
	 }

}


class FileTxThread extends Thread 
{
	Socket socket;
	String Path;
	FileTxThread(Socket socket,String s)
	{
		this.socket= socket;
		this.Path=s;
	}

	@Override
	public void run() 
	{ 
		try 
		{	
			receiveKey rK = new receiveKey(socket.getInputStream(),socket,Path);
			rK.run();
			
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
}
