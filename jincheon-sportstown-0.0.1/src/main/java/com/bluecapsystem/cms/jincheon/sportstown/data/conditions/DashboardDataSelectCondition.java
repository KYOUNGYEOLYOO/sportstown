package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData.DataType;

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
	private DataType dataType;
	
	
	/**
	 * 스포츠 종목
	 */
	private String sportsEventCode;
	
	
	
	
	public DashboardDataSelectCondition()
	{
		this(null,null);
	}
	
	public DashboardDataSelectCondition(String contentId, DataType dataType)
	{
		this.contentId = contentId;
		this.dataType = dataType;
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

	public DataType getDataType() {
		return dataType;
	}

	public void settDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getSportsEventCode() {
		return sportsEventCode;
	}

	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}

	
	
	
}
