package com.bluecapsystem.cms.core.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluecapsystem.cms.core.data.condition.ContentSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Content;
import com.bluecapsystem.cms.core.data.entity.Content.ContentState;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;

@Repository(value = "ContentDao")
public class ContentDao {

	private static final Logger logger = LoggerFactory.getLogger(ContentDao.class);

	public List<Content> selectContentList(EntityManager em, ContentSelectCondition condition) throws Exception {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Content> queryContent = cb.createQuery(Content.class);

		Root<Content> rootContent = queryContent.from(Content.class);

		queryContent.select(rootContent);
		queryContent.orderBy(cb.desc(rootContent.get("registDate")));
		TypedQuery<Content> query = em.createQuery(queryContent);

		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Content selectContent(EntityManager em, String contentId) throws RuntimeException {
		try {
			return em.find(Content.class, contentId);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public void insertContent(EntityManager em, Content content) {
		String contentId = UniqueIDGenerator.createNumber().toString();
		content.setContentId(contentId);
		logger.debug("컨텐츠를 등록 합니다 [content = {}]", content);
		em.persist(content);
	}

	public Content updateContent(EntityManager em, Content content) {
		logger.debug("컨텐츠를 수정 합니다 [content = {}]", content);
		return em.merge(content);
	}

	public Content deleteContent(EntityManager em, String contentId) throws RuntimeException {
		Content content = this.selectContent(em, contentId);

		logger.debug("컨텐츠를 삭제 합니다 [content = {}]", content);
		content.setDeleteDate(new Date());
		content.setIsDeleted(true);
		content.setState(ContentState.Delete);

		return em.merge(content);
	}

}
