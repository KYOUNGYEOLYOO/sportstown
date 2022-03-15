package com.bluecapsystem.cms.core.dao;

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
import com.bluecapsystem.cms.core.data.condition.TcJobSelectCondition;
import com.bluecapsystem.cms.core.data.entity.TcJob;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;



@Repository(value = "TcJobDao")
public class TcJobDao
{
	private static final Logger logger = LoggerFactory.getLogger(TcJobDao.class);

	/**
	 *  DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public TcJob selectTcJob(EntityManager em, TcJobSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<TcJob> q = cb.createQuery(TcJob.class);
		Root<TcJob> tcJobRoot = q.from(TcJob.class);

		List<Predicate> qWhere = getWhereConditions(cb, tcJobRoot, condition);

		q.select(tcJobRoot);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<TcJob> query = em.createQuery(q);

		TcJob tcJob = null;

		try
		{
			tcJob = query.getSingleResult();
		}catch(NoResultException ex)
		{
			tcJob = null;
		}
		return tcJob;
	}

	/**
	 * 목록을 DB 에서 조회해 온다
	 * @param em
	 * @param condition
	 * @return
	 */
	public List<TcJob> selectTcJobList(EntityManager em, TcJobSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<TcJob> q = cb.createQuery(TcJob.class);
		Root<TcJob> tcJobRoot = q.from(TcJob.class);
		List<Predicate> qWhere = getWhereConditions(cb, tcJobRoot, condition);

		q.select(tcJobRoot);
		// q.from(User.class);
		q.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<TcJob> query = em.createQuery(q);

		if(condition instanceof IPagingable)
		{
			Paging paging = ((IPagingable) condition).getPaging();
			if(paging != null && paging.getEnablePaging() == true)
			{
				query.setFirstResult(paging.getFirstResult());
				query.setMaxResults(paging.getPageSize());

				// 사용자 정보의 total count 를 가져온다
				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
				countQuery.select(cb.count(countQuery.from(TcJob.class)));
				countQuery.where(qWhere.toArray(new Predicate[]{}));
				Long totalCount = em.createQuery(countQuery).getSingleResult();
				paging.setTotalCount(totalCount);
			}
		}

		List<TcJob> tcJobs = null;
		try
		{
			tcJobs = query.getResultList();
		}catch(NoResultException ex)
		{
			tcJobs = new ArrayList<TcJob>();
		}
		return tcJobs;

	}



	public TcJob updateTcJob(EntityManager em, TcJob tcJob)
	{
		logger.debug("tcJob를 수정 합니다 [tcJob={}]", tcJob);
		return em.merge(tcJob);
	}


	public void insertTcJob(EntityManager em, TcJob tcJob)
	{
		Long tcId = UniqueIDGenerator.createNumber();
		tcJob.setTcId(tcId.toString());
		logger.debug("tcJob를 등록 합니다 [tcJob={}]", tcJob);
		em.persist(tcJob);
	}

	


	/**
	 * table 의 검색 조건을 추가 한다
	 * @param cb
	 * @param userRoot
	 * @param condition
	 * @return
	 */
	private List<Predicate> getWhereConditions(CriteriaBuilder cb, Root<TcJob> tcJobRoot, TcJobSelectCondition condition)
	{
		List<Predicate> conditions = new ArrayList<Predicate>();

		if(EmptyChecker.isNotEmpty(condition.getTcId()))
		{
			Predicate p = cb.and(cb.equal(tcJobRoot.get("tcId"), condition.getTcId()));
			conditions.add(p);
		}

		if(EmptyChecker.isNotEmpty(condition.getState()))
		{
			Predicate p = cb.and(cb.equal(tcJobRoot.get("state"), condition.getState()));
			conditions.add(p);
		}

		

		



		return conditions;
	}
}
