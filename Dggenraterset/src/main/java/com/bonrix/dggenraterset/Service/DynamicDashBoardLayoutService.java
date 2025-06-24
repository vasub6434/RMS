	package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignDashboardUsers;
import com.bonrix.dggenraterset.Model.DynamicDashBoardLayout;


public interface DynamicDashBoardLayoutService {
	
	DynamicDashBoardLayout saveDynamicDashBoard(DynamicDashBoardLayout dynamicDashBoardLayout);
	
	List<Object[]> getDynamiclayoutList();
	  
	public List<Object[]> getDeviceList(Long ids);
	
	public List<Object[]> getSiteList(Long siteid);
	
	String delete(long id);
	
	public List<Object[]> getAllUser(long id);
	
	public AssignDashboardUsers saveuserDashboard(AssignDashboardUsers assignDashboardUsers);
	
	DynamicDashBoardLayout updateDynamicDashBoard(DynamicDashBoardLayout dynamicDashBoardLayout);
	
}