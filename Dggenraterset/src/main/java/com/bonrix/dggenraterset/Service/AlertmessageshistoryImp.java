package com.bonrix.dggenraterset.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Repository.AlertmessageshistoryRepository;

@Service("alertmessageshistory")
public class AlertmessageshistoryImp implements Alertmessageshistory {

	@Autowired
	AlertmessageshistoryRepository repo;
	
	@Override
	public AlertMessages savealertMessage(AlertMessages msg) {
		return repo.saveAndFlush(msg);
	}

}
