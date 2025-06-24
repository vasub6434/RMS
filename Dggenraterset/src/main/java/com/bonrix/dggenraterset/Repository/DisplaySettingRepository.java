package com.bonrix.dggenraterset.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.DisplaySetting;

public interface DisplaySettingRepository extends BaseRepository<DisplaySetting,Long>{
	
	@Query(value = "SELECT * FROM displaysetting where homeurl Like '%'||:hostName||'%' ", nativeQuery = true)
	public List<Object[]> DisplaySettingList(@Param("hostName") String hostName);
	
	@Query(value = "SELECT * FROM displaysetting", nativeQuery = true)
	public List<Object[]> AdminDisplaySettingList();
	
	@Query(value = "UPDATE users SET password=:pass WHERE username=:userName AND contact=:mobileNum", nativeQuery = true)
	String updateUserPass(@Param("userName") String userName, @Param("mobileNum") String mobileNum,@Param("pass") String pass);
	
	@Query(value = "SELECT * FROM users WHERE username=:userName AND contact=:mobileNum", nativeQuery = true)
	public List<Object[]> getUserBymobile(@Param("userName") String userName, @Param("mobileNum") String mobileNum);
}
