package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.AssignUserGroup;

public interface AssignUserGroupRepository extends BaseRepository<AssignUserGroup, Long> {
	@Modifying
	@Transactional
	@Query("delete from AssignUserGroup u where u.usrgroupid = ?1")
	void deleteUserGroup(long usrgroup_id);

	@Query(value = "SELECT us.id,us.name FROM users us JOIN assignusergroup asg ON asg.userid=us.id WHERE asg.usrgroupid=:grp_id", nativeQuery = true)
	public List<Object[]> AssignedUsersByGrp(@Param("grp_id") long grp_id);

	@Query(value = "SELECT DISTINCT us.siteid,us.site_name FROM site us JOIN assignsiteusersgroup asg ON asg.siteid=us.siteid WHERE asg.groupid=:grp_id", nativeQuery = true)
	public List<Object[]> GetAssignedSiteByGrp(@Param("grp_id") long grp_id);

	@Query(value = "SELECT DISTINCT us.siteid,us.site_name FROM site us JOIN assignsiteusers asg ON asg.siteid=us.siteid WHERE asg.userid=:user_id", nativeQuery = true)
	public List<Object[]> GetAssignedSiteByUser(@Param("user_id") long user_id);
}
