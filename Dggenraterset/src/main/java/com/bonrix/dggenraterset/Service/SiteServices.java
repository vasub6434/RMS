package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.AssignSiteUsers;
import com.bonrix.dggenraterset.Model.AssignSiteUsersGroup;
import com.bonrix.dggenraterset.Model.Site;

public interface SiteServices {

	void save(Site bs);

	void saveassignsite(AssignSite bs);

	public List<Object[]> getSiteList(long managId);

	String delete(Long id);

	String deletesiteAssign(Long id);

	public List<Object[]> AssignedDeviceBySite(long site_id);

	Site findBysiteid(Long site_id);

	void saveassignsiteusers(AssignSiteUsers bs);

	void saveassignsiteusersgrp(AssignSiteUsersGroup bs);

	public List<Object[]> GetSiteByUser(long userId);

	public List<Object[]> GetUserBySite(long siteId);

	public List<Object[]> GetUserGrpBySite(long siteId);

	String deleteUserBySite(long siteId);

	String deleteUserGrpBySite(long siteId);
	
	public List<Object[]> GetDeviceIdBySite(long siteId);
	
	public List<Object[]> GetSiteByManager(long managerId);
	
	public List<Object[]> GetMySiteByUserNew(long userId);
	
	public List<Object[]> GetMySiteByUserGrp(long userId);	
	
}
