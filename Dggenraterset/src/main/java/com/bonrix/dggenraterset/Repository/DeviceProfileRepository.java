package com.bonrix.dggenraterset.Repository;


import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.DeviceProfile;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.User;

import org.springframework.data.repository.query.Param;


@Repository
public interface DeviceProfileRepository extends BaseRepository<DeviceProfile,Long>{

	
	@Modifying   
	@Transactional
	@Query(value ="SELECT dp.profilename,dp.prid, COUNT(dm.deviceid) AS device_count FROM deviceprofile dp join devicemaster dm on dp.prid=dm.prid_fk join assignuserdevice ad on ad.device_id=dm.deviceid where ad.user_id=:user_id group by dp.prid ORDER BY device_count DESC", nativeQuery = true)
	public List<Object[]> Assigndeviceprofilebyuid(@Param("user_id") long user_id);
  
	@Query(value ="SELECT dp.profilename,dp.prid FROM deviceprofile dp join devicemaster dm on dp.prid=dm.prid_fk join assignuserdevice ad on ad.device_id=dm.deviceid where ad.manager_id=:manager_id group by dp.prid", nativeQuery = true)
	public List<Object[]> assigndeviceprofilebymanagerid(@Param("manager_id") long manager_id);

	@Query(value="SELECT profilename FROM deviceprofile where prid=?1",nativeQuery=true)
	public List<Object[]> getprofilename22(long prid);
	
	@Query(value="SELECT profilename FROM deviceprofile where prid=?1",nativeQuery=true)
	public String getprofilenameByID(long prid);
	
	@Query(value ="SELECT prid,profilename FROM deviceprofile", nativeQuery = true)
	public List getProfileList();	
	   
	@Query("from DeviceProfile where prid=:pid")
	public DeviceProfile findByProfile(@Param("pid") long userid);
	  
	 @Query(value = "SELECT device_id, devicename, altdevicename, To_char(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, devicedata->'Digital'->>'284945' AS acmains_fail, devicedata->'Digital'->>'6348798' AS fire, devicedata->'Digital'->>'291934' AS door, devicedata->'Digital'->>'6348854' AS dg_running_hrs, devicedata->'Digital'->>'6348815' AS dg_fault, devicedata->'Digital'->>'6348821' AS battry_low, devicedata->'Digital'->>'6348824' AS pp_input_fail, coalesce(maintenancestaff.mobile, 'Not Set') FROM lasttrack JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id WHERE (devicedata->'Digital'->>'284945' = '0' OR devicedata->'Digital'->>'6348798' = '0' OR devicedata->'Digital'->>'291934' = '0' OR devicedata->'Digital'->>'6348854' = '0' OR devicedata->'Digital'->>'6348815' = '0' OR devicedata->'Digital'->>'6348821' = '0' OR devicedata->'Digital'->>'6348824' = '0') AND cast(device_date AS date) =CURRENT_DATE AND lasttrack.device_id IN (select dm.deviceid from devicemaster dm join lasttrack lt on lt.device_id = dm.deviceid join assignuserdevice ad on ad.device_id = dm.deviceid where dm.prid_fk = :profileId and ad.user_id = :userId)", nativeQuery = true)
	  List<Object[]> getAllDeviceStatusDetails(@Param("userId") long paramLong1, @Param("profileId") long paramLong2);
	  
	  @Query(value = "SELECT device_id, devicename, altdevicename, TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, devicedata -> 'Digital' ->> '284945' AS acmains_fail, devicedata -> 'Digital' ->> '6348798' AS fire, devicedata -> 'Digital' ->> '291934' AS door, devicedata -> 'Digital' ->> '6348854' AS dg_running_hrs, devicedata -> 'Digital' ->> '6348815' AS dg_fault, devicedata -> 'Digital' ->> '6348821' AS battry_low, devicedata -> 'Digital' ->> '6348824' AS pp_input_fail, COALESCE(maintenancestaff.mobile, 'Not Set') FROM lasttrack JOIN devicemaster ON devicemaster.deviceid = lasttrack.device_id LEFT JOIN maintenancestaff ON maintenancestaff.deviceid = lasttrack.device_id WHERE (devicedata -> 'Digital' ->> '284945' = '0' OR devicedata -> 'Digital' ->> '6348798' = '0' OR devicedata -> 'Digital' ->> '291934' = '0' OR devicedata -> 'Digital' ->> '6348854' = '0' OR devicedata -> 'Digital' ->> '6348815' = '0' OR devicedata -> 'Digital' ->> '6348821' = '0' OR devicedata -> 'Digital' ->> '6348824' = '0') AND CAST(device_date AS DATE) =CURRENT_DATE AND lasttrack.device_id IN (SELECT dm.deviceid FROM devicemaster dm JOIN lasttrack lt ON lt.device_id = dm.deviceid JOIN assignuserdevice ad ON ad.device_id = dm.deviceid JOIN assignsite ast ON ast.deviceid = dm.deviceid WHERE dm.prid_fk = :profileId AND ad.user_id = :userId AND ast.siteid = :siteId)", nativeQuery = true)
	  public List<Object[]> getAllDeviceStatusBySite(@Param("userId") long userId,@Param("profileId") long profileId,@Param("siteId") long siteId);
	  
	  @Query(value ="SELECT dp.profilename, dp.prid, COUNT(dm.deviceid) AS device_count " +
	            "FROM deviceprofile dp " +
	            "JOIN devicemaster dm ON dp.prid = dm.prid_fk " +
	            "WHERE dm.manager_id = :manager_id " +
	            "GROUP BY dp.prid, dp.profilename ORDER BY device_count DESC", nativeQuery = true)
	  public List<Object[]> assigndeviceprofilebymanageridCount(@Param("manager_id") long manager_id);
	  
	  @Query(value = "SELECT jsonb_array_elements(d.parameters->'Digital')->>'parameterId' FROM deviceprofile d WHERE d.prid = :profileId", nativeQuery = true)
	  public String[] getparameterIdByProfile(@Param("profileId") Long profileId);
	  
	  @Query(value = "SELECT jsonb_array_elements(d.parameters->'Digital')->>'parametername' FROM deviceprofile d WHERE d.prid = :profileId", nativeQuery = true)
	  public String[] getparameterNameByProfile(@Param("profileId") Long profileId);
	  
	  @Query(value = "SELECT dm.deviceId as deviceId FROM devicemaster dm JOIN deviceprofile dp ON dm.prid_fk = dp.prid WHERE dp.prid = :profileId", nativeQuery = true)
	  public String[] getDeviceIdsByProfile(@Param("profileId") Long profileId);
	   
	  @Query(value = "SELECT * FROM deviceprofile WHERE prid IN (:ids)", nativeQuery = true)
	  List<DeviceProfile> getByPridList(@Param("ids") List<Long> ids);

	  DeviceProfile findByPrid(Long prid);

	  @Query(value="SELECT prid FROM deviceprofile where profilename = :profileName", nativeQuery=true)
	  Long getProfileIdByProfileName(@Param("profileName") String profileName);
	  
	@Query(value = " SELECT  " + " analog_elem ->> 'Analoginput' AS analogInput, "
			+ "analog_elem ->> 'analogname' AS analogName " + "FROM devicemaster dm  "
			+ "JOIN deviceprofile dp ON dm.prid_fk = dp.prid, "
			+ "jsonb_array_elements(dp.parameters -> 'Analog') AS analog_elem "
			+ "WHERE dm.deviceid = :deviceId", nativeQuery = true)
	List<Object[]> findAnalogParamsByDeviceId(@Param("deviceId") Long deviceId);

	@Query(value = " SELECT  " + "  digital_elem ->> 'parameterId' AS parameterId, "
			+ " digital_elem ->> 'parametername' AS parameterName " + "FROM devicemaster dm  "
			+ "JOIN deviceprofile dp ON dm.prid_fk = dp.prid, "
			+ "jsonb_array_elements(dp.parameters -> 'Digital') AS digital_elem "
			+ " WHERE dm.deviceid = :deviceId", nativeQuery = true)
	List<Object[]> findDigitalParamsByDeviceId(@Param("deviceId") Long deviceId);
	
	@Query(value = "SELECT " + 
            "analog_elem ->> 'Analogunit' AS analogUnit, " +
            "analog_elem ->> 'analogname' AS analogName, " +
            "analog_elem ->> 'max' AS max, " +  
            "analog_elem ->> 'min' AS min " +   
            "FROM devicemaster dm " +
            "JOIN deviceprofile dp ON dm.prid_fk = dp.prid, " +
            "jsonb_array_elements(dp.parameters -> 'Analog') AS analog_elem " +
            "WHERE dm.deviceid = :deviceId", nativeQuery = true)
List<Object[]> findAnalogParamsByDeviceIdMinAndMax(@Param("deviceId") Long deviceId);

	@Query(value = "SELECT " + "analog_elem ->> 'Analogunit' AS analogUnit, "
			+ "analog_elem ->> 'analogname' AS analogName, " + "analog_elem ->> 'max' AS max, "
			+ "analog_elem ->> 'min' AS min " + "FROM devicemaster dm "
			+ "JOIN deviceprofile dp ON dm.prid_fk = dp.prid, "
			+ "jsonb_array_elements(dp.parameters -> 'Analog') AS analog_elem " + "WHERE dm.deviceid = :deviceId "
			+ "AND analog_elem ->> 'Analoginput' = :prmkey", nativeQuery = true)
	List<Object[]> findAnalogParamsByDeviceIdAndPrmkey(@Param("deviceId") Long deviceId,
			@Param("prmkey") String prmkey);
	
}
