package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bonrix.dggenraterset.Model.Devicemaster;

@Repository
public interface GetDeviceWithParameter extends JpaRepository<Devicemaster, Long> {

    @Query(value =
        "SELECT dm.deviceid, " +
        "       dm.devicename, " +
        "       dp.profilename, " +
        "       jsonb_array_elements_text(dp.parameters->'Digital') AS digital_value " +
        "FROM devicemaster dm " +
        "JOIN deviceprofile dp ON dm.prid_fk = dp.prid " +
        "WHERE dm.deviceid = :deviceId",
        nativeQuery = true
    )
    List<Object[]> getDeviceData(@Param("deviceId") Long deviceId);

    
	@Query(value = "SELECT dm.deviceid, " + 
			"       dm.devicename, " + 
			"       dp.profilename, "
			+ "       jsonb_array_elements_text(dp.parameters->'Digital') AS digital_value " + "FROM devicemaster dm "
			+ "JOIN deviceprofile dp ON dm.prid_fk = dp.prid "
			+ "WHERE dp.profilename IN (:profileList)", nativeQuery = true)
	List<Object[]> getAllDeviceData(@Param("profileList") List<String> profileList);
	
	@Query(value = "SELECT DISTINCT aud.device_id, dm.devicename FROM assignuserdevice aud join devicemaster dm on dm.deviceid=aud.device_id  where aud.user_id=:userId", nativeQuery = true)
	  List<Object[]> getDeviceForUser(@Param("userId") long paramLong);
	
	@Query(value = "select deviceid,devicename from devicemaster where devicemaster.manager_id=:managerId", nativeQuery = true)
	List<Object[]> getDeviceByManagerId(@Param("managerId") long managerId); 

}
