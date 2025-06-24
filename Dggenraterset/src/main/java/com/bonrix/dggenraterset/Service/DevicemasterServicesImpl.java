package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AssignDeviceUserGroup;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.AssignDeviceUserGroupRepository;
import com.bonrix.dggenraterset.Repository.AssignUserDeviceRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DevicemasterServicesImpl;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Deviceinfoservices")
public class DevicemasterServicesImpl implements DevicemasterServices {
  @Autowired
  DevicemasterRepository repository;
  
  @Autowired
  AssignUserDeviceRepository assignuserdevicerepository;
  
  @Autowired
  AssignDeviceUserGroupRepository deviceUsergrprepository;
  
  public void save(Devicemaster bs) {
    this.repository.save(bs);
  }
  
  public String getlist() {
    JSONArray arry = new JSONArray();
    List<Object[]> listt = this.repository.joinlist();
    listt.forEach(o -> {
          JSONObject obj = new JSONObject();
          obj.put("profilename", o[0].toString());
          obj.put("deviceinfo_id", o[1].toString());
          obj.put("devicename", o[2].toString());
          obj.put("prid", o[3].toString());
          arry.put(obj);
        });
    return arry.toString();
  }
  
  public List<Devicemaster> getlistdeviceinfo() {
    return this.repository.findAll();
  }
  
  public String delete(Long id) {
    this.repository.delete(id);
    return (new SpringException(true, "Deviceinfo sucessfully Deleted")).toString();
  }
  
  public List<Devicemaster> getlistBydeviceinfo_id(int deviceinfo_id) {
    System.out.println("the device info id" + deviceinfo_id);
    return null;
  }
  
  public Devicemaster get(Long id) {
    return (Devicemaster)this.repository.findOne(id);
  }
  
  public Devicemaster findOne(Long id) {
    return (Devicemaster)this.repository.findOne(id);
  }
  
  public String update(Devicemaster bs) {
    this.repository.saveAndFlush(bs);
    return (new SpringException(true, "Deviceinfo sucessfully Updated")).toString();
  }
  
  public Devicemaster findByImei(String imei) {
    return this.repository.findByImei(imei);
  }
  
  public List<Devicemaster> findByuserId(Long userId) {
    return this.repository.findByuserId(userId);
  }
  
  public List<Object[]> getDeviceMasterlist() {
    return this.repository.getAllDeviceMasterData();
  }
  
  public Devicemaster savedevicemaster(Devicemaster dvs) {
    return (Devicemaster)this.repository.saveAndFlush(dvs);
  }
  
  public List<Object[]> getDeviceMasterDataByManagerId() {
    return this.repository.getDeviceMasterDataByManagerId();
  }
  
  public void updatedevicemaster(long managerId, String deviceId) {
    this.repository.updateDevicemaster(managerId, deviceId);
  }
  
  public void saveDevice(Devicemaster mst) {
    this.repository.saveAndFlush(mst);
  }
  
  public String managetDeviceList(long managetId, long userId) {
    JSONArray arry = new JSONArray();
    List<Object[]> listt = this.repository.managetDeviceList(managetId, userId);
    listt.forEach(o -> {
          JSONObject obj = new JSONObject();
          obj.put("deviceId", o[0].toString());
          obj.put("deviceName", o[1].toString());
          arry.put(obj);
        });
    return arry.toString();
  }
  
  public List<Object[]> getMyDeviced(Long userId) {
    return this.repository.getMyDeviced(userId);
  }
  
  public void deleteBydeviceId(Long deviceId) {
    this.repository.deleteBydeviceId(deviceId);
  }
  
  public List<Object[]> GetUserByDevice(long deviceId) {
    return this.deviceUsergrprepository.getMyUserByDevice(Long.valueOf(deviceId));
  }
  
  public void saveDeviceUserGrp(AssignDeviceUserGroup bs) {
    this.deviceUsergrprepository.save(bs);
  }
  
  public String deleteUserGrpByDevice(long deviceId) {
    this.deviceUsergrprepository.deleteUserGrpByDevice(deviceId);
    return "1";
  }
  
  public List<Object[]> getDeviceByManagerId(long managerId) {
    return this.repository.getDeviceByManagerId(managerId);
  }
  
  public List<Object[]> getSiteForManager(long managerId) {
    return this.repository.getSiteForManager(managerId);
  }
  
  public String deleteUserByDevice(long deviceId) {
    this.deviceUsergrprepository.deleteUserByDevice(deviceId);
    return "1";
  }
  
  public List<Object[]> getSiteForUser(long userId) {
    return this.repository.getSiteForUser(userId);
  }
  
  public List<Object[]> getDeviceForUser(long userId) {
    return this.repository.getDeviceForUser(userId);
  }
  
  public List<Object[]> getDeviceAssignForUser(long userId) {
    return this.repository.getDeviceAssignForUser(userId);
  }
  
  public void updateDevice(Devicemaster mst) {
    this.repository.saveAndFlush(mst);
  }
  
  public String deletelastrack(Long device_id) {
    this.repository.deleteBylastrack(device_id);
    return (new SpringException(true, "Last Track Data Sucessfully Deleted")).toString();
  }
  
  public String deletehistory(Long device_id) {
    this.repository.deletehBydeviceId(device_id);
    return (new SpringException(true, "History Data Sucessfully Deleted")).toString();
  }
  
  public String deleteassignuserdevice(Long device_id) {
    this.repository.deleteassignuserdevice(device_id);
    return (new SpringException(true, "Assign User Data Sucessfully Deleted")).toString();
  }
  
  public List<Object[]> getsiteidbydeviceid(Long deviceid) {
    return this.repository.getsiteidbydeviceid(deviceid);
  }
  
  public String deleteassignusers(Long siteid) {
    this.repository.deleteassignusers(siteid);
    return (new SpringException(true, "AssignSiteUser Data Sucessfully Deleted")).toString();
  }
  
  public String deleteassignsiteusergroup(Long siteid) {
    this.repository.deleteassignsiteusergroup(siteid);
    return (new SpringException(true, "AssignSiteUserGroup Data Sucessfully Deleted")).toString();
  }
  
  public String deleteassignsite(Long deviceid) {
    this.repository.deleteassignsite(deviceid);
    return (new SpringException(true, "AssignSite Data Sucessfully Deleted")).toString();
  }
  
  public List<AssignUserDevice> findByDevice_id(Long device_id) {
    return this.assignuserdevicerepository.findBydeviceid(device_id.longValue());
  }
  
  public List<AssignUserDevice> findByDevice_idmang(Long device_id, Long managerid) {
    return this.assignuserdevicerepository.findBydeviceidmang(device_id.longValue(), managerid.longValue());
  }
  
  public List<Object[]> getusersBymanagerId() {
    return this.repository.getusersBymanagerId();
  }
  
  public List<Object[]> getDeviceBySite(long siteId) {
    return this.repository.getDeviceBySite(siteId);
  }
  
  public List<Object[]> getAdminSite() {
    return this.repository.getAdminSite();
  }
  
  public List<Object[]> getBymanagerId(long id) {
    return this.repository.getBymanagerId(id);
  }
  
  public List<Object[]> getdeviceByadminId(long adminid) {
    return this.repository.getdeviceByadminId(adminid);
  }
  
  public List<Object[]> getDevieByManager(long managerId) {
    return this.repository.getDevieByManager(managerId);
  }
  
  public List<Object[]> getProfileByDevice(long prId) {
    return this.repository.getProfileByDevice(prId);
  }
  
  public List<Object[]> getDevieByProfile(long deviceid) {
    return this.repository.getDevieByProfile(deviceid);
  }
  
  public void updateDevicename(long deviceid, String devicename, String altdevicename) {
    this.repository.updateDevicename(deviceid, devicename, altdevicename);
  }
  
  public void deleteassignuserdevice(long deviceid, long managerid) {
    this.assignuserdevicerepository.deleteassignuserdevice(Long.valueOf(deviceid), Long.valueOf(managerid));
  }
  
  public List<Object[]> getAnalogSettingsDevice(long managerId) {
    return this.repository.getAnalogSettingsDevice(managerId);
  }
  
  public List<Object[]> getAllDevieByManager(long managerId) {
    return this.repository.getSajanDevieByManagerId(managerId);
  }
  
  public List<Devicemaster> getMangerDeviceList(Long managerId) {
    return this.repository.findBymanagerId(managerId);
  }
  
  public List<Object[]> getUserprofileBySiteId(long userId, long siteId) {
    return this.repository.getUserprofileBySiteId(userId, siteId);
  }
  
  public List<Object[]> getprofileDevice(long userId, long profileId) {
    return this.repository.getprofileDevice(userId, profileId);
  }
}
