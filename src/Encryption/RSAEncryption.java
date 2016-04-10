package Encryption;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAEncryption {
	
	
	KeyPair myKeyPair;
	PublicKey publicKey;
	PrivateKey privateKey;
	
	
	public RSAEncryption()
	{
		myKeyPair =  generateKey("RSA" , 1024);
		this.publicKey =  myKeyPair.getPublic();
		this.privateKey = myKeyPair.getPrivate();
	}
	public KeyPair generateKey(String algorithm , int bit)
	{
		try 
		{
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(algorithm);
			SecureRandom random = new SecureRandom();
			random.setSeed("test".getBytes());
			keygen.initialize(bit, random);
			KeyPair generatedKeyPair = keygen.generateKeyPair();
			return generatedKeyPair;
		} catch (NoSuchAlgorithmException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public byte[] encryptKey (byte[] data , byte[] publicKey)
	{
	    try
	    {
	    	PublicKey pub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
	    	
	        // initialize the cipher with the user's public key
	    	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, pub );
	        return cipher.doFinal(data);
	    }
	    catch(Exception e )
	    {
	        System.out.println ( "exception encoding key: " + e.getMessage() );
	        e.printStackTrace();
	    }
	    return null;
	}
	public byte[] encrypt(String data)
	{
		byte[] temp = data.getBytes();
		return encrypt(temp);
	}
	public byte[] encrypt(Key key)
	{
		byte[] temp = key.getEncoded();
		return encrypt(temp);
	}
	public byte[] encrypt(byte[] data)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		    
		    int blockSize = 53;// java rsa加密一次只能處理小字串 若加密的資料過大會跳出exception
		    int writtenSize;
		    ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
		
			for (int readedBytes=0 ; readedBytes<data.length ; readedBytes+=blockSize) 
			{
				if(data.length > blockSize)
					writtenSize = blockSize;
				else{
					writtenSize = data.length - readedBytes;
					out.write(cipher.doFinal(data, readedBytes, writtenSize));
				}	
			}
			return out.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	    return null;
	}
	public byte[] encrypt(byte[] data , byte[] publicKey)
	{
		try
		{
			PublicKey pub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, pub);
		    
		    int blockSize = 53;
		    int writtenSize;
		    ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
		
			for (int readedBytes=0 ; readedBytes<data.length ; readedBytes+=blockSize) 
			{
				if(data.length > blockSize)
					writtenSize = blockSize;
				else{
					writtenSize = data.length - readedBytes;
					out.write(cipher.doFinal(data, readedBytes, writtenSize));
				}	
			}
			return out.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	    return null;
	}
	public byte[] decrypt(String data)
	{
		byte[] temp = data.getBytes();
		return decrypt(temp);
	}
	public byte[] decrypt(Key key)
	{
		byte[] temp = key.getEncoded();
		return decrypt(temp);
	}
	public byte[] decrypt(byte[] data)
	{
		int blockSize =128;
		try 
		{
				ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.DECRYPT_MODE, privateKey);

				int j = 0;
				int length = data.length - j*blockSize;
	            while ((length = data.length-j*blockSize) > 0) 
	            {
		            if(length > blockSize)
		            {
		            	length = blockSize;
		            }
	            
		            else
		            {
		            	length = data.length;
		            }
		            out.write(cipher.doFinal(data, j*blockSize, length));
	                ++j;
	            }
	            return out.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	public byte[] decrypt(byte[] data , byte[] privateKey)
	{
		int blockSize =128;
		try 
		{
				KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
				PrivateKey pri = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
				
				ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.DECRYPT_MODE, pri);

				int j = 0;
				int length = data.length - j*blockSize;
	            while ((length = data.length-j*blockSize) > 0) 
	            {
		            if(length > blockSize)
		            {
		            	length = blockSize;
		            }
	            
		            else
		            {
		            	length = data.length;
		            }
		            out.write(cipher.doFinal(data, j*blockSize, length));
	                ++j;
	            }
	            return out.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	public byte[] decryptAESKey(byte[] data , byte[] privateKey)
    {
        try
        {
        	KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
			PrivateKey pri = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
  
            // initialize the cipher...
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pri );

            // generate the aes key!
            return cipher.doFinal(data);
        }
        catch(Exception e)
        {
            System.out.println ( "exception decrypting the aes key: " 
                                                   + e.getMessage() );
            return null;
        }
    }
	public byte[] getPublicKey(){
		return this.publicKey.getEncoded();
	}
	public byte[] getPrivateKey(){
		return this.privateKey.getEncoded();
	}
}
