package com.bonrix.dggenraterset.Service;

import java.util.List;

import com.bonrix.dggenraterset.Model.FenceData;
import com.bonrix.dggenraterset.Model.PoiData;

public interface PoiService {

	 PoiData addPoi(PoiData poiData);
	    
	    List<PoiData> getAllPoisByManagerId(Long managerId);

	    PoiData updatePoi(Long id, Long managerId, PoiData updatedPoi);

	    boolean deletePoi(Long id, Long managerId);
	    
		Object getPoiHistoryReport(Long deviceId, Long managerId, String startDate, String endDate, String poiName, int radius);	

}
