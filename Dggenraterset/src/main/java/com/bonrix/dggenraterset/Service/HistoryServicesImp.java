package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AnalogInputAlert;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.DigitalInputAlert;
import com.bonrix.dggenraterset.Model.EmailTemplate;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.MessageTemplate;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.Tag;
import com.bonrix.dggenraterset.Model.TagType;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserGroup;
import com.bonrix.dggenraterset.Repository.AlertMessagesRepository;
import com.bonrix.dggenraterset.Repository.AnalogInputAlertRepository;
import com.bonrix.dggenraterset.Repository.AssignUserDeviceRepository;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.DigitalInputAlertRepositiry;
import com.bonrix.dggenraterset.Repository.EmailTemplateRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.LasttrackRepository;
import com.bonrix.dggenraterset.Repository.MessageTemplateRepository;
import com.bonrix.dggenraterset.Repository.ParameterRepository;
import com.bonrix.dggenraterset.Repository.SiteRepository;
import com.bonrix.dggenraterset.Repository.TagRepository;
import com.bonrix.dggenraterset.Repository.TagTypeRepository;
import com.bonrix.dggenraterset.Repository.UserGroupRepository;
import com.bonrix.dggenraterset.Repository.UserRepository;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.HistoryServicesImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("historyServices")
public class HistoryServicesImp implements HistoryServices {
	@Autowired
	HistoryRepository repository;

	@Autowired
	MessageTemplateRepository mtrepository;

	@Autowired
	TagTypeRepository tagtyperepository;

	@Autowired
	TagRepository tagrepository;

	@Autowired
	AlertMessagesRepository amrepository;

	@Autowired
	UserRepository userrepository;

	@Autowired
	UserGroupRepository usergrouprepository;

	@Autowired
	SiteRepository siterepository;

	@Autowired
	DevicemasterRepository devicerepository;

	@Autowired
	EmailTemplateRepository emailrepository;

	@Autowired
	DigitalInputAlertRepositiry digitalinputalertrepositiry;

	@Autowired
	AnalogInputAlertRepository analoginputalertrepository;

	@Autowired
	ParameterRepository prepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	AssignUserDeviceRepository userAsignRepository;

	@Autowired
	DeviceProfileRepository deviceprofileRepository;

	@Autowired
	LasttrackRepository lastrackrepository;

	public void saveAndFlush(History hist) {
		this.repository.saveAndFlush(hist);
	}

	public List<MessageTemplate> getlistByUser_id(Long userid) {
		return this.mtrepository.findByUser_id(userid);
	}

	public List<TagType> findAll() {
		return this.tagtyperepository.findAll();
	}

	public List<Tag> getsubtagbytag(String tag) {
		return this.tagrepository.findByTag(tag);
	}

	public MessageTemplate savemsgtemplate(MessageTemplate messagetemplate) {
		return (MessageTemplate) this.mtrepository.saveAndFlush(messagetemplate);
	}

	public void updatemsgtemplate(MessageTemplate messagetemplate) {
		this.mtrepository.saveAndFlush(messagetemplate);
	}

	public String deletemessagetemplate(Long m_id) {
		this.mtrepository.delete(m_id);
		return (new SpringException(true, "Messagetemplate sucessfully Deleted")).toString();
	}

	public List<MessageTemplate> getmessagetemplatebytype(String templatetype, Long uid) {
		return this.mtrepository.findByTemplatetype(templatetype, uid);
	}

	public User getstafflistbymid(Long manager_id) {
		return this.userrepository.findByAddedby(manager_id.longValue());
	}

	public List<UserGroup> getstaffgrouplistbymid(Long managerid) {
		return this.usergrouprepository.findByManagerid(managerid);
	}

	public List<Site> getsitelistbymid(Long managerid) {
		return this.siterepository.findByManagerid(managerid);
	}

	public List<Devicemaster> getdevicelistbymid(Long manager_id) {
		return this.devicerepository.findBymanagerId(manager_id);
	}

	public List<EmailTemplate> getemaillistBytemplatebytype(String templatetype, Long uid) {
		return this.emailrepository.findByTemplatetype(templatetype, uid);
	}

	public DigitalInputAlert savedigitalinputalert(DigitalInputAlert digitalinputalert) {
		return (DigitalInputAlert) this.digitalinputalertrepositiry.saveAndFlush(digitalinputalert);
	}

	public List<DigitalInputAlert> editdigitalinputalert(Long no) {
		return this.digitalinputalertrepositiry.findByNo(no);
	}

	public void updatedigitalinputalert(DigitalInputAlert digitalinputalert) {
		this.digitalinputalertrepositiry.saveAndFlush(digitalinputalert);
	}

	public String deletedigitalinputalert(Long no) {
		this.digitalinputalertrepositiry.delete(no);
		return (new SpringException(true, "Digitalinputalert sucessfully Deleted")).toString();
	}

	public List<Object[]> displaydigitalinputalert(Long manager_id) {
		return this.digitalinputalertrepositiry.getinputdigitalalertBymanager_id(manager_id);
	}

	public List getdata(String startDate, String endDate, String paramId, long device_id) {
		return this.repository.getdata(startDate, endDate, paramId, device_id);
	}

	public Parameter findByPrmname(String prmname) {
		return this.prepository.findByPrmname(prmname);
	}

	public List energymeterrpt2(Long id, String startdate, String endate, String pid) {
		return this.repository.energymeterrpt2(id, startdate, endate, pid);
	}

	public List deltameterrpt(Long id, String startdate, String endate, String prmname) {
		return this.repository.deltameterrpt(id, startdate, endate, prmname);
	}

	public List deltameterrpt2(Long id, String startdate, String endate, String prmname) {
		return this.repository.deltameterrpt2(id, startdate, endate, prmname);
	}

	public List deltameterrpt3(Long id, String startdate, String endate, String prmname) {
		return this.repository.deltameterrpt3(id, startdate, endate, prmname);
	}

	public List<Object[]> getalertmessagebymid(Long managerid) {
		return this.amrepository.getalertmessagebymid(managerid);
	}

	public List<Object[]> assigndeviceprofilebyuid(Long user_id) {
		return this.deviceprofileRepository.Assigndeviceprofilebyuid(user_id.longValue());
	}

	public List<Object[]> getdevicebyprid(Long prid, Long userid) {
		return this.devicerepository.getdevicebyprid(prid.longValue(), userid.longValue());
	}

	public Parameter findpOne(Long id) {
		return (Parameter) this.prepository.findOne(id);
	}

	public List<Object[]> getdevicekeyvalbydid(Long deviceid) {
		return this.lastrackrepository.getdevicekeyvalbydid(deviceid.longValue());
	}

	public List<Object[]> getdevicekeyvaldigitalbydid(Long deviceid) {
		return this.lastrackrepository.getdevicekeyvaldigitalbydid(deviceid.longValue());
	}

	public List<Object[]> getdhistorynobydid(Long deviceid, String startdate, String enddate) {
		return this.lastrackrepository.getdhistorynobydid(deviceid.longValue(), startdate, enddate);
	}

	public List<Object[]> getdhistoryanalogbyno(Long no) {
		return this.lastrackrepository.getdhistoryanalogbyno(no.longValue());
	}

	public List<Object[]> getdhistorydigitalbyno(Long no) {
		return this.lastrackrepository.getdhistorydigitalbyno(no.longValue());
	}

	public List<Object[]> getprofileanalogunit(Long prid, String analoginput) {
		return this.repository.getprofileanalogunit(prid, analoginput);
	}

	public History findOne(Long no) {
		return (History) this.repository.findOne(no);
	}

	public List<Object[]> adminprofiledevice() {
		return this.lastrackrepository.adminprofiledevice();
	}

	public List<Object[]> getdevicebyonlyprid(Long prid) {
		return this.lastrackrepository.getdevicebyonlyprid(prid.longValue());
	}

	public List<Object[]> assigndeviceprofilebymanagerid(Long manager_id) {
		return this.deviceprofileRepository.assigndeviceprofilebymanagerid(manager_id.longValue());
	}

	public List<Object[]> getdevicebyprmid(Long prid, Long manager_id) {
		return this.devicerepository.getdevicebyprmid(prid.longValue(), manager_id.longValue());
	}

	public List<Object[]> displayanaloginputalert(Long manager_id) {
		return this.analoginputalertrepository.getinputanalogalertBymanager_id(manager_id);
	}

	public AnalogInputAlert saveanaloginputalert(AnalogInputAlert analoginputalert) {
		return (AnalogInputAlert) this.analoginputalertrepository.saveAndFlush(analoginputalert);
	}

	public void updateanaloginputalert(AnalogInputAlert analoginputalert) {
		this.analoginputalertrepository.saveAndFlush(analoginputalert);
	}

	public List<AnalogInputAlert> editanaloginputalert(Long no) {
		return this.analoginputalertrepository.findByNo(no);
	}

	public String deleteanaloginputalert(Long no) {
		this.analoginputalertrepository.delete(no);
		return (new SpringException(true, "Analoginputalert sucessfully Deleted")).toString();
	}

	public List<Object[]> getDigitalHistory(Long deviceId, String startdate, String enddate) {
		return this.repository.getDigitalHistory(deviceId, startdate, enddate);
	}

	public List<History> findBydeviceId(Long deviceid) {
		return this.repository.findBydeviceId(deviceid);
	}

	public List<Object[]> getdeviceHistoryLocation(Long deviceid, String startdate, String enddate, Long max) {
		return this.repository.getdeviceHistoryLocation(deviceid, startdate, enddate, max);
	}

	public List<Object[]> getadminHistoryLocation(Long deviceid, String startdate, String enddate, Long max) {
		return this.repository.getadminHistoryLocation(deviceid, startdate, enddate, max);
	}

	public List getAnalogRawData(long deviceId, String date1, String date2) {
		return this.repository.getAnalogRawData(deviceId, date1, date2);
	}

	public List<Object[]> getperametername(Long prid) {
		return this.repository.getperametername(prid);
	}

	public List getAnalogData(Long deeviceId, String startdate, String endate, String prmname) {
		return this.repository.getAnalogData(deeviceId, startdate, endate, prmname);
	}

	public List getCandleData(Long id, String startdate, String endate, String prmname) {
		return this.repository.getCandleData(id, startdate, endate, prmname);
	}

	public List getFuelCandle(long deviceId, String date1, String date2) {
		return this.repository.getFuelCandle(deviceId, date1, date2);
	}

	public List getReport(Long id, String startdate, String endate, String prmname) {
		return this.repository.getReport(id, startdate, endate, prmname);
	}

	public List<Object[]> getSingleDigitalParmData(String startDate, String endDate, String paramId, long deviceId) {
		return this.repository.getSingleDigitalParmData(startDate, endDate, paramId, deviceId);
	}

	public List getLiveGraphData(Long id, String startdate, String enddate, String prmname) {
		return this.repository.getLiveGraphData(id, startdate, enddate, prmname);
	}

	public List getDeltaAnalogData(String startDate, long deviceId) {
		return this.repository.getDeltaAnalogData(startDate, deviceId);
	}

	public List<Object[]> getDeltaSingleAnalogData(String startDate, String enddate, String prmname, long deviceId) {
		return this.repository.getDeltaSingleAnalogData(startDate, enddate, prmname, deviceId);
	}

	public List getMonthlyDeltaDeviceCount(String startDate, String enddate, long deviceId) {
		return this.repository.getMonthlyDeltaDeviceCount(startDate, enddate, deviceId);
	}

	public List<Object[]> getDeltaDataReport(String inData, long deviceId) {
		return this.repository.getDeltaDataReport(inData, deviceId);
	}

	public long getDeltaDataReportCount(String devceDte, long deviceId) {
		return this.repository.getDeltaDataReportCount(devceDte, deviceId);
	}

	public List<Object[]> getDeltaDataReportSingleParam(String inData, long deviceId, String paramId) {
		return this.repository.getDeltaDataReportSingleParam(inData, deviceId, paramId);
	}

	public List<Object[]> getDeltaDataReportActiveClearData(String inData, long deviceId, String paramId,
			String status) {
		return this.repository.getDeltaDataReportActiveClearData(inData, deviceId, paramId, status);
	}

	public List<Object[]> getElasticsearch(String startDate, int limit, int offset) {
		return this.repository.getElasticsearch(startDate, limit, offset);
	}

	public List GetDeltaDeviceCount(String startDate) {
		return this.repository.GetDeltaDeviceCount(startDate);
	}

	public void DeleteHistoryData(String day) {
		this.repository.DeleteHistoryData(day);
	}

	public List<Object[]> getDigitalHistoryData(String startDate, String endDate, String paramId, long device_id) {
		return this.repository.DigitalHistoryData(startDate, endDate, paramId, device_id);
	}

	public List<Object[]> getalertmessagebyUserId(long userId, String alertType, Iterable<Long> deviceId,
			String startDate, String endDate) {
		return this.amrepository.getalertmessagebyUserId(userId, alertType, deviceId, startDate, endDate);
	}

	public List<Object[]> displaydigitalinputalertByUserId(Long userId) {
		return this.digitalinputalertrepositiry.getinputdigitalalertByuser_id(userId);
	}

	public void updateNumber(String mobileNos, Long alertId) {
		this.digitalinputalertrepositiry.updateMobileNos(mobileNos, alertId);
	}

	public List<Object[]> displayanaloginputalertByUserId(Long userId) {
		return this.analoginputalertrepository.getinputanalogalertByUser(userId);
	}

	public void updateAnalogAlertNumber(String mobileNos, Long alertId, Long volatege, String emailid, Long time) {
		this.analoginputalertrepository.updateAnalgoAlertMobileNos(mobileNos, alertId, volatege, emailid, time);
	}

	public List<Object[]> getFuelData(long deviceId, String startDate) {
		return this.repository.getFuelData(deviceId, startDate);
	}

	public void deleteHistoryData(String deleteDate) {
		this.repository.DeleteData(deleteDate);
	}

	public List GetRMSDeviceCount(String startDate, long deviceId) {
		return this.repository.GetRMSDeviceCount(startDate, deviceId);
	}

	public List<Object[]> getDeltaAllData(String inData, long deviceId) {
		return this.repository.getDeltaAllData(inData, deviceId);
	}

	public List getMyData(String query) {
		List data = this.em.createNativeQuery(query).getResultList();
		return data;
	}

	public List<Object[]> gerMonthlyBarChartData(long deviceId, String paramId, String startDate, String endDate) {
		return this.repository.gerMonthlyBarChartData(deviceId, paramId, startDate, endDate);
	}

	public List<Object[]> gerDailyBarChartData(long deviceId, String paramId, String startDate, String endDate) {
		return this.repository.getDailyBarChartData(deviceId, paramId, startDate, endDate);
	}

	public List<Object[]> gerYearChartData(long deviceId, String paramId, String startDate, String endDate) {
		return this.repository.getYearlyBarChartData(deviceId, paramId, startDate, endDate);
	}

	public List<Object[]> getdhistorynobydidPaginated(Long deviceid, String startdate, String enddate, int page,
			int size) {
		int offset = (page - 1) * size;
		return lastrackrepository.getdhistorynobydidPaginated(deviceid, startdate, enddate, offset, size);
	}

	@Override
	public int getdhistorynobydidCount(Long deviceid, String startdate, String enddate) {
		return lastrackrepository.getdhistorynobydidCount(deviceid, startdate, enddate);
	}

	@Override
	public ResponseEntity<Map<String, Object>> getPlaybackData(int deviceId, String startDate, String endDate,
			String dataType) {
		List<Object[]> rawData;
		if ("no".equalsIgnoreCase(dataType)) {
			rawData = repository.getNonZeroSpeedData(deviceId, startDate, endDate);
		} else {
			rawData = repository.getAllSpeedData(deviceId, startDate, endDate);
		}

		List<Map<String, Object>> data = new ArrayList<>();
		for (Object[] row : rawData) {
			Map<String, Object> map = new HashMap<>();
			map.put("deviceId", row[0]);
			map.put("latitude", row[1]);
			map.put("longitude", row[2]);
			map.put("speed", row[3]);
			map.put("angle", row[4]);
			map.put("deviceDate", row[5]);
			data.add(map);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("data", data);
		response.put("count", data.size());
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Map<String, Object>> getHistoryData(int deviceId, String startDate, String endDate) {
		List<Object[]> rawData = repository.getHistoryData(deviceId, startDate, endDate);

		List<Map<String, Object>> data = new ArrayList<>();
		for (Object[] row : rawData) {
			Map<String, Object> map = new HashMap<>();
			map.put("deviceId", row[0]);
			map.put("latitude", row[1]);
			map.put("longitude", row[2]);
			map.put("speed", row[3]);
			map.put("angle", row[4]);
			map.put("deviceDate", row[5]);
			data.add(map);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("data", data);
		response.put("count", data.size());

		return ResponseEntity.ok(response);
	}

	public List<Map<String, Object>> getAllByDeviceIdAndDateRange(Long deviceId) {
		List<Object[]> rows = repository.findAllByDeviceIdAndDateRange(deviceId);
		List<Map<String, Object>> result = new ArrayList<>();

		for (Object[] row : rows) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("deviceId", row[0]);
			map.put("deviceDate", row[1]);
			map.put("latitude", row[2]);
			map.put("longitude", row[3]);
			map.put("angle", row[4]);
			map.put("speed", row[5]);
			result.add(map);
		}

		return result;
	}

}
