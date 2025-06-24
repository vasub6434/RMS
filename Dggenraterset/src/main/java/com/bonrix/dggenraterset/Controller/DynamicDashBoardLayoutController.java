package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.AssignDashboardUsers;
import com.bonrix.dggenraterset.Model.AssignManagerLayout;
import com.bonrix.dggenraterset.Model.DashBoardLayout;
import com.bonrix.dggenraterset.Model.DynamicDashBoardLayout;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.DashBoardLayoutServices;
import com.bonrix.dggenraterset.Service.DynamicDashBoardLayoutService;
import com.bonrix.dggenraterset.Service.UserService;
import com.fasterxml.jackson.core.JsonGenerationException;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class DynamicDashBoardLayoutController {

	@Autowired
	public DynamicDashBoardLayoutService dynamicdashlayout;

	@Autowired
	public DashBoardLayoutServices dLServices;
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "/api/saveDynamicDashBoardLayout/{deviceId}/{siteId}/{dlId}/{layoutName}/{managerid}", method = RequestMethod.GET)
	public String saveDynamicDashBoardLayout(@PathVariable("deviceId") String deviceId,
			@PathVariable("siteId") String siteId, @PathVariable("dlId") Long dlId,
			@PathVariable("layoutName") String layoutName, @PathVariable("managerid") Long managerid) throws UnsupportedEncodingException {
		DynamicDashBoardLayout assgn = new DynamicDashBoardLayout();
		assgn.setDeviceid(deviceId);
		assgn.setDyname(layoutName);
		assgn.setDlid(dlId);
		assgn.setSiteid(siteId);
		assgn.setManagerid(managerid);
		dynamicdashlayout.saveDynamicDashBoard(assgn);

		return new SpringException(true, "Sucessfully Inserted").toString();
	}

	@RequestMapping(value = "/api/getDynamicDashboardList", produces = { "application/json" })
	public String getDynamicDashboardList(HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<Object[]> list = new ArrayList<>();
		list = dynamicdashlayout.getDynamiclayoutList();
		JSONArray jarray = new JSONArray();

		String dyid = "";
		String devicename = "";
		String dashboardname = "";
		String dyname = "";
		String sitename = "";
		String view = "";
		String view_type = "";
		String profile = "";
		String dlid = "";

		for (Object[] result1 : list) {
			dyid = result1[0].toString();
			devicename = result1[1].toString();
			dashboardname = result1[2].toString();
			dyname = result1[3].toString();
			sitename = result1[4].toString();
			view = result1[5].toString();
			view_type = result1[6].toString();
			profile = result1[7].toString();
			dlid = result1[8].toString();
			
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
						
						jo.put("profile1", profile1.toString());
					
				}
				
				//System.out.println(profile1.toString());
				}
			}
			
			String[] device = devicename.split(",", 50);
			String[] site = sitename.split(",", 50);
			StringBuilder devicename1 = new StringBuilder();
			StringBuilder sitename1 = new StringBuilder();
			if (device != null) {
				for (String ids : device) {
					if (ids.equalsIgnoreCase("NA")) {
						jo.put("devicename1", ids);
					} else {
						List list2 = dynamicdashlayout.getDeviceList(Long.parseLong(ids));
						for (int j = 0; j < list2.size(); j++) {
							devicename1.append(((String) list2.get(j)));
							devicename1.append(",");
							  
							 
							  //devicename1 += ((String) list2.get(j));
						}
					//	System.out.println("xcgxc "+yoyo.toString());
						jo.put("devicename1", devicename1.toString());			
					}
				}
			}  if(site!=null){	
				for (String ids : site) {
					if (ids.equalsIgnoreCase("NA")) {
						jo.put("sitename1", ids);
					} else {
						List list2 = dynamicdashlayout.getSiteList(Long.parseLong(ids));
						for (int j = 0; j < list2.size(); j++) {
							sitename1.append(((String) list2.get(j)));
							sitename1.append(",");
							//sitename1 += ((String) list2.get(j));
							
						}
						//System.out.println(sitename1);
						jo.put("sitename1", sitename1.toString());
					}				
				}
			}else {
				System.out.println("OK");
			}	
			jo.put("dashboardname", dashboardname);
			jo.put("dyname", dyname);
			jo.put("dyid", dyid);
			jo.put("view", view);
			jo.put("view_type", view_type);
			jo.put("dlid", dlid);

			jarray.put(jo);
		}
		return jarray.toString();
	}

	@RequestMapping(value = "/api/deleteDyanamicLayoutById/{id}", method = RequestMethod.GET)
	public void deleteDyanamicLayoutById(@PathVariable long id) {
		dLServices.deleteAssuser(id);
		dynamicdashlayout.delete(id);
	}

	@RequestMapping(value = "/api/getAlluser/{addedby}", produces = { "application/json" })
	public String getAlluser(HttpServletRequest request, @PathVariable Long addedby) {
		List<Object[]> list = new ArrayList<>();
		list = userService.getUserlistnew(addedby);
		System.out.println("Controller_list:::" + list.size());

		JSONArray jarray = new JSONArray();

		String id = "";
		String enabled = "";
		String password = "";
		String username = "";
		String contact = "";
		String email = "";
		String name = "";
		for (Object[] result1 : list) {
			id = result1[0].toString();
			enabled = result1[1].toString();
			password = result1[2].toString();
			username = result1[3].toString();
			contact = result1[4].toString();
			email = result1[5].toString();
			name = result1[6].toString();

			JSONObject jo = new JSONObject();
			jo.put("id", id);
			jo.put("username", username);
			jo.put("contact", contact.trim());
			jo.put("email", email);
			jo.put("enabled", enabled);
			jo.put("password", password);
			jo.put("name", name);

			jarray.put(jo);
		}
		return jarray.toString();

	}
	
	@RequestMapping(value = "/api/saveDynamicDashBoardLayoutUser/{dashboardid}/{userid}", method = RequestMethod.GET)
	public String saveDynamicDashBoardLayoutUser(@PathVariable("dashboardid") Long dashboardid,
			@PathVariable("userid") String userid ) throws UnsupportedEncodingException {
		String prm = URLDecoder.decode(userid, "UTF-8");
		String[] arrOfdeviceIds = userid.split(",", 50);
		for (String nwdmanager_id : arrOfdeviceIds) {
			
		AssignDashboardUsers assgn = new AssignDashboardUsers();
		assgn.setManagerdashId(dashboardid);
		assgn.setUserid(Long.parseLong(nwdmanager_id));
		dynamicdashlayout.saveuserDashboard(assgn);
		}
		return new SpringException(true, "Sucessfully Inserted").toString();
	}
	
	@RequestMapping(value = "/api/editDynamicLayoutById/{eid}/{dlId}/{layoutName}/{deviceId}/{siteId}/{managerid}", method = RequestMethod.GET)
	public String editDynamicLayoutById(@PathVariable long eid,@PathVariable long dlId, @PathVariable String layoutName,
			@PathVariable String deviceId, @PathVariable String siteId,@PathVariable long managerid) throws UnsupportedEncodingException {
	//	dynamicdashlayout.delete(eid);
		
		DynamicDashBoardLayout assgn = new DynamicDashBoardLayout();
		assgn.setDyid(eid);
		assgn.setDeviceid(deviceId);
		assgn.setDyname(layoutName);
		assgn.setDlid(dlId);
		assgn.setSiteid(siteId);
		assgn.setManagerid(managerid);
		dynamicdashlayout.updateDynamicDashBoard(assgn);

		return new SpringException(true, "Sucessfully Inserted").toString();
		

	}
	
	
	@RequestMapping(value = "/api/getmanagerlayout/{managerId}", produces = { "application/json" })
	public String getmanagerlayout(HttpServletRequest request, @PathVariable("managerId") long managerId) {

		List<Object[]> list = new ArrayList<>();
		list = dLServices.getmanagerlayout(managerId);
		JSONArray jarray = new JSONArray();

		String id = "";
		String name = "";
		String view = "";
		String viewtype = "";
		String profile="";
		String htmlname="";
		//String dyname="";

		for (Object[] result1 : list) {
			id = result1[0].toString();
			name = result1[1].toString();
			view = result1[2].toString();
			viewtype = result1[3].toString();
			profile = result1[4].toString();
			htmlname = result1[5].toString();
		//	dyname = result1[6].toString();
			JSONObject jo = new JSONObject();
			String[] profileid = profile.split(",", 50);
			StringBuilder profile1 = new StringBuilder();
			if (profile != null) {
				for (String ids : profileid) {
					if(ids.equalsIgnoreCase("NA")) {
						jo.put("profile1", profile1.toString());
					}else {
						List list2 = dLServices.getprofilename(Long.parseLong(ids));
						for (int j = 0; j < list2.size(); j++) {
							profile1.append(((String) list2.get(j)));
							profile1.append(",");
							  
							 
							  //devicename1 += ((String) list2.get(j));
						}
					//	System.out.println("xcgxc "+yoyo.toString());
						
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
			//jo.put("dyname", dyname);
			jarray.put(jo);
		}
		return jarray.toString();
	}
	
	
	
	
	
	@RequestMapping(value = "/api/getdlayoutview/{dlid}/{managerid}", produces = { "application/json" })
	public String getdlayoutview(HttpServletRequest request, @PathVariable("dlid") long dlid,@PathVariable("managerid") long managerid) {

		List<Object[]> list = new ArrayList<>();
		list = dLServices.getdlayoutview(dlid,managerid);
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