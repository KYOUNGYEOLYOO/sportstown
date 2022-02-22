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
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.DashboardDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;


@Repository(value = "DashboardDataDao")
public class DashboardDataDao
{
	private static final Logger logger = LoggerFactory.getLogger(DashboardDataDao.class);

	/**
	 * 대쉬보드 정보를 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public DashboardData selectDashboard(EntityManager em, DashboardDataSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DashboardData> q = cb.createQuery(DashboardData.class);
		Root<DashboardData> dashboardDataRoot = q.from(DashboardData.class);

		List<Predicate> qWhere = getWhereConditions(cb, dashboardDataRoot, condition);

		q.select(dashboardDataRoot);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<DashboardData> query = em.createQuery(q);

		DashboardData dashboardData = null;

		try
		{
			dashboardData = query.getSingleResult();
		}catch(NoResultException ex)
		{
			dashboardData = null;
		}
		return dashboardData;
	}

	/**
	 * 대쉬보드 목록을 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public List<DashboardData> selectDashboardDataList(EntityManager em, DashboardDataSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DashboardData> q = cb.createQuery(DashboardData.class);
		Root<DashboardData> dashboardDataRoot = q.from(DashboardData.class);
		List<Predicate> qWhere = getWhereConditions(cb, dashboardDataRoot, condition);

		q.select(dashboardDataRoot);
		// q.from(User.class);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<DashboardData> query = em.createQuery(q);

//		if(condition instanceof IPagingable)
//		{
//			Paging paging = ((IPagingable) condition).getPaging();
//			if(paging != null && paging.getEnablePaging() == true)
//			{
//				query.setFirstResult(paging.getFirstResult());
//				query.setMaxResults(paging.getPageSize());
//
//				// 대쉬보드 정보의 total count 를 가져온다
//				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//				countQuery.select(cb.count(countQuery.from(User.class)));
//				countQuery.where(qWhere.toArray(new Predicate[]{}));
//				Long totalCount = em.createQuery(countQuery).getSingleResult();
//				paging.setTotalCount(totalCount);
//			}
//		}
		
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		countQuery.select(cb.count(countQuery.from(User.class)));
		countQuery.where(qWhere.toArray(new Predicate[]{}));
		Long totalCount = em.createQuery(countQuery).getSingleResult();

		List<DashboardData> dashboardData = null;
		try
		{
			dashboardData = query.getResultList();
		}catch(NoResultException ex)
		{
			dashboardData = new ArrayList<DashboardData>();
		}
		return dashboardData;

	}



	


	public void insertDashboardData(EntityManager em, DashboardData dashboardData)
	{
		Long dataId = UniqueIDGenerator.createNumber();
		dashboardData.setDataId(dataId.toString());
		logger.debug("대쉬보드 정보를 등록 합니다 [dashboardData={}]", dashboardData);
		em.persist(dashboardData);
	}

	


	/**
	 * 사용자 table 의 검색 조건을 추가 한다
	 * @param cb
	 * @param userRoot
	 * @param condition
	 * @return
	 */
	private List<Predicate> getWhereConditions(CriteriaBuilder cb, Root<DashboardData> dashboardDataRoot, DashboardDataSelectCondition condition)
	{
		List<Predicate> conditions = new ArrayList<Predicate>();

		if(EmptyChecker.isNotEmpty(condition.getDataType()))
		{
			Predicate p = cb.and(cb.equal(dashboardDataRoot.get("dataType"), condition.getDataType()));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.getSportsEventCode()))
		{
			Predicate p = cb.and(cb.equal(dashboardDataRoot.get("sportsEventCode"), condition.getSportsEventCode()));
			conditions.add(p);
		}

		



		return conditions;
	}
}
