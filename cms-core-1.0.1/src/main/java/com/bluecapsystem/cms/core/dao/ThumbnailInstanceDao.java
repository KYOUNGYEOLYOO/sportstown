package com.bluecapsystem.cms.core.dao;


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

import com.bluecapsystem.cms.core.data.entity.ThumbnailInstance;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;

@Repository(value="ThumbnailInstanceDao")
public class ThumbnailInstanceDao 
{

	private static final Logger logger = LoggerFactory.getLogger(ThumbnailInstanceDao.class);
	
	
	
	public ThumbnailInstance selectThumbnail(EntityManager em, String thumbnailId) throws Exception
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ThumbnailInstance> queryThumbnail= cb.createQuery(ThumbnailInstance.class);
		
		Root<ThumbnailInstance> rootThumbnail = queryThumbnail.from(ThumbnailInstance.class);
		
		Predicate p = cb.and(cb.equal(rootThumbnail.get("thumbnailId"), thumbnailId));
		
		queryThumbnail.select(rootThumbnail);
		queryThumbnail.where(p);
		TypedQuery<ThumbnailInstance> query = em.createQuery(queryThumbnail);
		
		try
		{
			return query.getSingleResult();
		}catch(NoResultException ex)
		{
			return null;
		}
	}
	
	public void insertThumbnail(EntityManager em, ThumbnailInstance thumbnail)
	{
		String thumbnailId = UniqueIDGenerator.createNumber().toString();
		thumbnail.setThumbnailId(thumbnailId);
		logger.debug("썸네일을 등록 합니다 [thumbnail = {}]", thumbnail);
		em.persist(thumbnail);
	}
	
	public ThumbnailInstance updateThumbnail(EntityManager em, ThumbnailInstance thumbnail)
	{
		logger.debug("썸네일을 수정 합니다 [thumbnail = {}]", thumbnail);
		return em.merge(thumbnail);
	}
	

	public void deleteThumbnail(EntityManager em, ThumbnailInstance thumbnail)
	{
		logger.debug("썸네일을 삭제 합니다 [file = {}]", thumbnail);
		em.remove(thumbnail);
	}
	
	
	
}
