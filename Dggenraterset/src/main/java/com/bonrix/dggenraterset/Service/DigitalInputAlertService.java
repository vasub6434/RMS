package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.DigitalInputAlert;

public interface DigitalInputAlertService {
	
	List<DigitalInputAlert> findBymanagerid(Long deviceid);
}   
