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
import com.bluecapsystem.cms.jincheon.sportstown.dao.ContentAuthDao;
import com.bluecapsystem.cms.jincheon.sportstown.dao.UserDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.ContentAuthSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.ContentAuth;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;


@Service("ContentAuthManageService")
public class ContentAuthManageService 
{
	private static final Logger logger = LoggerFactory.getLogger(ContentAuthManageService.class);
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private ContentAuthDao contentAuthDao;
	
	/**
	 * 정보를 가져온다
	 * @param userId 사용자 key 값
	 * @return
	 */
	public ContentAuth getContentAuth(String contentId, String userId, String state, String contentAuthId) throws Exception
	{
		return this.getContentAuth(new ContentAuthSelectCondition(contentId,userId,state,contentAuthId));
	}
	
	/**
	 * 정보를 가져온다
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	public ContentAuth getContentAuth(ContentAuthSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		ContentAuth contentAuth = null;
		try
		{
			contentAuth = contentAuthDao.selectContentAuth(em, condition);
		}catch(Exception ex)
		{
			logger.error("승인 정보 조회중 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return contentAuth;
	}
	
	/**
	 *  목록을 조회한다
	 * @param condition
	 * @return
	 * @throws Throwable
	 */
	public List<ContentAuth> getContentAuths(ContentAuthSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<ContentAuth> contentAuthList = null;

		try
		{
			contentAuthList = contentAuthDao.selectContentAuthList(em, condition);
		}catch(Exception ex)
		{
			logger.error("승인 목록 조회의 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return contentAuthList;
	}
	
	
	/**
	 *  등록한다
	 * @param contentAuth
	 * @return
	 */
	public IResult registContentAuth(ContentAuth contentAuth)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
				try
				{
					// 기존 중복을 확인한다
					if(contentAuthDao.selectContentAuth(em, new ContentAuthSelectCondition(contentAuth.getContentId(), contentAuth.getUserId(), "wait", "")) != null)
					{
						result = UserResult.OverlapLoginID;
						break _TRANS;
					}
				}
				catch(Exception ex) { throw ex; }
				
				
				// DB 에  등록 한다
				contentAuthDao.insertContentAuth(em, contentAuth);
				result = CommonResult.Success;
				break _TRANS;
				
				
			}catch(Exception ex)
			{
				logger.error("승인 등록 오류 [contentAuth={}]\n{} ", 
						contentAuth, 
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
		logger.debug("승인 등록 결과 [contentAuth={}] => {} ", contentAuth, result);
		
		return result;
	}
	
	
	/**
	 *  수정 한다
	 * @param contentAuth
	 * @return
	 */
	public IResult modifyReturnContentAuth(String contentId, String userId, String state, String contentAuthId)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		ContentAuth contentAuth =null;
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				// 기존의 정보를 가져온다
				contentAuth = contentAuthDao.selectContentAuth(em, new ContentAuthSelectCondition(contentId, userId, state , contentAuthId));
				if( contentAuth == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				contentAuth.setState("return");
				contentAuth.update(contentAuth);

				// DB 에  등록 한다
				contentAuth = contentAuthDao.updateContentAuth(em, contentAuth);
				
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("승인 수정 오류 [contentAuth={}] \n{} ", 
						contentAuth, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("승인 수정 결과 [contentAuth={}] => {} ", contentAuth, result);
		
		return result;
	}

	
	/**
	 *  수정 한다
	 * @param contentAuth
	 * @return
	 */
	public IResult modifyApprovalContentAuth(String contentId, String userId, String state, String contentAuthId)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		ContentAuth contentAuth =null;
		ContentAuth contentAuth2 =null;
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				// 기존의 정보를 가져온다
				contentAuth = contentAuthDao.selectContentAuth(em, new ContentAuthSelectCondition(contentId, userId, state,contentAuthId));
				if( contentAuth == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				
				
				// 기존의 정보를 가져온다
				contentAuth2 = contentAuthDao.selectContentAuth(em, new ContentAuthSelectCondition(contentId, userId, "approval",""));
				if( contentAuth2 != null)
				{
					result = CommonResult.WrongParamertError;
					break _TRANS;
				}
				contentAuth.setState("approval");
				contentAuth.update(contentAuth);

				// DB 에  등록 한다
				contentAuth = contentAuthDao.updateContentAuth(em, contentAuth);
				
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("승인 수정 오류 [contentAuth={}] \n{} ", 
						contentAuth, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("승인 수정 결과 [contentAuth={}] => {} ", contentAuth, result);
		
		return result;
	}
	
	
}
