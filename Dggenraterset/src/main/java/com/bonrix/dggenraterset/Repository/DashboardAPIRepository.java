package com.bonrix.dggenraterset.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bonrix.dggenraterset.Model.DashBoardLayout;

@Repository
public interface DashboardAPIRepository extends BaseRepository<DashBoardLayout, Long> {

	@Query(value ="SELECT DISTINCT dm.prid_fk,dp.profilename FROM public.devicemaster dm JOIN public.deviceprofile dp ON dm.prid_fk = dp.prid WHERE dm.deviceid in ?1", nativeQuery = true)
	List<Object[]> geyMyProfileList(Iterable<Long> deviceIds);
	
	@Query(value =" SELECT CAST(json_data.key as text),json_data.value AS digitalvalue,dm.devicename,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') FROM history  JOIN devicemaster dm on dm.deviceid=history.device_id, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  history.no=?1", nativeQuery = true)
	List<Object[]> getHisrotyAnalogByNo(long no);
	 
	@Query(value =" SELECT CAST(json_data.key as text),json_data.value AS digitalvalue,dm.devicename,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') FROM history  JOIN devicemaster dm on dm.deviceid=history.device_id, jsonb_each_text(devicedata->'Digital') AS json_data WHERE  history.no=?1", nativeQuery = true)
	List<Object[]> getHisrotyDigitalByNo(long no);
	  
	/*@Query(value ="SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') ,json_data.value AS digitalvalue FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE json_data.key =?2 AND device_id =?1 ORDER BY device_date DESC FETCH FIRST ?3 ROWS ONLY", nativeQuery = true)
	List<Object[]> GetDeviceParameterRecords(Long deviceId, String prmname,int limit);*/
	
/*	@Query(value ="SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS'),json_data.value AS digitalvalue FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:prmname and  " + 
			" cast(device_date as date) = CURRENT_DATE and device_id=:deviceId  order by device_date desc limit 100", nativeQuery = true)*/
	
	@Query(value ="SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS'),json_data.value AS digitalvalue FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:prmname and  " + 
			" cast(device_date as date) = CURRENT_DATE and device_id=:deviceId  order by device_date desc", nativeQuery = true)
	List<Object[]> GetDeviceParameterRecords(@Param("deviceId") Long deviceId ,@Param("prmname") String   prmname);

	// new code
//	@Query(value ="SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS'),json_data.value AS digitalvalue FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:prmname and  " + 
//			" cast(device_date as date) =  '2025-06-11' and device_id=:deviceId  order by device_date desc Limit :limit", nativeQuery = true)
//	List<Object[]> GetDeviceParameterRecords(@Param("deviceId") Long deviceId ,@Param("prmname") String   prmname,@Param("limit") int limit);
	@Query(value = "SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS'), json_data.value AS digitalvalue "
			+ "FROM history, jsonb_each_text(devicedata->'Analog') AS json_data " + "WHERE json_data.key=:prmname "
			+ "AND cast(device_date as date) = '2025-06-11' " + "AND device_id=:deviceId "
			+ "ORDER BY device_date DESC " + "LIMIT CASE WHEN :limit > 0 THEN :limit END", nativeQuery = true)
	List<Object[]> GetDeviceParameterRecords(@Param("deviceId") Long deviceId, @Param("prmname") String prmname,
			@Param("limit") int limit);

	@Query(value = "SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS'), json_data.value AS digitalvalue "
			+ "FROM history, jsonb_each_text(devicedata->'Analog') AS json_data " + "WHERE json_data.key=:prmname "
			+ "AND cast(device_date as date) = '2025-06-11' " + "AND device_id=:deviceId "
			+ "ORDER BY device_date DESC", nativeQuery = true)
	List<Object[]> GetAllDeviceParameterRecords(@Param("deviceId") Long deviceId, @Param("prmname") String prmname);
	
	@Query(value = "WITH time_buckets AS ( "
            + "SELECT "
            + " date_trunc('hour', amh.entrytime) AS hour_bucket, "
            + " COUNT(CASE WHEN amh.message ILIKE '%Status ACTIVE%' THEN 1 END) AS new_alerts, "
            + " COUNT(CASE WHEN amh.message ILIKE '%Status CLEAR%' OR amh.message ILIKE '%Status INACTIVE%' THEN 1 END) AS resolved_alerts "
            + " FROM alertmessageshistory amh "
            + " JOIN devicemaster dm ON amh.deviceid = dm.deviceid "
            + " WHERE amh.managerid = :managerId "
            + " AND amh.entrytime BETWEEN :fromDate AND :toDate "
            + " AND ( "
            + " amh.message ILIKE '%Status ACTIVE%' OR "
            + " amh.message ILIKE '%Status CLEAR%' OR "
            + " amh.message ILIKE '%Status INACTIVE%' "
            + " ) "
            + " AND dm.prid_fk = :profileId "  
            + " GROUP BY hour_bucket "
            + " ORDER BY hour_bucket "
            + ") "
            + "SELECT "
            + " hour_bucket AS timestamp, "
            + " new_alerts, "
            + " resolved_alerts "
            + "FROM time_buckets "
            + "ORDER BY hour_bucket;", nativeQuery = true)
	List<Object[]> getAlertTimeline(@Param("managerId") Long managerId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("profileId") Long profileId); 


	@Query(value = "SELECT " + "COUNT(*) as total_alerts, "
			+ "COUNT(CASE WHEN COALESCE(message, '') ILIKE '%Status ACTIVE%' THEN 1 END) as active_count,"
			+ "COUNT(CASE WHEN COALESCE(message, '') ILIKE '%Status CLEAR%' THEN 1 END) as clear_count, "
			+ "COUNT(CASE WHEN COALESCE(message, '') ILIKE '%ACMAINS_FAIL%' OR COALESCE(message, '') ILIKE '%DG Fault%' OR COALESCE(message, '') ILIKE '%Battery Low%' THEN 1 END) as critical_count "
			+ "FROM alertmessageshistory " + " WHERE managerid = :managerId " + "AND entrytime  between  TO_DATE(:fromDate,'YYYY-MM-DD' ) AND TO_DATE(:toDate,'YYYY-MM-DD' )  + INTERVAL '1 day' ", nativeQuery = true)
	List<Object[]> findAlertSummaryByManageridAndEntrytimeBetween1(@Param("managerId") Long managerId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

		
	
	@Query(value = "WITH alert_extraction AS ( " +
	        "    SELECT message, entrytime, deviceid " +
	        "    FROM alertmessageshistory " +
	        "    WHERE managerid = :managerId " +
	        "   AND entrytime BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD') + INTERVAL '1 day'  " +	        
	        "    AND message LIKE '%Status ACTIVE%' " +
	        "), alert_types AS ( " +
	        "    SELECT CASE " +
	        "        WHEN message LIKE '%Battery Low%' THEN 'Battery Low' " +
	        "        WHEN message LIKE '%DG Fault%' THEN 'DG Fault' " +
	        "        WHEN message LIKE '%ACMAINS_FAIL%' THEN 'ACMAINS_FAIL' " +
	        "        WHEN message LIKE '%Temp High%' THEN 'Temperature High' " +
	        "        WHEN message LIKE '%Door Open%' OR message LIKE '%Door%' THEN 'Door' " +
	        "        WHEN message LIKE '%Smoke%' THEN 'Smoke Detected' " +
	        "        WHEN message LIKE '%Fire%' THEN 'Fire' " +
	        "        WHEN message LIKE '%PP Input Fail%' THEN 'PP Input Fail' " +
	        "        WHEN message LIKE '%DG Running%' THEN 'DG Running' " +
	        "        WHEN message LIKE '%Water Leak%' THEN 'Water Leak' " +
	        "        WHEN message LIKE '%Humidity%' THEN 'Humidity Alert' " +
	        "        WHEN message LIKE '%Alarm Name%' THEN SUBSTRING(message FROM 'Alarm Name ([^:]+):') " +
	        "        ELSE 'Other' " +
	        "    END AS alert_type, " +
	        "    entrytime, deviceid " +
	        "    FROM alert_extraction " +
	        ") " +
	        "SELECT alert_type, COUNT(*) as count " +
	        "FROM alert_types " +
	        "GROUP BY alert_type " +
	        "ORDER BY count DESC", nativeQuery = true)
	List<Object[]> getAlertCountsByType(@Param("managerId") Long managerId,
	                                   @Param("fromDate") Date fromDate,
	                                   @Param("toDate") Date toDate);
	
	@Query(value = "SELECT " + 
			"  (" + 
			"    SELECT CAST(devicedata->'Analog'->> :paramId AS numeric)" + 
			"    FROM public.lasttrack lt" + 
			"    WHERE jsonb_exists(devicedata->'Analog', :paramId)" + 
			"      AND device_id = :deviceId" + 
			"    ORDER BY device_date DESC" + 
			"    LIMIT 1" + 
			"  )" + 
			"  -" + 
			"  (" + 
			"    SELECT CAST(devicedata->'Analog'->> :paramId AS numeric)" + 
			"    FROM public.history" + 
			"    WHERE jsonb_exists(devicedata->'Analog', :paramId) " + 
			"      AND device_id = :deviceId " + 
			"    ORDER BY device_date DESC " + 
			"    LIMIT 1 " + 
			"  ) AS difference; " , nativeQuery = true)
	BigDecimal getAnalogDiffrence(@Param("deviceId") Long deviceId,@Param("paramId") String paramId);

	
}
