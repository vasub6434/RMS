package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.SystemParameter;
import com.bonrix.dggenraterset.Repository.SystemParameterRepository;


@Service("SysParameterservices")
public class SystemParameterServicesImpl implements SystemParameterServices{
	
	
	@Autowired
	SystemParameterRepository  repository;

	@Override
	public List<SystemParameter> getlist() {
		return repository.findAll();
	}

	@Override
	public void save(SystemParameter bs) {
		repository.save(bs);
	}

	@Override
	public SystemParameter get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public String delete(Long id) {
		repository.delete(id);
		 return new SpringException(true, "Parameter sucessfully Deleted").toString();
	}

	@Override
	public String update(SystemParameter bs) {
		repository.saveAndFlush(bs);
		return new SpringException(true, "Parameter sucessfully Updated").toString();
	}

	/*@Override
	public List<SystemParameter> getlistByprmtype(String prmtype) {
		return repository.findByPrmtype(prmtype);
	}*/

}
