package com.bluecapsystem.cms.jincheon.sportstown.dao;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.IllegalClassException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bcs.util.DateUtil;
import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.dao.IContentMetaRepository;
import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.data.entity.Content;
import com.bluecapsystem.cms.core.data.entity.Content.ContentState;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.SportstownContentSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.ContentUser;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.SportstownContentMeta;

@Repository(value = "contentMetaRepository")
public class SportstownContentMetaDao implements IContentMetaRepository {
	@SuppressWarnings("unchecked")
	@Override
	public List<ContentMeta> selectContentMetaList(EntityManager em, ISelectCondition condition) throws Exception {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<SportstownContentMeta> queryCt = cb.createQuery(SportstownContentMeta.class);
		Root<SportstownContentMeta> rootCt = queryCt.from(SportstownContentMeta.class);
		Join<SportstownContentMeta, Content> ctJoin = rootCt.join("content");

		// ctJoin.on(cb.not(ctJoin.get("state").in(Arrays.asList(ContentState.Delete, ContentState.Deleted))));
		{
			List<Predicate> qWhere = getWhere(cb, rootCt, condition);
			Predicate p = cb.and(cb.not(ctJoin.get("state").in(Arrays.asList(ContentState.Delete, ContentState.Deleted))));
			qWhere.add(p);

			queryCt.select(rootCt);
			queryCt.where(qWhere.toArray(new Predicate[] {}));
			queryCt.orderBy(cb.desc(rootCt.get("contentId")));
		}

		TypedQuery<SportstownContentMeta> query = em.createQuery(queryCt);

		if (condition instanceof IPagingable) {
			Paging paging = ((IPagingable) condition).getPaging();

			if (paging != null && paging.getEnablePaging() == true) {
				query.setFirstResult(paging.getFirstResult());
				query.setMaxResults(paging.getPageSize());

				// 사용자 정보의 total count 를 가져온다
				CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
				Root<SportstownContentMeta> countRoot = countQuery.from(SportstownContentMeta.class);
				Join<SportstownContentMeta, Content> countJoin = countRoot.join("content");
				{
					List<Predicate> qWhere = getWhere(cb, rootCt, condition);
					Predicate p = cb.and(cb.not(countJoin.get("state").in(Arrays.asList(ContentState.Delete, ContentState.Deleted))));
					qWhere.add(p);
					countQuery.select(cb.count(countRoot));
					// countQuery.select(cb.count(queryCt.from));
					countQuery.where(qWhere.toArray(new Predicate[] {}));
					Long totalCount = em.createQuery(countQuery).getSingleResult();
					paging.setTotalCount(totalCount);
				}
			}
		}

		try {
			@SuppressWarnings("rawtypes")
			List list = query.getResultList();
			return list;
		} catch (NoResultException ex) {
			return new ArrayList<ContentMeta>();
		}
	}

	@Override
	public ContentMeta selectContentMeta(EntityManager em, Content content) throws Exception {
		SportstownContentMeta meta = em.find(SportstownContentMeta.class, content.getContentId());

		meta.setContentUsers(this.seletectContentUsers(em, meta.getContentId(), null));

		return meta;
	}

	@Override
	public void insertContentMeta(EntityManager em, Content content, ContentMeta contentMeta) throws Exception {

		contentMeta.setContentId(content.getContentId());

		if (contentMeta instanceof SportstownContentMeta == false)
			throw new IllegalClassFormatException("ContentMeta가 SportstownContentMeta 타입이 아닙니다");
		SportstownContentMeta meta = (SportstownContentMeta) contentMeta;
		em.persist(meta);
		em.flush();

		insertContentUsers(em, meta);
	}

	@Override
	public ContentMeta updateContentMeta(EntityManager em, ContentMeta contentMeta) throws Exception {
		if (contentMeta instanceof SportstownContentMeta == false) {
			throw new IllegalClassException("ContentMeta 이 SportstownContentMeta 유형이 아닙니다 ");
		}

		SportstownContentMeta meta = (SportstownContentMeta) contentMeta;

		meta = em.merge(meta);
		em.flush();
		deleteContentUsers(em, meta);
		insertContentUsers(em, meta);
		return meta;
	}

	@Override
	public void deleteContentMeta(EntityManager em, ContentMeta contentMeta) throws Exception {

		if (contentMeta instanceof SportstownContentMeta == false) {
			throw new IllegalClassException("ContentMeta 이 SportstownContentMeta 유형이 아닙니다 ");
		}
		SportstownContentMeta meta = (SportstownContentMeta) contentMeta;

		em.remove(meta);
	}

	private List<ContentUser> seletectContentUsers(EntityManager em, String contentId, String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContentUser> queryCtUser = cb.createQuery(ContentUser.class);
		Root<ContentUser> rootCtUser = queryCtUser.from(ContentUser.class);

		List<Predicate> where = new ArrayList<Predicate>();

		if (EmptyChecker.isNotEmpty(contentId)) {
			Predicate p = cb.and(cb.or(cb.equal(rootCtUser.<String>get("contentId"), contentId)));

			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(userId)) {
			Predicate p = cb.and(cb.or(cb.equal(rootCtUser.<String>get("userId"), userId)));

			where.add(p);
		}

		queryCtUser.select(rootCtUser);
		queryCtUser.where(where.toArray(new Predicate[] {}));

		TypedQuery<ContentUser> query = em.createQuery(queryCtUser);

		try {
			List<ContentUser> list = query.getResultList();
			return list;
		} catch (NoResultException ex) {
			return new ArrayList<ContentUser>();
		}
	}

	@Transactional ( noRollbackFor = {EntityExistsException.class})
	private void insertContentUsers(EntityManager em, SportstownContentMeta meta) {

		for (ContentUser users : meta.getContentUsers()) {
			users.setContentId(meta.getContentId());
			em.persist(users);
		}
		em.flush();
	}

	private void deleteContentUsers(EntityManager em, SportstownContentMeta meta) {

		TypedQuery<ContentUser> query = em.createQuery("SELECT cu FROM ContentUser cu WHERE cu.contentId = :contentId ", ContentUser.class);

		query.setParameter("contentId", meta.getContentId());

		query.getResultList().forEach(user -> {
			em.remove(user);
		});
		em.flush();
	}

	private List<Predicate> getWhere(CriteriaBuilder cb, Root<SportstownContentMeta> root, ISelectCondition icon) {
		List<Predicate> where = new ArrayList<Predicate>();

		if (icon instanceof SportstownContentSelectCondition == false) {
			throw new RuntimeException("ISelectCondition 이 SportstownContentSelectCondition 유형이 아닙니다 ");
		}
		SportstownContentSelectCondition condition = (SportstownContentSelectCondition) icon;

		if (EmptyChecker.isNotEmpty(condition.getKeyword())) {
			Predicate p = cb.and(cb.or(cb.like(root.<String>get("title"), "%" + condition.getKeyword() + "%")));

			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(condition.getSportsEventCode())) {
			Predicate p = cb.and(cb.equal(root.get("sportsEventCode"), condition.getSportsEventCode()));
			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(condition.getTagUserId())) {
			Predicate p = cb.and(cb.equal(root.get("tagUserId"), condition.getTagUserId()));
			where.add(p);
		}

		if (EmptyChecker.isNotEmpty(condition.getRecordUserId())) {
			Predicate p = cb.and(cb.equal(root.get("recordUserId"), condition.getRecordUserId()));
			where.add(p);
		}
		
		// 0304 쿼리문 추가하려다가 모르겠어서 킵... // condition에 RecordFromDate / getRecordToDate getter, setter 추가완료
//		if (EmptyChecker.isNotEmpty(condition.getRecordFromDate()) && EmptyChecker.isNotEmpty(condition.getRecordToDate())) {
//			sql.append(" and ( meta.recordDate >= :recordFromDate and meta.recordDate < :recordToDate) ");
//			params.put("recordFromDate", condition.getRecordFromDate());
//			params.put("recordToDate", DateUtil.addDate(condition.getRecordToDate(), 1));
//		}

		return where;
	}

}
