package com.bonrix.dggenraterset.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.Model.Parameter;


public interface GenericApiService {

	List<Parameter> getParametersByIds(List<Long> ids);

	List<Map<String, Object>> getParameterIdsByNames(Long prid, List<String> names);

	Long getProfileIdByProfileName(String profileName);

	List<Map<String, Object>> getParametersByDeviceId(Long deviceId);

	List<Map<String, Object>> getParametersByDeviceIdMinAndMax(Long deviceId);

	
	Map<String, Object> getParameterWithId(Long deviceId, String paramId);

	Long getPridFkByDeviceId(Long deviceId);

	List<Map<String, Object>> getParameterDataForMultipleKeys(Long deviceId, Timestamp startDate, Timestamp endDate,List<String> parameterId, Integer limit);
}
