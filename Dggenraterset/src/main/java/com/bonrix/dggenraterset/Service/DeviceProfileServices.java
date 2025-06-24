package com.bonrix.dggenraterset.Service;

import java.util.List;


import com.bonrix.dggenraterset.Model.DeviceProfile;

public interface DeviceProfileServices {
	
	List<DeviceProfile> getlist();
	
	void save(DeviceProfile bs);

	DeviceProfile get(Long id);

	String delete(Long id);

	String update(DeviceProfile bs);
	
	List<DeviceProfile> getDeviceProfilesByIds(List<Long> ids);
	
}
