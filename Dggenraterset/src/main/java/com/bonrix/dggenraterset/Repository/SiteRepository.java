package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bonrix.dggenraterset.Model.Site;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.UserGroup;

public interface SiteRepository extends BaseRepository<Site,Long>{
	
	@Query(value ="SELECT st.siteid, st.site_name, st.managerid,(SELECT COUNT(*) AS total FROM assignsite WHERE siteid=st.siteid ) AS DeviceCount,st.is_active  FROM site st JOIN users us ON st.managerid=us.id where st.managerid=?1", nativeQuery = true)
	public List<Object[]> getSiteList(long managId);
	
	//List<Site> findBysiteid(Long site_id);
	//digital input

		@Query("from Site where managerid=:managerid")
		public List<Site> findByManagerid(@Param("managerid") Long managerid);
		
		@Query(value ="SELECT DISTINCT au.siteid,st.site_name,(SELECT COUNT(*) AS total FROM assignsite WHERE siteid=st.siteid ) AS DeviceCount FROM assignsiteusers au  JOIN site st ON au.siteid=st.siteid WHERE au.userid=:userId", nativeQuery = true)
		public List<Object[]> getMySiteList(@Param("userId") long userId);
		
		@Query(value ="SELECT DISTINCT ag.siteid,st.site_name,(SELECT COUNT(*) AS total FROM assignsite WHERE siteid=st.siteid ) AS DeviceCount  FROM assignsiteusersgroup ag  JOIN site st ON ag.siteid=st.siteid WHERE ag.userid=:userId", nativeQuery = true)
		public List<Object[]> getMySiteListGrp(@Param("userId") long userId);	
		
}
