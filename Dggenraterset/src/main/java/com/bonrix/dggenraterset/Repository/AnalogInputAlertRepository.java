package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.AnalogInputAlert;

public interface AnalogInputAlertRepository extends JpaRepository<AnalogInputAlert, Long> {
  @Query(value = "SELECT no,di.alertlimit,COALESCE(dm.devicename,'0') as devicename,di.analoginput,di.email_id,COALESCE(et.templatename,'0')as emailtemplatename,di.managerid,COALESCE(mt.templatename,'0')as messagetemplatename,di.mobileno,di.notification,COALESCE(st.site_name,'0') as sitename,di.conditionstring,di.conditionvalue,di.timedifference,di.avgtime,di.timing,COALESCE(us.username,'0') as username,COALESCE(up.usergroupname,'0') as usergroupname,pm.prmname FROM analoginputalert di Left join devicemaster dm on dm.deviceid=di.deviceid Left join emailtemplate et on et.eid=di.emailtemplate_id Left join messagetemplate mt on mt.mid=di.messagetemplate_id Left join site st on st.siteid=di.site_id Left join users us on us.id=di.user_id Left join  usergroup up on up.usergroupid=di.usergroup_id join parameter pm on pm.id=CAST (di.analoginput  AS INTEGER)  where di.managerid=?1", nativeQuery = true)
  List<Object[]> getinputanalogalertBymanager_id(Long paramLong);
  
  @Query("from AnalogInputAlert where no=:no")
  List<AnalogInputAlert> findByNo(@Param("no") Long paramLong);
  
  List<AnalogInputAlert> findBymanagerid(Long paramLong);
  
  @Query(value = "select analoginputalert.analoginput,alertmessageshistory.entrytime from analoginputalert inner join alertmessageshistory on cast(analoginputalert.no as varchar)=alertmessageshistory.ruleid where analoginputalert.no=:alert_id and alertmessageshistory.alerttype='ANALOG' order by alertmessageshistory.entrytime desc", nativeQuery = true)
  List<Object[]> alertGenerationTime(@Param("alert_id") long paramLong);
  
  @Query(value = "SELECT TO_CHAR(entrytime,'YYYY-MM-DD HH12:MI:SS'),ruleid FROM alertmessageshistory WHERE deviceid=:deviceId and alerttype='DIGITAL'  order by entrytime asc;", nativeQuery = true)
  List<Object[]> liveGrideAlertTime(@Param("deviceId") long paramLong);
  
  @Query(value = "SELECT no,di.alertlimit,COALESCE(dm.devicename,'0') as devicename,di.analoginput,di.email_id,COALESCE(et.templatename,'0')as emailtemplatename,di.managerid,COALESCE(mt.templatename,'0')as messagetemplatename,di.mobileno,di.notification,COALESCE(st.site_name,'0') as sitename,di.conditionstring,di.conditionvalue,di.timedifference,di.avgtime,di.timing,COALESCE(us.username,'0') as username,COALESCE(up.usergroupname,'0') as usergroupname,pm.prmname FROM analoginputalert di Left join devicemaster dm on dm.deviceid=di.deviceid Left join emailtemplate et on et.eid=di.emailtemplate_id Left join messagetemplate mt on mt.mid=di.messagetemplate_id Left join site st on st.siteid=di.site_id Left join users us on us.id=di.user_id Left join  usergroup up on up.usergroupid=di.usergroup_id join parameter pm on pm.id=CAST (di.analoginput  AS INTEGER)  where di.user_id=?1", nativeQuery = true)
  List<Object[]> getinputanalogalertByUser(Long paramLong);
  
  @Modifying
  @Query(value = "update analoginputalert set mobileno=?1 , conditionvalue=?3 , email_id=?4 , timedifference=?5 where no=?2", nativeQuery = true)
  void updateAnalgoAlertMobileNos(String paramString1, Long paramLong1, Long paramLong2, String paramString2, Long paramLong3);
}

