package com.bonrix.dggenraterset.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.DTO.ActiveAlertDTO;
import com.bonrix.dggenraterset.DTO.AlertCountBySiteDTO;
import com.bonrix.dggenraterset.DTO.AlertDurationDTO;
import com.bonrix.dggenraterset.DTO.AlertFrequency;
import com.bonrix.dggenraterset.DTO.AlertStatusDTO;
import com.bonrix.dggenraterset.DTO.AlertTrendDto;
import com.bonrix.dggenraterset.DTO.DeviceResponse;
import com.bonrix.dggenraterset.DTO.SiteStatsDTO;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.GetDeviceWithParameter;
import com.bonrix.dggenraterset.Service.AlertMessageSummaryService;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.UserService;
import com.ibm.icu.text.SimpleDateFormat;

@CrossOrigin(origins = { "*" })
@RestController
public class AlertMessageSummaryController {

	@Autowired
	private ApiService apiService;

	@Autowired
	private AlertMessageSummaryService alertService;

	@Autowired
	private UserService userService;

	@Autowired
	private GetDeviceWithParameter grepo;

	@Autowired
	private DevicemasterRepository Dservice;

	@Autowired
	private DeviceProfileRepository DpService;

	@RequestMapping(value = "/api/getRecentAlerts/{key}/{fromdate}/{todate}/{size}/{page}/{status}/{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getRecentAlerts(@PathVariable String key, @PathVariable String fromdate, @PathVariable String todate,
			@PathVariable int size, @PathVariable int page, @PathVariable String status, @PathVariable Long uid,
			@RequestParam(value = "deviceId", required = false) Long deviceId,
			@RequestParam(value = "parameterId", required = false) Long parameterId)
			throws JSONException, ParseException {

		if (page < 1)
			page = 1;

		User user = userService.getuserbyid(uid);
		boolean isManager = user.getUserRole().stream()
				.anyMatch(r -> "ROLE_MANAGER".equalsIgnoreCase(r.getRole().toString()));

		int totalCount;
		List<Object[]> rows;

		if (isManager) {
			totalCount = alertService.getRecentAlertsCount(fromdate, todate, deviceId, status, parameterId, uid);
			rows = alertService.getRecentAlertsPage(fromdate, todate, deviceId, status, size, page, parameterId, uid);
		} else {
			totalCount = alertService.getRecentAlertsCountForUser(fromdate, todate, deviceId, status, parameterId, uid);
			rows = alertService.getRecentAlertsPageForUser(fromdate, todate, deviceId, status, size, page, parameterId,
					uid);
		}

		JSONObject resp = new JSONObject();
		JSONArray cols = new JSONArray().put(new JSONArray().put("#")).put(new JSONArray().put("Site Name"))
				.put(new JSONArray().put("Device ID")).put(new JSONArray().put("Alert Name"))
				.put(new JSONArray().put("Start Time")).put(new JSONArray().put("End Time"))
				.put(new JSONArray().put("Duration")).put(new JSONArray().put("Since"))
				.put(new JSONArray().put("Status"));
		resp.put("columns", cols);

		JSONArray data = new JSONArray();
		int sr = (page - 1) * size + 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (Object[] r : rows) {
			JSONArray row = new JSONArray();
			row.put(sr++);
			row.put(r[0]);
			row.put(r[1]);
			row.put(r[2]);
			row.put(r[3]);
			Object endTime = r[4];
			row.put(endTime != null ? endTime.toString() : JSONObject.NULL);
			row.put(r[5]);
			String sinceText = "*Ongoing*";
			if (endTime != null) {
				try {
					Date start = sdf.parse(r[3].toString());
					Date end = sdf.parse(endTime.toString());
					long diffSeconds = (end.getTime() - start.getTime()) / 1000;
					long minutes = diffSeconds / 60;
					long seconds = diffSeconds % 60;
					sinceText = minutes + " min " + seconds + " sec";
				} catch (Exception e) {
					sinceText = "N/A";
				}
			}
			row.put(sinceText);
			row.put(r[6]);
			data.put(row);
		}

		JSONObject pg = new JSONObject();
		pg.put("totalItems", totalCount);
		pg.put("totalPages", (int) Math.ceil((double) totalCount / size));
		pg.put("currentPage", page);
		pg.put("pageSize", size);
		resp.put("pagination", pg);
		resp.put("data", data);

		return resp.toString();
	}

	@GetMapping("/api/alertCountByType/{fromDate}/{toDate}/{uid}")
	public List<AlertStatusDTO> countByType(
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@PathVariable("uid") Long uid) {

		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getAlertCountsByTypeForUser(fromDate, toDate, uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getAlertCountsByType(fromDate, toDate, uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getActiveAlert/{fromDate}/{toDate}/{uid}")
	public List<ActiveAlertDTO> active(
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getActiveAlertsForUser(fromDate, toDate, uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getActiveAlerts(fromDate, toDate, uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getAlertBySite/{fromDate}/{toDate}/{uid}")
	public List<AlertCountBySiteDTO> bySite(
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getAlertsBySiteAndType1ForUser(fromDate, toDate, uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getAlertsBySiteAndType1(fromDate, toDate, uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getAvarageDuration/{fromDate}/{toDate}/{uid}")
	public List<AlertDurationDTO> getAvgDurationByAlertType(
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getAvgDurationByAlertTypeForUser(fromDate, toDate, uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getAvgDurationByAlertType(fromDate, toDate, uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getTopFiveSite/{uid}")
	public List<SiteStatsDTO> topFiveSitesLast24h(@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.top5SitesLast24hForUser(uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.top5SitesLast24h(uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getTopFiveAlert/{uid}")
	public List<AlertStatusDTO> top5AlertsLast24h(@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.top5AlertsLast24hForUser(uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.top5AlertsLast24h(uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getActiveAlertByType/{fromDate}/{toDate}/{uid}")
	public List<AlertStatusDTO> activeByType(
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getActiveAlertsByTypeForUser(fromDate, toDate, uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getActiveAlertsByType(fromDate, toDate, uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/alert-trend/{fromDate}/{toDate}/{uid}")
	public List<AlertTrendDto> getTrend(
			@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getTrendForUser(fromDate, toDate, uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getTrend(fromDate, toDate, uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/getDevicesWithParameter/{uid}")
	public List<Map<String, Object>> getDeviceResponse(@PathVariable Long uid,
			@RequestParam(value = "deviceId", required = false) Long deviceId) {
		User user = userService.getuserbyid(uid);

		List<Object[]> profileList = null;
		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				profileList = DpService.Assigndeviceprofilebyuid(uid);
				break;
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				profileList = DpService.assigndeviceprofilebymanagerid(uid);
				break;
			}
		}
		if (profileList == null) {
			profileList = Collections.emptyList();
		}

		return alertService.getDeviceResponseGrouped(deviceId, profileList);
	}

	@GetMapping("/api/getDeviceByRole/{uid}")
	public ResponseEntity<List<Map<String, Object>>> getProfilesByUserManagerRole(@PathVariable Long uid) {
		User user = userService.getuserbyid(uid);
		List<Object[]> rawData = null;

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				rawData = grepo.getDeviceForUser(uid);
				break;
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				rawData = grepo.getDeviceByManagerId(uid);
				break;
			}
		}

		if (rawData == null) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		List<Map<String, Object>> responseList = new ArrayList<>();
		for (Object[] result1 : rawData) {
			Map<String, Object> jo = new LinkedHashMap<>();
			jo.put("deviceId", result1[0]);
			jo.put("deviceName", result1[1]);

			responseList.add(jo);
		}

		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/api/getResolutionTimeTrend/{uid}")
	public List<Map<String, Object>> getResolutionTimeTrend(@PathVariable("uid") Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getLast30DaysTrendForUSer(uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getLast30DaysTrend(uid);
			}
		}

		return Collections.emptyList();
	}

	@GetMapping("/api/frequencyHitmapOfLastWeek/{uid}")
	public List<AlertFrequency> frequencyLast7Days(@PathVariable Long uid) {
		User user = userService.getuserbyid(uid);

		for (UserRole role : user.getUserRole()) {
			String r = role.getRole().toString();
			if ("ROLE_USER".equalsIgnoreCase(r)) {
				return alertService.getLast7DaysFrequencyForUser(uid);
			} else if ("ROLE_MANAGER".equalsIgnoreCase(r)) {
				return alertService.getLast7DaysFrequency(uid);
			}
		}

		return Collections.emptyList();
	}

}
	