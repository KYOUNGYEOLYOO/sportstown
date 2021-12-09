package com.bluecapsystem.cms.jincheon.sportstown.data.entity;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@IdClass(ContentUserKey.class)
@Entity
@Table(name="content_user_tbl")
public class ContentUser
{
	@Id
	private String contentId;

	@Id
	private String userId;


	@OneToOne(optional=true)
	@JoinColumn(name="userId", referencedColumnName="userId", nullable=true, updatable=false, insertable=false, //
			foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private User user;


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


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

}
