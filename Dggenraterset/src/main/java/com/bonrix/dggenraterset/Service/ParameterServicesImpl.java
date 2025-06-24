package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.ParameterRepository;

@Service("Parameterservices")
public class ParameterServicesImpl implements ParameterServices{

	@Autowired
	ParameterRepository  repository;
	
	public List<Parameter> getlist() {
		return repository.findAll();
	}

	public void save(Parameter bs) {
		repository.saveAndFlush(bs);
		
	}

	public Parameter get(Long id) {
		return repository.findOne(id);
	}

	public String delete(Long id) {
		repository.delete(id);
		 return new SpringException(true, "Parameter sucessfully Deleted").toString();
	}

	public String update(Parameter bs) {
		repository.saveAndFlush(bs);
		return new SpringException(true, "Parameter sucessfully Updated").toString();
	}

	public List<Parameter> getlistByprmtype(String prmtype) {
		return repository.findByPrmtype(prmtype);
	}
	
	@Override
	public List<Object[]> getLasttrackByDeviceId(long deviceId) {
		return repository.getLasttrackByDeviceId(deviceId);
	}
	
	@Override
	public String getLasttrackUnits(long deviceId,String count) {
		return repository.getLasttrackUnitsNew(deviceId,count);
	}
	
	@Override
	public List<Object[]> getDeviceProfileByDeviceId(long deviceId) {
	return repository.getDeviceProfileByDeviceId(deviceId);
	}

}
