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
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.LoginDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;


@Repository(value = "LoginDataDao")
public class LoginDataDaoImpl
{
	private static final Logger logger = LoggerFactory.getLogger(LoginDataDaoImpl.class);

	/**
	 * login 정보를 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public LoginData selectLoginData(EntityManager em, LoginDataSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<LoginData> q = cb.createQuery(LoginData.class);
		Root<LoginData> loginDataRoot = q.from(LoginData.class);

		List<Predicate> qWhere = getWhereConditions(cb, loginDataRoot, condition);

		q.select(loginDataRoot);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<LoginData> query = em.createQuery(q);

		LoginData loginData = null;

		try
		{
			loginData = query.getSingleResult();
		}catch(NoResultException ex)
		{
			loginData = null;
		}
		return loginData;
	}

	/**
	 * 사용자 목록을 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public List<LoginData> selectLoginDataList(EntityManager em, LoginDataSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<LoginData> q = cb.createQuery(LoginData.class);
		Root<LoginData> loginDataRoot = q.from(LoginData.class);
		List<Predicate> qWhere = getWhereConditions(cb, loginDataRoot, condition);

		q.select(loginDataRoot);
		// q.from(User.class);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<LoginData> query = em.createQuery(q);

//		if(condition instanceof IPagingable)
//		{
//			Paging paging = ((IPagingable) condition).getPaging();
//			if(paging != null && paging.getEnablePaging() == true)
//			{
//				query.setFirstResult(paging.getFirstResult());
//				query.setMaxResults(paging.getPageSize());
//
//				// 사용자 정보의 total count 를 가져온다
//				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//				countQuery.select(cb.count(countQuery.from(User.class)));
//				countQuery.where(qWhere.toArray(new Predicate[]{}));
//				Long totalCount = em.createQuery(countQuery).getSingleResult();
//				paging.setTotalCount(totalCount);
//			}
//		}

		List<LoginData> loginDatas = null;
		try
		{
			loginDatas = query.getResultList();
		}catch(NoResultException ex)
		{
			loginDatas = new ArrayList<LoginData>();
		}
		return loginDatas;

	}





	public void insertLoginData(EntityManager em, LoginData loginData)
	{
		Long dataId = UniqueIDGenerator.createNumber();
		loginData.setDataId(dataId.toString());
		logger.debug("login Data를 등록 합니다 [loginData={}]", loginData);
		em.persist(loginData);
	}




	/**
	 * 사용자 table 의 검색 조건을 추가 한다
	 * @param cb
	 * @param userRoot
	 * @param condition
	 * @return
	 */
	private List<Predicate> getWhereConditions(CriteriaBuilder cb, Root<LoginData> loginDataRoot, LoginDataSelectCondition condition)
	{
		List<Predicate> conditions = new ArrayList<Predicate>();

		



		return conditions;
	}
}
