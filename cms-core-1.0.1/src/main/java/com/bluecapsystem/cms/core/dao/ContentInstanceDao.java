package com.bluecapsystem.cms.core.dao;


import java.util.ArrayList;
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

import com.bluecapsystem.cms.core.data.entity.ContentInstance;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;

@Repository(value="ContentInstanceDao")
public class ContentInstanceDao 
{

	private static final Logger logger = LoggerFactory.getLogger(ContentInstanceDao.class);
	
	
	public void insertInstance(EntityManager em, ContentInstance instance)
	{
		String instanceId = UniqueIDGenerator.createNumber().toString();
		instance.setInstanceId(instanceId);
		
		logger.debug("컨텐츠인스턴스를 등록 합니다 [instance = {}]", instance);
		
		em.persist(instance);
	}

	public void deleteInstance(EntityManager em, ContentInstance instance)
	{
		logger.debug("컨텐츠인스턴스를 삭제 합니다 [instance = {}]", instance);
		em.remove(instance);
	}

	
	public List<ContentInstance> selectInstanceList(EntityManager em, String contentId) throws Exception
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();	
		CriteriaQuery<ContentInstance> queryInst = cb.createQuery(ContentInstance.class);
		Root<ContentInstance> rootInst = queryInst.from(ContentInstance.class);
		
		
		queryInst.select(rootInst);
		queryInst.where(cb.and(cb.equal(rootInst.get("contentId"), contentId)));

		TypedQuery<ContentInstance> query = em.createQuery(queryInst);
		
		try
		{
			return query.getResultList(); 
		}catch(NoResultException ex)
		{
			return new ArrayList<ContentInstance>();
		}
	}
	
}
