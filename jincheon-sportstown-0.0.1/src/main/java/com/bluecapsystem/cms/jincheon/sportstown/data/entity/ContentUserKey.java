package com.bluecapsystem.cms.jincheon.sportstown.data.entity;

import java.io.Serializable;


public class ContentUserKey implements Serializable
{

	private static final long serialVersionUID = 9160268051271477289L;

	/**
	 * 설정 그룹 코드
	 */
	private String contentId;

	/**
	 * 설정 코드
	 */
	private String userId;



	public ContentUserKey()
	{
		this(null, null);
	}

	public ContentUserKey(String contentId, String userId)
	{
		this.contentId = contentId;
		this.userId = userId;
	}


	@Override
	public String toString()
	{
		return String.format("%s[contentId=%s, userId=%s]",
				ContentUserKey.class.getSimpleName(),
				this.contentId,
				this.userId);
	}


	@Override
	public int hashCode()
	{
		return this.contentId.hashCode() + this.userId.hashCode();
	}


	@Override
	public boolean equals(Object obj)
	{

		if(obj == null)
			return false;

		if( (obj instanceof ContentUserKey) == false )
			return false;
		ContentUserKey key = (ContentUserKey)obj;
		return key.contentId.equals(this.contentId)
				&& key.userId.equals(this.userId);
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






}
