package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.ProfileDigitalSetting;

public interface ProfileDigitalSettingService {
	
	void save(ProfileDigitalSetting bs);
	
	public List<Object[]> getDigitalSettingList(long userId);
	
	String delete(Long id);
}
