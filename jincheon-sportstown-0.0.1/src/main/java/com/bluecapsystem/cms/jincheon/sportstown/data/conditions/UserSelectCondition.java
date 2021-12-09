package com.bluecapsystem.cms.jincheon.sportstown.data.conditions;

import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class UserSelectCondition implements ISelectCondition, IPagingable
{
	/**
	 * 사용자 로그인 ID
	 */
	private String loginId;
	
	/**
	 * 사용자 key id
	 */
	private String userId;
	
	/**
	 * 삭제 사용자 포함여부
	 */
	private Boolean hasDeleted;
	
	
	/**
	 * 스포츠 종목
	 */
	private String sportsEventCode;
	
	/**
	 * 사용 안하는 사용자 포함여부
	 */
	private Boolean hasNotUsed;
	
	
	
	private Paging paging;
	
	/**
	 * 검색 키워드
	 */
	private String keyword;
	
	public UserSelectCondition()
	{
		this(null, null);
	}
	
	public UserSelectCondition(String userId, String loginId)
	{
		this.userId = userId;
		this.loginId=  loginId;
		this.hasDeleted = false;
		this.hasNotUsed = true;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s[userId=%s, loginId=%s, sportsEventCode=%s, hasNotUsed=%b, hasDeleted=%b, keyword=%s]", 
				this.getClass().getSimpleName(),
				userId, loginId, sportsEventCode, hasNotUsed, hasDeleted, keyword);
	}

	@Override
	public Paging getPaging() {
		return this.paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean hasDeleted() {
		return hasDeleted;
	}

	public void setHasDeleted(Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Boolean hasNotUsed() {
		return hasNotUsed;
	}

	public void setHasNotUsed(Boolean hasNotUsed) {
		this.hasNotUsed = hasNotUsed;
	}

	public String getSportsEventCode() {
		return sportsEventCode;
	}

	public void setSportsEventCode(String sportsEventCode) {
		this.sportsEventCode = sportsEventCode;
	}


	
	
}
