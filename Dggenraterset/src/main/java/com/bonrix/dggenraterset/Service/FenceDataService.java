package com.bonrix.dggenraterset.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bonrix.dggenraterset.Model.FenceData;
import com.bonrix.dggenraterset.Model.History;

public interface FenceDataService {

	FenceData addFence(FenceData fenceData);

	List<FenceData> getAllFencesByManagerId(Long managerId);

	FenceData updateFence(Long id, Long managerId, FenceData updatedData);

	boolean deleteFenceById(Long id, Long managerId);
	
	List<Map<String, Object>> getGeofenceReport(Long managerId, Long deviceId, Date startDateStr, Date endDateStr);

	public List<Map<String, Object>> getSurroundingLatLong(Long deviceId, Date date);
		      
}
