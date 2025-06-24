package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.MaintenanceStaff;

@Repository
public interface MaintenanceStaffRepository  extends BaseRepository<MaintenanceStaff, Long> {

	@Modifying
	@Transactional
	@Query("delete from MaintenanceStaff u where u.deviceid = ?1")
	void deleteStaffDevice(long userid);
	  
	
	@Query(value = "SELECT 	id,name,devicemaster.devicename,mobile,maintenancestaff.deviceid FROM public.maintenancestaff Inner join devicemaster on maintenancestaff.deviceid=devicemaster.deviceid WHERE 	maintenancestaff.deviceid   IN (select DISTINCT assignuserdevice.device_id from assignuserdevice  where  assignuserdevice.user_id=:userId)", nativeQuery = true)
	public List<Object[]> getStaffDetails(@Param("userId") long userId);


	MaintenanceStaff findBydeviceid(long deviceId);
}
