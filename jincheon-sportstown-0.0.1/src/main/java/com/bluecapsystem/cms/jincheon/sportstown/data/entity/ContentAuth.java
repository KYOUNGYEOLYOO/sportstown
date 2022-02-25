package com.bluecapsystem.cms.jincheon.sportstown.data.entity;


import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.Content;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="content_auth_tbl")
public class ContentAuth
{
	

	/**
	 * key 값
	 */
	@Id
	private String contentAuthId;

	/**
	 * content 아이디
	 */
	@Column(nullable = false, length=50)
	
	private String contentId;
	
	@OneToOne(optional=true)
	@JoinColumn(name="contentId", referencedColumnName="contentId", nullable=true, updatable=false, insertable=false)
	private SportstownContentMeta content;
	
	/**
	 * user 아이디
	 */
	@Column(nullable = false, length=50)
	private String userId;
	
	
	@OneToOne(optional=true)
	@JoinColumn(name="userId", referencedColumnName="userId", nullable=true, updatable=false, insertable=false)
	private User user;
	
	/**
	 * 등록 일자
	 */
	@Column(nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date registDate;

	/**
	 * 요청일자 to
	 */
	@Column(nullable = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",  timezone = "Asia/Seoul")
	private Date fromDate;
	
	/**
	 * 요청일자 from
	 */
	@Column(nullable = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",  timezone = "Asia/Seoul")
	private Date toDate;
	
	/**
	 * 사유
	 */
	@Column(nullable = true)
	@Lob
	@Type(type="text")
	private String description;

	/**
	 * 승인여부 상태 ( 대기:wait , 승인:approval , 반려:return)
	 */
	@Column(nullable = false, length=50)
	private String state;
	

	public ContentAuth()
	{
		registDate = new Date();
		

	}


	@Override
	public String toString()
	{
		return String.format("%s[contentAuthId=%s, contentId=%s, userId=%s, fromDate=%s, toDate=%b, state=%s]",
				this.getClass().getSimpleName(),
				contentAuthId, contentId, userId, fromDate, toDate, state);
		
	}


	public void update(ContentAuth newContentAuth)
	{
		

		
//		this.setFromDate(newContentAuth.getFromDate());
//		this.setToDate(newContentAuth.getToDate());
		this.setState(newContentAuth.getState());
		
	}

	
	public String getContentAuthId() {
		return contentAuthId;
	}


	public void setContentAuthId(String contentAuthId) {
		this.contentAuthId = contentAuthId;
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

	


	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public SportstownContentMeta getContent() {
		return content;
	}


	public void setContent(SportstownContentMeta content) {
		this.content = content;
	}
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
}
