package com.bcs.transcorder.ffmpeg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FFmpegDateUtil 
{
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS"); 
	
	
	/**
	 * 00:00:00.00 시간을 가져옴
	 * 
	 * @return
	 */
	public static Date createZeroDate()
	{
		try
		{
			return parse("00:00:00.00");
		} catch(Exception ex)
		{
			return null;
		}
	}
	
	public static Date parse(String parse) throws Exception
	{
		return dateFormat.parse(parse);
	}
	
	public static String format(Date d)
	{
		return dateFormat.format(d);
	}
	
	/**
	 * date를 millisecond 로 변환 한다
	 * @param d
	 * @return
	 */
	public static Long toMilliseconds(Date d)
	{
		return d.getTime() - createZeroDate().getTime();
	}
	
	
	/**
	 * milliceonds 를 date로 변환한다
	 * @param miliseconds
	 * @return
	 */
	public static Date toDate(Long miliseconds)
	{
		return new Date(miliseconds + createZeroDate().getTime());
	}
	
	
	
}
