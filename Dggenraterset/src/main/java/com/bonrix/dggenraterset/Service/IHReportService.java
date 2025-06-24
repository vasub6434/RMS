package com.bonrix.dggenraterset.Service;

import java.util.List;


import com.bonrix.dggenraterset.Model.IHReport;

public interface IHReportService {
	
	void saveReport(IHReport report);
	
	List<Object[]> getIRReport(String startDate,String endDate,long paramId,long device_id);  
	
	List<Object[]> getInputHistoryReportdata(String startDate,String endDate,long device_id); 
	
  
}
