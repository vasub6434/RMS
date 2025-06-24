package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AnalogInputAlert;
import com.bonrix.dggenraterset.Repository.AnalogInputAlertRepository;

@Service("AnalogInputAlertService")
public class AnalogInputAlertImpl implements AnalogInputAlertService {

	@Autowired
	AnalogInputAlertRepository alertRepo;
  
	@Override
	public List<AnalogInputAlert> findBymanagerid(Long managerid) {
		return alertRepo.findBymanagerid(managerid);
	}

	@Override
	public List<Object[]> getAlertGenerationTime(long alert_id) {
		return alertRepo.alertGenerationTime(alert_id);
	}

}
