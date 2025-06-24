package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.User;

public interface UserService {
	
	User saveuser(User user);
	
	void saveuser(String role, long username);

	List<User> getUserlist();
	
	List getUserlistnew1();
	
	List<Object[]> getUserlistnew(Long addedby);
	
	User getuserbyid(long userid);
	
	void update(User user);
	
	void deleteUsersById(long uid);  
	
	User getuserbyusername(String username);
	
	void saveassignuserdevice(AssignUserDevice bs);

	List getManagetList(long managerId);

	
	
	List getProfileList();  
	//ashishaj
	//user service
		List<Object[]> getAllmangerlist();
		List<Object[]> getuserverify(String username,String password);
	
		String deleteUserAssignDevice(long id);
		
		void newMasterPassword(long id,String password);
		
		void updatestatus(long id,boolean status);
		
		
		void updateManagerPassword(long id,String password);
		
		void deleteUsersRoleById(long uid);
		void deleteUsersDeviceById(long uid);
}
