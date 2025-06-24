package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.MaintenanceStaff;
import com.bonrix.dggenraterset.Repository.MaintenanceStaffRepository;

@Service("MaintenanceStaffService")
public class MaintenanceStaffImpl implements MaintenanceStaffService {

	@Autowired
	MaintenanceStaffRepository repo;

	@Override
	public void SaveStaff(MaintenanceStaff staff) {
		
		repo.save(staff);
	}

	@Override
	public MaintenanceStaff GetStaff(long staffId) {
		return repo.getOne(staffId);
	}

	@Override
	public void deleteStaff(long staffId) {
		repo.delete(staffId);
	}

	@Override
	public void deleteStaffDevice(long staffId) {
		repo.deleteStaffDevice(staffId);
		
	}

	@Override
	public List<Object[]> getStaffDetails(long userId) {  
		return repo.getStaffDetails(userId);
	}

	@Override
	public MaintenanceStaff getdeviceMaintenanceStaffByid(long deviceId) {
		// TODO Auto-generated method stub
		return repo.findBydeviceid(deviceId);
	}

}
