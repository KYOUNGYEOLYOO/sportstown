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

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.CodeSelectCondition;
import com.bluecapsystem.cms.core.data.entity.Code;
import com.bluecapsystem.cms.core.data.entity.CodeGroup;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;

@Repository(value="CodeDao")
public class CodeDao 
{
	
	private static final Logger logger = LoggerFactory.getLogger(CodeDao.class);
	
	
	/**
	 * 코드 그룹 목록을 가져온다
	 * @param em
	 * @param hasSystem
	 * @return
	 */
	public List<CodeGroup> selectCodeGroups(EntityManager em, Boolean hasSystem)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CodeGroup> queryGroup = cb.createQuery(CodeGroup.class);
		
		Root<CodeGroup> rootGroup = queryGroup.from(CodeGroup.class);
		
		queryGroup.select(rootGroup);
		// queryGroup.from(CodeGroup.class);
		
		if(hasSystem != true)
		{
			Predicate p = cb.and(cb.equal(rootGroup.get("isSystem"), false));
			queryGroup.where(p);
		}
		queryGroup.orderBy(cb.asc(rootGroup.get("name")));
		TypedQuery<CodeGroup> query = em.createQuery(queryGroup);
		try
		{
			return query.getResultList();
		}catch(NoResultException ex)
		{
			return null;
		}
	}
	
	/**
	 * 코드 그룹을 가져온다
	 * @param em
	 * @param propertyGroup
	 * @return
	 */
	public CodeGroup selectCodeGroup(EntityManager em, String groupCode)
	{
		return em.find(CodeGroup.class, groupCode);
	}
	
	/**
	 * 코드 목록을 가져온다
	 * @param em
	 * @param key
	 * @return
	 */
	public List<Code> selectCodes(EntityManager em, CodeSelectCondition condition)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Code> queryCode = cb.createQuery(Code.class);
		
		Root<Code> rootCode = queryCode.from(Code.class);
		
		queryCode.select(rootCode);
		// queryCode.from(Code.class);
		
		List<Predicate> whereCode = this.getCodeWhere(cb, rootCode, condition);
	
		queryCode.where(whereCode.toArray(new Predicate[]{}));
		queryCode.orderBy(cb.asc(rootCode.get("name")), cb.asc(rootCode.get("index")));
		TypedQuery<Code> query = em.createQuery(queryCode);
		
		try
		{
			return query.getResultList();
		}catch(NoResultException ex)
		{
			return new ArrayList<Code>();
		}
	}
	
	
	/**
	 * 코드를 가져온다
	 * @param em
	 * @param key
	 * @return
	 */
	public Code selectCode(EntityManager em, String codeId)
	{
		return em.find(Code.class, codeId);
	}
	
	/**
	 * 코드의 where 절을 생성 한다
	 * @param cb
	 * @param rootCode
	 * @param condition
	 * @return
	 */
	private List<Predicate> getCodeWhere(CriteriaBuilder cb, Root<Code> rootCode, CodeSelectCondition condition)
	{
	
		List<Predicate> where = new ArrayList<Predicate>();
		
		if(EmptyChecker.isNotEmpty(condition.getGroupCode()))
		{
			Predicate p = cb.and(cb.equal(rootCode.get("groupCode"), condition.getGroupCode()));
			where.add(p);
		}
		
		if(EmptyChecker.isNotEmpty(condition.getCodeId()))
		{
			Predicate p = cb.and(cb.equal(rootCode.get("codeId"), condition.getCodeId()));
			where.add(p);
		}
		
		if(condition.getHasNotUsed() != true)
		{
			Predicate p = cb.and(cb.equal(rootCode.get("isUsed"), true));
			where.add(p);
		}
		
		Predicate p = cb.and(cb.equal(rootCode.get("isDeleted"), false));
		where.add(p);
		
		return where;
	}
	
	
	/**
	 * 코드를 수정한다
	 * @param em
	 * @param code
	 * @return
	 */
	public Code updateCode(EntityManager em, Code code)
	{
		logger.debug("코드를 변경 합니다 [code = {}]", code);
		return em.merge(code);
	}
	
	/**
	 * 코드를 등록 합니다
	 * @param em
	 * @param code
	 */
	public void insertCode(EntityManager em, Code code)
	{
		String codeId = UniqueIDGenerator.createNumber().toString();
		code.setCodeId(codeId);
		logger.debug("코드를 등록 합니다 [code = {}]", code);
		em.persist(code);
	}
	
	
	/**
	 * 코드를 삭제 합니다
	 * @param em
	 * @param code
	 */
	public void deleteCode(EntityManager em, Code code)
	{
		logger.debug("코드를 삭제 합니다 [code = {}]", code);
		em.remove(code);
	}
	
}
