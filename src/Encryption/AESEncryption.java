package Encryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import util.fileHelper;



public class AESEncryption {
	
	private String ALGORITHM = "AES";
	private int KEY_LENGTH = 128;
	public static SecretKeySpec key;
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	
	static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);
	
	private Cipher cipher;
	
	public AESEncryption()
	{
		generateKey();
	}
	
	private void generateKey()
	{
		try
		{
			KeyGenerator keyG = KeyGenerator.getInstance(ALGORITHM);
			keyG.init(KEY_LENGTH);
			
			SecretKey secuK = keyG.generateKey();
			byte[] temp = secuK.getEncoded();
			key = new SecretKeySpec(temp, ALGORITHM);
			cipher = Cipher.getInstance(TRANSFORMATION);	
			
		} catch (NoSuchAlgorithmException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[] encrypt(File file) throws IOException
	{
		String mimeType = fileHelper.getMimeType(file.toURI().toString());
		if(!mimeType.substring(0, 5).equalsIgnoreCase("image"))
		{
			return null;
		}
		FileInputStream fis = new FileInputStream(file);
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try 
		 {
			 byte[] buf = new byte[1024];
       
			 for (int readNum; (readNum = fis.read(buf)) != -1;) 
			 {
                //Writes to this byte array output stream
				 bos.write(buf, 0, readNum); 
			 }
			 fis.close();
			 return encrypt(bos.toByteArray());
		 } 
		 catch (IOException ex)
		 {
           fis.close();
           bos.close();
           ex.printStackTrace();
		 }
        return null;
	}
	public byte[] encrypt(String data)
	{
		byte[] temp = data.getBytes();
		return encrypt(temp);
	}
	public byte[] encrypt(byte[] data)
	{
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, key , ivspec);
			return cipher.doFinal(data);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public byte[] encrypt(byte[] data , byte[] aesKey) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		SecretKeySpec temp =  new SecretKeySpec(aesKey, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, temp , ivspec);
		return cipher.doFinal(data);
	}
	
	public byte[] decrypt(byte[] data)
	{
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, key , ivspec);
			return cipher.doFinal(data);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return null;
	}
	public byte[] decrypt(byte[] data , byte[] aesKey) 
	{
		try
		{
			SecretKeySpec temp =  new SecretKeySpec(aesKey, "AES");
			cipher.init(Cipher.DECRYPT_MODE, temp , ivspec);
			return cipher.doFinal(data);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return null;
	}
	public byte[] getKeyByte(){
		return this.key.getEncoded();
	}
}
