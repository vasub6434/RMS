package com.bonrix.dggenraterset.Controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.AssignDeviceUserGroup;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.BonrixUser;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DeviceFailureNoticeService;
import com.bonrix.dggenraterset.Service.DeviceProfileServices;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Service.UserGroupService;
import com.bonrix.dggenraterset.Service.UserService;

@CrossOrigin(origins = "*")
@Transactional
@RestController   
public class DeviceInfoController {
	private static final Logger log = Logger.getLogger(DeviceInfoController.class);
	@Autowired
	DeviceProfileServices DeviceProfileservices;

	@Autowired
	DevicemasterServices Deviceinfoservices;

	@Autowired
	ApiService apiService;

	@Autowired
	UserGroupService UserGroupservices;

	@Autowired
	UserService userService;

	@Autowired
	DeviceFailureNoticeService Dfnservices;

	@Autowired
	SiteServices Siteservices;

	@RequestMapping(method = RequestMethod.POST, value = "/api/savedeviceinfo/{pId}")
	@ExceptionHandler(SpringException.class)
	public String savedeviceinfo(@RequestBody Devicemaster bs, @PathVariable("pId") Long pId, Authentication auth) {
		BonrixUser currentUser = (BonrixUser) auth.getPrincipal();
		Set<UserRole> liss = currentUser.getUserRole();
		System.out.println(liss);

		if (currentUser.getUserRole().stream().anyMatch(u -> u.getRole().equalsIgnoreCase("ROLE_ADMIN"))) {
			bs.setDp(DeviceProfileservices.get(pId));
			bs.setUserId(currentUser.getUserId());
			bs.setFlagcondition(true);
			Deviceinfoservices.save(bs);
			log.info("DeviceInfo Sucessfully Added");
			return new SpringException(true, "DeviceInfo Sucessfully Added").toString();
		} else {
			return new SpringException(false, "you have no access to add device").toString();
		}
	}

	/*
	 * @RequestMapping(method = RequestMethod.POST, value = "/api/savedevicemaster")
	 * 
	 * @ExceptionHandler(SpringException.class) public String savedata(@RequestBody
	 * Devicemaster user,Authentication auth) {
	 * 
	 * BonrixUser currentUser = (BonrixUser) auth.getPrincipal();
	 * 
	 * System.out.println("getDevicedescription::--->>"+user.getDevicedescription()
	 * +" "+"getDevicemodel::"+user.getDevicemodel());
	 * System.out.println("getDevicename::--->>"+user.getDevicename()+" "
	 * +"getImei::"+user.getImei());
	 * System.out.println("getSimcardno::--->>"+user.getSimcardno()+" "
	 * +"getDeviceid::"+user.getDeviceid());
	 * System.out.println("getSimcardno::--->>"+user.getManagerId()+" "
	 * +"getDeviceid::"+user.getUserId());
	 * 
	 * user.setFlagcondition(true);
	 * 
	 * user=Deviceinfoservices.savedevicemaster(user);
	 * 
	 * return new SpringException(true,
	 * "Device Master Sucessfully Added ").toString(); }
	 */

	@RequestMapping(value = "/api/gerDeviceListBymanager/{managetId}/{userId}/{key}", method = RequestMethod.GET)
	public String managetDeviceList(@PathVariable long managetId, @PathVariable long userId, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			return Deviceinfoservices.managetDeviceList(managetId, userId);
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	/*
	 * @RequestMapping(value = "/api/deviceinfolist", method = RequestMethod.GET)
	 * public String getdeviceinfolist() { return Deviceinfoservices.getlist(); }
	 */

	@RequestMapping(value = "/api/deviceinfolist/{key}", method = RequestMethod.GET)
	public String getdeviceinfolist(@PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			return Deviceinfoservices.getlist();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	@RequestMapping(value = "/api/deviceinfolist/{key}/{id}", method = RequestMethod.GET)
	public String getdeviceinfolist(@PathVariable String key, @PathVariable long id) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			return Deviceinfoservices.getlist();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	/*
	 * @RequestMapping(value = "/api/deviceinfolist/{id}", method =
	 * RequestMethod.GET) public Devicemaster getdeviceinfolist(@PathVariable long
	 * id) { return Deviceinfoservices.get(id); }
	 */

	@RequestMapping(method = RequestMethod.PUT, value = "/api/updatedeviceinfo/{id}")
	public String updatedeviceinfo(@RequestBody Devicemaster bs, @PathVariable long id) {
		bs.setDeviceid(id);
		return Deviceinfoservices.update(bs);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/deldeviceinfo/{id}")
	public String deleteparameter(@PathVariable Long id) {
		return Deviceinfoservices.delete(id);
	}

	@RequestMapping(value = "/api/deviceinfo/{deviceid}", produces = { "application/json" }, method = RequestMethod.GET)
	public List<Devicemaster> getlistBydeviceinfo_id(@PathVariable int deviceid) {
		return Deviceinfoservices.getlistBydeviceinfo_id(deviceid);
	}

	@RequestMapping(value = "api/devicetree", method = RequestMethod.GET)
	public String getdevicetree() {
		return Deviceinfoservices.getlist();
	}

	@RequestMapping(value = "/api/getDevicemasterList/{key}", produces = { "application/json" })
	public String getDevicemasterList(HttpServletRequest request, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {

			List<Object[]> list = new ArrayList<>();
			list = Deviceinfoservices.getDeviceMasterlist();
			System.out.println("Controller_list:::" + list.size());

			JSONArray jarray = new JSONArray();

			String id = "";
			String devicename = "";
			String devicedesc = "";
			String devicemodel = "";
			String deviceimei = "";
			String simnum = "";
			String enabled = "";
			String userid = "";
			String prid = "";
			String managerid = "";
			for (Object[] result1 : list) {
				id = result1[0].toString();
				devicename = result1[3].toString();
				devicedesc = result1[1].toString();
				devicemodel = result1[2].toString();
				deviceimei = result1[5].toString();
				simnum = result1[6].toString();
				enabled = result1[4].toString();
				userid = result1[7].toString();
				prid = result1[8].toString();
				managerid = result1[10].toString();

				JSONObject jo = new JSONObject();
				jo.put("id", id);
				jo.put("devicename", devicename);
				jo.put("devicedesc", devicedesc);
				jo.put("devicemodel", devicemodel);
				jo.put("deviceimei", deviceimei);
				jo.put("simnum", simnum);
				jo.put("enabled", enabled);
				jo.put("userid", userid);
				jo.put("prid", prid);
				jo.put("managerid", managerid);

				jarray.put(jo);
			}
			return jarray.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getdevicebymanager", produces = { "application/json" })
	public String getdevicebymanager(HttpServletRequest request) {

		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getDeviceMasterDataByManagerId();
		System.out.println("Controller_list:::" + list.size());

		JSONArray jarray = new JSONArray();
		String id = "";
		String desc = "";
		String model = "";
		String name = "";
		for (Object[] result1 : list) {
			id = result1[0].toString();
			desc = result1[1].toString();
			name = result1[3].toString();
			model = result1[2].toString();

			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("desc", desc);
			jo.put("model", model);
			jo.put("name", name);

			jarray.put(jo);
		}
		return jarray.toString();
	}

	/*
	 * @RequestMapping(value = "/api/asigndevice/{deviceId}/{managerId}",method =
	 * RequestMethod.GET ) public String asigndevice(@PathVariable("managerId") long
	 * managerId,@PathVariable("deviceId") long deviceId) {
	 * 
	 * System.out.println("AssignDevice Called :::"+deviceId);
	 * Deviceinfoservices.updatedevicemaster(managerId, deviceId); // return new
	 * SpringException(true, "Device  Assigned Sucessfully").toString();
	 * 
	 * return "Device  Assigned Sucessfully"; }
	 */

	@RequestMapping(value = "/api/asigndevice/{managerId}", method = RequestMethod.GET)
	public String asigndevice(@PathVariable("managerId") long managerId) {

		String deviceId = "10,186254,186255";

		// String line = "1,2,3,1,2,2,1,2,3,";
		String[] parts = deviceId.split(",");
		int[] ints = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			ints[i] = Integer.parseInt(parts[i]);
		}

		System.out.println("ints :::" + ints + " " + "Array::" + Arrays.toString(ints));

		// String[] numbers = Arrays.toString(ints);

		System.out.println("AssignDevice Called :::" + deviceId);
		Deviceinfoservices.updatedevicemaster(managerId, Arrays.toString(ints));
		// return new SpringException(true, "Device Assigned Sucessfully").toString();

		return "Device  Assigned Sucessfully";
	}

	@RequestMapping(value = "/api/savedeviceusergrp/{devieId}/{usergrp_ids}/{managerId}", method = RequestMethod.GET)
	public String savedeviceusergrp(@PathVariable("devieId") Long devieId,
			@PathVariable("usergrp_ids") String usergrp_ids, @PathVariable("managerId") Long managerId,
			Authentication auth) throws UnsupportedEncodingException {

		String stat = Deviceinfoservices.deleteUserGrpByDevice(devieId);
		if (stat.equalsIgnoreCase("1")) {
			String[] arrOfgrpIds = usergrp_ids.split(",", 50);
			ArrayList<Integer> arr = new ArrayList<Integer>(4);
			String userId = "";
			String userName = "";
			List<Object[]> list = new ArrayList<>();
			for (String nwgrp_id : arrOfgrpIds) {
				list = UserGroupservices.AssignedUsersByGrp(Long.parseLong(nwgrp_id));
				for (Object[] result1 : list) {
					userId = result1[0].toString();
					userName = result1[1].toString();
					AssignDeviceUserGroup devicegrp = new AssignDeviceUserGroup();
					devicegrp.setDeviceid(devieId);
					devicegrp.setGroupid(Long.parseLong(nwgrp_id));
					devicegrp.setManagerid(managerId);
					devicegrp.setUserid(Long.parseLong(userId));
					Deviceinfoservices.saveDeviceUserGrp(devicegrp);
				}
			}
			return "Site Assigned Sucessfully";
		} else {
			return "Error During Assign";
		}

	}

	@RequestMapping(value = "/api/assigndeviceuser/{deviceId}/{user_ids}/{managerId}", method = RequestMethod.GET)
	public String assigndeviceuser(@PathVariable("deviceId") String deviceId, @PathVariable("user_ids") String user_ids,
			@PathVariable("managerId") long managerId, Authentication auth) throws UnsupportedEncodingException {
        
		String[] arrOfUserIds = user_ids.split(",", 50);
		Deviceinfoservices.deleteassignuserdevice(Long.parseLong(deviceId), managerId);
		if(!user_ids.equalsIgnoreCase("0")) {
		for (String nwuser_id : arrOfUserIds) {
			AssignUserDevice assgn = new AssignUserDevice();
			assgn.setDevice_id(Long.parseLong(deviceId));
			assgn.setManager_id(managerId);
			assgn.setUser_id(Long.parseLong(nwuser_id));
			userService.saveassignuserdevice(assgn);
		}
		return "Device  Assigned Sucessfully";
		}else {
			
			
			return "Device Not Assigned";
			
		}
		
		}

	@RequestMapping(value = "/api/GetUserListByDevie/{deviceId}", produces = { "application/json" })
	public String GetUserListBySite(@PathVariable Long deviceId) {

		JSONArray arry = new JSONArray();
		List<Object[]> listt = Deviceinfoservices.GetUserByDevice(deviceId);
		listt.forEach((Object[] o) -> {
			JSONObject obj = new JSONObject();
			obj.put("userId", o[0].toString());
			obj.put("userName", o[1].toString());
			arry.put(obj);
		});
		return arry.toString();
	}

	@RequestMapping(value = "/api/getdeviceInfoByManager/{managerId}/{key}", produces = { "application/json" })
	public String getdeviceInfoByManager(@PathVariable String managerId, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = Deviceinfoservices.getDeviceByManagerId(Long.parseLong(managerId));
			JSONArray jarray = new JSONArray();
			String id = "";
			String desc = "";
			String model = "";
			String name = "";
			String IMEI = "";
			String SimNO = "";
			for (Object[] result1 : list) {
				id = result1[0].toString();
				desc = result1[1].toString();
				name = result1[3].toString();
				model = result1[2].toString();
				IMEI = result1[5].toString();
				SimNO = result1[6].toString();

				JSONObject jo = new JSONObject();
				jo.put("id", id);
				jo.put("desc", desc);
				jo.put("model", model);
				jo.put("name", name);
				jo.put("IMEI", IMEI);
				jo.put("SimNO", SimNO);
				
				List assignuserdevice = Deviceinfoservices.findByDevice_idmang(Long.parseLong(result1[0].toString()),Long.parseLong(managerId));
					if (assignuserdevice.size() != 0) {
						jo.put("allocationstatus",true);

					}else  {
						jo.put("allocationstatus",false);

					}

				jarray.put(jo);
			}
			return jarray.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}



	@RequestMapping(value = "/api/getSiteViseDeviceInfo/{siteId}", produces = { "application/json" })
	public String getSiteViseDeviceInfo(HttpServletRequest request, @PathVariable long siteId) {

		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getDeviceBySite(siteId);

		JSONObject mainJson = new JSONObject();
		JSONArray DataArray = new JSONArray();
		JSONArray ColumnsArray = new JSONArray();

		JSONArray Auto_ColumnsArray = new JSONArray();
		Auto_ColumnsArray.put("#");
		JSONArray DeviceName_ColumnsArray = new JSONArray();
		DeviceName_ColumnsArray.put("Device Name");
		JSONArray DeviceModel_ColumnsArray = new JSONArray();
		DeviceModel_ColumnsArray.put("Device Model");
		JSONArray SimNum_ColumnsArray = new JSONArray();
		SimNum_ColumnsArray.put("SimCard Num");
		JSONArray DeviceIMEI_ColumnsArray = new JSONArray();
		DeviceIMEI_ColumnsArray.put("IMEI");
		JSONArray DeviceAction_ColumnsArray = new JSONArray();
		DeviceAction_ColumnsArray.put("Action");

		ColumnsArray.put(Auto_ColumnsArray);
		ColumnsArray.put(DeviceName_ColumnsArray);
		ColumnsArray.put(DeviceModel_ColumnsArray);
		ColumnsArray.put(SimNum_ColumnsArray);
		ColumnsArray.put(DeviceIMEI_ColumnsArray);
		ColumnsArray.put(DeviceAction_ColumnsArray);

		int auto_num = 1;

		for (Object[] deviceListresult : list) {
			JSONArray Inner_DataArray = new JSONArray();

			Inner_DataArray.put(auto_num);
			Inner_DataArray.put(deviceListresult[3].toString());
			Inner_DataArray.put(deviceListresult[4].toString());
			Inner_DataArray.put(deviceListresult[5].toString());
			Inner_DataArray.put(deviceListresult[6].toString());
			Inner_DataArray.put(
					"<button type=button class='btn btn-success btn-sm' id='assign-userbtn' data-toggle='modal' data-target='#assignuserModel' onclick=devicehistory(\""
							+ deviceListresult[3].toString() + "\",\"" + deviceListresult[0].toString()
							+ "\")>Assign User</button>");
			DataArray.put(Inner_DataArray);
			auto_num++;
		}

		mainJson.put("data", DataArray);
		mainJson.put("columns", ColumnsArray);
		System.out.println("Main_Json::" + mainJson.toString());
		return mainJson.toString();
	}

	@RequestMapping(value = "/api/getUserSite/{userId}", produces = { "application/json" })
	public String getUserSite(HttpServletRequest request, @PathVariable String userId) {
		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getSiteForUser(Long.parseLong(userId));
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			JSONObject jo = new JSONObject();
			jo.put("managerId", result1[0].toString());
			jo.put("siteId", result1[1].toString());
			jo.put("userId", result1[2].toString());
			jo.put("siteName", result1[3].toString());
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getdeviceInfoUser/{userId}", produces = { "application/json" })
	public String getdeviceInfoUser(HttpServletRequest request, @PathVariable String userId) {
		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getDeviceForUser(Long.parseLong(userId));
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			JSONObject jo = new JSONObject();
			jo.put("id", result1[0].toString());
			jo.put("deviceId", result1[1].toString());
			jo.put("managerId", result1[2].toString());
			jo.put("userId", result1[3].toString());
			jo.put("deviceName", result1[4].toString());
			jo.put("deviceModel", result1[5].toString());
			jo.put("IMEI", result1[6].toString());
			jo.put("simcardNo", result1[7].toString());
			jarray.put(jo);
		}
		log.info("SAJAN :: "+jarray.toString());
		return jarray.toString();
	}


	@RequestMapping(value = "/api/getdeviceAssignUser/{userId}", produces = { "application/json" })
	public String getdeviceAssignUser(HttpServletRequest request, @PathVariable String userId) {
		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getDeviceAssignForUser(Long.parseLong(userId));
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			
			JSONObject jo = new JSONObject();
			jo.put("id", result1[0].toString());
			jo.put("deviceId", result1[1].toString());
			jo.put("managerId", result1[2].toString());
			jo.put("userId", result1[3].toString());
			jo.put("deviceName", result1[4].toString());
			jo.put("deviceModel", result1[5].toString());
			jo.put("IMEI", result1[6].toString());
			jo.put("simcardNo", result1[7].toString());
			System.out.println("dsdfgdgf"+result1[8].toString());
			
			jo.put("altdevicename", result1[8].toString().equalsIgnoreCase(null)? "NotSet":result1[8].toString());
			
		/*	if(result1[8].toString().equalsIgnoreCase("null")) {
				
				jo.put("altdevicename", "Empty");
				
			}else {
				jo.put("altdevicename", result1[8].toString());
			}
			*/
			jarray.put(jo);
		}
		log.info("SAJAN :: "+jarray.toString());
		return jarray.toString();
	}
	
	
	
	
	
	/*
	 * @RequestMapping(value =
	 * "/api/assigndeviceuser/{deviceId}/{user_ids}/{managerId}",method =
	 * RequestMethod.GET ) public String assigndeviceuser(@PathVariable("deviceId")
	 * String deviceId,@PathVariable("user_ids") String user_ids
	 * ,@PathVariable("managerId") long managerId,Authentication auth) throws
	 * UnsupportedEncodingException {
	 * 
	 * String userStat =
	 * Deviceinfoservices.deleteUserByDevice(Long.parseLong(deviceId));
	 * if(userStat.equalsIgnoreCase("1")) { String[] arrOfUserIds =
	 * user_ids.split(",", 50); for (String nwuser_id : arrOfUserIds) {
	 * AssignUserDevice assgn = new AssignUserDevice();
	 * assgn.setDevice_id(Long.parseLong(deviceId)); assgn.setManager_id(managerId);
	 * assgn.setUser_id(Long.parseLong(nwuser_id));
	 * userService.saveassignuserdevice(assgn); } return
	 * "Device  Assigned Sucessfully"; }else { return "Error During Assign"; }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/api/GetUserListByDevie/{deviceId}", produces = {
	 * "application/json" }) public String GetUserListBySite(@PathVariable Long
	 * deviceId) throws JsonGenerationException, JsonMappingException, IOException {
	 * 
	 * JSONArray arry=new JSONArray(); List<Object[]> listt=
	 * Deviceinfoservices.GetUserByDevice(deviceId); listt.forEach((Object[] o)->{
	 * JSONObject obj=new JSONObject(); obj.put("userId", o[0].toString());
	 * obj.put("userName", o[1].toString()); arry.put(obj); }); return
	 * arry.toString(); }
	 */

	@RequestMapping(value = "/api/getManagerSite/{managerId}", produces = { "application/json" })
	public String getManagerSite(HttpServletRequest request, @PathVariable String managerId) {
		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getSiteForManager(Long.parseLong(managerId));
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			JSONObject jo = new JSONObject();
			jo.put("siteId", result1[0].toString());
			jo.put("siteName", result1[1].toString());
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getDeviceBySite/{siteId}", produces = { "application/json" })
	public String getDeviceBySite(HttpServletRequest request, @PathVariable String siteId) {
		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getDeviceBySite(Long.parseLong(siteId));
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			JSONObject jo = new JSONObject();
			jo.put("deviceId", result1[0].toString());
			jo.put("managerId", result1[1].toString());
			jo.put("siteId", result1[2].toString());
			jo.put("deviceName", result1[3].toString());
			jo.put("deviceModel", result1[4].toString());
			jo.put("simCardNo", result1[5].toString());
			jo.put("IMEI", result1[6].toString());
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/siteViseDeviceSummary/{siteId}", method = RequestMethod.GET)
	public String siteViseDeviceSummary(@PathVariable Long siteId) {
		long userID = 2;
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Object[]> deviceFailureAdminlist = Dfnservices.getAdminDeviceFailureData(userID);
		String adminFailureminute = "";
		String adminWarningminute = "";
		for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
			adminFailureminute = DeviceAdminresult[0].toString();
			adminWarningminute = DeviceAdminresult[1].toString();
		}
		List<Object[]> deviceCountList = Siteservices.GetDeviceIdBySite(siteId);
		int deviceCount = 0;
		String deviceIdNew = "0";
		int deviceIdealCount = 0;
		int deviceNeverUsedCount = 0;
		int warningCount = 0;
		int failureCount = 0;
		String deviceId = "";
		String deviceDate = "";
		String sysDate = "";
		Date d1 = null;
		Date d2 = null;
		Date d = new Date();
		String strDate = format.format(d);
		int finalMinutes = 0;
		for (Object[] result1 : deviceCountList) {
			deviceIdNew = result1[0].toString();
			List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
			for (Object[] DeviceDataresult : idealDeviceList) {
				deviceDate = DeviceDataresult[1].toString();
			}
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
			if (finalMinutes > Integer.parseInt(adminWarningminute)
					&& finalMinutes < Integer.parseInt(adminFailureminute)) {
				warningCount++;
			} else if (finalMinutes > Integer.parseInt(adminFailureminute)) {
				failureCount++;
			} else {
				failureCount = 0;
				warningCount = 0;
			}
			if (idealDeviceList.size() == 1) {
				deviceIdealCount++;
			}
			if (idealDeviceList.size() == 0) {
				deviceNeverUsedCount++;
			}
			deviceCount++;
		}
		System.out.println("failureCount:::" + failureCount + " " + "warningCount:::" + warningCount + " "
				+ "deviceCount:::" + deviceCount + " " + "deviceIdealCount:::" + deviceIdealCount + " "
				+ "deviceNeverUsedCount:::" + deviceNeverUsedCount);
		jo.put("failureCount", failureCount);
		jo.put("warningCount", warningCount);
		jo.put("deviceCount", deviceCount);
		jo.put("deviceIdealCount", deviceIdealCount);
		jo.put("deviceNeverUsedCount", deviceNeverUsedCount);
		jarray.put(jo);
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getAdminSite", produces = { "application/json" })
	public String getAdminSite(HttpServletRequest request) {
		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getAdminSite();
		JSONArray jarray = new JSONArray();
		for (Object[] result1 : list) {
			JSONObject jo = new JSONObject();
			jo.put("siteId", result1[0].toString());
			jo.put("siteName", result1[1].toString());
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/managerDeviceSummary/{managerid}", method = RequestMethod.GET)
	public String managerDeviceSummary(@PathVariable("managerid") long managerid) throws java.text.ParseException {
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();

		List<Object[]> deviceFailureAdminlist = Dfnservices.getManagerDeviceFailureData(managerid);
		String managerFailureminute = "";
		String managerWarningminute = "";
		for (Object[] DeviceAdminresult : deviceFailureAdminlist) {
			managerFailureminute = DeviceAdminresult[0].toString();
			managerWarningminute = DeviceAdminresult[1].toString();

		}
		int warningCount = 0;
		int failureCount = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Object[]> list = Dfnservices.getmanagerDeviceFailureDateDiff(managerid);
		String deviceId = "";
		String deviceDate = "";
		String sysDate = "";
		Date d1 = null;
		Date d2 = null;
		Date d = new Date();
		String strDate = format.format(d);
		for (Object[] result1 : list) {
			deviceDate = result1[0].toString();
			sysDate = result1[1].toString();
			deviceId = result1[2].toString();

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
			if (finalMinutes > Integer.parseInt(managerWarningminute)
					&& finalMinutes < Integer.parseInt(managerFailureminute)) {
				warningCount++;
			} else if (finalMinutes > Integer.parseInt(managerFailureminute)) {
				failureCount++;
			} else {
				failureCount = 0;
				warningCount = 0;
			}
		}
		List<Object[]> deviceCountList = Dfnservices.getTotalManagerDeviceCount(managerid);
		int deviceCount = 0;
		String deviceIdNew = "0";
		int deviceIdealCount = 0;
		int deviceNeverUsedCount = 0;
		for (Object[] result1 : deviceCountList) {
			deviceIdNew = result1[0].toString();
			List<Object[]> idealDeviceList = Dfnservices.getDeviceDataById(Long.parseLong(deviceIdNew));
			if (idealDeviceList.size() == 1) {
				deviceIdealCount++;
			}
			if (idealDeviceList.size() == 0) {
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
	
	@RequestMapping(value = "/api/updatedevicename/{deviceid}/{devicename}/{altdevicename}", method = RequestMethod.GET)
	public String updatedevicename(@PathVariable("deviceid") long deviceid,@PathVariable String devicename,@PathVariable String altdevicename) {

	Deviceinfoservices.updateDevicename(deviceid,devicename,altdevicename);

	 return new SpringException(true, "DeviceName Sucessfully Updated!").toString();
	}

}
