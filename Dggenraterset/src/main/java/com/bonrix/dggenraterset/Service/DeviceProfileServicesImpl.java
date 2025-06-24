package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;


@Service("DeviceProfileServices")
public class DeviceProfileServicesImpl implements DeviceProfileServices  {
	@Autowired
	DeviceProfileRepository repository;
	
	 private static final Logger log = Logger.getLogger(DeviceProfileServicesImpl.class);
	
	public List<DeviceProfile> getlist() {
		return repository.findAll();
	}
	
	public void save(DeviceProfile bs) {
		log.info("Profile::: "+bs.getParameters().size());
		repository.save(bs);
		
	}
	public DeviceProfile get(Long id) {
		return repository.findOne(id);
	}
	
	public List<DeviceProfile> getDeviceProfilesByIds(List<Long> ids) {
	    return repository.getByPridList(ids);
	}


	public String delete(Long id) {
		 repository.delete(id);
		 return new SpringException(true, "Componet sucessfully Deleted").toString();
	}
	public String update(DeviceProfile bs) {
		repository.saveAndFlush(bs);
		return new SpringException(true, "Componet sucessfully Updated").toString();
	}
	
	
}
