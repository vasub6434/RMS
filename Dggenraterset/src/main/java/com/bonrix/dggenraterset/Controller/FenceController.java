package com.bonrix.dggenraterset.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.FenceData;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.PoiData;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Service.FenceDataService;
import com.bonrix.dggenraterset.Service.UserService;

@RestController
@CrossOrigin(origins = { "*" })
public class FenceController {

	@Autowired
	private FenceDataService fenceDataService;
	
	@Autowired
	private UserService userService;
	
	private static final Logger log = Logger.getLogger(FenceController.class);

	@PostMapping("/api/addFenceData/{fencename}/{fencevalue}/{managerId}")
	public FenceData addFence(@PathVariable String fencename, @PathVariable String fencevalue,
			@PathVariable Long managerId, @RequestBody(required = false) Map<String, Object> optionalData) {

		FenceData fenceData = new FenceData();
		fenceData.setManagerid(managerId);
		fenceData.setFencename(fencename);
		fenceData.setFencevalue(fencevalue);

		if (optionalData != null) {
			fenceData.setFencetype((String) optionalData.getOrDefault("fencetype", "circle"));
			fenceData.setStatus(optionalData.get("status") != null ? (Boolean) optionalData.get("status") : true);
		} else {
			fenceData.setFencetype("circle");
			fenceData.setStatus(true);
		}

		return fenceDataService.addFence(fenceData);
	}

	@GetMapping("/api/getAllFenceData/{managerId}")
	public List<FenceData> getAllFences(@PathVariable Long managerId) {
		return fenceDataService.getAllFencesByManagerId(managerId);
	}

	@PutMapping("/api/updateFenceData/{id}/{managerId}")
	public FenceData updateFence(@PathVariable Long id, @PathVariable Long managerId,
			@RequestBody FenceData updatedFence) {
		return fenceDataService.updateFence(id, managerId, updatedFence);
	}

	@DeleteMapping("/api/deleteFenceData/{id}/{managerId}")
	public String deleteFence(@PathVariable Long id, @PathVariable Long managerId) {
		boolean deleted = fenceDataService.deleteFenceById(id, managerId);
		if (deleted) {
			return "Fence with ID " + id + " has been deleted.";
		} else {
			return "Fence with ID " + id + " not found or unauthorized.";
		}
	}

	@GetMapping("/api/getGeofenceReport/{managerId}/{deviceId}")
	public List<Map<String, Object>> getGeofenceReport(@PathVariable Long managerId, @PathVariable Long deviceId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date  startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date  endDate) {
		return fenceDataService.getGeofenceReport(managerId, deviceId, startDate, endDate);
	}
	
	@GetMapping("/api/getGeofenceReportByUser/{userId}/{deviceId}")
	public List<Map<String, Object>> getGeofenceReportByUser(@PathVariable Long userId, @PathVariable Long deviceId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
		 
		User user=userService.getuserbyid(userId);
		Long managerId=user.getAddedby();
		
		return fenceDataService.getGeofenceReport(managerId, deviceId, startDate, endDate);
	}

	@GetMapping("/api/getGPSPoint")
	public ResponseEntity<Map<String, Object>> getSurroundingData(@RequestParam Long deviceId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {

		Map<String, Object> response = new HashMap<>();
		try {
			log.info("Ravi :-  date is : " + date);
			List<Map<String, Object>> result = fenceDataService.getSurroundingLatLong(deviceId, date);

			response.put("data", result);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Failed to fetch data: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
