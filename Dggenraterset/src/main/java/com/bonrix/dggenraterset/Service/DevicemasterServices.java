package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AssignDeviceUserGroup;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.Devicemaster;
import java.util.List;

public interface DevicemasterServices {
  void save(Devicemaster paramDevicemaster);
  
  String getlist();
  
  List<Devicemaster> getlistdeviceinfo();
  
  String delete(Long paramLong);
  
  Devicemaster get(Long paramLong);
  
  String update(Devicemaster paramDevicemaster);
  
  Devicemaster findOne(Long paramLong);
  
  List<Devicemaster> getlistBydeviceinfo_id(int paramInt);
  
  Devicemaster findByImei(String paramString);
  
  List<Devicemaster> findByuserId(Long paramLong);
  
  List<Object[]> getDeviceMasterlist();
  
  Devicemaster savedevicemaster(Devicemaster paramDevicemaster);
  
  List<Object[]> getDeviceMasterDataByManagerId();
  
  void updatedevicemaster(long paramLong, String paramString);
  
  void saveDevice(Devicemaster paramDevicemaster);
  
  String managetDeviceList(long paramLong1, long paramLong2);
  
  List<Object[]> getMyDeviced(Long paramLong);
  
  void deleteBydeviceId(Long paramLong);
  
  List<Object[]> GetUserByDevice(long paramLong);
  
  void saveDeviceUserGrp(AssignDeviceUserGroup paramAssignDeviceUserGroup);
  
  String deleteUserGrpByDevice(long paramLong);
  
  List<Object[]> getDeviceByManagerId(long paramLong);
  
  List<Object[]> getSiteForManager(long paramLong);
  
  String deleteUserByDevice(long paramLong);
  
  List<Object[]> getSiteForUser(long paramLong);
  
  List<Object[]> getDeviceForUser(long paramLong);
  
  List<Object[]> getDeviceAssignForUser(long paramLong);
  
  void updateDevice(Devicemaster paramDevicemaster);
  
  String deletelastrack(Long paramLong);
  
  String deletehistory(Long paramLong);
  
  String deleteassignuserdevice(Long paramLong);
  
  List<Object[]> getsiteidbydeviceid(Long paramLong);
  
  String deleteassignusers(Long paramLong);
  
  String deleteassignsiteusergroup(Long paramLong);
  
  String deleteassignsite(Long paramLong);
  
  List<AssignUserDevice> findByDevice_id(Long paramLong);
  
  List<AssignUserDevice> findByDevice_idmang(Long paramLong1, Long paramLong2);
  
  List<Object[]> getusersBymanagerId();
  
  List<Object[]> getDeviceBySite(long paramLong);
  
  List<Object[]> getAdminSite();
  
  List<Object[]> getBymanagerId(long paramLong);
  
  List<Object[]> getdeviceByadminId(long paramLong);
  
  List<Object[]> getDevieByManager(long paramLong);
  
  List<Object[]> getProfileByDevice(long paramLong);
  
  List<Object[]> getDevieByProfile(long paramLong);
  
  void updateDevicename(long paramLong, String paramString1, String paramString2);
  
  void deleteassignuserdevice(long paramLong1, long paramLong2);
  
  List<Object[]> getAnalogSettingsDevice(long paramLong);
  
  List<Object[]> getAllDevieByManager(long paramLong);
  
  List<Devicemaster> getMangerDeviceList(Long paramLong);
  
  List<Object[]> getUserprofileBySiteId(long paramLong1, long paramLong2);
  
  List<Object[]> getprofileDevice(long paramLong1, long paramLong2);
}