package com.bluecapsystem.cms.jincheon.sportstown.data.entity;

import java.io.Serializable;

import com.bluecapsystem.cms.jincheon.sportstown.data.entity.CameraStreamMeta.WowzaMetaClass;


public class CameraStreamMetaKey implements Serializable
{
	/**
	 * auto generation version id
	 */
	private static final long serialVersionUID = 7326091726913845412L;

	/**
	 * 카메라 ID
	 */
	private String camId;
	
	/**
	 * 설정 구분
	 */
	private WowzaMetaClass metaClass;
	
	
	
	public CameraStreamMetaKey()
	{
		this(null, null);
	}
	
	public CameraStreamMetaKey(String camId, WowzaMetaClass metaClass)
	{
		this.camId = camId;
		this.metaClass = metaClass;
	}
	
	
	@Override
	public String toString()
	{
		return String.format("%s[camId=%s, metaClass=%s]", 
				CameraStreamMetaKey.class.getSimpleName(),
				this.camId,
				this.metaClass);
	}
	
	@Override
	public int hashCode()
	{
		return this.camId.hashCode() + this.metaClass.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{

		if(obj == null)
			return false;
		
		if( (obj instanceof CameraStreamMetaKey) == false )
			return false;
		CameraStreamMetaKey key = (CameraStreamMetaKey)obj;
		return key.camId.equals(this.camId) 
				&& key.metaClass.equals(this.metaClass);
	}
	
	
	//=============================================================================================
	// getter setter methods

	public String getCamId() {
		return camId;
	}

	public void setCamId(String camId) {
		this.camId = camId;
	}

	public WowzaMetaClass getMetaClass() {
		return metaClass;
	}

	public void setMetaClass(WowzaMetaClass metaClass) {
		this.metaClass = metaClass;
	}
	
	
	


}
