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
import com.bluecapsystem.cms.jincheon.sportstown.dao.UserDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;


@Service("UserManageService")
public class UserManageService 
{
	private static final Logger logger = LoggerFactory.getLogger(UserManageService.class);
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 사용자를 가져온다
	 * @param userId 사용자 key 값
	 * @return
	 */
	public User getUser(String userId, String loginId) throws Exception
	{
		return this.getUser(new UserSelectCondition(userId, loginId));
	}
	
	/**
	 * 사용자를 가져온다
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	public User getUser(UserSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		User user = null;
		try
		{
			user = userDao.selectUser(em, condition);
		}catch(Exception ex)
		{
			logger.error("사용자 조회중 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return user;
	}
	
	/**
	 * 사용자 목록을 조회한다
	 * @param condition
	 * @return
	 * @throws Throwable
	 */
	public List<User> getUsers(UserSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<User> userList = null;

		try
		{
			userList = userDao.selectUserList(em, condition);
		}catch(Exception ex)
		{
			logger.error("사용자 목록 조회의 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return userList;
	}
	
	
	/**
	 * 사용자를 등록한다
	 * @param user
	 * @return
	 */
	public IResult registUser(User user)
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
					// 기존 Login ID 중복을 확인한다
					if(userDao.selectUser(em, new UserSelectCondition(null, user.getLoginId())) != null)
					{
						result = UserResult.OverlapLoginID;
						break _TRANS;
					}
				}
				catch(Exception ex) { throw ex; }
				
				
				// DB 에 사용자를 등록 한다
				userDao.insertUser(em, user);
				result = CommonResult.Success;
				break _TRANS;
				
				
			}catch(Exception ex)
			{
				logger.error("사용자 등록 오류 [user={}]\n{} ", 
						user, 
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
		logger.debug("사용자 등록 결과 [user={}] => {} ", user, result);
		
		return result;
	}
	
	
	/**
	 * 사용자를 수정 한다
	 * @param user
	 * @return
	 */
	public IResult modifyUser(User newUser)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				// 기존의 사용자 정보를 가져온다
				User user = userDao.selectUser(em, new UserSelectCondition(newUser.getUserId(), null));
				if( user == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				
				user.update(newUser);

				// DB 에 사용자를 등록 한다
				user = userDao.updateUser(em, user);
				
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("사용자 수정 오류 [user={}] \n{} ", 
						newUser, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("사용자 수정 결과 [user={}] => {} ", newUser, result);
		
		return result;
	}

	public IResult modifyPassword(Long userId, String orignPassword, String newPassword)
	{
		return CommonResult.UnknownError;
	}
	
	
	
	/**
	 * 사용자를 삭제 한다
	 * @param userId
	 * @return
	 */
	public IResult deleteUser(String userId)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
				// 기존 Login ID 중복을 확인한다\
				User user = userDao.selectUser(em, new UserSelectCondition(userId, null)); 
				if( user == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				
				user.setDeleteDate(new Date());
				user.setIsDeleted(true);
				
				// DB 에 사용자를 삭제 한다
				userDao.updateUser(em, user);
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("사용자 삭제 오류 [userId={}]\n{} ", 
						userId, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("사용자 삭제 결과 [userId={}] => {} ", userId, result);
		
		return result;
	}
}
