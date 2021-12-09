package com.bcs.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil 
{
	
	public static Date getCurrentDate()
	{
		return new Date();
	}
	
	
	public static Date addDate(Date d, int days)
	{
		return add(d, Calendar.DATE, days);
	}
	
	
	public static Date add(Date d, int field, int value)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(field, value);
		return c.getTime();
	}

}
