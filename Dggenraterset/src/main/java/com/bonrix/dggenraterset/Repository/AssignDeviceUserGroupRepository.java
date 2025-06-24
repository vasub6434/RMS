package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.AssignDeviceUserGroup;

public interface AssignDeviceUserGroupRepository extends BaseRepository<AssignDeviceUserGroup,Long>{
	
	@Modifying
	@Transactional
	@Query("delete from AssignDeviceUserGroup u where u.deviceid = ?1")
	void deleteUserGrpByDevice(long deviceId);
	
	@Query(value = "select DISTINCT users.id,users.username from  users left join assignuserdevice on users.id = assignuserdevice.user_id where users.id  IN (select  DISTINCT assignuserdevice.user_id from assignuserdevice  where  assignuserdevice.device_id=:deviceId)", nativeQuery = true)
	public abstract List<Object[]> getMyUserByDevice(@Param("deviceId") Long deviceId);
	
	
	@Modifying
	@Transactional
	@Query("delete from AssignUserDevice u where u.device_id = ?1")
	void deleteUserByDevice(long deviceId);
}
