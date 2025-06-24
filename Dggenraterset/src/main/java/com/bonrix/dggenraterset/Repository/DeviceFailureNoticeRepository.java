package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.DeviceFailureNotice;

@Repository
public interface DeviceFailureNoticeRepository extends BaseRepository<DeviceFailureNotice, Long> {

	@Query("from DeviceFailureNotice where no=:no")
	public List<DeviceFailureNotice> findbyno(@Param("no") Long no);

	@Query(value = "SELECT df.no, df.failureminute, df.userid, df.warningminute,us.username FROM devicefailurenotice df JOIN users us ON us.id=df.userid ", nativeQuery = true)
	public List<Object[]> getDeviceFailureNoticeList();

	@Query(value = "select * from devicefailurenotice WHERE userid=:userID", nativeQuery = true)
	public List<Object[]> getUserDeviceFailureCount(@Param("userID") long userID);

	@Query(value = "SELECT df.no, df.failureminute, df.userid, df.warningminute FROM devicefailurenotice df WHERE df.userid=1", nativeQuery = true)
	public List<Object[]> getAdminDeviceFailure();

	@Query(value = "SELECT df.no, df.failureminute, df.userid, df.warningminute FROM devicefailurenotice df WHERE df.userid=:userID", nativeQuery = true)
	public List<Object[]> getManagerDeviceFailureById(@Param("userID") long userID);

	@Modifying
	@Transactional
	@Query("delete from DeviceFailureNotice d WHERE d.userid = ?1")
	void deleteManagerById(long userID);
	
	/*@Query(value ="SELECT failureminute, warningminute FROM devicefailurenotice WHERE userid=?1",nativeQuery=true)
	public List<Object[]> getAdminDeviceFailureData(long userID);
	
	
	@Query(value ="SELECT device_id,device_date FROM lasttrack WHERE device_id=:deviceID",nativeQuery=true)
	public List<Object[]> getDeviceDataById(@Param("deviceID") long deviceID);
	
	@Query(value ="SELECT device_id, device_date, system_date FROM lasttrack",nativeQuery=true)
	public List<Object[]> getDeviceFailureDateDiff();
		
	@Query(value ="SELECT * FROM devicemaster",nativeQuery=true)
	public List<Object[]> getTotalDeviceCount();*/	
	
	@Query(value ="SELECT failureminute, warningminute FROM devicefailurenotice WHERE userid=?1",nativeQuery=true)
	public List<Object[]> getManagerDeviceFailureData(long userID);		


		@Query(value ="SELECT lasttrack.device_date,lasttrack.system_date,lasttrack.device_id\r\n" + 
			"			FROM devicemaster inner  join lasttrack  on devicemaster.deviceid =lasttrack.device_id\r\n" + 
			"			where devicemaster.manager_id=?1",nativeQuery=true)
	public List<Object[]> getmanagerDeviceFailureDateDiff(long id);	
	
	@Query(value ="SELECT * from devicemaster where manager_id=?1",nativeQuery=true)
	public List<Object[]> getTotalManagerDeviceCount(long id);
	
	
	
	
	@Query(value ="SELECT lt.device_id, lt.device_date, lt.system_date,dm.devicename,dm.prid_fk,dp.profilename FROM lasttrack lt join devicemaster dm on dm.deviceid= lt.device_id join deviceprofile dp on dp.prid=dm.prid_fk",nativeQuery=true)
	public List<Object[]> getDeviceFailureDateDiff();
	
	@Query(value ="SELECT failureminute, warningminute FROM devicefailurenotice WHERE userid=?1",nativeQuery=true)
	public List<Object[]> getAdminDeviceFailureData(long userID);
	
	@Query(value ="SELECT * FROM devicemaster",nativeQuery=true)
	public List<Object[]> getTotalDeviceCount();
	
	@Query(value ="SELECT device_id,device_date FROM lasttrack WHERE device_id=:deviceID",nativeQuery=true)
	public List<Object[]> getDeviceDataById(@Param("deviceID") long deviceID);
	
	/*@Query(value ="SELECT prid_sett_id,warn_min, fail_min, paramname, profilename,rule_signature FROM profile_analog_setting WHERE profilename=:profName",nativeQuery=true)
	public List<Object[]> paramWarnFailByProfile(@Param("profName") String profName);*/
	
	@Query(value ="SELECT pm.id, pm.prmname,pd.criticalness,pd.profilename FROM profile_digital_setting pd JOIN parameter pm on pd.paramname=pm.prmname WHERE pd.profilename=:profName",nativeQuery=true)
	public List<Object[]> paramDigitalWarnFailByProfile(@Param("profName") String profName);
	
	@Query(value ="SELECT  device_id,digitals->>:paramId digitalvalue FROM lasttrack, jsonb_array_elements(digitaldata->'Digital') digitals  WHERE device_id=:deviceID AND digitals->>:paramId IS NOT NULL",nativeQuery=true)
	public List<Object[]> getLasttrackDigital(@Param("deviceID") long deviceID,@Param("paramId")String paramId);
	
	
	@Query(value ="SELECT pm.id,ps.prid_sett_id,ps.warn_min, ps.fail_min, ps.paramname, ps.profilename,ps.rule_signature FROM profile_analog_setting ps JOIN parameter pm on ps.paramname=pm.prmname WHERE ps.profilename=:profName",nativeQuery=true)
	public List<Object[]> paramWarnFailByProfile(@Param("profName") String profName);
	
	@Query(value ="SELECT  device_id,analogs->>:paramId analogvalue FROM lasttrack, jsonb_array_elements(digitaldata->'Analog') analogs  WHERE device_id=:deviceID AND analogs->>:paramId IS NOT NULL",nativeQuery=true)
	public List<Object[]> getLasttrackAnalog(@Param("deviceID") long deviceID,@Param("paramId")String paramId);
		
}
