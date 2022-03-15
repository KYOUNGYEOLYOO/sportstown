package com.bluecapsystem.cms.jincheon.sportstown.data.entity;


import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="tc_job_tbl")
public class TcJob 
{
	

	
	/**
	 * key 값
	 */
	@Id
	private String tcId;
	
	/**
	 * 등록 일자
	 */
	@Column(nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date registDate;

	

	public TcJob()
	{
		registDate = new Date();
		
	}

	@Column(nullable = true)
	private String fileName;

	
	@Column(nullable = true)
	private String filePath;
	
	@Column(nullable = true)
	private String state;
	
	@Column(nullable = true)
	private String contentId;

	
	

	
	public String getTcId() {
		return tcId;
	}


	public void setTcId(String tcId) {
		this.tcId = tcId;
	}

	
	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}

	public void updateState(String state)
	{
		this.setState(state);
		
	}
	
	public String getContentId() {
		return contentId;
	}


	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	
}
