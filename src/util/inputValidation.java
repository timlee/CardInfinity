package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class inputValidation {
	//�r���P�_�Ҳ�
    
	public boolean checkNumberAndEng(String input)// �O�_���^��+�Ʀr?
	{
		String onlyNumberAndEng = "^[a-zA-Z0-9]+$";
	    Pattern NumberAndEngPattern = Pattern.compile(onlyNumberAndEng);
	    Matcher matcher = NumberAndEngPattern.matcher(input);
	    if(matcher.find()){
	    	return true;
	    }
	    else{
	    	System.out.println("�榡���D�k�r��");
	    	return false;
	    }
	}
	public boolean checkIllegal(String input){
		String illegal = "^[^?\\/*'`><()!@#$%^&_~'\\\\]+$";
		Pattern checkIllegal = Pattern.compile(illegal);
	    Matcher matcher = checkIllegal.matcher(input);
	    if(matcher.find()){
	    	return true;
	    }
	    else{
	    	System.out.println("�榡���D�k�r��");
	    	return false;
	    }
	}
	public boolean checkURL(String input){
		String illegal = "^[^?*'`+,><()!@#$%^&_~'\\\\]+$";
		Pattern checkIllegal = Pattern.compile(illegal);
	    Matcher matcher = checkIllegal.matcher(input);
	    if(matcher.find()){
	    	return true;
	    }
	    else{
	    	System.out.println("�榡���D�k�r��");
	    	return false;
	    }
	}
	public boolean checkDate(String input){
		String illegal = "^[0-9]{4}\\/[0-9]{1,2}\\/[0-9]{1,2}$";
		Pattern checkDate = Pattern.compile(illegal);
	    Matcher matcher = checkDate.matcher(input);
	    if(matcher.find()){
	    	return true;
	    }
	    else{
	    	System.out.println("�榡���D�k�r��");
	    	return false;
	    }
	}
	public boolean checkEmail(String input)//�O�_��email�榡
	{
		String onlyEmail = "^[a-zA-Z0-9]+[@][A-Za-z0-9]+[.][A-Za-z0-9]+$"; // ex: lsc830621@gmail.com
		String onlyEmail2 = "^[a-zA-Z0-9]+[@][A-Za-z0-9]+[.][A-Za-z0-9]+[.][A-Za-z0-9]+$";  // ex:lsc830621@yahoo.com.tw
	    Pattern EmailPattern = Pattern.compile(onlyEmail);
	    Pattern EmailPattern2 = Pattern.compile(onlyEmail2);
	    Matcher matcher = EmailPattern.matcher(input);
	    Matcher matcher2 = EmailPattern2.matcher(input);
	    if(matcher.find() || matcher2.find()){
	    	return true;
	    }
	    else{
	    	System.out.println("e-mail�榡���~");
	    	return false;
	    }
	}
	public boolean checkStringLength(String input , int maxLength){
		if(input.length() > maxLength){
			System.out.println("�K�X���׶W�L" + maxLength + "���");
			return false;
		}
		else{
			return true;
		}
	}
}
