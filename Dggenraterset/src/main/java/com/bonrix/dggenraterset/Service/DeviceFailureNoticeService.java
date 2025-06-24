package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.DeviceFailureNotice;

public interface DeviceFailureNoticeService {

	List<DeviceFailureNotice> getlist();

	DeviceFailureNotice saveDeviceFailureNotice(DeviceFailureNotice deviceFailureNotice);

	String delete(long no);

	DeviceFailureNotice update(DeviceFailureNotice bs);

	List<Object[]> getDeviceFailureNoticeList();

	List<Object[]> getUserDeviceFailureCount(long userID);

	List<Object[]> getAdminDeviceFailure();

	List<Object[]> getManagerDeviceFailure(Long userId);

	void deleteManagerById(long uid);

	DeviceFailureNotice saveAdminDeviceFailureNotice(DeviceFailureNotice deviceFailureNotice);
	
List<Object[]> getAdminDeviceFailureData(long userID);
	
	public List<Object[]> getDeviceDataById(long deviceID);
	
	List<Object[]> getDeviceFailureDateDiff();

	public List<Object[]> getTotalDeviceCount();
	
/**/
	public List<Object[]> getManagerDeviceFailureData(long userID);
	
	public List<Object[]> getmanagerDeviceFailureDateDiff(long id);

public List<Object[]> getTotalManagerDeviceCount(long id);


/*List<Object[]> getAdminDeviceFailureData(long userID);

List<Object[]> getDeviceFailureDateDiff();*/

public List<Object[]> paramWarnFailByProfile(String profileName);

/*public List<Object[]> getTotalDeviceCount();

public List<Object[]> getDeviceDataById(long deviceID);*/

public List<Object[]> paramDigitalWarnFailByProfile(String profileName);

public List<Object[]> getLasttrackDigital(long deviceId,String paramId);

public List<Object[]> getLasttrackAnalog(long deviceId,String paramId);

}
