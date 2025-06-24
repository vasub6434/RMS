package com.bonrix.dggenraterset.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.AssignManagerLayout;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.DashBoardLayout;
import com.bonrix.dggenraterset.Model.DeviceFailureNotice;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.DashBoardLayoutServices;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DynamicDashBoardLayoutService;
import com.bonrix.dggenraterset.Service.SiteServices;
import com.bonrix.dggenraterset.Service.UserService;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class DashBoardLayoutController {

	@Autowired
	public DynamicDashBoardLayoutService dynamicdashlayout;
	
	@Autowired
	DevicemasterServices Deviceinfoservices;

	@Autowired
	public DashBoardLayoutServices dLServices;

	@Autowired
	public SiteServices siteServices;

	@Autowired
	public UserService userServices;

	@RequestMapping(value = "/api/saveDashBoardLayout/{name}/{htmlname}/{view}/{viewtype}/{profilename}", method = RequestMethod.GET)
	public String saveDashBoardLayout(@PathVariable("name") String name, @PathVariable("htmlname") String htmlname,
			@PathVariable("view") String view, @PathVariable("viewtype") String viewtype,
			@PathVariable("profilename") String profilename) throws UnsupportedEncodingException {
	//	System.out.println("getprofilerList:::"+profilename);
		if (profilename.equalsIgnoreCase("NA")) {
			DashBoardLayout assgn = new DashBoardLayout();
			assgn.setProfile("NA");
			assgn.setName(name);
			assgn.setHtmlName(htmlname);
			assgn.setView(view);
			assgn.setViewType(viewtype);
			dLServices.saveDAshBoardLayout(assgn);

		} else {
				DashBoardLayout assgn = new DashBoardLayout();
				assgn.setName(name);
				assgn.setHtmlName(htmlname);
				assgn.setView(view);
				assgn.setViewType(viewtype);
				assgn.setProfile(profilename);
				System.out.println(profilename);
				dLServices.saveDAshBoardLayout(assgn);
			}
			return new SpringException(true, "Sucessfully Inserted").toString();
		
	}

	@RequestMapping(value = "/api/getDashBoardLayoutList", produces = { "application/json" })
	public String getDashBoardLayoutList(HttpServletRequest request) {
		List<Object[]> list = new ArrayList<>();
		list = dLServices.getAllDashBoardLayout();
		JSONArray jarray = new JSONArray();

		String dlid = "";
		String view = "";
		String viewtype = "";
		String html_name = "";
		String name = "";
		String profile = "";
		for (Object[] result1 : list) {
			dlid = result1[0].toString();
			view = result1[1].toString();
			viewtype = result1[2].toString();
			html_name = result1[3].toString();
			name = result1[4].toString();
			profile = result1[5].toString();

			JSONObject jo = new JSONObject();
			String[] profileid = profile.split(",", 50);
			StringBuilder profile1 = new StringBuilder();
			if (profile != null) {
				for (String ids : profileid) {
					if(ids.equalsIgnoreCase("NA")) {
						jo.put("profile", ids);
					}else {
						List list2 = dLServices.getprofilename(Long.parseLong(ids));
						for (int j = 0; j < list2.size(); j++) {
							profile1.append(((String) list2.get(j)));
							profile1.append(",");
						}
					//	profile1.deleteCharAt(profile1.lastIndexOf(","));
						jo.put("profile", profile1.toString());
					
				}
				
				//System.out.println(profile1.toString());
				}
			}
			jo.put("dlid", dlid);
			jo.put("view", view);
			jo.put("viewtype", viewtype);
			jo.put("html_name", html_name);
			jo.put("name", name);
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/deleteDashBoardLayoutById/{id}", method = RequestMethod.GET)
	public String deleteDashBoardLayoutById(@PathVariable long id) {
		dLServices.deleteAssManager(id);
		dLServices.deletedashboard(id);
		return new SpringException(true, " sucessfully Deleted").toString();
	}

	@RequestMapping(value = "/api/editDashBoardLayoutById/{id}/{name}/{htmlname}/{view}/{viewtype}/{profile}", method = RequestMethod.GET)
	public String editDashBoardLayoutById(@PathVariable long id, @PathVariable String name,
			@PathVariable String htmlname, @PathVariable String view, @PathVariable String viewtype,
			@PathVariable String profile) throws UnsupportedEncodingException {
//		dLServices.delete(id);
		
			DashBoardLayout assgn = new DashBoardLayout();
			assgn.setDlid(id);
			assgn.setName(name);
			assgn.setHtmlName(htmlname);
			assgn.setView(view);
			assgn.setViewType(viewtype);
			assgn.setProfile(profile);
			dLServices.update(assgn);
		

		return new SpringException(true, "Sucessfully Inserted").toString();

	}

	@RequestMapping(value = "/api/getmanagerList", produces = { "application/json" })
	public String getmanagerList(HttpServletRequest request) {

		List<Object[]> list = new ArrayList<>();
		list = userServices.getAllmangerlist();
		JSONArray jarray = new JSONArray();

		String id = "";
		String username = "";
		

		for (Object[] result1 : list) {
			id = result1[0].toString();
			username = result1[1].toString();
			

			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("username", username);
			
			jarray.put(jo);
		}
		return jarray.toString();

	}

	@RequestMapping(value = "/api/getprofilerList", produces = { "application/json" })
	public String getprofilerList(HttpServletRequest request) {

		List<Object[]> list = new ArrayList<>();
		list = dLServices.getProfilelist();
		JSONArray jarray = new JSONArray();

		String id = "";
		String profile = "";

		for (Object[] result1 : list) {
			id = result1[0].toString();
			profile = result1[1].toString();

			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("profile", profile);
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/savemanagerDashBoardLayout/{dlid}/{managerids}", method = RequestMethod.GET)
	public String savemanagerDashBoardLayout(@PathVariable("dlid") Long dlid,
			@PathVariable("managerids") String managerids) throws UnsupportedEncodingException {
		String prm = URLDecoder.decode(managerids, "UTF-8");
		String[] arrOfdeviceIds = managerids.split(",", 50);
		for (String nwdmanager_id : arrOfdeviceIds) {
			AssignManagerLayout assgn = new AssignManagerLayout();
			assgn.setDlid(dlid);
			assgn.setManagerid(Long.parseLong(nwdmanager_id));
			dLServices.saveManagerDashBoardLayout(assgn);
		}
		return "Manager  Assigned Sucessfully";
	}

	@RequestMapping(value = "/api/getassignprofileByid/{grpId}", produces = { "application/json" })
	public String getassignprofileByid(HttpServletRequest request, @PathVariable Long grpId) {
		List<Object[]> list = new ArrayList<>();

		list = dLServices.getprofileByid(grpId);
		JSONArray jarray = new JSONArray();
		String prid = "";
		String prname = "";
		for (Object[] result1 : list) {
			prid = result1[0].toString();
			prname = result1[1].toString();
			JSONObject jo = new JSONObject();
			jo.put("prid", prid);
			jo.put("prname", prname);
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getlayoutListByManager/{managerId}", produces = { "application/json" })
	public String getlayoutListByManager(HttpServletRequest request, @PathVariable("managerId") long managerId) {

		List<Object[]> list = new ArrayList<>();
		list = dLServices.getlayoutListByManager(managerId);
		JSONArray jarray = new JSONArray();

		String id = "";
		String name = "";
		String view = "";
		String viewtype = "";
		String profile="";
		String htmlname="";

		for (Object[] result1 : list) {
			id = result1[0].toString();
			name = result1[1].toString();
			view = result1[2].toString();
			viewtype = result1[3].toString();
			profile = result1[4].toString();
			htmlname = result1[5].toString();
			
			JSONObject jo = new JSONObject();
			String[] profileid = profile.split(",", 50);
			StringBuilder profile1 = new StringBuilder();
			if (profile != null) {
				for (String ids : profileid) {
					if(ids.equalsIgnoreCase("NA")) {
						jo.put("profile1", "Profile Not Selected");
					}else {
						List list2 = dLServices.getprofilename(Long.parseLong(ids));
						for (int j = 0; j < list2.size(); j++) {
							profile1.append(((String) list2.get(j)));
							profile1.append(",");
							 
							  //devicename1 += ((String) list2.get(j));
						}
					//	System.out.println("xcgxc "+yoyo.toString());
						profile1.deleteCharAt(profile1.lastIndexOf(","));
						jo.put("profile1", profile1.toString());
					
				}
				
				//System.out.println(profile1.toString());
				}
			}
			jo.put("id", id);
			jo.put("name", name);
			jo.put("view", view);
			jo.put("viewtype", viewtype);
			jo.put("htmlname", htmlname);
			
			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/getsiteListmanager/{managId}", produces = { "application/json" })
	public String getsiteListmanager(HttpServletRequest request, @PathVariable long managId) {

		List<Object[]> list = new ArrayList<>();
		list = siteServices.getSiteList(managId);

		JSONArray jarray = new JSONArray();
		String siteId = "";
		String siteName = "";
		
		for (Object[] result1 : list) {
			siteId = result1[0].toString();
			siteName = result1[1].toString();
			
			JSONObject jo = new JSONObject();
			jo.put("siteId", siteId);
			jo.put("siteName", siteName);
			
			jarray.put(jo);
		}
		return jarray.toString();

	}

	@RequestMapping(value = "/api/getDeviceList/{managetId}", method = RequestMethod.GET)
	public String getDeviceList(@PathVariable long managetId) {

		return dLServices.getDeviceList(managetId);
	}

	@RequestMapping(value = "/api/getdevicelistByManager/{managerId}", produces = { "application/json" })
	public String getdevicelistByManager(@PathVariable Long managerId) {

		List<Object[]> list = new ArrayList<>();
		list = Deviceinfoservices.getDeviceByManagerId(managerId);
		JSONArray jarray = new JSONArray();
		String id = "";
		String name = "";
		
		for (Object[] result1 : list) {
			id = result1[0].toString();
			name = result1[3].toString();
			

			JSONObject jo = new JSONObject();
			jo.put("id", id);	
			jo.put("name", name);
			jarray.put(jo);
		}
		return jarray.toString();
	}
	
	
	@RequestMapping(value = "/api/getAssigneddashboradUser/{mamagerdashid}", produces = { "application/json" })
	public String getAssigneddashboradUser(HttpServletRequest request, @PathVariable Long mamagerdashid) {
	List<Object[]> list = new ArrayList<>();
	list = dLServices.getassUser(mamagerdashid);
	JSONArray jarray = new JSONArray();
	String id = "";
	String userName = "";
	for (Object[] result1 : list) {
	id = result1[0].toString();
	userName = result1[1].toString();
	JSONObject jo = new JSONObject();
	jo.put("id", id);
	jo.put("userName", userName);
	jarray.put(jo);
	}
	return jarray.toString();

	}
	@RequestMapping(value = "/api/getAssigneddashborad/{dlid}", produces = { "application/json" })
	public String getAssigneddashborad(HttpServletRequest request, @PathVariable Long dlid) {
			List<Object[]> list = new ArrayList<>();
			list = dLServices.getassManager(dlid);
			JSONArray jarray = new JSONArray();
			String id = "";
			String userName = "";
			for (Object[] result1 : list) {
				id = result1[0].toString();
				userName = result1[1].toString();
				JSONObject jo = new JSONObject();
				jo.put("id", id);
				jo.put("userName", userName);
				jarray.put(jo);
			}
			return jarray.toString();
		
	}

	@RequestMapping(value = "/api/getAssignedprofile/{dlid}", produces = { "application/json" })
	public String getAssignedprofile(HttpServletRequest request, @PathVariable Long dlid) {
			List<Object[]> list = new ArrayList<>();
			list = dLServices.getassprofileManager(dlid);
			JSONArray jarray = new JSONArray();
			String profileId = "";
			String dlid1 = "";
			for (Object[] result1 : list) {
				profileId = result1[0].toString();
				dlid1 = result1[1].toString();
				
				String[] profileid = profileId.split(",", 50);
				StringBuilder profile1 = new StringBuilder();
					for (String ids : profileid) {
						JSONObject jo = new JSONObject();
						if(ids.equalsIgnoreCase("NA")) {
							jo.put("profile1", "Profile Not Selected");
						}else {
							List list2 = dLServices.getprofilename(Long.parseLong(ids));
							for (int j = 0; j < list2.size(); j++) {
								profile1.append(((String) list2.get(j)));
								profile1.append(",");
								
							}
							profile1.deleteCharAt(profile1.lastIndexOf(","));
							jo.put("profile1", profile1.toString());
							jo.put("prId", ids);
						
					}
					
						jarray.put(jo);
					}
			}
			return jarray.toString();
	}

	
	//user dashboard
	 @RequestMapping(value = "/api/getuserlayout/{userid}", produces = { "application/json" })
	 public String getuseridlayout(HttpServletRequest request, @PathVariable("userid") long userid) {

	 List<Object[]> list = new ArrayList<>();
	 list = dLServices.getuserlayout(userid);
	 JSONArray jarray = new JSONArray();

	 String id = "";
	 String name = "";
	 String view = "";
	 String viewtype = "";
	 String profile="";
	 String htmlname="";
	 String dyname="";

	 for (Object[] result1 : list) {
	 id = result1[0].toString();
	 name = result1[1].toString();
	 view = result1[2].toString();
	 viewtype = result1[3].toString();
	 profile = result1[4].toString();
	 htmlname = result1[5].toString();
	 dyname = result1[6].toString();
	 JSONObject jo = new JSONObject();
	 String[] profileid = profile.split(",", 50);
	 StringBuilder profile1 = new StringBuilder();
	 if (profile != null) {
	 for (String ids : profileid) {
	 if(ids.equalsIgnoreCase("NA")) {
		 profile1.deleteCharAt(profile1.lastIndexOf(","));
	 jo.put("profile1", profile1.toString());
	 }else {
	 List list2 = dLServices.getprofilename(Long.parseLong(ids));
	 for (int j = 0; j < list2.size(); j++) {
	 profile1.append(((String) list2.get(j)));
	 profile1.append(",");
	  

	  //devicename1 += ((String) list2.get(j));
	 }
//	 	System.out.println("xcgxc "+yoyo.toString());

	 jo.put("profile1", profile1.toString());

	 }

	 //System.out.println(profile1.toString());
	 }
	 }
	 jo.put("id", id);
	 jo.put("name", name);
	 jo.put("view", view);
	 jo.put("viewtype", viewtype);
	 jo.put("htmlname", htmlname);
	 jo.put("dyname", dyname);
	 jarray.put(jo);
	 }
	 return jarray.toString();
	 }

	
	@RequestMapping(value = "/api/getdulayoutview/{dlid}/{userid}", produces = { "application/json" })
	public String getdulayoutview(HttpServletRequest request, @PathVariable("dlid") long dlid,@PathVariable("userid") long userid) {

		List<Object[]> list = new ArrayList<>();
		list = dLServices.getdulayoutview(dlid,userid);
		JSONArray jarray = new JSONArray();
		
		String dyid = "";
	
		String view = "";
		String viewtype = "";
		String deviceid="";
		String siteid="";
		String profile="";
		for (Object[] result1 : list) {
			
			
dyid = result1[0].toString();
view = result1[2].toString();
viewtype = result1[3].toString();
deviceid = result1[4].toString();
siteid = result1[5].toString();
profile = result1[6].toString();
			JSONObject jo = new JSONObject();

			jo.put("dyid", dyid);
			jo.put("view", view);
			jo.put("viewtype", viewtype);
			jo.put("deviceid", deviceid);
			jo.put("siteid", siteid);
			jo.put("profile", profile);
			jarray.put(jo);
		}
		return jarray.toString();
	}

	
	
}