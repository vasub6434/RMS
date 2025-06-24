package com.bonrix.dggenraterset.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.bonrix.dggenraterset.Model.AssignUserGroup;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.AssignSiteUsers;
import com.bonrix.dggenraterset.Model.AssignSiteUsersGroup;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.UserGroup;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.UserGroupService;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class UserGroupController {

	private static final Logger log = Logger.getLogger(SiteController.class);

	@Autowired
	ApiService apiService;

	@Autowired
	UserGroupService UserGroupservices;

	@RequestMapping(value = "/api/saveusergrp/{grpname}/{user_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String saveusergrp(@PathVariable("grpname") String grpname, @PathVariable("user_ids") String user_ids,
			@PathVariable String key, @PathVariable("managerId") Long managerId) throws UnsupportedEncodingException {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			UserGroup ug = new UserGroup();
			ug.setUsergroupname(grpname);
			ug.setManagerid(managerId);
			ug.setIsActive(true);
			UserGroupservices.save(ug);
			Long grpId = ug.getUsergroupid();
			String[] arrOfuserIds = user_ids.split(",", 50);
			for (String nwuser_id : arrOfuserIds) {
				AssignUserGroup assgn = new AssignUserGroup();
				assgn.setManagerid(managerId);
				assgn.setUserid(Long.parseLong(nwuser_id));
				assgn.setUsrgroupid(grpId);
				UserGroupservices.saveassigngrp(assgn);
			}
			return "User  Assigned Sucessfully";
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getUsergrpList/{key}/{managId}", produces = { "application/json" })
	public String getUsergrpList(HttpServletRequest request, @PathVariable String key, @PathVariable Long managId) {

		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = UserGroupservices.getUserGroupList(managId);
			System.out.println("Controller_list:::" + list.size());

			JSONArray jarray = new JSONArray();
			String groupId = "";
			String groupName = "";
			String managerId = "";
			String userCount = "";
			String managerName = "";
			String enabled = "";

			for (Object[] result1 : list) {
				groupId = result1[0].toString();
				groupName = result1[1].toString();
				managerId = result1[2].toString();
				userCount = result1[3].toString();
				// managerName = result1[4].toString();
				enabled = Boolean.toString((boolean) result1[4]);

				JSONObject jo = new JSONObject();
				jo.put("groupId", groupId);
				jo.put("groupName", groupName);
				jo.put("userCount", userCount);
				// jo.put("managerName", managerName);
				jo.put("managerId", managerId);
				jo.put("enabled", enabled);
				jarray.put(jo);
			}
			return jarray.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/del_usergrp/{id}/{key}")
	public String deleteusergrp(@PathVariable Long id, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			UserGroupservices.deleteGrpAssign(id);
			UserGroupservices.deleteGroup(id);
			return new SpringException(true, "User Group  Successfully Deleted!").toString();
		} else
			return new SpringException(false, "Invalid Key").toString();
	}

	@RequestMapping(value = "/api/updateusergrp/{grpname}/{user_ids}/{grp_id}/{managerId}/{key}", method = RequestMethod.GET)
	public String updateusergrp(@PathVariable("grpname") String grpname, @PathVariable("user_ids") String user_ids,
			@PathVariable String key, @PathVariable("grp_id") Long grp_id, @PathVariable("managerId") Long managerId,
			Authentication auth) throws UnsupportedEncodingException {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			UserGroup ug = new UserGroup();
			ug.setUsergroupname(grpname);
			ug.setManagerid(managerId);
			ug.setIsActive(true);
			ug.setUsergroupid(grp_id);
			UserGroupservices.save(ug);
			String stat = UserGroupservices.deleteGrpAssign(grp_id);
			if (stat == "1") {
				String prm = URLDecoder.decode(user_ids, "UTF-8");
				System.out.println("prmvalue::" + user_ids + " " + "prm::" + prm);
				String[] arrOfuserIds = user_ids.split(",", 50);
				for (String nwuser_id : arrOfuserIds) {
					AssignUserGroup assgn = new AssignUserGroup();
					assgn.setManagerid(managerId);
					assgn.setUserid(Long.parseLong(nwuser_id));
					assgn.setUsrgroupid(grp_id);
					UserGroupservices.saveassigngrp(assgn);
				}
				return "UserGroup Updated Sucessfully";
			} else {
				return "Error During Update";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	/*
	 * @RequestMapping(value = "/api/updateusergrp/{grpname}/{grp_id}",method =
	 * RequestMethod.GET ) public String updateusergrp(@PathVariable("grpname")
	 * String grpname,@PathVariable("grp_id") Long grp_id,Authentication auth)
	 * throws UnsupportedEncodingException {
	 * 
	 * System.out.println("saveusergrp Called :::"+grpname); BonrixUser currentUser
	 * = (BonrixUser) auth.getPrincipal();
	 * 
	 * String user_ids = "1,2";
	 * 
	 * Long managerId = currentUser.getUserId();
	 * 
	 * UserGroup ug = new UserGroup(); ug.setUsergroup_name(grpname);
	 * ug.setManager_id(managerId); ug.setIsActive(true);
	 * ug.setUsergroup_id(grp_id);
	 * 
	 * UserGroupservices.save(ug);
	 * 
	 * String stat = UserGroupservices.deleteGrpAssign(grp_id);
	 * 
	 * if(stat =="1") { String prm =URLDecoder.decode(user_ids, "UTF-8");
	 * System.out.println("prmvalue::"+user_ids+" "+"prm::"+prm); String[]
	 * arrOfuserIds = user_ids.split(",", 50); for (String nwuser_id : arrOfuserIds)
	 * { System.out.println("nwuser_id:::"+nwuser_id);
	 * 
	 * AssignUserGroup assgn = new AssignUserGroup();
	 * assgn.setManager_id(managerId); assgn.setUser_id(Long.parseLong(nwuser_id));
	 * assgn.setUsrgroup_id(grp_id);
	 * 
	 * UserGroupservices.saveassigngrp(assgn); }
	 * 
	 * return "UserGroup Updated Sucessfully"; }else { return "Error During Update";
	 * } }
	 */

	@RequestMapping(value = "/api/getAssigneduserListByGrp/{grpId}/{key}", produces = { "application/json" })
	public String getAssigneduserListByGrp(HttpServletRequest request, @PathVariable Long grpId,
			@PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = UserGroupservices.AssignedUsersByGrp(grpId);
			JSONArray jarray = new JSONArray();
			String userId = "";
			String userName = "";
			for (Object[] result1 : list) {
				userId = result1[0].toString();
				userName = result1[1].toString();
				JSONObject jo = new JSONObject();
				jo.put("id", userId);
				jo.put("text", userName);
				jarray.put(jo);
			}
			return jarray.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getAssignedsiteListByGrp/{grpId}/{key}", produces = { "application/json" })
	public String getAssignedsiteListByGrp(HttpServletRequest request, @PathVariable Long grpId,
			@PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = UserGroupservices.GetAssignedSiteByGrp(grpId);

			JSONArray jarray = new JSONArray();
			String siteId = "";
			String siteName = "";

			for (Object[] result1 : list) {
				siteId = result1[0].toString();
				siteName = result1[1].toString();
				JSONObject jo = new JSONObject();
				jo.put("id", siteId);
				jo.put("text", siteName);
				jarray.put(jo);
			}
			return jarray.toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	@RequestMapping(value = "/api/getAssignedsiteListByUserId/{usrId}/{key}", produces = { "application/json" })
	public String getAssignedsiteListByUserId(HttpServletRequest request, @PathVariable Long usrId,
			@PathVariable String key) {   

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = UserGroupservices.GetAssignedSiteByUser(usrId);

			JSONArray jarray = new JSONArray();
			String siteId = "";
			String siteName = "";

			for (Object[] result1 : list) {
				siteId = result1[0].toString();
				siteName = result1[1].toString();
				JSONObject jo = new JSONObject();
				jo.put("id", siteId);
				jo.put("text", siteName);
				jarray.put(jo);
			}
			return jarray.toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

}
