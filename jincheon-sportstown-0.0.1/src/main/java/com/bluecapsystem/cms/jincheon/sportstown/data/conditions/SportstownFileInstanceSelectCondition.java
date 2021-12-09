package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import java.util.Date;

import com.bluecapsystem.cms.core.data.condition.FileInstanceSelectCondition;
import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraType;

public class SportstownFileInstanceSelectCondition extends FileInstanceSelectCondition implements IPagingable {
	private String camId;

	private CameraType cameraType;

	private String sportsEventCode;

	private Paging paging;

	// @DateTimeFormat(pattern="yyyy-MM-dd")

	/**
	 * 녹화일 기간 조건
	 */
	private Date recordFromDate;

	/**
	 * 녹화일 기간 조건
	 */
	private Date recordToDate;

	public SportstownFileInstanceSelectCondition() {
		super();
	}

	@Override
	public String toString() {
		return String.format("%s[%s, camId=%s, cameraType=%s, sportsEventCode=%s]", this.getClass().getSimpleName(), super.toString(), camId, cameraType,
				sportsEventCode);
	}

	@Override
	public Paging getPaging() {
		return this.paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	public String getCamId() {
		return camId;
	}

	public void setCamId(String camId) {
		this.camId = camId;
	}

	public CameraType getCameraType() {
		return cameraType;
	}

	public void setCameraType(CameraType cameraType) {
		this.cameraType = cameraType;
	}

	public String getSportsEventCode() {
		return sportsEventCode;
	}

	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}

	/**
	 * @return 녹화 시작일 조건
	 */
	public Date getRecordFromDate() {
		return recordFromDate;
	}

	/**
	 * @param recordFromDate
	 *            녹화 시작일 조건
	 */
	public void setRecordFromDate(Date recordFromDate) {
		this.recordFromDate = recordFromDate;
	}

	/**
	 * @return 녹화 종료일 조건
	 */
	public Date getRecordToDate() {
		return recordToDate;
	}

	/**
	 * @param recordToDate
	 *            녹화 종료일 조건
	 */
	public void setRecordToDate(Date recordToDate) {
		this.recordToDate = recordToDate;
	}

}
