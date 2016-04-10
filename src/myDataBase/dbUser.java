package myDataBase;


import java.util.ArrayList;

import util.inputValidation;
import member.user;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class dbUser {
	
	private  SQLiteDatabase db;
	
	public dbUser(myDB database){
		db = database.db;
	}
	//確認資料庫是否開啟
	private boolean checkDB(){
		if(db.isOpen() == true){
			return true;
		}
		else{
			System.out.println("Database not opened");
			return false;
		}
	}
	//新增使用者
	public boolean addUser(String account , String password , String name , String age ,String email, String type)
	{
		if(checkDB())
		{
			String SQL = "INSERT OR IGNORE INTO user(account,password,name,age,email,type)"
					+ " VALUES(" + "'" + account + "'" +","+
								   "'" + password + "'" +","+ 
					               "'" + name + "'" +","+
								   "'" + age + "'" +","+ 					 
					               "'" +email + "'" +","+ 
								   "'" + type +"'" + 
					               ")";
			db.execSQL(SQL);
			return true;
		}
		else
		{
			return true;
		}
	}
	//藉由id取得使用者
	public user getUserByID(String id){
		if(checkDB())
		{
				String where = "user_id" + "=" + id; //查詢sql句子
				user result = null;
				Cursor cursor = db.query
						("user",null,where,null,null,null,null,null);
				
				
				if(cursor.moveToNext())//有資料時
				{
					result = setUser(cursor);
					cursor.close();
					return result;
				}
				else//沒有資料時
				{
					System.out.println("Data not found");
					cursor.close();
					return null;
				}
		}
		else
		{
			return null;
		}
	}
	//藉由ID刪除使用者
	public boolean deleteByID(String id){
		if(checkDB()){
			if(getUserByID(id) != null)
			{
				String where = "user_id" + "=" + id;
				db.delete("user", where, null);
				return true;
			}
			else{
				return false;
			}
		}
		else
		{
			return false;
		}

	}
	//更新使用者資料
	public boolean updateByID(String id , user updateUser){
		if(checkDB())
		{
			String where = "user_id" + "=" + id;
			ContentValues cv = new ContentValues();
			cv.put("user_id", updateUser.getID());
			cv.put("name", updateUser.getName());
			cv.put("age", updateUser.getAge());
			cv.put("email", updateUser.getEmail());
			cv.put("account", updateUser.getAccount());
			cv.put("password", updateUser.getPassword());
			cv.put("type", updateUser.getType());
			db.update("user", cv, where, null);
			return true;
		}
		else
		{
			return false;
		}
	}
	//取得所有使用者
	public ArrayList<user> getAllUser(){
		if(checkDB())
		{
			ArrayList<user> result = new ArrayList<user>();
			Cursor cursor = db.query("user", null, null, null, null, null, null);
			
			while(cursor.moveToNext())
			{
				result.add(setUser(cursor));
			}
			
			cursor.close();
			return result;
		}
		else
		{
			return null;
		}
	}
	//取得使用者數量
	public int getUserCount(){
		if(checkDB())
		{
			int result = 0;
			Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + "user", null);
			if(cursor.moveToNext()){
				result = cursor.getInt(0);
			}
			else{
				System.out.println("User not found");
				return -1;
			}
			cursor.close();
			return result;
		}
		else
		{
			return -1;
		}
		
	}
	//使用者是否登入成功
	public user checkUser(String account , String password){
		if(checkDB())
		{
			String where = "account = ? AND password = ?";
			String[] columns = {account , password};
			Cursor result = db.query("user",null ,where,columns , null, null, null);
			if(result.getCount() != 0 && result.getCount() == 1)
			{
				return setUser(result);
			}
			else
			{
				System.out.println("user not found");
				return null;
			}
		}
		else
		{
			System.out.println("database not opened");
			return null;
		}
		
	}
	//使用者是否重複
	public boolean checkUserAccountIsUnique(String account){
		
		String where = "account = ? ";
		String[] columns = {account};
		Cursor result = db.query("user",null ,where,columns , null, null, null);
		
		if(result.getCount() == 0){
			return true;
		}
		else{
			return false;
		}
	}
	//使用者忘記密碼
	public String getForgetPassword(String account , String name , String email){
		String where = "account = ? AND name = ? AND email =?";
		String[] columns = {account , name , email};
		Cursor result = db.query("user",null ,where,columns , null, null, null);
		if(result.getCount() == 1) //找到使用者
		{
			user usr = setUser(result);
			return usr.getPassword();
		}
		else{
			return "找不到使用者，請重新輸入";
		}
		
	}
	//封裝使用者
	private user setUser(Cursor result)
	{
		result.moveToFirst();
		user temp = new user();
		temp.setID(result.getInt(0));
		temp.setName(result.getString(1));
		temp.setAge(result.getString(2));
		temp.setEmail(result.getString(3));
		temp.setAccount(result.getString(4));
		temp.setPassword(result.getString(5));
		temp.setType(result.getString(6));
		
		return temp;
		
	}
}
