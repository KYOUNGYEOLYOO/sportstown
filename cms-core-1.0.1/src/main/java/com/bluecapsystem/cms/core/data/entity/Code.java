package com.bluecapsystem.cms.core.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="code_tbl")
public class Code 
{
	@Id
	private String codeId;
	
	@Column(nullable = false)
	private String groupCode;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, name="idx")
	private Integer index;
	
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isUsed;
	
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isDeleted;
	
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isPartition;
	
	public Code()
	{
		isDeleted = false;
		isPartition = false;
		isUsed = true;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s["
				+ "groupCode=%s, codeSeq=%s, name=%s, isUsed=%s"
				+ "]",
				this.getClass().getSimpleName(),
				groupCode, codeId, name, isUsed.toString());
	}
	

	public Boolean update(Code code)
	{
		this.name = code.getName();
		this.isUsed = code.getIsUsed();
		
		return true;
	}
	
	//=============================================================================================
	// getter setter methods

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeSeq) {
		this.codeId = codeSeq;
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

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		
		if(isUsed == null)
			isUsed = true;
		this.isUsed = isUsed;
	}

	public Boolean getIsPartition() {
		return isPartition;
	}

	public void setPartition(Boolean isPartition) {
		this.isPartition = isPartition;
	}
	
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
