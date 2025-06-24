package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.DashboardMaster;
import com.bonrix.dggenraterset.Model.MenuMst;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DashBoardMasterService;
import com.bonrix.dggenraterset.Service.LoginService;
import com.bonrix.dggenraterset.Service.MenuService;
import com.bonrix.dggenraterset.TcpServer.TK103ServerNew;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class LoginController {

	@Autowired
	LoginService loginService;

	@Autowired
	ApiService apiService;
	
	@Autowired
	DashBoardMasterService dashboardService;
	
	@Autowired
	MenuService menuService;
	
	private Logger log = Logger.getLogger(LoginController.class);
	
	
	//exesting code:-
//	@RequestMapping(value = "/user/UserLogin/{username}/{password}", produces = { "application/json" })
//	public String getcomponetlist(HttpServletRequest request, @PathVariable String username,
//			@PathVariable String password) throws JsonGenerationException, JsonMappingException, IOException {
//		User user = loginService.Login(username);
//		log.info(" LOG BONRIX :: "+username);
//		log.info("user id:"+user.getId());
//		if (user != null) {
//			JSONObject jo = new JSONObject();
//			JSONArray jarray = new JSONArray();
//
//			HttpSession session = request.getSession();
//			Apikey api = new Apikey();
//			UUID key = UUID.randomUUID();
//			for (UserRole s : user.getUserRole()) {
//				if (BCrypt.checkpw(password, user.getPassword())) {
//					api = apiService.findByuid(user.getId());
//					if (api == null) {
//						api = new Apikey();
//						api.setCreateDate(new Date());
//						api.setKeyValue(key.toString());
//						session.setAttribute("LoginKEY", key);
//						api.setUid(user.getId());
//						apiService.saveObject(api);
//						jo.put("api", api.getKeyValue());
//					} else {
//						api.setCreateDate(new Date());
//						api.setKeyValue(key.toString());
//						session.setAttribute("LoginKEY", key);
//						apiService.saveObject(api);
//						jo.put("api", api.getKeyValue());
//					}
//					
//					System.out.println("test bitbucket");
//					jo.put("responseCode", "SUCCESS");
//					jo.put("uId", user.getId());
//					jo.put("role", s.getRole());
//					jo.put("username", user.getUsername());
//					jo.put("redirectUrl", user.getRedirectUrl());
//					jarray.put(jo);
//					return jarray.toString();
//				} else {
//					jo.put("responseCode", "FAIL");
//					jarray.put(jo);
//					return jarray.toString();
//				}
//			}
//		}
//		return "[{\"responseCode\":\"FAIL\"}]";
//	}
	
	@RequestMapping(value = "/user/UserLogin/{username}/{password}", produces = "application/json")
	public String loginUser(HttpServletRequest request, @PathVariable String username, @PathVariable String password)
			throws IOException {

		log.info(" LOG BONRIX :: " + username);
		User user = loginService.Login(username);

		JSONArray responseArray = new JSONArray();
		JSONObject response = new JSONObject();

		if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
			response.put("responseCode", "FAIL");
			responseArray.put(response);
			return responseArray.toString();
		}

		UUID sessionKey = UUID.randomUUID();
		HttpSession session = request.getSession();
		session.setAttribute("LoginKEY", sessionKey);

		Apikey api = apiService.findByuid(user.getId());
		if (api == null) {
			api = new Apikey();
		}
		api.setCreateDate(new Date());
		api.setKeyValue(sessionKey.toString());
		api.setUid(user.getId());
		apiService.saveObject(api);

		for (UserRole roleObj : user.getUserRole()) {
			String role = roleObj.getRole();
			DashboardMaster dashboard = null;

			if ("ROLE_USER".equalsIgnoreCase(role)) {
				dashboard = dashboardService.findByUserIdAndRole(user.getId(), role);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(role)) {
				dashboard = dashboardService.findByManagerIdAndRole(user.getId(), role);
			} else {
				log.info(" LOG BONRIX the role is:: " + user.getUserRole());
				continue;
			}

			if (dashboard != null) {
				MenuMst menu = menuService.findByMid(dashboard.getMid());
				if (menu != null) {
					user.setRedirectUrl(menu.getMenuurl());
				}
			}

			response.put("api", api.getKeyValue());
			response.put("responseCode", "SUCCESS");
			response.put("uId", user.getId());
			response.put("role", role);
			response.put("username", user.getUsername());
			response.put("redirectUrl", user.getRedirectUrl());

			responseArray.put(response);
			return responseArray.toString();
		}

		response.put("responseCode", "FAIL");
		responseArray.put(response);
		return responseArray.toString();
	}

}
