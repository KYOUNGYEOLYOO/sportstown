package com.bluecapsystem.cms.core.data.condition;

import com.bluecapsystem.cms.core.data.condition.ISelectCondition;

public class CodeSelectCondition implements ISelectCondition
{
	
	private String groupCode;
	
	private String codeId;
	
	private Boolean hasNotUsed;
	
	public CodeSelectCondition()
	{
		this(null, null);
	}
	
	
	public CodeSelectCondition(String groupCode, String codeId)
	{
		this(groupCode, codeId, false);
	}
	
	public CodeSelectCondition(String groupCode, String codeId, Boolean hasNotUsed)
	{
		this.groupCode = groupCode;
		this.codeId = codeId;
		this.hasNotUsed = hasNotUsed;
	}

	
	@Override
	public String toString()
	{
		return String.format("%s[groupCode=%s, codeId=%s, hasSystem=%b]", 
				this.getClass().getSimpleName(),
				groupCode, codeId, hasNotUsed);
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public Boolean getHasNotUsed() {
		return hasNotUsed;
	}

	public void setHasNotUsed(Boolean hasNotUsed) {
		this.hasNotUsed = hasNotUsed;
	}

	

	
}
