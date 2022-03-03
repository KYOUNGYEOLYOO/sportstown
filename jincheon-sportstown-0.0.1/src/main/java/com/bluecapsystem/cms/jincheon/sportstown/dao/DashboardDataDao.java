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
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.DashboardData.DataType;
import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User;



public interface DashboardDataDao extends JpaRepository<DashboardData, String>
{
	
	@Query(value = "SELECT dd.sportsEvent.name, count(*) FROM DashboardData dd group by dd.sportsEvent.name ")
	List<DashboardData> findGroupByData();

	@Query(value = "SELECT to_char(dd.registDate, 'mm'), count(*) FROM DashboardData dd WHERE to_char(dd.registDate, 'yyyy') = :year group by to_char(dd.registDate, 'mm') order by to_char(dd.registDate, 'mm') asc  ")
	List<DashboardData> findGroupByMonthData( @Param("year") String year);

	@Query(value = "SELECT to_char(dd.registDate, 'mm'), count(*) FROM DashboardData dd WHERE dd.sportsEventCode = :sportsEventCode and to_char(dd.registDate, 'yyyy') = :year group by to_char(dd.registDate, 'mm') order by to_char(dd.registDate, 'mm') asc ")
	List<DashboardData> findGroupByColumnData( @Param("sportsEventCode") String sportsEventCode, @Param("year") String year);
	
	@Query(value = "SELECT sportsEventCode, count(*) as cnt FROM DashboardData dd WHERE to_char(dd.registDate, 'yyyy') = :year group by dd.sportsEventCode order by cnt DESC ")
	List<DashboardData> findBestCodeData( @Param("year") String year);
	
	@Query(value = "SELECT count(*) FROM DashboardData dd ")
	List<DashboardData> findGroupByAllContent();
	
	@Query(value = "SELECT count(*) FROM DashboardData dd where to_char(dd.registDate, 'yyyymmdd') = :today")
	List<DashboardData> findGroupByTodayContent( @Param("today") String today);
}
