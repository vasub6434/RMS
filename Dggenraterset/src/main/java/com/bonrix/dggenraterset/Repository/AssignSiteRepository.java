package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.AssignSite;

public interface AssignSiteRepository extends BaseRepository<AssignSite, Long> {

	@Modifying
	@Transactional
	@Query("delete from AssignSite u where u.siteid = ?1")
	void deleteSiteAssignId(long site_id);

	@Query(value = "SELECT dm.deviceid,dm.devicename FROM devicemaster dm JOIN assignsite ast ON ast.deviceid=dm.deviceid WHERE ast.siteid=:site_id", nativeQuery = true)
	public List<Object[]> AssignedDeviceBySite(@Param("site_id") long site_id);

	List<AssignSite> findBysiteid(Long site_id);

	@Query(value = "SELECT username,contact, email, name, addedby FROM users inner join assignuserdevice on users.id=assignuserdevice.user_id where addedby=?2 and assignuserdevice.device_id=?1", nativeQuery = true)
	List<Object[]> getuserBySevices(Long device_id, Long manager_id);

	/*
	 * @Query("SELECT * from assignsite where device_id IN :ids") void
	 * deactivateAll(@Param("ids") Iterable<Long> ids);
	 */

	@Query(value = "SELECT deviceid FROM AssignSite where device_id IN ?1", nativeQuery = true)
	List<Object[]> demo(Iterable<Long> ids);
	/*
	 * @Query("select u from User u where u.firstname = :#{#customer.firstname}")
	 * List<User> findUsersByCustomersFirstname(@Param("customer") Customer
	 * customer);
	 */

	@Query(value = "SELECT u.contact , u.email FROM users u inner join assignusergroup on u.id = assignusergroup.userid where usrgroupid=?1 and  u.addedby=?2", nativeQuery = true)
	List<Object[]> getuserfromGroup(Long usergroup_id, Long managerId);

	@Query(value = "FROM AssignSite WHERE deviceid=:deviceid AND siteid=:siteid")
	List<AssignSite> findBySiteidAndeviceid(@Param("deviceid") long deviceid, @Param("siteid") long siteid);

	@Query(value = "SELECT id, contact, email, name FROM users where addedby=?2 and id in(select DISTINCT userid from assignusergroup where usrgroupid=?1)", nativeQuery = true)
	List<Object[]> getuserByGroupId(Long usergroup_id, Long managerid);

	@Modifying
	@Transactional
	@Query("delete from AssignSiteUsers u where u.userid = ?1")
	void deleteUserAssignsite(long user_id);

	@Modifying
	@Transactional
	@Query("delete from AssignSiteUsersGroup u where u.groupid = ?1")
	void deleteUserAssignsiteusergrp(long user_id);
	List<AssignSite> findBydeviceid(Long deviceid);
	
//	@Query("SELECT count(*) FROM assignsite where site_id=?1 and device_id=?2", nativeQuery = true)
	@Query(value = "SELECT count(*) FROM assignsite where siteid=?1 and deviceid=?2", nativeQuery = true)
	int checkAvailableDeviceInSite(long site_id,long device_id);
	
	
}
