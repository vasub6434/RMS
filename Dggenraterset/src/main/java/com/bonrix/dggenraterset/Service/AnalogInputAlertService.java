package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.AnalogInputAlert;

public interface AnalogInputAlertService {
	
	List<AnalogInputAlert> findBymanagerid(Long managerid);
	
	List<Object[]> getAlertGenerationTime(long alert_id);

}  
