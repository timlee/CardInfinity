package util;

import java.io.FileOutputStream;
import java.io.IOException;

import android.webkit.MimeTypeMap;

public class fileHelper {
	
	public static String getMimeType(String url) 
	{
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
	public static void toImage(byte[] data , String path) throws IOException
	{
		if(data != null)
		{
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(data);
		}
		else{
			return;
		}
	}
}
