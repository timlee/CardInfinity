package edu.sfsu.cs.orange.ocr;

public class OcrResultProcess {
	String lastResult;
	String ocrLanguage;
	String engName[] = {"Chairman","Supervisor","Director","Cheif","Officer","President","Manager","Assistant","Adviser","Consultant",
			"Excutive","Leader","Specialist","Auditor","Engineer","Secretary","Management","Representative","Administrator","Accountant","Analyst",};
	String chiTraName[] = {"��","��","�`","�z","�U��","�D��","��Z","�M��",
			"�]��","�v","����","�F��","�N��","�|�p","��"};
	
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
					System.out.println("¾�� : "+temp);
				}
			}

			if (temp.contains("���q") ) {
				System.out.println("���q:" + temp);
			}
			if (temp.contains("��") || temp.contains("��") || temp.contains("��")) {
				address = address + temp;
				System.out.println("�a�}:" + address);
			}
			if (temp.contains("���") ) {
				temp = temp.replace(":", "");
				temp = temp.replace("���", "");
				System.out.println("���:" + temp);
			}
			if (temp.contains("�ǯu")) {
				temp = temp.replace(":", "");
				temp = temp.replace("�ǯu", "");
				System.out.println("Fax:" + temp);
			}
			if (temp.contains("�q��")) {
				temp = temp.replace(":", "");
				temp = temp.replace("�q��", "");
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
