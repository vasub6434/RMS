package com.bonrix.dggenraterset.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.DeviceFailureNotice;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DeviceFailureNoticeService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class DeviceFailureNoticeController {

	private static final Logger log = Logger.getLogger(DeviceFailureNoticeController.class);

	@Autowired
	public DeviceFailureNoticeService Dfnservices;
	@Autowired
	DevicemasterServices devicemasterservices;
	@Autowired
	ApiService apiService;
	

	@RequestMapping(value = "/api/deviceFailuerNoticelist", method = RequestMethod.GET)
	public List<DeviceFailureNotice> getAllDeviceFailureNoticelist() {
		log.info("Sucessfully getAll List:");
		return Dfnservices.getlist();
	}

	@RequestMapping(value = "/api/delete/deviceFailuerNotice/{id}", method = RequestMethod.GET)
	public void deleteDeviceFailureNoticeById(@PathVariable long id) {
		log.info("Sucessfully delete ByID:");
		Dfnservices.delete(id);
	}

	@RequestMapping(value = "/api/savedeviceFailuerNotice", method = RequestMethod.POST)
	public String saveDeviceFailureNotice(@RequestBody DeviceFailureNotice deviceFailureNotice) {

		long userId = deviceFailureNotice.getUserid();

		List<Object[]> list = new ArrayList<>();
		list = Dfnservices.getUserDeviceFailureCount(userId);

		if (list.size() == 1) {
			return new SpringException(false, "DeviceFailure Criteria Already Set For This User").toString();
		} else {
			log.info("Sucessfully Save:");
			Dfnservices.saveDeviceFailureNotice(deviceFailureNotice);
			return new SpringException(true, "Sucessfully Save").toString();
		}

	}

	@RequestMapping(value = "/api/editdeviceFailuerNotice/{editfailmin}/{editwarnmin}/{aaid}/{userId}", method = RequestMethod.GET)
	public DeviceFailureNotice editDeviceFailureNoticeById(@PathVariable long editfailmin,
			@PathVariable long editwarnmin, @PathVariable long aaid, @PathVariable long userId) {

		log.info("  " + "editfailmin:::" + editfailmin + "editwarnmin::" + editwarnmin + "aaid::" + aaid);
		DeviceFailureNotice deviceFailureNotice = new DeviceFailureNotice();
		deviceFailureNotice.setFailureminute(editfailmin);
		deviceFailureNotice.setWarningminute(editwarnmin);
		deviceFailureNotice.setNo(aaid);
		deviceFailureNotice.setUserid(userId);
		return Dfnservices.update(deviceFailureNotice);
	}

	@RequestMapping(value = "/api/getDeviceFailureList", produces = { "application/json" })
	public String getDeviceFailureList(HttpServletRequest request) {
		List<Object[]> list = new ArrayList<>();
		list = Dfnservices.getDeviceFailureNoticeList();
		JSONArray jarray = new JSONArray();

		String no = "";
		String failureminute = "";
		String userid = "";
		String username = "";
		String warningminute = "";
		for (Object[] result1 : list) {
			no = result1[0].toString();
			failureminute = result1[1].toString();
			userid = result1[2].toString();
			username = result1[4].toString();
			warningminute = result1[3].toString();

			JSONObject jo = new JSONObject();
			jo.put("no", no);
			jo.put("userid", userid);
			jo.put("username", username);
			jo.put("warningminute", warningminute);
			jo.put("failureminute", failureminute);
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getAdminDeviceFailureList", produces = { "application/json" })
	public String getAdminDeviceFailureList(HttpServletRequest request) {
		List<Object[]> list = new ArrayList<>();
		list = Dfnservices.getAdminDeviceFailure();
		JSONArray jarray = new JSONArray();

		String no = "";
		String failureminute = "";
		String userid = "";
		String warningminute = "";
		for (Object[] result1 : list) {
			no = result1[0].toString();
			failureminute = result1[1].toString();
			userid = result1[2].toString();
			warningminute = result1[3].toString();

			JSONObject jo = new JSONObject();
			jo.put("no", no);
			jo.put("failureminute", failureminute);
			jo.put("userid", userid);
			jo.put("warningminute", warningminute);

			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getManagerDeviceFailureList/{id}", produces = { "application/json" })
	public String getManagerDeviceFailureList(HttpServletRequest request, @PathVariable Long id) {
		List<Object[]> list = new ArrayList<>();
		list = Dfnservices.getManagerDeviceFailure(id);
		JSONArray jarray = new JSONArray();

		String no = "";
		String failureminute = "";
		String userid = "";
		String warningminute = "";
		for (Object[] result1 : list) {
			no = result1[0].toString();
			failureminute = result1[1].toString();
			userid = result1[2].toString();
			warningminute = result1[3].toString();

			JSONObject jo = new JSONObject();
			jo.put("no", no);
			jo.put("failureminute", failureminute);
			jo.put("userid", userid);
			jo.put("warningminute", warningminute);

			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "api/deleteUserById/{id}")
	public void deleteUserById(HttpServletRequest request, @PathVariable Long id) {

		System.out.println("id::" + id);
		Dfnservices.deleteManagerById(id);
	}

	@RequestMapping(value = "/api/saveManagerDeviceFailureNoticeById/{failureminute}/{warningminute}/{Id}", method = RequestMethod.GET)
	public String saveManagerDeviceFailureNoticeById(@PathVariable long failureminute, @PathVariable long warningminute,
			@PathVariable long Id) {

		DeviceFailureNotice deviceFailureNotice = new DeviceFailureNotice();
		List<Object[]> list = new ArrayList<>();
		list = Dfnservices.getUserDeviceFailureCount(Id);
		System.out.println("Controller_list:::" + list.size());

		String autoNum = "";
		for (Object[] result1 : list) {
			autoNum = result1[0].toString();
			System.out.println(autoNum);
		}

		if (list.size() == 1) {
			deviceFailureNotice.setFailureminute(failureminute);
			deviceFailureNotice.setWarningminute(warningminute);
			deviceFailureNotice.setUserid(Id);
			deviceFailureNotice.setNo(Long.parseLong(autoNum));
			Dfnservices.update(deviceFailureNotice);
			return new SpringException(false, "Sucessfully Updated").toString();
		} else {
			deviceFailureNotice.setFailureminute(failureminute);
			deviceFailureNotice.setWarningminute(warningminute);
			deviceFailureNotice.setUserid(Id);
			Dfnservices.saveDeviceFailureNotice(deviceFailureNotice);
			return new SpringException(true, "Sucessfully Save").toString();
		}
	}
	
	@RequestMapping(value = "/api/deviceSummary", method = RequestMethod.GET)
	public String deviceSummary() {
		long userID = 2;
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();
		
		 List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
		 String adminFailureminute = "";
		 String adminWarningminute = "";
		 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
			 adminFailureminute = DeviceAdminresult[0].toString();
			 adminWarningminute = DeviceAdminresult[1].toString();
		 }
		 int warningCount=0;
		 int failureCount = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
		String deviceId = "";
		String deviceDate = "";
		String sysDate = "";
		Date d1 = null;
	    Date d2 = null;
		Date d  = new Date();
		String strDate = format.format(d);
		for (Object[] result1 : list) {
			deviceId = result1[0].toString();
			deviceDate = result1[1].toString();
			sysDate = result1[2].toString();
			int finalMinutes = 0;
			try {
				 d1 = format.parse(deviceDate);
				 d2 = format.parse(strDate);
				 long diff = d2.getTime() - d1.getTime();
				 long diffMinutes = diff / (60 * 1000) % 60;
				 int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				  finalMinutes = days * 1440;
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(finalMinutes > Integer.parseInt(adminWarningminute)   && finalMinutes < Integer.parseInt(adminFailureminute)) {
				warningCount++;
			}else if(finalMinutes > Integer.parseInt(adminFailureminute)) {
				failureCount++;
			}else{
				failureCount=0;
				warningCount=0;
			}
		}
		List<Object[]> deviceCountList = Dfnservices.getTotalDeviceCount();
		int deviceCount=0;
		String deviceIdNew="0";
		int deviceIdealCount=0;
		int deviceNeverUsedCount=0;
		for (Object[] result1 : deviceCountList) {
			deviceIdNew= result1[0].toString();
			List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
			if(idealDeviceList.size() == 1) {
				deviceIdealCount++;
			}
			if(idealDeviceList.size() == 0) {
				deviceNeverUsedCount++;
			}
			deviceCount++;
		}
		jo.put("failureCount", failureCount);
		jo.put("warningCount", warningCount);
		jo.put("deviceCount", deviceCount);
		jo.put("deviceIdealCount", deviceIdealCount);
		jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
		jarray.put(jo);
		return jarray.toString();

	}
	@RequestMapping(value = "/api/deviceSummarymanager/{managerid}", method = RequestMethod.GET)
	public String deviceSummarymanager(@PathVariable("managerid") long managerid) throws java.text.ParseException  {
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();
		
		 List<Object[]> deviceFailureAdminlist =Dfnservices.getManagerDeviceFailureData(managerid);
		 String adminFailureminute = "";
		 String adminWarningminute = "";
		 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
			 adminFailureminute = DeviceAdminresult[0].toString();
			 adminWarningminute = DeviceAdminresult[1].toString();
		 }
		 int warningCount=0;
		 int failureCount = 0;
		 int success = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Object[]> list = Dfnservices.getmanagerDeviceFailureDateDiff(managerid);
		String deviceId = "";
		String deviceDate = "";
		String sysDate = "";
		Long diffMinutes = null;
		long diffMinutes2 = 0;
		Date d1 = null;
	    Date d2 = null;
		Date d  = new Date();
		String strDate = format.format(d);
		for (Object[] result1 : list) {
			deviceId = result1[2].toString();
			deviceDate = result1[0].toString();
			sysDate = result1[1].toString();
			int finalMinutes = 0;
			try {
				 d1 = format.parse(deviceDate);
				 d2 = format.parse(strDate);
				 long diff = d2.getTime() - d1.getTime();
				 diffMinutes = diff / (60 * 1000) % 60;
				 diffMinutes2=diff / (60 * 1000) ;
				 
				 int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				  finalMinutes = days * 1440;
			} catch (Exception e) {
				e.printStackTrace();
			}	
			if(diffMinutes2 > Integer.parseInt(adminWarningminute)   && diffMinutes2 < Integer.parseInt(adminFailureminute)) {
				warningCount++;
			}else if(diffMinutes2 > Integer.parseInt(adminFailureminute)) {
				failureCount++;	
			}else if(diffMinutes2==0){
				success++;
			}
		}
		List<Object[]> deviceCountList = Dfnservices.getTotalManagerDeviceCount(managerid);
		int deviceCount=0;
		String deviceIdNew="0";
		int deviceIdealCount=0;
		int deviceNeverUsedCount=0;
		for (Object[] result1 : deviceCountList) {
			deviceIdNew= result1[0].toString();
			List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
			if(idealDeviceList.size() == 1) {
				deviceIdealCount++;
			}
			if(idealDeviceList.size() == 0) {
				deviceNeverUsedCount++;
			}
			deviceCount++;
		}
		jo.put("failureCount", failureCount);
		jo.put("warningCount", warningCount);
		jo.put("deviceCount", deviceCount);
		jo.put("deviceIdealCount", deviceIdealCount);
		jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
		jo.put("success", success);
		jarray.put(jo);
		return jarray.toString();

	}
	
	
	
	
	
	/*@RequestMapping(value = "/api/deviceSummaryCritical/{userID}", method = RequestMethod.GET)  //{userID}
	public String deviceSummaryCritical(@PathVariable long userID) {      // @PathVariable long userID
	//	long userID = 2;
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();
		JSONArray blinkArray = new JSONArray();
		
		
		 List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
		 String adminFailureminute = "";
		 String adminWarningminute = "";
		 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
			 adminFailureminute = DeviceAdminresult[0].toString();
			 adminWarningminute = DeviceAdminresult[1].toString();
		 }
		 int warningCount=0;
		 int failureCount = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
		String deviceId = "";
		String deviceDate = "";
		String sysDate = "";
		String devicename = "";
		String prid_fk = "";
		String profilename = "";
		
		
		String paramName = "";
		String warnMin = "";
		String failMin = "";
		String sign_rule="";
		
		Date d1 = null;
	    Date d2 = null;
		Date d  = new Date();
		String strDate = format.format(d);
		for (Object[] result1 : list) {
			deviceId = result1[0].toString();
			deviceDate = result1[1].toString();
			sysDate = result1[2].toString();
			devicename = result1[3].toString();
			prid_fk = result1[4].toString();
			profilename = result1[5].toString();
			
			JSONObject blinkObj = new JSONObject();
			
			System.out.println("devicename:::"+devicename+" "+"prid_fk:::"+prid_fk+" "+"profilename:::"+profilename);
			
			List<Object[]> analogWarnFail = Dfnservices.paramWarnFailByProfile(profilename);
			for(Object[] rs :analogWarnFail) {
				warnMin = rs[1].toString();
				failMin = rs[2].toString();
				paramName = rs[3].toString();
				sign_rule = rs[5].toString();
			
			System.out.println("warnMin:::"+warnMin+" "+"failMin:::"+failMin+" "+"paramName:::"+paramName+" "+"sign_rule:::"+sign_rule);
			
			blinkObj.put("signRule", sign_rule);
			if(Integer.parseInt(warnMin) > Integer.parseInt(adminWarningminute)  && Integer.parseInt(failMin) < Integer.parseInt(adminFailureminute)) {
				blinkObj.put("Parameter", paramName);
			}else if (Integer.parseInt(failMin) > Integer.parseInt(adminFailureminute)) {
				blinkObj.put("Parameter", paramName);
			}else {
				blinkObj.put("Parameter", paramName);
			}
			
		}
			
			int finalMinutes = 0;
			try {
				 d1 = format.parse(deviceDate);
				 d2 = format.parse(strDate);
				 long diff = d2.getTime() - d1.getTime();
				 long diffMinutes = diff / (60 * 1000) % 60;
				 int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				  finalMinutes = days * 1440;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(finalMinutes > Integer.parseInt(adminWarningminute) && finalMinutes < Integer.parseInt(adminFailureminute)) {
				warningCount++;
				blinkObj.put("Stat", "WARN");
				blinkObj.put("adminMin", adminWarningminute);
				blinkObj.put("settMin", warnMin);
			}else if(finalMinutes > Integer.parseInt(adminFailureminute)) {
				failureCount++;
				blinkObj.put("Stat", "FAIL");
				blinkObj.put("adminMin", adminFailureminute);
				blinkObj.put("settMin", failMin);
			}else{
				failureCount=0;
				warningCount=0;
			}
			blinkObj.put("deviceId", deviceId);
			blinkObj.put("deviceName", devicename);
			blinkObj.put("deviceDate", deviceDate);
			blinkArray.put(blinkObj);
			
			System.out.println("finalMinutes::::"+finalMinutes+" "+"deviceId::::"+deviceId+" "+"deviceName::::"+ devicename);
		}
		List<Object[]> deviceCountList = Dfnservices.getTotalDeviceCount();
		int deviceCount=0;
		String deviceIdNew="0";
		int deviceIdealCount=0;
		int deviceNeverUsedCount=0;
		for (Object[] result1 : deviceCountList) {
			deviceIdNew= result1[0].toString();
			List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
			if(idealDeviceList.size() == 1) {
				deviceIdealCount++;
			}
			if(idealDeviceList.size() == 0) {
				deviceNeverUsedCount++;
			}
			deviceCount++;
		}
		jo.put("failureCount", failureCount);
		jo.put("warningCount", warningCount);
		jo.put("deviceCount", deviceCount);
		jo.put("deviceIdealCount", deviceIdealCount);
		jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
		jo.put("Data", blinkArray);
		jarray.put(jo);
		return jarray.toString();

	}
	
	
	
	
	
	@RequestMapping(value = "/api/deviceDigitalSummaryCritical/{userID}", method = RequestMethod.GET)  //{userID}
	public String deviceDigitalSummaryCritical(@PathVariable long userID) {      // @PathVariable long userID
	//	long userID = 2;
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();
		JSONArray blinkArray = new JSONArray();
		
		
		 List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
		 String adminFailureminute = "";
		 String adminWarningminute = "";
		 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
			 adminFailureminute = DeviceAdminresult[0].toString();
			 adminWarningminute = DeviceAdminresult[1].toString();
		 }
		 int warningCount=0;
		 int failureCount = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
		String deviceId = "";
		String deviceDate = "";
		String sysDate = "";
		String devicename = "";
		String prid_fk = "";
		String profilename = "";
		
		
		String paramId = "";
		String paramName = "";
		String criticalness = "";
		
		String devNewID="";
		String paramStat="";
		
		Date d1 = null;
	    Date d2 = null;
		Date d  = new Date();
		String strDate = format.format(d);
		for (Object[] result1 : list) {
			deviceId = result1[0].toString();
			deviceDate = result1[1].toString();
			sysDate = result1[2].toString();
			devicename = result1[3].toString();
			prid_fk = result1[4].toString();
			profilename = result1[5].toString();
			
			
			
		//	System.out.println("deviceId:::"+deviceId+" "+"devicename:::"+devicename+" "+"prid_fk:::"+prid_fk+" "+"profilename:::"+profilename);
			
			List<Object[]> analogWarnFail = Dfnservices.paramDigitalWarnFailByProfile(profilename);

			for(Object[] rs :analogWarnFail) {
				JSONObject blinkObj = new JSONObject();
		    	int val=2;
				paramId = rs[0].toString();
				paramName = rs[1].toString();
				criticalness = rs[2].toString();
			
			System.out.println("paramId:::"+paramId+" "+"criticalness:::"+criticalness+" "+"paramName:::"+paramName);
			if(criticalness.equalsIgnoreCase("ON")) {
				val=1;
			}else if(criticalness.equalsIgnoreCase("OFF")) {
				val=0;
			}else {
				val=3;
			}
			List<Object[]> digilasttrackParams = Dfnservices.getLasttrackDigital(Long.parseLong(deviceId),paramId);
			for(Object[] rs2 : digilasttrackParams) {
				devNewID=rs2[0].toString();
				paramStat=rs2[1].toString();
				
				System.out.println("paramId:::"+paramId+" "+"paramName:::"+paramName);
				
				if(!(Integer.parseInt(paramStat)==val)) {
				   blinkObj.put("Stat", "OFF");
				}else {
				   blinkObj.put("Stat", "ON");
				}
				System.out.println("devNewID:::"+devNewID+" "+"paramStat:::"+paramStat+" "+"val:::"+val);
				
				blinkObj.put("deviceId", deviceId);
				blinkObj.put("paramName", paramName);
				blinkObj.put("deviceName", devicename);
				blinkObj.put("deviceDate", deviceDate);
				blinkArray.put(blinkObj);
			 }
				
		    }
		}
		jo.put("Data", blinkArray);
		jarray.put(jo);
		return jarray.toString();

	}
	*/
	
	@RequestMapping(value = "/api/getDeviceInfoById/{device_id}", produces = { "application/json" })
	public String getDeviceInfoById(HttpServletRequest request, @PathVariable Long device_id) {
		List<Object[]> list = new ArrayList<>();
		list = devicemasterservices.getDevieByProfile(device_id);
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			JSONObject jo = new JSONObject();
			jo.put("deviceDesc", result1[4].toString());
			jo.put("deviceModel", result1[1].toString());
			jo.put("deviceName", result1[0].toString());
			jo.put("isActive", result1[5].toString());
			jo.put("deviceIMEI", result1[2].toString());
			jo.put("deviceSIMCardNo", result1[3].toString());
			jo.put("deviceDate", result1[6].toString());
			jo.put("systemDate", result1[7].toString());
			
			jarray.put(jo);
		}
		return jarray.toString();
	}
	
	@RequestMapping(value = "/api/deviceSummaryCritical/{userID}/{key}", method = RequestMethod.GET)  //{userID}
	public String deviceSummaryCritical(@PathVariable long userID,@PathVariable String key) {      // @PathVariable long userID
	//	long userID = 2;
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
		
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			JSONArray blinkArray = new JSONArray();
			
			
			 List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			 String adminFailureminute = "";
			 String adminWarningminute = "";
			 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				 adminFailureminute = DeviceAdminresult[0].toString();
				 adminWarningminute = DeviceAdminresult[1].toString();
			 }
			 int warningCount=0;
			 int failureCount = 0;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			String devicename = "";
			String prid_fk = "";
			String profilename = "";
			
			Date d1 = null;
		    Date d2 = null;
			Date d  = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceDate = result1[1].toString();
				sysDate = result1[2].toString();
				devicename = result1[3].toString();
				prid_fk = result1[4].toString();
				profilename = result1[5].toString();
				
				System.out.println("devicename:::"+devicename+" "+"prid_fk:::"+prid_fk+" "+"profilename:::"+profilename);
				
				int finalMinutes = 0;
				try {
					 d1 = format.parse(deviceDate);
					 d2 = format.parse(strDate);
					 long diff = d2.getTime() - d1.getTime();
					 long diffMinutes = diff / (60 * 1000) % 60;
					 int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					  finalMinutes = days * 1440;
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("finalMinutes::::"+finalMinutes+" "+"deviceId::::"+deviceId+" "+"deviceName::::"+ devicename);
			}
			List<Object[]> deviceCountList = Dfnservices.getTotalDeviceCount();
			int deviceCount=0;
			String deviceIdNew="0";
			int deviceIdealCount=0;
			int deviceNeverUsedCount=0;
			for (Object[] result1 : deviceCountList) {
				deviceIdNew= result1[0].toString();
				List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
				if(idealDeviceList.size() == 1) {
					deviceIdealCount++;
				}
				if(idealDeviceList.size() == 0) {
					deviceNeverUsedCount++;
				}
				deviceCount++;
			}
			jo.put("failureCount", failureCount);
			jo.put("warningCount", warningCount);
			jo.put("deviceCount", deviceCount);
			jo.put("deviceIdealCount", deviceIdealCount);
			jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
			jarray.put(jo);
			return jarray.toString();
		
		}else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}
	
	
	
	@RequestMapping(value = "/api/deviceAnalogSummaryCritical/{userID}//{key}", method = RequestMethod.GET)  //{userID}
	public String deviceAnalogSummaryCritical(@PathVariable long userID,@PathVariable String key) {      // @PathVariable long userID
	//	long userID = 2;
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
		
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			JSONArray blinkArray = new JSONArray();
			
			
			 List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			 String adminFailureminute = "";
			 String adminWarningminute = "";
			 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				 adminFailureminute = DeviceAdminresult[0].toString();
				 adminWarningminute = DeviceAdminresult[1].toString();
			 }
			 int warningCount=0;
			 int failureCount = 0;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			String devicename = "";
			String prid_fk = "";
			String profilename = "";
			String paramName = "";
			String warnMin = "";
			String failMin = "";
			String sign_rule="";
			String paramId="";
			String paramStat="";
			
			Date d1 = null;
		    Date d2 = null;
			Date d  = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceDate = result1[1].toString();
				sysDate = result1[2].toString();
				devicename = result1[3].toString();
				prid_fk = result1[4].toString();
				profilename = result1[5].toString();
				
				JSONObject blinkObj = new JSONObject();
				
				System.out.println("devicename:::"+devicename+" "+"prid_fk:::"+prid_fk+" "+"profilename:::"+profilename);
				
				List<Object[]> analogWarnFail = Dfnservices.paramWarnFailByProfile(devicename);
				for(Object[] rs :analogWarnFail) {
					paramId=rs[0].toString();
					warnMin = rs[2].toString();
					failMin = rs[3].toString();
					paramName = rs[4].toString();
					sign_rule = rs[6].toString();
					
				
				System.out.println("warnMin:::"+warnMin+" "+"failMin:::"+failMin+" "+"paramName:::"+paramName+" "+"sign_rule:::"+sign_rule);
				System.out.println("paramId:::"+paramId+" "+"deviceId:::"+deviceId);
				
				List<Object[]> analoglasttrackParams = Dfnservices.getLasttrackAnalog(Long.parseLong(deviceId),paramId);
				if(analoglasttrackParams.size() == 0) {
					
					blinkObj.put("deviceId", deviceId);
					blinkObj.put("deviceName", devicename);
					blinkObj.put("deviceDate", deviceDate);
					blinkArray.put(blinkObj);
				}else {
					for(Object[] analog :analoglasttrackParams) {
						paramStat=analog[1].toString();
						System.out.println("paramStat:::"+paramStat);
						if(Float.parseFloat(paramStat) > Float.parseFloat(warnMin)  && Float.parseFloat(paramStat) < Float.parseFloat(failMin)) {
							blinkObj.put("Parameter", paramName);
							blinkObj.put("Stat", "WARN");
							blinkObj.put("adminMin", warnMin);
							blinkObj.put("settMin", paramStat);
							blinkObj.put("signRule", sign_rule);
						}else if(Float.parseFloat(paramStat) > Float.parseFloat(failMin)) {
							blinkObj.put("Parameter", paramName);
							blinkObj.put("Stat", "FAIL");
							blinkObj.put("adminMin", failMin);
							blinkObj.put("settMin", paramStat);
							blinkObj.put("signRule", sign_rule);
						}else {
							blinkObj.put("Parameter", paramName);
							blinkObj.put("Stat", "NA");
							blinkObj.put("adminMin", "0");
							blinkObj.put("settMin", "0");
							blinkObj.put("signRule", " ");
						}
						blinkObj.put("deviceId", deviceId);
						blinkObj.put("deviceName", devicename);
						blinkObj.put("deviceDate", deviceDate);
						blinkArray.put(blinkObj);
					}
				}
				
			}
	    }
			jo.put("Data", blinkArray);
			jarray.put(jo);
			return jarray.toString();
		}else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}
	
	
	
	@RequestMapping(value = "/api/deviceDigitalSummaryCritical/{userID}/{key}", method = RequestMethod.GET)  //{userID}
	public String deviceDigitalSummaryCritical(@PathVariable long userID,@PathVariable String key) {      // @PathVariable long userID
	//	long userID = 2;
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray jarray = new JSONArray();
			JSONObject jo = new JSONObject();
			JSONArray blinkArray = new JSONArray();
			
			
			 List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
			 String adminFailureminute = "";
			 String adminWarningminute = "";
			 for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
				 adminFailureminute = DeviceAdminresult[0].toString();
				 adminWarningminute = DeviceAdminresult[1].toString();
			 }
			 int warningCount=0;
			 int failureCount = 0;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        List<Object[]> list = Dfnservices.getDeviceFailureDateDiff();
			String deviceId = "";
			String deviceDate = "";
			String sysDate = "";
			String devicename = "";
			String prid_fk = "";
			String profilename = "";
			
			
			String paramId = "";
			String paramName = "";
			String criticalness = "";
			
			String devNewID="";
			String paramStat="";
			
			Date d1 = null;
		    Date d2 = null;
			Date d  = new Date();
			String strDate = format.format(d);
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceDate = result1[1].toString();
				sysDate = result1[2].toString();
				devicename = result1[3].toString();
				prid_fk = result1[4].toString();
				profilename = result1[5].toString();
				
				System.out.println("deviceId:::"+deviceId+" "+"devicename:::"+devicename+" "+"prid_fk:::"+prid_fk+" "+"profilename:::"+profilename);
				
				List<Object[]> analogWarnFail = Dfnservices.paramDigitalWarnFailByProfile(devicename);

				for(Object[] rs :analogWarnFail) {
					JSONObject blinkObj = new JSONObject();
			    	int val=2;
					paramId = rs[0].toString();
					paramName = rs[1].toString();
					criticalness = rs[2].toString();
				
				System.out.println("paramId:::"+paramId+" "+"criticalness:::"+criticalness+" "+"paramName:::"+paramName);
				if(criticalness.equalsIgnoreCase("ON")) {
					val=1;
				}else if(criticalness.equalsIgnoreCase("OFF")) {
					val=0;
				}else {
					val=3;
				}
				List<Object[]> digilasttrackParams = Dfnservices.getLasttrackDigital(Long.parseLong(deviceId),paramId);
				for(Object[] rs2 : digilasttrackParams) {
					devNewID=rs2[0].toString();
					paramStat=rs2[1].toString();
					
					System.out.println("paramId:::"+paramId+" "+"paramName:::"+paramName);
					
					if(!(Integer.parseInt(paramStat)==val)) {
					   blinkObj.put("Stat", "OFF");
					}else {
					   blinkObj.put("Stat", "ON");
					}
					System.out.println("devNewID:::"+devNewID+" "+"paramStat:::"+paramStat+" "+"val:::"+val);
					
					blinkObj.put("deviceId", deviceId);
					blinkObj.put("paramName", paramName);
					blinkObj.put("deviceName", devicename);
					blinkObj.put("deviceDate", deviceDate);
					blinkArray.put(blinkObj);
				 }
					
			    }
			}
			jo.put("Data", blinkArray);
			jarray.put(jo);
			return jarray.toString();
		
		}else {
			return new SpringException(false, "Invalid Key").toString();
		}
}
	
}