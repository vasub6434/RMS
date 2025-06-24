package com.bonrix.dggenraterset.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonrix.dggenraterset.DTO.AlertResponse;
import com.bonrix.dggenraterset.DTO.AlertSummary;
import com.bonrix.dggenraterset.DTO.AlertTimelineResponse;
import com.bonrix.dggenraterset.DTO.AlertTypeDTO;
import com.bonrix.dggenraterset.DTO.AlertTypeDTOLive;
import com.bonrix.dggenraterset.DTO.ErrorResponse;
import com.bonrix.dggenraterset.DTO.SuccessResponse;
import com.bonrix.dggenraterset.Model.Apikey;
import com.bonrix.dggenraterset.Model.Devicemaster;
import com.bonrix.dggenraterset.Model.IHReport;
import com.bonrix.dggenraterset.Model.Lasttrack;
import com.bonrix.dggenraterset.Model.Parameter;
import com.bonrix.dggenraterset.Model.Site;
import com.bonrix.dggenraterset.Model.User;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Repository.DeviceProfileRepository;
import com.bonrix.dggenraterset.Repository.DevicemasterRepository;
import com.bonrix.dggenraterset.Service.ApiService;
import com.bonrix.dggenraterset.Service.AssignSiteService;
import com.bonrix.dggenraterset.Service.DashboardAPIService;
import com.bonrix.dggenraterset.Service.DevicemasterServices;
import com.bonrix.dggenraterset.Service.DynamicQueryService;
import com.bonrix.dggenraterset.Service.HistoryServices;
import com.bonrix.dggenraterset.Service.IHReportService;
import com.bonrix.dggenraterset.Service.LasttrackServices;
import com.bonrix.dggenraterset.Service.UserService;
import com.bonrix.dggenraterset.Utility.JsonUtills;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin(origins = {"*"})
@Transactional
@RestController
public class DashboardAPIController {
	
	  private static final Logger log = Logger.getLogger(DashboardAPIController.class);
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  static SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	  static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	  @Autowired
	  ApiService apiService;
	  
	  @Autowired
	  DashboardAPIService dashboardService;
	  
	  @Autowired
	  DevicemasterServices deviceService;
	  
	  @Autowired
	  HistoryServices hstServide;
	  
	  @Autowired
	  DeviceProfileRepository DpService;
	  
	  @Autowired
	  AssignSiteService assignsiteservices;
	  
	  @Autowired
	  LasttrackServices lasttrackservices;
	  
	  @Autowired
	  UserService userService;
	  
	  @Autowired
	  DynamicQueryService deviceQueryService;
	  
	  @Autowired
	  DevicemasterRepository DrRepo;
	  
	  @Autowired
	  IHReportService ihreportService;
	  
	  
	 
		 @RequestMapping(value = { "/api/GetMyProfile/{user_id}/{key}" }, produces = {"application/json" })
	public String GetMyProfile(@PathVariable long user_id,@PathVariable String key) throws ParseException, IOException {
			 Apikey api = this.apiService.findBykeyValue(key);
			 
			 User user=userService.getuserbyid(user_id);
			 JSONArray arry = new JSONArray();
			 log.info("SAJAN : "+user.getUserRole().toString());
			 if (api == null) {
				 List<Object[]> profileList= null; 
				 
				 
				 Set<UserRole> roles = user.getUserRole();

				 for (UserRole role : roles) {
					 if(role.getRole().toString().equalsIgnoreCase("ROLE_USER")) {
						 profileList= DpService.Assigndeviceprofilebyuid(user_id);
						 log.info("SAJAN : "+profileList.size());
						 
					 }
					 else if(role.getRole().toString().equalsIgnoreCase("ROLE_MANAGER")) {
						  profileList= DpService.assigndeviceprofilebymanageridCount(user_id);
						  
					 }
				 }
				 
				 
				
					 
				 if(profileList!=null) {  
				 for (Object[] profile : profileList) {
					 JSONObject profileJson = new JSONObject();
					 profileJson.put("profileId",profile[1].toString());
					 profileJson.put("profileName",profile[0].toString());
					 profileJson.put("deviceCount",profile[2].toString());
					 arry.put(profileJson);
				 }
				 }
				   
		 }
			return arry.toString();
		 } 
		 
		 
		 @RequestMapping(value = { "/api/GetMyDevices/{user_id}/{key}" }, produces = {"application/json" })
			public String GetMyDevices(@PathVariable long user_id,@PathVariable String key) throws ParseException, IOException {
					 Apikey api = this.apiService.findBykeyValue(key);
					 User user=userService.getuserbyid(user_id);
					 JSONArray arry = new JSONArray();
					 log.info("SAJAN : "+user.getUserRole().toString());
					// if (api == null) {
						 List<Object[]> list = new ArrayList<>();
						 Set<UserRole> roles = user.getUserRole();
						 for (UserRole role : roles) {
							 if(role.getRole().toString().equalsIgnoreCase("ROLE_USER")) {
								 list = deviceService.getMyDeviced(user_id);
								    list.forEach((Object[] o) -> {
								    	JSONObject obj = new JSONObject();
									obj.put("deviceId", o[0].toString());
									obj.put("deviceName", o[1].toString());
									arry.put(obj);
								    });
							 }
							 else if(role.getRole().toString().equalsIgnoreCase("ROLE_MANAGER")) {
								 list = deviceService.getDeviceByManagerId(user_id);
								 
									for (Object[] result1 : list) {
										JSONObject jo = new JSONObject();
								    jo.put("deviceId", result1[0].toString());
									jo.put("deviceName", result1[3].toString());
									arry.put(jo);
									}
							 }
						 }
				// }
					return arry.toString();
				 } 
		 
		 
	 @RequestMapping(value = { "/api/GetDeviceDataByProfile/{user_id}/{profile_id}/{key}" }, produces = {
		"application/json" })
public String GetDeviceDataByProfile(@PathVariable long user_id,@PathVariable long profile_id,@PathVariable String key) throws ParseException, IOException {
		 Apikey api = this.apiService.findBykeyValue(key);
		 if (api == null) {
		        List<Object[]> list = new ArrayList<>();
		        Object[] data1 = { profile_id };
		        list.add(data1);
			   // Apikey api = this.apiService.findBykeyValue(key);
			   log.info("assigndeviceprofilebyuid : "+list.size());
			    log.info("Apikey : "+list.size());
			    if (list.size() != 0) {
			   // if (list.size() != 0 && api != null) {
			    	log.info("IN IF");
			      JSONObject obj1 = new JSONObject();
			      JSONArray jarray1 = new JSONArray();
			      JSONArray jarray3 = new JSONArray();
			      JSONArray jcolumn4 = new JSONArray();
			      jcolumn4.put("#");
			      jarray1.put(jcolumn4);
			      JSONArray jcolumn1 = new JSONArray();
			      jcolumn1.put("Site Name");
			      jarray1.put(jcolumn1);
			      JSONArray jcolumn2 = new JSONArray();
			      jcolumn2.put("Device Time");
			      jarray1.put(jcolumn2);
			      int flag = 0;
			      int flag2 = 0;
			      int flag3 = 0;  
			      for (Object[] result : list) {
			        List<Object[]> list2 = this.hstServide.getdevicebyprid(Long.valueOf(Long.parseLong(result[0].toString())), user_id);
			        int i = 1;
			        for (Object[] result1 : list2) {
			          List<Object[]> list3 = this.hstServide.getdevicekeyvalbydid(Long.valueOf(Long.parseLong(result1[0].toString())));
			          log.info("Process Device iD ==> "+result1[0]);
			          if(Long.valueOf(Long.parseLong(result1[0].toString()))==894145323)
			        	  break;
			          List<Object[]> list4 = this.hstServide
			            .getdevicekeyvaldigitalbydid(Long.valueOf(Long.parseLong(result1[0].toString())));
			          JSONArray jarray2 = new JSONArray();
			          //jarray2.put(i);
			          i++;
			          jarray2.put(result1[0].toString());
			          jarray2.put(result1[1].toString());
			          jarray2.put(result1[2].toString());
			          for (Object[] result2 : list3) {
			            if (flag == 0) {
			              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
			              JSONArray jcolumn = new JSONArray();
			              List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[0].toString()),result2[0].toString());
			              String analogunit1 ="";
			              if(list6.size()!=0)
			               analogunit1 = (String) list6.get(0);
			              jcolumn.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
			              jarray1.put(jcolumn);
			            } 
			            jarray2.put(result2[1].toString().replaceAll("\"", "") );
			          } 
			          for (Object[] result4 : list4) {
			            if (flag2 == 0) {
			              JSONArray jcolumnd = new JSONArray();
			              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result4[0].toString())));
			              jcolumnd.put(prmnamelist.getPrmname());
			              jarray1.put(jcolumnd);
			            } 
			            if (result4[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
			              jarray2.put("OFF");
			              continue;
			            } 
			            jarray2.put("ON");
			          } 
			          flag++;
			          flag2++;
			          jarray3.put(jarray2);
			          obj1.put("columns", jarray1);
			          obj1.put("data", jarray3);
			        } 
			        if (flag3 == 0) {
			          JSONArray jcolumn3 = new JSONArray();
			          jcolumn3.put("Action");
			          jarray1.put(jcolumn3);
			          flag3++;
			        } 
			      } 
			      return obj1.toString();
		 }
		
		 }
		 return "Invalid Key";   
	 }
	 
	/* @RequestMapping(value = {"/api/getUserDeviceHistoryData/{deviceid}/{startdate}/{enddate}/{key}"}, produces = {"application/json"})
	  public String getUserDeviceHistoryData(@PathVariable Long deviceid, @PathVariable String startdate, @PathVariable String enddate, @PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException {
	    Apikey api = this.apiService.findBykeyValue(key);
	    if (api == null) {
	      List<Object[]> list = this.hstServide.getdhistorynobydid(deviceid, startdate, enddate);
	      log.info("list : "+list);
	      JSONObject obj1 = new JSONObject();
	      JSONArray jarray1 = new JSONArray();
	      JSONArray jarray2 = new JSONArray();
	      JSONArray jarray3 = new JSONArray();
	      int flag = 0;
	      int flag2 = 0;
	      int i = 1;
	      JSONArray jcolumn4 = new JSONArray();
	      jcolumn4.put("#");
	      jarray1.put(jcolumn4);
	      JSONArray jcolumnname = new JSONArray();
	      jcolumnname.put("Device Name");
	      jarray1.put(jcolumnname);
	      JSONArray jcolumn2 = new JSONArray();
	      jcolumn2.put("Device Date");
	      jarray1.put(jcolumn2);
	      for (Object[] result : list) {
	        JSONArray jarray24 = new JSONArray();
	       // List<Object[]> list1 = this.hstServide.getdhistoryanalogbyno(Long.valueOf(Long.parseLong(result[0].toString())));
	         List<Object[]> list1 =dashboardService.getHisrotyAnalogByNo(Long.valueOf(Long.parseLong(result[0].toString())));
	        String storedate = null;
	        jarray24.put(i);
	        jarray24.put(result[3].toString());
	        i++;
	        if (list1.size() != 0)
	          for (Object[] result1 : list1) {
	            if (flag == 0) {
	              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result1[0].toString())));
	              JSONArray jcolumnd = new JSONArray();
	              List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[2].toString()),result1[0].toString());
	              log.info("Parameter ::  "+result1[0]);
	              log.info("Profile ::  "+result[2]);
	              log.info("Analog ::  "+result[0]);
					String analogunit1 = (String) list6.get(0);
	              jcolumnd.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
	              jarray1.put(jcolumnd);
	            } 
	            if (storedate == null) {
	              jarray24.put(result1[3].toString());
	              storedate = "datestored";
	            } 
	            jarray24.put(result1[1].toString().replaceAll("\"", ""));
	          }  
	        List<Object[]> list2 = this.dashboardService.getHisrotyDigitalByNo(Long.valueOf(Long.parseLong(result[0].toString())));
	      //  if (list2.size() != 0)
	          for (Object[] result2 : list2) {
	            if (flag2 == 0) {
	              Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
	              JSONArray jcolumnd2 = new JSONArray();
	              jcolumnd2.put(prmnamelist.getPrmname());
	              jarray1.put(jcolumnd2);
	            } 
	            if (result2[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
	              jarray24.put("OFF");
	              continue;
	            } 
	            jarray24.put("ON");
	          }  
	     // }
	        flag++;
	        flag2++;
	        jarray3.put(jarray24);
	      } 
	      obj1.put("columns", jarray1);
	      obj1.put("data", jarray3);
	      return obj1.toString();
	    } 
	    return "Invalid Key";
	  }*/
	 
	// api for count alerts and device of each profile
	/*	@RequestMapping(value = { "/api/deviceStatusDetailsWithAlerts/{user_id}/{key}"}, produces = { "application/json" })
		public String getDeviceLiveAlermData(@PathVariable long user_id, @PathVariable String key) throws ParseException, IOException {

		    Apikey api = this.apiService.findBykeyValue(key);
		    if (api == null) {
		        return "Invalid Key";
		    }
		    JSONArray resultArray = new JSONArray();
		    List<Object[]> profileList = DpService.Assigndeviceprofilebyuid(user_id);
		    List<Long> longList = new ArrayList<>();
		    for (Object[] profile : profileList) {
		    	List<String> failureCodes = Arrays.asList("284945", "6348798", "291934", "6348854", "6348815", "6348821", "6348824");
			    List<String> digitalFields = Arrays.asList("284945", "6348798", "291934", "6348854", "6348815", "6348821", "6348824");
			    JSONObject dataJson = new JSONObject();
		        dataJson.put("profileId", profile[1].toString());
		        dataJson.put("profileName", profile[0].toString());

		        List<Object[]> allDetails = deviceQueryService.getDynamicDeviceStatusDetails(1L,2L,3L,failureCodes,digitalFields);
		       
		        Map<String, JSONObject> profileMap = new HashMap<>();

		        int ACMINS_FAIL_CONT = 0;
		        int FIRE_CONT = 0;
		        int DOOR_CONT = 0;
		        int DG_RUNNING_HRS_CONT = 0;
		        int DG_FAULT_CONT = 0;
		        int BATTRY_LOW_CONT = 0;
		        int PP_INPUT_FAIL_CONT = 0;

		        if (allDetails != null && !allDetails.isEmpty()) {
		            for (Object[] detail : allDetails) {
		                dataJson.put("deviceName", detail[1].toString());
		                dataJson.put("altDeviceName", detail[2].toString());
		                dataJson.put("deviceDate", detail[3].toString());

		                if (detail[4].toString().equalsIgnoreCase("0")) ACMINS_FAIL_CONT++;
		                if (detail[5].toString().equalsIgnoreCase("0")) FIRE_CONT++;
		                if (detail[6].toString().equalsIgnoreCase("0")) DOOR_CONT++;
		                if (detail[7].toString().equalsIgnoreCase("0")) DG_RUNNING_HRS_CONT++;
		                if (detail[8].toString().equalsIgnoreCase("0")) DG_FAULT_CONT++;
		                if (detail[9].toString().equalsIgnoreCase("0")) BATTRY_LOW_CONT++;
		                if (detail[10].toString().equalsIgnoreCase("0")) PP_INPUT_FAIL_CONT++;
		            }
		        }

		        dataJson.put("ACMINS_FAIL_CONT", ACMINS_FAIL_CONT  );
		        dataJson.put("FIRE_CONT", FIRE_CONT  );
		        dataJson.put("DOOR_CONT", DOOR_CONT );
		        dataJson.put("DG_RUNNING_HRS_CONT", DG_RUNNING_HRS_CONT  );
		        dataJson.put("DG_FAULT_CONT", DG_FAULT_CONT  );
		        dataJson.put("BATTRY_LOW_CONT", BATTRY_LOW_CONT  );
		        dataJson.put("PP_INPUT_FAIL_CONT", PP_INPUT_FAIL_CONT  );

		        resultArray.put(dataJson);
		    }
		    return resultArray.toString();
		}*/
	 
	// api for count alerts and device of each profile
		/*@RequestMapping(value = { "/api/deviceStatusDetailsWithAlerts/{user_id}/{key}" }, produces = { "application/json" })
		public String getDeviceLiveAlermData(@PathVariable long user_id, @PathVariable String key)
				throws ParseException, IOException {

			Apikey api = this.apiService.findBykeyValue(key);
			if (api == null) {
				return "Invalid Key";
			}
			JSONArray resultArray = new JSONArray();  
			List<Object[]> profileList = DpService.Assigndeviceprofilebyuid(user_id);
			List<Long> longList = new ArrayList<>();
			for (Object[] profile : profileList) {
				List<String> failureCodes = Arrays.asList("284945", "6348798", "291934", "6348854", "6348815", "6348821",
						"6348824");
				List<String> digitalFields = Arrays.asList("284945", "6348798", "291934", "6348854", "6348815", "6348821",
						"6348824");
				JSONObject dataJson = new JSONObject();
				dataJson.put("profileId", profile[1].toString());
				dataJson.put("profileName", profile[0].toString());

				List<Object[]> allDetails = deviceQueryService.getDynamicDeviceStatusDetails(1L, 2L, 3L, failureCodes,
						digitalFields);

				Map<String, JSONObject> profileMap = new HashMap<>();

				int ACMINS_FAIL_CONT = 0;
				int FIRE_CONT = 0;
				int DOOR_CONT = 0;
				int DG_RUNNING_HRS_CONT = 0;
				int DG_FAULT_CONT = 0;
				int BATTRY_LOW_CONT = 0;
				int PP_INPUT_FAIL_CONT = 0;

				if (allDetails != null && !allDetails.isEmpty()) {
					for (Object[] detail : allDetails) {
						dataJson.put("deviceName", detail[1].toString());
						dataJson.put("altDeviceName", detail[2].toString());
						dataJson.put("deviceDate", detail[3].toString());

						if (detail[4].toString().equalsIgnoreCase("0"))
							ACMINS_FAIL_CONT++;
						if (detail[5].toString().equalsIgnoreCase("0"))
							FIRE_CONT++;
						if (detail[6].toString().equalsIgnoreCase("0"))
							DOOR_CONT++;
						if (detail[7].toString().equalsIgnoreCase("0"))
							DG_RUNNING_HRS_CONT++;
						if (detail[8].toString().equalsIgnoreCase("0"))
							DG_FAULT_CONT++;
						if (detail[9].toString().equalsIgnoreCase("0"))
							BATTRY_LOW_CONT++;
						if (detail[10].toString().equalsIgnoreCase("0"))
							PP_INPUT_FAIL_CONT++;
					}
				}

				dataJson.put("ACMINS_FAIL_CONT", ACMINS_FAIL_CONT);
				dataJson.put("FIRE_CONT", FIRE_CONT);
				dataJson.put("DOOR_CONT", DOOR_CONT);
				dataJson.put("DG_RUNNING_HRS_CONT", DG_RUNNING_HRS_CONT);
				dataJson.put("DG_FAULT_CONT", DG_FAULT_CONT);
				dataJson.put("BATTRY_LOW_CONT", BATTRY_LOW_CONT);
				dataJson.put("PP_INPUT_FAIL_CONT", PP_INPUT_FAIL_CONT);

				resultArray.put(dataJson);
			}
			return resultArray.toString();
		}*/

	 
	 @RequestMapping(value = {"/api/GetDeviceTemperatureChartRecords/{id}/{prmname}/{limit}"}, produces = {"application/json"})
	  public String GetDeviceTemperatureChartRecords(@PathVariable long id,@PathVariable String prmname,@PathVariable int limit) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
	    Devicemaster devicemaster = this.deviceService.get(id);
	    List list = dashboardService.GetDeviceParameterRecords(id, prmname);
		List list5 = hstServide.getprofileanalogunit(devicemaster.getDp().getPrid(), prmname);
		List list6 = hstServide.getperametername(Long.parseLong(prmname));
	    JSONArray finalMainJSON = new JSONArray();
	 // Assuming list and other variables are already initialized
	    JSONArray temperatureChartData = new JSONArray();
	    try {
	    // Process the list to fill data for both charts
	    for (int i = 0; i < list.size(); i++) {
	      //  LinkedHashMap<String, String> record = (LinkedHashMap<String, String>) list.get(i);
	    	Object[] record = (Object[]) list.get(i);
	        String deviceDate = record[0].toString();
	        
	        String data = record[1].toString();
Date date = df.parse(deviceDate);
  /*          
double value = Double.parseDouble(data);
double min = 0.0;
double max = 50000.0;

double normalized = (value - min) / (max - min) * 100;*/

            // Get the time in milliseconds
            long millis = date.getTime();
	        // Process the data into chart points
	        JSONObject temperaturePoint = new JSONObject();
	        temperaturePoint.put("x", millis); // Using deviceDate as timestamp
	        temperaturePoint.put("y", data); // Assuming data is temperature
	       /// temperaturePoint.put("y", deviceDate);
	        temperatureChartData.put(temperaturePoint);

	    }
	 } catch (NumberFormatException e) {
		    log.warn("Invalid number format, using 0.0 for: " + e);
		    log.info("Invalid number format, using 0.0 for: " + e);
		}


	    JSONObject temperatureChartJSON = new JSONObject();
	    temperatureChartJSON.put("temperatureChart", new JSONObject()
	        .put("type", "line")
	        .put("series", new JSONArray()
	            .put(new JSONObject().put("name", "Temperature").put("data", temperatureChartData))));
	    finalMainJSON.put(temperatureChartJSON);
	    return finalMainJSON.toString();
	  }
	 
	 
	 @RequestMapping(value = {"/api/GetDeviceHumidityChartRecords/{id}/{prmname}/{limit}"}, produces = {"application/json"})
	  public String GetDeviceHumidityChartRecords(@PathVariable long id,@PathVariable String prmname,@PathVariable int limit) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
	    List list = dashboardService.GetDeviceParameterRecords(id, prmname);
	    JSONArray finalMainJSON = new JSONArray();
	    JSONArray humidityChartData = new JSONArray();
log.info("GetDeviceHumidityChartRecords : "+list.size());
	    // Process the list to fill data for both charts
try {
	    for (int i = 0; i < list.size(); i++) {
	      //  LinkedHashMap<String, String> record = (LinkedHashMap<String, String>) list.get(i);
	    	Object[] record = (Object[]) list.get(i);
	        String deviceDate = record[0].toString();
	        log.info("GetDeviceHumidityChartRecords : "+i); 
	        String data = record[1].toString();
Date date = df.parse(deviceDate);
           
/*
double value = Double.parseDouble(data);
double min = 0.0;
double max = 50000.0;

double normalized = (value - min) / (max - min) * 100;
*/

           // Get the time in millisecondsf
           long millis = date.getTime();
	        // Process the data into chart points

	        // Similarly for humidity chart
	        JSONObject humidityPoint = new JSONObject();
	        humidityPoint.put("x", millis); // Timestamp for humidity
	        humidityPoint.put("y",data ); // Assuming data is humidity value
	        humidityChartData.put(humidityPoint);
	    }
} catch (NumberFormatException e) {
    log.warn("Invalid number format, using 0.0 for: " + e);
    log.info("Invalid number format, using 0.0 for: " + e);
}
	    JSONObject humidityChartChartJSON = new JSONObject();
	    humidityChartChartJSON.put("humidityChart", new JSONObject()
	        .put("type", "area")
	        .put("series", new JSONArray()
	            .put(new JSONObject().put("name", "Humidity").put("data", humidityChartData))));

	    // finalJSON will now contain the structure needed for your charts
	    finalMainJSON.put(humidityChartChartJSON);
	    return finalMainJSON.toString();
	  }

	 
	 @RequestMapping(method = {RequestMethod.GET}, value = {"/api/VodeoconDigitalDataONOFF/{id}"}, produces = {"application/json"})
	  public String VodeoconDigitalDataONOFF(@PathVariable long id) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
	    ObjectMapper mapper = new ObjectMapper();
	    ArrayNode analogNodearrayNode = mapper.createArrayNode();
	    List<Object[]> grideData = this.lasttrackservices.GetVodeoconLastTrack(id);
	   
	    ObjectNode DIGITALNode = mapper.createObjectNode();
	    if(grideData.size()>0) {
	    	 Object[] result1 = grideData.get(0);
	    if (result1[0] != null)
	    {
	    	DIGITALNode.putPOJO("ACMAINS_FAIL", 
	    	          result1[0].toString().equalsIgnoreCase("1") ? 
	    	          "ON" : 
	    	          "OFF");
	    }
	    if (result1[1] != null)
	    {
	    	 DIGITALNode.putPOJO("Fire", 
	    	          result1[1].toString().equalsIgnoreCase("1") ? 
	    	        		  "ON" : 
	    		    	          "OFF");
	    }
	    if (result1[2] != null)
	    {
	    	  DIGITALNode.putPOJO("Door", 
	    	          result1[2].toString().equalsIgnoreCase("1") ? 
	    	        		  "ON" : 
	    		    	          "OFF");
	    }
	    if (result1[3] != null)
	    {
	    	 DIGITALNode.putPOJO("DG_Running_Hrs", 
	    	          result1[3].toString().equalsIgnoreCase("1") ? 
	    	        		  "ON" : 
	    		    	          "OFF");
	    }
	    if (result1[4] != null)
	    {
	    	 DIGITALNode.putPOJO("DG_Fault", 
	    	          result1[4].toString().equalsIgnoreCase("1") ? 
	    	        		  "ON" : 
	    		    	          "OFF");
	    }
	    if (result1[5] != null)
	    {
	    	 DIGITALNode.putPOJO("Battry_Low", 
	    	          result1[5].toString().equalsIgnoreCase("1") ? 
	    	        		  "ON" : 
	    		    	          "OFF");
	    }
	    if (result1[6] != null)
	    {
	    	 DIGITALNode.putPOJO("PP_Input_Fail", result1[6].toString().equalsIgnoreCase("1") ? 
	    			 "ON" : 
		    	          "OFF");
	    }
	 }else {
		 DIGITALNode.putPOJO("ACMAINS_FAIL","OFF");
	 }
	    analogNodearrayNode.add((JsonNode)DIGITALNode); 
	    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(analogNodearrayNode);
	  }
	 
	 @RequestMapping(value = {"/api/getUserDeviceHistoryData/{deviceid}/{startdate}/{enddate}/{size}/{page}/{key}"}, produces = {"application/json"})
	 public String getUserDeviceHistoryData(
	     @PathVariable Long deviceid, 
	     @PathVariable String startdate, 
	     @PathVariable String enddate,
	     @PathVariable int size, 
	     @PathVariable int page,
	     @PathVariable String key
	 ) throws JsonGenerationException, JsonMappingException, IOException {

	     // Ensure page is at least 1
	     if (page < 1) {
	         page = 1;
	     }

	     Apikey api = this.apiService.findBykeyValue(key);
	     if (api == null) {
	         // Get the total count of records for pagination purposes
	         int totalCount = this.hstServide.getdhistorynobydidCount(deviceid, startdate, enddate);

	         // Calculate pagination bounds
	         int offset = (page - 1) * size;  // Ensure offset is always >= 0
	         List<Object[]> list = this.hstServide.getdhistorynobydidPaginated(deviceid, startdate, enddate, page, size);
	         
	         log.info("list : " + list);

	         JSONObject obj1 = new JSONObject();
	         JSONArray jarray1 = new JSONArray();
	         JSONArray jarray2 = new JSONArray();
	         JSONArray jarray3 = new JSONArray();
	         int flag = 0;
	         int flag2 = 0;
	         int i = 1;
	         JSONArray jcolumn4 = new JSONArray();
	         jcolumn4.put("#");
	         jarray1.put(jcolumn4);
	         JSONArray jcolumnname = new JSONArray();
	         jcolumnname.put("Device Name");
	         jarray1.put(jcolumnname);
	         JSONArray jcolumn2 = new JSONArray();
	         jcolumn2.put("Device Date");
	         jarray1.put(jcolumn2);
	         
	         if (list != null && !list.isEmpty()) {
	             for (Object[] result : list) {
	                 // Ensure result is not null and has the expected size
	                 if (result != null && result.length > 3) {
	                     JSONArray jarray24 = new JSONArray();
	                     List<Object[]> list1 = dashboardService.getHisrotyAnalogByNo(Long.valueOf(Long.parseLong(result[0].toString())));
	                     String storedate = null;
	                     jarray24.put(i);
	                     jarray24.put(result[3].toString());
	                     i++;

	                     if (list1 != null && !list1.isEmpty()) {
	                         for (Object[] result1 : list1) {  
	                             if (result1 != null && result1.length > 3) {
	                                 if (flag == 0) {
	                                     Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result1[0].toString())));
	                                     if (prmnamelist != null) {
	                                         JSONArray jcolumnd = new JSONArray();
	                                         List list6 = hstServide.getprofileanalogunit(Long.parseLong(result[2].toString()), result1[0].toString());

	                                         if (list6 != null && !list6.isEmpty()) {
	                                             String analogunit1 = (String) list6.get(0);
	                                             jcolumnd.put(String.valueOf(prmnamelist.getPrmname()) + "(" + analogunit1 + ")");
	                                             jarray1.put(jcolumnd);
	                                         } else {
	                                             log.warn("list6 is empty or null for deviceId: " + result[0]);
	                                         }
	                                     } else {
	                                         log.warn("prmnamelist is null for result1: " + result1[0]);
	                                     }
	                                 }
	                                 if (storedate == null) {
	                                     jarray24.put(result1[3].toString());
	                                     storedate = "datestored";
	                                 }
	                                 jarray24.put(result1[1].toString().replaceAll("\"", ""));
	                             }
	                         }
	                     } else {
	                         log.warn("list1 is empty or null for deviceId: " + result[0]);
	                     }

	                     List<Object[]> list2 = this.dashboardService.getHisrotyDigitalByNo(Long.valueOf(Long.parseLong(result[0].toString())));
	                     if (list2 != null && !list2.isEmpty()) {
	                         for (Object[] result2 : list2) {
	                             if (result2 != null && result2.length > 1) {
	                                 if (flag2 == 0) {
	                                     Parameter prmnamelist = this.hstServide.findpOne(Long.valueOf(Long.parseLong(result2[0].toString())));
	                                     if (prmnamelist != null) {
	                                         JSONArray jcolumnd2 = new JSONArray();
	                                         jcolumnd2.put(prmnamelist.getPrmname());
	                                         jarray1.put(jcolumnd2);
	                                     }
	                                 }
	                                 if (result2[1] != null && result2[1].toString().replaceAll("\"", "").equalsIgnoreCase("0")) {
	                                     jarray24.put("OFF");
	                                     continue;
	                                 }
	                                 jarray24.put("ON");
	                             }
	                         }
	                     }
	                     flag++;
	                     flag2++;
	                     jarray3.put(jarray24);
	                 }
	             }
	         } else {
	             log.warn("list is null or empty.");
	         }

	         JSONObject pagination = new JSONObject();
	         pagination.put("totalItems", totalCount);
	         pagination.put("totalPages", (int) Math.ceil((double) totalCount / size));
	         pagination.put("currentPage", page);
	         pagination.put("pageSize", size);

	         obj1.put("columns", jarray1);
	         obj1.put("data", jarray3);
	         obj1.put("pagination", pagination);

	         return obj1.toString();
	     }

	     return "Invalid Key";
	}
	 
	/* @RequestMapping(value = { "/api/alertByZone/{user_id}/{siteId}/{key}"}, produces = { "application/json" })
		public String alertByZone(@PathVariable long user_id,@PathVariable long siteId,@PathVariable String key) throws ParseException, IOException {

		    
		    JSONArray resultArray = new JSONArray();
		     List<Object[]> profileList = DpService.Assigndeviceprofilebyuid(user_id);
		
		
		   for (Object[] profile : profileList) {

		        JSONObject dataJson = new JSONObject();
		        dataJson.put("profileId", profile[1].toString());
		        dataJson.put("profileName", profile[0].toString());

		        List<Object[]> allDetails = DpService.getAllDeviceStatusBySite(user_id, Long.parseLong(profile[1].toString()), siteId);

		        int ACMINS_FAIL_CONT = 0;
		        int FIRE_CONT = 0;
		        int DOOR_CONT = 0;
		        int DG_RUNNING_HRS_CONT = 0;
		        int DG_FAULT_CONT = 0;
		        int BATTRY_LOW_CONT = 0;
		        int PP_INPUT_FAIL_CONT = 0;

		        if (allDetails != null && !allDetails.isEmpty()) {
		            for (Object[] detail : allDetails) {
		                dataJson.put("deviceName", detail[1].toString());
		                dataJson.put("altDeviceName", detail[2].toString());
		                dataJson.put("deviceDate", detail[3].toString());

		                if (detail[4].toString().equalsIgnoreCase("0")) ACMINS_FAIL_CONT++;
		                if (detail[5].toString().equalsIgnoreCase("0")) FIRE_CONT++;
		                if (detail[6].toString().equalsIgnoreCase("0")) DOOR_CONT++;
		                if (detail[7].toString().equalsIgnoreCase("0")) DG_RUNNING_HRS_CONT++;
		                if (detail[8].toString().equalsIgnoreCase("0")) DG_FAULT_CONT++;
		                if (detail[9].toString().equalsIgnoreCase("0")) BATTRY_LOW_CONT++;
		                if (detail[10].toString().equalsIgnoreCase("0")) PP_INPUT_FAIL_CONT++;
		            }
		        }

		        dataJson.put("ACMINS_FAIL_CONT", ACMINS_FAIL_CONT );
		        dataJson.put("FIRE_CONT", FIRE_CONT );
		        dataJson.put("DOOR_CONT", DOOR_CONT );
		        dataJson.put("DG_RUNNING_HRS_CONT", DG_RUNNING_HRS_CONT );
		        dataJson.put("DG_FAULT_CONT", DG_FAULT_CONT );
		        dataJson.put("BATTRY_LOW_CONT", BATTRY_LOW_CONT );
		        dataJson.put("PP_INPUT_FAIL_CONT", PP_INPUT_FAIL_CONT );

		        if (ACMINS_FAIL_CONT > 0 || FIRE_CONT > 0 || DOOR_CONT > 0 || DG_RUNNING_HRS_CONT > 0 || DG_FAULT_CONT > 0 || BATTRY_LOW_CONT > 0 || PP_INPUT_FAIL_CONT > 0) {
		            resultArray.put(dataJson);
		        }
		    }
		    return resultArray.toString();
		}*/
	 
	/* @RequestMapping(value = "/api/alertByZone/{managerId}/{profileId}/{key}", produces = "application/json")
	    public String alertByZoneLive(@PathVariable long managerId,
	                                   @PathVariable long profileId,
	                                   @PathVariable String key) throws ParseException, IOException {


	        String profileName = DpService.getprofilenameByID(profileId);
	        String[] parameterNameList = DpService.getparameterNameByProfile(profileId);
	        System.out.println("Parameter Name List: " + Arrays.toString(parameterNameList));

	        return deviceQueryService.getDeviceAlertFromAlertMsgHistory(managerId, parameterNameList);
	    }*/
	 
/*	 @RequestMapping(value = "/api/getAlertTimeline/{fromDate}/{toDate}/{managerId}/{profileId}", method = RequestMethod.GET, produces = "application/json")
	  public ResponseEntity<?> getAlertTimeline(
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
	          @PathVariable Long managerId,
	          @PathVariable Long profileId,
	          @RequestParam(defaultValue = "1 hour") String interval) {  // Removed deviceId here

	      AlertTimelineResponse response = dashboardService.getAlertTimeline(managerId, fromDate, toDate, profileId,interval);  // Removed deviceId here
	      
	      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
	  }
	 */
	 @RequestMapping(value = "/api/getAlerts/{fromDate}/{toDate}/{managerId}/{profileId}", method = RequestMethod.GET, produces = "application/json")
	  public ResponseEntity<AlertResponse> getAlerts(
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date fromDate,
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date toDate,
	          @PathVariable Long managerId,
	  			@PathVariable Long profileId)
	  {

	      AlertResponse response = dashboardService.getAlerts(fromDate, toDate, managerId,profileId);
	      return ResponseEntity.ok(response);
	  }

	  @RequestMapping(value = "/api/getAlertsSummaryAPI/{fromDate}/{toDate}/{managerId}", method = RequestMethod.GET, produces = "application/json")
	  public ResponseEntity<?> getAlertsSummaryAPI(
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date fromDate,
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date toDate,
	          @PathVariable Long managerId) {

	      try {
	          AlertSummary summary = dashboardService.getAlertSummary(managerId, fromDate, toDate);
	          SuccessResponse response = new SuccessResponse(
	                  summary,
	                  fromDate.toInstant().toString(),
	                  toDate.toInstant().toString()
	          );
	          return ResponseEntity.ok(response);
	      } catch (Exception e) {
	          ErrorResponse errorResponse = new ErrorResponse(
	                  "An error occurred while fetching the alerts summary",
	                  e.getMessage()
	          );
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	      }
	  }
	  
	  @RequestMapping(value = "/api/getAlertTimeline/{fromDate}/{toDate}/{managerId}/{profileId}", method = RequestMethod.GET, produces = "application/json")
	  public ResponseEntity<?> getAlertTimeline(
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
	          @PathVariable Long managerId,
	          @PathVariable Long profileId,
	          @RequestParam(defaultValue = "1 hour") String interval) {  // Removed deviceId here
	      AlertTimelineResponse response = dashboardService.getAlertTimeline(managerId, fromDate, toDate, profileId,interval);  // Removed deviceId here
	      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
	  }  
	  
	/*  @RequestMapping(value = "/api/getAlertsByTypeOld/{fromDate}/{toDate}/{managerId}/{profileId}", method = RequestMethod.GET, produces = "application/json")
	  public ResponseEntity<?> getAlertsByTypeOld(
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
	          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
	          @PathVariable Long managerId,
	          @PathVariable Long profileId){
	      AlertTypeDTO response = dashboardService.getAlertData(fromDate, toDate, managerId);
	      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
	  }*/
	  
		@RequestMapping(value = { "/api/GetMyProfilewithCount/{manager_id}/{key}" }, produces = { "application/json" })
		public String GetMyProfilewithCount(@PathVariable long manager_id, @PathVariable String key)
		        throws ParseException, IOException {
		    JSONArray arry = new JSONArray();
		    List<Object[]> profileList = DpService.assigndeviceprofilebymanageridCount(manager_id);
		    for (Object[] profile : profileList) {
		        System.out.println("Profile: " + Arrays.toString(profile));
		        JSONObject profileJson = new JSONObject();
		        profileJson.put("profileName", profile[0].toString());   
		        profileJson.put("profileId", profile[1].toString());     
		        profileJson.put("deviceCount", profile[2].toString());   
		        arry.put(profileJson);
		    }
		    return arry.toString();
		}
	 
		  @RequestMapping(value = {"/api/getDeviceCurrentStatus/{deviceId}/{userId}"}, produces = {"application/json"})
		  public String getDeviceCurrentStatus(@PathVariable long deviceId,@PathVariable long userId) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
		      JSONArray dataArray = new JSONArray();
		          List<Object[]> result = this.lasttrackservices.getDeviceLocation(userId,deviceId);
		         Devicemaster device= DrRepo.findOne(deviceId);
		          JSONObject jo = new JSONObject();
		          if(result.size()>0) {
		            Date TodayDate = new Date();
		            Date deviceDate = sdf1.parse(((Object[])result.get(0))[4].toString());
		            long differenceHours = TimeUnit.MILLISECONDS
		              .toHours(TodayDate.getTime() - deviceDate.getTime());
		         log.info("SAJAN :: "+differenceHours);
		            jo.put("deviceid", ((Object[])result.get(0))[0].toString());
		            jo.put("devicename", ((Object[])result.get(0))[1].toString());
		            jo.put("latitude", ((Object[])result.get(0))[2].toString());
		            jo.put("longitude", ((Object[])result.get(0))[3].toString());
		            jo.put("devicedate",String.valueOf(((Object[])result.get(0))[4].toString()) + "()");
		            jo.put("status", (differenceHours < 2L) ? "Online" : "Offline");
		  }else
		  {
			  jo.put("deviceid", device.getDeviceid());
	            jo.put("devicename", device.getDevicename());
	            jo.put("latitude", "0");
	            jo.put("longitude", "0");
	            jo.put("devicedate", "00:00:00" + "()" );
	            jo.put("status", "Offline");
		  }
		            dataArray.put(jo);
		    return jo.toString();
		  }
		  
		  @RequestMapping(value = { "/api/GetDistance/{device_id}/{key}" }, produces = {"application/json" })
			public String GetDistance(@PathVariable long device_id,@PathVariable String key) throws ParseException, IOException {
			     Devicemaster device = DrRepo.findBydeviceid(device_id);
			     JSONArray arry = new JSONArray();
			  Lasttrack lst=   lasttrackservices.findOne(device_id);
			  Map<String, Object> deviceData= lst.getDevicedata();
			  Map<String, Object> analogData = (Map<String, Object>) deviceData.get("Analog");
			  Double hours = (Double) analogData.get("1355411238");
			  Double minutesPart = (Double) analogData.get("1355411608");

			  if(lst!=null) {
			     BigDecimal  distance = dashboardService.getAnalogDiffrence(device_id,"1290902357");
			 
			  if(distance!=null) {
			        JSONObject profileJson = new JSONObject();
			        profileJson.put("deviceId", device.getDeviceid());   
			        profileJson.put("deviceName", device.getDevicename());   
			        profileJson.put("distance",distance);
			        profileJson.put("hourMeter",String.format("%02d:%02d", hours.intValue(), minutesPart.intValue()));
			        
			        arry.put(profileJson);
		  }else
		  {
			  JSONObject profileJson = new JSONObject();
		        profileJson.put("deviceId", device.getDeviceid());   
		        profileJson.put("deviceName", device.getDevicename());   
		        profileJson.put("distance", "00");   
		        profileJson.put("hourMeter",String.format("%02d:%02d", hours.intValue(), minutesPart.intValue()));
		        arry.put(profileJson);
		  }
		  }else
		  {
			  JSONObject profileJson = new JSONObject();
		        profileJson.put("deviceId", device.getDeviceid());   
		        profileJson.put("deviceName", device.getDevicename());   
		        profileJson.put("distance", "00");   
		        profileJson.put("hourMeter",String.format("%02d:%02d", hours.intValue(), minutesPart.intValue()));
		        arry.put(profileJson);
		  }
			    return arry.toString();
				 } 
		  
		  @RequestMapping(value = { "/api/deviceStatusDetailsWithAlerts/{user_id}/{key}" }, produces = { "application/json" })
			public String getDeviceLiveAlermData(@PathVariable long user_id, @PathVariable String key) throws ParseException, IOException {
			    Apikey api = this.apiService.findBykeyValue(key);
			   /* if (api == null) {
			        return "Invalid Key";
			    }*/
			    JSONArray resultArray = new JSONArray();
			    List<Object[]> profileList = DpService.Assigndeviceprofilebyuid(user_id);
			 
			    for (Object[] profile : profileList) {
			        Long profileId = Long.parseLong(profile[1].toString());
			        String profileName = profile[0].toString();
			        
			        String[] parameterList = DpService.getparameterIdByProfile(profileId);
			        String[] parameterNameList = DpService.getparameterNameByProfile(profileId);
			        
			        if (parameterList == null || parameterList.length == 0) {
			            JSONObject dataJson = new JSONObject();
			            dataJson.put("profileId", profileId.toString());
			            dataJson.put("profileName", profileName);
			            dataJson.put("status", "No parameters found for this profile");
			            resultArray.put(dataJson);
			            continue;
			        }
			        
			        List<String> failureCodes = Arrays.asList(parameterList);
			        List<String> digitalFields = failureCodes;
			        
			        List<String> parameterNames;
			        if (parameterNameList != null && parameterNameList.length == parameterList.length) {
			            parameterNames = Arrays.asList(parameterNameList);
			        } else {
			            parameterNames = failureCodes;
			        }
			        
			       // List<Object[]> allDetails = deviceQueryService.getDynamicDeviceStatusDetails(user_id, profileId, failureCodes, digitalFields);
			        List<Object[]> allDetails = deviceQueryService.getDynamicDeviceStatusDetails(user_id, profileId, failureCodes, digitalFields);
			        System.out.println(allDetails);
			           
			        JSONObject dataJson = new JSONObject();
			        dataJson.put("profileId", profileId.toString());
			        dataJson.put("profileName", profileName);
			        
			       Map<String, Set<String>> deviceAlertMap = new HashMap<>();
			        for (String paramName : parameterNames) {
			            deviceAlertMap.put(paramName, new HashSet<>());  
			        }
			        
			        if (allDetails != null && !allDetails.isEmpty()) {
			            Object[] lastDevice = allDetails.get(0);
			            
			            dataJson.put("deviceName", lastDevice[1] != null ? lastDevice[1].toString() : "N/A");
			            dataJson.put("altDeviceName", lastDevice[2] != null ? lastDevice[2].toString() : "N/A");
			            dataJson.put("deviceDate", lastDevice[3] != null ? lastDevice[3].toString() : "N/A");
			             for (Object[] detail : allDetails) {
			                if (detail == null || detail.length < 4) continue;
			                
			                String deviceId = detail[0] != null ? detail[0].toString() : "";
			                if (deviceId.isEmpty()) continue;
			                
			                for (int i = 0; i < parameterNames.size() && i < failureCodes.size(); i++) {
			                    int detailIndex = i + 4; 
			                    if (detail.length > detailIndex && detail[detailIndex] != null) {
			                        String digitalValue = detail[detailIndex].toString().trim();
			                        
			                         if ("0".equals(digitalValue)) {
			                            String paramName = parameterNames.get(i);
			                            deviceAlertMap.get(paramName).add(deviceId);
			                        }
			                    }
			                }
			            }
			            
			            for (String paramName : parameterNames) {
			                int alertCount = deviceAlertMap.get(paramName).size();
			                dataJson.put(paramName + "_CONT", alertCount);
			            }
			        } else {
			            dataJson.put("deviceName", "N/A");
			            dataJson.put("altDeviceName", "N/A");
			            dataJson.put("deviceDate", "N/A");
			            
			           for (String paramName : parameterNames) {
			                dataJson.put(paramName + "_CONT", 0);
			            }
			        }
			        
			        resultArray.put(dataJson);
			    }
			    
			    return resultArray.toString();
			}
		  
		  @RequestMapping(value = { "/api/GetRunningHours/{device_id}/{key}" }, produces = {"application/json" })
			public String GetRunningHours(@PathVariable long device_id,@PathVariable String key) throws ParseException, IOException {
			     Devicemaster device = DrRepo.findBydeviceid(device_id);
			     JSONArray arry = new JSONArray();
			    Date today = new Date(); 
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        String reportDate = sdf.format(today);
			     Devicemaster deviceObject = this.DrRepo.findBydeviceid(device_id);
			     HashMap<String, List<Object[]>> data = new LinkedHashMap<>();
			        	List<Object[]> Mains_Fail=new ArrayList<Object[]>();
			        	List<Object> ALlDigitalList=new ArrayList<Object>();
						final List<Object[]> DataLst = (List<Object[]>)ihreportService.getInputHistoryReportdata(reportDate,
								reportDate,deviceObject.getDeviceid());
							for (int i = 0; i < DataLst.size(); ++i) {
								final Object[] obj = DataLst.get(i);
								Object[] Mains_FailObj= {obj[0].toString(),obj[1].toString()};
								Mains_Fail.add(Mains_FailObj);
								data.put("284945", Mains_Fail);
							}
			          if (ALlDigitalList != null) {
			        	  for( Map.Entry<String, List<Object[]>> mapData : data.entrySet()) {
								List<Object[]> lst=(List<Object[]>) mapData.getValue();
								final JSONArray mainJSON = new JSONArray();
			            String lstatus = null;
			            if (lst != null) {
			            int ct = 0;
			            for (int i = 0; i < lst.size(); i++) {
			              Object[] obj = lst.get(i);
			              if (i == 0) {
			                lstatus = obj[1].toString();
			                JSONObject jo = new JSONObject();
			                jo.put("Start Date", obj[0].toString());
			                jo.put("Start Status", obj[1].toString());
			                mainJSON.put(ct, jo);
			                ct++;
			              } else {
			                if (!lstatus.equalsIgnoreCase(obj[1].toString())) {
			                  JSONObject innerJSON12 = mainJSON.getJSONObject(ct - 1);
			                  innerJSON12.put("End Date", obj[0].toString());
			                  mainJSON.put(ct - 1, innerJSON12);
			                  JSONObject jo2 = new JSONObject();
			                  jo2.put("Start Date", obj[0].toString());
			                  jo2.put("Start Status", obj[1].toString());
			                  mainJSON.put(ct, jo2);
			                  ct++;
			                } 
			                lstatus = obj[1].toString();
			              } 
			            } 
			          
			          for (int j = 0; j < mainJSON.length(); j++) {
			            JSONObject jsonObject1 = mainJSON.getJSONObject(j);
			            jsonObject1.put("Parameter", mapData.getKey());
			            if (j != mainJSON.length() - 1) {
			              Date JSONStartDate = df.parse(jsonObject1.getString("Start Date"));
			              Date JSONendDate = df.parse(jsonObject1.getString("End Date"));
			              JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate);
			              if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
			                jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
			                jsonObject1.put("Open", "");
			              } else {
			                jsonObject1.put("Close", "");
			                jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate, JSONendDate));
			              } 
			            } else {
			              JSONObject jsonObjectLast = mainJSON.getJSONObject(j);
			              Date JSONStartDate2 = df.parse(jsonObjectLast.getString("Start Date"));
			              Date date21 = df1.parse(reportDate.toString());
			              Date date22 = df1.parse(df1.format(new Date()));
			              int date23 = date21.compareTo(date22);
			              if (date23 == 0) {
			                jsonObjectLast.put("End Date", df.format(new Date()));
			                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {//1=Close,0=Open
			                  jsonObject1.put("Close",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
			                  jsonObject1.put("Open", "");
			                } else {
			                  jsonObject1.put("Close", "");
			                  jsonObject1.put("Open",JsonUtills.getTimeDiffrence(JSONStartDate2, new Date()));
			                } 
			              } else if (date23 == -1) {
			                jsonObjectLast.put("End Date", df
			                    .format(df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")));
			                if (jsonObject1.getString("Start Status").toString().equalsIgnoreCase("1")) {
			                  jsonObject1.put("Close", JsonUtills.getTimeDiffrence(JSONStartDate2, 
			                        df.parse(df.format(
			                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
			                  jsonObject1.put("Open", "");
			                } else {
			                  jsonObject1.put("Close", "");
			                  jsonObject1.put("Open", JsonUtills.getTimeDiffrence(JSONStartDate2, 
			                        df.parse(df.format(
			                            df.parse(String.valueOf(String.valueOf(reportDate.toString())) + " 23:59:59")))));
			                } 
			              } 
			            } 
			            
			            jsonObject1.put("DeviceName", deviceObject.getDevicename());
			            jsonObject1.put("deviceDescription", deviceObject.getDevicedescription());
			          /*  DataamainJSON.put(jsonObject1);
			            dataJson.put("data", DataamainJSON);*/
			          } 
			        	  
				        }
			        } 
			          }
			    	
			    return arry.toString();
				 } 
		    
		  
		  
		  @RequestMapping(
				    value = "/api/getAlertsByType/{fromDate}/{toDate}/{managerId}/{profileId}",method = RequestMethod.GET, produces = "application/json")
				public ResponseEntity<?> getAlertsByType(
				    @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
				    @PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
				    @PathVariable("managerId") Long managerId,
				    @PathVariable("profileId") Long profileId) {

				    AlertTypeDTO response = dashboardService.getAlertData(fromDate, toDate, managerId, profileId);
				    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
				}
		  
		  @RequestMapping(
			        value = "/api/getAlertsByTypeLive/{managerId}/{profileId}",
			        method = RequestMethod.GET,
			        produces = "application/json"
			    )
			    public ResponseEntity<?> getAlertsByTypeLive(
			        @PathVariable("managerId") Long managerId,
			        @PathVariable("profileId") Long profileId) {
			        AlertTypeDTOLive response = dashboardService.getAlertDataLive(managerId, profileId);
			        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
			    }
		  
		  
		  /*@RequestMapping(value = "/api/alertByZoneLive/{user_id}/{siteId}/{profileId}/{key}", produces = "application/json")
		  public String alertByZone(@PathVariable long user_id, @PathVariable long siteId, @PathVariable long profileId, @PathVariable String key)
		          throws ParseException, IOException {
		      Apikey api = apiService.findBykeyValue(key);
		      String profileName = DpService.getprofilenameByID(profileId);
		      JSONArray resultArray = new JSONArray();
		      
		      List<String> failureCodes = Arrays.asList(DpService.getparameterIdByProfile(profileId));
		      List<String> digitalFields = failureCodes;
		      String[] parameterNameList = DpService.getparameterNameByProfile(profileId);
		      
		      Map<String, String> parameterIdToNameMap = new HashMap<>();
		      for (int i = 0; i < failureCodes.size() && i < parameterNameList.length; i++) {
		          parameterIdToNameMap.put(failureCodes.get(i), parameterNameList[i]);
		      }
		      
		      JSONObject dataJson = new JSONObject();
		      dataJson.put("profileId", profileId);
		      dataJson.put("profileName", profileName);
		      
		      List<Object[]> allDetails = deviceQueryService.getAllDeviceStatusBySite(user_id, profileId, siteId, failureCodes, digitalFields);
		      
		      System.out.println("Device details count: " + (allDetails != null ? allDetails.size() : 0));
		      
		      Map<String, Integer> failCountMap = new HashMap<>();
		      for (String field : digitalFields) {
		          String paramName = parameterIdToNameMap.get(field);
		          if (paramName != null) {
		              failCountMap.put(paramName, 0);
		          } else {
		              failCountMap.put(field, 0);
		          }
		      }
		      
		      if (allDetails != null && !allDetails.isEmpty()) {
		          for (Object[] detail : allDetails) {
		              dataJson.put("deviceName", detail[1]);
		              dataJson.put("altDeviceName", detail[2]);
		              dataJson.put("deviceDate", detail[3]);
		              for (int i = 0; i < digitalFields.size(); i++) {
		                  String fieldId = digitalFields.get(i);
		                  String fieldName = parameterIdToNameMap.get(fieldId);
		                  if (fieldName == null) {
		                      fieldName = fieldId; 
		                  }
		                  String value = (String) detail[4 + i];
		                  System.out.println("Field: " + fieldName + ", Value: " + value);
		                  if ("0".equalsIgnoreCase(value)) {
		                      failCountMap.put(fieldName, failCountMap.get(fieldName) + 1);
		                  }
		              }
		          }
		      }
		      System.out.println("Failure counts: " + failCountMap);
		      
		      failCountMap.forEach(dataJson::put);
		      resultArray.put(dataJson);
		      
		      return resultArray.toString();
		  }*/
		  
		// new
			@GetMapping(path = "/api/alertByZoneLive/{managerId}/{profileId}/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<String> alertByZoneLive(@PathVariable("managerId") long managerId,
					@PathVariable("profileId") long profileId, @PathVariable("key") String key)
					throws ParseException, IOException {

			/*	if (apiService.findBykeyValue(key) == null) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key");
				}*/

				String[] paramIds = DpService.getparameterIdByProfile(profileId);
				String[] paramNames = DpService.getparameterNameByProfile(profileId);

				String json = deviceQueryService.getAlertCountsFromLasttrack(managerId,profileId, paramIds, paramNames);

				return ResponseEntity.ok(json);
			}

			@RequestMapping(value = "/api/alertByZone/{managerId}/{profileId}/{fromDate}/{toDate}/{key}", produces = "application/json")
			public String alertByZoneLive(@PathVariable long managerId, @PathVariable long profileId, @PathVariable String key,
					@PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
					@PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate

			) throws IOException {

				Apikey api = apiService.findBykeyValue(key);
				/*if (api == null) {
					return "Invalid Key";
				}*/

				String[] parameterNameList = DpService.getparameterNameByProfile(profileId);

				return deviceQueryService.getDeviceAlertFromAlertMsgHistory(managerId,profileId, parameterNameList, fromDate, toDate);
			}
			
			 @RequestMapping(value = {"/api/GetTravelDistance/{deviceid}/{startdate}/{enddate}{key}"}, produces = {"application/json"})
			  public double  GetTravelDistance(@PathVariable long deviceid,@PathVariable String startdate,@PathVariable String enddate,@PathVariable String key) throws JsonGenerationException, JsonMappingException, IOException, ParseException {
				  double totalDistance = 0.0;
				 List<Object[]> results = hstServide.getadminHistoryLocation(deviceid,startdate,enddate,2000L);
				 // Variable to accumulate the total distance
			        //double totalDistance = 0.0;

			        // Variable to keep track of the previous row for distance calculation
			        Object[] previousRow = null;

			        // Iterate through each result and calculate the distance
			        for (Object[] row : results) {
			            double latitude = Double.parseDouble((String) row[2]);
			            double longitude = Double.parseDouble((String) row[3]);

			            // If there's a previous row, calculate the distance
			            if (previousRow != null) {
			                double prevLatitude = Double.parseDouble((String) previousRow[2]);
			                double prevLongitude = Double.parseDouble((String) previousRow[3]);

			                // Calculate the distance between the current and previous coordinates
			                double distance = distance_Haversine(prevLatitude, prevLongitude, latitude, longitude);

			                // Add the distance to the total distance
			                totalDistance += distance;
			            }

			            // Update the previous row
			            previousRow = row;
			        }

			        // Return the total distance
			        return totalDistance;
			  }
			 public double distance_Haversine(double lat1, double lon1, double lat2, double lon2) {
					double deltaLat = Math.toRadians(lat2 - lat1);
					double deltaLon = Math.toRadians(lon2 - lon1);
					lat1 = Math.toRadians(lat1);
					lat2 = Math.toRadians(lat2);

					double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
							+ Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
					double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

					return 6371 * c; // Earth radius in km
				}
				    // Method to calculate distance between two points using the Haversine formula
				    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
				        final int R = 6371; // Radius of the Earth in kilometers

				        // Convert latitude and longitude from degrees to radians
				        double lat1Rad = Math.toRadians(lat1);
				        double lon1Rad = Math.toRadians(lon1);
				        double lat2Rad = Math.toRadians(lat2);
				        double lon2Rad = Math.toRadians(lon2);

				        // Differences in coordinates
				        double dLat = lat2Rad - lat1Rad;
				        double dLon = lon2Rad - lon1Rad;

				        // Haversine formula
				        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
				                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
				        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

				        // Distance in kilometers
				        return R * c;
				    }
}
