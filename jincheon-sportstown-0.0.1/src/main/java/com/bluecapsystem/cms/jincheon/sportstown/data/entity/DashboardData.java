package com.bluecapsystem.cms.jincheon.sportstown.data.entity;


import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="dashboard_data_tbl")
public class DashboardData
{
	public enum DataType{
		Contents,
		Archive,
		Login,
		View,
		Download
	}


	
	/**
	 * key 값
	 */
	@Id
	private String dataId;
	
	/**
	 * 등록 일자
	 */
	@Column(nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date registDate;

	/**
	 * 종목 코드
	 */
	@Column(nullable = false)
	private String sportsEventCode;

	@OneToOne(optional=true)
	@JoinColumn(name="sportsEventCode", referencedColumnName="codeId", nullable=true, updatable=false, insertable=false)
	private Code sportsEvent;


	/**
	 * 로그인 아이디
	 */
	@Column(nullable = false, length=256)
	private String userId;

	/**
	 * 컨텐츠 아이디
	 */
	@Column(nullable = true, length=256)
	private String contentId;
	
	/**
	 * data 유형
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DataType dataType;
	
	

	public DashboardData()
	{
		registDate = new Date();
		
	}


	public String getDataId() {
		return dataId;
	}


	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getContentId() {
		return contentId;
	}


	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public DataType getDataType() {
		return dataType;
	}


	public void setUserType(DataType dataType) {
		this.dataType = dataType;
	}


	public String getSportsEventCode() {
		return sportsEventCode;
	}


	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}


	public Code getSportsEvent() {
		return sportsEvent;
	}


	public void setSportsEvent(Code sportsEvent) {
		this.sportsEvent = sportsEvent;
	}



	
}
