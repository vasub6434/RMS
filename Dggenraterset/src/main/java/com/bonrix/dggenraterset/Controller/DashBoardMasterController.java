package com.bonrix.dggenraterset.Controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.bonrix.dggenraterset.Model.*;
import com.bonrix.dggenraterset.Service.*;

@CrossOrigin(origins = { "*" })
@RestController
public class DashBoardMasterController {

	@Autowired
	private UserService userService;

	@Autowired
	private DashBoardMasterService dashboardService;

	@RequestMapping(value = "/api/allocateRedirectUrl", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> allocateRedirectUrl(@RequestParam(value = "uid") Long uid,
			@RequestParam(value = "mid") Long mid) {

		Map<String, Object> response = new HashMap<>();
		try {
			User user = userService.getuserbyid(uid);

			for (UserRole role : user.getUserRole()) {
				String r = role.getRole().toString();

				DashboardMaster mst = new DashboardMaster();
				if ("ROLE_USER".equalsIgnoreCase(r)) {
					mst.setManagerId(0L);
					mst.setUserId(uid);
					mst.setRole("ROLE_USER");
					
				} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
					mst.setManagerId(uid);
					mst.setUserId(0L);
					mst.setRole("ROLE_MANAGER");
				} else {
					continue;
				}

				mst.setMid(mid);
				String menuurl = dashboardService.getMenuUrl(mid);
				System.out.print(menuurl);
				user.setRedirectUrl(menuurl);
				userService.saveuser(user);
				dashboardService.newMenu(mst);
			}

			response.put("status", "success");
			response.put("message", "Redirect URL allocated successfully.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@RequestMapping(value = "/api/getAllocatedMenuById/{id}", produces = {
			"application/json" }, method = RequestMethod.GET)
	public String getMenuById(@PathVariable Long id) {
		User user = userService.getuserbyid(id);
		JSONArray jarray = new JSONArray();
		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();

			if ("ROLE_USER".equalsIgnoreCase(r)) {
				List<Object[]> list = dashboardService.getAllocatedMenuByUserId(id);
				if (!list.isEmpty()) {
					for (Object[] result1 : list) {
						Map<String, String> jsonOrderedMap = new LinkedHashMap<>();
						jsonOrderedMap.put("id", result1[0].toString());
						jsonOrderedMap.put("menuname", result1[1].toString());
						jsonOrderedMap.put("url", result1[2].toString());
						jsonOrderedMap.put("allocation", result1[3].toString());
						jsonOrderedMap.put("userid", result1[4] == null ? "0" : result1[4].toString());
						jarray.put(jsonOrderedMap);
					}
				}
			}

			if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				List<Object[]> list = dashboardService.getAllocatedMenuByManagerId(id);
				if (!list.isEmpty()) {
					for (Object[] result1 : list) {
						Map<String, String> jsonOrderedMap = new LinkedHashMap<>();
						jsonOrderedMap.put("id", result1[0].toString());
						jsonOrderedMap.put("menuname", result1[1].toString());
						jsonOrderedMap.put("url", result1[2].toString());
						jsonOrderedMap.put("allocation", result1[3].toString());
						jsonOrderedMap.put("managerid", result1[4] == null ? "0" : result1[4].toString());
						jarray.put(jsonOrderedMap);
					}
				}

			}
		}
		return jarray.toString();
	}
}
