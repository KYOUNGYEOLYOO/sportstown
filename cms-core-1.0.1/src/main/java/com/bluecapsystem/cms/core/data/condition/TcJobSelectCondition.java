package com.bluecapsystem.cms.core.data.condition;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class TcJobSelectCondition implements ISelectCondition, IPagingable
{
	
	private String tcId;
	
	
	private String state;
	
	
	
	
	
	private Paging paging;
	
	
	
	public TcJobSelectCondition()
	{
		this(null, null);
	}
	
	public TcJobSelectCondition(String tcId, String state)
	{
		this.tcId = tcId;
		this.state=  state;
		
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[tcId=%s, state=%s]", 
				this.getClass().getSimpleName(),
				tcId, state);
	}

	@Override
	public Paging getPaging() {
		return this.paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	

	public String getTcId() {
		return tcId;
	}

	public void setTcId(String tcId) {
		this.tcId = tcId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	


	
	
}
