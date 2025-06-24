package com.bonrix.dggenraterset.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.dggenraterset.DTO.AlertResponse;
import com.bonrix.dggenraterset.DTO.AlertSummary;
import com.bonrix.dggenraterset.DTO.AlertTimelineResponse;
import com.bonrix.dggenraterset.DTO.AlertType;
import com.bonrix.dggenraterset.DTO.AlertTypeDTO;
import com.bonrix.dggenraterset.DTO.AlertTypeDTOLive;
import com.bonrix.dggenraterset.Model.AlertMessages;
import com.bonrix.dggenraterset.Repository.AlertMsgRepo;
import com.bonrix.dggenraterset.Repository.DashboardAPIRepository;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;

@Service
public class DashboardAPIServiceImpl implements DashboardAPIService {
	 
	@Autowired
	DashboardAPIRepository repo;
	
	@Autowired
	AlertMsgRepo alertmsg;
	
	 @Autowired
	  DeviceProfileRepository DpService;
	 
	 @Autowired
		DynamicQueryService deviceQueryService;
  
	@Override
	public List<Object[]> geyMyProfileList(Iterable<Long> deviceIds) {
		return repo.geyMyProfileList(deviceIds);
	}

	@Override
	public List<Object[]> getHisrotyAnalogByNo(long no) {
		return repo.getHisrotyAnalogByNo(no);
	}

	@Override
	public List<Object[]> getHisrotyDigitalByNo(long no) {
		return repo.getHisrotyDigitalByNo(no);
	}

	@Override
	public List<Object[]> GetDeviceParameterRecords(Long deviceId,String prmname) {
		return repo.GetDeviceParameterRecords(deviceId, prmname);
	}
	//new code
	@Override
	public List<Object[]> GetDeviceParameterRecords(Long deviceId, String prmname, int limit) {
	    if (limit <= 0) {
	        return repo.GetAllDeviceParameterRecords(deviceId, prmname);
	    }
	    return repo.GetDeviceParameterRecords(deviceId, prmname, limit);
	}
	
	
	@Override
    public AlertResponse getAlerts(Date fromDate, Date toDate, Long managerId,Long profileId) {
        List<AlertMessages> alerts = alertmsg.findAlertsByManageridAndEntrytimeBetweenAndProfileId(managerId, fromDate, toDate,profileId);

        // Calculate metrics
        int activeAlerts = calculateActiveAlerts(alerts);
        int criticalAlerts = calculateCriticalAlerts(alerts);
        int mtr = calculateMTR(alerts);
        int resolvedToday = calculateResolvedToday(alerts);

        // Return the response
        return new AlertResponse(true, alerts.size(), new AlertResponse.Metrics(activeAlerts, criticalAlerts, mtr, resolvedToday), alerts);
    }

    private int calculateActiveAlerts(List<AlertMessages> alerts) {
        // Group alerts by deviceid and keep the latest entry for each device
        Map<Long, AlertMessages> deviceMap = new HashMap<>(); // Use Long for deviceid

        for (AlertMessages alert : alerts) {
            Long deviceId = alert.getDeviceid(); // Use Long for deviceid
            boolean isStatusClear = alert.getMessage().contains("Status CLEAR") || alert.getMessage().contains("Status INACTIVE");

            // Update the map with the latest alert for each device
            if (!deviceMap.containsKey(deviceId)) {
                deviceMap.put(deviceId, alert);
            } else {
                AlertMessages existingAlert = deviceMap.get(deviceId);
                if (alert.getEntrytime().after(existingAlert.getEntrytime())) {
                    deviceMap.put(deviceId, alert);
                }
            }
        }

        // Count alerts that are not cleared
        return (int) deviceMap.values().stream()
                .filter(alert -> !(alert.getMessage().contains("Status CLEAR") || alert.getMessage().contains("Status INACTIVE")))
                .count();
    }

    private int calculateCriticalAlerts(List<AlertMessages> alerts) {
        List<String> criticalKeywords = Arrays.asList("ACMAINS_FAIL", "DG Fault", "Battery Low");

        // Group alerts by deviceid and keep the latest entry for each device
        Map<Long, AlertMessages> deviceMap = new HashMap<>(); // Use Long for deviceid

        for (AlertMessages alert : alerts) {
            Long deviceId = alert.getDeviceid(); // Use Long for deviceid
            boolean isCritical = criticalKeywords.stream().anyMatch(keyword -> alert.getMessage().contains(keyword));
            boolean isStatusClear = alert.getMessage().contains("Status CLEAR") || alert.getMessage().contains("Status INACTIVE");

            // Update the map with the latest critical alert for each device
            if (isCritical) {
                if (!deviceMap.containsKey(deviceId)) {
                    deviceMap.put(deviceId, alert);
                } else {
                    AlertMessages existingAlert = deviceMap.get(deviceId);
                    if (alert.getEntrytime().after(existingAlert.getEntrytime())) {
                        deviceMap.put(deviceId, alert);
                    }
                }
            }
        }

        // Count critical alerts that are not cleared
        return (int) deviceMap.values().stream()
                .filter(alert -> !(alert.getMessage().contains("Status CLEAR") || alert.getMessage().contains("Status INACTIVE")))
                .count();
    }

    private int calculateMTR(List<AlertMessages> alerts) {
        Map<String, Map<String, Date>> alertPairs = new HashMap<>();

        // Pair ACTIVE and CLEAR alerts for each device and alert type
        for (AlertMessages alert : alerts) {
            Long deviceId = alert.getDeviceid(); // Use Long for deviceid
            String alertType = extractAlertType(alert.getMessage());

            if (alertType == null) continue;

            String key = deviceId + "_" + alertType; // Concatenate Long and String
            boolean isStatusClear = alert.getMessage().contains("Status CLEAR") || alert.getMessage().contains("Status INACTIVE");
            boolean isStatusActive = alert.getMessage().contains("Status ACTIVE");

            alertPairs.putIfAbsent(key, new HashMap<>());

            if (isStatusActive) {
                alertPairs.get(key).put("active", alert.getEntrytime());
            }
            if (isStatusClear) {
                alertPairs.get(key).put("clear", alert.getEntrytime());
            }
        }

        // Calculate resolution times
        List<Long> resolutionTimes = new ArrayList<>();
        for (Map<String, Date> pair : alertPairs.values()) {
            Date activeTime = pair.get("active");
            Date clearTime = pair.get("clear");

            if (activeTime != null && clearTime != null && clearTime.after(activeTime)) {
                long resolutionTime = (clearTime.getTime() - activeTime.getTime()) / (1000 * 60); // Convert to minutes
                resolutionTimes.add(resolutionTime);
            }
        }

        // Calculate average resolution time (MTR)
        if (resolutionTimes.isEmpty()) {
            return 0;
        }
        return (int) resolutionTimes.stream().mapToLong(Long::longValue).average().orElse(0);
    }

    private int calculateResolvedToday(List<AlertMessages> alerts) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date todayStart = today.getTime();

        Calendar tomorrow = (Calendar) today.clone();
        tomorrow.add(Calendar.DATE, 1);
        Date todayEnd = tomorrow.getTime();

        return (int) alerts.stream()
                .filter(alert -> alert.getEntrytime().after(todayStart) && alert.getEntrytime().before(todayEnd))
                .filter(alert -> alert.getMessage().contains("Status CLEAR") || alert.getMessage().contains("Status INACTIVE"))
                .count();
    }

    private String extractAlertType(String message) {
        if (message == null || !message.contains("Alarm Name")) {
            return null;
        }
        String[] parts = message.split(":");
        if (parts.length > 0) {
            return parts[0].replace("Alarm Name", "").trim();
        }
        return null;
    }

    @Override
    public AlertSummary getAlertSummary(Long managerId, Date fromDate, Date toDate) {
        // Fetch data from the repository
        List<Object[]> result = repo.findAlertSummaryByManageridAndEntrytimeBetween1(managerId, fromDate, toDate);

        // Log the result for debugging
        System.out.println("Query Result: " + result);

        // Check if the result is not empty
        if (result.isEmpty()) {
            throw new RuntimeException("No data found for the given parameters");
        }

        // Extract the first row of the result
        Object[] row = result.get(0);

        // Safely convert BigInteger to Long
        Long totalAlerts = ((BigInteger) row[0]).longValue();  // Convert from BigInteger to Long
        Long activeCount = ((BigInteger) row[1]).longValue();
        Long clearCount = ((BigInteger) row[2]).longValue();
        Long criticalCount = ((BigInteger) row[3]).longValue();

        // Return the mapped result in an AlertSummary DTO
        return new AlertSummary(totalAlerts, activeCount, clearCount, criticalCount);
    }

    @Override
    public AlertTimelineResponse getAlertTimeline(Long managerId, Date fromDate, Date toDate, Long profileId,String interval) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (fromDate == null || toDate == null) {
            toDate = new Date();
            fromDate = subtractHours(toDate, 24);
        }

        // Log the parameters for debugging
       // System.out.println("From Date: " + dateFormat.format(fromDate));
        //System.out.println("To Date: " + dateFormat.format(toDate));
        //System.out.println("Manager ID: " + managerId);

        List<Object[]> timelineData = repo.getAlertTimeline(managerId, fromDate, toDate,profileId);
        
        
        if (timelineData == null || timelineData.isEmpty()) {
            return new AlertTimelineResponse(
                    true,
                    new AlertTimelineResponse.Period(dateFormat.format(fromDate), dateFormat.format(toDate)),
                    new ArrayList<>(),
                    new AlertTimelineResponse.Summary(0, 0)
            );
        }

        // Further log the mapping process if needed
        List<AlertTimelineResponse.TimelineData> mappedData = timelineData.stream()
                .map(row -> {
                    AlertTimelineResponse.TimelineData data = new AlertTimelineResponse.TimelineData(
                            dateFormat.format((Date) row[0]),
                            ((Number) row[1]).intValue(),
                            ((Number) row[2]).intValue()
                    );
                    return data;
                })
                .collect(Collectors.toList());

        int totalNewAlerts = mappedData.stream().mapToInt(AlertTimelineResponse.TimelineData::getNew_alerts).sum();
        int totalResolvedAlerts = mappedData.stream().mapToInt(AlertTimelineResponse.TimelineData::getResolved_alerts).sum();

        return new AlertTimelineResponse(
                true,
                new AlertTimelineResponse.Period(dateFormat.format(fromDate), dateFormat.format(toDate)),
                mappedData,
                new AlertTimelineResponse.Summary(totalNewAlerts, totalResolvedAlerts)
        );
    }



    // Helper method to subtract hours from a given date
    private Date subtractHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -hours); // Subtract the specified number of hours
        return calendar.getTime();
    }
  
   /* @Override
    public AlertTypeDTO getAlertData(Date fromDate, Date toDate, Long managerId) {
        List<Object[]> result = repo.getAlertCountsByType(managerId, fromDate, toDate);

        // Safe conversion from BigInteger to int
        List<AlertType> alertTypes = result.stream()
            .map(row -> new AlertType(
                (String) row[0], 
                ((Number) row[1]).intValue()
            ))
            .collect(Collectors.toList());

        int totalAlerts = alertTypes.stream()
            .mapToInt(AlertType::getCount)
            .sum();

        alertTypes.forEach(alert -> {
            double percentage = totalAlerts > 0 ? 
                ((double) alert.getCount() / totalAlerts) * 100 : 0;
            alert.setPercentage(Math.round(percentage * 10) / 10.0);
        });

        return new AlertTypeDTO(true, fromDate, toDate, totalAlerts, alertTypes);
    }*/


	@Override
	public BigDecimal getAnalogDiffrence(Long deviceId, String paramId) {
		return repo.getAnalogDiffrence(deviceId, paramId);
	}
	
	 @Override
	    public AlertTypeDTO getAlertData(Date fromDate, Date toDate, Long managerId, Long profileId) {
	        List<String> parameterNameList = Arrays.asList(DpService.getparameterNameByProfile(profileId));
	        
	        List<Object[]> result = deviceQueryService.getAlertCountsByTypeDynamic(managerId, fromDate, toDate, parameterNameList);

	        List<AlertType> alertTypes = result.stream()
	            .map(row -> new AlertType((String) row[0], ((Number) row[1]).intValue()))
	            .collect(Collectors.toList());

	        int totalAlerts = alertTypes.stream().mapToInt(AlertType::getCount).sum();

	        alertTypes.forEach(alert -> {
	            double percentage = totalAlerts > 0 ? ((double) alert.getCount() / totalAlerts) * 100 : 0;
	            alert.setPercentage(Math.round(percentage * 10) / 10.0);
	        });  

	        return new AlertTypeDTO(true, fromDate, toDate, profileId, totalAlerts, alertTypes);
	    }
	 
	 public AlertTypeDTOLive getAlertDataLive(Long managerId, Long profileId) {
	        String[] paramIdArray   = DpService.getparameterIdByProfile(profileId);
	        String[] paramNameArray = DpService.getparameterNameByProfile(profileId);

	        List<String> parameterIds   = Arrays.asList(paramIdArray);
	        List<String> parameterNames = Arrays.asList(paramNameArray);

	        List<Object[]> results =
	            deviceQueryService.getAlertCountsByTypeDynamicLive(managerId, parameterIds);

	        List<AlertType> alertTypes = new ArrayList<>();
	        int totalAlerts = 0;

	        for (Object[] row : results) {
	            for (int i = 0; i < parameterIds.size(); i++) {
	                Integer val = (Integer) row[i + 2];
	                if (val != null && val == 0) {
	                    String name = parameterNames.get(i);

	                    Optional<AlertType> existing =
	                        alertTypes.stream()
	                                  .filter(a -> a.getAlertType().equals(name))
	                                  .findFirst();

	                    if (existing.isPresent()) {
	                        existing.get().setCount(existing.get().getCount() + 1);
	                    } else {
	                        alertTypes.add(new AlertType(name, 1));
	                    }
	                    totalAlerts++;
	                }
	            }
	        }

	         for (AlertType at : alertTypes) {
	            double pct = totalAlerts > 0
	                       ? ((double) at.getCount() / totalAlerts) * 100
	                       : 0;
	            at.setPercentage(Math.round(pct * 10) / 10.0);
	        }
	        alertTypes.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

	        return new AlertTypeDTOLive(true, profileId, totalAlerts, alertTypes);
	    }
}

