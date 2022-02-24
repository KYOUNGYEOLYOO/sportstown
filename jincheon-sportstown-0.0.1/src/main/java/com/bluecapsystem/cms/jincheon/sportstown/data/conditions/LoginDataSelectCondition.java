package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class LoginDataSelectCondition implements ISelectCondition
{
	/**
	 * id
	 */
	private String dataId;
	
	public LoginDataSelectCondition()
	{
		this(null);
	}
	
	public LoginDataSelectCondition(String dataId)
	{
		this.dataId = dataId;
		
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[dataId=%s]", 
				this.getClass().getSimpleName(),
				dataId);
	}

	

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	
}
