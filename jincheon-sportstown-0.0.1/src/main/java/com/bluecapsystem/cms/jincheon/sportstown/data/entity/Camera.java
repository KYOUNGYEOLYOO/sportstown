package com.bluecapsystem.cms.jincheon.sportstown.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "camera_tbl")
public class Camera {
	public enum CameraType {
		Static, Shift,
	}

	public enum CameraState {
		Recording, Wait
	}

	/**
	 * 카메라 관리 ID
	 */
	@Id
	private String camId;

	/**
	 * 카메라명
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * 카메라 위치 코드
	 */
	@Column(nullable = true)
	private String locationCode;

	@OneToOne(optional = true)
	@JoinColumn(name = "locationCode", referencedColumnName = "codeId", nullable = true, updatable = false, insertable = false)
	private Code location;

	/**
	 * 종목 코드
	 */
	@Column(nullable = true)
	private String sportsEventCode;

	@OneToOne(optional = true)
	@JoinColumn(name = "sportsEventCode", referencedColumnName = "codeId", nullable = true, updatable = false, insertable = false)
	private Code sportsEvent;

	/**
	 * 카메라 유형
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CameraType cameraType;

	/**
	 * 카메라 상태
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private CameraState state;

	/**
	 * 사용여부
	 */
	@Type(type = "true_false")
	@Column(nullable = false)
	private Boolean isUsed;

	/**
	 * 삭제 여부
	 */
	@Type(type = "true_false")
	@Column(nullable = false)
	private Boolean isDeleted;

	/**
	 * 등록 일자
	 */
	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date registDate;

	/**
	 * 삭제일자
	 */
	@Column(nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date deletedDate;

	/**
	 * 라이브만 서비스 하는지 여부 (DVR 작업 안함)
	 */
	@Type(type = "true_false")
	@Column(nullable = true)
	private Boolean isLiveOnly;

	@Transient
	private List<CameraStreamMeta> streamMetaItems;

	public Camera() {
		this.isDeleted = false;
		this.isUsed = true;
		this.registDate = new Date();

		this.streamMetaItems = new ArrayList<CameraStreamMeta>();

		this.isLiveOnly = false;

		this.state = CameraState.Wait;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Boolean update(Camera camera) {
		this.name = camera.getName();
		this.isUsed = camera.getIsUsed();
		this.cameraType = camera.getCameraType();

		this.locationCode = camera.getLocationCode();
		this.sportsEventCode = camera.getSportsEventCode();

		this.streamMetaItems = camera.getStreamMetaItems();

		this.isLiveOnly = camera.getIsLiveOnly();
		return true;
	}

	// =============================================================================================
	// getter setter methods

	public String getCamId() {
		return camId;
	}

	public void setCamId(String camId) {
		this.camId = camId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		if (EmptyChecker.isEmpty(locationCode) == true)
			locationCode = null;
		this.locationCode = locationCode;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Code getLocation() {
		return location;
	}

	public void setLocation(Code location) {
		this.location = location;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public CameraType getCameraType() {
		return cameraType;
	}

	public void setCameraType(CameraType cameraType) {
		this.cameraType = cameraType;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
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

	public List<CameraStreamMeta> getStreamMetaItems() {
		return streamMetaItems;
	}

	public void setStreamMetaItems(List<CameraStreamMeta> streamMetaItems) {
		this.streamMetaItems = streamMetaItems;
	}

	public CameraState getState() {
		return state;
	}

	public void setState(CameraState state) {
		this.state = state;
	}

	/**
	 * @return 라이브만 서비스 하는지 여부 (DVR 작업 안함)
	 */
	public Boolean getIsLiveOnly() {
		return isLiveOnly;
	}

	/**
	 * @param 라이브만
	 *            서비스 하는지 여부 (DVR 작업 안함)
	 */
	public void setIsLiveOnly(Boolean isLiveOnly) {
		this.isLiveOnly = isLiveOnly;
	}

}
