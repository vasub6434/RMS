package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.AssignSiteUsers;
import com.bonrix.dggenraterset.Model.AssignSiteUsersGroup;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DynamicDashBoardLayoutService;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Service.UserGroupService;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class SiteController {

	private static final Logger log = Logger.getLogger(SiteController.class);

	@Autowired
	SiteServices Siteservices;

	@Autowired
	ApiService apiService;

	@Autowired
	UserGroupService UserGroupservices;

	@Autowired
	AssignSiteService userAssignsiteService;
	
	@Autowired
	DynamicDashBoardLayoutService dynamicdashlayout;

	@RequestMapping(value = "/api/savesite/{sitename}/{device_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String asigndevice(@PathVariable("sitename") String sitename, @PathVariable("device_ids") String device_ids,
			@PathVariable String key, @PathVariable("managerId") Long managerId, Authentication auth)
			throws UnsupportedEncodingException {
		
		Apikey api=apiService.findBykeyValue(key);
		if(api!=null){
		if(device_ids.equalsIgnoreCase("0")) {
		Site bs = new Site();
		bs.setSite_name(sitename);
		bs.setManagerid(managerId);
		bs.setIsActive(true);
		Siteservices.save(bs);
		}else {
		Site bs = new Site();
		bs.setSite_name(sitename);
		bs.setManagerid(managerId);
		bs.setIsActive(true);
		Siteservices.save(bs);

		Long siteId = bs.getSiteid();
		System.out.println("siteId:::" + siteId);
		String prm = URLDecoder.decode(device_ids, "UTF-8");
		String[] arrOfdeviceIds = device_ids.split(",", 50);
		for (String nwdevice_id : arrOfdeviceIds) {
		AssignSite assgn = new AssignSite();
		assgn.setDeviceid(Long.parseLong(nwdevice_id));
		assgn.setManagerid(managerId);
		assgn.setSiteid(siteId);
		Siteservices.saveassignsite(assgn);
		}
		}
		return "Device  Assigned Sucessfully";
		}else {
		return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getsiteList/{key}/{managId}", produces = { "application/json" })
	public String getsiteList(HttpServletRequest request, @PathVariable String key, @PathVariable long managId) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = Siteservices.getSiteList(managId);

			JSONArray jarray = new JSONArray();
			String siteId = "";
			String siteName = "";
			String managerId = "";
			String deviceCount = "";
			String enabled = "";
			for (Object[] result1 : list) {
				siteId = result1[0].toString();
				siteName = result1[1].toString();
				managerId = result1[2].toString();
				deviceCount = result1[3].toString();
				enabled = Boolean.toString((boolean) result1[4]);

				JSONObject jo = new JSONObject();
				jo.put("siteId", siteId);
				jo.put("siteName", siteName);
				jo.put("deviceCount", deviceCount);
				jo.put("managerId", managerId);
				jo.put("enabled", enabled);
				jarray.put(jo);
			}
			return jarray.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/del_site/{id}/{key}")
	public String deleteparameter(@PathVariable Long id, @PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			Siteservices.delete(id);
			return new SpringException(true, "Site Successfully Deleted.").toString();
		} else
			return new SpringException(false, "Invalid Key").toString();

	}

	@RequestMapping(value = "/api/updatesite/{sitename}/{site_id}/{device_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String updateusergrp(@PathVariable("sitename") String sitename, @PathVariable("site_id") Long site_id,
			@PathVariable("device_ids") String device_ids, @PathVariable("managerId") Long managerId,
			@PathVariable String key, Authentication auth) throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			Site bs = new Site();
			bs.setSite_name(sitename);
			bs.setManagerid(managerId);
			bs.setIsActive(true);
			bs.setSiteid(site_id);
			Siteservices.save(bs);

			String stat = Siteservices.deletesiteAssign(site_id);
			if (stat == "1") {
				String prm = URLDecoder.decode(device_ids, "UTF-8");
				String[] arrOfdeviceIds = device_ids.split(",", 50);
				for (String nwdevice_id : arrOfdeviceIds) {
					AssignSite assgn = new AssignSite();
					assgn.setDeviceid(Long.parseLong(nwdevice_id));
					assgn.setManagerid(managerId);
					assgn.setSiteid(site_id);
					Siteservices.saveassignsite(assgn);
				}
				return "Site Updated Sucessfully";
			} else {
				return "Error During Update";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getAssignedsiteListBySite/{siteId}/{key}", produces = { "application/json" })
	public String getAssignedsiteListBySite(HttpServletRequest request, @PathVariable Long siteId,
			@PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = Siteservices.AssignedDeviceBySite(siteId);
			JSONArray jarray = new JSONArray();
			String deviceId = "";
			String deviceName = "";
			for (Object[] result1 : list) {
				deviceId = result1[0].toString();
				deviceName = result1[1].toString();
				JSONObject jo = new JSONObject();
				jo.put("id", deviceId);
				jo.put("text", deviceName);
				jarray.put(jo);
			}
			return jarray.toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	@RequestMapping(value = "/api/savesiteusers/{siteId}/{user_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String savesiteusers(@PathVariable("siteId") Long siteId, @PathVariable("user_ids") String user_ids,
			@PathVariable String key, @PathVariable("managerId") Long managerId, Authentication auth)
			throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String stat = Siteservices.deleteUserBySite(siteId);
			if (stat.equalsIgnoreCase("1")) {
				String prm = URLDecoder.decode(user_ids, "UTF-8");
				String[] arrOfuserIds = user_ids.split(",", 50);
				for (String nwuser_id : arrOfuserIds) {
					AssignSiteUsers assgnusr = new AssignSiteUsers();
					assgnusr.setManagerid(managerId);
					assgnusr.setSiteid(siteId);
					assgnusr.setUserid(Long.parseLong(nwuser_id));
					Siteservices.saveassignsiteusers(assgnusr);
				}
				return "Site Assigned Sucessfully";
			} else {
				return "Error During Assign";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/savesiteusergrp/{siteId}/{usergrp_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String savesiteusergrp(@PathVariable("siteId") Long siteId, @PathVariable("usergrp_ids") String usergrp_ids,
			@PathVariable String key, @PathVariable("managerId") Long managerId, Authentication auth)
			throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String stat = Siteservices.deleteUserGrpBySite(siteId);
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
						AssignSiteUsersGroup sitegrp = new AssignSiteUsersGroup();
						sitegrp.setGroupid(Long.parseLong(nwgrp_id));
						sitegrp.setManagerid(managerId);
						sitegrp.setSiteid(siteId);
						sitegrp.setUserid(Long.parseLong(userId));
						Siteservices.saveassignsiteusersgrp(sitegrp);
					}
				}
				return "Site Assigned Sucessfully";
			} else {
				return "Error During Assign";
			}

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/saveuserssite/{userId}/{site_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String saveuserssite(@PathVariable("userId") Long userId, @PathVariable("site_ids") String site_ids,
			@PathVariable String key, @PathVariable("managerId") Long managerId, Authentication auth)
			throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String stat = userAssignsiteService.deleteUserAssignSite(userId);
			if (stat.equalsIgnoreCase("1")) {
				String prm = URLDecoder.decode(site_ids, "UTF-8");
				String[] arrOfsiteIds = site_ids.split(",", 50);
				for (String nwsite_id : arrOfsiteIds) {
					AssignSiteUsers assgnusr = new AssignSiteUsers();
					assgnusr.setManagerid(managerId);
					assgnusr.setSiteid(Long.parseLong(nwsite_id));
					assgnusr.setUserid(userId);
					Siteservices.saveassignsiteusers(assgnusr);
				}
				return "Site Assigned Sucessfully";
			} else {
				return "Error During Update";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/deluserssite/{userId}/{managerId}/{key}", method = RequestMethod.GET)
	public String deluserssite(@PathVariable("userId") Long userId, @PathVariable String key,
			@PathVariable("managerId") Long managerId, Authentication auth) throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String stat = userAssignsiteService.deleteUserAssignSite(userId);
			return new SpringException(true, "Site Assigned Sucessfully").toString();

			//return "Site Assigned Sucessfully";
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}
	

	@RequestMapping(value = "/api/saveusergrpsite/{usergrpId}/{site_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String saveusergrpsite(@PathVariable("usergrpId") Long usergrpId, @PathVariable("site_ids") String site_ids,
			@PathVariable String key, @PathVariable("managerId") Long managerId, Authentication auth)
			throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String[] arrOfsiteIds = site_ids.split(",", 50);
			String stat = userAssignsiteService.deleteUserGrpAssignSite(usergrpId);
			if (stat.equalsIgnoreCase("1")) {
				String userId = "";
				String userName = "";
				List<Object[]> list = new ArrayList<>();
				for (String nwsite_id : arrOfsiteIds) {
					list = UserGroupservices.AssignedUsersByGrp(usergrpId);
					for (Object[] result1 : list) {
						userId = result1[0].toString();
						userName = result1[1].toString();
						AssignSiteUsersGroup sitegrp = new AssignSiteUsersGroup();
						sitegrp.setGroupid(usergrpId);
						sitegrp.setManagerid(managerId);
						sitegrp.setSiteid(Long.parseLong(nwsite_id));
						sitegrp.setUserid(Long.parseLong(userId));
						Siteservices.saveassignsiteusersgrp(sitegrp);
					}
				}
				return "Site Assigned Sucessfully";
			} else {
				return "Error During Assign";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/GetSiteList/{userId}/{key}", produces = { "application/json" })
	public String getSiteList(@PathVariable Long userId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {
		// List<Object[]> mydevice = devicemasterservices.getMyDeviced(userId);
		// List<Devicemaster> device = devicemasterservices.findByuserId(id);

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetSiteByUser(userId);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("siteId", o[0].toString());
				obj.put("siteName", o[1].toString());
				arry.put(obj);
			});
			return arry.toString();

		} else {

			return new SpringException(false, "Invalid Key").toString();
		}

	}

	@RequestMapping(value = "/api/GetUserListBySite/{siteId}/{key}", produces = { "application/json" })
	public String GetUserListBySite(@PathVariable Long siteId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetUserBySite(siteId);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("userId", o[0].toString());
				obj.put("userName", o[1].toString());
				arry.put(obj);
			});
			return arry.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/GetUserGrpListBySite/{siteId}/{key}", produces = { "application/json" })
	public String GetUserGrpListBySite(@PathVariable Long siteId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetUserGrpBySite(siteId);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("userId", o[0].toString());
				obj.put("userName", o[1].toString());
				arry.put(obj);
			});
			return arry.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/GetManagerSiteList/{managerId}/{key}", produces = { "application/json" })
	public String GetManagerSiteList(@PathVariable Long managerId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {
		// List<Object[]> mydevice = devicemasterservices.getMyDeviced(userId);
		// List<Devicemaster> device = devicemasterservices.findByuserId(id);
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetSiteByManager(managerId);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("siteId", o[0].toString());
				obj.put("siteName", o[1].toString());
				arry.put(obj);
			});
			return arry.toString();

		} else {

			return new SpringException(false, "Invalid Key").toString();
		}
	}
	@RequestMapping(value = "/api/getUserSiteList/{userid}/{key}", produces = { "application/json" })
	public String getUserSiteList(@PathVariable Long userid, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetSiteByUser(userid);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("siteId", o[0].toString());
				obj.put("siteName", o[1].toString());
				arry.put(obj);
			});
			return arry.toString();

		} else {

			return new SpringException(false, "Invalid Key").toString();
		}
	}
	
	
	@RequestMapping(value = "/api/getsitename/{ids}", produces = { "application/json" })
	public String getsitename(HttpServletRequest request, @PathVariable String ids) {

		String[] sids=ids.split(",");//splits the string based on whitespace  
		//using java foreach loop to print elements of string array  
		JSONArray jarray = new JSONArray();
		for(String w:sids){ 
		
		//	List<Object[]> list = new ArrayList<>();
		List	list = dynamicdashlayout.getSiteList(Long.valueOf(w));

			
			String siteId = "";
			String siteName = "";
			
			for (int j = 0; j < list.size(); j++) {
			
				siteId = w;
				siteName = ((String) list.get(j));

				JSONObject jo = new JSONObject();
				jo.put("siteId", siteId);
				jo.put("siteName", siteName);
					jarray.put(jo);
			}
		}
		
		return jarray.toString();
	}
	
	
	@RequestMapping(value = "/api/getUserAssignSiteList/{userid}/{key}", produces = { "application/json" })
	public String getUserAssignSiteList(@PathVariable Long userid, @PathVariable String key)throws JsonGenerationException, JsonMappingException,IOException {
		
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray arry = new JSONArray();
			List<Object[]> listt = Siteservices.GetMySiteByUserNew(userid);
			listt.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				obj.put("siteId", o[0].toString());
				obj.put("siteName", o[1].toString());
				obj.put("DeviceCount", o[2].toString());
				arry.put(obj);
			});
			
			List<Object[]> listgrp = Siteservices.GetMySiteByUserGrp(userid);
			listgrp.forEach((Object[] o) -> {
				JSONObject obj = new JSONObject();
				if(arry.toString().contains(o[0].toString()) == false) {
					obj.put("siteId", o[0].toString());
					obj.put("siteName", o[1].toString());
					obj.put("DeviceCount", o[2].toString());
					arry.put(obj);
				}else {
					log.info("Already Added");
				}
			});
			return arry.toString();
		}else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}
	
	
	
	
}
