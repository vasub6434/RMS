package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Repository.BaseRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlertMessagesRepository extends BaseRepository<AlertMessages, Long> {
  @Query("from AlertMessages where managerid=:manager_id")
  List<AlertMessages> findByManagerid(@Param("manager_id") Long paramLong);
  
  @Query(value = "SELECT am.alertid,am.alerttype,am.ruleid,am.message,am.response,TO_CHAR(am.entrytime, 'YYYY-MM-DD HH24:MI:SS') as entrytime, COALESCE(dm.devicename,'Not Known') as devicename,COALESCE(si.site_name,'Not Known') as sitename , COALESCE(ug.usergroupname,'Not Known') as groupname,COALESCE(us.username,'Not Known') as username,am.sentmobile as mobileno from alertmessageshistory am left outer join  devicemaster dm on am.deviceid=dm.deviceid left outer join  site si on am.siteid=si.siteid left outer join  usergroup ug on am.usergroupid=ug.usergroupid left outer join  users us on am.userid=us.id where am.managerid=?1 order by am.entrytime desc", nativeQuery = true)
  List<Object[]> getalertmessagebymid(Long paramLong);
  
  @Query(value = "SELECT am.alertid,am.alerttype,am.message,TO_CHAR(am.entrytime, 'YYYY-MM-DD HH12:MI:SS') as entrytime, COALESCE(dm.devicename,'Not Known') as devicename,COALESCE(si.site_name,'Not Known') as sitename ,am.sentmobile as mobileno from alertmessageshistory am left outer join  devicemaster dm on am.deviceid=dm.deviceid left outer join  site si on am.siteid=si.siteid  inner join  users us on am.userid=us.id where am.userid=?1 and am.alerttype=?2 and am.deviceid IN ?3 and am.entrytime between TO_DATE(?4, 'YYYY-MM-DD') and TO_DATE(?5, 'YYYY-MM-DD')+ interval '1 day'  order by am.entrytime desc", nativeQuery = true)
  List<Object[]> getalertmessagebyUserId(long paramLong, String paramString1, Iterable<Long> paramIterable, String paramString2, String paramString3);
}

