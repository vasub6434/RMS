	package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.bonrix.dggenraterset.Model.AssignManagerLayout;
import com.bonrix.dggenraterset.Model.DashBoardLayout;

public interface DashBoardLayoutServices {

	public DashBoardLayout saveDAshBoardLayout(DashBoardLayout dashBoardLayout);

	public List<Object[]> getAllDashBoardLayout();

	void deletedashboard(long no);

	public DashBoardLayout update(DashBoardLayout dashBoardLayout);

	public AssignManagerLayout saveManagerDashBoardLayout(AssignManagerLayout assignManagerLayout);

	public List<Object[]> getProfilelist();

	public List<Object[]> getprofileByid(Long pr_id);

	public List<Object[]> getlayoutListByManager(long managerId);

	String getDeviceList(long managetId);
	
	public 	List<Object[]> getprofilename(long prid);
	
	public List<Object[]> getassManager(long dlid);
	
	public List<Object[]> getassprofileManager(long dlid);
	
	void deleteAssManager(long no);
	
	void deleteAssuser(@Param("id") Long id);
	public List<Object[]> getmanagerlayout(long managerId);
	public List<Object[]> getdlayoutview(long dlid,long managerId);
	
	public List<Object[]> getuserlayout(long userid);
	public List<Object[]> getdulayoutview(long dlid,long userid);
	
	public List<Object[]> getassUser(long mamagerdashid);

	
	
}