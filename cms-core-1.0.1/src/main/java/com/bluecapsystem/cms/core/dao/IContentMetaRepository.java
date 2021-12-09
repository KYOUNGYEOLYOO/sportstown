package com.bluecapsystem.cms.core.dao;

import java.util.List;

import javax.persistence.EntityManager;


import com.bluecapsystem.cms.core.data.condition.ISelectCondition;
import com.bluecapsystem.cms.core.data.entity.Content;
import com.bluecapsystem.cms.core.data.entity.ContentMeta;

public interface IContentMetaRepository
{

	List<ContentMeta> selectContentMetaList(EntityManager em, ISelectCondition condition) throws Exception;
	ContentMeta selectContentMeta(EntityManager em, Content content) throws Exception;
	
	void insertContentMeta(EntityManager em, Content content, ContentMeta contentMeta) throws Exception;
	ContentMeta updateContentMeta(EntityManager em, ContentMeta contentMeta) throws Exception;
	void deleteContentMeta(EntityManager em, ContentMeta contentMeta) throws Exception;
	
	
}
