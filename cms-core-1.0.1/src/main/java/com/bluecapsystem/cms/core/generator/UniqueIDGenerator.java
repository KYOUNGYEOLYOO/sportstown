package com.bluecapsystem.cms.core.generator;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UniqueIDGenerator 
{
	private static Long numberID = 0L;

	public static synchronized String createString()
	{
		return UUID.randomUUID().toString();
	}
	
	
	public static synchronized Long createNumber()
	{
		Date now = new Date();
		
		SimpleDateFormat smf = new SimpleDateFormat("yyyyMMddHHmmssSS");
		
		try
		{
			Thread.sleep(1);
		}catch(Exception ex){}
		
		Long newId = Long.parseLong(String.format("%s00", smf.format(now)).substring(0, 16)) * 100L + (UniqueIDGenerator.numberID++ % 100L);
		
		if(UniqueIDGenerator.numberID >= 100L)
			UniqueIDGenerator.numberID = 0L;
		
		return newId;
	}
	
}
