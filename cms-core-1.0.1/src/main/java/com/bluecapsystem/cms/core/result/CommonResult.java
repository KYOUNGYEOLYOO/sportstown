package com.bluecapsystem.cms.core.result;

import com.bluecapsystem.cms.core.result.IResult;

public enum CommonResult implements IResult 
{
	Success("Success"),
	SystemError("SystemError"),
	DAOError("DAOError"),
	NotFoundInstanceError("NotFoundInstance"),
	WrongParamertError("WrongValuesError"),
	UnknownError("UnknownError"),
	;
	
	
	private String value;
	
	private CommonResult(String v)
	{
		value = v;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
