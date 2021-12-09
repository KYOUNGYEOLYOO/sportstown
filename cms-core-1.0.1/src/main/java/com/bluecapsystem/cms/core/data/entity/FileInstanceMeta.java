package com.bluecapsystem.cms.core.data.entity;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;


@MappedSuperclass
public abstract class FileInstanceMeta
{
	
	@Id
	private String fileId;
	
	
	@OneToOne(optional=true)
	@JoinColumn(name="fileId", referencedColumnName="fileId", nullable=false, updatable=false, insertable=false)
	
	private FileInstance file;
	
	public FileInstanceMeta()
	{
	}
	
	public FileInstanceMeta(String fileId)
	{
		this.fileId = fileId;
	}

	// ===================================================================================== // 
	// setter nad getter

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


}
