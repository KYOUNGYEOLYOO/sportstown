package com.bluecapsystem.cms.core.data.entity;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;


@MappedSuperclass
public abstract class ContentMeta
{

	@Id
	private String contentId;


	@OneToOne(optional=true)
	@JoinColumn(name="contentId", referencedColumnName="contentId", nullable=true, updatable=false, insertable=false)
	private Content content;

	public ContentMeta()
	{
	}

	public ContentMeta(String contentId)
	{
		this.contentId = contentId;
	}

	public void update(ContentMeta meta) {

	}

	// ===================================================================================== //
	// setter nad getter

	public String getContentId()
	{
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}


}
