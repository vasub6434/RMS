package com.bonrix.dggenraterset.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.DTO.AlertResponse;
import com.bonrix.dggenraterset.DTO.AlertSummary;
import com.bonrix.dggenraterset.DTO.AlertTimelineResponse;
import com.bonrix.dggenraterset.DTO.AlertTypeDTO;
import com.bonrix.dggenraterset.DTO.AlertTypeDTOLive;
  
public interface DashboardAPIService {

	List<Object[]> geyMyProfileList(Iterable<Long> deviceIds);    

	List<Object[]> getHisrotyAnalogByNo(long no);

	List<Object[]> getHisrotyDigitalByNo(long no);
  
	List<Object[]> GetDeviceParameterRecords(Long deviceId,String prmname);
	
	List<Object[]> GetDeviceParameterRecords(Long deviceId,String prmname, int limit);
	
	AlertResponse getAlerts(Date fromDate, Date toDate, Long managerId,Long profileId);
	
    AlertSummary getAlertSummary(Long managerId, Date fromDate, Date toDate);
	
	AlertTimelineResponse getAlertTimeline(Long managerId, Date fromDate, Date toDate,Long profileId, String interval);
	
	//AlertTypeDTO getAlertData(Date fromDate, Date toDate, Long managerId);
	
	BigDecimal getAnalogDiffrence(@Param("deviceId") Long deviceId,@Param("paramId") String paramId);
	
	AlertTypeDTO getAlertData(Date fromDate, Date toDate, Long managerId,Long profileId);
	
	AlertTypeDTOLive getAlertDataLive(Long managerId, Long profileId);
	
}
