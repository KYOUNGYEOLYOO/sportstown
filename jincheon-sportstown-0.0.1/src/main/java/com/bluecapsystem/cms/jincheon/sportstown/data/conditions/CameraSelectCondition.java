package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraState;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraType;

public class CameraSelectCondition implements ISelectCondition, IPagingable
{
	private String camId;

	private String locationCode;
	
	private CameraType cameraType;
	
	
	private String sportsEventCode;
	
	/**
	 * 사용안함 포함 여부
	 */
	private Boolean hasNotUsed;

	
	/**
	 * Stream meta 정보를 포함할지 여부
	 */
	private Boolean hasStreamMeta;
	
	/**
	 * 검색 키워드
	 */
	private String keyword;
	
	private String stateString;
	
	private CameraState state;
	
	
	private Paging paging;
	
	
	public CameraSelectCondition()
	{
		this(null, null);
	}
	
	public CameraSelectCondition(String camId)
	{
		this(camId, null);
	}
	
	public CameraSelectCondition(String camId, String keyword)
	{
		this(camId, keyword, false);
	}
	
	public CameraSelectCondition(String camId, String keyword, Boolean hasNotUsed)
	{
		this.camId = camId;
		this.keyword = keyword;
		this.hasNotUsed = hasNotUsed;
		this.hasStreamMeta = false;
		
		this.sportsEventCode = null;
		this.locationCode = null;
		
		this.cameraType = null;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[camId=%s, cameraType=%s, locationCode=%s, sportsEventCode=%s, hasNotUsed=%s, keyword=%s]", 
				this.getClass().getSimpleName(),
				camId, hasNotUsed, cameraType, locationCode, sportsEventCode, hasNotUsed, keyword);
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


	public Boolean getHasNotUsed() {
		return hasNotUsed;
	}

	public void setHasNotUsed(Boolean hasNotUsed) {
		this.hasNotUsed = hasNotUsed;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getStateString() {
		return stateString;
	}

	public void setStateString(String stateString) {
		this.stateString = stateString;
	}
	public CameraState getState() {
		return state;
	}

	public void setState(CameraState state) {
		this.state = state;
	}

	public Boolean getHasStreamMeta() {
		return hasStreamMeta;
	}

	public void setHasStreamMeta(Boolean hasStreamMeta) {
		this.hasStreamMeta = hasStreamMeta;
	}

	public CameraType getCameraType() {
		return cameraType;
	}

	public void setCameraType(CameraType cameraType) {
		this.cameraType = cameraType;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getSportsEventCode() {
		return sportsEventCode;
	}

	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}





	
	
	
}
