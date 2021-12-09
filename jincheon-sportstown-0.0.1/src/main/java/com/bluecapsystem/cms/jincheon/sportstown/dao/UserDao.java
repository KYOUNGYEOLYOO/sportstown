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
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;


@Repository(value = "UserDao")
public class UserDao
{
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	/**
	 * 사용자 정보를 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public User selectUser(EntityManager em, UserSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> userRoot = q.from(User.class);

		List<Predicate> qWhere = getWhereConditions(cb, userRoot, condition);

		q.select(userRoot);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<User> query = em.createQuery(q);

		User user = null;

		try
		{
			user = query.getSingleResult();
		}catch(NoResultException ex)
		{
			user = null;
		}
		return user;
	}

	/**
	 * 사용자 목록을 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public List<User> selectUserList(EntityManager em, UserSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> userRoot = q.from(User.class);
		List<Predicate> qWhere = getWhereConditions(cb, userRoot, condition);

		q.select(userRoot);
		// q.from(User.class);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<User> query = em.createQuery(q);

		if(condition instanceof IPagingable)
		{
			Paging paging = ((IPagingable) condition).getPaging();
			if(paging != null && paging.getEnablePaging() == true)
			{
				query.setFirstResult(paging.getFirstResult());
				query.setMaxResults(paging.getPageSize());

				// 사용자 정보의 total count 를 가져온다
				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
				countQuery.select(cb.count(countQuery.from(User.class)));
				countQuery.where(qWhere.toArray(new Predicate[]{}));
				Long totalCount = em.createQuery(countQuery).getSingleResult();
				paging.setTotalCount(totalCount);
			}
		}

		List<User> users = null;
		try
		{
			users = query.getResultList();
		}catch(NoResultException ex)
		{
			users = new ArrayList<User>();
		}
		return users;

	}



	public User updateUser(EntityManager em, User user)
	{
		logger.debug("사용자를 수정 합니다 [user={}]", user);
		return em.merge(user);
	}


	public void insertUser(EntityManager em, User user)
	{
		Long userId = UniqueIDGenerator.createNumber();
		user.setUserId(userId.toString());
		logger.debug("사용자를 등록 합니다 [user={}]", user);
		em.persist(user);
	}

	public void deleteUser(EntityManager em, User user)
	{
		logger.debug("사용자를 삭제 합니다 [user={}]", user);
		em.remove(user);
	}


	/**
	 * 사용자 table 의 검색 조건을 추가 한다
	 * @param cb
	 * @param userRoot
	 * @param condition
	 * @return
	 */
	private List<Predicate> getWhereConditions(CriteriaBuilder cb, Root<User> userRoot, UserSelectCondition condition)
	{
		List<Predicate> conditions = new ArrayList<Predicate>();

		if(EmptyChecker.isNotEmpty(condition.getUserId()))
		{
			Predicate p = cb.and(cb.equal(userRoot.get("userId"), condition.getUserId()));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.getLoginId()))
		{
			Predicate p = cb.and(cb.equal(userRoot.get("loginId"), condition.getLoginId()));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.getSportsEventCode()))
		{
			Predicate p = cb.and(cb.equal(userRoot.get("sportsEventCode"), condition.getSportsEventCode()));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.hasNotUsed()) && condition.hasNotUsed() == false)
		{
			Predicate p = cb.and(cb.equal(userRoot.get("isUsed"), true));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.hasDeleted()) && condition.hasDeleted() == false)
		{
			Predicate p = cb.and(cb.equal(userRoot.get("isDeleted"), false));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.getKeyword()))
		{
			Predicate p = cb.and(
					cb.or(cb.like(userRoot.<String>get("userName"), "%" + condition.getKeyword() + "%")),
					cb.or(cb.like(userRoot.<String>get("userName"), "%" + condition.getKeyword() + "%"))
				);
			conditions.add(p);
		}



		return conditions;
	}
}
