package com.bluecapsystem.cms.jincheon.sportstown.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bluecapsystem.cms.core.dao.FileInstanceDao;
import com.bluecapsystem.cms.core.dao.IFileInstanceMetaRepository;
import com.bluecapsystem.cms.core.data.condition.FileInstanceSelectCondition;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.FileInstanceMeta;
import com.bluecapsystem.cms.core.properties.StoragePathProperties;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.FileInstanceResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.jincheon.sportstown.dao.TcJobDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.TcJobSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.TcJob;


@Service("TcJobService")
public class TcJobService {
	private static final Logger logger = LoggerFactory.getLogger(TcJobService.class);

	@Autowired
	private TcJobDao tcJobDao;
	

	@Autowired
	private EntityManagerFactory emf;

	
	public List<TcJob> getTcJobs(TcJobSelectCondition condition) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<TcJob> list = null;
		try {
			list = getTcJobs(em, condition);
			em.close();
		} catch (Exception ex) {
			em.close();
			throw ex;
		}
		return list;
	}

	/**
	 *  리스트를 가져온다
	 *
	 * @param em
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<TcJob> getTcJobs(EntityManager em, TcJobSelectCondition condition) throws Exception {
		return tcJobDao.selectTcJobList(em, condition);
	}



	/**
	 *  조회 한다
	 *
	 * @param em
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public TcJob getTcJob(EntityManager em, String tcId) throws Exception {
		
		return this.getTcJob(new TcJobSelectCondition(tcId, ""));
	}
	
	public TcJob getTcJob(TcJobSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		TcJob tcJob = null;
		try
		{
			tcJob = tcJobDao.selectTcJob(em, condition);
		}catch(Exception ex)
		{
			logger.error(" 조회중 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return tcJob;
	}

	/**
	 *  등록 한다
	 *
	 * @param tcJob
	 * @return
	 */
	public IResult registTcJob(TcJob tcJob) {
		IResult result = CommonResult.UnknownError;

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		result = registTcJob(em, tcJob);

		if (result == CommonResult.Success)
			em.getTransaction().commit();
		else
			em.getTransaction().rollback();

		em.close();
		return result;
	}

	/**
	 *  등록 한다
	 *
	 * @param em
	 * @param tcJob
	 * @return
	 */
	public IResult registTcJob(EntityManager em, TcJob tcJob) {
		IResult result = CommonResult.UnknownError;

		try {
			_TRANSACTION: {
				

				tcJobDao.insertTcJob(em, tcJob);
				em.flush();
				result = CommonResult.Success;
			}
		} catch (Exception ex) {
			logger.error("tcJob 등록 중 오류 발생 [file = {}] \n{}", tcJob, ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.DAOError;
		} finally {

			logger.debug("tcJob 등록 결과 [file = {}] => {}", tcJob, result);
		}

		return result;
	}

	

	public IResult modifyTcJob(String tcId, String state)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				// 기존의 사용자 정보를 가져온다
				TcJob tcJob = tcJobDao.selectTcJob(em, new TcJobSelectCondition(tcId, null));
				if( tcJob == null)
				{
					result = CommonResult.NotFoundInstanceError;
					break _TRANS;
				}
				
				tcJob.updateState(state);

				// DB 에 사용자를 등록 한다
				tcJob = tcJobDao.updateTcJob(em, tcJob);
				
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("tcJob 수정 오류 [tcId={}] \n{} ", 
						tcId, 
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}
		
		em.close();
		logger.debug("tcJob 수정 결과 [tcId={}] => {} ", tcId, result);
		
		return result;
	}

}
