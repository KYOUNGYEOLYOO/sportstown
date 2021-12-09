package com.bluecapsystem.cms.core.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluecapsystem.cms.core.dao.CodeDao;
import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.CodeGroup;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;

@Service("CodeService")
public class CodeService 
{
	private static final Logger logger = LoggerFactory.getLogger(CodeService.class);
	
	@Autowired
	private CodeDao codeDao;
	
	
	@Autowired
	private EntityManagerFactory emf;
	
	/**
	 * 코드 그룹 목록을 가져온다
	 * @param hasSystem
	 * @return
	 * @throws Exception
	 */
	public List<CodeGroup> getCodeGroups(Boolean hasSystem) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<CodeGroup> codeGroups = null;
		
		try
		{
			codeGroups = codeDao.selectCodeGroups(em, hasSystem);
			em.close();
		}catch(Exception ex)
		{
			logger.error("코드 그룹 목록 조회중 오류 발생 [hasSystem={}] \n{}",
					hasSystem,
					ExceptionUtils.getFullStackTrace(ex)
				);
			em.close();
			throw ex;
		}finally{
			
		}
		
		return codeGroups;
		
	}
	
	/**
	 * 코드 그룹을 가져온다
	 * @param groupCode
	 * @return
	 * @throws Exception
	 */
	public CodeGroup getCodeGroup(String groupCode) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CodeGroup codeGroup = null;
		
		try
		{
			codeGroup = codeDao.selectCodeGroup(em, groupCode);
		}catch(Exception ex)
		{
			logger.error("코드 그룹 조회중 오류 발생 [groupCode={}] \n{}",
					groupCode,
					ExceptionUtils.getFullStackTrace(ex)
				);
			em.close();
			throw ex;
		}finally{
			em.close();
		}
		
		return codeGroup;
	}
	
	
	/**
	 * 코드 목록을 가져온다
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(CodeSelectCondition condition) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		List<Code> codes = null;
		try
		{
			codes  = this.getCodes(em, condition);
		}catch(Exception ex)
		{
			em.close();
			throw ex;
		}finally{
			em.close();
		}
		return codes;
	}
	
	/**
	 * 코드 목록을 가져온다
	 * @param em
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private List<Code> getCodes(EntityManager em, CodeSelectCondition condition) throws Exception
	{
		List<Code> codes = null;
		try
		{
			codes  = codeDao.selectCodes(em, condition);
		}catch(Exception ex)
		{
			logger.error("코드 목록 조회중 오류 발생 [condition={}] \n{}",
					condition,
					ExceptionUtils.getFullStackTrace(ex)
				);
			throw ex;
		}
		return codes;
	}
	
	/**
	 * 코드 정보를 가져온다
	 * @param codeId
	 * @return
	 * @throws Exception
	 */
	public Code getCode(String codeId) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		Code code = null;
		try
		{
			code = this.getCode(em, codeId);
		}catch(Exception ex)
		{
			em.close();
			throw ex;
		}finally{
			em.close();
		}	
		return code;
	}
	
	
	/**
	 * 코드 정보를 가져온다
	 * @param em
	 * @param codeId
	 * @return
	 * @throws Exception
	 */
	private Code getCode(EntityManager em, String codeId) throws Exception
	{
		Code code = null;
		try
		{
			code = codeDao.selectCode(em, codeId);
		}catch(Exception ex)
		{
			logger.error("코드 조회중 오류 발생 [codeId={}] \n{}",
					codeId,
					ExceptionUtils.getFullStackTrace(ex)
				);
			throw ex;
		}	
		return code;
	}
	
	/**
	 * 코드를 등록한다
	 * @param code
	 * @return
	 */
	public IResult registCode(Code code)
	{
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		IResult result = this.registCode(em, code);
		
		
		if(result == CommonResult.Success)
			em.getTransaction().commit();
		else
			em.getTransaction().rollback();
		em.close();
		return result;
	}
	
	/**
	 * 코드를 등록한다
	 * @param em
	 * @param code
	 * @return
	 */
	private IResult registCode(EntityManager em,  Code code)
	{
		IResult result = CommonResult.UnknownError;
		try
		{
			
			code.setIndex(0);
			codeDao.insertCode(em, code);
			result = CommonResult.Success;
		}catch(Exception ex)
		{
			logger.error("코드 등록중 오류 발생 [code = {}] \n{}",
					code,
					ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.DAOError;
		}finally
		{
			logger.debug("코드 등록 결과 [code = {}] => {}", code, result);
		}
		return result;
	}
	
	
	/**
	 * 코드를 수정한다
	 * @param code
	 * @return
	 */
	public IResult modifyCode(Code code)
	{
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		IResult result = this.modifyCode(em, code);
		
		if(result == CommonResult.Success)
		{
			em.getTransaction().commit();
		}else
		{
			em.getTransaction().rollback();
		}
		
		em.close();
		
		return result;
	}
	
	/**
	 * 코드를 수정한다
	 * @param em
	 * @param code
	 * @return
	 */
	private IResult modifyCode(EntityManager em, Code code)
	{
		IResult result = CommonResult.UnknownError;
		_TRANSACTION:
		{
			Code srcCode = null;
			try{
				srcCode = codeDao.selectCode(em, code.getCodeId());
				if(srcCode == null)
				{
					result = CommonResult.NotFoundInstanceError;
					break _TRANSACTION;
				}
				
				if(srcCode.update(code) == false)
				{
					result = CommonResult.WrongParamertError;
					break _TRANSACTION;
				}
				
				code = codeDao.updateCode(em, srcCode);
				result = CommonResult.Success;
			}catch(Exception ex)
			{
				logger.error("코드 수정중 오류 발생 [code = {}] \n{}",
						code,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			}finally
			{
				logger.debug("코드 수정 결과 [code = {}] => {}", code, result);
			}
		}
		return result;
	}
	
	
	/**
	 * 코드를 삭제 한다
	 * @param codeId
	 * @return
	 */
	public IResult removeCode(String codeId)
	{
		IResult result = CommonResult.UnknownError;
		
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		
		result = this.removeCode(em, codeId);
		
		if(result == CommonResult.Success)
		{
			em.getTransaction().commit();
		}else
		{
			em.getTransaction().rollback();
		}
		em.close();
		return result;
	}
	
	
	/**
	 * 코드를 삭제한다
	 * @param em
	 * @param codeId
	 * @return
	 */
	private IResult removeCode(EntityManager em, String codeId)
	{
		IResult result = CommonResult.UnknownError;
		
		Code srcCode = null;
		_TRANSACTION:
		{
			try
			{
				srcCode = codeDao.selectCode(em, codeId);
				if(srcCode == null)
				{
					result = CommonResult.NotFoundInstanceError;
					break _TRANSACTION;
				}
				srcCode.setIsDeleted(true);
				codeDao.updateCode(em, srcCode);
				result = CommonResult.Success;
			}catch(Exception ex)
			{
				logger.error("코드 삭제중 오류 발생 [codeId = {}] \n{}",
						codeId,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			}finally
			{
				logger.debug("코드 수정 결과 [codeId = {}] => {}", codeId, result);
			}
		}
		return result;
	}
	
}
