package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bonrix.dggenraterset.Model.UserGroup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.UserGroup;

public interface UserGroupRepository extends BaseRepository<UserGroup, Long>{
	
	@Query(value ="SELECT ug.usergroupid, ug.usergroupname, ug.managerid,(SELECT COUNT(*) AS total FROM assignusergroup WHERE usrgroupid=ug.usergroupid ) AS UserCount,ug.is_active  FROM UserGroup ug JOIN users us ON ug.managerid=us.id where managerid=?1", nativeQuery = true)
	public List<Object[]> getUserGroupList(long managId);

	@Query("from UserGroup where managerid=:managerid")
	public List<UserGroup> findByManagerid(@Param("managerid") Long managerid);
}
