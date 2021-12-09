package com.bluecapsystem.cms.core.data.condition;

public class ContentSelectCondition implements ISelectCondition {

	private String keyword;

	private String contentId;

	private Boolean hasDeleted;

	public ContentSelectCondition() {
		this(null, null);
	}

	public ContentSelectCondition(final String contentId, final String keyword) {
		this(contentId, keyword, false);
	}

	public ContentSelectCondition(final String contentId, final String keyword, final Boolean hasDeleted) {
		this.contentId = contentId;
		this.keyword = keyword;
		this.hasDeleted = hasDeleted;
	}

	@Override
	public String toString() {
		return String.format("%s[contentId=%s, keyword=%s, hasDeleted=%s]", this.getClass().getSimpleName(), contentId,
				keyword, hasDeleted);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(final String contentId) {
		this.contentId = contentId;
	}

	public Boolean getHasDeleted() {
		return hasDeleted;
	}

	public void setHasDeleted(final Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}
}
