package sharedPref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class sessionManager {
	
	SharedPreferences pref;
	Editor editor;
	Context _context;
	
	int PRIVATE_MODE = 0;
	//sharedPreference file name
	private static String prefName = "LoginSession";
	private static String isLoginTag = "isLogin";
	private static String userNameTag = "userName";
	
	
	public sessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(prefName, PRIVATE_MODE); 
		editor = pref.edit();
	}
	public void setLogin(boolean isLogin){
		editor.putBoolean(isLoginTag, isLogin);
		//commit change
		editor.commit();
	}
	public void setUserName(String name){
		editor.putString(userNameTag, name);
		//commit change
		editor.commit();
	}
	public boolean isLogin(){
		return pref.getBoolean(isLoginTag, false);
	}
	public String getUserName(){
		return pref.getString(userNameTag, "not exist");
	}
	public void logOut(){
		setLogin(false);
		setUserName("");
	}
}
