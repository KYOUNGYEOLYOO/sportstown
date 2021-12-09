package com.bluecapsystem.cms.core.data.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;


@Entity
@Table(name="content_instance_tbl")
public class ContentInstance
{
	@Id
	private String instanceId;
	
	
	@Column(nullable = false)
	private String contentId;
	
	@Column(nullable = false)
	private String fileId;
	
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isDeleted;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registDate;
	
	@Column(nullable = true)
	private Date deleteDate;
	
	@OneToOne(optional=true)
	@JoinColumn(name="fileId", referencedColumnName="fileId", nullable=true, updatable=false, insertable=false)
	private FileInstance file;
	
	
	public ContentInstance()
	{
		this(null, null);
	}
	
	public ContentInstance(String contentId, String fileId)
	{
		this.contentId = contentId;
		this.fileId = fileId;
		
		this.registDate = new Date();
		this.deleteDate = null;
		this.isDeleted = false;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s["
				+ "instanceId=%s, contentId=%s, fileId=%s"
				+ "]",
				this.getClass().getSimpleName(),
				instanceId, contentId, fileId);
	}
	
	

	// ===================================================================================== // 
	// setter nad getter

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public FileInstance getFile() {
		return file;
	}

	public void setFile(FileInstance file) {
		this.file = file;
	}

	
	
}
