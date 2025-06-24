package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.IHReport;

public interface IHReportRepository extends BaseRepository<IHReport, Long> {  
	
	@Query(value = "select startdate,enddate,status,parameterid,open,close,deviceid FROM ihreport " + 
			"WHERE  entrydate BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') and TO_DATE(:endDate, 'YYYY-MM-DD') " + 
			" AND parameterid=:paramId AND deviceid=:device_id", nativeQuery = true)
	public abstract List<Object[]> getIRdata(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("paramId") long paramId, @Param("device_id") long device_id);  
	
	@Query(value = "SELECT TO_CHAR( device_date,'YYYY-MM-DD HH24:MI:SS' ) AS device_date," + 
			"devicedata -> 'Digital' ->> '284945' as Mains_Fail, " + 
			"devicedata -> 'Digital' ->> '291934' as Door, " + 
			"devicedata -> 'Digital' ->> '6348815' as DG_Fault, " + 
			"devicedata -> 'Digital' ->> '6348824' as PP_Input_Fail, " + 
			"devicedata -> 'Digital' ->> '6348798' as Fire, " + 
			"devicedata -> 'Digital' ->> '6348854' as DG_Running," + 
			"devicedata -> 'Digital' ->> '6348821' as Battery_Low " + 
			"FROM history  WHERE device_date between TO_DATE( :startDate,'YYYY-MM-DD' ) " + 
			"AND TO_DATE(:endDate,'YYYY-MM-DD' ) + interval '1 day' AND device_id = :device_id ORDER BY device_date", nativeQuery = true)
	public abstract List<Object[]> getInputHistoryReportdata(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("device_id") long device_id); 

}
