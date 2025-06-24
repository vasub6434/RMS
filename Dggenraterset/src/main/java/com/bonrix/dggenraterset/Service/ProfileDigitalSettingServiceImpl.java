package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.ProfileDigitalSetting;
import com.bonrix.dggenraterset.Repository.ProfileDigitalSettingRepository;


@Service("ProfileDigitalSettingService")
public class ProfileDigitalSettingServiceImpl implements ProfileDigitalSettingService{

	@Autowired
	ProfileDigitalSettingRepository repo;

	@Override
	public void save(ProfileDigitalSetting bs) {
		repo.save(bs);
	}
	
	@Override
	public List<Object[]> getDigitalSettingList(long userId) {
		return repo.getDigitalSettingList(userId);
	}

	@Override
	public String delete(Long id) {
		repo.delete(id);
		return "Successfully Deleted";
	}
	
}
