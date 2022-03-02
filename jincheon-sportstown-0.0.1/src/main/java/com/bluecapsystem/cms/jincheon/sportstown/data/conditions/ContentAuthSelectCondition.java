package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import java.util.Date;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class ContentAuthSelectCondition implements ISelectCondition, IPagingable
{
	
	private String contentAuthId;
	
	private String contentId;

	private String userId;
	
	
	
	
	
	private Paging paging;
	
	
	private String state;
	
	private String keyword;
	
	/**
	 *  기간 조건
	 */
	private Date registFromDate;

	/**
	 *  기간 조건
	 */
	private Date registToDate;
	
	
	public ContentAuthSelectCondition()
	{
		this(null, null, null, null );
	}
	
	public ContentAuthSelectCondition(String contentId, String userId, String state, String contentAuthId)
	{
		this.contentId = contentId;
		this.userId = userId;
		this.state = state;
		this.contentAuthId = contentAuthId;
	}
	
	
	@Override
	public String toString()
	{
		return String.format("%s[contentAuthId=%s, contentId=%s, userId=%s]", 
				this.getClass().getSimpleName(),
				contentAuthId, contentId, userId);
	}

	@Override
	public Paging getPaging() {
		return this.paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return  시작일 조건
	 */
	public Date getRegistFromDate() {
		return registFromDate;
	}

	/**
	 * @param recordFromDate
	 *             시작일 조건
	 */
	public void setRegistFromDate(Date registFromDate) {
		this.registFromDate = registFromDate;
	}

	/**
	 * @return  종료일 조건
	 */
	public Date getRegistToDate() {
		return registToDate;
	}

	/**
	 * @param registToDate
	 *             종료일 조건
	 */
	public void setRegistToDate(Date registToDate) {
		this.registToDate = registToDate;
	}


	
	
}
