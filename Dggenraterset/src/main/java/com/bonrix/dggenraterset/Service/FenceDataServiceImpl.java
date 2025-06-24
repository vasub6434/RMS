package com.bonrix.dggenraterset.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bonrix.dggenraterset.Model.FenceData;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Repository.FenceRepository;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Service.FenceDataService;

import java.text.SimpleDateFormat;
import com.bonrix.common.utils.FenceUtils;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class FenceDataServiceImpl implements FenceDataService {

	@Autowired
	private FenceRepository fenceDataRepository;

	@Autowired
	private HistoryRepository historyRepository;
	private static final Logger log = Logger.getLogger(FenceDataServiceImpl.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public FenceData addFence(FenceData fenceData) {
		return fenceDataRepository.save(fenceData);
	}

	@Override
	public List<FenceData> getAllFencesByManagerId(Long managerId) {
		return fenceDataRepository.findAllByManagerId(managerId);
	}

	@Override
	public FenceData updateFence(Long id, Long managerId, FenceData updatedData) {
		FenceData existing = fenceDataRepository.findFenceByIdAndManagerId(id, managerId);
		if (existing == null) {
			return null;
		}
		existing.setFencename(updatedData.getFencename());
		existing.setFencetype(updatedData.getFencetype());
		existing.setFencevalue(updatedData.getFencevalue());
		existing.setStatus(updatedData.getStatus());
		return fenceDataRepository.save(existing);
	}

	@Override
	public boolean deleteFenceById(Long id, Long managerId) {
		int result = fenceDataRepository.deleteFenceByFenceidAndManagerId(id, managerId);
		return result > 0;
	}

	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<Map<String, Object>> getGeofenceReport(Long managerId, Long deviceId, Date startDateStr,
			Date endDateStr) {
		List<Map<String, Object>> result = new ArrayList<>();

		

		List<FenceData> fences = fenceDataRepository.findAllByManagerId(managerId);
		if (fences.isEmpty()) {
			return Collections.emptyList();
		}

		List<History> historyList = historyRepository.findByDeviceIdAndDateRange(deviceId, startDateStr, endDateStr);
		if (historyList.isEmpty()) {
			return Collections.emptyList();
		}
		log.info("Ravi :-  fence list is : " + fences.size());
		log.info("Ravi :-  history list is : " + historyList.size());
		for (FenceData fence : fences) {
			Date insideFenceStart = null;
			boolean wasInside = false;

			for (History history : historyList) {
				Map<String, Object> gps = history.getGpsdata();
				if (gps == null || !gps.containsKey("latitude") || !gps.containsKey("longitude"))
					continue;

				double lat = Double.parseDouble(gps.get("latitude").toString());
				double lon = Double.parseDouble(gps.get("longitude").toString());
				Date currentTime = history.getDeviceDate();

				boolean isInside = false;

				if ("circle".equalsIgnoreCase(fence.getFencetype())) {
					String[] parts = fence.getFencevalue().split(",");
					if (parts.length != 3)
						continue;

					double centerLat = Double.parseDouble(parts[0].trim());
					double centerLon = Double.parseDouble(parts[1].trim());
					double radius = Double.parseDouble(parts[2].trim());
					isInside = FenceUtils.isPointInsideCircle(lat, lon, centerLat, centerLon, radius);
				} else if ("polygon".equalsIgnoreCase(fence.getFencetype())) {
					String[] points = fence.getFencevalue().split(";");
					List<double[]> polygon = Arrays.stream(points).map(p -> {
						String[] coords = p.trim().split(",");
						return new double[] { Double.parseDouble(coords[0]), Double.parseDouble(coords[1]) };
					}).collect(Collectors.toList());
					if (!polygon.isEmpty()) {
						polygon.add(polygon.get(0));
						isInside = FenceUtils.isPointInsidePolygon(lat, lon, polygon);
					}
				}

				if (isInside && !wasInside) {
					insideFenceStart = currentTime;
				}

				if (!isInside && wasInside && insideFenceStart != null) {
					long diffMillis = currentTime.getTime() - insideFenceStart.getTime();
					String diffStr = formatDuration(diffMillis);

					Map<String, Object> period = new HashMap<>();
					period.put("startTime", sdfDateTime.format(insideFenceStart));
					period.put("endTime", sdfDateTime.format(currentTime));
					period.put("differenceTime", diffStr);
					period.put("deviceId", deviceId);
					period.put("fenceId", fence.getFenceid());
					period.put("fenceName", fence.getFencename());

					result.add(period);
					insideFenceStart = null;
				}

				wasInside = isInside;
			}

			if (wasInside && insideFenceStart != null) {
				Date lastTime = historyList.get(historyList.size() - 1).getDeviceDate();
				long diffMillis = lastTime.getTime() - insideFenceStart.getTime();
				String diffStr = formatDuration(diffMillis);

				Map<String, Object> period = new HashMap<>();
				period.put("startTime", sdfDateTime.format(insideFenceStart));
				period.put("endTime", sdfDateTime.format(lastTime));
				period.put("differenceTime", diffStr);
				period.put("deviceId", deviceId);
				period.put("fenceId", fence.getFenceid());
				period.put("fenceName", fence.getFencename());

				result.add(period);
			}
		}

		return result;
	}

	private String formatDuration(long millis) {
		long seconds = millis / 1000;
		long hh = seconds / 3600;
		long mm = (seconds % 3600) / 60;
		long ss = seconds % 60;
		return String.format("%02d:%02d:%02d", hh, mm, ss);
	}

	public List<Map<String, Object>> getSurroundingLatLong(Long deviceId, Date date) {
		List<History> records = historyRepository.findSurroundingLatLong(deviceId, date);
		List<Map<String, Object>> response = new ArrayList<>();

		for (History record : records) {
			log.info("Ravi :- rows from the history " + record.toString());
			Map<String, Object> gps = record.getGpsdata();
			if (gps != null && gps.containsKey("latitude") && gps.containsKey("longitude")) {
				Map<String, Object> data = new HashMap<>();
				data.put("device_date", sdf.format(record.getDeviceDate()));
				data.put("latitude", gps.get("latitude"));
				data.put("longitude", gps.get("longitude"));
				response.add(data);
			}
		}

		return response;
	}

}
