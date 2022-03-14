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
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.StorageInfo;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;


@Repository(value = "StorageInfoDao")
public class StorageInfoDaoImpl
{
	private static final Logger logger = LoggerFactory.getLogger(StorageInfoDaoImpl.class);

	public StorageInfo selectStorageInfo(EntityManager em)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<StorageInfo> q = cb.createQuery(StorageInfo.class);
		Root<StorageInfo> storageInfoRoot = q.from(StorageInfo.class);

		
		q.select(storageInfoRoot);
		
		TypedQuery<StorageInfo> query = em.createQuery(q);

		StorageInfo storageInfo = null;

		try
		{
			storageInfo = query.getSingleResult();
		}catch(NoResultException ex)
		{
			storageInfo = null;
		}
		return storageInfo;
	}
	
	public List<StorageInfo> selectStorageInfoList(EntityManager em)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<StorageInfo> q = cb.createQuery(StorageInfo.class);
		Root<StorageInfo> storageInfoRoot = q.from(StorageInfo.class);
		
		q.select(storageInfoRoot);
		

		TypedQuery<StorageInfo> query = em.createQuery(q);

		

		List<StorageInfo> storageInfos = null;
		try
		{
			storageInfos = query.getResultList();
		}catch(NoResultException ex)
		{
			storageInfos = new ArrayList<StorageInfo>();
		}
		return storageInfos;

	}




	public void insertStorageInfo(EntityManager em, StorageInfo storageInfo)
	{
	
		Long infoId = UniqueIDGenerator.createNumber();
		storageInfo.setInfoId(infoId.toString());
		
		logger.debug("스토리지 정보를 등록 합니다 [storageInfo={}]", storageInfo);
		em.persist(storageInfo);
	}

	public void deleteStorageInfo(EntityManager em, StorageInfo storageInfo)
	{
	
		
		
		logger.debug("스토리지 정보를 삭제 합니다 [storageInfo={}]", storageInfo);
		em.remove(storageInfo);
	}


	
}
