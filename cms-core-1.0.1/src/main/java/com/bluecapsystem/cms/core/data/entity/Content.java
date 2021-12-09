package com.bluecapsystem.cms.core.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "content_tbl")
public class Content {
	public enum ContentState {
		Empty, // 빈 컨텐츠
		Regist, // 등록됨
		Progress, // 작업 진행중
		Complate, // 완료됨
		Delete, // 삭제 예정
		Deleted, // 삭제 됨
	}

	@Id
	private String contentId;

	@Type(type = "true_false")
	@Column(nullable = false)
	private Boolean isDeleted;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date registDate;

	@Column(nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date deleteDate;

	@Column(nullable = false)
	private String contentType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ContentState state;

	@Transient
	private ContentMeta contentMeta;

	@Transient
	private List<ContentInstance> instances;

	public Content() {
		registDate = new Date();
		isDeleted = false;

		deleteDate = null;
		contentMeta = null;

		this.setState(ContentState.Empty);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void update(Content content) {
	}

	// ===================================================================================== //
	// setter nad getter

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public ContentMeta getContentMeta() {
		return contentMeta;
	}

	public void setContentMeta(ContentMeta contentMeta) {
		this.contentMeta = contentMeta;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<ContentInstance> getInstances() {
		return instances;
	}

	public void setInstances(List<ContentInstance> instances) {
		this.instances = instances;
	}

	/**
	 * @return the state
	 */
	public ContentState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(ContentState state) {
		this.state = state;
	}

}
