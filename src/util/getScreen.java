package util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class getScreen {
	private DisplayMetrics displaymetrics = null;
	public getScreen(Activity activity){
		displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	}
	public int getScreenHeight(){
		
		return displaymetrics.heightPixels;
		
	}
	public int getScreenWidth(){
		return displaymetrics.widthPixels;
	}
}
