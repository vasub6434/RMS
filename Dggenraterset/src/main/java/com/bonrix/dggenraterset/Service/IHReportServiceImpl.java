package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.IHReport;
import com.bonrix.dggenraterset.Repository.IHReportRepository;

@Service("IHReportService")
public class IHReportServiceImpl implements IHReportService {

	@Autowired
	IHReportRepository repo;
  
	@Override
	public void saveReport(IHReport report) {
		repo.save(report);
	}

	@Override
	public List<Object[]> getIRReport(String startDate, String endDate, long paramId, long device_id) {
		return repo.getIRdata(startDate, endDate, paramId, device_id);  
	}

	@Override
	public List<Object[]> getInputHistoryReportdata(String startDate, String endDate, long device_id) {
		return repo.getInputHistoryReportdata(startDate,endDate,device_id);
	}
}
