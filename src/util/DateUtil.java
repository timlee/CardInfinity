package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//處理DATE相關資訊


public class DateUtil {
	public  Calendar cal = Calendar.getInstance(); //現在時間
	
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
	public String getTime()//取得時間
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:00:00");
		return dateFormat.format(cal.getTime());
		
	}
	public String getWeekDays() //星期幾
	{
		int Weekdays = cal.get(Calendar.DAY_OF_WEEK);
		
		if(Weekdays == Calendar.SUNDAY){
			return "(日)";
		}
		else if(Weekdays == Calendar.MONDAY){
			return "(一)";
		}
		else if(Weekdays == Calendar.TUESDAY){
			return "(二)";
		}
		else if(Weekdays == Calendar.WEDNESDAY){
			return "(三)";
		}
		else if(Weekdays == Calendar.THURSDAY){
			return "(四)";
		}
		else if(Weekdays == Calendar.FRIDAY){
			return "(五)";
		}
		else if(Weekdays == Calendar.SATURDAY){
			return "(六)";
		}
		else{
			return null;
		}
	}
	public void addDay(int AddTime)//增加日子
	{
		cal.add(cal.DAY_OF_YEAR, AddTime);
	}
}
