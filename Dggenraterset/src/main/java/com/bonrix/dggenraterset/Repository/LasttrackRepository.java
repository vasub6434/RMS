package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Repository.BaseRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LasttrackRepository extends BaseRepository<Lasttrack, Long> {
  List<Lasttrack> findByuserId(Long paramLong);
  
  List<Lasttrack> findBydeviceId(Long paramLong);
  
  @Query(value = "select  pa.key ,CAST(pa.value as text),dm.devicename from public.lasttrack lt JOIN devicemaster dm on dm.deviceid=lt.device_id  JOIN jsonb_array_elements(lt.digitaldata->'Analog') d ON true, jsonb_each(d)\tpa where lt.device_id=:deviceid", nativeQuery = true)
  List<Object[]> getdevicekeyvalbydid(@Param("deviceid") long paramLong);
  
  @Query(value = "select  pa.key ,CAST(pa.value as text),dm.devicename from public.lasttrack lt JOIN devicemaster dm on dm.deviceid=lt.device_id  JOIN jsonb_array_elements(lt.digitaldata->'Digital') d ON true, jsonb_each(d) pa where lt.device_id=:deviceid", nativeQuery = true)
  List<Object[]> getdevicekeyvaldigitalbydid(@Param("deviceid") long paramLong);
  
  @Query(value = "select ht.no,ht.device_id,prid_fk,dm.devicename from public.history ht JOIN devicemaster dm on dm.deviceid=ht.device_id  where ht.device_id=:deviceid and  ht.device_date  between TO_DATE(:startdate, 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(:enddate, 'YYYY-MM-DD HH24:MI:SS')+ interval '1 day'", nativeQuery = true)
  List<Object[]> getdhistorynobydid(@Param("deviceid") long paramLong, @Param("startdate") String paramString1, @Param("enddate") String paramString2);
  
  @Query(value = "select  pa.key ,CAST(pa.value as text),dm.devicename,TO_CHAR(ht.device_date, 'DD-MM-YYYY HH24:MI:SS') as device_date  from public.history ht JOIN devicemaster dm on dm.deviceid=ht.device_id  JOIN jsonb_array_elements(ht.digitaldata->'Analog') d ON true, jsonb_each(d)\tpa  where ht.no=:no ", nativeQuery = true)
  List<Object[]> getdhistoryanalogbyno(@Param("no") long paramLong);
  
  @Query(value = "select  pa.key ,CAST(pa.value as text),dm.devicename,TO_CHAR(ht.device_date, 'DD-MM-YYYY HH24:MI:SS') as device_date  from public.history ht JOIN devicemaster dm on dm.deviceid=ht.device_id  JOIN jsonb_array_elements(ht.digitaldata->'Digital') d ON true, jsonb_each(d)\tpa  where ht.no=:no ", nativeQuery = true)
  List<Object[]> getdhistorydigitalbyno(@Param("no") long paramLong);
  
  @Query(value = "select dp.prid,dp.profilename as devices,count(dm.deviceid) from public.deviceprofile dp  left JOIN devicemaster dm on dp.prid=dm.prid_fk group by dp.prid", nativeQuery = true)
  List<Object[]> adminprofiledevice();
  
  @Query(value = "select dm.deviceid,dm.devicename,us.username,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS') from devicemaster dm JOIN lasttrack lt on lt.device_id=dm.deviceid JOIN assignuserdevice au on au.device_id=lt.device_id JOIN users  us on us.id=au.manager_id where prid_fk=:prid", nativeQuery = true)
  List<Object[]> getdevicebyonlyprid(@Param("prid") long paramLong);
  
  @Query(value = "SELECT DISTINCT lasttrack.device_id, lasttrack.device_date, lasttrack.system_date,lasttrack.digitaldata, lasttrack.gpsdata, lasttrack.user_id FROM public.lasttrack inner join assignuserdevice on lasttrack.user_id=assignuserdevice.manager_id where assignuserdevice.user_id=:userId", nativeQuery = true)
  List<Lasttrack> getLastrackDigitalData(@Param("userId") long paramLong);
  
  @Query(value = "SELECT dm.deviceid,dm.devicename,lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'longitude' as longitude,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM') from lasttrack lt join devicemaster dm on dm.deviceid=lt.device_id where (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL)", nativeQuery = true)
  List<Object[]> getlasttackloc();
  
  @Query(value = "SELECT dm.deviceid,dm.devicename,lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'longitude' as longitude,\r\nTO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM') \r\nfrom lasttrack lt join assignsite ass on ass.deviceid=lt.device_id \r\njoin devicemaster dm on ass.deviceid=dm.deviceid\r\nwhere   ass.siteid=:siteId and ass.managerid=:managerId and (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL)", nativeQuery = true)
  List<Object[]> getDeviceDataByIdLocation(@Param("managerId") long paramLong1, @Param("siteId") long paramLong2);
  
  @Query(value = "SELECT TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM'),lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'angle' as angle,dm.devicename,lt.gpsdata->>'speed' as speed,lt.gpsdata->>'longitude' as longitude from lasttrack lt join devicemaster dm on dm.deviceid=lt.device_id where lt.device_id=:deviceId", nativeQuery = true)
  List<Object[]> getLiveLocation(@Param("deviceId") Long paramLong);
  
  @Query(value = "select dm.devicename,lt.device_date as devicedate,lt.gpsdata->>'latitude' as latitude,lt.gpsdata->>'longitude' as longitude,lt.gpsdata->>'speed' as speed,lt.gpsdata->>'angle' as angle from lasttrack lt join devicemaster dm on lt.device_id=dm.deviceid  where lt.device_id=:deviceid  and (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL)", nativeQuery = true)
  List<Object[]> getlivelasttracklocation(@Param("deviceid") long paramLong);
  
  @Query(value = "SELECT dm.deviceid,dm.devicename,lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'longitude' as longitude, TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM') from lasttrack lt join assignsite ass on ass.deviceid=lt.device_id join devicemaster dm on ass.deviceid=dm.deviceid join assignuserdevice ad on ad.device_id=dm.deviceid where ass.siteid=:siteId and ad.user_id=:userId and (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL)", nativeQuery = true)
  List<Object[]> getUserDeviceDataByIdLocation(@Param("userId") long paramLong1, @Param("siteId") long paramLong2);
  
  @Query(value = "SELECT dm.deviceid,dm.devicename,lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'longitude'as longitude,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM') from lasttrack lt join devicemaster dm on dm.deviceid=lt.device_id where (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL) and dm.deviceid in (select deviceid from devicemaster where manager_id=:managetId)", nativeQuery = true)
  List<Object[]> getDeviceLocationByManager(@Param("managetId") long paramLong);
  
  @Query(value = "SELECT dm.deviceid,dm.devicename,lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'longitude' as longitude,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM') from lasttrack lt join devicemaster dm on dm.deviceid=lt.device_id where (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL)and dm.deviceid in (select dm.deviceid from devicemaster dm join assignuserdevice ad on dm.deviceid=ad.device_id where user_id=:userId group by dm.deviceid)", nativeQuery = true)
  List<Object[]> getDeviceLocationByUser(@Param("userId") long paramLong);
  
  @Query(value = "SELECT lasttrack.device_id,to_char(lasttrack.device_date, 'yyyy-mm-dd hh12:mi:ss') as devicedate,devicemaster.devicename,devicemaster.altdevicename FROM public.lasttrack inner join assignuserdevice on lasttrack.device_id = assignuserdevice.device_id inner join public.devicemaster on public.lasttrack.device_id=public.devicemaster.deviceid where public.assignuserdevice.user_id=:userId order by  lasttrack.device_date desc", nativeQuery = true)
  List<Object[]> getAllLastTrackData(@Param("userId") long paramLong);
  
  @Query(value = "select device_id,devicename,altdevicename,TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS') as device_date, digitaldata->'Digital'->0->>'237935' as Mains_Fail,digitaldata->'Digital'->1->>'237937' as Battery_LVD, digitaldata->'Digital'->2->>'761793' as LOAD_ON_BATTERY, digitaldata->'Digital'->3->>'761816' as LOAD_ON_DG, digitaldata->'Digital'->4->>'761827' as LOW_BATTRY, digitaldata->'Digital'->5->>'11489123' as FUSE_FAIL  from lasttrack  join devicemaster  on devicemaster.deviceid=lasttrack.device_id where ( digitaldata->'Digital'->0->>'237935'='0' or  digitaldata->'Digital'->1->>'237937'='0' or  digitaldata->'Digital'->2->>'761793'='0' or  digitaldata->'Digital'->3->>'761816'='0' or  digitaldata->'Digital'->4->>'761827'='0' or  digitaldata->'Digital'->5->>'11489123'='0') and  device_date between TO_DATE(:deviceDate, 'YYYY-MM-DD') and TO_DATE(:deviceDate, 'YYYY-MM-DD')+ interval '1 day' order by device_date desc", nativeQuery = true)
  List<Object[]> getLiveGrideData(@Param("deviceDate") String paramString);   
  
  @Query(value = "SELECT lasttrack.device_id, lasttrack.device_date, lasttrack.system_date,lasttrack.digitaldata, lasttrack.gpsdata, lasttrack.user_id FROM public.lasttrack  where lasttrack.device_id=:deviceId and lasttrack.device_date between TO_DATE(:deviceDate, 'YYYY-MM-DD') and TO_DATE(:deviceDate, 'YYYY-MM-DD')+ interval '1 day'", nativeQuery = true)
  List<Lasttrack> getGolbalLiveGrideData(@Param("deviceId") long paramLong, @Param("deviceDate") String paramString);
  
  @Query(value = "SELECT device_id,analogs ->>:param digitalvalue,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS'),system_date from lasttrack,jsonb_array_elements(digitaldata->'Analog') analogs where device_date  between TO_DATE(:deviceDate, 'YYYY-MM-DD') and TO_DATE(:deviceDate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId and analogs ->>:param IS NOT NULL ", nativeQuery = true)
  List<Object[]> GetAnalogValue(@Param("deviceId") long paramLong, @Param("deviceDate") String paramString1, @Param("param") String paramString2);
  
  @Query(value = "select device_id,devicename,altdevicename,TO_CHAR(device_date, 'YYYY-MM-DD HH12:MI:SS AM') as device_date,\t\t\tdigitaldata->'Digital'->0->>'284945' as ACMAINS_FAIL,\t\t\tdigitaldata->'Digital'->1->>'6348798' as Fire,\t\t\tdigitaldata->'Digital'->2->>'291934' as Door,\t\t\tdigitaldata->'Digital'->3->>'6348854' as DG_Running_Hrs,\t\t\tdigitaldata->'Digital'->4->>'6348815' as DG_Fault,\t\t\tdigitaldata->'Digital'->5->>'6348821' as Battry_Low, \t\t\tdigitaldata->'Digital'->6->>'6348824' as PP_Input_Fail\t\t\t from lasttrack  join devicemaster  on devicemaster.deviceid=lasttrack.device_id where\t\t\t( digitaldata->'Digital'->0->>'284945'='0' or \t\t\t digitaldata->'Digital'->1->>'6348798'='0' or \t\t\t digitaldata->'Digital'->2->>'291934'='0' or \t\t\t digitaldata->'Digital'->3->>'6348854'='0' or \t\t\t digitaldata->'Digital'->4->>'6348815'='0' or \t\t\t digitaldata->'Digital'->5->>'6348821'='0' or \t\t\t digitaldata->'Digital'->6->>'6348824'='0') and \t\t\t device_date between TO_DATE(:deviceDate, 'YYYY-MM-DD') and TO_DATE(:deviceDate, 'YYYY-MM-DD')+ interval '1 day'                 AND lasttrack.device_id=:deviceId", nativeQuery = true)
  List<Object[]> VodeoconLiveGride(@Param("deviceId") long paramLong, @Param("deviceDate") String paramString);
  
  @Query(value = "select \t\t\tdigitaldata->'Digital'->0->>'284945' as ACMAINS_FAIL,\t\t\tdigitaldata->'Digital'->1->>'6348798' as Fire,\t\t\tdigitaldata->'Digital'->2->>'291934' as Door,\t\t\tdigitaldata->'Digital'->3->>'6348854' as DG_Running_Hrs,\t\t\tdigitaldata->'Digital'->4->>'6348815' as DG_Fault,\t\t\tdigitaldata->'Digital'->5->>'6348821' as Battry_Low, \t\t\tdigitaldata->'Digital'->6->>'6348824' as PP_Input_Fail \t\t\t from lasttrack  where                   lasttrack.device_id=:deviceId", nativeQuery = true)
  List<Object[]> VideoconLastTrack(@Param("deviceId") long paramLong);
  
  @Query(value = "SELECT dm.deviceid,dm.devicename,lt.gpsdata->>'latitude'as latitude,lt.gpsdata->>'longitude' as longitude,TO_CHAR(lt.device_date,'DD-MM-YYYY HH24:MI:SS AM') from lasttrack lt join devicemaster dm on dm.deviceid=lt.device_id where (lt.gpsdata->>'latitude' IS NOT NULL or lt.gpsdata->>'longitude' IS NOT NULL)and dm.deviceid in (select dm.deviceid from devicemaster dm join assignuserdevice ad on dm.deviceid=:deviceId where user_id=:userId group by dm.deviceid)", nativeQuery = true)
  List<Object[]> getDeviceLocation(@Param("userId") long paramLong1, @Param("deviceId") long paramLong2);
  
  @Query(value = "SELECT analogs ->>:paramId digitalvalue,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS'),system_date from lasttrack,jsonb_array_elements(digitaldata->'Analog') analogs where  device_id=:deviceId and analogs ->>:paramId IS NOT NULL", nativeQuery = true)
  List<Object[]> VodeoconAnalogVoltlData(@Param("paramId") String paramString, @Param("deviceId") long paramLong);
  
  @Query(value = "select count(*) from public.history ht " +  
          "JOIN devicemaster dm on dm.deviceid = ht.device_id " +
          "where ht.device_id = :deviceid and ht.device_date between " +
          "TO_DATE(:startdate, 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(:enddate, 'YYYY-MM-DD HH24:MI:SS') + interval '1 day'", nativeQuery = true)
  int getdhistorynobydidCount(@Param("deviceid") long deviceid,
                               @Param("startdate") String startdate,
                               @Param("enddate") String enddate);  
  
  @Query(value = "select ht.no, ht.device_id, prid_fk, dm.devicename from public.history ht " +
	        "JOIN devicemaster dm on dm.deviceid = ht.device_id " +
	        "where ht.device_id = :deviceid and ht.device_date between " +
	        "TO_DATE(:startdate, 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(:enddate, 'YYYY-MM-DD HH24:MI:SS') + interval '1 day' " +
	        "LIMIT :size OFFSET :offset", nativeQuery = true)
	List<Object[]> getdhistorynobydidPaginated(@Param("deviceid") long deviceid, 
	                                           @Param("startdate") String startdate, 
	                                           @Param("enddate") String enddate, 
	                                           @Param("offset") int offset, 
	                                           @Param("size") int size);

}
