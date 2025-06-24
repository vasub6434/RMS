package com.bonrix.dggenraterset.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;

import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.AssignSite;
import com.bonrix.dggenraterset.Model.AssignUserDevice;
import com.bonrix.dggenraterset.Model.BonrixUser;
import com.bonrix.dggenraterset.Model.DatatableJsonObject;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.UserService;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class UserController {

	@Autowired
	ApiService apiService;

	@Autowired
	UserService userService;

	String httpResponse = "";

	@RequestMapping(method = RequestMethod.POST, value = "/user/saveUser/{role}/{UID}/{key}")
	@ExceptionHandler(SpringException.class)
	public String savedata(@RequestBody User user, @PathVariable String role, @PathVariable Long UID,
			@PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String pass = user.getPassword();
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			String password = encoder.encode(pass);
			String mobileno = user.getContact();
			user.setPassword(password);
			user.setAddedby(UID);
			user.setContact(mobileno.trim());
			if (role.equals("ROLE_ADMIN")) {
				User u = userService.saveuser(user);
				userService.saveuser("ROLE_MANAGER", u.getId());
				return new SpringException(true, "Manager Sucessfully Added :: " + u.getId()).toString();
			} else if (role.equals("ROLE_MANAGER")) {
				User u = userService.saveuser(user);
				userService.saveuser("ROLE_USER", u.getId());
				return new SpringException(true, "User Sucessfully Added!").toString();
			} else {
				return new SpringException(false, "Something Went Wrong").toString();
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/user/getuserListnew/{key}", produces = { "application/json" })
	public List getuserListnew() {
		return userService.getUserlistnew1();
	}

	@RequestMapping(value = "/api/getmanagetList/{managerId}/{key}", produces = { "application/json" })
	public String getcomponetlist(@PathVariable long managerId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(key);
		List list = null;
		// org.json.simple.JSONArray jarray = new org.json.simple.JSONArray();
		JSONArray jarray = new JSONArray();
		if (api != null) {
			list = userService.getManagetList(managerId);
			if (list.size() != 0) {

				for (int i = 0; i < list.size(); i++) {
					JSONObject leadmap = new JSONObject();
					Object[] result = (Object[]) list.get(i);
					leadmap.put("managerId", result[0]);
					leadmap.put("username", result[1]);
					jarray.put(leadmap);
				}

			}
		}
		return jarray.toString();
	} 
	
	@RequestMapping(value = "/api/getprofiletList/{key}", produces = { "application/json" })
	public String getcomponetlist(@PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {
		Apikey api = apiService.findBykeyValue(key);
		List list = null;
		
		JSONArray jarray = new JSONArray();
		if (api != null) {
			list = userService.getProfileList();
			if (list.size() != 0) {

				for (int i = 0; i < list.size(); i++) {
					JSONObject leadmap = new JSONObject();
					Object[] result = (Object[]) list.get(i);
					leadmap.put("prid", result[0]);
					leadmap.put("profilename", result[1]);
					jarray.put(leadmap);
				}

			}
		}
		return jarray.toString();
	}

	/*
	 * @RequestMapping(value="/api/getmanagetList/{managetId}/{key}"
	 * ,produces={"application/json"}) public List getmanagetList(@PathVariable long
	 * managerId,@PathVariable String key) {
	 * 
	 * Apikey api=apiService.findBykeyValue(key); List lst=null; if(api!=null) {
	 * lst=userService.getManagetList(managerId); } return lst; }
	 */

	@RequestMapping(value = "/api/getuserList/{key}/{addedby}", produces = { "application/json" })
	public String getuserList(HttpServletRequest request, @PathVariable String key, @PathVariable Long addedby) {
		Apikey api = apiService.findBykeyValue(key);
		System.out.println("addedby:::" + addedby);
		if (api != null) {

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
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	// @RequestMapping(method=RequestMethod.GET,value="user/updateusers/{id}/{editusername}/{editusercontact}/{edituseremail}/{enabled}/{password}/{name}/{key}")
	@RequestMapping(method = RequestMethod.GET, value = "api/updateusers/{id}/{editusername}/{editusercontact}/{edituseremail}/{key}/{UID}")
	public String updateusers(HttpServletRequest request, @PathVariable long id, @PathVariable String editusername,
			@PathVariable String editusercontact, @PathVariable String edituseremail, @PathVariable String key,
			@PathVariable Long UID) {
		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {

			User user = userService.getuserbyid(id);
			boolean enabled = user.isEnabled();
			String name = user.getName();
			String password = user.getPassword();
			System.out.println("enabled::" + enabled + " " + "password::" + password + " " + "name::" + name + " "
					+ "key::" + key);

			User usr = new User();
			usr.setUsername(editusername);
			usr.setContact(editusercontact.trim());
			usr.setEmail(edituseremail);
			usr.setEnabled(enabled);
			usr.setPassword(password);
			usr.setName(name);
			usr.setId(id);
			usr.setAddedby(UID);
			userService.update(usr);
			return new SpringException(true, "User Sucessfully Updated").toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	

	@RequestMapping(method = RequestMethod.GET, value = "api/ActDactUser/{id}/{actstat}/{key}/{UID}")
	public String ActDactUser(HttpServletRequest request, @PathVariable long id, @PathVariable int actstat,
			@PathVariable String key, @PathVariable Long UID) {

		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {
			System.out.println("id::" + id);
			User user = userService.getuserbyid(id);
			String name = user.getName();
			String contact = user.getContact();
			String username = user.getUsername();
			String email = user.getEmail();
			String password = user.getPassword();

			boolean enabled;
			String message = null;
			String status = null;

			if (actstat == 1) {
				enabled = true;
				message = "User Activated Successfully";
				status = "True";
			} else {
				enabled = false;
				message = "User DeActivated Successfully";
				status = "False";
			}

			JSONArray jarray = new JSONArray();
			if (user == null) {
				JSONObject jo = new JSONObject();
				jo.put("Status", "False");
				jo.put("Message", "No User Found");
				jarray.put(jo);
				System.out.println("No User");
				return jarray.toString();
			} else {
				User usr = new User();
				usr.setUsername(username);
				usr.setContact(contact);
				usr.setEmail(email);
				usr.setEnabled(enabled);
				usr.setPassword(password);
				usr.setName(name);
				usr.setId(id);
				usr.setAddedby(UID);
				userService.update(usr);

				JSONObject jo = new JSONObject();
				jo.put("Status", status);
				jo.put("Message", message.toString());
				jarray.put(jo);
				return jarray.toString();
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "api/changepassword/{id}/{newpass}/{oldpass}/{key}")
	public String changepassword(HttpServletRequest request, @PathVariable long id, @PathVariable String newpass,
			@PathVariable String oldpass, @PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			String password = encoder.encode(newpass);

			System.out.println(
					"id::" + id + " " + "newpass::" + newpass + " " + "key::" + key + " " + "Password::" + password);

			User user = userService.getuserbyid(id);
			boolean enabled = user.isEnabled();
			String name = user.getName();
			String contact = user.getContact();
			String username = user.getUsername();
			String email = user.getEmail();
			String passwordold1 = user.getPassword();

			if (!BCrypt.checkpw(oldpass, passwordold1)) {
				System.out.println("Invalid Old Password");
				return new SpringException(false, "Invalid Old Password").toString();
			}

			User usr = new User();
			usr.setUsername(username);
			usr.setContact(contact);
			usr.setEmail(email);
			usr.setEnabled(enabled);
			usr.setPassword(password);
			usr.setName(name);
			usr.setId(id);
			userService.update(usr);
			return new SpringException(true, "Password Sucessfully Changed").toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "api/resetpassword/{username}")
	public String resetpassword(@PathVariable String username) throws IOException {

		User user = userService.getuserbyusername(username);
		JSONArray jarray = new JSONArray();

		if (user == null) {
			JSONObject jo = new JSONObject();
			jo.put("Status", "False");
			jo.put("Message", "No User Found");
			jarray.put(jo);

			System.out.println("No User");
			return jarray.toString();
		}

		String contact = user.getContact();
		long id = user.getId();
		boolean enabled = user.isEnabled();
		String name = user.getName();
		String username1 = user.getUsername();
		String email = user.getEmail();

		System.out.println("contact is::" + contact);

		String httpResponse = "";
		long expirytime;
		String newpass = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = encoder.encode(newpass);

		String msg = newpass + " " + "is Your New Password.";
		System.out.println("password is::" + password + " " + "msg is::" + msg + " " + "newpass::" + newpass);
		int value = 1;

		User usr = new User();
		usr.setUsername(username1);
		usr.setContact(contact);
		usr.setEmail(email);
		usr.setEnabled(enabled);
		usr.setPassword(password);
		usr.setName(name);
		usr.setId(id);
		userService.update(usr);

		try {
			msg = URLEncoder.encode(msg, "UTF-8");

			String passurl = "http://topsms.highspeedsms.com/sendsms.aspx?mobile=8155045500&pass=bonrixgps&senderid=BONRIX&to=<mobile_number>&msg=<message>";
			System.out.println("URL::" + passurl);

			passurl = passurl.replaceAll("\\<mobile_number\\>", contact.trim());
			passurl = passurl.replaceAll("\\<message\\>", msg.trim());
			System.out.println("COMPLETE URL" + username);
			URL sms;
			sms = new URL(passurl);
			HttpURLConnection httpConn = (HttpURLConnection) sms.openConnection();
			BufferedReader httpReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			this.httpResponse = "";
			String line = "";
			while ((line = httpReader.readLine()) != null) {
				this.httpResponse = line;
				System.out.println("Conformation From SMS Provider: :" + this.httpResponse);
				if (this.httpResponse.equalsIgnoreCase("Message dropped but Not Sent".trim()))
					value = 0;
			}
			return "Your New Password Send On " + contact;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			value = 0;
			return "Something Went Wrong";
		}
	}

	@RequestMapping(value = "/api/assignusersite/{userId}/{device_ids}/{managerId}/{key}", method = RequestMethod.GET)
	public String asigndevice(@PathVariable("userId") String userId, @PathVariable("device_ids") String device_ids,
			@PathVariable("managerId") long managerId, Authentication auth, @PathVariable String key)
			throws UnsupportedEncodingException {
   
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			String prm = URLDecoder.decode(device_ids, "UTF-8");
			String[] arrOfdeviceIds = device_ids.split(",");
			for (String nwdevice_id : arrOfdeviceIds) {   
				AssignUserDevice assgn = new AssignUserDevice();
				assgn.setDevice_id(Long.parseLong(nwdevice_id));
				assgn.setManager_id(managerId);
				assgn.setUser_id(Long.parseLong(userId));
				userService.saveassignuserdevice(assgn);
			}
			return "Device  Assigned Sucessfully";

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/assignuserdevice/{userId}/{device_ids}/{managerId}/{key}",method = RequestMethod.GET )
	public String assignuserdevice(@PathVariable("userId") String userId,@PathVariable("device_ids") String device_ids ,@PathVariable("managerId") long managerId,Authentication auth,@PathVariable String key) throws UnsupportedEncodingException {
		
		Apikey api=apiService.findBykeyValue(key);
		if(api!=null){
			String stat = userService.deleteUserAssignDevice(Long.parseLong(userId));
			if(stat.equalsIgnoreCase("1")) {
				String prm =URLDecoder.decode(device_ids, "UTF-8");
				String[] arrOfdeviceIds = device_ids.split(",", 50);
				for (String nwdevice_id : arrOfdeviceIds) {
					AssignUserDevice assgn = new AssignUserDevice();
					assgn.setDevice_id(Long.parseLong(nwdevice_id));
					assgn.setManager_id(managerId);
					assgn.setUser_id(Long.parseLong(userId));
					userService.saveassignuserdevice(assgn);
				}
				return "Device  Assigned Sucessfully";
			}else {
				return "Error During Update";
			}
		}else {
			 return new SpringException(false, "Invalid Key").toString();
		 }
	}
	
	@RequestMapping(value = "/api/delusersDevice/{userId}/{managerId}/{key}", method = RequestMethod.GET)
	public String delusersDevice(@PathVariable("userId") Long userId,@PathVariable String key,@PathVariable("managerId") Long managerId, Authentication auth) throws UnsupportedEncodingException {
	
		Apikey api=apiService.findBykeyValue(key);
		if(api!=null)
		{	
			String stat =userService.deleteUserAssignDevice(userId);
			return "Device Assigned Sucessfully";
		}else {
			 return new SpringException(false, "Invalid Key").toString();
		 }
	}

	@RequestMapping(method = RequestMethod.GET, value = "api/newMasterPWD/{id}/{password}/{key}")
	public String newMasterPWD(HttpServletRequest request, @PathVariable long id,@PathVariable String password, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			userService.newMasterPassword(id, password);
			return "true";
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}
	@RequestMapping(method = RequestMethod.GET, value = "api/getMasterPWDStatus/{id}/{key}")
	public String getMasterPWDStatus(HttpServletRequest request, @PathVariable long id, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			User user = userService.getuserbyid(id);
			
			if (user.getMasterPassword() != null) {  
				return "true";
				
			} else {
				return "false";
			}
		} else
			return new SpringException(false, "Invalid Key").toString();

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "api/updateStatusById/{id}/{Status}/{key}")
	public String updateStatus(HttpServletRequest request, @PathVariable long id, @PathVariable boolean Status,
			@PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		JSONArray jarray = new JSONArray();
		if (api != null) {
			userService.updatestatus(id, Status);
			JSONObject jo = new JSONObject();
			jo.put("Status", "1");
			jarray.put(jo);
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
		return jarray.toString();

	}
	@RequestMapping(method = RequestMethod.GET, value = "api/updateManagerPass/{mid}/{adminpassword}/{newpassword}/{adminid}/{key}")
	public String updateManagerPass(HttpServletRequest request, @PathVariable long mid,@PathVariable long adminid,@PathVariable String adminpassword,@PathVariable String newpassword, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			User user = userService.getuserbyid(adminid);
			if(user.getMasterPassword().equalsIgnoreCase(adminpassword))
			{
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				String password = encoder.encode(newpassword);
				userService.updateManagerPassword(mid, password);
				return "true";
			}
			else {
				return "false";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}         

	@RequestMapping(method = RequestMethod.GET, value = "api/updateuserPass/{uid}/{adminpassword}/{newpassword}/{mid}/{key}")
	public String updateuserPass(HttpServletRequest request, @PathVariable long uid,@PathVariable long mid,@PathVariable String adminpassword,@PathVariable String newpassword, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			
			User user = userService.getuserbyid(mid);
			Long addedid=user.getAddedby();
			User user1 = userService.getuserbyid(addedid);
			if(user1.getMasterPassword().equalsIgnoreCase(adminpassword))
			{
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				String password = encoder.encode(newpassword);
				userService.updateManagerPassword(uid, password);
				return "true";
			}
			else {
				return "false";
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}
	@RequestMapping(method = RequestMethod.GET, value = "api/deleteUserById/{id}/{key}")
	public String deleteUserById(HttpServletRequest request, @PathVariable long id, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);

		if (api != null) {
			
			User user = userService.getuserbyid(id);
			JSONArray jarray = new JSONArray();
			if (user == null) {
				JSONObject jo = new JSONObject();
				jo.put("Status", "False");
				jo.put("Message", "No User Found");
				jarray.put(jo);
				System.out.println("No User");
				return jarray.toString();
			} else {

				userService.deleteUsersRoleById(id);
				userService.deleteUsersDeviceById(id);
				userService.deleteUsersById(id);
				JSONObject jo = new JSONObject();
				jo.put("Status", "True");
				jo.put("Message", "User Deleted Successfully");
				jarray.put(jo);
				return jarray.toString();
			}
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "api/updateMasterPWD/{id}/{oldpassword}/{password}/{key}")
	public String updateMasterPWD(HttpServletRequest request, @PathVariable long id,@PathVariable String oldpassword,@PathVariable String password, @PathVariable String key) {
		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			User user = 
					 userService.getuserbyid(id);
			if(user.getMasterPassword().equalsIgnoreCase(oldpassword))
			{
				userService.newMasterPassword(id, password);
				return "true";
			}
			else {
				return "false";
			}
		} else
			return new SpringException(false, "Invalid Key").toString();

	}
	
	
}
