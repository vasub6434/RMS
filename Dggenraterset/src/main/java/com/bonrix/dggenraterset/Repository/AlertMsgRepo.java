package com.bonrix.dggenraterset.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.AlertMessages;

public interface AlertMsgRepo  extends BaseRepository<AlertMessages, Long>
{

	@Query(value = "SELECT amh.alertid, amh.deviceid, amh.entrytime, amh.historyid, amh.managerid, "
			+ "amh.message, amh.responce, amh.siteid, amh.usergroupid, amh.userid, amh.alerttype, "
			+ "amh.response, amh.ruleid, amh.sentmobile "
			+ "FROM alertmessageshistory amh "
			+ "JOIN devicemaster dm ON amh.deviceid = dm.deviceid "
			+ "WHERE amh.managerid = :managerId "
			+ "AND entrytime BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD') + INTERVAL '1 day'"
			+ "AND dm.prid_fk = :profileId "
			+ "ORDER BY amh.entrytime DESC", nativeQuery = true)
List<AlertMessages> findAlertsByManageridAndEntrytimeBetweenAndProfileId(
		@Param("managerId") Long managerId,
		@Param("fromDate") Date fromDate,
		@Param("toDate") Date toDate,
		@Param("profileId") Long profileId);

}