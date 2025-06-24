package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalInputAlertRepositiry extends JpaRepository<DigitalInputAlert, Long> {
  List<DigitalInputAlert> findBymanagerid(Long paramLong);
  
  @Query("from DigitalInputAlert where no=:no")
  List<DigitalInputAlert> findByNo(@Param("no") Long paramLong);
  
  @Query(value = "SELECT no,di.alertlimit,COALESCE(dm.devicename,'0') as devicename,di.digitalinput,di.email_id,COALESCE(et.templatename,'0')as emailtemplatename,di.managerid,COALESCE(mt.templatename,'0')as messagetemplatename,di.mobileno,di.notification,COALESCE(st.site_name,'0') as sitename,di.status,di.timing,COALESCE(us.username,'0') as username,COALESCE(up.usergroupname,'0') as usergroupname FROM digitalinputalert di Left join devicemaster dm on dm.deviceid=di.deviceid Left join emailtemplate et on et.eid=di.emailtemplate_id Left join messagetemplate mt on mt.mid=di.messagetemplate_id Left join site st on st.siteid=di.site_id Left join users us on us.id=di.user_id Left join  usergroup up on up.usergroupid=di.usergroup_id  where di.managerid=?1", nativeQuery = true)
  List<Object[]> getinputdigitalalertBymanager_id(Long paramLong);
  
  @Query(value = "SELECT no,di.alertlimit,COALESCE(dm.devicename,'0') as devicename,di.digitalinput,di.email_id,COALESCE(et.templatename,'0')as emailtemplatename,di.managerid,COALESCE(mt.templatename,'0')as messagetemplatename,di.mobileno,di.notification,COALESCE(st.site_name,'0') as sitename,di.status,di.timing,COALESCE(us.username,'0') as username,COALESCE(up.usergroupname,'0') as usergroupname FROM digitalinputalert di Left join devicemaster dm on dm.deviceid=di.deviceid Left join emailtemplate et on et.eid=di.emailtemplate_id Left join messagetemplate mt on mt.mid=di.messagetemplate_id Left join site st on st.siteid=di.site_id Left join users us on us.id=di.user_id Left join  usergroup up on up.usergroupid=di.usergroup_id  where di.user_id=?1 and devicename <> '0'", nativeQuery = true)
  List<Object[]> getinputdigitalalertByuser_id(Long paramLong);
  
  @Modifying
  @Query(value = "update digitalinputalert set mobileno=?1 where no=?2", nativeQuery = true)
  void updateMobileNos(String paramString, Long paramLong);
}
