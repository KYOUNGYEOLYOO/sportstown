package com.bluecapsystem.cms.core.data.entity;

import java.io.Serializable;


public class PropertyKey implements Serializable
{
	
	/**
	 * auto generated serialVersionID
	 */
	private static final long serialVersionUID = -1969107524998801541L;

	/**
	 * 설정 그룹 코드
	 */
	private String propertyGroup;
	
	/**
	 * 설정 코드
	 */
	private String propertyCode;
	
	
	
	public PropertyKey()
	{
		this(null, null);
	}
	
	public PropertyKey(String propertyGroup, String propertyCode)
	{
		this.propertyGroup = propertyGroup;
		this.propertyCode = propertyCode;
	}
	
	
	@Override
	public String toString()
	{
		return String.format("%s[propertyGroup=%s, propertyCode=%s]", 
				PropertyKey.class.getSimpleName(),
				this.propertyGroup,
				this.propertyCode);
	}
	
	
	@Override
	public int hashCode()
	{
		return this.propertyGroup.hashCode() + this.propertyCode.hashCode();
	}
	
	
	@Override
	public boolean equals(Object obj)
	{

		if(obj == null)
			return false;
		
		if( (obj instanceof PropertyKey) == false )
			return false;
		PropertyKey key = (PropertyKey)obj;
		return key.propertyGroup.equals(this.propertyGroup) 
				&& key.propertyCode.equals(this.propertyCode);
	}
	
	
	



	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public String getPropertyGroup() {
		return propertyGroup;
	}
	public void setPropertyGroup(String propertyGroup) {
		this.propertyGroup = propertyGroup;
	}
}
