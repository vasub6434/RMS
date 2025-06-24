package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Repository.HistoryRepository;

@Service("ReportService")
public class ReportServiceImp implements ReportService {

	@Autowired
	HistoryRepository repository;
	
	@Override
	public List<History> findBydeviceId(Long dId) {
		// TODO Auto-generated method stub
		return repository.findBydeviceId(dId);
	}

}
