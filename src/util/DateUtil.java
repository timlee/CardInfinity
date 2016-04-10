package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//�B�zDATE������T


public class DateUtil {
	public  Calendar cal = Calendar.getInstance(); //�{�b�ɶ�
	
	private String getYear(){
		return Integer.toString(cal.get(Calendar.YEAR));
	}
	private String getMonth(){
		return Integer.toString(cal.get(Calendar.MONTH) + 1);
	}
	private String getDay(){
		return Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
	}
	public String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(cal.getTime());
	}
	public String getTime()//���o�ɶ�
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:00:00");
		return dateFormat.format(cal.getTime());
		
	}
	public String getWeekDays() //�P���X
	{
		int Weekdays = cal.get(Calendar.DAY_OF_WEEK);
		
		if(Weekdays == Calendar.SUNDAY){
			return "(��)";
		}
		else if(Weekdays == Calendar.MONDAY){
			return "(�@)";
		}
		else if(Weekdays == Calendar.TUESDAY){
			return "(�G)";
		}
		else if(Weekdays == Calendar.WEDNESDAY){
			return "(�T)";
		}
		else if(Weekdays == Calendar.THURSDAY){
			return "(�|)";
		}
		else if(Weekdays == Calendar.FRIDAY){
			return "(��)";
		}
		else if(Weekdays == Calendar.SATURDAY){
			return "(��)";
		}
		else{
			return null;
		}
	}
	public void addDay(int AddTime)//�W�[��l
	{
		cal.add(cal.DAY_OF_YEAR, AddTime);
	}
}
