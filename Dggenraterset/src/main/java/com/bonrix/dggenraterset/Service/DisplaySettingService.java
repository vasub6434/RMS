package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.DisplaySetting;

public interface DisplaySettingService {

	
	void save(DisplaySetting ds);
	
	List<Object[]> DisplaySettingList(String hostName);
	
	List<Object[]> AdminDisplaySettingList();
	
	String delete(Long id);
	
	/*public Object getSingleObject(String query);*/
	
	List<Object[]> getUserBymobile(String userName, String mobileNum);
	
	String updateUserPass(String userName, String mobileNum,String pass);
}

