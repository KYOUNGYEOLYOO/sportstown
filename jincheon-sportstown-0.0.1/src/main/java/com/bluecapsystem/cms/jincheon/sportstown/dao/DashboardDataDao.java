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
	
	@Query(value = "SELECT dd.sportsEvent.name, count(*) FROM DashboardData dd where dd.dataType= :dataType group by dd.sportsEvent.name ")
	List<DashboardData> findGroupByData(@Param("dataType") DataType dataType);

	@Query(value = "SELECT to_char(dd.registDate, 'mm'), count(*) FROM DashboardData dd WHERE to_char(dd.registDate, 'yyyy') = :year and dd.dataType= :dataType group by to_char(dd.registDate, 'mm') order by to_char(dd.registDate, 'mm') asc  ")
	List<DashboardData> findGroupByMonthData( @Param("year") String year, @Param("dataType") DataType dataType);

	@Query(value = "SELECT to_char(dd.registDate, 'mm'), count(*) FROM DashboardData dd WHERE dd.sportsEventCode = :sportsEventCode and to_char(dd.registDate, 'yyyy') = :year and dd.dataType= :dataType group by to_char(dd.registDate, 'mm') order by to_char(dd.registDate, 'mm') asc ")
	List<DashboardData> findGroupByColumnData( @Param("sportsEventCode") String sportsEventCode, @Param("year") String year, @Param("dataType") DataType dataType);
	
	@Query(value = "SELECT sportsEventCode, count(*) as cnt FROM DashboardData dd WHERE to_char(dd.registDate, 'yyyy') = :year and dd.dataType= :dataType group by dd.sportsEventCode order by cnt DESC ")
	List<DashboardData> findBestCodeData( @Param("year") String year, @Param("dataType") DataType dataType);
	
	@Query(value = "SELECT count(*) FROM DashboardData dd where dd.dataType= :dataType")
	List<DashboardData> findGroupByAllContent(@Param("dataType") DataType dataType);
	
	@Query(value = "SELECT count(*) FROM DashboardData dd where to_char(dd.registDate, 'yyyymmdd') = :today and dd.dataType= :dataType")
	List<DashboardData> findGroupByTodayContent( @Param("today") String today, @Param("dataType") DataType dataType);
	
	@Query(value = "SELECT dd.sportsEvent.name, dd.sportsEventCode FROM DashboardData dd where to_char(dd.registDate, 'yyyymm') = :today group by dd.sportsEvent.name, dd.sportsEventCode")
	List<DashboardData> statSportsCode( @Param("today") String today);
	
	@Query(value = "SELECT dd.sportsEventCode, dd.dataType FROM DashboardData dd where to_char(dd.registDate, 'yyyymm') = :today")
	List<DashboardData> typeCount( @Param("today") String today);
	
	
	@Query(value = "SELECT dd.dataId FROM DashboardData dd where dd.contentId = :contentId and dd.dataType= :dataType")
	List<DashboardData> contentViewCount( @Param("contentId") String contentId, @Param("dataType") DataType dataType);
	
}
