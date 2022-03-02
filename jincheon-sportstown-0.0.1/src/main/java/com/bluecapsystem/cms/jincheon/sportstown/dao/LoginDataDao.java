package com.bluecapsystem.cms.jincheon.sportstown.dao;

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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.IPagingable;
import com.bluecapsystem.cms.core.data.condition.Paging;
import com.bluecapsystem.cms.core.generator.UniqueIDGenerator;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.DashboardDataSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.conditions.UserSelectCondition;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.LoginData;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;



public interface LoginDataDao extends JpaRepository<LoginData, String>
{
	
	@Query(value = "SELECT ld.userId FROM LoginData ld where ld.registDate between to_timestamp(:value,  'YYYY-MM-DD HH24:MI:SS') AND CURRENT_TIMESTAMP group by ld.userId")
	List<LoginData> findGroupByLoginData(@Param("value") String value);

	@Query(value = "SELECT ld.userId FROM LoginData ld WHERE ld.registDate between(current_timestamp - interval '5min') and current_timestamp group by ld.userId",nativeQuery = true)
	List<LoginData> findGroupByData();
}
