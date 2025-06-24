package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.Model.AlearSummary;

import java.util.List;
import java.util.Optional;

public interface AlearSummaryService {

    AlearSummary saveOrUpdate(AlearSummary alearSummary);
    
    AlearSummary findByDeviceIdAndParameterIdAndIsActive(long deviceId, long parameterId, boolean isActive);
    
    AlearSummary findByDeviceIdAndParameterIdAndIsActiveAndEndTimeNull(long deviceId, long parameterId, boolean isActive);
    
    List<AlearSummary> findByStartTimeMoreThan4HoursAgoWithNullEndTimeAndManagerId(Long managerid);

    
}
