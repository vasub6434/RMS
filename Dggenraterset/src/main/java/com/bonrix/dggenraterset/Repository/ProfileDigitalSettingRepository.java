package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bonrix.dggenraterset.Model.ProfileDigitalSetting;

public interface ProfileDigitalSettingRepository extends BaseRepository<ProfileDigitalSetting, Long>{

	@Query(value ="SELECT prid_sett_id, criticalness, paramname, profilename FROM profile_digital_setting WHERE user_id=?1", nativeQuery = true)
	public List<Object[]> getDigitalSettingList(long userId);
}
