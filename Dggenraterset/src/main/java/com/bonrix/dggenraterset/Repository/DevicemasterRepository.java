package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Repository.BaseRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DevicemasterRepository extends BaseRepository<Devicemaster, Long> {
  @Query(value = "select deviceprofile.profilename,devicemaster.deviceid,devicemaster.devicename,deviceprofile.prid from deviceprofile join devicemaster on  deviceprofile.prid=devicemaster.prid_fk", nativeQuery = true)
  List<Object[]> joinlist();
  
  Devicemaster findByImei(String paramString);
  
  Devicemaster findByDevicename(String paramString);
  
  List<Devicemaster> findByuserId(Long paramLong);
  
  Devicemaster findBydeviceid(Long paramLong);
  
  @Query(value = "select * from devicemaster", nativeQuery = true)
  List<Object[]> getAllDeviceMasterData();
  
  @Query(value = "select * from devicemaster where devicemaster.manager_id=0 ", nativeQuery = true)
  List<Object[]> getDeviceMasterDataByManagerId();
  
  @Transactional
  @Modifying
  @Query(value = "UPDATE devicemaster u set manager_id =:managerId where u.deviceid  IN (:deviceId)", nativeQuery = true)
  void updateDevicemaster(@Param("managerId") long paramLong, @Param("deviceId") String paramString);
  
  @Query(value = "select DISTINCT  deviceid,devicename,altdevicename from  devicemaster left join assignuserdevice on devicemaster.deviceid = assignuserdevice.device_id where  deviceid  IN (select DISTINCT assignuserdevice.device_id from assignuserdevice  where  assignuserdevice.user_id=?1)", nativeQuery = true)
  List<Object[]> getMyDeviced(Long paramLong);
  
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM devicemaster u  where u.deviceid =:deviceId", nativeQuery = true)
  void deleteBydeviceId(@Param("deviceId") Long paramLong);
  
  List<Devicemaster> findBymanagerId(Long paramLong);
  
  @Query(value = "select dm.deviceid,dm.devicename,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS') from devicemaster dm join lasttrack lt on lt.device_id=dm.deviceid join assignuserdevice ad on ad.device_id=dm.deviceid where dm.prid_fk=:prid and ad.user_id=:userid", nativeQuery = true)
  List<Object[]> getdevicebyprid(@Param("prid") long paramLong1, @Param("userid") long paramLong2);
  
  @Query(value = "select DISTINCT dm.deviceid,dm.devicename,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS') from devicemaster dm join lasttrack lt on lt.device_id=dm.deviceid join assignuserdevice ad on ad.device_id=dm.deviceid where dm.prid_fk=:prid and ad.manager_id=:manager_id", nativeQuery = true)
  List<Object[]> getdevicebyprmid(@Param("prid") long paramLong1, @Param("manager_id") long paramLong2);
  
  @Query(value = "select DISTINCT  deviceid,devicename from  devicemaster left join assignuserdevice on devicemaster.deviceid = assignuserdevice.device_id where assignuserdevice.manager_id=?1 AND assignuserdevice.user_id=?2", nativeQuery = true)
  List<Object[]> managetDeviceList(long paramLong1, long paramLong2);
  
  @Query(value = "select * from devicemaster where devicemaster.manager_id=:managerId", nativeQuery = true)
  List<Object[]> getDeviceByManagerId(@Param("managerId") long paramLong);
  
  @Query(value = "SELECT DISTINCT aud.id, aud.device_id, aud.manager_id, aud.user_id,dm.devicename,dm.devicemodel,dm.imei,dm.simcardno FROM assignuserdevice aud join devicemaster dm on dm.deviceid=aud.device_id  where aud.user_id=:userId", nativeQuery = true)
  List<Object[]> getDeviceForUser(@Param("userId") long paramLong);
  
  @Query(value = "SELECT DISTINCT aud.id, aud.device_id, aud.manager_id, aud.user_id,dm.devicename,dm.devicemodel,dm.imei,dm.simcardno,dm.altdevicename from devicemaster dm inner join assignuserdevice aud on dm.deviceid=aud.device_id where aud.user_id=:userId", nativeQuery = true)
  List<Object[]> getDeviceAssignForUser(@Param("userId") long paramLong);
  
  @Query(value = "SELECT au.managerid,au.siteid,au.userid,st.site_name from assignsiteusers au join site st on st.siteid=au.siteid where au.userid=:userId", nativeQuery = true)
  List<Object[]> getSiteForUser(@Param("userId") long paramLong);
  
  @Query(value = "SELECT siteid,site_name from site where managerid=:managerId", nativeQuery = true)
  List<Object[]> getSiteForManager(@Param("managerId") long paramLong);
  
  @Modifying
  @Query(value = "DELETE FROM lasttrack u  where u.device_id =:device_id", nativeQuery = true)
  void deleteBylastrack(@Param("device_id") Long paramLong);
  
  @Modifying
  @Query(value = "DELETE FROM history u  where u.device_id =:device_id", nativeQuery = true)
  void deletehBydeviceId(@Param("device_id") Long paramLong);
  
  @Modifying
  @Query(value = "DELETE FROM assignuserdevice u where u.device_id =:device_id", nativeQuery = true)
  void deleteassignuserdevice(@Param("device_id") Long paramLong);
  
  @Query(value = "select id,deviceid,managerid,siteid from assignsite where deviceid=:deviceid", nativeQuery = true)
  List<Object[]> getsiteidbydeviceid(@Param("deviceid") Long paramLong);
  
  @Modifying
  @Query(value = "DELETE FROM assignsiteusers u where u.siteid =:siteid", nativeQuery = true)
  void deleteassignusers(@Param("siteid") Long paramLong);
  
  @Modifying
  @Query(value = "DELETE FROM assignsiteusersgroup u where u.siteid =:siteid", nativeQuery = true)
  void deleteassignsiteusergroup(@Param("siteid") Long paramLong);
  
  @Modifying
  @Query(value = "DELETE FROM assignsite u where u.deviceid =:deviceid", nativeQuery = true)
  void deleteassignsite(@Param("deviceid") Long paramLong);
  
  @Query(value = " SELECT  devicemaster.devicename,devicemaster.imei,devicemaster.simcardno,devicemaster.devicedescription,devicemaster.devicemodel,devicemaster.deviceid FROM public.devicemaster inner join users on devicemaster.managerid=users.id", nativeQuery = true)
  List<Object[]> getusersBymanagerId();
  
  @Query(value = "SELECT ast.deviceid,ast.managerid,ast.siteid,dm.devicename,dm.devicemodel,dm.simcardno,dm.imei from assignsite ast join devicemaster dm on dm.deviceid=ast.deviceid where ast.siteid=:siteId", nativeQuery = true)
  List<Object[]> getDeviceBySite(@Param("siteId") long paramLong);
  
  @Query(value = "SELECT siteid,site_name From site ", nativeQuery = true)
  List<Object[]> getAdminSite();
  
  @Query(value = "SELECT devicemaster.devicename,devicemaster.imei,devicemaster.simcardno,devicemaster.devicedescription, devicemaster.devicemodel,devicemaster.deviceid FROM devicemaster where devicemaster.manager_id=?1", nativeQuery = true)
  List<Object[]> getBymanagerId(long paramLong);
  
  @Query(value = "SELECT us.username,dm.devicename,dm.imei,dm.simcardno,dm.devicedescription,dm.devicemodel,TO_CHAR(dm.registerdate,'DD-MM-YYYY HH12:MI:SS'),dm.deviceid,dm.prid_fk,dm.altdevicename,dp.profilename,us.id FROM devicemaster dm join users us on us.id=dm.manager_id join deviceprofile dp on dp.prid=dm.prid_fk  where dm.userid_fk=?1 order by dm.registerdate desc", nativeQuery = true)
  List<Object[]> getdeviceByadminId(long paramLong);
  
  @Query(value = "select devicename from devicemaster where deviceid=?1", nativeQuery = true)
  List<Object[]> getlistDevice(long paramLong);
  
  @Query(value = "SELECT distinct ad.device_id, dm.devicename,dm.prid_fk FROM assignuserdevice ad JOIN devicemaster dm ON ad.device_id=dm.deviceid WHERE ad.manager_id=:managerId", nativeQuery = true)
  List<Object[]> getDevieByManager(@Param("managerId") long paramLong);
  
  @Query(value = "SELECT dm.devicename,dm.devicemodel,dm.imei,dm.simcardno,dm.devicedescription,dm.flagcondition,lt.device_date,lt.system_date FROM devicemaster dm  join lasttrack lt on lt.device_id=dm.deviceid where dm.deviceid=:deviceid", nativeQuery = true)
  List<Object[]> getDevieByProfile(@Param("deviceid") long paramLong);
  
  @Query(value = "SELECT prid,profilename FROM deviceprofile WHERE prid=:prId", nativeQuery = true)
  List<Object[]> getProfileByDevice(@Param("prId") long paramLong);
  
  @Transactional
  @Modifying
  @Query(value = "UPDATE devicemaster u set devicename =:devicename,altdevicename =:altdevicename where u.deviceid=:deviceid", nativeQuery = true)
  void updateDevicename(@Param("deviceid") long paramLong, @Param("devicename") String paramString1, @Param("altdevicename") String paramString2);
  
  @Query(value = "SELECT deviceid, devicename FROM devicemaster where manager_id=:managerId and deviceid not in (select deviceid_fk from analog_data where managerid=:managerId);", nativeQuery = true)
  List<Object[]> getAnalogSettingsDevice(@Param("managerId") long paramLong);
  
  @Query(value = "SELECT us.username,dm.devicename,dm.imei,dm.simcardno,dm.devicedescription,dm.devicemodel,dm.deviceid,dm.prid_fk,dm.altdevicename,dp.profilename FROM devicemaster dm join users us on us.id=dm.manager_id join deviceprofile dp on dp.prid=dm.prid_fk where dm.userid_fk=?1", nativeQuery = true)
  List<Object[]> getAlldeviceByadminId(long paramLong);
  
  @Query(value = "SELECT deviceid,devicename,imei,altdevicename FROM devicemaster WHERE manager_id =:managerId", nativeQuery = true)
  List<Object[]> getSajanDevieByManagerId(@Param("managerId") long paramLong);
  
  @Query(value = "SELECT\tDISTINCT ON (deviceprofile.profilename) profilename,deviceprofile.prid  FROM devicemaster inner join assignsite on devicemaster.deviceid=assignsite.deviceid inner join deviceprofile on devicemaster.prid_fk=deviceprofile.prid inner join assignuserdevice on devicemaster.deviceid=assignuserdevice.device_id where assignuserdevice.user_id=:userId and assignsite.siteid=:siteId", nativeQuery = true)
  List<Object[]> getUserprofileBySiteId(@Param("userId") long paramLong1, @Param("siteId") long paramLong2);
  
  @Query(value = "SELECT \tdeviceid,devicename,imei FROM public.devicemaster inner join assignuserdevice on devicemaster.deviceid=assignuserdevice.device_id  where prid_fk=:profileId and assignuserdevice.user_id=:userId", nativeQuery = true)
  List<Object[]> getprofileDevice(@Param("userId") long paramLong1, @Param("profileId") long paramLong2);
  
  @Query(value = "SELECT prid_fk FROM devicemaster WHERE deviceid = :deviceId", nativeQuery = true)
  Long findPridFkByDeviceId(@Param("deviceId") Long deviceId);

}
