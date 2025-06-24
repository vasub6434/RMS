package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Dashboarddetails;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.DashboardRepository;

@Service
public class DashboardServicesImp implements DashboardServices {
	
	@Autowired
	DashboardRepository repository;

	@Override
	public void save(Dashboarddetails dd) {
		repository.save(dd);
		
	}

	@Override
	public List<Dashboarddetails> getlist() {
		return repository.findAll();
	}

	@Override
	public Dashboarddetails get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public String update(Dashboarddetails dd) {
		 repository.saveAndFlush(dd);
		 return new SpringException(true, "Dashboard sucessfully Updated").toString();
	}

	@Override
	public String delete(Long id) {
		repository.delete(id);
		 return new SpringException(true, "Dashboard sucessfully Deletaed").toString();
	}

}
