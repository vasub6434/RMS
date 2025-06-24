package com.bonrix.dggenraterset.Service;

import com.bonrix.dggenraterset.DTO.ActiveAlertDTO;
import com.bonrix.dggenraterset.DTO.AlertCountBySiteDTO;
import com.bonrix.dggenraterset.DTO.AlertDurationDTO;
import com.bonrix.dggenraterset.DTO.AlertFrequency;
import com.bonrix.dggenraterset.DTO.AlertStatusDTO;
import com.bonrix.dggenraterset.DTO.AlertTrendDto;
import com.bonrix.dggenraterset.DTO.DeviceResponse;
import com.bonrix.dggenraterset.DTO.SiteStatsDTO;
import com.bonrix.dggenraterset.Model.AlearSummary;
import com.bonrix.dggenraterset.Model.DeviceProfile;
import com.bonrix.dggenraterset.Repository.GetDeviceWithParameter;
import com.bonrix.dggenraterset.Repository.RecentAlertRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.time.ZoneId;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AlertMessageSummaryService {

	@Autowired
	RecentAlertRepository repo;
	@Autowired
	GetDeviceWithParameter getDevice;

	public int getRecentAlertsCount(String fromdate, String todate, Long deviceId, String status, Long parameterId,
			Long managerId) {
		String statusStr = (status == null || "none".equalsIgnoreCase(status)) ? "none" : status.toLowerCase();

		Long deviceIdParam = (deviceId != null && deviceId > 0) ? deviceId : 0L;
		Long parameterIdParam = (parameterId != null && parameterId > 0) ? parameterId : 0L;

		return repo.countAlerts(fromdate, todate, deviceIdParam, statusStr, parameterIdParam, managerId);
	}

	public List<Object[]> getRecentAlertsPage(String fromdate, String todate, Long deviceId, String status, int size,
			int page, Long parameterId, Long managerId) {
		int offset = (page - 1) * size;

		String statusStr = (status == null || "none".equalsIgnoreCase(status)) ? "none" : status.toLowerCase();

		Long deviceIdParam = (deviceId != null && deviceId > 0) ? deviceId : 0L;
		Long parameterIdParam = (parameterId != null && parameterId > 0) ? parameterId : 0L;

		return repo.findAlerts(fromdate, todate, deviceIdParam, statusStr, parameterIdParam, managerId, size, offset);
	}

	public int getRecentAlertsCountForUser(String fromdate, String todate, Long deviceId, String status,
			Long parameterId, Long userId) {
		String statusStr = (status == null || "none".equalsIgnoreCase(status)) ? "none" : status.toLowerCase();
		Long deviceIdParam = (deviceId != null && deviceId > 0) ? deviceId : 0L;
		Long parameterIdParam = (parameterId != null && parameterId > 0) ? parameterId : 0L;

		return repo.countAlertsForUser(fromdate, todate, deviceIdParam, statusStr, parameterIdParam, userId);
	}

	public List<Object[]> getRecentAlertsPageForUser(String fromdate, String todate, Long deviceId, String status,
			int size, int page, Long parameterId, Long userId) {
		int offset = (page - 1) * size;
		String statusStr = (status == null || "none".equalsIgnoreCase(status)) ? "none" : status.toLowerCase();
		Long deviceIdParam = (deviceId != null && deviceId > 0) ? deviceId : 0L;
		Long parameterIdParam = (parameterId != null && parameterId > 0) ? parameterId : 0L;

		return repo.findAlertsForUser(fromdate, todate, deviceIdParam, statusStr, parameterIdParam, userId, size,
				offset);
	}

	public List<AlertStatusDTO> getAlertCountsByType(Date fromDate, Date toDate, Long managerId) {
		return repo.countAlertsByTypeBetweenDates(fromDate, toDate, managerId).stream()
				.map(row -> new AlertStatusDTO((String) row[0], ((Number) row[1]).longValue()))
				.collect(Collectors.toList());
	}

	public List<AlertStatusDTO> getAlertCountsByTypeForUser(Date fromDate, Date toDate, Long userId) {
		return repo.countAlertsByTypeForUser(fromDate, toDate, userId).stream()
				.map(row -> new AlertStatusDTO((String) row[0], ((Number) row[1]).longValue()))
				.collect(Collectors.toList());
	}

	public List<ActiveAlertDTO> getActiveAlerts(Date fromDate, Date toDate, Long managerId) {
		return repo.findActiveAlertsRaw(fromDate, toDate, managerId).stream()
				.map(row -> new ActiveAlertDTO(((Number) row[0]).longValue(), (String) row[1], (String) row[2],
						(String) row[3]))
				.collect(Collectors.toList());
	}

	public List<ActiveAlertDTO> getActiveAlertsForUser(Date fromDate, Date toDate, Long userId) {
		return repo.findActiveAlertsForUser(fromDate, toDate, userId).stream()
				.map(row -> new ActiveAlertDTO(((Number) row[0]).longValue(), (String) row[1], (String) row[2],
						(String) row[3]))
				.collect(Collectors.toList());
	}

	public List<AlertCountBySiteDTO> getAlertsBySiteAndType1(Date fromDate, Date toDate, Long managerId) {
		List<Object[]> raw = repo.countBySiteAndType(fromDate, toDate, managerId);

		return raw.stream()
				.map(row -> new AlertCountBySiteDTO((String) row[0], (String) row[1], ((Number) row[2]).longValue()))
				.collect(Collectors.toList());
	}

	public List<AlertCountBySiteDTO> getAlertsBySiteAndType1ForUser(Date fromDate, Date toDate, Long uid) {
		List<Object[]> raw = repo.countBySiteAndTypeForUser(fromDate, toDate, uid);

		return raw.stream()
				.map(row -> new AlertCountBySiteDTO((String) row[0], (String) row[1], ((Number) row[2]).longValue()))
				.collect(Collectors.toList());
	}

	public List<AlertDurationDTO> getAvgDurationByAlertType(Date fromDate, Date toDate, Long managerId) {

		List<Object[]> raw = repo.findAvgDurationRawBetweenDates(fromDate, toDate, managerId);

		return raw.stream().map(r -> {
			String alertType = (String) r[0];
			double avgMinutes = ((Number) r[1]).doubleValue();
			int roundedAvg = (int) Math.round(avgMinutes);
			return new AlertDurationDTO(alertType, roundedAvg);
		}).collect(Collectors.toList());
	}

	public List<AlertDurationDTO> getAvgDurationByAlertTypeForUser(Date fromDate, Date toDate, Long uid) {

		List<Object[]> raw = repo.findAvgDurationRawBetweenDatesForUser(fromDate, toDate, uid);

		return raw.stream().map(r -> {
			String alertType = (String) r[0];
			double avgMinutes = ((Number) r[1]).doubleValue();
			int roundedAvg = (int) Math.round(avgMinutes);
			return new AlertDurationDTO(alertType, roundedAvg);
		}).collect(Collectors.toList());
	}

	public List<SiteStatsDTO> top5SitesLast24h(Long managerId) {
		return repo.findTop5Native(managerId).stream().map(row -> new SiteStatsDTO((String) row[0], // site_name
				((Number) row[1]).intValue())).collect(Collectors.toList());
	}

	public List<SiteStatsDTO> top5SitesLast24hForUser(Long uid) {
		return repo.findTop5SitesForUser(uid).stream().map(row -> new SiteStatsDTO((String) row[0], // site_name
				((Number) row[1]).intValue())).collect(Collectors.toList());
	}

	public List<AlertStatusDTO> top5AlertsLast24hForUser(Long uid) {
		return repo.findTopAlertTypesForUser(uid).stream().map(row -> new AlertStatusDTO((String) row[0], // alertName
				((Number) row[1]).longValue())).collect(Collectors.toList());
	}

	public List<AlertStatusDTO> top5AlertsLast24h(Long managerId) {
		return repo.findTopAlertTypesNative(managerId).stream().map(row -> new AlertStatusDTO((String) row[0], // alertName
				((Number) row[1]).longValue())).collect(Collectors.toList());
	}

	public List<AlertStatusDTO> getActiveAlertsByType(Date fromDate, Date toDate, Long managerId) {

		return repo.findActiveAlertCountsByTypeBetween(fromDate, toDate, managerId).stream()
				.map(r -> new AlertStatusDTO((String) r[0], ((Number) r[1]).longValue())).collect(Collectors.toList());
	}

	public List<AlertStatusDTO> getActiveAlertsByTypeForUser(Date fromDate, Date toDate, Long uid) {

		return repo.findActiveAlertCountsByTypeForUserBetween(fromDate, toDate, uid).stream()
				.map(r -> new AlertStatusDTO((String) r[0], ((Number) r[1]).longValue())).collect(Collectors.toList());
	}

	public List<AlertTrendDto> getTrend(Date from, Date to, Long managerId) {
		List<Object[]> rows = repo.countAlertsPerDayBetween(from, to, managerId);

		Map<LocalDate, Long> counts = new HashMap<>();
		for (Object[] row : rows) {
			java.sql.Date sqlDate = (java.sql.Date) row[0];
			long cnt = ((Number) row[1]).longValue();
			counts.put(sqlDate.toLocalDate(), cnt);
		}

		List<AlertTrendDto> result = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(to);

		SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);

		while (!cal.after(endCal)) {
			Date d = cal.getTime();
			LocalDate current = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			String dayAbbrev = sdf.format(d);
			long cnt = counts.getOrDefault(current, 0L);

			result.add(new AlertTrendDto(dayAbbrev, cnt));
			cal.add(Calendar.DATE, 1);
		}

		return result;
	}

	public List<AlertTrendDto> getTrendForUser(Date from, Date to, Long uid) {
		List<Object[]> rows = repo.countAlertsPerDayForUserBetween(from, to, uid);

		Map<LocalDate, Long> counts = new HashMap<>();
		for (Object[] row : rows) {
			java.sql.Date sqlDate = (java.sql.Date) row[0];
			long cnt = ((Number) row[1]).longValue();
			counts.put(sqlDate.toLocalDate(), cnt);
		}

		List<AlertTrendDto> result = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(to);

		SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);

		while (!cal.after(endCal)) {
			Date d = cal.getTime();
			LocalDate current = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			String dayAbbrev = sdf.format(d);
			long cnt = counts.getOrDefault(current, 0L);

			result.add(new AlertTrendDto(dayAbbrev, cnt));
			cal.add(Calendar.DATE, 1);
		}

		return result;
	}

	private final ObjectMapper mapper = new ObjectMapper();

	public List<Map<String, Object>> getDeviceResponseGrouped(Long deviceId, List<Object[]> profileList) {

		List<String> profiles = profileList.stream().map(row -> row[0] == null ? null : row[0].toString())
				.filter(Objects::nonNull).collect(Collectors.toList());

		List<Object[]> rows = (deviceId != null) ? getDevice.getDeviceData(deviceId)
				: getDevice.getAllDeviceData(profiles);

		List<Map<String, Object>> flat = new ArrayList<>();
		Set<String> seen = new HashSet<>();

		for (Object[] row : rows) {
			String profName = (String) row[2];
			String jsonParam = (String) row[3];

			try {
				JsonNode node = mapper.readTree(jsonParam);
				long pid = node.path("parameterId").asLong();
				String pnam = node.path("parametername").asText();
				String key = profName + "|" + pid;
				if (seen.add(key)) {
					Map<String, Object> rec = new LinkedHashMap<>();
					rec.put("parameterId", pid);
					rec.put("parametername", pnam);
					rec.put("profileName", profName);
					flat.add(rec);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return flat;
	}

	public List<Map<String, Object>> getLast30DaysTrend(Long managerId) {
		List<Object[]> rawData = repo.findLast30DaysAvgDurationInMinutes(managerId);

		Map<LocalDate, Double> dailyAverages = rawData.stream().collect(Collectors.toMap(
				row -> ((Timestamp) row[0]).toLocalDateTime().toLocalDate(), row -> ((Number) row[1]).doubleValue()));

		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(29);

		List<Map<String, Object>> result = new ArrayList<>();
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			Map<String, Object> dayData = new HashMap<>();
			dayData.put("date", date.toString());
			dayData.put("avg_resolution_time", dailyAverages.getOrDefault(date, 0.0));
			result.add(dayData);
		}

		return result;
	}

	public List<Map<String, Object>> getLast30DaysTrendForUSer(Long uid) {
		List<Object[]> rawData = repo.findLast30DaysAvgDurationForUserInMinutes(uid);

		Map<LocalDate, Double> dailyAverages = rawData.stream().collect(Collectors.toMap(
				row -> ((Timestamp) row[0]).toLocalDateTime().toLocalDate(), row -> ((Number) row[1]).doubleValue()));

		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(29);

		List<Map<String, Object>> result = new ArrayList<>();
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			Map<String, Object> dayData = new HashMap<>();
			dayData.put("date", date.toString());
			dayData.put("avg_resolution_time", dailyAverages.getOrDefault(date, 0.0));
			result.add(dayData);
		}

		return result;
	}

	public List<AlertFrequency> getLast7DaysFrequency(Long managerId) {

		Timestamp cutoff = Timestamp.valueOf(LocalDate.now().minusDays(6).atStartOfDay());

		List<Object[]> raw = repo.findRawFrequencySince(cutoff, managerId);

		Map<String, Map<Integer, Long>> byDay = new LinkedHashMap<>();
		for (int offset = 6; offset >= 0; offset--) {
			LocalDate date = LocalDate.now().minusDays(offset);
			String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

			Map<Integer, Long> hours = new LinkedHashMap<>();
			for (int h = 0; h < 24; h += 2) {
				hours.put(h, 0L);
			}
			byDay.put(dayName, hours);
		}

		for (Object[] row : raw) {
			String day = (String) row[0];
			int hour = ((Number) row[1]).intValue();
			long cnt = ((Number) row[2]).longValue();

			Map<Integer, Long> hours = byDay.get(day);
			if (hours != null) {
				hours.put(hour, cnt);
			}
		}

		List<AlertFrequency> result = new ArrayList<>();
		byDay.forEach((day, hours) -> {
			hours.forEach((hour, cnt) -> {
				result.add(new AlertFrequency(day, hour, cnt));
			});
		});

		return result;
	}

	public List<AlertFrequency> getLast7DaysFrequencyForUser(Long uid) {

		Timestamp cutoff = Timestamp.valueOf(LocalDate.now().minusDays(6).atStartOfDay());

		List<Object[]> raw = repo.findRawFrequencySinceForUser(cutoff, uid);

		Map<String, Map<Integer, Long>> byDay = new LinkedHashMap<>();
		for (int offset = 6; offset >= 0; offset--) {
			LocalDate date = LocalDate.now().minusDays(offset);
			String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

			Map<Integer, Long> hours = new LinkedHashMap<>();
			for (int h = 0; h < 24; h += 2) {
				hours.put(h, 0L);
			}
			byDay.put(dayName, hours);
		}

		for (Object[] row : raw) {
			String day = (String) row[0];
			int hour = ((Number) row[1]).intValue();
			long cnt = ((Number) row[2]).longValue();

			Map<Integer, Long> hours = byDay.get(day);
			if (hours != null) {
				hours.put(hour, cnt);
			}
		}

		List<AlertFrequency> result = new ArrayList<>();
		byDay.forEach((day, hours) -> {
			hours.forEach((hour, cnt) -> {
				result.add(new AlertFrequency(day, hour, cnt));
			});
		});

		return result;
	}

}
