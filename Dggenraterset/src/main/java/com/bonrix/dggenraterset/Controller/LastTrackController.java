package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.BonrixUser;
import com.bonrix.dggenraterset.Model.Dashboarddetails;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.SpringException;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.DashboardServices;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.jobs.MyAlerts;

@CrossOrigin(origins = "*")
@Transactional
@RestController
public class LastTrackController {

	@Autowired
	ApiService apiService;

	@Autowired
	LasttrackServices lasttrackServices;
	
	  @Autowired
	    DevicemasterRepository deviceReop;

	@RequestMapping(method = RequestMethod.GET, value = "/api/getLiveLocation/{deviceId}/{key}")
	@ExceptionHandler(SpringException.class)
	public String getLiveLocation(@PathVariable("deviceId") Long deviceId, @PathVariable String key) {

		Apikey api = apiService.findBykeyValue(key);
		if (api != null) {
			JSONArray jarry = new JSONArray();

			List<Object[]> listLocation = lasttrackServices.getLiveLocation(deviceId);

			String devicedate = "";
			String latitude = "";
			String angle = "";
			String devicename = "";
			String speed = "";
			String longitude = "";

			for (Object[] result1 : listLocation) {
				devicedate = result1[0].toString();
				latitude = result1[1].toString();
				angle = result1[2].toString();
				devicename = result1[3].toString();
				speed = result1[4].toString();
				longitude = result1[5].toString();

				JSONObject jo = new JSONObject();
				jo.put("devicedate", devicedate);
				jo.put("latitude", latitude);
				jo.put("angle", angle);
				jo.put("devicename", devicename);
				jo.put("speed", speed);
				jo.put("longitude", longitude);

				jarry.put(jo);

			}
			return jarry.toString();
		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/GetDeviceInfoBySiteloction/{siteId}/{managerId}/{key}", produces = {
			"application/json" })
	public String getDeviceInfoBySitealocation(@PathVariable("siteId") long siteId,
			@PathVariable("managerId") long managerId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(key);

		List<Object[]> list = lasttrackServices.getDeviceDataByIdLocation(managerId, siteId);

		if (list.size() != 0 && api != null) {

			JSONArray jarray = new JSONArray();

			for (Object[] result : list) {

				JSONObject jo = new JSONObject();
				jo.put("deviceid", result[0].toString());
				jo.put("devicename", result[1].toString());
				jo.put("latitude", result[2].toString());
				jo.put("longitude", result[3].toString());
				jo.put("devicedate", result[4].toString());
				jarray.put(jo);
				System.out.println(result[0].toString());
			}

			return jarray.toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getlivelasttracklocation/{deviceid}/{key}", produces = { "application/json" })
	public String getlivelasttracklocation(@PathVariable String key, @PathVariable Long deviceid)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(key);

		List<Object[]> list = lasttrackServices.getlivelasttracklocation(deviceid);

		if (list.size() != 0 && api != null) {

			JSONArray jarray = new JSONArray();

			for (Object[] result : list) {

				JSONObject jo = new JSONObject();
				jo.put("devicename", result[0].toString());
				jo.put("devicedate", result[1].toString());
				jo.put("latitude", result[2].toString());
				jo.put("longitude", result[3].toString());
				jo.put("speed", result[4].toString());
				jo.put("angle", result[5].toString());
				jarray.put(jo);

			}

			return jarray.toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}

	@RequestMapping(value = "/api/getUserDeviceInfoBySiteloction/{siteId}/{userId}/{key}", produces = {
			"application/json" })
	public String getUserDeviceInfoBySiteloction(@PathVariable("siteId") long siteId,
			@PathVariable("userId") long userId, @PathVariable String key)
			throws JsonGenerationException, JsonMappingException, IOException {

		Apikey api = apiService.findBykeyValue(key);

		List<Object[]> list = lasttrackServices.getUserDeviceDataByIdLocation(userId, siteId);

		if (list.size() != 0 && api != null) {

			JSONArray jarray = new JSONArray();

			for (Object[] result : list) {

				JSONObject jo = new JSONObject();
				jo.put("deviceid", result[0].toString());
				jo.put("devicename", result[1].toString());
				jo.put("latitude", result[2].toString());
				jo.put("longitude", result[3].toString());
				jo.put("devicedate", result[4].toString());
				jarray.put(jo);
				System.out.println(result[0].toString());
			}

			return jarray.toString();

		} else {
			return new SpringException(false, "Invalid Key").toString();
		}
	}
	
	@RequestMapping(value = { "/api/st/{dId}" }, produces = { "application/json" })
    public String st(@PathVariable final Long dId) throws JsonGenerationException, JsonMappingException, IOException {
      MyAlerts alert=new MyAlerts();
     Lasttrack ltack= lasttrackServices.findOne(3L);
     Lasttrack ltackold= lasttrackServices.findOne(9L);
     alert.sendMsg(deviceReop.findBydeviceid(3L), ltack, ltackold);
        return "HI";
    }
}