package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.DashboardMaster;
import com.bonrix.dggenraterset.Model.MenuMst;

public interface DashBoardMasterService {

	void newMenu(DashboardMaster mst);
	
	String getMenuUrl(long mid);
	
	List<Object[]> getAllocatedMenuByUserId(long Id);
	
	List<Object[]> getAllocatedMenuByManagerId(long Id);
	
	public DashboardMaster findByUserIdAndRole(Long userId, String role);

	public DashboardMaster findByManagerIdAndRole(Long managerId, String role);
	
}
