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
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="user_tbl")
public class User
{
	public enum UserType{
		Admin,
		Coach,
		Athlete,
	}


	public enum ConnectLocation {
		External, /* 공인 IP 접속 */
		Internal  /* 내부 IP 접속 */
	}

	/**
	 * 사용자 유일 key 값
	 */
	@Id
	private String userId;

	/**
	 * 로그인 아이디
	 */
	@Column(nullable = false, length=50)
	private String loginId;

	/**
	 * 로그인 passwrod
	 */
	@Column(nullable = false, length=256)
	private String password;

	/**
	 * 사용자 명
	 */
	@Column(nullable = false, length=50)
	private String userName;

	/**
	 * 등록 일자
	 */
	@Column(nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date registDate;

	/**
	 * 삭제일자
	 */
	@Column(nullable = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date deleteDate;

	/**
	 * 사용자 유형
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserType userType;


	/**
	 * 종목 코드
	 */
	@Column(nullable = true)
	private String sportsEventCode;

	@OneToOne(optional=true)
	@JoinColumn(name="sportsEventCode", referencedColumnName="codeId", nullable=true, updatable=false, insertable=false)
	private Code sportsEvent;


	/**
	 * 삭제여부
	 */
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isDeleted;

	/**
	 * 개발자 계정 구분
	 */
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isDeveloper;

	/**
	 * 관리자 계정 여부
	 */
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isAdmin;

	/**
	 * 사용여부
	 */
	@Type(type="true_false")
	@Column(nullable = false)
	private Boolean isUsed;
	
	
	/**
	 * 권한 시작 일자
	 */
	@Column(nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
	private Date authFromDate;
	
	/**
	 * 권한 종료 일자
	 */
	@Column(nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
	private Date authToDate;
	
	/**
	 * 변경 일자
	 */
	@Column(nullable = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date changeDate;


	/**
	 * 접속 위치 정보
	 */
	@Transient
	private ConnectLocation connectLocation;

	public User()
	{
		registDate = new Date();
		changeDate = new Date();
		isDeleted = false;
		isDeveloper = false;
		isAdmin = false;
		isUsed = true;

		// 내부 접속을 기본으로 한다
		connectLocation = ConnectLocation.Internal;
	}


	@Override
	public String toString()
	{
		return String.format("%s[userId=%s, loginId=%s, userName=%s, userType=%s, isUsed=%b, changeDate=%s]",
				this.getClass().getSimpleName(),
				userId, loginId, userName, userType, isUsed, changeDate);
	}


	public void update(User newUser)
	{
		this.setUserName(newUser.getUserName());
		this.setUserType(newUser.getUserType());
		this.setIsUsed(newUser.getIsUsed());

		this.setSportsEventCode(newUser.getSportsEventCode());
		this.setSportsEvent(newUser.getSportsEvent());
		
		this.setAuthFromDate(newUser.getAuthFromDate());
		this.setAuthToDate(newUser.getAuthToDate());

		this.setPassword(Optional.ofNullable(newUser.getPassword()).orElse(this.getPassword()));
	}
	
	public void updatePassword(String password)
	{

		this.changeDate = new Date();
		this.setPassword(password);
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
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
	
	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsDeveloper() {
		return isDeveloper;
	}

	public void setIsDeveloper(Boolean isDeveloper) {
		this.isDeveloper = isDeveloper;
	}


	public Boolean getIsAdmin() {
		if(this.isDeveloper == true)
			return true;
		return isAdmin;
	}


	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public Boolean getIsUsed() {
		return isUsed;
	}


	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}


	public UserType getUserType() {
		return userType;
	}


	public void setUserType(UserType userType) {
		this.userType = userType;
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


	/**
	 * @return the connectLocation
	 */
	public ConnectLocation getConnectLocation() {
		return connectLocation;
	}


	/**
	 * @param connectLocation the connectLocation to set
	 */
	public void setConnectLocation(ConnectLocation connectLocation) {
		this.connectLocation = connectLocation;
	}


	
	public Date getAuthFromDate() {
		return authFromDate;
	}

	public void setAuthFromDate(Date authFromDate) {
		this.authFromDate = authFromDate;
	}

	public Date getAuthToDate() {
		return authToDate;
	}

	public void setAuthToDate(Date authToDate) {
		this.authToDate = authToDate;
	}
}
