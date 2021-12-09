package com.bluecapsystem.cms.core.service;

import java.text.SimpleDateFormat;
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

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.dao.ContentDao;
import com.bluecapsystem.cms.core.dao.ContentInstanceDao;
import com.bluecapsystem.cms.core.dao.IContentMetaRepository;
import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.entity.Content;
import com.bluecapsystem.cms.core.data.entity.ContentInstance;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;
import com.bluecapsystem.cms.core.data.entity.FileInstance;
import com.bluecapsystem.cms.core.result.CommonResult;
import com.bluecapsystem.cms.core.result.IResult;

@Service("ContentService")
public class ContentService {
	private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

	@Autowired(required = true)
	@Qualifier("contentMetaRepository")
	private IContentMetaRepository metaDao;

	@Autowired
	private ContentDao contentDao;

	@Autowired
	private ContentInstanceDao instanceDao;

	@Autowired
	private FileInstanceService fileServ;

	@Autowired
	private EntityManagerFactory emf;

	public List<ContentMeta> getContentList(ISelectCondition condition) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<ContentMeta> contents = null;

		try {
			contents = metaDao.selectContentMetaList(em, condition);
		} catch (Exception ex) {
			logger.error("컨텐츠 목록 조회중 오류 발생 [condition={}] \n{}", condition, ExceptionUtils.getFullStackTrace(ex));
		} finally {
			em.close();
		}

		return contents;
	}

	public ContentMeta getContent(String contentId) {
		EntityManager em = emf.createEntityManager();
		ContentMeta meta = null;
		try {
			Content content = contentDao.selectContent(em, contentId);
			meta = metaDao.selectContentMeta(em, content);
			List<ContentInstance> instances = instanceDao.selectInstanceList(em, meta.getContentId());
			meta.getContent().setInstances(instances);
		} catch (Exception ex) {
			logger.error("컨텐츠 조회중 오류 발생 [contentId={}] \n{}", contentId, ExceptionUtils.getFullStackTrace(ex));
		} finally {
			em.close();
		}
		return meta;
	}

	public IResult registContent(Content content) {
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		result = this.registContent(em, content);

		if (result == CommonResult.Success) {
			em.getTransaction().commit();
		} else {
			em.getTransaction().rollback();
		}

		em.close();

		return result;
	}

	public IResult registContent(EntityManager em, Content content) {
		IResult result = CommonResult.UnknownError;
		_TRANS: {
			try {
				// 컨텐츠를 등록 한다
				contentDao.insertContent(em, content);

				String contentId = content.getContentId();

				content.getContentMeta().setContentId(contentId);
				metaDao.insertContentMeta(em, content, content.getContentMeta());

				em.flush();

				// 컨텐츠 인스턴스를 등록 한다
				for (ContentInstance instance : content.getInstances()) {
					instance.setContentId(contentId);
					instanceDao.insertInstance(em, instance);

					FileInstance fileInstance = fileServ.getFileinstance(em, instance.getFileId());

					SimpleDateFormat smf = new SimpleDateFormat("/yyyy/MM/dd");
					String descFilePath = String.format("%s/%s.%s", smf.format(new Date()), fileInstance.getFileId(), fileInstance.getExtension())
							.toLowerCase();

					em.flush();

					result = fileServ.transferFile(em, fileInstance, "CONTENT", descFilePath);
					if (result != CommonResult.Success)
						break _TRANS;
				}
				result = CommonResult.Success;
				break _TRANS;
			} catch (Exception ex) {
				logger.error("컨텐츠 저장중 오류 발생 [content={}] \n{}", content, ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.SystemError;
				break _TRANS;
			}
		}

		return result;
	}

	public IResult modifyContent(Content content) {
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		result = this.modifyContent(em, content);

		if (result == CommonResult.Success) {
			em.getTransaction().commit();
		} else {
			em.getTransaction().rollback();
		}
		em.close();
		return result;
	}

	public IResult modifyContent(EntityManager em, Content content) {
		IResult result = CommonResult.UnknownError;
		_TRANS: {
			try {
				// 컨텐츠를 수정한다
				Content orgContent = contentDao.selectContent(em, content.getContentId());
				ContentMeta orgMeta = metaDao.selectContentMeta(em, orgContent);


				orgContent.update(content);

				if (EmptyChecker.isNotEmpty(orgMeta)) {
					orgMeta.update(content.getContentMeta());
				}
				contentDao.updateContent(em, orgContent);
				metaDao.updateContentMeta(em, orgMeta);

				em.flush();
				result = CommonResult.Success;
				break _TRANS;
			} catch (Exception ex) {
				logger.error("컨텐츠 수정중 오류 발생 [content={}] \n{}", content, ExceptionUtils.getFullStackTrace(ex));
				result = CommonResult.SystemError;
				break _TRANS;
			}
		}
		return result;
	}


	public IResult deleteContent(String contentId) {
		IResult result = CommonResult.UnknownError;
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		result = this.deleteContent(em, contentId);

		if (result == CommonResult.Success) {
			em.getTransaction().commit();
		} else {
			em.getTransaction().rollback();
		}
		em.close();
		return result;
	}

	public IResult deleteContent(EntityManager em, String contentId) {
		IResult result = CommonResult.UnknownError;

		try {
			contentDao.deleteContent(em, contentId);
			em.flush();
			result = CommonResult.Success;

		}catch(Exception ex) {
			logger.error("컨텐츠 삭제중 오류 발생 [contentId={}] \n{}", contentId, ExceptionUtils.getFullStackTrace(ex));
			result = CommonResult.SystemError;
		}

		return result;
	}
}
