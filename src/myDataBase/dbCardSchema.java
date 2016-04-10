package myDataBase;

public interface dbCardSchema {
	static final String[] CardSchemaNameList = 
		{"Card_ID","Card_Name","Card_Company","Card_Department","Card_JobTitle",
		"Card_Email","Card_Tel","Card_MobilePhone","Card_Address",
		"Card_Fax","Card_Image","Card_Url","Card_Type"};
	static final String[] CardSchemaTypeList = 
		{"VARCHAR","VARCHAR","VARCHAR","VARCHAR","VARCHAR",
		"VARCHAR","VARCHAR","VARCHAR","VARCHAR",
		"VARCHAR","VARCHAR","VARCHAR","VARCHAR"};
	static final Integer[] CardSchemaLengthList = 
		{100,100,100,100,100,
		100,100,100,100,
		100,100,100,100};
	static final Integer Card_SCHEMA_LENGTH = CardSchemaLengthList.length; 
}
