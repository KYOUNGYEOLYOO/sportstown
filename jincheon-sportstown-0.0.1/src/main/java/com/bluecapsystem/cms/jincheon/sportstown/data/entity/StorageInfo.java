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
@Table(name="Storage_Info_tbl")
public class StorageInfo
{
	


	/**
	 * key 값
	 */
	@Id
	private String infoId;
	
	/**
	 * 사용용량
	 */
	@Column(nullable = false, length=256)
	private String useInfo;
	/**
	 * 전체용량
	 */
	@Column(nullable = false, length=256)
	private String totalInfo;
	
	

	public String getInfoId() {
		return infoId;
	}


	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	
	public String getUseInfo() {
		return useInfo;
	}


	public void setUseInfo(String useInfo) {
		this.useInfo = useInfo;
	}

	public String getTotalInfo() {
		return totalInfo;
	}


	public void setTotalInfo(String totalInfo) {
		this.totalInfo = totalInfo;
	}

	

	
}
