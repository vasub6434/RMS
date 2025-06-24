package com.bonrix.dggenraterset.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.DTO.AnalogParameter;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Repository.GenericApiRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GenericApiServiceImpl implements GenericApiService {

	@Autowired
	private GenericApiRepository genericApiRepository;

	@Autowired
	private DeviceProfileRepository deviceProfileRepository;
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private DevicemasterRepository devicemasterRepository;

	@Override
	public List<Parameter> getParametersByIds(List<Long> ids) {
		return genericApiRepository.findByIdIn(ids);
	}

	@Override
	public List<Map<String, Object>> getParameterIdsByNames(Long prid, List<String> names) {
		DeviceProfile profile = deviceProfileRepository.findByPrid(prid);
		List<Map<String, Object>> result = new ArrayList<>();

		if (profile == null || profile.getParameters() == null)
			return result;

		try {
			Map<String, Object> parametersMap = profile.getParameters();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.convertValue(parametersMap, JsonNode.class);

			JsonNode analogArray = root.path("Analog");
			if (analogArray.isArray()) {
				for (JsonNode node : analogArray) {
					String analogname = node.path("analogname").asText();
					long analoginput = node.path("Analoginput").asLong();

					if (names.contains(analogname)) {
						Map<String, Object> map = new HashMap<>();
						map.put("parameterType", "Analog");
						map.put("parameterName", analogname);
						map.put("parameterId", analoginput);
						result.add(map);
					}
				}
			}

			JsonNode digitalArray = root.path("Digital");
			if (digitalArray.isArray()) {
				for (JsonNode node : digitalArray) {
					String pname = node.path("parametername").asText();
					long pid = node.path("parameterId").asLong();

					if (names.contains(pname)) {
						Map<String, Object> map = new HashMap<>();
						map.put("parameterType", "Digital");
						map.put("parameterName", pname);
						map.put("parameterId", pid);
						result.add(map);
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error processing parameters", e);
		}

		return result;
	}
	

	@Override
    public Long getProfileIdByProfileName(String profileName) {
        return deviceProfileRepository.getProfileIdByProfileName(profileName);
    }
	
	
	@Override
    public List<Map<String, Object>> getParametersByDeviceId(Long deviceId) {
        List<Object[]> analogRaw = deviceProfileRepository.findAnalogParamsByDeviceId(deviceId);
        List<Map<String, Object>> allParams = new ArrayList<>();
        for (Object[] row : analogRaw) {
            Map<String, Object> analogMap = new LinkedHashMap<>();
            analogMap.put("parameterType", "Analog");
            analogMap.put("parameterId", Long.parseLong((String) row[0])); 
            analogMap.put("parameterName", row[1]);
            allParams.add(analogMap);
        }

        return allParams;
	
}
	
	
	@Override
    public List<Map<String, Object>> getParametersByDeviceIdMinAndMax(Long deviceId) {
        List<Object[]> analogRaw = deviceProfileRepository.findAnalogParamsByDeviceIdMinAndMax(deviceId);
        String deviceDataJson = historyRepository.findLatestDeviceDataByDeviceId(deviceId);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> deviceDataMap = new HashMap<>();
        try {
            deviceDataMap = objectMapper.readValue(deviceDataJson, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> analogValues = new HashMap<>();
        if (deviceDataMap.containsKey("Analog")) {
            Object analogObj = deviceDataMap.get("Analog");
            if (analogObj instanceof Map) {
                analogValues = (Map<String, Object>) analogObj;
            }
        }

        Map<String, String> analogKeyMap = new HashMap<>();
        analogKeyMap.put("Pressure", "1290902357");
        analogKeyMap.put("Temperature", "1290902193");
        analogKeyMap.put("RUNNING HRS", "1355411238");
        analogKeyMap.put("RUNNING Minutes", "1355411608");
        analogKeyMap.put("Odometer", "1271458761");


        List<Map<String, Object>> allParams = new ArrayList<>();

        for (Object[] row : analogRaw) {
            Map<String, Object> analogMap = new LinkedHashMap<>();
            String analogName = (String) row[1];

            analogMap.put("AnalogUnit", row[0]);
            analogMap.put("AnalogName", analogName);
            analogMap.put("maxValue", row[2]);
            analogMap.put("minValue", row[3]);

            String mappedKey = analogKeyMap.get(analogName);
            if (mappedKey != null && analogValues.containsKey(mappedKey)) {
                analogMap.put("currentValue", analogValues.get(mappedKey));
            } else {
                analogMap.put("currentValue", "N/A");
            }

            allParams.add(analogMap);
        }


        return allParams;
    }
	
	private static final Map<String, String> PARAMETER_KEY_MAP = new HashMap<>();
    static {
        PARAMETER_KEY_MAP.put("Temperature", "1290902193");
        PARAMETER_KEY_MAP.put("Pressure", "1290902357");
        PARAMETER_KEY_MAP.put("RUNNING HRS", "1355411238");
        PARAMETER_KEY_MAP.put("RUNNING Minutes", "1355411608");
        PARAMETER_KEY_MAP.put("Odometer", "1271458761");

    }

    public Map<String, Object> getParameterWithId(Long deviceId, String analogKey) {
        String jsonData = historyRepository.findLatestDeviceDataByDeviceId(deviceId);
        if (jsonData == null) return null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> analogMap = (Map<String, Object>) jsonMap.get("Analog");

            if (analogMap == null || !analogMap.containsKey(analogKey)) return null;

            Object value = analogMap.get(analogKey);

            List<AnalogParameter> parameters = deviceProfileRepository
                    .findAnalogParamsByDeviceIdMinAndMax(deviceId)
                    .stream()
                    .map(row -> new AnalogParameter(
                            (String) row[1],  // analogName
                            (String) row[0],  // analogUnit
                            (String) row[2],  // max
                            (String) row[3])  // min
                    ).collect(Collectors.toList());

            AnalogParameter matchedParam = parameters.stream()
                    .filter(p -> PARAMETER_KEY_MAP.get(p.getAnalogName()).equals(analogKey))
                    .findFirst()
                    .orElse(null);

            String name = matchedParam != null ? matchedParam.getAnalogName() : PARAMETER_KEY_MAP.get(analogKey);
            String unit = matchedParam != null ? matchedParam.getUnit() : null;
            String min = matchedParam != null ? matchedParam.getMin() : null;
            String max = matchedParam != null ? matchedParam.getMax() : null;

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("parameter", name);
            response.put("value", value);
            response.put("unit", unit);
            response.put("min", min);
            response.put("max", max);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public Long getPridFkByDeviceId(Long deviceId) {
        return devicemasterRepository.findPridFkByDeviceId(deviceId);

    }
    
    public List<Map<String, Object>> getParameterDataForMultipleKeys(Long deviceId, Timestamp sdate, Timestamp edate, List<String> parameterId, Integer limit) {
        List<Object[]> results;
        if (limit != null && limit > 0) {
            results = historyRepository.findDeviceDataByDeviceIdAndDateRangeWithLimit(deviceId, sdate, edate, limit);
        } else {
            results = historyRepository.findDeviceDataByDeviceIdAndDateRange(deviceId, sdate, edate);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> finalResponse = new ArrayList<>();

        for (String prmkey : parameterId) {
            List<Object[]> metaData = deviceProfileRepository.findAnalogParamsByDeviceIdAndPrmkey(deviceId, prmkey);
            String unit = null, min = null, max = null;
            if (!metaData.isEmpty()) {
                Object[] row = metaData.get(0);
                unit = (String) row[0];
                max = String.valueOf(row[2]);
                min = String.valueOf(row[3]);
            }

            List<Map<String, Object>> dataList = new ArrayList<>();

            for (Object[] row : results) {
                String json = (String) row[0];
                Timestamp deviceDate = (Timestamp) row[1];

                try {
                    Map<String, Object> jsonMap = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
                    Map<String, Object> analogMap = (Map<String, Object>) jsonMap.get("Analog");

                    if (analogMap != null && analogMap.containsKey(prmkey)) {
                        Object value = analogMap.get(prmkey);
                        if (value != null) {
                            Map<String, Object> dataEntry = new LinkedHashMap<>();
                            dataEntry.put("device_date", deviceDate.toString());
                            dataEntry.put("value", value);
                            dataList.add(dataEntry);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

         
            Map<String, Object> paramBlock = new LinkedHashMap<>();
            paramBlock.put("parameterId", prmkey);
            paramBlock.put("unit", unit);
            paramBlock.put("min", min);
            paramBlock.put("max", max);
            paramBlock.put("sdate", sdate.toString());
            paramBlock.put("edate", edate.toString());
            paramBlock.put("data", dataList);

            finalResponse.add(paramBlock);
        }

        return finalResponse;
    }

}
