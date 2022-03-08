package com.bluecapsystem.cms.jincheon.sportstown.data.entity;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.bluecapsystem.cms.core.data.entity.Code;

@Entity
@Table(name = "camera_stream_meta_tbl")
@IdClass(CameraStreamMetaKey.class)
public class CameraStreamMeta {
	public enum WowzaMetaClass {
		HD, Proxy
	}

	/**
	 * 카메라 ID
	 */
	@Id
	private String camId;

	/**
	 * 설정 구분
	 */
	@Enumerated(EnumType.STRING)
	@Id
	private WowzaMetaClass metaClass;

	/**
	 * 어플리캐이션 코드
	 */
	@Column(nullable = true)
	private String applicationCode;

	@OneToOne(optional = true)
	@JoinColumn(name = "applicationCode", referencedColumnName = "codeId", nullable = true, updatable = false, insertable = false, //
			foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Code application;

	/** 스트리밍 서버 코드 */
	@Column(nullable = true)
	private String streamServerCode;

	/** 스트리밍 서버 */
	@OneToOne(optional = true)
	@JoinColumn(name = "streamServerCode", referencedColumnName = "codeId", nullable = true, updatable = false, insertable = false, //
			foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Code streamServer;

	/**
	 * 스트림 이름
	 */
	@Column(nullable = true)
	private String streamName;
	
	/**
	 * 이전 스트림 이름
	 */
	@Column(nullable = true)
	private String streamNameBefore;

	/**
	 * 스트림 소스 url
	 */
	@Column(nullable = true)
	private String streamSourceUrl;

	/**
	 * 스트림 소스 접속 사용자 id
	 */
	@Column(nullable = true)
	private String streamUserId;

	/**
	 * 스트림 소스 접속 사용자 password
	 */
	@Column(nullable = true)
	private String streamUserPassword;


	public CameraStreamMetaKey getKey() {
		return new CameraStreamMetaKey(camId, metaClass);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	// =============================================================================================
	// getter setter methods

	public WowzaMetaClass getMetaClass() {
		return metaClass;
	}

	public void setMetaClass(WowzaMetaClass metaClass) {
		this.metaClass = metaClass;
	}

	public String getCamId() {
		return camId;
	}

	public void setCamId(String camId) {
		this.camId = camId;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getStreamName() {
		return streamName;
//		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	//// 이전 스트림 이름 삭제하기 위한 용도
	public String getStreamNameBefore() {
		return streamNameBefore;
	}

	public void setStreamNameBefore(String streamNameBefore) {
		this.streamNameBefore = streamNameBefore;
	}

	public String getStreamSourceUrl() {
		return streamSourceUrl;
	}

	public void setStreamSourceUrl(String streamSourceUrl) {
		this.streamSourceUrl = streamSourceUrl;
	}

	public String getStreamUserId() {
		return streamUserId;
	}

	public void setStreamUserId(String streamUserId) {
		this.streamUserId = streamUserId;
	}

	public String getStreamUserPassword() {
		return streamUserPassword;
	}

	public void setStreamUserPassword(String streamUserPassword) {
		this.streamUserPassword = streamUserPassword;
	}

	public Code getApplication() {
		return application;
	}

	public void setApplication(Code application) {
		this.application = application;
	}

	/**
	 * @return 스트리밍 서버 코드
	 */
	public String getStreamServerCode() {
		return streamServerCode;
	}

	/**
	 * @param streamServerCode
	 *            스트리밍 서버 코드
	 */
	public void setStreamServerCode(String streamServerCode) {
		this.streamServerCode = streamServerCode;
	}

	/**
	 * @return 스트리밍 서버
	 */
	public Code getStreamServer() {
		return streamServer;
	}

	/**
	 * @param streamServer
	 *            스트리밍 서버
	 */
	public void setStreamServer(Code streamServer) {
		this.streamServer = streamServer;
	}


}
