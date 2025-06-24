package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Repository.AssignUserDeviceRepository;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.UserRepository;

@Service("UserService")
public class UserServiceInp implements UserService {
  
	@Autowired   
	UserRepository userRepository;   
	@Autowired   
	AssignUserDeviceRepository userAsignRepository; 
	
	@Autowired   
	DeviceProfileRepository profilerepository; 
	

	
	@Override
	public User saveuser(User user) {
		return userRepository.saveAndFlush(user);
	}
	@Override
	public void saveuser(String role, long username) {
		 userRepository.saveRole( role ,username);
	}
	@Override    
	public List<User> getUserlist() {
		return userRepository.getAllUserData();
	}
	
	@Override    
	public 	List<Object[]> getUserlistnew(Long addedby) {
		return userRepository.getAllUserDatanew(addedby);
	}
	
	@Override
	public User getuserbyid(long userid) {
		return  userRepository.findByUserIdnew(userid);
	}
	
	@Override
	public void update(User user) {
		userRepository.saveAndFlush(user);
	}
	
	@Override
	public void deleteUsersById(long uid) {
		userRepository.deleteUsersById(uid);
	}
	
	@Override
	public User getuserbyusername(String username) {
		return userRepository.findByUserName(username);
	}
	@Override
	public List getUserlistnew1() {
		return userRepository.getAllUserData();
	}
	@Override
	public void saveassignuserdevice(AssignUserDevice bs) {
		userAsignRepository.save(bs);
	}
	@Override
	public List getManagetList(long managerId) {
		return userRepository.getManagetList(managerId);
	}

	
	
	//ashishaj
	//get managerlist
		@Override
		public List<Object[]> getAllmangerlist() {
			return userRepository.getAllmangerlist();
		}
		@Override
		public List<Object[]> getuserverify(String username, String password) {
			
			return userRepository.getuserverify(username, password);
		}
		
		@Override
		 public String deleteUserAssignDevice(long id) {
			userRepository.deleteUserAssignDevice(id);
			return "1";
		}
		@Override
		public List getProfileList() {   
			
			return profilerepository.getProfileList();
		}
		@Override
		public void newMasterPassword(long id, String password) {
			userRepository.newMasterPassword(id, password);
			
		}
		@Override
		public void updatestatus(long id, boolean status) {
			userRepository.updateStatus(id, status);
			
		}
		
		@Override
		public void updateManagerPassword(long id, String password) {
			userRepository.updateManagerPassword(id, password);
			
		}
		
		@Override
		public void deleteUsersRoleById(long uid) {
			userRepository.deleteUsersroleById(uid);
			
		}
		@Override
		public void deleteUsersDeviceById(long uid) {
			userRepository.deleteUsersDeviceById(uid);
			
		}
}
