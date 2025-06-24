package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.ProfileAnalogSetting;
import com.bonrix.dggenraterset.Repository.ProfileAnalogSettingRepository;

@Service("ProfileAnalogSettingService")
public class ProfileAnalogSettingServiceImpl implements ProfileAnalogSettingService{
	
	@Autowired
	ProfileAnalogSettingRepository repo;

	@Override
	public void save(ProfileAnalogSetting bs) {
		repo.save(bs);
	}
	
	@Override
	public List<Object[]> getAnalogSettingList(long userId) {
		return repo.getAnalogSettingList(userId);
	}

	@Override
	public String delete(Long id) {
		repo.delete(id);
		return "Successfully Deleted";
	}

}
