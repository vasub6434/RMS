package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.AssignSiteUsers;

public interface AssignSiteUserRepository extends BaseRepository<AssignSiteUsers, Long> {

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

	@Modifying
	@Transactional
	@Query("delete from AssignSiteUsers u where u.siteid = ?1")
	void deleteUserBySite(long siteId);

	@Modifying
	@Transactional
	@Query("delete from AssignSiteUsersGroup u where u.siteid = ?1")
	void deleteUserGrpBySite(long siteId);

	@Query(value = "select  DISTINCT usergroup.usergroupid,usergroup.usergroupname from  usergroup left join assignsiteusersgroup on usergroup.usergroupid = assignsiteusersgroup.groupid where  usergroup.usergroupid  IN (select  DISTINCT assignsiteusersgroup.groupid from assignsiteusersgroup  where  assignsiteusersgroup.siteid=:siteId)", nativeQuery = true)
	public abstract List<Object[]> getMyUserGrpBySite(@Param("siteId") Long siteId);

	@Query(value = "select DISTINCT  site.siteid,site.site_name from  site left join assignsiteusers on site.siteid = assignsiteusers.siteid where  site.siteid  IN (select DISTINCT assignsiteusers.siteid from assignsiteusers  where  assignsiteusers.userid=:userId)", nativeQuery = true)
	public abstract List<Object[]> getMySite(@Param("userId") Long userId);

	@Query(value = "select   DISTINCT users.id,users.username from  users left join assignsiteusers on users.id = assignsiteusers.userid where  users.id  IN (select  DISTINCT assignsiteusers.userid from assignsiteusers  where  assignsiteusers.siteid=:siteId)", nativeQuery = true)
	public abstract List<Object[]> getMyUserBySite(@Param("siteId") Long siteId);
	
	@Query(value = "SELECT deviceid, managerid 	FROM assignsite where siteid=:siteId", nativeQuery = true)
	public abstract List<Object[]> getMyDeviceBySite(@Param("siteId") Long siteId);
	
	@Query(value = "select DISTINCT  site.siteid,site.site_name from  site left join assignsiteusers on site.siteid = assignsiteusers.siteid where  site.siteid  IN (select DISTINCT assignsiteusers.siteid from assignsiteusers  where  assignsiteusers.managerid=:managerId)", nativeQuery = true)
	public abstract List<Object[]> getManagerSite(@Param("managerId") Long managerId);

}
