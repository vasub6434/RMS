package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Repository.AssignSiteRepository;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.AssignSiteServiceImp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AssignSiteService")
public class AssignSiteServiceImp implements AssignSiteService {
  @Autowired
  AssignSiteRepository repo;
  
  public List<AssignSite> findBysiteid(Long site_id) {
    return this.repo.findBysiteid(site_id);
  }
  
  public List<Object[]> getuserBySevices(Long device_id, Long manager_id) {
    return this.repo.getuserBySevices(device_id, manager_id);
  }
  
  public List<Object[]> demo(Iterable<Long> ids) {
    return this.repo.demo(ids);
  }
  
  public List<Object[]> getuserfromGroup(Long usergroup_id, Long managerId) {
    return this.repo.getuserfromGroup(usergroup_id, managerId);
  }
  
  public List<AssignSite> findBysiteidAnddeviceid(Long deviceid, Long siteid) {
    return this.repo.findBySiteidAndeviceid(deviceid.longValue(), siteid.longValue());
  }
  
  public List<Object[]> getuserByGroupId(Long usergroup_id, Long managerid) {
    return this.repo.getuserByGroupId(usergroup_id, managerid);
  }
  
  public String deleteUserAssignSite(long id) {
    this.repo.deleteUserAssignsite(id);
    return "1";
  }
  
  public String deleteUserGrpAssignSite(long id) {
    this.repo.deleteUserAssignsiteusergrp(id);
    return "1";
  }
  
  public List<AssignSite> findBydeviceid(Long deviceid) {
    return this.repo.findBydeviceid(deviceid);
  }
  
  public List<Object[]> getAssignedDeviceBySite(long siteId) {
    return this.repo.AssignedDeviceBySite(siteId);
  }
}
