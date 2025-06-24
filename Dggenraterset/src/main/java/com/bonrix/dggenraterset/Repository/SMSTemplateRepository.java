package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Repository.BaseRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SMSTemplateRepository extends BaseRepository<MessageTemplate, Long> {
  @Query("from MessageTemplate where mid=:mid and templatetype=:temptype")
  MessageTemplate findByIdAndAlerTypet(@Param("mid") long paramLong, @Param("temptype") String paramString);
  
  @Query(value = " SELECT device_id,       devicename,       altdevicename,       To_char(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date,       digitaldata->'Digital'->0->>'284945'             AS acmains_fail,       digitaldata->'Digital'->1->>'6348798'            AS fire,       digitaldata->'Digital'->2->>'291934'             AS door,       digitaldata->'Digital'->3->>'6348854'            AS dg_running_hrs,       digitaldata->'Digital'->4->>'6348815'            AS dg_fault,       digitaldata->'Digital'->5->>'6348821'            AS battry_low,       digitaldata->'Digital'->6->>'6348824'            AS pp_input_fail  FROM   lasttrack  JOIN   devicemaster  ON     devicemaster.deviceid=lasttrack.device_id  WHERE (              digitaldata->'Digital'->0->>'284945'='0'        OR     digitaldata->'Digital'->1->>'6348798'='0'        OR     digitaldata->'Digital'->2->>'291934'='0'        OR     digitaldata->'Digital'->3->>'6348854'='0'        OR     digitaldata->'Digital'->4->>'6348815'='0'        OR     digitaldata->'Digital'->5->>'6348821'='0'        OR     digitaldata->'Digital'->6->>'6348824'='0'        or device_date < current_TIMESTAMP - interval '2 HOUR') AND    lasttrack.device_id=:deviceId", nativeQuery = true)
  List<Object[]> VodeoconNewLiveGride(@Param("deviceId") long paramLong);
  
  @Query(value = "SELECT TO_CHAR(entrytime,'YYYY-MM-DD HH12:MI:SS'),usergroupid FROM alertmessageshistory WHERE deviceid=:deviceId and alerttype='DIGITAL' and usergroupid=:digitalInputId order by entrytime desc limit 1", nativeQuery = true)
  List<Object[]> liveGrideNewAlertTime(@Param("deviceId") long paramLong1, @Param("digitalInputId") long paramLong2);
  
  @Query(value = "SELECT device_id,\t\t\t       devicename,\t\t\t       altdevicename,\t\t\t       To_char(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date,\t\t\t       devicedata->'Digital'->>'284945'  AS acmains_fail,\t\t\t       devicedata->'Digital'->>'6348798' AS fire,\t\t\t       devicedata->'Digital'->>'291934'  AS door,\t\t\t       devicedata->'Digital'->>'6348854' AS dg_running_hrs,\t\t\t       devicedata->'Digital'->>'6348815' AS dg_fault,\t\t\t       devicedata->'Digital'->>'6348821' AS battry_low,\t\t\t       devicedata->'Digital'->>'6348824'  AS pp_input_fail \t\t\t FROM   lasttrack \t\t\t JOIN   devicemaster  \t\t\t ON     devicemaster.deviceid=lasttrack.device_id    \t\t\t WHERE (  devicedata->'Digital'->>'284945'='0'  \t\t\t       OR     devicedata->'Digital'->>'6348798'='0' \t\t\t       OR     devicedata->'Digital'->>'291934'='0'  \t\t\t       OR     devicedata->'Digital'->>'6348854'='0' \t\t\t       OR     devicedata->'Digital'->>'6348815'='0' \t\t\t       OR     devicedata->'Digital'->>'6348821'='0' \t\t\t       OR     devicedata->'Digital'->>'6348824'='0'  \t\t\t       or device_date < current_TIMESTAMP - interval '2 HOUR')  \t\t\t AND    lasttrack.device_id=:deviceId", nativeQuery = true)
  List<Object[]> VodeoconNewLiveNewGride(@Param("deviceId") long paramLong);
  
  @Query(value = "SELECT device_id,devicename,altdevicename,To_char(device_date, 'YYYY-MM-DD HH12:MI:SS AM') AS device_date, devicedata->'Digital'->>'6348798' AS fire,  devicedata->'Digital'->>'291934'  AS door  FROM   lasttrack  \t\t\t\t\t\t inner JOIN   devicemaster   \t\t\t\t\t\t ON     devicemaster.deviceid=lasttrack.device_id  \t\t\t\t\t\t WHERE (devicedata->'Digital'->>'6348798'='0' \t\t\t\t\t\t       OR     devicedata->'Digital'->>'291934'='0'  \t\t\t\t\t\t       or device_date < current_TIMESTAMP - interval '2 HOUR')   \t\t\t\t\t\t       and devicedata->'Digital'->>'6348798' is not null \t\t\t\t\t\t       and devicedata->'Digital'->>'291934' is not null  \t\t\t\t\t\t AND  lasttrack.device_id IN (select DISTINCT assignuserdevice.device_id from assignuserdevice  where  assignuserdevice.user_id=:userId) ", nativeQuery = true)
  List<Object[]> GetVodeoconSelectedLiveGride(@Param("userId") long paramLong);
}
