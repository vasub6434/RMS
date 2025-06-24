package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignDashboardUsers;
import com.bonrix.dggenraterset.Model.DynamicDashBoardLayout;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.AssignDashboardUsersRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.DynamicDashBoardLayoutRepository;

@Service("dynamicDashBoardLayoutServiceImpl")
public class DynamicDashBoardLayoutServiceImpl implements DynamicDashBoardLayoutService {
	
	@Autowired
	DynamicDashBoardLayoutRepository dDashBoardname;

	@Autowired
	AssignDashboardUsersRepository assignDashboardUsersRepository;
	
	@Autowired
	DevicemasterRepository devicemasterrepository;

	@Override
	public DynamicDashBoardLayout saveDynamicDashBoard(DynamicDashBoardLayout dynamicDashBoardLayout) {		
		return dDashBoardname.save(dynamicDashBoardLayout);
	}

	@Override
	public List<Object[]> getDynamiclayoutList() {
		return dDashBoardname.getdynamiclayoutList();
	}

	@Override
	public List<Object[]> getDeviceList(Long deviceid) {
		return devicemasterrepository.getlistDevice(deviceid);
	}

	@Override
	public List<Object[]> getSiteList(Long ids) {
		return dDashBoardname.getSitelist(ids);
	}

	@Override
	public String delete(long no) {
		dDashBoardname.delete(no);
		return new SpringException(true, " sucessfully Deleted").toString();
	}

	@Override
	public List<Object[]> getAllUser(long id) {
		return dDashBoardname.getAllUserData(id);
	}

	@Override
	public AssignDashboardUsers saveuserDashboard(AssignDashboardUsers assignDashboardUsers) {
		return assignDashboardUsersRepository.save(assignDashboardUsers);
	}

	@Override
	public DynamicDashBoardLayout updateDynamicDashBoard(DynamicDashBoardLayout dynamicDashBoardLayout) {
		// TODO Auto-generated method stub
		return dDashBoardname.saveAndFlush(dynamicDashBoardLayout);
	}
}