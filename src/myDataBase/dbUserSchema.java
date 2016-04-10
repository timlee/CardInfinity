package myDataBase;

public interface dbUserSchema {
	static final String[] UserSchemaNameList = 
		{"user_id","name","age","email","account","password","type"};
	static final String[] UserSchemaTypeList = 
		{"INTEGER","VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR"};
	static final Integer[] UserSchemaLengthList = 
		{10,10,10,20,20,20,20};
	static final Integer USER_SCHEMA_LENGTH = UserSchemaLengthList.length; 
}
