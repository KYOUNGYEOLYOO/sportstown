package com.bluecapsystem.cms.core.dao;

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

import com.bluecapsystem.cms.core.data.entity.Property;
import com.bluecapsystem.cms.core.data.entity.PropertyKey;

@Repository(value="PropertyDao")
public class PropertyDao 
{
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyDao.class);
	
	
	/**
	 * CMS 설정을 가져온다
	 * @param em
	 * @param key
	 * @return
	 */
	public Property selectProperty(EntityManager em, PropertyKey key)
	{
		try
		{
			return em.find(Property.class, key);
		}catch(NoResultException ex)
		{
			return null;
		}
	}
	
	/**
	 * CMS 설정 목록을 가져온다
	 * @param em
	 * @param propertyGroup
	 * @return
	 */
	public List<Property> selectProperties(EntityManager em, String propertyGroup)
	{
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Property> propQuery = cb.createQuery(Property.class);
		Root<Property> propRoot = propQuery.from(Property.class);

		
		propQuery.select(propRoot);
		propQuery.from(Property.class);
		
		Predicate p = cb.and(cb.equal(propRoot.get("propertyGroup"), propertyGroup));
		propQuery.where(p);
		
		TypedQuery<Property> query = em.createQuery(propQuery);
		
		try
		{
			return query.getResultList();
		}catch(NoResultException ex)
		{
			return new ArrayList<Property>();
		}
		
	}
	
	
	/**
	 * CMS 설정을 수정한다
	 * @param em
	 * @param property
	 * @return
	 */
	public Property updateProperty(EntityManager em, Property property)
	{
		logger.debug("CMS 설정을 변경 합니다 [property = {}]", property);
		return em.merge(property);
	}
	
	
	/**
	 * CMS 설정을 등록 한다
	 * @param em
	 * @param property
	 */
	public void insertProperty(EntityManager em, Property property)
	{
		logger.debug("CMS 설정을 등록 합니다 [property = {}]", property);
		em.persist(property);
	}
	
	
	/**
	 * CMS 설정을 삭제 한다
	 * @param em
	 * @param property
	 */
	public void deleteProperty(EntityManager em, Property property)
	{
		logger.debug("CMS 설정을 삭제 합니다 [property = {}]", property);
		em.remove(property);
	}

}
