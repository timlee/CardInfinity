package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.cardinfinity.R;

public class ImageUtil {
	
	public static void saveImage(Context context ,String path ,Bitmap image , String imageName) throws IOException
	{
		
		FileOutputStream outStream;
		//使用者未上傳名片
		if(image.sameAs(BitmapFactory.decodeResource(context.getResources(),R.drawable.man)))
		{
			outStream = new FileOutputStream(new File(path , "default_image.jpg"));
	        image.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
	        /* 100 to keep full quality of the image */
	        outStream.flush();
	        outStream.close();
	        return;
		}
		File save = new File(path, imageName);
		outStream = new FileOutputStream(save);
		image.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
		/* 100 to keep full quality of the image */
		outStream.flush();
		outStream.close();
	}
	
	public static Bitmap decodeFile(File f) 
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
	        int scale = 0;
	        /*while(o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) 
	        {
	            scale *= 2;
	        }*/
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
