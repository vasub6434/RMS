package com.bonrix.dggenraterset.Repository;

import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Repository.BaseRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends BaseRepository<History, Long> {
	List<History> findBydeviceId(Long paramLong);

	@Query(value = "SELECT no,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,system_date,json_data.value AS digitalvalue FROM history, jsonb_each_text(devicedata->'Digital') AS json_data WHERE  json_data.key=:paramId and device_date  between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:endDate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:device_id  order by device_date", nativeQuery = true)
	List<Object[]> getdata(@Param("startDate") String paramString1, @Param("endDate") String paramString2,
			@Param("paramId") String paramString3, @Param("device_id") long paramLong);

	@Query(value = "SELECT TO_CHAR(device_date, 'yyyy-mm-dd'),digitaldata ->'Rs232'->0->>:pid FROM history where history.device_date  between TO_DATE(:startdate, 'YYYY-MM-DD')and TO_DATE(:enddate, 'YYYY-MM-DD')  and history.device_id=:id order by device_date desc", nativeQuery = true)
	List energymeterrpt2(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("pid") String paramString3);

	@Query(value = "SELECT json_data.value AS digitalvalue,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:prmname and device_date  between TO_DATE(:startdate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:id  order by device_date", nativeQuery = true)
	List deltameterrpt(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "SELECT TO_CHAR(device_date,'DD-MM-YYYY HH24:MI:SS'),analogs ->>:prmname digitalvalue from history,jsonb_array_elements(digitaldata->'Analog') analogs where device_date  between TO_DATE(:startdate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:id and analogs ->>:prmname IS NOT NULL order by device_date desc", nativeQuery = true)
	List deltameterrpt2(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "SELECT TO_CHAR(device_date,'DD-MM-YYYY HH24:MI:SS'),analogs ->>:prmname digitalvalue from history,jsonb_array_elements(digitaldata->'Analog') analogs where device_date  between TO_DATE(:startdate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:id and analogs ->>:prmname IS NOT NULL order by device_date asc", nativeQuery = true)
	List deltameterrpt3(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "select analogs ->>'Analogunit' analogunit  from public.deviceprofile,jsonb_array_elements(parameters->'Analog') analogs where analogs->>'Analoginput'=:analoginput AND prid=:prid", nativeQuery = true)
	List<Object[]> getprofileanalogunit(@Param("prid") Long paramLong, @Param("analoginput") String paramString);

	@Query(value = "SELECT TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,devicedata->>'Digital' AS digitalvalue FROM history WHERE   device_date  between TO_DATE(:startdate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId  order by device_date", nativeQuery = true)
	List<Object[]> getDigitalHistory(@Param("deviceId") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2);

	@Query(value = "select hs.no,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,hs.gpsdata->>'latitude' as latitude,hs.gpsdata->>'longitude' as longitude,hs.gpsdata->>'speed' as speed,hs.gpsdata->>'angle' as angle,hs.gpsdata->>'odometer'   as odometer,hs.gpsdata->>'dig_input_2' as digitalinput from history hs where hs.device_id=:deviceid  and hs.device_date  between TO_DATE(:startdate, 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(:enddate,'YYYY-MM-DD HH24:MI:SS')+ interval '1 day' and (hs.gpsdata->>'latitude' IS NOT NULL or hs.gpsdata->>'longitude' IS NOT NULL ) order by device_date asc limit :max", nativeQuery = true)
	List<Object[]> getdeviceHistoryLocation(@Param("deviceid") Long paramLong1, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("max") Long paramLong2);

	@Query(value = "select hs.no,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,hs.gpsdata->>'latitude' as latitude,hs.gpsdata->>'longitude' as longitude,hs.gpsdata->>'speed' as speed,hs.gpsdata->>'angle' as angle ,dm.devicename as devicename from history hs  join devicemaster dm on dm.deviceid=hs.device_id where hs.device_id=:deviceid  and hs.device_date  between TO_DATE(:startdate, 'YYYY-MM-DD HH24:MI:SS') and TO_DATE(:enddate,'YYYY-MM-DD HH24:MI:SS')+ interval '1 day' and (hs.gpsdata->>'latitude' IS NOT NULL or hs.gpsdata->>'longitude' IS NOT NULL) order by device_date asc  limit :max", nativeQuery = true)
	List<Object[]> getadminHistoryLocation(@Param("deviceid") Long paramLong1, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("max") Long paramLong2);

	@Query(value = "SELECT device_date,gpsdata ->>'fuel'  FROM history WHERE device_id=:deviceId and gpsdata ->>'fuel' is not null AND device_id=:deviceId ANd device_date between TO_DATE(:date1, 'YYYY-MM-DD') and TO_DATE(:date2 , 'YYYY-MM-DD') + interval '1 day' order by device_date asc", nativeQuery = true)
	List getAnalogRawData(@Param("deviceId") long paramLong, @Param("date1") String paramString1,
			@Param("date2") String paramString2);

	@Query(value = "SELECT parameter.prmname from parameter where parameter.id=:prid", nativeQuery = true)
	List<Object[]> getperametername(@Param("prid") Long paramLong);

	@Query(value = "SELECT no,analogs ->>:prmname digitalvalue,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS'),system_date from history,jsonb_array_elements(digitaldata->'Analog') analogs where device_date  between :startdate and :enddate and device_id=:id and analogs ->>:prmname IS NOT NULL order by device_date asc", nativeQuery = true)
	List getAnalogData(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "SELECT no,analogs ->>:prmname digitalvalue,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS') FROM   history,jsonb_array_elements(digitaldata->'Analog') analogs WHERE  device_id=:id and device_date  between to_timestamp(:startdate, 'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:enddate, 'YYYY-MM-DD HH24:MI:SS') and analogs ->>:prmname IS NOT NULL", nativeQuery = true)
	List getCandleData(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "SELECT device_date,gpsdata ->>'fuel'  FROM history WHERE device_id=:deviceId and gpsdata ->>'fuel' is not null AND device_id=:deviceId ANd device_date between to_timestamp(:date1, 'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:date2, 'YYYY-MM-DD HH24:MI:SS') order by device_date asc", nativeQuery = true)
	List getFuelCandle(@Param("deviceId") long paramLong, @Param("date1") String paramString1,
			@Param("date2") String paramString2);

	@Query(value = "SELECT no,analogs ->>:prmname digitalvalue,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS'),system_date from history,jsonb_array_elements(digitaldata->'Analog') analogs where device_date  between TO_DATE(:startdate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:id and analogs ->>:prmname IS NOT NULL order by device_date ", nativeQuery = true)
	List getReport(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "select no,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,system_date,digitals ->>:paramId digitalvalue from public.history,jsonb_array_elements(digitaldata->'Digital') digitals where digitals ->>:paramId IS NOT NULL and device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:endDate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId  order by device_date ", nativeQuery = true)
	List<Object[]> getSingleDigitalParmData(@Param("startDate") String paramString1,
			@Param("endDate") String paramString2, @Param("paramId") String paramString3,
			@Param("deviceId") long paramLong);

	@Query(value = "SELECT no,analogs ->>:prmname digitalvalue,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS'),system_date from history,jsonb_array_elements(digitaldata->'Analog') analogs where (device_date   BETWEEN TO_TIMESTAMP(:startdate,'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(:enddate,'YYYY-MM-DD HH24:MI:SS')) and device_id=:id and analogs ->>:prmname IS NOT NULL order by device_date asc", nativeQuery = true)
	List getLiveGraphData(@Param("id") Long paramLong, @Param("startdate") String paramString1,
			@Param("enddate") String paramString2, @Param("prmname") String paramString3);

	@Query(value = "SELECT device_date,cast(digitaldata as varchar) FROM history WHERE device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:startDate, 'YYYY-MM-DD')+ interval '1 day' and device_id =:deviceId order by device_date asc", nativeQuery = true)
	List getDeltaAnalogData(@Param("startDate") String paramString, @Param("deviceId") long paramLong);

	@Query(value = "SELECT no,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,system_date,analog ->>:prmname analogvalue from public.history,jsonb_array_elements(digitaldata->'Analog') analog where analog ->>:prmname IS NOT NULL and device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId  order by device_date", nativeQuery = true)
	List getDeltaSingleAnalogData(@Param("startDate") String paramString1, @Param("enddate") String paramString2,
			@Param("prmname") String paramString3, @Param("deviceId") long paramLong);

	@Query(value = "select history.no,device_date,system_date,device_id from history where history.device_id=:deviceId and (device_date between TO_DATE(:startdate, 'YYYY-MM-DD') and TO_DATE(:enddate, 'YYYY-MM-DD')+ interval '1 day') ", nativeQuery = true)
	List getMonthlyDeltaDeviceCount(@Param("startdate") String paramString1, @Param("enddate") String paramString2,
			@Param("deviceId") long paramLong);

	@Query(value = "SELECT device_date,cast(digitaldata as varchar) FROM history where device_id=:deviceId and device_date>=TO_TIMESTAMP(:inData,'YYYY-MM-DD HH24:MI:SS') order by device_date asc limit 1", nativeQuery = true)
	List<Object[]> getDeltaDataReport(@Param("inData") String paramString, @Param("deviceId") long paramLong);

	@Query(value = " select count(*) from history where history.device_id=:deviceId and device_date between TO_DATE(:devceDte, 'YYYY-MM-DD') and TO_DATE(:devceDte, 'YYYY-MM-DD')+ interval '1 day'", nativeQuery = true)
	long getDeltaDataReportCount(@Param("devceDte") String paramString, @Param("deviceId") long paramLong);

	@Query(value = "SELECT no,device_id,analogs->>:paramId,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS'),system_date from history,jsonb_array_elements(digitaldata->'Analog') analogs where device_date  between TO_DATE(:devceDte, 'YYYY-MM-DD') and TO_DATE(:devceDte,'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId and device_date>=TO_TIMESTAMP(:devceDte,'YYYY-MM-DD HH24:MI:SS') AND analogs->>:paramId IS NOT NULL order by device_date asc limit 1", nativeQuery = true)
	List<Object[]> getDeltaDataReportSingleParam(@Param("devceDte") String paramString1,
			@Param("deviceId") long paramLong, @Param("paramId") String paramString2);

	@Query(value = "SELECT analogs ->>:paramId,TO_CHAR(device_date,'YYYY-MM-DD HH24:MI:SS') from history,jsonb_array_elements(digitaldata->'Digital') analogs where device_date  between TO_DATE(:devceDte, 'YYYY-MM-DD') and TO_DATE(:devceDte, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId and analogs ->>:paramId=:status order by device_date", nativeQuery = true)
	List<Object[]> getDeltaDataReportActiveClearData(@Param("devceDte") String paramString1,
			@Param("deviceId") long paramLong, @Param("paramId") String paramString2,
			@Param("status") String paramString3);

	@Query(value = "SELECT no,device_date,cast(digitaldata as varchar),device_id FROM history WHERE device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:startDate, 'YYYY-MM-DD')+ interval '1 day' order by device_date asc limit :limit offset :offset", nativeQuery = true)
	List<Object[]> getElasticsearch(@Param("startDate") String paramString, @Param("limit") int paramInt1,
			@Param("offset") int paramInt2);

	@Query(value = "SELECT count(*) as ct,devicemaster.devicename,devicemaster.imei,devicemaster.altdevicename FROM history inner join devicemaster on history.device_id=devicemaster.deviceid where  device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:startDate, 'YYYY-MM-DD')+ interval '1 day' group by  device_id,devicemaster.devicename,devicemaster.imei,devicemaster.altdevicename order by ct desc", nativeQuery = true)
	List GetDeltaDeviceCount(@Param("startDate") String paramString);

	@Modifying
	@Transactional
	@Query(value = "FROM history WHERE device_date < NOW() - INTERVAL ':day DAY'", nativeQuery = true)
	void DeleteHistoryData(@Param("day") String paramString);

	@Query(value = "select no,TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') as device_date,system_date,digitals ->>:paramId digitalvalue from public.history,jsonb_array_elements(digitaldata->'Digital') digitals where digitals ->>:paramId IS NOT NULL and device_date between :startDate::timestamp AND :endDate::timestamp and device_id=:device_id  order by device_date ", nativeQuery = true)
	List<Object[]> DigitalHistoryData(@Param("startDate") String paramString1, @Param("endDate") String paramString2,
			@Param("paramId") String paramString3, @Param("device_id") long paramLong);

	@Query(value = "SELECT json_data.value AS value,device_date FROM history, jsonb_each_text(devicedata->'Analog') AS json_data  WHERE  json_data.key='6387982' AND  device_id=:deviceId and device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:startDate, 'YYYY-MM-DD')+ interval '1 day' order by device_date asc", nativeQuery = true)
	List<Object[]> getFuelData(@Param("deviceId") long paramLong, @Param("startDate") String paramString);

	@Modifying
	@Query(value = "DELETE FROM history WHERE TO_CHAR(system_date ,'YYYY-MM-DD')=:deleteDate", nativeQuery = true)
	void DeleteData(@Param("deleteDate") String paramString);

	@Query(value = "SELECT count(*) as ct,devicemaster.devicename,devicemaster.imei,devicemaster.altdevicename FROM history inner join devicemaster on history.device_id=devicemaster.deviceid where  device_date between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:startDate, 'YYYY-MM-DD')+ interval '1 day' and  devicemaster.deviceid=:deviceId group by  device_id,devicemaster.devicename,devicemaster.imei,devicemaster.altdevicename", nativeQuery = true)
	List GetRMSDeviceCount(@Param("startDate") String paramString, @Param("deviceId") long paramLong);

	@Query(value = "SELECT device_date,cast(digitaldata as varchar) FROM history where device_id=:deviceId and device_date>=TO_TIMESTAMP(:inData,'YYYY-MM-DD HH24:MI:SS') order by device_date asc", nativeQuery = true)
	List<Object[]> getDeltaAllData(@Param("inData") String paramString, @Param("deviceId") long paramLong);

	@Query(value = "SELECT DISTINCT DATE_TRUNC('day', device_date) Created_date,     MIN(device_date) OVER (PARTITION BY DATE_TRUNC('day', device_date)) fst_record,     FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('day', device_date)                               ORDER BY device_date ASC) fst_Value ,    MAX(device_date) OVER (PARTITION BY DATE_TRUNC('day', device_date)) last_record,     FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('day', device_date)      ORDER BY device_date DESC) last_Value,                          FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('day', device_date)                               ORDER BY device_date DESC) -                                  FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('day', device_date)                               ORDER BY device_date ASC) diff_in_Value FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:paramId and device_date  between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:endDate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId  order by Created_date ", nativeQuery = true)
	List<Object[]> gerMonthlyBarChartData(@Param("deviceId") long paramLong, @Param("paramId") String paramString1,
			@Param("startDate") String paramString2, @Param("endDate") String paramString3);

	@Query(value = "SELECT DISTINCT DATE_TRUNC('month', device_date) Created_date,     MIN(device_date) OVER (PARTITION BY DATE_TRUNC('month', device_date)) fst_record,     FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('month', device_date)                               ORDER BY device_date ASC) fst_Value ,    MAX(device_date) OVER (PARTITION BY DATE_TRUNC('month', device_date)) last_record,     FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('month', device_date)      ORDER BY device_date DESC) last_Value,                          FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('month', device_date)                               ORDER BY device_date DESC) -                                  FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('month', device_date)                               ORDER BY device_date ASC) diff_in_Value FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:paramId and device_date  between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:endDate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId  order by Created_date ", nativeQuery = true)
	List<Object[]> getYearlyBarChartData(@Param("deviceId") long paramLong, @Param("paramId") String paramString1,
			@Param("startDate") String paramString2, @Param("endDate") String paramString3);

	@Query(value = "SELECT DISTINCT DATE_TRUNC('hour', device_date) Created_date,     MIN(device_date) OVER (PARTITION BY DATE_TRUNC('hour', device_date)) fst_record,\r\n    FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('hour', device_date)                               ORDER BY device_date ASC) fst_Value ,     MAX(device_date) OVER (PARTITION BY DATE_TRUNC('hour', device_date)) last_record,\r\n    FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('hour', device_date)      ORDER BY device_date DESC) last_Value,                                                 FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('hour', device_date)                               ORDER BY device_date DESC) -                                        FIRST_VALUE(cast(json_data.value as double precision))  OVER (PARTITION BY DATE_TRUNC('hour', device_date)                               ORDER BY device_date ASC) diff_in_Value FROM history, jsonb_each_text(devicedata->'Analog') AS json_data WHERE  json_data.key=:paramId and device_date  between TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:endDate, 'YYYY-MM-DD')+ interval '1 day' and device_id=:deviceId  order by Created_date ", nativeQuery = true)
	List<Object[]> getDailyBarChartData(@Param("deviceId") long paramLong, @Param("paramId") String paramString1,
			@Param("startDate") String paramString2, @Param("endDate") String paramString3);

	@Query(value = "SELECT h.device_id AS deviceId, " + "h.gpsdata->>'latitude' AS latitude, "
			+ "h.gpsdata->>'longitude' AS longitude, " + "h.gpsdata->>'speed' AS speed, "
			+ "h.gpsdata->>'angle' AS angle, " + "TO_CHAR(h.device_date, 'YYYY-MM-DD HH24:MI:SS') AS deviceDate "
			+ "FROM history h " + "WHERE h.device_id = :deviceId "
			+ "AND h.device_date BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp) "
			+ "ORDER BY h.device_date ASC", nativeQuery = true)
	List<Object[]> getAllSpeedData(@Param("deviceId") int deviceId, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

	@Query(value = "SELECT h.device_id AS deviceId, " + "h.gpsdata->>'latitude' AS latitude, "
			+ "h.gpsdata->>'longitude' AS longitude, " + "h.gpsdata->>'speed' AS speed, "
			+ "h.gpsdata->>'angle' AS angle, " + "TO_CHAR(h.device_date, 'YYYY-MM-DD HH24:MI:SS') AS deviceDate "
			+ "FROM history h " + "WHERE h.device_id = :deviceId "
			+ "AND h.device_date BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp) "
			+ "AND CAST(h.gpsdata->>'speed' AS FLOAT) > 0 " + "ORDER BY h.device_date ASC", nativeQuery = true)
	List<Object[]> getNonZeroSpeedData(@Param("deviceId") int deviceId, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

	@Query(value = "SELECT l.device_id AS deviceId, " + "l.gpsdata->>'latitude' AS latitude, "
			+ "l.gpsdata->>'longitude' AS longitude, " + "l.gpsdata->>'speed' AS speed, "
			+ "l.gpsdata->>'angle' AS angle, " + "TO_CHAR(l.device_date, 'YYYY-MM-DD HH24:MI:SS') AS deviceDate "
			+ "FROM history l " + "WHERE l.device_id = :deviceId "
			+ "AND l.device_date BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp) "
			+ "ORDER BY l.device_date ASC", nativeQuery = true)
	List<Object[]> getHistoryData(@Param("deviceId") int deviceId, @Param("startDate") String startDate,
			@Param("endDate") String endDate);

	@Query(value = "SELECT device_id, " + "TO_CHAR(device_date, 'YYYY-MM-DD HH24:MI:SS') AS device_date, "
			+ "gpsdata->>'latitude' AS latitude, " + "gpsdata->>'longitude' AS longitude, "
			+ "gpsdata->>'angle' AS angle, " + "gpsdata->>'speed' AS speed " + "FROM lasttrack "
			+ "WHERE device_id = :deviceId " + "ORDER BY device_date DESC", nativeQuery = true)
	List<Object[]> findAllByDeviceIdAndDateRange(@Param("deviceId") Long deviceId);
//
//	@Query(value = "SELECT * FROM history WHERE device_id = :deviceId AND device_date BETWEEN :start AND :end", nativeQuery = true)
//	List<History> findByDeviceIdAndDateRange(@Param("deviceId") Long deviceId, @Param("start") Date start,
//			@Param("end") Date end);

	@Query(value = "SELECT * FROM history WHERE device_id = :deviceId AND device_date BETWEEN :start AND :end ORDER BY device_date ASC", nativeQuery = true)
    List<History> findByDeviceIdAndDateRange(@Param("deviceId") Long deviceId,
                                             @Param("start") Date start,
                                             @Param("end") Date end);
	
	@Query(value = "SELECT * FROM history WHERE no = ("
			+ "SELECT no FROM history WHERE device_id = :deviceId AND device_date < :date ORDER BY device_date DESC LIMIT 1) "
			+ "UNION ALL " + "SELECT * FROM history WHERE device_id = :deviceId AND device_date = :date " + "UNION ALL "
			+ "SELECT * FROM history WHERE no = ("
			+ "SELECT no FROM history WHERE device_id = :deviceId AND device_date > :date ORDER BY device_date ASC LIMIT 1)", nativeQuery = true)
	List<History> findSurroundingLatLong(@Param("deviceId") Long deviceId, @Param("date") Date date);

	@Query(value = "SELECT CAST(devicedata AS text) FROM history WHERE device_id = :deviceId ORDER BY device_date DESC LIMIT 1", nativeQuery = true)
	String findLatestDeviceDataByDeviceId(@Param("deviceId") Long deviceId);
	
	@Query(value = "SELECT CAST(devicedata AS text), device_date FROM history " + "WHERE device_id = :deviceId "
			+ "AND device_date BETWEEN :sdate AND :edate " + "ORDER BY device_date "
			+ "LIMIT :limit", nativeQuery = true)
	List<Object[]> findDeviceDataByDeviceIdAndDateRangeWithLimit(@Param("deviceId") Long deviceId,
			@Param("sdate") Timestamp sdate, @Param("edate") Timestamp edate, @Param("limit") int limit);

	@Query(value = "SELECT CAST(devicedata AS text), device_date FROM history " + "WHERE device_id = :deviceId "
			+ "AND device_date BETWEEN :sdate AND :edate " + "ORDER BY device_date", nativeQuery = true)
	List<Object[]> findDeviceDataByDeviceIdAndDateRange(@Param("deviceId") Long deviceId,
			@Param("sdate") Timestamp sdate, @Param("edate") Timestamp edate);
	
}
