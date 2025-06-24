package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.MaintenanceStaff;

public interface MaintenanceStaffService {

	public void SaveStaff(MaintenanceStaff staff);

	public MaintenanceStaff GetStaff(long staffId);

	public void deleteStaff(long staffId);
	
	public void deleteStaffDevice(long staffId);
	
	public List<Object[]> getStaffDetails(long userId); 
	
	public MaintenanceStaff getdeviceMaintenanceStaffByid(long staffId);
}
