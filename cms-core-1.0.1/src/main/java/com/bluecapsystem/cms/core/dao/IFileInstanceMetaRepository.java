package com.bluecapsystem.cms.core.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.bluecapsystem.cms.core.data.condition.FileInstanceSelectCondition;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.FileInstanceMeta;

public interface IFileInstanceMetaRepository
{
	
	Query selectFileList(EntityManager em, FileInstanceSelectCondition condition) throws Exception;
	void getWhere(EntityManager em, CriteriaBuilder cb, CriteriaQuery<FileInstance> queryFile, Root<FileInstance> root, FileInstanceSelectCondition icon, List<Predicate> where);
	
	
	void insert(EntityManager em, FileInstance file, FileInstanceMeta meta) throws Exception;
	void update(EntityManager em, FileInstance file, FileInstanceMeta meta) throws Exception;
	void delete(EntityManager em, FileInstance file, FileInstanceMeta meta) throws Exception;
	
}
