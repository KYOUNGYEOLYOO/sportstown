package com.bluecapsystem.cms.jincheon.sportstown.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.DashboardDataDaoImpl;
import com.bluecapsystem.cms.jincheon.sportstown.dao.LoginDataDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.LoginDataDaoImpl;
import com.bluecapsystem.cms.jincheon.sportstown.dao.StorageInfoDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.StorageInfoDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.UserDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.DashboardDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.LoginDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.StorageInfo;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;


@Service("StorageInfoManageService")
public class StorageInfoManageService 
{
	private static final Logger logger = LoggerFactory.getLogger(StorageInfoManageService.class);
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private StorageInfoDao storageInfoDao;
	

	
	public List<StorageInfo> getStorageInfos() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<StorageInfo> storageInfoList = null;

		try
		{
			storageInfoList = storageInfoDao.selectStorageInfoList(em);
		}catch(Exception ex)
		{
			logger.error("???????????? ?????? ????????? ?????? ?????? \n{}",
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return storageInfoList;
	}
	
	

	public IResult registStorageInfo(StorageInfo storageInfo)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
							
				
				// DB ??? login ???????????? ?????? ??????
				storageInfoDao.insertStorageInfo(em, storageInfo);
				result = CommonResult.Success;
				break _TRANS;
				
				
			}catch(Exception ex)
			{
				logger.error("???????????? ????????? ?????? ?????? [storageInfo={}]\n{} ", 
						storageInfo, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				break _TRANS;
			}
		}
		
		if(result != CommonResult.Success)
			em.getTransaction().rollback();
		else
			em.getTransaction().commit();
		
		em.close();
		logger.debug("???????????? ????????? ?????? ?????? [storageInfo={}] => {} ", storageInfo, result);
		
		return result;
	}
	
	public IResult deleteStorageInfo()
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
				
				StorageInfo storageInfo = storageInfoDao.selectStorageInfo(em); 
				
			
				if( storageInfo == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				
				
				// DB ??? ????????????????????? ?????? ??????
				storageInfoDao.deleteStorageInfo(em,storageInfo);
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("???????????? ?????? ?????? ?????? \n{} ", 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("???????????? ?????? ??????=> {} ",  result);
		
		return result;
	}
	
}
