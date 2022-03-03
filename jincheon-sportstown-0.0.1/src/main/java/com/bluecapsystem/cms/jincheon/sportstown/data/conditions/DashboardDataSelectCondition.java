package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class DashboardDataSelectCondition implements ISelectCondition
{
	
	
	/**
	 * id
	 */
	private String dataId;
	
	private String contentId;
	
	/**
	 * data 종류
	 */
	private String dataType;
	
	
	/**
	 * 스포츠 종목
	 */
	private String sportsEventCode;
	
	
	
	
	public DashboardDataSelectCondition()
	{
		this(null);
	}
	
	public DashboardDataSelectCondition(String contentId)
	{
		this.contentId = contentId;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[dataId=%s, sportsEventCode=%s, contentId=%s]", 
				this.getClass().getSimpleName(),
				dataId, sportsEventCode, contentId);
	}

	

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDataType() {
		return dataType;
	}

	public void settDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSportsEventCode() {
		return sportsEventCode;
	}

	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}

	
	
	
}
