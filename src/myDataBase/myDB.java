package myDataBase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDB extends SQLiteOpenHelper{
	
	private static final String _DB_NAME = "myDB.db";
	private static final int _DB_VERSION = 1; 
	//private static final String _TABLE_NAME = "user";
	
	private static final String[] UserSchemaNameList = dbUserSchema.UserSchemaNameList;
	private static final String[] UserSchemaTypeList = dbUserSchema.UserSchemaTypeList;
	private static final Integer[] UserSchemaLengthList = dbUserSchema.UserSchemaLengthList;
	
	private static final String[] CardSchemaNameList = dbCardSchema.CardSchemaNameList;
	private static final String[] CardSchemaTypeList = dbCardSchema.CardSchemaTypeList;
	private static final Integer[] CardSchemaLengthList = dbCardSchema.CardSchemaLengthList;
	
	
	private static final String[] OtherCardSchemaNameList = dbOtherCardSchema.OtherCardSchemaNameList;
	private static final String[] OtherCardSchemaTypeList = dbOtherCardSchema.OtherCardSchemaTypeList;
	private static final Integer[] OtherCardSchemaLengthList = dbOtherCardSchema.OtherCardSchemaLengthList;
	
	public static SQLiteDatabase db;
		
	public myDB(Context context) {
		super(context, _DB_NAME, null, _DB_VERSION); // pass parameter to SQLiteOpenHelper
	}
	public boolean openDB(){
		try{	
			db = this.getWritableDatabase();
			return true;
		}
		catch(Exception e){
			System.out.println(e.toString());
			return false;
		}
	}
	public boolean closeDB(){
		try{
			this.close();
			return true;
		}
		catch(Exception e){
			System.out.println(e.toString());
			return false;
		}
	}
	@Override
	public void onCreate(SQLiteDatabase db) // create table and scheama
	{
		createTable(db,"user" , UserSchemaNameList , UserSchemaTypeList , UserSchemaLengthList);
		createTable(db,"card" , CardSchemaNameList , CardSchemaTypeList , CardSchemaLengthList);
		createTable(db,"othercard" , OtherCardSchemaNameList , OtherCardSchemaTypeList , OtherCardSchemaLengthList);
	}
	private void createTable(SQLiteDatabase db,String tableName , String[] schemaNameList , String[] schemaTypeList , Integer[] schemaLengthList)
	{
		String SQL = "CREATE TABLE IF NOT EXISTS " + tableName + "( " +
				schemaNameList[0] + " VARCHAR(200), ";
		
		for(int i=1;i<schemaNameList.length;i++)
		{

				SQL += " " + schemaNameList[i] + " " + schemaTypeList[i] + "(" + schemaLengthList[i] + ")" +",";
		}
		
		SQL += "UNIQUE (";
		for(int i=0;i<schemaNameList.length;i++)
		{
			if(i!=schemaNameList.length - 1) // 非最後一項加逗號
			{
				SQL += schemaNameList[i] +",";
			}
			else
			{
				SQL += schemaNameList[i];
			}
		}
		SQL += ")";
		
		SQL += ");";
		
		System.out.println(SQL);
		
		db.execSQL(SQL);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//final String SQL = "DROP TABLE " + _TABLE_NAME;
        //db.execSQL(SQL);	
	}
}
