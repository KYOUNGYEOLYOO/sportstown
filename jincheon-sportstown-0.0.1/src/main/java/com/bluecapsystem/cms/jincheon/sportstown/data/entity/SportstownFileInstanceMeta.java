package com.bluecapsystem.cms.jincheon.sportstown.data.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.bcs.util.DateUtil;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.FileInstanceMeta;


@Entity
@Table(name="file_instance_meta_tbl")
public class SportstownFileInstanceMeta extends FileInstanceMeta
{
	@Column(nullable = true)
	private String camId;

	@OneToOne(optional=true)
	@JoinColumn(name="camId", referencedColumnName="camId", nullable=true, updatable=false, insertable=false)
	private Camera camera;

	@Column(nullable = true)
	private String recordUserId;

	@OneToOne(optional=true)
	@JoinColumn(name="recordUserId", referencedColumnName="userId", nullable=true, updatable=false, insertable=false)
	private User recordUser;

	@Column(nullable = false)
	private String sportsEventCode;

	@OneToOne(optional=true)
	@JoinColumn(name="sportsEventCode", referencedColumnName="codeId", nullable=true, updatable=false, insertable=false)
	private Code sportsEvent;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date recordDate;


	public SportstownFileInstanceMeta()
	{
		recordDate = DateUtil.getCurrentDate();
	}

	@Override
	public String toString()
	{
		return String.format("%s["
				+ "camId=%s, recordUserId=%s, sportsEventCode=%s"
				+ "]",
				this.getClass().getSimpleName(),
				camId, recordUserId, sportsEventCode);
	}


	//=============================================================================================
		// getter setter methods

	public String getCamId() {
		return camId;
	}


	public void setCamId(String camId) {
		this.camId = camId;
	}


	public String getSportsEventCode() {
		return sportsEventCode;
	}


	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}


	public String getRecordUserId() {
		return recordUserId;
	}


	public void setRecordUserId(String recordUserId) {
		this.recordUserId = recordUserId;
	}


	public Date getRecordDate() {
		return recordDate;
	}


	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Code getSportsEvent() {
		return sportsEvent;
	}

	public void setSportsEvent(Code sportsEvent) {
		this.sportsEvent = sportsEvent;
	}



}
