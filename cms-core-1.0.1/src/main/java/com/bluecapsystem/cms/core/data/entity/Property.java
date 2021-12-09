package com.bluecapsystem.cms.core.data.entity;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;



@IdClass(PropertyKey.class)
@Entity
@Table(name="property_tbl")
public class Property 
{
	
	/**
	 * 설정 그룹 코드
	 */
	@Id
	private String propertyGroup;
	
	/**
	 * 설정 코드
	 */
	@Id
	private String propertyCode;
	
	
	/**
	 * 설정 값
	 */
	@Column(nullable = true)
	@Lob
	@Type(type="text")
	private String value;
	
	@Override
	public String toString()
	{
		return String.format("%s[propertyGroup=%s, propertyCode%s, value=%s]", 
				this.getClass().getSimpleName(),
				propertyGroup, propertyCode, value );
	}
	
	public PropertyKey getKey()
	{
		return new PropertyKey(this.propertyGroup, this.propertyCode);
	}
	
	public Boolean update(Property property)
	{
		this.setValue(property.getValue());
		return true;
	}
	
	public String valueToString()
	{
		return value;
	}
	
	public Long valueToLong()
	{
		return Long.parseLong(value);
	}
	
	public Integer valueToInteger()
	{
		return Integer.parseInt(value);
	}
	
	public Float valueToFloat()
	{
		return Float.parseFloat(value);
	}
	
	
	public Double valueToDoublue()
	{
		return Double.parseDouble(value);
	}
	
	public Boolean valueToBoolean()
	{
		return Boolean.parseBoolean(value);
	}
	
	
	public File valueToFile()
	{
		String path = value.replace(File.separator, "/");
		return new File(path);
	}
	
	
	// ==================================================================================== //
	// setter and getter methods  
	// ==================================================================================== //

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


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
}
