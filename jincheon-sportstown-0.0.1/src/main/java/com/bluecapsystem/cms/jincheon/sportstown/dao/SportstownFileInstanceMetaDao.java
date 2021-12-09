package com.bluecapsystem.cms.jincheon.sportstown.dao;

import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.bcs.util.DateUtil;
import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.dao.IFileInstanceMetaRepository;
import com.bluecapsystem.cms.core.data.condition.FileInstanceSelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.data.entity.FileInstanceMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.SportstownFileInstanceSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.Camera;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownFileInstanceMeta;

@Repository(value = "fileInstanceMetaRepository")
public class SportstownFileInstanceMetaDao implements IFileInstanceMetaRepository {

	private void getFileInstanceWhere(StringBuilder sql, HashMap<String, Object> params, FileInstanceSelectCondition condition) {
		if (EmptyChecker.isNotEmpty(condition.getFileId())) {
			sql.append("and f.fileId = :fileId ");
			params.put("fileId", condition.getFileId());
		}

		if (EmptyChecker.isNotEmpty(condition.getLocationRootCode())) {
			sql.append("and f.locationRootCode = :locationRootCode ");
			params.put("locationRootCode", condition.getLocationRootCode());
		}

		if (EmptyChecker.isNotEmpty(condition.getFilePath())) {
			sql.append("and f.locationRootCode = :locationRootCode ");
			params.put("locationRootCode", condition.getLocationRootCode());
		}

		if (EmptyChecker.isNotEmpty(condition.getFileName())) {
			sql.append("and f.fileName = :fileName ");
			params.put("fileName", condition.getFileName());
		}
		if (condition.getHasNotDeleted() == false) {
			sql.append("and f.isDeleted = false ");
		}
	}

	private void getFileMetaWhere(StringBuilder sql, HashMap<String, Object> params, SportstownFileInstanceSelectCondition condition) {
		if (EmptyChecker.isNotEmpty(condition.getSportsEventCode())) {
			sql.append("and meta.sportsEventCode = :sportsEventCode ");
			params.put("sportsEventCode", condition.getSportsEventCode());
		}

		if (EmptyChecker.isNotEmpty(condition.getCameraType())) {
			sql.append("and camera.cameraType = :cameraType ");
			params.put("cameraType", condition.getCameraType());
		}

		if (EmptyChecker.isNotEmpty(condition.getCamId())) {
			sql.append("and camera.camId = :camId ");
			params.put("camId", condition.getCamId());
		}

		if (EmptyChecker.isNotEmpty(condition.getRecordFromDate()) && EmptyChecker.isNotEmpty(condition.getRecordToDate())) {
			sql.append(" and ( meta.recordDate >= :recordFromDate and meta.recordDate < :recordToDate) ");
			params.put("recordFromDate", condition.getRecordFromDate());
			params.put("recordToDate", DateUtil.addDate(condition.getRecordToDate(), 1));
		}

	}

	@Override
	public Query selectFileList(EntityManager em, FileInstanceSelectCondition condition) throws Exception {

		if (condition instanceof SportstownFileInstanceSelectCondition == false) {
			return null;
		}
		SportstownFileInstanceSelectCondition metaCondition = (SportstownFileInstanceSelectCondition) condition;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f ");

		sql.append("FROM SportstownFileInstanceMeta meta " + "JOIN meta.file f " + "LEFT JOIN meta.camera camera " + "LEFT JOIN meta.sportsEvent sportEvent ");

		StringBuilder where = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();

		this.getFileInstanceWhere(where, params, metaCondition);
		this.getFileMetaWhere(where, params, metaCondition);

		sql.append("\nWHERE f.isDeleted = false \n");
		sql.append(where);
		sql.append("\n");
		sql.append("ORDER BY meta.recordDate desc, f.orignFileName, f.fileId ");

		Query query = em.createQuery(sql.toString());

		Paging pageParams = Optional.ofNullable(metaCondition.getPaging()).orElse( new Paging(0, Integer.MAX_VALUE, false));

		query.setFirstResult(pageParams.getFirstResult());
		query.setMaxResults(pageParams.getPageSize());

		StringBuilder countSql = new StringBuilder();
		countSql.append("SELECT COUNT(f) ");
		countSql.append("FROM SportstownFileInstanceMeta meta " + "JOIN meta.file f " + "LEFT JOIN meta.camera camera " + "LEFT JOIN meta.sportsEvent sportEvent ");
		countSql.append("\nWHERE f.isDeleted = false \n");
		countSql.append(where);
		TypedQuery<Long> countQuery = em.createQuery(countSql.toString(), Long.class);

		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
			countQuery.setParameter(key,  params.get(key));
		}


		Long totalCount = countQuery.getSingleResult();
		if(metaCondition.getPaging() != null) {
			metaCondition.getPaging().setTotalCount(totalCount);
		}


		return query;

	}

	@Override
	public void getWhere(EntityManager em, CriteriaBuilder cb, CriteriaQuery<FileInstance> queryFile, Root<FileInstance> root, FileInstanceSelectCondition con,
			List<Predicate> where) {
		if (con instanceof SportstownFileInstanceSelectCondition == false) {
			return;
		}

		SportstownFileInstanceSelectCondition condition = (SportstownFileInstanceSelectCondition) con;

		// SetJoin<FileInstance, SportstownFileInstanceMeta> joinMeta = root.joinSet("fileId", JoinType.INNER);

		Join<FileInstance, SportstownFileInstanceMeta> joinMeta = root.join("fileId", JoinType.INNER);
		Join<SportstownFileInstanceMeta, Camera> joinCam = joinMeta.join("camId", JoinType.INNER);

		if (EmptyChecker.isNotEmpty(condition.getSportsEventCode())) {
			Predicate p = cb.and(cb.equal(joinMeta.get("sportsEventCode"), condition.getSportsEventCode()));
			where.add(p);
		}
		if (EmptyChecker.isNotEmpty(condition.getCameraType())) {
			Predicate p = cb.and(cb.equal(joinCam.get("cameraType"), condition.getCameraType()));
			where.add(p);
		}

	}

	@Override
	public void insert(EntityManager em, FileInstance file, FileInstanceMeta meta) throws Exception {
		meta.setFileId(file.getFileId());

		if (meta instanceof SportstownFileInstanceMeta == false)
			throw new IllegalClassFormatException("FileInstanceMeta 가 SportstownFileInstanceMeta 유형이 아닙니다");

		em.persist(meta);
	}

	@Override
	public void update(EntityManager em, FileInstance file, FileInstanceMeta meta) throws Exception {
		meta.setFileId(file.getFileId());

		if (meta instanceof SportstownFileInstanceMeta == false)
			throw new IllegalClassFormatException("FileInstanceMeta 가 SportstownFileInstanceMeta 유형이 아닙니다");
		em.merge(meta);
	}

	@Override
	public void delete(EntityManager em, FileInstance file, FileInstanceMeta meta) throws Exception {

		if (meta instanceof SportstownFileInstanceMeta == false)
			throw new IllegalClassFormatException("FileInstanceMeta 가 SportstownFileInstanceMeta 유형이 아닙니다");
		em.remove(meta);
	}

}
