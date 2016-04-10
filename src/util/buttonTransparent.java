package util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class buttonTransparent implements OnTouchListener{
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN ){
			v.getBackground().setAlpha(128); // onfoucs 50% transparent
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			v.getBackground().setAlpha(255); // leave and back to original color
		}
		return false;
	}
}