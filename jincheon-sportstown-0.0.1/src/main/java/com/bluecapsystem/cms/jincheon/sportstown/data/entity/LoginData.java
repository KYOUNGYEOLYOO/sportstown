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
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="Login_data_tbl")
public class LoginData
{
	

	
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
	 * 로그인 아이디
	 */
	@Column(nullable = false, length=256)
	private String userId;
	
	

	public LoginData()
	{
		registDate = new Date();
		
	}


	public String getDataId() {
		return dataId;
	}


	public void setDataId(String dataId) {
		this.dataId = dataId;
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

	

	
}
