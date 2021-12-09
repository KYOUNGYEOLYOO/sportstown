package com.bluecapsystem.cms.core.data.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name="code_group_tbl")
public class CodeGroup 
{
	@Id
	private String groupCode;
	
	@Column(nullable = false, length=250)
	private String name;
	
	@Column(nullable = true)
	@Lob
	@Type(type="text")
	private String description;
	
	
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isSystem;

	@Transient
	private List<Code> codes;
	
	public CodeGroup()
	{
		codes = new ArrayList<Code>();
		isSystem = false;
	}
	
	public synchronized void sortCodes()
	{
		for(int i = 0; i < codes.size(); i++)
		{
			codes.get(i).setIndex(i);
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("%s["
				+ "groupCode=%s, name=%s, isSystem=%b"
				+ "]",
				this.getClass().getSimpleName(),
				groupCode, name, isSystem);
	}
	
	
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public List<Code> getCodes() {
		return codes;
	}

	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

}
