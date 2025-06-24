package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.Manager;


public interface ManagerService {
	
	Manager savemanager(Manager manager);
	
	Manager getmanagerbyid(long mngrid);
	
	Manager getmanagerbymanagername(String mngrname);
	
	void savemanager(String role, long mngrname);

	List<Manager> getManagerlist();
	
	List<Object[]> getManagerlistnew();
	
	List<Manager> getlist();
	
	void update(Manager manager);
	
  /* Object getSingleObject(String query);*/
	
	void deleteManagersById(long mngrid);

}
