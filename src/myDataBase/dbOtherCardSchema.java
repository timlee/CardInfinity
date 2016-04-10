package myDataBase;

public interface dbOtherCardSchema {
	static final String[] OtherCardSchemaNameList = 
		{"OtherCard_ID","OtherCard_Type","OtherCard_Name","OtherCard_Image","OtherCard_Description"};
	static final String[] OtherCardSchemaTypeList = 
		{"VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR"};
	static final Integer[] OtherCardSchemaLengthList = 
		{100,100,100,100,200};
	static final Integer OtherCard_SCHEMA_LENGTH = OtherCardSchemaLengthList.length; 
}
