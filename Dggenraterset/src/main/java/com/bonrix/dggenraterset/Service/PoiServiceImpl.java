package com.bonrix.dggenraterset.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonrix.common.utils.Distance;
import com.bonrix.dggenraterset.Controller.FenceController;
import com.bonrix.dggenraterset.Model.History;
import com.bonrix.dggenraterset.Model.PoiData;
import com.bonrix.dggenraterset.Repository.HistoryRepository;
import com.bonrix.dggenraterset.Repository.PoiRepository;
import com.google.gson.Gson;

@Service
public class PoiServiceImpl implements PoiService {

	@Autowired
	private PoiRepository poiRepository;

	@Autowired
	private HistoryRepository historyRepository;
	
	private static final Logger log = Logger.getLogger(PoiServiceImpl.class);

	@Override
	public PoiData addPoi(PoiData poiData) {
		return poiRepository.save(poiData);
	}

	@Override
	public List<PoiData> getAllPoisByManagerId(Long managerId) {
		return poiRepository.findAllByManagerId(managerId);
	}

	@Override
	public PoiData updatePoi(Long id, Long managerId, PoiData updatedPoi) {
		PoiData existing = poiRepository.findPoiByIdAndManagerId(id, managerId);
		if (existing == null) {
			return null;
		}
		existing.setAddress(updatedPoi.getAddress());
		existing.setLongitude(updatedPoi.getLongitude());
		existing.setLatitude(updatedPoi.getLatitude());
		existing.setLocation(updatedPoi.getLocation());
		existing.setStatus(updatedPoi.getStatus());
		return poiRepository.save(existing);
	}

	@Override
	public boolean deletePoi(Long id, Long managerId) {
		int deleted = poiRepository.deletePoiByIdAndManagerId(id, managerId);
		return deleted > 0;
	}
	
	@Override
	public Object getPoiHistoryReport(Long deviceId, Long managerId, String startDate, String endDate, String poiName,
			int radius) {
		Gson gson = new Gson();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> maplist = new ArrayList<>();

		try {
			Distance distanceUtil = new Distance();

			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);

			log.info("Ravi :- history start" + start + " end:::" + end);
			List<History> historyList = historyRepository.findByDeviceIdAndDateRange(deviceId, start, end);
			List<PoiData> poiList = poiRepository.getPoiByManager(managerId, poiName);
			log.info("Ravi :- history Size" + historyList.size());
			log.info("Ravi :- poi size" + poiList.size());

			if (poiList.isEmpty() || historyList.isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("status", false);
				response.put("message", "No data found");
				response.put("data", Collections.emptyList());
				return gson.toJson(response);
			}

			for (PoiData poi : poiList) {
				boolean insidePoi = false;
				Map<String, Object> entry = null;

				log.info("--- Checking POI: " + poi.getLocation() + " ---");

				for (History history : historyList) {
					Map<String, Object> gps = history.getGpsdata();
					if (gps == null || gps.isEmpty())
						continue;

					double lat = gps.get("latitude") != null ? Double.parseDouble(gps.get("latitude").toString()) : 0.0;
					double lng = gps.get("longitude") != null ? Double.parseDouble(gps.get("longitude").toString())
							: 0.0;

					double dist = distanceUtil.getDistanceFromPosition(
							poi.getLatitude() != null ? poi.getLatitude() : 0.0,
							poi.getLongitude() != null ? poi.getLongitude() : 0.0, lat, lng) * 1000;

//	                System.out.println("POI Latitude: " + poi.getLatitude() +
//	                        ", POI Longitude: " + poi.getLongitude() +
//	                        ", Current Latitude: " + lat +
//	                        ", Current Longitude: " + lng +
//	                        ", Distance: " + dist + " meters");

					if (dist < radius && !insidePoi) {
						entry = new HashMap<>();
						entry.put("startTime", sdf.format(history.getDeviceDate()));
						entry.put("location", poi.getLocation());
						insidePoi = true;

						System.out.println(">> Entered POI at: " + sdf.format(history.getDeviceDate()));

					} else if (dist >= radius && insidePoi) {
						if (entry != null) {
							Date startTime = sdf.parse(entry.get("startTime").toString());
							Date endTime = history.getDeviceDate();
						//	long holdTimeSeconds = (endTime.getTime() - startTime.getTime()) / 1000;

							entry.put("endTime", sdf.format(endTime));
//	                          entry.put("holdTime", holdTimeSeconds);
							entry.put("holdTime", formatDuration(startTime, endTime));

							maplist.add(entry);

							System.out.println("<< Exited POI at: " + sdf.format(endTime));
						}
						insidePoi = false;
					}
				}

				if (insidePoi && entry != null) {
					Date startTime = sdf.parse(entry.get("startTime").toString());
					Date endTime = historyList.get(historyList.size() - 1).getDeviceDate();
					long holdTimeSeconds = (endTime.getTime() - startTime.getTime()) / 1000;

					entry.put("endTime", sdf.format(endTime));
//	                entry.put("holdTime", holdTimeSeconds);
					entry.put("holdTime", formatDuration(startTime, endTime));

					maplist.add(entry);

					log.debug("Still in POI at end of period, hold time: " + holdTimeSeconds + " seconds");
				}
			}

			Map<String, Object> successResponse = new HashMap<>();
			successResponse.put("status", true);
			successResponse.put("message", "POI history data fetched successfully");
			successResponse.put("data", maplist);
			return gson.toJson(successResponse);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", false);
			errorResponse.put("message", "Error generating report: " + e.getMessage());
			errorResponse.put("data", Collections.emptyList());
			return gson.toJson(errorResponse);
		}
	}

	private String formatDuration(Date start, Date end) {
		long durationMillis = end.getTime() - start.getTime();
		long hours = durationMillis / (1000 * 60 * 60);
		long minutes = (durationMillis / (1000 * 60)) % 60;
		long seconds = (durationMillis / 1000) % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

}
