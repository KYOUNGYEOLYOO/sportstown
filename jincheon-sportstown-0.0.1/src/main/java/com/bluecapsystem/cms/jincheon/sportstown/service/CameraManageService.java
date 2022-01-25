package com.bluecapsystem.cms.jincheon.sportstown.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.EmResult;
import com.bluecapsystem.cms.core.result.IResult;
import com.bluecapsystem.cms.jincheon.sportstown.dao.CameraDao;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.CameraSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera.CameraState;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.CameraStreamMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.result.UserResult;


@Service("CameraManageService")
public class CameraManageService
{
	private static final Logger logger = LoggerFactory.getLogger(CameraManageService.class);

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private CameraDao camDao;


	public Camera getCamera(String camId)
	{

		CameraSelectCondition condition = new CameraSelectCondition(camId);
		condition.setHasStreamMeta(true);
		return this.getCamera(condition);
	}


	public Camera getCamera(CameraSelectCondition condition)
	{
		logger.info("camera 단일 조회 시작 getCamera");
		EntityManager em = emf.createEntityManager();
		Camera camera = null;
		try
		{
			camera = camDao.selectCamera(em, condition);

			if(camera != null && condition.getHasStreamMeta())
			{
				camera.setStreamMetaItems(camDao.selectCameraStreamMetaList(em, camera.getCamId()));
			}

		}catch(Exception ex)
		{
			logger.error("카메라 조회중 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}

		logger.info("camera 단일 조회 결과 : {}", camera);
		return camera;
	}


	public List<Camera> getCameras(CameraSelectCondition condition) throws Exception
	{
		logger.info("getCmaeras///////////////////////11111");
		EntityManager em = emf.createEntityManager();
		List<Camera> cameras = null;

		try
		{
			cameras = camDao.selectCameraList(em, condition);
		}catch(Exception ex)
		{
			logger.error("카메라 목록 조회의 오류 발생 [condition={}]\n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex));
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		logger.info("getCmaeras///////////////////////2222222");
		logger.info("cameras : " + cameras);
		return cameras;
	}


	/* public IResult registCamera(Camera camera) */
	public EmResult registCamera(Camera camera)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();
		
		EmResult emResult = new EmResult();
		_TRANS :
		{
			try
			{
				em.getTransaction().begin();

				camDao.insertCamera(em, camera);
				for(CameraStreamMeta streamMeta : camera.getStreamMetaItems())
				{
					camDao.insertCameraStreamMeta(em, camera.getCamId(), streamMeta);
				}

				result = CommonResult.Success;
				/* em.getTransaction().commit(); */
				
				break _TRANS;


			}catch(Exception ex)
			{
				logger.error("카메라 등록 오류 [camera={}]\n{} ",
						camera,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				/* em.getTransaction().rollback(); */
				break _TRANS;
			}finally{
				/* em.close(); */
				emResult.setEm(em);
				emResult.setResult(result);
			}
		}


		logger.debug("카메라 등록 결과 [camera={}] => {} ", camera, result);

		return emResult;
	}


	public IResult modifyCamera(Camera newCamera)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();

		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				// 기존의 사용자 정보를 가져온다
				Camera camera = camDao.selectCamera(em, new CameraSelectCondition(newCamera.getCamId()));
				if( camera == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}

				camera.update(newCamera);
				newCamera = camDao.updateCamera(em, camera);

				for(CameraStreamMeta streamMeta : camera.getStreamMetaItems())
				{
					camDao.updateCameraStreamMeta(em, streamMeta);
				}

				result = CommonResult.Success;

				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("카메라 수정 오류 [newCamera={}] \n{} ",
						newCamera,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;

				break _TRANS;
			}
		}

		if(result == CommonResult.Success)
		{
			em.getTransaction().commit();
		}else
		{
			em.getTransaction().rollback();
		}

		em.close();
		logger.debug("카메라 수정 결과 [newCamera={}] => {} ", newCamera, result);

		return result;
	}


	public IResult changeStateCamera(String camId, CameraState state)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();

		_TRANS :
		{
			try
			{
				em.getTransaction().begin();
				// 기존의 사용자 정보를 가져온다
				Camera camera = camDao.selectCamera(em, new CameraSelectCondition(camId));
				if( camera == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}
				camera.setState(state);
				camera = camDao.updateCamera(em, camera);
				result = CommonResult.Success;
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("카메라 상태 수정 오류 [camId={}, state={}] \n{} ",
						camId,
						state,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;

				break _TRANS;
			}
		}

		if(result == CommonResult.Success)
		{
			em.getTransaction().commit();
		}else
		{
			em.getTransaction().rollback();
		}

		em.close();
		logger.debug("카메라 수정 결과 [camId={}, state={}] => {} ", camId, state, result);

		return result;
	}

	public IResult deleteCamera(String camId)
	{
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();

		_TRANS :
		{
			try
			{
				em.getTransaction().begin();

				// 기존 Login ID 중복을 확인한다\
				Camera camera = camDao.selectCamera(em, new CameraSelectCondition(camId));
				if( camera == null)
				{
					result = UserResult.UserNotFound;
					break _TRANS;
				}

				camera.setDeletedDate(new Date());
				camera.setIsDeleted(true);

				// DB 에 사용자를 삭제 한다
				camDao.updateCamera(em, camera);
				result = CommonResult.Success;
				em.getTransaction().commit();
				break _TRANS;
			}catch(Exception ex)
			{
				logger.error("카메라 삭제 오류 [camId={}]\n{} ",
						camId,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
				em.getTransaction().rollback();
				break _TRANS;
			}
		}

		em.close();
		logger.debug("카메라 삭제 결과 [camId={}] => {} ", camId, result);

		return result;
	}
}
