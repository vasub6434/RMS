package com.bonrix.dggenraterset.Controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bonrix.dggenraterset.Model.PoiData;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Service.PoiService;
import com.bonrix.dggenraterset.Service.UserService;

@RestController
@CrossOrigin(origins = { "*" })
public class POIController {

	@Autowired
	private PoiService poiService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/api/addPOIData/{managerId}")
	public PoiData addPoi(@RequestBody PoiData poiData, @PathVariable Long managerId) {
		poiData.setManagerId(managerId);
		return poiService.addPoi(poiData);
	}

	@GetMapping("/api/getAllPOIData/{managerId}")
	public List<PoiData> getAllPois(@PathVariable Long managerId) {
		return poiService.getAllPoisByManagerId(managerId);
	}

	@PutMapping("/api/updatePOIData/{id}/{managerId}")
	public ResponseEntity<PoiData> updatePoi(
			@PathVariable Long id,
			@PathVariable Long managerId,
			@RequestBody PoiData updatedPoi) {

		PoiData result = poiService.updatePoi(id, managerId, updatedPoi);
		if (result == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/api/deletePOIData/{id}/{managerId}")
	public String deletePoi(@PathVariable Long id, @PathVariable Long managerId) {
		boolean deleted = poiService.deletePoi(id, managerId);
		if (deleted) {
			return "POI with ID " + id + " has been deleted.";
		} else {
			return "POI with ID " + id + " not found or unauthorized.";
		}
	}
	
	@GetMapping("/api/getPoiReport")
    public ResponseEntity<?> getPoiReport(
            @RequestParam Long deviceId,
            @RequestParam Long managerId,
            @RequestParam String sdate,
            @RequestParam String edate,
            @RequestParam String poiname,
            @RequestParam int radius) {
        try {
            Object response = poiService.getPoiHistoryReport(deviceId, managerId, sdate, edate, poiname, radius);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to generate report"));
        }
    }
	
	@GetMapping("/api/getPoiReportByUser")
	public ResponseEntity<?> getPoiReportByUser(@RequestParam Long deviceId, @RequestParam Long userId,
			@RequestParam String sdate, @RequestParam String edate, @RequestParam String poiname,
			@RequestParam int radius) {
		try {
			User user = userService.getuserbyid(userId);
			Long managerId = user.getAddedby();
			Object response = poiService.getPoiHistoryReport(deviceId, managerId, sdate, edate, poiname, radius);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Failed to generate report"));
		}
	}
}