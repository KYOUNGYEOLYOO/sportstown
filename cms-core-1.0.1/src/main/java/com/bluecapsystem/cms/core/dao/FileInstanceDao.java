package com.bluecapsystem.cms.core.dao;

import java.util.ArrayList;
import java.util.Collections;
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
import com.bluecapsystem.cms.core.data.condition.FileInstanceSelectCondition;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;

@Repository(value = "FileInstanceDao")
public class FileInstanceDao {

	private static final Logger logger = LoggerFactory.getLogger(FileInstanceDao.class);

	@SuppressWarnings("unchecked")
	public List<FileInstance> selectFileList(EntityManager em, FileInstanceSelectCondition condition, IFileInstanceMetaRepository metaDao) throws Exception {

		List<FileInstance> files = Collections.emptyList();

		try {

			if (metaDao != null) {

				return metaDao.selectFileList(em, condition).getResultList();
			} else
				return selectFileList(em, condition);

		} catch (NoResultException ex) {
			logger.error("파일 목록 조회 오류", ex);
		}

		return files;
	}

	public List<FileInstance> selectFileList(EntityManager em, FileInstanceSelectCondition condition) throws Exception {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FileInstance> queryFile = cb.createQuery(FileInstance.class);

		Root<FileInstance> rootFile = queryFile.from(FileInstance.class);

		List<Predicate> whereFile = this.getWhere(cb, rootFile, condition);

		queryFile.select(rootFile);
		queryFile.where(whereFile.toArray(new Predicate[] {}));
		queryFile.orderBy(cb.asc(rootFile.get("registDate")));
		TypedQuery<FileInstance> query = em.createQuery(queryFile);

		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return new ArrayList<FileInstance>();
		}
	}

	public FileInstance selectFile(EntityManager em, FileInstanceSelectCondition condition, IFileInstanceMetaRepository metaDao) throws Exception {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FileInstance> queryFile = cb.createQuery(FileInstance.class);

		Root<FileInstance> rootFile = queryFile.from(FileInstance.class);

		List<Predicate> whereFile = this.getWhere(cb, rootFile, condition);

		if (metaDao != null) {
			metaDao.getWhere(em, cb, queryFile, rootFile, condition, whereFile);
		}

		queryFile.select(rootFile);
		queryFile.where(whereFile.toArray(new Predicate[] {}));
		queryFile.orderBy(cb.asc(rootFile.get("registDate")));
		TypedQuery<FileInstance> query = em.createQuery(queryFile);

		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public FileInstance selectFile(EntityManager em, String fileId) throws Exception {
		return selectFile(em, new FileInstanceSelectCondition(fileId, null), null);
	}

	public void insertFile(EntityManager em, FileInstance file) {
		String fileId = UniqueIDGenerator.createNumber().toString();
		file.setFileId(fileId);
		logger.debug("파일을 등록 합니다 [file = {}]", file);
		em.persist(file);
	}

	public FileInstance updateFile(EntityManager em, FileInstance file) {
		logger.debug("파일을 수정 합니다 [file = {}]", file);
		return em.merge(file);
	}

	public void deleteFile(EntityManager em, FileInstance file) {
		logger.debug("파일을 삭제 합니다 [file = {}]", file);
		em.remove(file);
	}

	private List<Predicate> getWhere(CriteriaBuilder cb, Root<FileInstance> root, FileInstanceSelectCondition condition) {
		List<Predicate> where = new ArrayList<Predicate>();

		if (EmptyChecker.isNotEmpty(condition.getFileId())) {
			Predicate p = cb.and(cb.equal(root.get("fileId"), condition.getFileId()));
			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(condition.getLocationRootCode())) {
			Predicate p = cb.and(cb.equal(root.get("locationRootCode"), condition.getLocationRootCode()));
			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(condition.getFilePath())) {
			Predicate p = cb.and(cb.equal(root.get("filePath"), condition.getFilePath()));
			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(condition.getFileName())) {
			Predicate p = cb.and(cb.equal(root.get("fileName"), condition.getFileName()));
			where.add(p);
		}

		if (condition.getHasNotDeleted() == false) {
			Predicate p = cb.and(cb.equal(root.get("isDeleted"), false));
			where.add(p);
		}

		return where;
	}

}
