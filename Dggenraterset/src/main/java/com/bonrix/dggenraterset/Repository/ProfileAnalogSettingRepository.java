package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bonrix.dggenraterset.Model.ProfileAnalogSetting;

public interface ProfileAnalogSettingRepository extends BaseRepository<ProfileAnalogSetting, Long>{
	
	@Query(value ="SELECT prid_sett_id, fail_min, paramname, profilename, rule_signature, warn_min FROM profile_analog_setting WHERE user_id=?1", nativeQuery = true)
	public List<Object[]> getAnalogSettingList(long userId);

}
