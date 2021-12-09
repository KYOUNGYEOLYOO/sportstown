package com.bluecapsystem.cms.jincheon.sportstown.data.result;

import com.bluecapsystem.cms.core.result.IResult;

public enum UserResult implements IResult 
{
	
	OverlapLoginID("OverlapLoginID"),
	UserNotFound("UserNotFound"),
	WorngPassword("WorngPassword"),
	NoLoginUser("NoLoginUser")
	;
	
	
	private String value;
	
	private UserResult(String v)
	{
		value = v;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
