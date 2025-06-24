package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Repository.DigitalInputAlertRepositiry;

@Service("DigitalInputAlertService")
public class DigitalInputAlertServiceImp implements DigitalInputAlertService {

	@Autowired  
	DigitalInputAlertRepositiry repo;
	@Override
	public List<DigitalInputAlert> findBymanagerid(Long managerid) {
		return repo.findBymanagerid(managerid);
	}

}
