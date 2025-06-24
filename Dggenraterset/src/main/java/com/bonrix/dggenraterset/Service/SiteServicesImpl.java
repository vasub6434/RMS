package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Repository.AssignSiteRepository;
import com.bonrix.dggenraterset.Repository.SiteRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.AssignSiteUsers;
import com.bonrix.dggenraterset.Model.AssignSiteUsersGroup;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Repository.AssignSiteRepository;
import com.bonrix.dggenraterset.Repository.AssignSiteUserGroupRepository;
import com.bonrix.dggenraterset.Repository.AssignSiteUserRepository;
import com.bonrix.dggenraterset.Repository.SiteRepository;

@Service("Siteservices")
public class SiteServicesImpl implements SiteServices{
	
	@Autowired
	SiteRepository  repository;
	
	@Autowired
	AssignSiteRepository  assgnrepository;

	@Autowired
	AssignSiteUserRepository  assgnuserrepository;
	
	@Autowired
	AssignSiteUserGroupRepository  assgnusergrprepository;
	
	@Override
	public void save(Site bs) {
		repository.save(bs);
	}

	@Override
	public void saveassignsite(AssignSite bs) {
		assgnrepository.save(bs);
	}

	@Override
	public List<Object[]> getSiteList(long managId) {
		return repository.getSiteList(managId);
	}

	@Override
	public String delete(Long id) {
		repository.delete(id);
		
		return "Successfully Deleted";
	}

	@Override
	public String deletesiteAssign(Long id) {
		assgnrepository.deleteSiteAssignId(id);
		return "1";
	}

	@Override
	public List<Object[]> AssignedDeviceBySite(long site_id) {
		return assgnrepository.AssignedDeviceBySite(site_id);
	}

	@Override
	public Site findBysiteid(Long site_id) {
		return repository.findOne(site_id);
	}
	
	
/*	@Override
	public Site findBysiteid(Long site_id) {
		return repository.findOne(site_id);
	}*/

	@Override
	public void saveassignsiteusers(AssignSiteUsers bs) {
		assgnuserrepository.save(bs);
	}

	@Override
	public void saveassignsiteusersgrp(AssignSiteUsersGroup bs) {
		assgnusergrprepository.save(bs);
	}
	
	@Override
	public List<Object[]> GetSiteByUser(long userId) {
		return assgnuserrepository.getMySite(userId);
	}

	@Override
	public List<Object[]> GetUserBySite(long siteId) {
		return assgnuserrepository.getMyUserBySite(siteId);
	}

	@Override
	public List<Object[]> GetUserGrpBySite(long siteId) {
		return assgnuserrepository.getMyUserGrpBySite(siteId);
	}

	@Override
	public String deleteUserBySite(long siteId) {
		assgnuserrepository.deleteUserBySite(siteId);
		return "1";
	}

	@Override
	public String deleteUserGrpBySite(long siteId) {
		assgnuserrepository.deleteUserGrpBySite(siteId);
		return "1";
	}
	
	@Override
	public List<Object[]> GetDeviceIdBySite(long siteId) {
		return assgnuserrepository.getMyDeviceBySite(siteId);
	}

	@Override
	public List<Object[]> GetSiteByManager(long managerId) {
		return assgnuserrepository.getManagerSite(managerId);
	}

	@Override
	public List<Object[]> GetMySiteByUserNew(long userId) {
		return repository.getMySiteList(userId);
	}

	@Override
	public List<Object[]> GetMySiteByUserGrp(long userId) {
		return repository.getMySiteListGrp(userId);
	}
	

}
