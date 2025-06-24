package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignManagerLayout;
import com.bonrix.dggenraterset.Model.DashBoardLayout;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.AssignManagerLayoutRepository;
import com.bonrix.dggenraterset.Repository.DashBoardLayoutRepository;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;

@Service
public class DashBoardLayoutServicesImpl implements DashBoardLayoutServices {

	@Autowired
	DashBoardLayoutRepository dashBoardLayoutRepository;
	
	@Autowired
	AssignManagerLayoutRepository assignManagerLayoutRepository;
	
	@Autowired
	DeviceProfileRepository deviceprofilerepository;
	
	
	@Override
	public DashBoardLayout saveDAshBoardLayout(DashBoardLayout dashBoardLayout) {
		return dashBoardLayoutRepository.save(dashBoardLayout);
	}

	@Override
	public List<Object[]> getAllDashBoardLayout() {
		return dashBoardLayoutRepository.getAllDashBoardLayout();
	}

	
	@Override
	public DashBoardLayout update(DashBoardLayout dashBoardLayout) {
		return dashBoardLayoutRepository.saveAndFlush(dashBoardLayout);
	}

	@Override
	public AssignManagerLayout saveManagerDashBoardLayout(AssignManagerLayout assignManagerLayout) {
		return assignManagerLayoutRepository.save(assignManagerLayout);
	}

	@Override
	public List<Object[]> getProfilelist() {
		return assignManagerLayoutRepository.getProfilelist();
	}

	@Override
	public List<Object[]> getprofileByid(Long pr_id) {
		return assignManagerLayoutRepository.getprofileByid(pr_id);
	}

	@Override
	public String getDeviceList(long managetId) {
		JSONArray arry = new JSONArray();
		List<Object[]> listt = assignManagerLayoutRepository.getDeviceList(managetId);
		listt.forEach((Object[] o) -> {
			JSONObject obj = new JSONObject();
			obj.put("deviceId", o[0].toString());
			obj.put("deviceName", o[1].toString());
			arry.put(obj);
		});
		return arry.toString();
	}
	@Override
	public List<Object[]> getprofilename(long prid) {
		return deviceprofilerepository.getprofilename22(prid);
	}

	@Override
	public List<Object[]> getlayoutListByManager(long managerId) {
		return dashBoardLayoutRepository.getlayoutListByManager(managerId);
	}

	@Override
	public List<Object[]> getassManager(long dlid) {
		return dashBoardLayoutRepository.getassManager(dlid);
	}

	@Override
	public List<Object[]> getassprofileManager(long dlid) {
		
		return dashBoardLayoutRepository.getassprofileManager(dlid);
	}
	@Override
	public void deleteAssManager(long no) {
		dashBoardLayoutRepository.deleteAssManager(no);
		
	}

	@Override
	public void deletedashboard(long no) {
		dashBoardLayoutRepository.deleteDashboard(no);
		
	}

	@Override
	public void deleteAssuser(Long id) {
		dashBoardLayoutRepository.deleteAssuser(id);
		
	}
	
	@Override
	public List<Object[]> getmanagerlayout(long managerId) {
		return dashBoardLayoutRepository.getmanagerlayout(managerId);
	}
	
	@Override
	public List<Object[]> getdlayoutview(long dlid,long managerId) {
		return dashBoardLayoutRepository.getdlayoutview(dlid,managerId);
	}
	@Override
	public List<Object[]> getuserlayout(long userid) {
		return dashBoardLayoutRepository.getuserlayout(userid);
	}
	
	@Override
	public List<Object[]> getdulayoutview(long dlid,long managerId) {
		return dashBoardLayoutRepository.getdulayoutview(dlid,managerId);
	}

	@Override
	public List<Object[]> getassUser(long mamagerdashid) {
		
		return dashBoardLayoutRepository.getassuser(mamagerdashid);
	}
}