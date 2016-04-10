package edu.sfsu.cs.orange.ocr;

public class OcrResultProcess {
	String lastResult;
	String ocrLanguage;
	String engName[] = {"Chairman","Supervisor","Director","Cheif","Officer","President","Manager","Assistant","Adviser","Consultant",
			"Excutive","Leader","Specialist","Auditor","Engineer","Secretary","Management","Representative","Administrator","Accountant","Analyst",};
	String chiTraName[] = {"長","事","總","理","顧問","主任","領班","專員",
			"稽核","師","秘書","幹部","代表","會計","員"};
	
	public OcrResultProcess(String lastResult,String ocrLanguage) {
		this.lastResult = lastResult;
		this.ocrLanguage = ocrLanguage;
	}
	
	public void processResult (String lastResult,String ocrLanguage){
		System.out.println("enter processResult");
		if (ocrLanguage == "eng"){
			processEng(lastResult);
		}
		else if(ocrLanguage == "zh_TW");{
			processChiTra(lastResult);
		}
	}
	
	public void processEng(String lastResult) {
		String[] outputText = lastResult.split("\n");

		for (int i = 0; i < outputText.length; i++) {

			String temp = outputText[i];
			String address = "";
			for(int j = 0; j < engName.length; j++){
				if(temp.contains(engName[j])){
					System.out.println("job : "+temp);
				}
			}
			

			if (temp.contains("Ltd.")) {
				System.out.println("company : " + temp);
			}
			if (temp.contains("Rd.")|| temp.contains("Dist") || temp.contains("City")) {
				address = address + temp;
				System.out.println("address : " + address);
			}
			if (temp.contains("Mobile") || temp.contains("CELL PHONE")) {
				temp = temp.replace(":", "");
				temp = temp.replace("Mobile", "");
				temp = temp.replace("CELL PHONE", "");
				System.out.println("mobile : " + temp);
			}
			if (temp.contains("Fax")) {
				temp = temp.replace(":", "");
				temp = temp.replace("Fax", "");
				System.out.println("Fax : " + temp);
			}
			if (temp.contains("Tel")) {
				temp = temp.replace(":", "");
				temp = temp.replace("Tel", "");
				System.out.println("Tel : " + temp);
			}
			if (temp.contains("E-Mail")) {
				temp = temp.replace(":", "");
				temp = temp.replace("E-Mail", "");
				System.out.println("E-mail : " + temp);
			}
		}
	}

	public void processChiTra(String lastResult) {
		String[] outputText = lastResult.split("\n");

		for (int i = 0; i < outputText.length; i++) {

			String temp = outputText[i];
			String address = "";
			
			for(int j = 0; j < chiTraName.length; j++){
				if(temp.contains(chiTraName[j])){
					System.out.println("職稱 : "+temp);
				}
			}

			if (temp.contains("公司") ) {
				System.out.println("公司:" + temp);
			}
			if (temp.contains("市") || temp.contains("區") || temp.contains("路")) {
				address = address + temp;
				System.out.println("地址:" + address);
			}
			if (temp.contains("行動") ) {
				temp = temp.replace(":", "");
				temp = temp.replace("行動", "");
				System.out.println("手機:" + temp);
			}
			if (temp.contains("傳真")) {
				temp = temp.replace(":", "");
				temp = temp.replace("傳真", "");
				System.out.println("Fax:" + temp);
			}
			if (temp.contains("電話")) {
				temp = temp.replace(":", "");
				temp = temp.replace("電話", "");
				System.out.println("Tel:" + temp);
			}
			if (temp.contains("E-Mail")) {
				temp = temp.replace(":", "");
				temp = temp.replace("E-Mail", "");
				System.out.println("E-mail:" + temp);
			}
		}

	}

}
