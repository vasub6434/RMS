package com.bonrix.dggenraterset.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.repository.query.Param;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AssignUserDeviceRepository extends BaseRepository<AssignUserDevice, Long>{

	
	@Query("from AssignUserDevice where device_id=:device_id")
	public List<AssignUserDevice> findBydeviceid(@Param("device_id") long device_id);
	
	@Query("from AssignUserDevice where device_id=:device_id and manager_id=:managerid")
	public List<AssignUserDevice> findBydeviceidmang(@Param("device_id") long device_id,@Param("managerid") long managerid);
	
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM assignuserdevice ad  where ad.device_id =:deviceid and ad.manager_id=:managerid ", nativeQuery = true)
	public abstract void deleteassignuserdevice(@Param("deviceid") Long deviceid,@Param("managerid") Long managerid);
	
	
	
	
	
	
	
}