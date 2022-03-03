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
import com.bluecapsystem.cms.jincheon.sportstown.dao.UserDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.DashboardDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;


@Service("DashboardDataManageService")
public class DashboardDataManageService 
{
	private static final Logger logger = LoggerFactory.getLogger(DashboardDataManageService.class);
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private DashboardDataDao dashboardDataDao;
	
	@Autowired
	private DashboardDataDaoImpl dashboardDataDaoImpl;
	
	/**
	 * 대시보드 데이터 목록을 조회한다
	 * @param condition
	 * @return
	 * @throws Throwable
	 */
	public List<DashboardData> getDashboardData(DashboardDataSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<DashboardData> dashboardDataList = null;

		try
		{
			dashboardDataList = dashboardDataDaoImpl.selectDashboardDataList(em, condition);
		}catch(Exception ex)
		{
			logger.error("대시보드 데이터 목록 조회의 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return dashboardDataList;
	}
	
	
	/**
	 * 대시보드 데이터를 등록한다
	 * @param DashboardData
	 * @return
	 */
	public IResult registDashboardData(DashboardData dashboardData)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
								
				
				// DB 에 대시보드 데이터를 등록 한다
				dashboardDataDaoImpl.insertDashboardData(em, dashboardData);
				result = CommonResult.Success;
				break _TRANS;
				
				
			}catch(Exception ex)
			{
				logger.error("대시보드 데이터 등록 오류 [user={}]\n{} ", 
						dashboardData, 
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
		logger.debug("대시보드 데이터 등록 결과 [dashboardData={}] => {} ", dashboardData, result);
		
		return result;
	}
	
	
	/**
	 * 대시보드 삭제 한다
	 * @param contentId
	 * @return
	 */
	public IResult deleteDashboardData(String contentId)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				
				// 기존 대시보드 데이터를 확인한다\
				DashboardData dashboardData = dashboardDataDaoImpl.selectDashboard(em, new DashboardDataSelectCondition(contentId)); 
				if( dashboardData == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				
				
				
				// DB 에 대시보드를 삭제 한다
				dashboardDataDaoImpl.deleteDashboardData(em, dashboardData);
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("대시보드 삭제 오류 [contentId={}]\n{} ", 
						contentId, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("대시보드 삭제 결과 [contentId={}] => {} ", contentId, result);
		
		return result;
	}
}
