package com.bonrix.dggenraterset.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.Manager;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.ManagerService;
import com.bonrix.dggenraterset.Utility.JsonUtills;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class ManagerController {

	@Autowired
	ApiService apiService;

	@Autowired
	ManagerService managerService;

	@RequestMapping(method = RequestMethod.POST, value = "/api/saveManager")
	@ExceptionHandler(SpringException.class)
	public String savemanager(@RequestBody Manager manager) {
		Manager u = managerService.savemanager(manager);
		managerService.savemanager("ROLE_MANAGER", u.getId());
		return new SpringException(true, "Manager Sucessfully Added :: " + u.getId()).toString();
	}

	@RequestMapping(value = "/api/getmanagerList/{key}", produces = { "application/json" })
	public String getmanagerList(HttpServletRequest request, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			List<Object[]> list = new ArrayList<>();
			list = managerService.getManagerlistnew();

			JSONArray jarray = new JSONArray();
			String id = "";
			String enabled = "";
			String password = "";
			String managername = "";
			String contact = "";
			String email = "";
			String name = "";
			for (Object[] result1 : list) {
				id = result1[0].toString();
				enabled = result1[3].toString();
				password = result1[6].toString();
				managername = result1[4].toString();
				contact = result1[1].toString();
				email = result1[2].toString();
				name = result1[5].toString();

				JSONObject jo = new JSONObject();
				jo.put("id", id);
				jo.put("managername", managername);
				jo.put("contact", contact);
				jo.put("email", email);
				jo.put("enabled", enabled);
				jo.put("password", password);
				jo.put("name", name);
				jarray.put(jo);
			}
			return jarray.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/manager/getmanagerList/{key}", produces = { "application/json" })
	public String getmanagerList(@PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		List<Manager> list = null;
		if (api != null) {
			list = managerService.getManagerlist();
			return JsonUtills.ListToJava(list);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.GET, value = "api/updatemanagers/{id}/{editmanagername}/{editmanagercontact}/{editmanageremail}/{key}")
	public String updatemanagers(@PathVariable long id, @PathVariable String editmanagername,
			@PathVariable String editmanagercontact, @PathVariable String editmanageremail, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		Manager user = managerService.getmanagerbyid(id);
		boolean enabled = user.isEnabled();
		String name = user.getName();
		String password = user.getPassword();

		if (api != null) {
			Manager usr = new Manager();
			usr.setContact(editmanagercontact);
			usr.setEmail(editmanageremail);
			usr.setEnabled(enabled);
			usr.setManagername(editmanagername);
			usr.setName(name);
			usr.setPassword(password);
			usr.setId(id);
			managerService.update(usr);
			return new SpringException(true, "Manager Sucessfully Updated").toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "api/deleteManagerById/{id}")
	public String deleteManagerById(@PathVariable long id) {
		Manager user = managerService.getmanagerbyid(id);
		JSONArray jarray = new JSONArray();
		if (user == null) {
			JSONObject jo = new JSONObject();
			jo.put("Status", "False");
			jo.put("Message", "No Manager Found");
			jarray.put(jo);
			System.out.println("No Manager");
			return jarray.toString();
		} else {
			managerService.deleteManagersById(id);
			JSONObject jo = new JSONObject();
			jo.put("Status", "True");
			jo.put("Message", "Manager Deleted Successfully");
			jarray.put(jo);
			return jarray.toString();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "api/ActDactManager/{id}/{actstat}")
	public String ActDactManager(@PathVariable long id, @PathVariable int actstat) {
		Manager user = managerService.getmanagerbyid(id);
		String name = user.getName();
		String contact = user.getContact();
		String username = user.getManagername();
		String email = user.getEmail();
		String password = user.getPassword();
		boolean enabled;
		String message = null;
		String status = null;

		if (actstat == 1) {
			enabled = true;
			message = "Manager Activated Successfully";
			status = "True";
		} else {
			enabled = false;
			message = "Manager DeActivated Successfully";
			status = "False";
		}

		JSONArray jarray = new JSONArray();
		if (user == null) {
			JSONObject jo = new JSONObject();
			jo.put("Status", "False");
			jo.put("Message", "No Manager Found");
			jarray.put(jo);
			System.out.println("No Manager");
			return jarray.toString();
		} else {
			Manager usr = new Manager();
			usr.setManagername(username);
			usr.setContact(contact);
			usr.setEmail(email);
			usr.setEnabled(enabled);
			usr.setPassword(password);
			usr.setName(name);
			usr.setId(id);
			managerService.update(usr);
			JSONObject jo = new JSONObject();
			jo.put("Status", status);
			jo.put("Message", message.toString());
			jarray.put(jo);
			return jarray.toString();
		}
	}
}
