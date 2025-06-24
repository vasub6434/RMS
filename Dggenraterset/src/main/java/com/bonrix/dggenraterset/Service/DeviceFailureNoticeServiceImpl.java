package com.bonrix.dggenraterset.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.DeviceFailureNotice;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Repository.DeviceFailureNoticeRepository;

@Service("DeviceFailureNoticeServiceImpl")

public class DeviceFailureNoticeServiceImpl implements DeviceFailureNoticeService {

	@Autowired
	DeviceFailureNoticeRepository deviceFailureNoticeRepository;

	@Override
	public List<DeviceFailureNotice> getlist() {
		return deviceFailureNoticeRepository.findAll();
	}

	@Override
	public DeviceFailureNotice saveDeviceFailureNotice(DeviceFailureNotice deviceFailureNotice) {
		return deviceFailureNoticeRepository.saveAndFlush(deviceFailureNotice);
	}

	@Override
	public String delete(long no) {
		deviceFailureNoticeRepository.delete(no);
		return new SpringException(true, " sucessfully Deleted").toString();
	}

	@Override
	public DeviceFailureNotice update(DeviceFailureNotice bs) {
		return deviceFailureNoticeRepository.saveAndFlush(bs);
	}

	@Override
	public List<Object[]> getDeviceFailureNoticeList() {
		return deviceFailureNoticeRepository.getDeviceFailureNoticeList();
	}

	@Override
	public List<Object[]> getUserDeviceFailureCount(long userID) {
		return deviceFailureNoticeRepository.getUserDeviceFailureCount(userID);
	}

	@Override
	public List<Object[]> getAdminDeviceFailure() {
		return deviceFailureNoticeRepository.getAdminDeviceFailure();
	}

	@Override
	public List<Object[]> getManagerDeviceFailure(Long userId) {
		return deviceFailureNoticeRepository.getUserDeviceFailureCount(userId);
	}

	@Override
	public void deleteManagerById(long uid) {
		deviceFailureNoticeRepository.deleteManagerById(uid);

	}

	@Override
	public DeviceFailureNotice saveAdminDeviceFailureNotice(DeviceFailureNotice deviceFailureNotice) {
		return deviceFailureNoticeRepository.saveAndFlush(deviceFailureNotice);
	}
	
	@Override
	public List<Object[]> getAdminDeviceFailureData(long userID) {
		return deviceFailureNoticeRepository.getAdminDeviceFailureData(userID);
	}
	
	@Override
	public List<Object[]> getDeviceDataById(long deviceID) {
		return deviceFailureNoticeRepository.getDeviceDataById(deviceID);
	}
	
	@Override
	public List<Object[]> getDeviceFailureDateDiff() {
		return deviceFailureNoticeRepository.getDeviceFailureDateDiff();
	}

    @Override
	public List<Object[]> getTotalDeviceCount() {
		return deviceFailureNoticeRepository.getTotalDeviceCount();
	}	
	
	@Override
	public List<Object[]> getManagerDeviceFailureData(long userID) {
		
		return deviceFailureNoticeRepository.getManagerDeviceFailureData(userID);
	}	
	
	@Override
	public List<Object[]> getmanagerDeviceFailureDateDiff(long id) {
		return deviceFailureNoticeRepository.getmanagerDeviceFailureDateDiff(id);
	}
	
	@Override
	public List<Object[]> getTotalManagerDeviceCount(long id) {
		return deviceFailureNoticeRepository.getTotalManagerDeviceCount(id);
	}

	
	/*@Override
	public List<Object[]> getDeviceFailureDateDiff() {
		return deviceFailureNoticeRepository.getDeviceFailureDateDiff();
	}

	@Override
	public List<Object[]> getAdminDeviceFailureData(long userID) {
		return deviceFailureNoticeRepository.getAdminDeviceFailureData(userID);
	}

	@Override
	public List<Object[]> getTotalDeviceCount() {
		return deviceFailureNoticeRepository.getTotalDeviceCount();
	}

	@Override
	public List<Object[]> getDeviceDataById(long deviceID) {
		return deviceFailureNoticeRepository.getDeviceDataById(deviceID);
	}*/


	@Override
	public List<Object[]> paramWarnFailByProfile(String profileName) {
		return deviceFailureNoticeRepository.paramWarnFailByProfile(profileName);
	}

	@Override
	public List<Object[]> paramDigitalWarnFailByProfile(String profileName) {
		return deviceFailureNoticeRepository.paramDigitalWarnFailByProfile(profileName);
	}

	@Override
	public List<Object[]> getLasttrackDigital(long deviceId, String paramId) {
		return deviceFailureNoticeRepository.getLasttrackDigital(deviceId, paramId);
	}
	
	@Override
	public List<Object[]> getLasttrackAnalog(long deviceId, String paramId) {
		return deviceFailureNoticeRepository.getLasttrackAnalog(deviceId, paramId);
	}

}
