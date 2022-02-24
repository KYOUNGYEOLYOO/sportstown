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
import com.bluecapsystem.cms.jincheon.sportstown.dao.UserDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.DashboardDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.LoginDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;


@Service("LoginDataManageService")
public class LoginDataManageService 
{
	private static final Logger logger = LoggerFactory.getLogger(LoginDataManageService.class);
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private LoginDataDao loginDataDao;
	
	@Autowired
	private LoginDataDaoImpl loginDataDaoImpl;
	
	/**
	 * login 데이터 목록을 조회한다
	 * @param condition
	 * @return
	 * @throws Throwable
	 */
	public List<LoginData> getDashboardData(LoginDataSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<LoginData> loginDataList = null;

		try
		{
			loginDataList = loginDataDaoImpl.selectLoginDataList(em, condition);
		}catch(Exception ex)
		{
			logger.error("login 데이터 목록 조회의 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return loginDataList;
	}
	
	
	/**
	 * login 데이터를 등록한다
	 * @param DashboardData
	 * @return
	 */
	public IResult registLoginData(LoginData loginData)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
								
				
				// DB 에 login 데이터를 등록 한다
				loginDataDaoImpl.insertLoginData(em, loginData);
				result = CommonResult.Success;
				break _TRANS;
				
				
			}catch(Exception ex)
			{
				logger.error("login 데이터 등록 오류 [user={}]\n{} ", 
						loginData, 
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
		logger.debug("login 데이터 등록 결과 [dashboardData={}] => {} ", loginData, result);
		
		return result;
	}
	
	
	
}
