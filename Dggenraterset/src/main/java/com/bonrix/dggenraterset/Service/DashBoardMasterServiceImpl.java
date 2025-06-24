package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.DashboardMaster;
import com.bonrix.dggenraterset.Model.MenuAllocationMst;
import com.bonrix.dggenraterset.Model.MenuMst;
import com.bonrix.dggenraterset.Repository.DashBoardMasterRepository;
import com.bonrix.dggenraterset.Repository.MenuRepositiry;

@Service
public class DashBoardMasterServiceImpl implements DashBoardMasterService {

	@Autowired
	DashBoardMasterRepository repository;
	
	
	@Override
	public void newMenu(DashboardMaster mst) {
		repository.save(mst);
	}


	@Override
	public String getMenuUrl(long mid) {
		return repository.getMenuUrl(mid);
	}
	
	@Override
	public List<Object[]> getAllocatedMenuByUserId(long Id) {
			return repository.getAllocatedMenuByUserId(Id);
	}
	@Override
	public List<Object[]> getAllocatedMenuByManagerId(long Id) {
			return repository.getAllocatedMenuByManagerId(Id);
	}


	@Override
	public DashboardMaster findByUserIdAndRole(Long userId, String role) {
	    return repository.findByUserIdAndRole(userId, role);
	}
	
	@Override
	public DashboardMaster findByManagerIdAndRole(Long managerId, String role) {
	    return repository.findByManagerIdAndRole(managerId, role);
	}
}
