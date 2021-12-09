package com.bluecapsystem.cms.core.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluecapsystem.cms.core.dao.PropertyDao;
import com.bluecapsystem.cms.core.data.entity.Property;
import com.bluecapsystem.cms.core.data.entity.PropertyKey;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;

@Service("PropertyService")
public class PropertyService 
{
	private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);
	
	@Autowired
	private PropertyDao propDao;
	
	
	@Autowired
	private EntityManagerFactory emf;
	
	
	/**
	 * 시스템 설정을 가져온다
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Property getProperty(PropertyKey key) throws Exception
	{
		EntityManager em = emf.createEntityManager();
		
		Property property = null;
		
		try
		{
			property = propDao.selectProperty(em, key);
		}catch(Exception ex)
		{
			logger.error("CMS 시스템 설정 조회중 오류 발생 [key={}] \n{}",
					key,
					ExceptionUtils.getFullStackTrace(ex)
				);
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		return property;
	}
	
	
	public Property getProperty(String propertyGroup, String propertyCode)
	{
		try
		{
			return getProperty(new PropertyKey(propertyGroup, propertyCode));
		}catch(Exception ex)
		{
			return null;
		}
	}

	/**
	 * CMS 시스템의 그룹 설정 목록을 가져온다
	 * @param propertyGroup
	 * @return
	 * @throws Exception
	 */
	public List<Property> getProperties(String propertyGroup) throws Exception
	{
		
		EntityManager em = emf.createEntityManager();
		
		List<Property> properties = null;
		try
		{
			properties = propDao.selectProperties(em, propertyGroup);
		}catch(Exception ex)
		{
			logger.error("CMS 시스템 설정 목록 조회중 오류 발생 [propertyGroup={}] \n{}",
					propertyGroup,
					ExceptionUtils.getFullStackTrace(ex)
				);
			em.close();
			throw ex;
		}finally
		{
			em.close();
		}
		
		return properties;
	}
	
	
	
	public IResult registProperty(Property property)
	{
		
		EntityManager em = emf.createEntityManager();
		
		
		IResult result = registProperty(em, property);
	
		em.close();
		return result;
	}
	
	
	private IResult registProperty(EntityManager em, Property property)
	{
		IResult result = CommonResult.UnknownError;
		try
		{
			propDao.insertProperty(em,  property);
			result = CommonResult.Success;
		}catch(Exception ex)
		{
			logger.error("시스템 설정 등록중 오류 발생 [property = {}] \n{}", 
					property,
					ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.DAOError;
		}
		
		return result;
	}
	
	/**
	 * 시스템 설정을 변경 한다
	 * @param property
	 * @return
	 */
	public IResult modifyProperty(Property property)
	{
		EntityManager em = emf.createEntityManager();
	
		em.getTransaction().begin();
		IResult result = modifyProperty(em, property);
		
		
		if(result == CommonResult.Success)
		{
			em.getTransaction().commit();
		}else
		{
			em.getTransaction().rollback();
		}
		em.close();
		
		logger.debug("시스템 설정 변경 결과 => {}", result);
		return result;
	}
	
	public IResult modifyProperty(EntityManager em, Property property)
	{
		IResult result = CommonResult.UnknownError;

		_TRANS :
		{
			try 
			{
				Property orgProperty = em.find(Property.class, property.getKey());
				if (orgProperty == null) 
				{
					result = CommonResult.NotFoundInstanceError;
					logger.error("시스템 설정 원본을 찾을 수 없습니다 [propertyKey={}]", property.getKey());
					break _TRANS;
				}

				if (orgProperty.update(property) == true) 
				{
					propDao.updateProperty(em, orgProperty);
					result = CommonResult.Success;
				} else 
				{
					result = CommonResult.SystemError;
				}
			} catch (Exception ex)
			{
				logger.error("시스템 설정 변경 중 오류 발생 [property = {}] \n{}", property, ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			}
		}
		
		return result;
	}
	
	
	/**
	 * 시스템 설정을 삭제 한다
	 * @param key
	 * @return
	 */
	public IResult removeProperty(PropertyKey key)
	{
		
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		IResult result = this.removeProperty(em, key);
		
		if(result == CommonResult.Success)
		{
			em.getTransaction().commit();
		}else
		{
			em.getTransaction().rollback();
		}
	
		em.close();
		
		logger.debug("시스템 설정 삭제 결과 => {}", result);
		return CommonResult.UnknownError;
	}
	
	private IResult removeProperty(EntityManager em, PropertyKey key)
	{
		IResult result = CommonResult.UnknownError;
		
		_TRANS :
		{
			try
			{
				Property property = em.find(Property.class, key);
				
				if(property == null)
				{
					result = CommonResult.Success;
					break _TRANS;
				}
				propDao.deleteProperty(em,  property);
				result = CommonResult.Success;
			}catch(Exception ex)
			{
				logger.error("시스템 설정 변경 중 오류 발생 [propertyKey = {}] \n{}", 
						key,
						ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.DAOError;
			}
		}
		
		return result;
	}
}
