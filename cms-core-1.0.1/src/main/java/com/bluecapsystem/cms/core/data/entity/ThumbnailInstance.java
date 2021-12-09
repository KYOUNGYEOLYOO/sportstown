package com.bluecapsystem.cms.core.data.entity;



import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name="thumbnail_instance_tbl")
public class ThumbnailInstance
{
	@Id
	private String thumbnailId;
	
	@Column(nullable = false)
	private String thumbnailType;
	
	@Column(nullable = true)
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] image;

	@Column(nullable = false)
	private Date registDate;
	
	@Column(nullable = true)
	private Date deletedDate;
	
	@Type(type="true_false")
	@Column(nullable = false)
	private boolean isDeleted;
	
	public ThumbnailInstance()
	{
		this.registDate = new Date();
		this.isDeleted = false;
	}

	public String getThumbnailId() {
		return thumbnailId;
	}

	public void setThumbnailId(String thumbnailId) {
		this.thumbnailId = thumbnailId;
	}

	public String getThumbnailType() {
		return thumbnailType;
	}

	public void setThumbnailType(String thumbnailType) {
		this.thumbnailType = thumbnailType;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}
	
}
