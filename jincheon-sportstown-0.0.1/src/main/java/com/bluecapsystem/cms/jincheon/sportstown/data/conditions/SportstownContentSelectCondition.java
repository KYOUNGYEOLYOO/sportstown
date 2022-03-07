package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class SportstownContentSelectCondition implements ISelectCondition, IPagingable
{
	

	private String keyword;
	
	private String tagInfo;
	
	private String contentId;
	
	private String sportsEventCode;
	
	private String tagUserId;
	
	private String recordUserId;
	
	private Boolean hasDeleted;
	
	
	private Date recordFromDate;
	
	
	private Date recordToDate;
	
	
	private Paging paging;
	
	public SportstownContentSelectCondition()
	{
		this(null, null);
	}
	
	
	public SportstownContentSelectCondition(String contentId, String keyword)
	{
		this(contentId, keyword, false);
	}
	
	public SportstownContentSelectCondition(String contentId, String keyword, Boolean hasDeleted)
	{
		this.contentId = contentId;
		this.keyword = keyword;
		this.hasDeleted = hasDeleted;
	}

	
	@Override
	public String toString()
	{
		return String.format("%s[contentId=%s, keyword=%s, hasDeleted=%s]", 
				this.getClass().getSimpleName(),
				contentId, keyword, hasDeleted);
	}
	


	@Override
	public Paging getPaging() {
		return this.paging;
	}
	
	public void setPaging(Paging paging)
	{
		this.paging = paging;
	}

	public String getTagInfo() {
		return tagInfo;
	}


	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}

	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getContentId() {
		return contentId;
	}


	public void setContentId(String contentId) {
		this.contentId = contentId;
	}


	public Boolean getHasDeleted() {
		return hasDeleted;
	}


	public void setHasDeleted(Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}


	public String getSportsEventCode() {
		return sportsEventCode;
	}


	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}


	public String getTagUserId() {
		return tagUserId;
	}


	public void setTagUserId(String tagUserId) {
		this.tagUserId = tagUserId;
	}


	public String getRecordUserId() {
		return recordUserId;
	}


	public void setRecordUserId(String recordUserId) {
		this.recordUserId = recordUserId;
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
