package com.bluecapsystem.cms.jincheon.sportstown.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.ContentAuthSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.ContentAuth;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;


@Repository(value = "ContentAuthDao")
public class ContentAuthDao
{
	private static final Logger logger = LoggerFactory.getLogger(ContentAuthDao.class);

	/**
	 * 정보를 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public ContentAuth selectContentAuth(EntityManager em, ContentAuthSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ContentAuth> q = cb.createQuery(ContentAuth.class);
		Root<ContentAuth> contentAuthRoot = q.from(ContentAuth.class);

		List<Predicate> qWhere = getWhereConditions(cb, contentAuthRoot, condition);

		q.select(contentAuthRoot);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<ContentAuth> query = em.createQuery(q);

		ContentAuth contentAuth = null;

		try
		{
			contentAuth = query.getSingleResult();
		}catch(NoResultException ex)
		{
			contentAuth = null;
		}
		return contentAuth;
	}

	/**
	 * 목록을 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public List<ContentAuth> selectContentAuthList(EntityManager em, ContentAuthSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ContentAuth> q = cb.createQuery(ContentAuth.class);
		Root<ContentAuth> contentAuthRoot = q.from(ContentAuth.class);
		List<Predicate> qWhere = getWhereConditions(cb, contentAuthRoot, condition);

		q.select(contentAuthRoot);
		// q.from(User.class);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<ContentAuth> query = em.createQuery(q);

		if(condition instanceof IPagingable)
		{
			Paging paging = ((IPagingable) condition).getPaging();
			if(paging != null && paging.getEnablePaging() == true)
			{
				query.setFirstResult(paging.getFirstResult());
				query.setMaxResults(paging.getPageSize());

				// 사용자 정보의 total count 를 가져온다
				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
				countQuery.select(cb.count(countQuery.from(ContentAuth.class)));
				countQuery.where(qWhere.toArray(new Predicate[]{}));
				Long totalCount = em.createQuery(countQuery).getSingleResult();
				paging.setTotalCount(totalCount);
			}
		}

		List<ContentAuth> contentAuths = null;
		try
		{
			contentAuths = query.getResultList();
		}catch(NoResultException ex)
		{
			contentAuths = new ArrayList<ContentAuth>();
		}
		return contentAuths;

	}



	public ContentAuth updateContentAuth(EntityManager em, ContentAuth contentAuth)
	{
		logger.debug("contentAuth를 수정 합니다 [contentAuth={}]", contentAuth);
		return em.merge(contentAuth);
	}


	public void insertContentAuth(EntityManager em, ContentAuth contentAuth)
	{
		Long contentAuthId = UniqueIDGenerator.createNumber();
		contentAuth.setContentAuthId(contentAuthId.toString());
		logger.debug("contentAuth를 등록 합니다 [contentAuth={}]", contentAuth);
		em.persist(contentAuth);
	}

	


	/**
	 *  table 의 검색 조건을 추가 한다
	 * @param cb
	 * @param contentAuthRoot
	 * @param condition
	 * @return
	 */
	private List<Predicate> getWhereConditions(CriteriaBuilder cb, Root<ContentAuth> contentAuthRoot, ContentAuthSelectCondition condition)
	{
		List<Predicate> conditions = new ArrayList<Predicate>();

		if(EmptyChecker.isNotEmpty(condition.getState()))
		{
			Predicate p = cb.and(cb.equal(contentAuthRoot.get("state"), condition.getState()));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.getUserId()))
		{
			Predicate p = cb.and(cb.equal(contentAuthRoot.get("userId"), condition.getUserId()));
			conditions.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getContentId()))
		{
			Predicate p = cb.and(cb.equal(contentAuthRoot.get("contentId"), condition.getContentId()));
			conditions.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getContentAuthId()))
		{
			Predicate p = cb.and(cb.equal(contentAuthRoot.get("contentAuthId"), condition.getContentAuthId()));
			conditions.add(p);
		}



		return conditions;
	}
}
