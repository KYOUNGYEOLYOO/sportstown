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
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.CameraSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.CameraStreamMeta;


@Repository(value = "CameraDao")
public class CameraDao 
{
	private static final Logger logger = LoggerFactory.getLogger(CameraDao.class);
	
	public Camera selectCamera(EntityManager em, CameraSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Camera> queryCam = cb.createQuery(Camera.class);
		Root<Camera> rootCam = queryCam.from(Camera.class);
		
		List<Predicate> qWhere = getWhereConditions(cb, rootCam, condition);
		
		queryCam.select(rootCam);
		queryCam.where(qWhere.toArray(new Predicate[]{}));

		TypedQuery<Camera> query = em.createQuery(queryCam);
		
		Camera user = null;
		
		try
		{
			user = query.getSingleResult();
		}catch(NoResultException ex)
		{
			user = null;
		}
		return user;
	}
	
	public List<Camera> selectCameraList(EntityManager em, CameraSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Camera> queryCam = cb.createQuery(Camera.class);
		Root<Camera> rootCam = queryCam.from(Camera.class);
		List<Predicate> qWhere = getWhereConditions(cb, rootCam, condition);
		
		queryCam.select(rootCam);
		// q.from(User.class);
		queryCam.where(qWhere.toArray(new Predicate[]{}));
		
		queryCam.orderBy(cb.asc(rootCam.get("name")));
		
		TypedQuery<Camera> query = em.createQuery(queryCam);

		if(condition instanceof IPagingable)
		{
			Paging paging = ((IPagingable) condition).getPaging();
			if(paging != null && paging.getEnablePaging() == true)
			{
				query.setFirstResult(paging.getFirstResult());
				query.setMaxResults(paging.getPageSize());
				
				// 사용자 정보의 total count 를 가져온다
				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
				countQuery.select(cb.count(countQuery.from(Camera.class)));
				countQuery.where(qWhere.toArray(new Predicate[]{}));
				
				Long totalCount = em.createQuery(countQuery).getSingleResult();
				paging.setTotalCount(totalCount);
			}
		}
		
		List<Camera> cameras = null;
		try
		{
			cameras = query.getResultList();
		}catch(NoResultException ex)
		{
			cameras = new ArrayList<Camera>();
		}
		return cameras;
		
	}
	
	
	public Camera updateCamera(EntityManager em, Camera camera)
	{
		logger.debug("카메라를 수정 합니다 [camera={}]", camera);
		return em.merge(camera);	
	}
	
	
	public void insertCamera(EntityManager em, Camera camera)
	{
		Long camId = UniqueIDGenerator.createNumber();
		camera.setCamId(camId.toString());
		logger.debug("카메라를 등록 합니다 [camera={}]", camera);
		em.persist(camera);
	}
	
	public void deleteCamera(EntityManager em, Camera camera)
	{
		logger.debug("카메라를 삭제 합니다 [camera={}]", camera);
		em.remove(camera);
	}
	
	
	/**
	 * 검색 조건을 생성 한다
	 * @param cb
	 * @param rootCam
	 * @param condition
	 * @return
	 */
	private List<Predicate> getWhereConditions(CriteriaBuilder cb, Root<Camera> rootCam, CameraSelectCondition condition)
	{
		List<Predicate> conditions = new ArrayList<Predicate>();
		
		
		// block 
		{
			Predicate p = cb.and(cb.equal(rootCam.get("isDeleted"), false));
			conditions.add(p);
		}
		
		
		if(EmptyChecker.isNotEmpty(condition.getCamId()))
		{
			Predicate p = cb.and(cb.equal(rootCam.get("camId"), condition.getCamId()));
			conditions.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getCameraType()))
		{
			Predicate p = cb.and(cb.equal(rootCam.get("cameraType"), condition.getCameraType()));
			conditions.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getLocationCode()))
		{
			Predicate p = cb.and(cb.equal(rootCam.get("locationCode"), condition.getLocationCode()));
			conditions.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getSportsEventCode()))
		{
			Predicate p = cb.and(cb.equal(rootCam.get("sportsEventCode"), condition.getSportsEventCode()));
			conditions.add(p);
		}
		
		if(EmptyChecker.isEmpty(condition.getHasNotUsed()) && condition.getHasNotUsed() == false)
		{
			Predicate p = cb.and(cb.equal(rootCam.get("isUsed"), true));
			conditions.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getKeyword()))
		{
			
			Predicate p = cb.and(
					cb.like(rootCam.<String>get("name"), "%" + condition.getKeyword() + "%")
				);
			conditions.add(p);
		}
		
		return conditions;
	}
	
	
	
	
	
	public List<CameraStreamMeta> selectCameraStreamMetaList(EntityManager em, String camId)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<CameraStreamMeta> queryStream = cb.createQuery(CameraStreamMeta.class);
		Root<CameraStreamMeta> rootStream = queryStream.from(CameraStreamMeta.class);
		
		
		queryStream.select(rootStream);
		queryStream.where(cb.and(cb.equal(rootStream.get("camId"), camId)));
		
		TypedQuery<CameraStreamMeta> query = em.createQuery(queryStream);
		queryStream.orderBy(cb.asc(rootStream.get("metaClass")));	
		
		List<CameraStreamMeta> streamMetaItems = null;
		try
		{
			streamMetaItems = query.getResultList();
		}catch(NoResultException ex)
		{
			streamMetaItems = new ArrayList<CameraStreamMeta>();
		}
		return streamMetaItems;
		
	}
	
	public CameraStreamMeta updateCameraStreamMeta(EntityManager em, CameraStreamMeta streamMeta)
	{
		logger.debug("CameraStreamMeta 를 수정 합니다 [streamMeta={}]", streamMeta);
		return em.merge(streamMeta);	
	}
	
	
	public void insertCameraStreamMeta(EntityManager em, String camId, CameraStreamMeta streamMeta)
	{
		streamMeta.setCamId(camId);
		logger.debug("CameraStreamMeta 를 등록 합니다 [streamMeta={}]", streamMeta);
		em.persist(streamMeta);
	}
	
	public void deleteCameraStreamMeta(EntityManager em, CameraStreamMeta streamMeta)
	{
		logger.debug("CameraStreamMeta 를 삭제 합니다 [streamMeta={}]", streamMeta);
		em.remove(streamMeta);
	}
}
