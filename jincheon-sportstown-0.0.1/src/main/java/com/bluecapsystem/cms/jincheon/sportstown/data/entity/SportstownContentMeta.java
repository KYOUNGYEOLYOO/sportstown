package com.bluecapsystem.cms.jincheon.sportstown.data.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.bluecapsystem.cms.jincheon.sportstown.common.define.FormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="content_meta_tbl")
public class SportstownContentMeta extends ContentMeta
{
	@Column(nullable = false)
	private String title;

	@Column(nullable = true)
	@Lob
	@Type(type="text")
	private String summary;

	@Column(nullable = true)
	private String sportsEventCode;

	@OneToOne(optional=true)
	@JoinColumn(name="sportsEventCode", referencedColumnName="codeId", nullable=true, updatable=false, insertable=false, //
			foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Code sportsEvent;

	@Column(nullable = true)
	private String tagUserId;

	@OneToOne(optional=true)
	@JoinColumn(name="tagUserId", referencedColumnName="userId", nullable=true, updatable=false, insertable=false, //
			foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private User tagUser;

	@Column(nullable = true)
	private String recordUserId;

	@OneToOne(optional=true)
	@JoinColumn(name="recordUserId", referencedColumnName="userId", nullable=true, updatable=false, insertable=false, //
			foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private User recordUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date recordDate;


	@Transient
	private List<ContentUser> contentUsers;


	@Override
	public String toString()
	{
		return String.format("%s["
				+ "contentId=%s, title=%s"
				+ "]",
				this.getClass().getSimpleName(),
				getContentId(), title);
	}

	@Override
	public void update(ContentMeta meta) {

		super.update(meta);

		if(meta instanceof SportstownContentMeta == false) {
			throw new RuntimeException("잘못된 형식의 meta 정보");
		}

		SportstownContentMeta newMeta = (SportstownContentMeta)meta;

		this.setTitle(newMeta.getTitle());
		this.setSummary(newMeta.getSummary());

		this.setRecordDate(newMeta.getRecordDate());
		this.setRecordUser(newMeta.getRecordUser());

		this.setContentUsers(newMeta.getContentUsers());

	}


	/**
	 * record date 의 date string 을 가져온다
	 * @return
	 */
	public String getFormatedRecordDate() {
		return Optional.ofNullable(this.getRecordDate()).map(dt -> {
			return FormatConstant.DATE_FORMAT.format(this.getRecordDate());
		}).orElse("");
	}

	//=============================================================================================
	// getter setter methods

	public String getContentUserNames()
	{
		if(contentUsers == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for(ContentUser user : contentUsers)
		{
			if(user.getUser() != null)
				sb.append(user.getUser().getUserName() + ",");
		}

		return sb.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Code getSportsEvent() {
		return sportsEvent;
	}

	public void setSportsEvent(Code sportsEvent) {
		this.sportsEvent = sportsEvent;
	}

	public User getTagUser() {
		return tagUser;
	}

	public void setTagUser(User tagUser) {
		this.tagUser = tagUser;
	}

	public User getRecordUser() {
		return recordUser;
	}

	public void setRecordUser(User recordUser) {
		this.recordUser = recordUser;
	}

	public List<ContentUser> getContentUsers() {
		return contentUsers;
	}

	public void setContentUsers(List<ContentUser> contentUsers) {
		this.contentUsers = contentUsers;
	}

}
