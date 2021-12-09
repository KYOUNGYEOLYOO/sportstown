package com.bluecapsystem.cms.core.result;

import com.bluecapsystem.cms.core.result.IResult;

public enum FileInstanceResult implements IResult 
{
	AlreadyRegistFile("AlreadyRegistFile"),
	CannotAccessFile("CannotAccessFile"),
	;
	
	
	private String value;
	
	private FileInstanceResult(String v)
	{
		value = v;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
