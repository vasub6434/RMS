package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Service.DashboardAPIService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.GenericApiService;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = { "*" })
@RestController
public class GenericApiController {

	@Autowired
	GenericApiService genericApiService;

	@Autowired
	DashboardAPIService dashboardService;

	@Autowired
	DevicemasterServices deviceService;

	@Autowired
	HistoryServices hstServide;

	private static final Logger log = Logger.getLogger(DashboardAPIController.class);
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@PostMapping("/api/GetGenericParameters")
	public List<Parameter> GetGenericParameters(@RequestBody List<Long> ids) {
		return genericApiService.getParametersByIds(ids);

	}

	@PostMapping(value = "/api/GetDeviceChartRecords", produces = "application/json")
	public String GetDeviceChartRecords(@RequestBody Map<String, Object> request)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		long id = Long.parseLong(request.get("id").toString());
		String prmnameStr = request.get("prmname").toString();
		List<String> prmnameList = Arrays.stream(prmnameStr.split(",")).map(String::trim).collect(Collectors.toList());
		int limit = request.containsKey("limit") ? Integer.parseInt(request.get("limit").toString()) : 0;
		JSONArray finalMainJSON = new JSONArray();

		for (String prmname : prmnameList) {
			List<Object[]> list = dashboardService.GetDeviceParameterRecords(id, prmname, limit);

			log.info("GetDeviceChartRecords for prmname " + prmname + ": " + list.size());

			JSONArray chartData = new JSONArray();
			try {
				for (Object[] record : list) {
					String deviceDate = record[0].toString();
					String data = record[1].toString();
					Date date = df.parse(deviceDate);
					long millis = date.getTime();

					JSONObject point = new JSONObject();
					point.put("x", millis);
					point.put("y", data);
					chartData.put(point);
				}
			} catch (NumberFormatException e) {
				log.warn("Invalid number format, using 0.0 for: " + e);
			}

			String label;
			if (prmname.equals("1290902193")) {
				label = "Temperature";
			} else if (prmname.equals("1290902357")) {
				label = "Pressure";
			} else if (prmname.equals("1355411238")) {
				label = "RUNNING HRS";
			} else if (prmname.equals("1355411608")) {
				label = "RUNNING Minutes";
			} else if (prmname.equals("1271458761")) {
				label = "Odometer";
			} else {
				label = "Parameter-" + prmname;
			}

			JSONObject chart = new JSONObject();
			JSONObject chartContent = new JSONObject();
			chartContent.put("type", "line");
			chartContent.put("series", new JSONArray().put(new JSONObject().put("name", label).put("data", chartData)));
			chart.put(label + "Chart", chartContent);
			finalMainJSON.put(chart);
		}

		return finalMainJSON.toString();
	}
	
	
	@GetMapping("/api/getProfileIdByProfileName")
    public ResponseEntity<?> getProfileIdByProfileName(@RequestParam String profileName) {
        Long profileId = genericApiService.getProfileIdByProfileName(profileName);
        if (profileId != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("profileName", profileName);
            response.put("profileId", profileId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
	
	@GetMapping("/api/getParameterIdByProfileId")
    public ResponseEntity<?> getParameterIdByProfileId(
        @RequestParam Long prid,
        @RequestParam List<String> names) {

        List<Map<String, Object>> result = genericApiService.getParameterIdsByNames(prid, names);
        return ResponseEntity.ok(result);
    }
	
	@GetMapping("/api/getParameterBydeviceId/{deviceId}")
    public ResponseEntity<List<Map<String, Object>>> getParameterBydeviceId(@PathVariable Long deviceId) {
        List<Map<String, Object>> parameters = genericApiService.getParametersByDeviceId(deviceId);
        return ResponseEntity.ok(parameters);
    }
	
	@GetMapping("/api/getRecordsForMeter/{deviceId}")
    public ResponseEntity<List<Map<String, Object>>> getRecordsForMeter(@PathVariable Long deviceId) {
        List<Map<String, Object>> parameters = genericApiService.getParametersByDeviceIdMinAndMax(deviceId);
        return ResponseEntity.ok(parameters);
    }

	@GetMapping("/api/getRecordsFormMultipalParameterForMeter")
	public ResponseEntity<?> getRecordsFormMultipalParameterForMeter(@RequestParam Long deviceId, @RequestParam String prmnames) {

	    List<String> paramIdList = Arrays.stream(prmnames.split(",")).map(String::trim).collect(Collectors.toList());
	    List<Map<String, Object>> results = new ArrayList<>();

	    for (String paramId : paramIdList) {
	        Map<String, Object> result = genericApiService.getParameterWithId(deviceId, paramId);
	        if (result != null && !result.isEmpty()) {
	            results.add(result);
	        } else {
	            Map<String, Object> error = new HashMap<>();
	            error.put("parameterId", paramId);
	            error.put("error", "Parameter ID not found or value missing");
	            results.add(error);
	        }
	    }

	    return ResponseEntity.ok(results);
	}

	@GetMapping("/api/getProfileIdByDeviceId/{deviceId}")
    public ResponseEntity<Map<String, Long>> getProfileIdByDeviceId(@PathVariable Long deviceId) {
        Long pridFk = genericApiService.getPridFkByDeviceId(deviceId);
        if (pridFk != null) {
            Map<String, Long> response = new HashMap<>();
            response.put("deviceId", deviceId);
            response.put("profileId", pridFk);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@GetMapping("/api/getParameterDataForLineChart")
	public ResponseEntity<?> getParameterDataForLineChart(@RequestParam("deviceId") Long deviceId,
			@RequestParam("parameterId") List<String> parameterId, @RequestParam("sdate") String sdate,
			@RequestParam("edate") String edate, @RequestParam(value = "limit", required = false) Integer limit) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Timestamp startDate = Timestamp.valueOf(LocalDateTime.parse(sdate, formatter));
			Timestamp endDate = Timestamp.valueOf(LocalDateTime.parse(edate, formatter));

			List<Map<String, Object>> result = genericApiService.getParameterDataForMultipleKeys(deviceId, startDate,
					endDate, parameterId, limit);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
		}
	}

}
