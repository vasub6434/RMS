package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.ProfileAnalogSetting;

public interface ProfileAnalogSettingService {

	void save(ProfileAnalogSetting bs);
	
	public List<Object[]> getAnalogSettingList(long userId);
	
	String delete(Long id);
}
